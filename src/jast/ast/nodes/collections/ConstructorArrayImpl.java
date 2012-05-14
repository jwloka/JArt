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
package jast.ast.nodes.collections;
import jast.ast.collections.TypedHashArrayBase;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.Type;

public class ConstructorArrayImpl extends    TypedHashArrayBase
                                  implements ConstructorArray
{

    public void add(ConstructorDeclaration constructor)
    {
        set(constructor.getSignature(), constructor);
    }

    public ConstructorDeclaration get(int idx)
    {
        return (ConstructorDeclaration)at(idx);
    }

    public ConstructorDeclaration get(String name, Type[] paramTypes)
    {
        return (ConstructorDeclaration)at(ArrayHelper.toSignature(name, paramTypes));
    }

    public InvocableIterator getInvocableIterator()
    {
        return new InvocableIterator()
        {
            private int _curIdx = 0;

            public boolean hasNext()
            {
                return (_curIdx < getCount());
            }

            public InvocableDeclaration getNext()
            {
                return get(_curIdx++);
            }
        };
    }

    public ConstructorIterator getIterator()
    {
        return new ConstructorIterator()
        {
            private int _curIdx = 0;

            public boolean hasNext()
            {
                return (_curIdx < getCount());
            }

            public ConstructorDeclaration getNext()
            {
                return get(_curIdx++);
            }
        };
    }

    public void remove(String name, Type[] paramTypes)
    {
        remove(ArrayHelper.toSignature(name, paramTypes));
    }

    public void updateSignatures()
    {
        String oldSignature;
        String newSignature;

        for (int idx = 0; idx < getCount(); idx++)
        {
            oldSignature = getName(idx);
            newSignature = get(idx).getSignature();

            if (!oldSignature.equals(newSignature))
            {
                changeName(oldSignature, newSignature);
            }
        }
    }
}
