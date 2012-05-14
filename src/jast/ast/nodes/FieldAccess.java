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

public class FieldAccess extends TrailingPrimary
{
    private FieldDeclaration _decl;
    private String           _fieldName;

    public FieldAccess(Primary baseExpr, FieldDeclaration fieldDecl)
    {
        setBaseExpression(baseExpr);
        _fieldName = fieldDecl.getName();
        _decl      = fieldDecl;
    }

    public FieldAccess(Primary baseExpr, String fieldName)
    {
        setBaseExpression(baseExpr);
        _fieldName = fieldName;
    }

    public FieldAccess(String fieldName)
    {
        _fieldName = fieldName;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitFieldAccess(this);
    }

    public FieldDeclaration getFieldDeclaration()
    {
        return _decl;
    }

    public String getFieldName()
    {
        if (_decl != null)
        {
            return _decl.getName();
        }
        else
        {
            return _fieldName;
        }
    }

    public Type getType()
    {
        return (_decl != null ? _decl.getType() : null);
    }

    public void setFieldDeclaration(FieldDeclaration decl)
    {
        _decl = decl;
    }
}
