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
package jast.analysis;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ConstructorArray;
import jast.ast.nodes.collections.ConstructorArrayImpl;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.MethodArray;
import jast.ast.nodes.collections.MethodArrayImpl;
import jast.ast.nodes.collections.MethodIterator;


/**
 * Contains for a given type name, all features which were accessed
 * via that type.
 */
public class TypeUsage
{
    private TypeDeclaration  _type;
    private FieldArray       _fields;
    private MethodArray      _methods;
    private ConstructorArray _constructors;
    private int              _usageCount;

    public TypeUsage(TypeDeclaration type)
    {
        _type = type;

        _fields = new FieldArrayImpl();
        _constructors = new ConstructorArrayImpl();
        _methods = new MethodArrayImpl();
        _usageCount = 0;
    }

    public void addUsage(ConstructorDeclaration constructor)
    {
        _constructors.add(constructor);
    }

    public void addUsage(FieldDeclaration field)
    {
        _fields.add(field);
    }

    public void addUsage(MethodDeclaration method)
    {
        _methods.add(method);
    }

    public TypeDeclaration getType()
    {
        return _type;
    }

    public ConstructorArray getUsedConstructors()
    {
        return _constructors;
    }

    public int getUsedFeatureCount()
    {
        return getUsedFields().getCount()
            + getUsedConstructors().getCount()
            + getUsedMethods().getCount();
    }

    public FieldArray getUsedFields()
    {
        return _fields;
    }

    public MethodArray getUsedMethods()
    {
        return _methods;
    }

    public boolean isUsed(ConstructorDeclaration constructor)
    {
        if ((constructor == null)
            || !_constructors.contains(constructor.getSignature()))
            {
            return false;
        }

        ConstructorDeclaration storedConstr =
            _constructors.get(
                constructor.getName(),
                constructor.getParameterList() == null
                    ? null
                    : constructor.getParameterList().getParameterTypes());

        return storedConstr == constructor;
    }

    public boolean isUsed(FieldDeclaration field)
    {
        if ((field == null) || !_fields.contains(field.getName()))
            {
            return false;
        }

        return _fields.get(field.getName()) == field;
    }

    public boolean isUsed(MethodDeclaration method)
    {
        if ((method == null) || !_methods.contains(method.getSignature()))
            {
            return false;
        }

        MethodDeclaration storedMethod =
            _methods.get(
                method.getName(),
                method.getParameterList() == null
                    ? null
                    : method.getParameterList().getParameterTypes());

        return storedMethod == method;
    }

    public boolean usesMethod(String methodNameStartsWith)
    {
        String methName = methodNameStartsWith.toLowerCase();

        if (_methods == null)
            {
            return false;
        }

        for (MethodIterator iter = _methods.getIterator(); iter.hasNext();)
            {
            String curMethName = iter.getNext().getSignature().toLowerCase();

            if (curMethName.startsWith(methName))
                {
                return true;
            }
        }

        return false;
    }

    public int getUsageCount()
    {
        return _usageCount;
    }

    public void incUsageCount()
    {
        _usageCount++;
    }
}
