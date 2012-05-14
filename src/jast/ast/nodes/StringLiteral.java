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

public class StringLiteral extends Literal
{
    private String _value;
    // static ?
    private Type   _type = Global.getFactory().createType("java.lang.String", 0);

    public StringLiteral(String asString) throws SyntaxException
    {
        super(asString);

        // There seems to be no function to do something similar to
        // Integer.parseInt for strings; therefore we do it ourselves
        try
        {
            String decoded = parseString(asString);

            // The decoded string starts and ends with a double-apostroph
            if ((decoded == null) || (decoded.length() < 2))
            {
                throw new SyntaxException("Literal " + asString + " is not a valid string");
            }
            _value = decoded.substring(1, decoded.length() - 1);
        }
        catch (IllegalArgumentException ex)
        {
            throw new SyntaxException("Literal " + asString + " is not a valid string");
        }
    }

    public void accept(Visitor visitor)
    {
        visitor.visitStringLiteral(this);
    }

    public Type getType()
    {
        return _type;
    }

    public String getValue()
    {
        return _value;
    }

    public static String parseString(String rep) throws IllegalArgumentException
    {
        if (rep == null)
        {
            return null;
        }

        char[] result = new char[rep.length()];
        int    count  = 0;
        int    idx    = 0;

        while (idx < result.length)
        {
            if ((rep.charAt(idx) == '\\') && (idx < result.length - 1))
            {
                idx++;

                char start = rep.charAt(idx);
                char real  = ' ';
                int  value;

                switch (start)
                {
                    case 'b' :
                        real = '\b';
                        break;
                    case 't' :
                        real = '\t';
                        break;
                    case 'n' :
                        real = '\n';
                        break;
                    case 'f' :
                        real = '\f';
                        break;
                    case 'r' :
                        real = '\r';
                        break;
                    case '"' :
                        real = '"';
                        break;
                    case '\'' :
                        real = '\'';
                        break;
                    case '\\' :
                        real = '\\';
                        break;
                    case '0' :
                    case '1' :
                    case '2' :
                    case '3' :
                    case '4' :
                    case '5' :
                    case '6' :
                    case '7' :
                        if ((start <= '3') &&
                            (idx < result.length - 2) &&
                            (rep.charAt(idx + 1) >= '0') &&
                            (rep.charAt(idx + 1) <= '7') &&
                            (rep.charAt(idx + 2) >= '0') &&
                            (rep.charAt(idx + 2) <= '7'))
                        {
                            value = 64 * Character.digit(start, 10) +
                                     8 * Character.digit(rep.charAt(idx + 1), 10) +
                                         Character.digit(rep.charAt(idx + 2), 10);
                            idx += 2;
                        }
                        else if ((idx < result.length - 1) &&
                                 (rep.charAt(idx + 1) >= '0') &&
                                 (rep.charAt(idx + 1) <= '7'))
                        {
                            value = 8 * Character.digit(start, 10) +
                                        Character.digit(rep.charAt(idx + 1), 10);
                            idx++;
                        }
                        else
                        {
                            value = Character.digit(start, 10);
                        }
                        real = (char)value;
                        break;
                    case 'u' :
                        // there could be more than one 'u' present
                        while (rep.charAt(idx+1) == 'u')
                        {
                            idx++;
                        }
                        if (idx >= result.length - 4)
                        {
                            throw new IllegalArgumentException("Unicode escape ended premature");
                        }
                        value = 4096 * Character.digit(rep.charAt(idx + 1), 16) +
                                 256 * Character.digit(rep.charAt(idx + 2), 16) +
                                  16 * Character.digit(rep.charAt(idx + 3), 16) +
                                       Character.digit(rep.charAt(idx + 4), 16);
                        real = (char)value;
                        idx += 4;
                        break;
                    default :
                        throw new IllegalArgumentException("Unknown escape sequence");
                }
                result[count] = real;
                count++;
            }
            else
            {
                result[count] = rep.charAt(idx);
                count++;
            }
            idx++;
        }
        if (count < result.length)
        {
            char[] realResult = new char[count];

            System.arraycopy(result, 0, realResult, 0, count);
            result = realResult;
        }
        return new String(result);
    }
}
