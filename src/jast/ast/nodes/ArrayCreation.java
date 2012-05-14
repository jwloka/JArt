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

import jast.SyntaxException;
import jast.ast.nodes.collections.ExpressionArray;
import jast.ast.nodes.collections.ExpressionArrayImpl;
import jast.ast.visitor.Visitor;

public class ArrayCreation extends Primary
{
    private Type             _createdType;
    private ExpressionArray  _dimExpressions = new ExpressionArrayImpl();
    private ArrayInitializer _initializer;

    public ArrayCreation(Type baseType)
    {
        _createdType = baseType.getClone();
        _createdType.setDimensions(0);
        _dimExpressions.setOwner(this);
    }

    public ArrayCreation(Type baseType, int dimensions)
    {
        this(baseType);
        _createdType.setDimensions(dimensions);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitArrayCreation(this);
    }

    public void addDimension()
    {
        _createdType.incDimensions();
    }

    public void addDimension(Expression dimExpression) throws SyntaxException
    {
        if (dimExpression != null)
        {
            if (_createdType.getDimensions() > _dimExpressions.getCount())
            {
                throw new SyntaxException("A dimension expression is not allowed here");
            }
            _dimExpressions.add(dimExpression);
        }
        addDimension();
    }

    public boolean containsArrayCreation()
    {
        return true;
    }

    public Type getCreatedType()
    {
        return _createdType;
    }

    public int getDimensions()
    {
        return _createdType.getDimensions();
    }

    public ExpressionArray getDimExpressions()
    {
        return _dimExpressions;
    }

    public ArrayInitializer getInitializer()
    {
        return _initializer;
    }

    public Type getType()
    {
        return _createdType;
    }

    public boolean isInitialized()
    {
        return (_initializer != null);
    }

    public void setInitializer(ArrayInitializer initializer)
    {
        _initializer = initializer;
        if (_initializer != null)
        {
            _initializer.setContainer(this);
        }
    }
}
