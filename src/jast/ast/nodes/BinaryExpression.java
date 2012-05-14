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

public class BinaryExpression extends Expression
{

    public static final int MULTIPLY_OP                 = 0;
    public static final int DIVIDE_OP                 = 1;
    public static final int MOD_OP                   = 2;

    public static final int PLUS_OP                  = 3;
    public static final int MINUS_OP                 = 4;

    public static final int SHIFT_LEFT_OP            = 5;
    public static final int SHIFT_RIGHT_OP            = 6;
    public static final int ZERO_FILL_SHIFT_RIGHT_OP = 7;

    public static final int LOWER_OP                    = 8;
    public static final int GREATER_OP                = 9;
    public static final int LOWER_OR_EQUAL_OP        = 10;
    public static final int GREATER_OR_EQUAL_OP      = 11;

    public static final int EQUAL_OP                    = 12;
    public static final int NOT_EQUAL_OP                = 13;

    public static final int BITWISE_AND_OP            = 14;

    public static final int BITWISE_XOR_OP            = 15;

    public static final int BITWISE_OR_OP            = 16;

    public static final int AND_OP                    = 17;

    public static final int OR_OP                    = 18;

    private int        _operator;
    private Expression _leftOp;
    private Expression _rightOp;

    public BinaryExpression(int        operator,
                            Expression leftOp,
                            Expression rightOp)
    {
        if ((operator < MULTIPLY_OP) || (operator > OR_OP))
        {
            throw new IllegalArgumentException();
        }
        _operator = operator;
        setLeftOperand(leftOp);
        setRightOperand(rightOp);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitBinaryExpression(this);
    }

    public Expression getLeftOperand()
    {
        return _leftOp;
    }

    public int getOperator()
    {
        return _operator;
    }

    public Expression getRightOperand()
    {
        return _rightOp;
    }

    public Type getType()
    {
        Type leftType = _leftOp.getType();
        Type rightType = _rightOp.getType();

        if ((leftType == null) || (rightType == null))
        {
            return null;
        }
        if ((_operator == LOWER_OP) ||
            (_operator == GREATER_OP) ||
            (_operator == LOWER_OR_EQUAL_OP) ||
            (_operator == GREATER_OR_EQUAL_OP) ||
            (_operator == EQUAL_OP) ||
            (_operator == NOT_EQUAL_OP) ||
            (_operator == AND_OP) ||
            (_operator == OR_OP))
        {
            return Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 0);
        }
        if ((_operator == SHIFT_LEFT_OP) ||
            (_operator == SHIFT_RIGHT_OP) ||
            (_operator == ZERO_FILL_SHIFT_RIGHT_OP))
        {
            return UnaryExpression.promoteUnaryNumeric(leftType);
        }
        if (_operator == PLUS_OP)
        {
            // Assuming that the expression is correct
            // it only can be a string
            if (leftType.isReference())
            {
                return leftType;
            }
            if (rightType.isReference())
            {
                return rightType;
            }
        }
        if (((_operator == BITWISE_AND_OP) ||
             (_operator == BITWISE_OR_OP) ||
             (_operator == BITWISE_XOR_OP)) &&
            (leftType.getPrimitiveBaseType() == PrimitiveType.BOOLEAN_TYPE))
        {
            return leftType;
        }
        return promoteBinaryNumeric(leftType, rightType);
    }

    public static Type promoteBinaryNumeric(Type leftType, Type rightType)
    {
        if (leftType.isPrimitive() &&
            (leftType.getDimensions() == 0) &&
            rightType.isPrimitive() &&
            (rightType.getDimensions() == 0))
        {
            PrimitiveType leftPrimType = leftType.getPrimitiveBaseType();
            PrimitiveType rightPrimType = rightType.getPrimitiveBaseType();

            // checking for binary numeric promotion
            if ((leftPrimType == PrimitiveType.DOUBLE_TYPE) ||
                (rightPrimType == PrimitiveType.DOUBLE_TYPE))
            {
                return Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0);
            }
            else if ((leftPrimType == PrimitiveType.FLOAT_TYPE) ||
                     (rightPrimType == PrimitiveType.FLOAT_TYPE))
            {
                return Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0);
            }
            else if ((leftPrimType == PrimitiveType.LONG_TYPE) ||
                     (rightPrimType == PrimitiveType.LONG_TYPE))
            {
                return Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0);
            }
            else
            {
                return Global.getFactory().createType(PrimitiveType.INT_TYPE, 0);
            }
        }
        return null;
    }

    public void setLeftOperand(Expression expr)
    {
        _leftOp = expr;
        _leftOp.setContainer(this);
    }

    public void setRightOperand(Expression expr)
    {
        _rightOp = expr;
        _rightOp.setContainer(this);
    }
}
