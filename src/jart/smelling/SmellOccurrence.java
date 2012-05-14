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
package jart.smelling;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.ParsePosition;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.TypeDeclaration;

public class SmellOccurrence
{
    private Class         _smell;
    private Node          _node;
    private ParsePosition _pos;

    public SmellOccurrence(Class smell, Node node, ParsePosition pos)
    {
        _smell = smell;
        _node = node;
        _pos = pos;
    }

    public Class getSmell()
    {
        return _smell;
    }

    public String getSmellName()
    {
        String fullName = _smell.getName();

        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }

    public Node getNode()
    {
        return _node;
    }

    public CompilationUnit getEnclosingUnit()
    {
        return ASTHelper.getCompilationUnitOf((ContainedNode) _node);
    }

    public TypeDeclaration getEnclosingType()
    {
        return ASTHelper.getTypeDeclarationOf((ContainedNode) _node);
    }

    public ParsePosition getPosition()
    {
        return _pos;
    }
}
