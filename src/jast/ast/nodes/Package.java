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
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.visitor.Visitor;

public class Package extends ContainedNode
{
    private String               _qualifiedName;
    private TypeDeclarationArray _types = new TypeDeclarationArrayImpl();

    public Package(String qualifiedName)
    {
        _qualifiedName = qualifiedName;
        // As types are contained within compilation units,
        // not packages, we cannot set an owner for the type
        // declaration array
    }

    public void accept(Visitor visitor)
    {
        visitor.visitPackage(this);
    }

    public void addType(TypeDeclaration typeDecl)
    {
        typeDecl.setPackage(this);
        _types.add(typeDecl);
    }

    public String getQualifiedName()
    {
        return _qualifiedName;
    }

    public TypeDeclarationArray getTypes()
    {
        return _types;
    }

    public boolean isDefault()
    {
        return (_qualifiedName == null) ||
               (_qualifiedName.length() == 0);
    }

    public boolean isEqualTo(Package other)
    {
        if (other == null)
        {
            return false;
        }
        if (_qualifiedName == null)
        {
            return (other._qualifiedName == null);
        }
        else
        {
            return _qualifiedName.equals(other._qualifiedName);
        }
    }
}
