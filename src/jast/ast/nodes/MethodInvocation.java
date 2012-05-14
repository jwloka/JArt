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

public class MethodInvocation extends Invocation
{
    private MethodDeclaration _decl;
    private String            _methodName;

    public MethodInvocation(Primary baseExpr, String name, ArgumentList arguments)
    {
        super(baseExpr, arguments);
        _methodName = name;
    }

    public MethodInvocation(String name, ArgumentList arguments)
    {
        super(null, arguments);
        _methodName = name;
    }

    public MethodInvocation(MethodDeclaration decl, ArgumentList arguments)
    {
        this(decl.getName(), arguments);
        _decl = decl;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitMethodInvocation(this);
    }

    public MethodDeclaration getMethodDeclaration()
    {
        return _decl;
    }

    public String getMethodName()
    {
        return (_decl != null ? _decl.getName() : _methodName);
    }

    public Type getType()
    {
        return (_decl != null ? _decl.getReturnType() : null);
    }

    public void setMethodDeclaration(MethodDeclaration decl)
    {
        _decl = decl;
    }


    public MethodInvocation(Primary baseExpr,  MethodDeclaration decl, ArgumentList arguments)
    {
        this(baseExpr, decl.getName(), arguments);
        _decl = decl;
    }
}
