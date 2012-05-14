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
import jast.ast.visitor.Visitor;

public class UnaryExpression extends Expression
{
    public static final int PLUS_OP       = 0;
    public static final int MINUS_OP      = 1;
    public static final int INCREMENT_OP  = 2;
    public static final int DECREMENT_OP  = 3;
    public static final int COMPLEMENT_OP = 4;
    public static final int NEGATION_OP   = 5;
    public static final int CAST_OP       = 6;

    private int        _operator;
    private Expression _innerExpr;
    private Type       _castType;

    public UnaryExpression(int operator, Expression innerExpr)
    {
        if ((operator < PLUS_OP) || (operator > CAST_OP))
        {
            throw new IllegalArgumentException();
        }
        _operator = operator;
        setInnerExpression(innerExpr);
        _castType = null;
    }

    public UnaryExpression(Type castType, Expression innerExpr)
    {
        _operator = CAST_OP;
        setInnerExpression(innerExpr);
        _castType = castType;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitUnaryExpression(this);
    }

    public Type getCastType()
    {
        return _castType;
    }

    public Expression getInnerExpression()
    {
        return _innerExpr;
    }

    public int getOperator()
    {
        return _operator;
    }

    public Type getType()
    {
        if (_operator == CAST_OP)
        {
            return _castType;
        }
        else
        {
            Type result = _innerExpr.getType();

            if ((_operator == PLUS_OP) ||
                (_operator == MINUS_OP) ||
                (_operator == COMPLEMENT_OP))
            {
                result = promoteUnaryNumeric(result);
            }
            return result;
        }
    }

    public static Type promoteUnaryNumeric(Type src)
    {
        if ((src != null) && src.isPrimitive() && (src.getDimensions() == 0))
        {
            PrimitiveType primType = src.getPrimitiveBaseType();

            if ((primType == PrimitiveType.BYTE_TYPE) ||
                (primType == PrimitiveType.CHAR_TYPE) ||
                (primType == PrimitiveType.SHORT_TYPE))
            {
                return Global.getFactory().createType(PrimitiveType.INT_TYPE, 0);
            }
        }
        return src;
    }

    public void setInnerExpression(Expression expr)
    {
        _innerExpr = expr;
        _innerExpr.setContainer(this);
    }
}
