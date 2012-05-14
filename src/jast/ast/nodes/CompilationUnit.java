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
package jast.ast.nodes;
import jast.ast.ContainedNode;
import jast.ast.nodes.collections.ImportArray;
import jast.ast.nodes.collections.ImportArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.visitor.Visitor;

public class CompilationUnit extends ContainedNode
{
    private String               _name;
    private Package              _package;
    private ImportArray          _imports = new ImportArrayImpl();
    private TypeDeclarationArray _types   = new TypeDeclarationArrayImpl();
    // states whether the unit is complete (e.g. all expressions etc. are present)
    private boolean              _isComplete;
    // states whether the unit can be modified (i.e. parsed from java source
    // file instead of from class file)
    private boolean              _canBeModified;
    // states whether the unit is "officially" part of the project; only these
    // units are relevant for smelling etc.
    private boolean              _isProjectUnit;
    // convenience flag to prevent unnecessary/circular resolving
    private boolean              _isResolved = false;

    public CompilationUnit(String name)
    {
        _name = name;
        _imports.setOwner(this);
        _types.setOwner(this);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitCompilationUnit(this);
    }

    public boolean canBeModified()
    {
        return _canBeModified;
    }

    public ImportArray getImportDeclarations()
    {
        return _imports;
    }

    // Returns the source name
    public String getName()
    {
        return _name;
    }

    public Package getPackage()
    {
        return _package;
    }

    // Returns the unqualified name of the first or public type in this unit
    // Note that it returns null if no type is in the unit
    public String getTopLevelName()
    {
        if ((_package == null) || _types.isEmpty())
        {
            return null;
        }

        TypeDeclaration type;
        String          typeName = null;

        for (TypeDeclarationIterator it = _types.getIterator(); it.hasNext();)
        {
            type = it.getNext();
            if (type.getModifiers().isPublic() || (typeName == null))
            {
                typeName = type.getName();
            }
        }
        return typeName;
    }

    // Returns the qualified name of the first or public type in this unit
    // Note that it returns null if no type is in the unit
    public String getTopLevelQualifiedName()
    {
        String name = getTopLevelName();

        if (name != null)
        {
            String pckgName = _package.getQualifiedName();

            if ((pckgName != null) && (pckgName.length() > 0))
            {
                name = pckgName + "." + name;
            }
        }
        return name;
    }

    public TypeDeclarationArray getTypeDeclarations()
    {
        return _types;
    }

    public boolean isComplete()
    {
        return _isComplete;
    }

    public boolean isResolved()
    {
        return _isResolved;
    }

    public boolean isProjectUnit()
    {
        return _isProjectUnit;
    }

    public void setComplete(boolean isComplete)
    {
        _isComplete = isComplete;
    }

    public void setModifiableStatus(boolean canBeModified)
    {
        _canBeModified = canBeModified;
    }

    public void setProjectUnitStatus(boolean isProjectUnit)
    {
        _isProjectUnit = isProjectUnit;
    }

    public void setPackage(Package pckg)
    {
        _package = pckg;
    }

    public void setResolvingStatus(boolean isResolved)
    {
        _isResolved = isResolved;
    }
}
