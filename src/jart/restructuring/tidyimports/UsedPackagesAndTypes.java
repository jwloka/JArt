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
package jart.restructuring.tidyimports;

import jast.ast.ASTHelper;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;

import java.util.Enumeration;
import java.util.Hashtable;

public class UsedPackagesAndTypes
{
    private CompilationUnit      _unit;
    private String               _curPackage;
    private Hashtable            _packages             = new Hashtable();
    private TypeDeclarationArray _localTypes           = new TypeDeclarationArrayImpl();
    private Hashtable            _shortNames           = new Hashtable();
    private Hashtable            _duplicatedShortNames = new Hashtable();
    private Hashtable            _ambiguousShortNames  = new Hashtable();
    private StringArray          _directImports        = new StringArray();
    private StringArray          _onDemandImports      = new StringArray();
    private int                  _threshold;

    public UsedPackagesAndTypes(CompilationUnit unit, int onDemandImportThreshold)
    {
        TypeGatherer typeGatherer = new TypeGatherer(this);

        unit.accept(typeGatherer);

        _unit       = unit;
        _curPackage = _unit.getPackage().getQualifiedName();
        _threshold  = onDemandImportThreshold;

        classifyImports();
    }

    private void addDirectImport(String pckg, String type)
    {
        if ("".equals(pckg))
        {
            _directImports.add(type);
        }
        else
        {
            _directImports.add(pckg+"."+type);
        }
    }

    private void addInheritedInnerTypes(TypeDeclaration baseType)
    {
        if (baseType == null)
        {
            return;
        }

        boolean         inSamePckg = baseType.getPackage().isEqualTo(_unit.getPackage());
        TypeDeclaration innerType;
        Modifiers       mods;

        for (TypeDeclarationIterator it = baseType.getInnerTypes().getIterator(); it.hasNext();)
        {
            innerType  = it.getNext();
            mods       = innerType.getModifiers();
            if (mods.isPrivate() ||
                mods.isProtected() ||
                (inSamePckg && mods.isFriendly()))
            {
                if (!_localTypes.contains(innerType.getName()))
                {
                    _localTypes.add(innerType);
                }
                addInheritedInnerTypes(innerType);
            }
        }
        if (baseType instanceof ClassDeclaration)
        {
            ClassDeclaration baseClass = (ClassDeclaration)baseType;

            if (baseClass.hasBaseClass())
            {
                addInheritedInnerTypes(baseClass.getBaseClass().getReferenceBaseTypeDecl());
            }
        }
        for (TypeIterator it = baseType.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            addInheritedInnerTypes(it.getNext().getReferenceBaseTypeDecl());
        }
    }

    private void addLocalTypes(TypeDeclaration curType)
    {
        _localTypes.add(curType);

        if (curType instanceof ClassDeclaration)
        {
            ClassDeclaration curClass = (ClassDeclaration)curType;

            if (curClass.hasBaseClass())
            {
                addInheritedInnerTypes(curClass.getBaseClass().getReferenceBaseTypeDecl());
            }
        }
        for (TypeIterator it = curType.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            addInheritedInnerTypes(it.getNext().getReferenceBaseTypeDecl());
        }
        for (TypeDeclarationIterator it = curType.getInnerTypes().getIterator(); it.hasNext();)
        {
            addLocalTypes(it.getNext());
        }
    }

    private void addOnDemandImport(String pckg, boolean implicit)
    {
        Project prj      = ASTHelper.getProjectOf(_unit);
        String  pckgPart = pckg;
        String  shortName;

        if (pckgPart.length() > 0)
        {
            pckgPart += ".";
        }
        for (Enumeration en = _shortNames.elements(); en.hasMoreElements();)
        {
            shortName = (String)en.nextElement();
            if (ASTHelper.existsType(prj, pckgPart + shortName))
            {
                _ambiguousShortNames.put(shortName, pckg);
            }
        }
        if (!implicit)
        {
            _onDemandImports.add(pckg);
        }
    }

    public void addType(Type type)
    {
        if (type.isReference())
        {
            addType(type.getBaseType().getReferenceBaseTypeDecl());
        }
    }

    public void addType(TypeDeclaration typeDecl)
    {
        if ((typeDecl == null) || (typeDecl.getName().indexOf('[') >= 0))
        {
            return;
        }
        if ((typeDecl instanceof ClassDeclaration) &&
            ((ClassDeclaration)typeDecl).isLocal())
        {
            return;
        }

        String pckgName = ASTHelper.getCompilationUnitOf(typeDecl).getPackage().getQualifiedName();
        String typeName = typeDecl.getQualifiedName();

        if (pckgName.length() > 0)
        {
            typeName = typeName.substring(pckgName.length() + 1);
        }
        // we're only interested in the top-level type
        if (typeName.indexOf('.') >= 0)
        {
            typeName = typeName.substring(0, typeName.indexOf('.'));
        }

        StringArray types = (StringArray)_packages.get(pckgName);

        if (types == null)
        {
            types = new StringArray();
            _packages.put(pckgName, types);
        }
        if (!types.contains(typeName))
        {
            types.add(typeName);
        }
    }

