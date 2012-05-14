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

public class AssignmentExpression extends Expression
{
    public static final int ASSIGN_OP                         = 0;
    public static final int MULTIPLY_ASSIGN_OP                = 1;
    public static final int DIVIDE_ASSIGN_OP                = 2;
    public static final int MOD_ASSIGN_OP                     = 3;
    public static final int PLUS_ASSIGN_OP                     = 4;
    public static final int MINUS_ASSIGN_OP                     = 5;
    public static final int SHIFT_LEFT_ASSIGN_OP            = 6;
    public static final int SHIFT_RIGHT_ASSIGN_OP            = 7;
    public static final int ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP = 8;
    public static final int BITWISE_AND_ASSIGN_OP            = 9;
    public static final int BITWISE_XOR_ASSIGN_OP            = 10;
    public static final int BITWISE_OR_ASSIGN_OP            = 11;

    private Expression _leftHandSide;
    private int        _operator;
    private Expression _valueExpr;

    public AssignmentExpression(Expression leftHandSide,
                                int        operator,
                                Expression valueExpr)
    {
        if ((operator < ASSIGN_OP) || (operator > BITWISE_OR_ASSIGN_OP))
        {
            throw new IllegalArgumentException();
        }
        if (!(leftHandSide instanceof FieldAccess) && !(leftHandSide instanceof VariableAccess) && !(leftHandSide instanceof UnresolvedAccess) && !(leftHandSide instanceof ArrayAccess))
        {
            throw new IllegalArgumentException();
        }
        _operator = operator;
        setLeftHandSide(leftHandSide);
        setValueExpression(valueExpr);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitAssignmentExpression(this);
    }

    public Expression getLeftHandSide()
    {
        return _leftHandSide;
    }

    public int getOperator()
    {
        return _operator;
    }

    public Type getType()
    {
        return _leftHandSide.getType();
    }

    public Expression getValueExpression()
    {
        return _valueExpr;
    }

    public void setLeftHandSide(Expression expr)
    {
        _leftHandSide = expr;
        _leftHandSide.setContainer(this);
    }

    public void setValueExpression(Expression expr)
    {
        _valueExpr = expr;
        _valueExpr.setContainer(this);
    }
}
