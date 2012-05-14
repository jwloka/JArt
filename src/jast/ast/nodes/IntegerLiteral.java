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

public class IntegerLiteral extends Literal
{
    private long    _value;
    private boolean _isLong  = false;
    private boolean _isHex   = false;
    private boolean _isOctal = false;

    public IntegerLiteral(String asString) throws SyntaxException
    {
        super(asString);

        String number = asString();

        if (asString.toLowerCase().endsWith("l"))
        {
            _isLong = true;
            number = number.substring(0, number.length() - 1);
        }
        if (asString.toLowerCase().startsWith("0x"))
        {
            _isHex = true;
            try
            {
                _value = (_isLong ? Long.parseLong(number.substring(2), 16) : Integer.parseInt(number.substring(2), 16));
            }
            catch (NumberFormatException ex)
            {
                // the parse method is not able to parse all legal
                // long literals (such as '0xff00000000000000L')
                _value = -1;
            }
        }
        else
            if (asString.startsWith("0") && (asString.length() > 1))
            {
                _isOctal = true;
                try
                {
                    _value = (_isLong ? Long.parseLong(number, 8) : Integer.parseInt(number, 8));
                }
                catch (NumberFormatException ex)
                {
                    _value = -1;
                }
            }
            else
            {
                try
                {
                    _value = (_isLong ? Long.parseLong(number) : Integer.parseInt(number));
                }
                catch (NumberFormatException ex)
                {
                    _value = -1;
                }
            }
    }

    public void accept(Visitor visitor)
    {
        visitor.visitIntegerLiteral(this);
    }

    public Type getType()
    {
        return (_isLong ? Global.getFactory().createType(PrimitiveType.LONG_TYPE, 0) :
                          Global.getFactory().createType(PrimitiveType.INT_TYPE, 0));
    }

    public long getValue()
    {
        return _value;
    }

    public boolean isHexadecimal()
    {
        return _isHex;
    }

    public boolean isLong()
    {
        return _isLong;
    }

    public boolean isOctal()
    {
        return _isOctal;
    }
}
