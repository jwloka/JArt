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

public class Instantiation extends Invocation
{
    private Type                      _type;
    private ConstructorDeclaration    _constructor;
    private AnonymousClassDeclaration _anonymousClass;

    public Instantiation(Primary                   baseExpr,
                         Type                      instantiatedType,
                         ArgumentList              args,
                         AnonymousClassDeclaration anonymousClass)
    {
        super(baseExpr, args);
        _type = instantiatedType;
        setAnonymousClass(anonymousClass);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitInstantiation(this);
    }

    public AnonymousClassDeclaration getAnonymousClass()
    {
        return _anonymousClass;
    }

    public Type getInstantiatedType()
    {
        return _type;
    }

    public Type getType()
    {
        return _type;
    }

    public void setAnonymousClass(AnonymousClassDeclaration decl)
    {
        _anonymousClass = decl;
        if (_anonymousClass != null)
        {
            _anonymousClass.setContainer(this);
        }
    }

    public void setInvokedConstructor(ConstructorDeclaration decl)
    {
        _constructor = decl;
    }

    public ConstructorDeclaration getInvokedConstructor()
    {
        return _constructor;
    }

    public boolean withAnonymousClass()
    {
        return (_anonymousClass != null);
    }
}
