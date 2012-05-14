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

public class MethodDeclaration extends InvocableDeclaration
{
    private Type _returnType;

    public MethodDeclaration(Modifiers           mods,
                             Type                returnType,
                             String              name,
                             FormalParameterList params)
    {
        super(mods, name, params);
        _returnType = returnType;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitMethodDeclaration(this);
    }

    public Type getReturnType()
    {
        return _returnType;
    }

    public boolean hasReturnType()
    {
        return (_returnType != null);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();

        result.append(getModifiers().toString());
        if (result.length() > 0)
        {
            result.append(" ");
        }
        result.append(_returnType == null ? "void" : _returnType.toString());
        result.append(" ");
        result.append(getName());
        result.append("(");
        if (hasParameters())
        {
            result.append(getParameterList().toString());
        }
        result.append(")");

        return result.toString();
    }
}
