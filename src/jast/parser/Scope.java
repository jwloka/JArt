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
package jast.parser;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.LocalVariableArray;
import jast.ast.nodes.collections.LocalVariableIterator;

import java.util.Hashtable;

public class Scope
{
    private TypeDeclaration _typeDecl     = null;
    private Hashtable       _fields       = null;
    private Hashtable       _variables    = null;
    private Hashtable       _labels       = null;
    private Hashtable       _localClasses = null;

    public Scope()
    {
        _variables    = new Hashtable();
        _labels       = new Hashtable();
        _localClasses = new Hashtable();
    }

    public Scope(TypeDeclaration typeDecl)
    {
        _typeDecl = typeDecl;
        _fields   = new Hashtable();
    }

    public void defineField(FieldDeclaration decl)
    {
        _fields.put(decl.getName(), decl);
    }

    public void defineLabel(LabeledStatement label)
    {
        _labels.put(label.getName(), label);
    }

    public void defineLocalClass(ClassDeclaration decl)
    {
        _localClasses.put(decl.getName(), decl);
    }

    public void defineVariables(FormalParameterList params)
    {
        if (params != null)
        {
            for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
            {
                defineVariable(it.getNext());
            }
        }
    }

    public void defineVariable(LocalVariableDeclaration decl)
    {
        _variables.put(decl.getName(), decl);
    }

    public void defineVariables(LocalVariableArray variables)
    {
        if ((variables != null) && !variables.isEmpty())
        {
            for (LocalVariableIterator it = variables.getIterator(); it.hasNext();)
            {
                defineVariable(it.getNext());
            }
        }
    }

    public void deleteLabel(String name)
    {
        _labels.remove(name);
    }

    public TypeDeclaration getTypeDeclaration()
    {
        return _typeDecl;
    }

    public boolean isTypeScope()
    {
        return _fields != null;
    }

    public FieldDeclaration resolveField(String name)
    {
        if (_fields.containsKey(name))
        {
            return (FieldDeclaration)_fields.get(name);
        }
        else
        {
            return (_typeDecl != null ? _typeDecl.getFields().get(name) : null);
        }
    }

    public LabeledStatement resolveLabel(String name)
    {
        return (LabeledStatement)_labels.get(name);
    }

    public ClassDeclaration resolveLocalClass(String name)
    {
        return (ClassDeclaration)_localClasses.get(name);
    }

    public LocalVariableDeclaration resolveVariable(String name)
    {
        return (LocalVariableDeclaration)_variables.get(name);
    }
}
