/*
 * Copyright (C) 2002 Thomas Dudziak, Jan Wloka
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package jast.analysis;

import jast.ast.ASTHelper;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodArray;
import jast.ast.nodes.collections.MethodArrayImpl;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.QualifiedTypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;

import java.util.Enumeration;
import java.util.Hashtable;

public class InheritanceAnalyzer
{
    private TypeDeclaration      _localType;
    private TypeDeclarationArray _ancestors;

    public InheritanceAnalyzer()
    {
        super();
        _ancestors = new QualifiedTypeDeclarationArrayImpl();
    }

    public InheritanceAnalyzer(TypeDeclaration localType) throws IllegalArgumentException
    {
        this();

        if (localType == null)
        {
            throw new IllegalArgumentException("null is not allowed");
        }

        _localType = localType;
    }

    private void addAncestors(ClassDeclaration clsDecl)
    {
        Type            curAncestor = clsDecl.getBaseClass();
        TypeDeclaration curAncestorDecl;

        while ((curAncestor != null) &&
               (curAncestor.hasReferenceBaseTypeDecl()))
        {
            curAncestorDecl = curAncestor.getReferenceBaseTypeDecl();
            addPossibleBaseInterfaces(curAncestorDecl);

            if (!curAncestor.getQualifiedName().equals("java.lang.Object"))
            {
                _ancestors.add(curAncestorDecl);
            }
            curAncestor = ((ClassDeclaration) curAncestorDecl).getBaseClass();
        }
    }

    private void addAncestors(InterfaceDeclaration interfaceDecl)
    {
        if ((interfaceDecl != null) && interfaceDecl.hasBaseInterfaces())
        {
            TypeDeclaration curAncestorDecl;

            for (TypeIterator iter = interfaceDecl.getBaseInterfaces().getIterator(); iter.hasNext();)
            {
                curAncestorDecl = iter.getNext().getReferenceBaseTypeDecl();
                if (curAncestorDecl != null)
                {
                    _ancestors.add(curAncestorDecl);

                    // recursive invocation
                    addAncestors((InterfaceDeclaration)curAncestorDecl);
                }
            }
        }
    }

    private void addPossibleBaseInterfaces(TypeDeclaration typeDecl)
    {
        if (typeDecl.hasBaseInterfaces())
        {
            TypeDeclaration curAncestorDecl;

            for (TypeIterator iter = typeDecl.getBaseInterfaces().getIterator(); iter.hasNext();)
            {
                curAncestorDecl = iter.getNext().getReferenceBaseTypeDecl();
                if (curAncestorDecl != null)
                {
                    _ancestors.add(curAncestorDecl);
                    addAncestors((InterfaceDeclaration)curAncestorDecl);
                }
            }
        }
    }

    public static boolean areRelated(TypeDeclaration typeA, TypeDeclaration typeB)
    {
        return new InheritanceAnalyzer(typeA).isParent(typeB) ||
               new InheritanceAnalyzer(typeB).isParent(typeA);
    }

    public static boolean areUnRelated(TypeDeclaration typeA, TypeDeclaration typeB)
    {
        if ((typeA == null) || (typeB == null))
        {
            return false;
        }

        // same type
        if (typeA.equals(typeB))
        {
            return false;
        }

        // both are defined in same compilation unit
        if (ASTHelper.isInSameCompilationUnit(typeA, typeB))
        {
            return false;
        }

        // typeA is child of typeB or vice versa
        if (InheritanceAnalyzer.areRelated(typeA, typeB))
        {
            return false;
        }

        return true;
    }

    public TypeDeclarationArray getAncestors()
    {
        if ((_ancestors != null) && !_ancestors.isEmpty())
        {
            return _ancestors;
        }

        _ancestors = new QualifiedTypeDeclarationArrayImpl();

        if (_localType == null)
        {
            return _ancestors;
        }

        if (_localType instanceof ClassDeclaration)
        {
            addAncestors((ClassDeclaration)_localType);
        }

        addPossibleBaseInterfaces(_localType);

        return _ancestors;
    }

    public TypeDeclarationArray getChildren(boolean onlyDirect)
    {
        TypeDeclarationArray result        = new TypeDeclarationArrayImpl();
        Project              project       = ASTHelper.getProjectOf(_localType);
        Hashtable            knownSubTypes = new Hashtable();
        Hashtable            finishedTypes = new Hashtable();

        for (PackageIterator pkgIt = project.getPackages().getIterator(); pkgIt.hasNext();)
        {
            for (TypeDeclarationIterator typeIt = pkgIt.getNext().getTypes().getIterator(); typeIt.hasNext();)
            {
                checkForSubType(typeIt.getNext(),
                                knownSubTypes,
                                finishedTypes,
                                !onlyDirect);
            }
        }
        for (Enumeration en = knownSubTypes.elements(); en.hasMoreElements();)
        {
            result.add((TypeDeclaration)en.nextElement());
        }
        return result;
    }

    public FieldArray getInheritedFields()
    {
        FieldArray       result = new FieldArrayImpl();
        FieldDeclaration curField;

        for (TypeDeclarationIterator typeIter = getAncestors().getIterator(); typeIter.hasNext();)
        {
            for (FieldIterator fieldIter = typeIter.getNext().getFields().getIterator(); fieldIter.hasNext(); )
            {
                curField = fieldIter.getNext();
                if (isVisible(curField) && !isRedefined(curField))
                {
                    result.add(curField);
                }
            }
        }

        return result;
    }

    public MethodArray getInheritedMethods()
    {
        MethodArray       result = new MethodArrayImpl();
        MethodDeclaration curMeth;

        for (TypeDeclarationIterator typeIter = getAncestors().getIterator(); typeIter.hasNext();)
        {
            for (MethodIterator methIter = typeIter.getNext().getMethods().getIterator(); methIter.hasNext();)
            {
                curMeth = methIter.getNext();
                if (isVisible(curMeth) && !isRedefined(curMeth))
                {
                    result.add(curMeth);
                }
            }
        }

        return result;
    }

    public TypeDeclaration getLocalType()
    {
        return _localType;
    }

    public boolean hasChildrenIn(Project prj)
    {
        for (PackageIterator pkgIter = prj.getPackages().getIterator(); pkgIter.hasNext();)
        {
            for (TypeDeclarationIterator typeIter = pkgIter.getNext().getTypes().getIterator(); typeIter.hasNext();)
            {
                if (isChild(typeIter.getNext()))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /** Returns true if type is an indirect or direct child. */
    public boolean isChild(TypeDeclaration type)
    {
        return new InheritanceAnalyzer(type).getAncestors().contains(getLocalType());
    }

    /** Returns true if pkg is the package where the type is defined */
    private boolean isLocal(jast.ast.nodes.Package pkg)
    {
        return _localType.getPackage().equals(pkg);
    }

    /** Returns true if all features of type are visible
     *  (particulary private features).
     */
    public boolean isLocal(TypeDeclaration type)
    {
        return getLocalType().equals(type) ||
               ASTHelper.isDefinedInSameType(getLocalType(), type);
    }

    /** Retruns true if type is an indirect or direct parent */
    public boolean isParent(TypeDeclaration type)
    {
        return getAncestors().contains(type);
    }

    private boolean isRedefined(FieldDeclaration field)
    {
        return _localType.getFields().contains(field.getName());
    }

    private boolean isRedefined(MethodDeclaration method)
    {
        return _localType.getMethods().contains(method.getSignature());
    }

    /** Returns true the features of type are only via its public
     *  interface accessable (IOW type is defined in a different
     *  compilation unit and not a parent).
     */
    public boolean isRemote(TypeDeclaration type)
    {
        return (!isLocal(type) && !isParent(type));
    }

    private boolean isVisible(FieldDeclaration field)
    {
        boolean samePackage = isLocal(ASTHelper.getOutermostTypeDeclarationOf(field).getPackage());

        if (samePackage && !field.getModifiers().isPrivate())
        {
            return true;
        }
        if (field.getModifiers().isPublic() ||
            field.getModifiers().isProtected())
        {
            return true;
        }

        return false;
    }

    private boolean isVisible(MethodDeclaration method)
    {
        boolean samePackage = isLocal(ASTHelper.getOutermostTypeDeclarationOf(method).getPackage());

        if (samePackage && !method.getModifiers().isPrivate())
        {
            return true;
        }
        if (method.getModifiers().isPublic() ||
            method.getModifiers().isProtected())
        {
            return true;
        }

        return false;
    }

    public void setLocalType(TypeDeclaration type) throws IllegalArgumentException
    {
        if (type == null)
        {
            throw new IllegalArgumentException("null is not allowed");
        }

        _localType = type;
        _ancestors = new QualifiedTypeDeclarationArrayImpl();
    }


    private boolean checkForSubType(TypeDeclaration curTypeDecl,
                                    Hashtable       knownSubTypes,
                                    Hashtable       finishedTypes,
                                    boolean         descent)
    {
        if (curTypeDecl == null)
        {
            return false;
        }
        if (curTypeDecl == _localType)
        {
            return true;
        }
        if (knownSubTypes.containsKey(curTypeDecl))
        {
            return true;
        }
        if (finishedTypes.containsKey(curTypeDecl))
        {
            return false;
        }

        boolean result = false;

        if (curTypeDecl instanceof ClassDeclaration)
        {
            ClassDeclaration classDecl = (ClassDeclaration)curTypeDecl;

            if (classDecl.hasBaseClass())
            {
                if (descent)
                {
                    if (checkForSubType(classDecl.getBaseClass().getReferenceBaseTypeDecl(),
                                        knownSubTypes,
                                        finishedTypes,
                                        true))
                    {
                        knownSubTypes.put(curTypeDecl, curTypeDecl);
                        result = true;
                    }
                }
                else
                {
                    if (_localType == classDecl.getBaseClass().getReferenceBaseTypeDecl())
                    {
                        knownSubTypes.put(curTypeDecl, curTypeDecl);
                        result = true;
                    }
                }
            }
        }
        if (!result)
        {
            for (TypeIterator it = curTypeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                if (descent)
                {
                    if (checkForSubType(it.getNext().getReferenceBaseTypeDecl(),
                                        knownSubTypes,
                                        finishedTypes,
                                        true))
                    {
                        knownSubTypes.put(curTypeDecl, curTypeDecl);
                        result = true;
                    }
                }
                else
                {
                    if (_localType == it.getNext().getReferenceBaseTypeDecl())
                    {
                        knownSubTypes.put(curTypeDecl, curTypeDecl);
                        result = true;
                    }
                }
            }
        }
        finishedTypes.put(curTypeDecl, curTypeDecl);
        return result;
    }
}
