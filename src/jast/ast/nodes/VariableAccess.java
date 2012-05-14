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

import jast.ast.visitor.Visitor;

public class VariableAccess extends Primary
{
    private LocalVariableDeclaration _decl;

    public VariableAccess(LocalVariableDeclaration decl)
    {
        _decl = decl;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitVariableAccess(this);
    }

    public Type getType()
    {
        return (_decl != null ? _decl.getType() : null);
    }

    public LocalVariableDeclaration getVariableDeclaration()
    {
        return _decl;
    }

    public String getVariableName()
    {
        return _decl.getName();
    }

    public void setVariableDeclaration(LocalVariableDeclaration decl)
    {
        _decl = decl;
    }
}
