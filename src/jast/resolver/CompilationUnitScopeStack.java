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
package jast.resolver;
import jast.ast.nodes.CompilationUnit;

import java.util.Hashtable;
import java.util.Vector;

public class CompilationUnitScopeStack
{
    private Vector    _data  = new Vector();
    private Hashtable _names = new Hashtable();

    public void clear()
    {
        _data.removeAllElements();
    }

    public boolean contains(String compilationUnitName)
    {
        return _names.containsKey(compilationUnitName);
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public void pop()
    {
        if (!_data.isEmpty())
        {
            _data.removeElementAt(0);
        }
    }

    public void push(CompilationUnit unit)
    {
        CompilationUnitScope context = new CompilationUnitScope(unit);

        _data.insertElementAt(context, 0);
        _names.put(unit.getName(), context);
    }

    public void push(CompilationUnitScope scope)
    {
        _data.insertElementAt(scope, 0);
        _names.put(scope.getCompilationUnit().getName(), scope);
    }

    public CompilationUnitScope top()
    {
        return _data.isEmpty() ? null : (CompilationUnitScope)_data.firstElement();
    }
}
