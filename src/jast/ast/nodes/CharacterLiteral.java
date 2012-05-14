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

public class CharacterLiteral extends Literal
{
    private char _value;

    public CharacterLiteral(String asString) throws SyntaxException
    {
        super(asString);

        // There seems to be no function to do something similar to
        // Integer.parseInt for strings; therefore we do it ourselves
        String decoded = StringLiteral.parseString(asString);

        // The decoded string starts and ends with a apostroph
        if ((decoded == null) || (decoded.length() != 3))
        {
            throw new SyntaxException("Literal '" + asString + "' does not contain a single character");
        }
        _value = decoded.charAt(1);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitCharacterLiteral(this);
    }

    public Type getType()
    {
        return Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0);
    }

    public char getValue()
    {
        return _value;
    }
}