    private void classifyImports()
    {
        _localTypes.clear();
        _duplicatedShortNames.clear();
        _directImports.clear();
        _onDemandImports.clear();

        determineLocalTypes();
        classifyShortNames();

        addOnDemandImport("java.lang", true);
        addOnDemandImport(_curPackage, true);

        determineNecessaryDirectImports();
        determineOtherImports();
    }

    private void classifyShortNames()
    {
        Hashtable   firstUsages = new Hashtable();
        StringArray pckgs;
        StringArray types;
        String      pckg;
        String      type;

        for (Enumeration en = _packages.keys(); en.hasMoreElements();)
        {
            pckg  = (String)en.nextElement();
            types = (StringArray)_packages.get(pckg);
            for (StringIterator it = types.getIterator(); it.hasNext();)
            {
                type = it.getNext();
                _shortNames.put(type, type);
                if (firstUsages.containsKey(type))
                {
                    // short name already used -> duplication
                    pckgs = (StringArray)_duplicatedShortNames.get(type);
                    if (pckgs == null)
                    {
                        // this is the second usage -> we have to create the duplication list
                        pckgs = new StringArray();
                        _duplicatedShortNames.put(type, pckgs);
                        pckgs.add((String)firstUsages.get(type));
                    }
                    pckgs.add(pckg);
                }
                else
                {
                    // first usage; we store the package name for later use
                    firstUsages.put(type, pckg);
                }
            }
        }
    }

    private void determineLocalTypes()
    {
        for (TypeDeclarationIterator it = _unit.getTypeDeclarations().getIterator(); it.hasNext();)
        {
            addLocalTypes(it.getNext());
        }
    }

        private void determineNecessaryDirectImports()
        {
            StringArray pckgs;
            String      type;
            String      pckg;
            String      importedPckg;
            boolean     samePckg;

    outer:    for (Enumeration en = _duplicatedShortNames.keys(); en.hasMoreElements();)
            {
                type = (String)en.nextElement();

                if (_localTypes.contains(type))
                {
                    // local or inherited type
                    continue;
                }

                pckgs        = (StringArray)_duplicatedShortNames.get(type);
                importedPckg = "";

                if ("".equals(_curPackage) || !pckgs.contains(""))
                {
                    for (StringIterator it = pckgs.getIterator(); it.hasNext();)
                    {
                        pckg = it.getNext();
                        if (_curPackage.equals(pckg))
                        {
                            // same package -> no need for an import
                            continue outer;
                        }
                        else if (importedPckg.length() < pckg.length())
                        {
                            importedPckg = pckg;
                        }
                    }
                }
                addDirectImport(importedPckg, type);
            }
        }

    private void determineOtherImports()
    {
        String pckg;

        for (Enumeration en = _packages.keys(); en.hasMoreElements();)
        {
            pckg = (String)en.nextElement();

            if ("java.lang".equals(pckg) || _curPackage.equals(pckg))
            {
                continue;
            }
            determineOtherImportsForPackage(pckg);
        }
    }

    private void determineOtherImportsForPackage(String pckg)
    {
        StringArray types = (StringArray)_packages.get(pckg);
        String      type;

        if ("".equals(pckg))
        {
            types.sort();
            for (StringIterator it = types.getIterator(); it.hasNext();)
            {
                type = it.getNext();
                if (!_duplicatedShortNames.containsKey(type))
                {
                    addDirectImport(pckg, type);
                }
            }
        }
        else
        {
            StringArray directCandidates   = new StringArray();
            StringArray onDemandCandidates = new StringArray();
            String      otherPckg;

            for (StringIterator it = types.getIterator(); it.hasNext();)
            {
                type = it.getNext();
                if (!_duplicatedShortNames.containsKey(type) &&
                    !_localTypes.contains(type))
                {
                    otherPckg = (String)_ambiguousShortNames.get(type);
                    if ((otherPckg != null) &&
                        !pckg.equals(otherPckg))
                    {
                        if (!_curPackage.equals(otherPckg))
                        {
                            directCandidates.add(type);
                        }
                    }
                    else
                    {
                        onDemandCandidates.add(type);
                    }
                }
            }
            if (onDemandCandidates.getCount() >= _threshold)
            {
                addOnDemandImport(pckg, false);
            }
            else
            {
                for (StringIterator it = onDemandCandidates.getIterator(); it.hasNext();)
                {
                    directCandidates.add(it.getNext());
                }
            }
            directCandidates.sort();
            for (StringIterator it = directCandidates.getIterator(); it.hasNext();)
            {
                addDirectImport(pckg, it.getNext());
            }
        }
    }

    public StringArray getDirectImports()
    {
        return _directImports;
    }

    public StringArray getOnDemandImports()
    {
        return _onDemandImports;
    }
}
