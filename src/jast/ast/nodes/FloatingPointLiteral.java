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
import jast.SyntaxException;
import jast.ast.visitor.Visitor;

public class FloatingPointLiteral extends Literal
{
    private double  _value;
    private boolean _isDouble = true;

    public FloatingPointLiteral(String asString) throws SyntaxException
    {
        super(asString);

        String number = asString.toLowerCase();

        _isDouble = !number.endsWith("f");
        try
        {
            // Interestingly, there seems to be no possibility to
            // provoke a NumberFormatException via parsing a literal
            // For literals which denote a number out of the range of
            // Float/Double, an Infinity value is returned

            if (number.endsWith("f") || number.endsWith("d"))
            {
                number = number.substring(0, number.length() - 1);
            }
            if (_isDouble)
            {
                _value = Double.parseDouble(number);
                if (Double.isInfinite(_value))
                {
                    throw new SyntaxException("Double literal " + asString + " is not a valid double number");
                }
            }
            else
            {
                float tmp = Float.parseFloat(number);

                if (Float.isInfinite(tmp))
                {
                    throw new SyntaxException("Float literal " + asString + " is not a valid float number");
                }
                _value = tmp;
            }
        }
        catch (NumberFormatException ex)
        {
            throw new SyntaxException("Literal " + asString + " is not a valid number");
        }
    }

    public void accept(Visitor visitor)
    {
        visitor.visitFloatingPointLiteral(this);
    }

    public Type getType()
    {
        return (_isDouble ? Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 0) :
                            Global.getFactory().createType(PrimitiveType.FLOAT_TYPE, 0));
    }

    public double getValue()
    {
        return _value;
    }

    public boolean isDouble()
    {
        return _isDouble;
    }
}
