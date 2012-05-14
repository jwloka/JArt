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
import jast.Global;
import jast.SyntaxException;
import jast.ast.visitor.Visitor;

// Note that anonymous classes are considered local as they
// are always defined in a block context (which is different
// from the java language spec)
public class AnonymousClassDeclaration extends ClassDeclaration
{
    // As we cannot know whether it is a class or interface
    // we store it separatley and must wait for the resolving
    private Type _baseType;

    public AnonymousClassDeclaration(Type baseType)
    {
        super(Global.getFactory().createModifiers(), null, true);
        getModifiers().setFinal();
        _baseType = baseType;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitAnonymousClassDeclaration(this);
    }

    public void addDeclaration(Feature decl) throws SyntaxException
    {
        if (decl instanceof ConstructorDeclaration)
        {
            throw new SyntaxException("ConstructorDeclaration not allowed here");
        }
        else
        {
            super.addDeclaration(decl);
        }
    }

    public Type getBaseType()
    {
        return _baseType;
    }
}
