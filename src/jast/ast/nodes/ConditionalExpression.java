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

public class ConditionalExpression extends Expression
{
    private Expression _condition;
    private Expression _trueExpression;
    private Expression _falseExpression;

    public ConditionalExpression(Expression condition,
                                 Expression trueExpression,
                                 Expression falseExpression)
    {
        setCondition(condition);
        setTrueExpression(trueExpression);
        setFalseExpression(falseExpression);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitConditionalExpression(this);
    }

    public Expression getCondition()
    {
        return _condition;
    }

    public Expression getFalseExpression()
    {
        return _falseExpression;
    }

    public Expression getTrueExpression()
    {
        return _trueExpression;
    }

    public Type getType()
    {
        Type trueType  = _trueExpression.getType();
        Type falseType = _falseExpression.getType();

        if ((trueType == null) || (falseType == null))
        {
            return null;
        }
        if (trueType.isEqualTo(falseType))
        {
            return trueType;
        }
        if (trueType.isPrimitive() && (trueType.getDimensions() == 0))
        {
            // falseType must be primitive, as well
            PrimitiveType primTrueType  = trueType.getPrimitiveBaseType();
            PrimitiveType primFalseType = falseType.getPrimitiveBaseType();

            if ((primTrueType  == PrimitiveType.BYTE_TYPE) &&
                (primFalseType == PrimitiveType.SHORT_TYPE))
            {
                return falseType;
            }
            if ((primTrueType  == PrimitiveType.SHORT_TYPE) &&
                (primFalseType == PrimitiveType.BYTE_TYPE))
            {
                return trueType;
            }
            // This not exactly correct (ref. P15.25 in Java Spec, type
            // determination point 2), but the only possible thing for
            // static analysis without symbolic evaluation of expressions
            return BinaryExpression.promoteBinaryNumeric(trueType, falseType);
        }
        // is it of null type ?
        if (trueType.getBaseName() == null)
        {
            return falseType;
        }
        if (falseType.getBaseName() == null)
        {
            return trueType;
        }
        return (falseType.isAssignmentCompatibleTo(trueType) ? trueType : falseType);
    }

    public void setCondition(Expression expr)
    {
        _condition = expr;
        _condition.setContainer(this);
    }

    public void setFalseExpression(Expression expr)
    {
        _falseExpression = expr;
        _falseExpression.setContainer(this);
    }

    public void setTrueExpression(Expression expr)
    {
        _trueExpression = expr;
        _trueExpression.setContainer(this);
    }
}
