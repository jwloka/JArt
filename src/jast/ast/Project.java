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
package jast.ast;
import jast.Global;
import jast.ast.nodes.ArrayType;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.Package;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.CompilationUnitArrayImpl;
import jast.ast.nodes.collections.PackageArray;
import jast.ast.nodes.collections.PackageArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.visitor.Visitor;
import jast.helpers.StringArray;

public class Project extends Node
{
    private String               _name;
    private PackageArray         _packages         = new PackageArrayImpl();
    private CompilationUnitArray _compilationUnits = new CompilationUnitArrayImpl();
    private ArrayType            _arrayType        = createArrayType();

    public Project(String name)
    {
        _name = name;
        _compilationUnits.setOwner(this);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitProject(this);
    }

    // Adds the given unit to this project
    // If there is already a unit of the same name, then it will
    // be replaced and all references to it will be invalidated
    // (the concerned units will need re-resolving)
    public void addCompilationUnit(CompilationUnit unit, String pckgName)
    {
        // we first remove a old unit of that name if present and
        // invalidate any references to it
        delCompilationUnit(unit.getName());

        // then we add the new one
        getCompilationUnits().add(unit);

        Package pckg = ensurePackage(pckgName);

        unit.setPackage(pckg);
        for (TypeDeclarationIterator it = unit.getTypeDeclarations().getIterator(); it.hasNext();)
        {
            pckg.addType(it.getNext());
        }

        unit.setContainer(this);
    }

    private ArrayType createArrayType()
    {
        CompilationUnit arrayTypeUnit = new CompilationUnit("[]");
        ArrayType       result        = new ArrayType();

        arrayTypeUnit.setPackage(ensurePackage("java.lang"));
        arrayTypeUnit.setComplete(false);
        arrayTypeUnit.setResolvingStatus(false);
        arrayTypeUnit.setModifiableStatus(false);
        arrayTypeUnit.setProjectUnitStatus(false);
        arrayTypeUnit.getTypeDeclarations().add(result);
        addCompilationUnit(arrayTypeUnit, "java.lang");

        return result;
    }

    // Removes the compilation unit with the given name (if any)
    public void delCompilationUnit(String unitName)
    {
        CompilationUnit unit = getCompilationUnits().get(unitName);

        if (unit == null)
        {
            // none there
            return;
        }
        new InvalidationVisitor().invalidate(unit);
        getCompilationUnits().remove(unitName);
    }

    public Package ensurePackage(String qualifiedName)
    {
        Package result = _packages.get(qualifiedName);

        if (result == null)
        {
            result = Global.getFactory().createPackage(qualifiedName);
            result.setContainer(this);
            _packages.add(result);
        }
        return result;
    }

    public ArrayType getArrayType()
    {
        return _arrayType;
    }

    // Helper method to retreive a compilation unit by its
    // qualified name (as opposed to its source name)
    public CompilationUnit getCompilationUnit(String qualifiedName)
    {
        int    pos      = qualifiedName.lastIndexOf('.');
        String pckgName = (pos > 0 ? qualifiedName.substring(0, pos) : "");
        String typeName = (pos > 0 ? qualifiedName.substring(pos+1) : qualifiedName);

        if (_packages.contains(pckgName))
        {
            TypeDeclaration type = _packages.get(pckgName).getTypes().get(typeName);

            return (type == null ? null : (CompilationUnit)type.getContainer());
        }
        else
        {
            return null;
        }
    }

    public CompilationUnitArray getCompilationUnits()
    {
        return _compilationUnits;
    }

    public String getName()
    {
        return _name;
    }

    public Package getPackage(String qualifiedName)
    {
        return _packages.get(qualifiedName);
    }

    public PackageArray getPackages()
    {
        return _packages;
    }

    // Helper method which tries to find the type with the given name
    // Note that inner/nested types are found as well
    public TypeDeclaration getType(String qualifiedName)
    {
        Package         pckg   = null;
        TypeDeclaration result = null;
        StringArray     parts  = StringArray.fromString(qualifiedName, ".");

        for (int idx = 0; idx < parts.getCount(); idx++)
        {
            // already found top-level type ?
            if (result == null)
            {
                // no, so we use the parts before the current as the
                // package identifier
                if (idx == 0)
                {
                    // default package
                    pckg = getPackage("");
                }
                else
                {
                    pckg = getPackage(parts.asString(".", idx));
                }
                if (pckg != null)
                {
                    // if there is a package of this name,
                    // we check whether it contains a type with
                    // the current part as the name
                    result = pckg.getTypes().get(parts.get(idx));
                }
            }
            else
            {
                // yes, so check for an inner type
                result = result.getInnerTypes().get(parts.get(idx));
                if (result == null)
                {
                    // no inner type of that name
                    // however it cannot be a package name,
                    // which means there is no type of that name at all
                    break;
                }
            }
        }
        return result;
    }
}
