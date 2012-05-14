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
package jast.prettyprinter;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.LocalVariableArray;
import jast.ast.nodes.collections.LocalVariableArrayImpl;

// Helper class which enables the pretty printer to uniformly
// handle local variables and fields
public class VarDeclArray
{
    private LocalVariableArray _localVars;
    private FieldArray         _fields;

    public VarDeclArray()
    {}

    public VarDeclArray(FieldArray fields)
    {
        _fields = fields;
    }

    public VarDeclArray(LocalVariableArray localVars)
    {
        _localVars = localVars;
    }

    public void clear()
    {
        _localVars = null;
        _fields    = null;
    }

    public void add(FieldDeclaration decl)
    {
        if (_fields == null)
        {
            _fields = new FieldArrayImpl();
        }
        _fields.add(decl);
    }

    public void add(LocalVariableDeclaration decl)
    {
        if (_localVars == null)
        {
            _localVars = new LocalVariableArrayImpl();
        }
        _localVars.add(decl);
    }

    public VariableDeclaration get(int idx)
    {
        if (_localVars != null)
        {
            return _localVars.get(idx);
        }
        else if (_fields != null)
        {
            return _fields.get(idx);
        }
        else
        {
            return null;
        }
    }

    public int getCount()
    {
        if (_localVars != null)
        {
            return _localVars.getCount();
        }
        else if (_fields != null)
        {
            return _fields.getCount();
        }
        else
        {
            return 0;
        }
    }

    public boolean isEmpty()
    {
        if (_localVars != null)
        {
            return _localVars.isEmpty();
        }
        else if (_fields != null)
        {
            return _fields.isEmpty();
        }
        else
        {
            return true;
        }
    }
}
