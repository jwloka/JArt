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
package jart.analysis.duplication.collections;

import jast.ast.collections.TypedHashArrayBase;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.collections.InvocableIterator;


public class InvocableArrayImpl extends    TypedHashArrayBase
                                implements InvocableArray
{

    public void add(InvocableDeclaration method)
    {
        set(method.getName(), method);
    }

    public boolean contains(InvocableDeclaration method)
    {
        return contains((Object) method);
    }

    public InvocableDeclaration get(int idx)
    {
        return (InvocableDeclaration) at(idx);
    }

    public InvocableIterator getIterator()
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
}
