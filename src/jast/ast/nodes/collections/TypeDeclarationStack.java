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
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.TypeDeclaration;

import java.util.Vector;

public class TypeDeclarationStack
{
    private Vector _data = new Vector();

    public void clear()
    {
        _data.removeAllElements();
    }

    public TypeDeclarationIterator getIterator()
    {
        return new TypeDeclarationIterator()
        {
            private int _curIdx = 0;

            public boolean hasNext()
            {
                return (_curIdx < _data.size());
            }

            public TypeDeclaration getNext()
            {
                return (TypeDeclaration)_data.get(_curIdx++);
            }
        };
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public boolean isInLocalClass()
    {
        Object obj;

        for (int idx = 0; idx < _data.size(); idx++)
        {
            obj = _data.elementAt(idx);
            if ((obj instanceof ClassDeclaration) &&
                ((ClassDeclaration)obj).isLocal())
            {
                return true;
            }
        }
        return false;
    }

    public void pop()
    {
        if (!_data.isEmpty())
        {
            _data.removeElementAt(0);
        }
    }

    public void push(TypeDeclaration decl)
    {
        _data.insertElementAt(decl, 0);
    }

    public TypeDeclaration top()
    {
        return (TypeDeclaration) _data.firstElement();
    }
}
