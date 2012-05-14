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
import jast.ast.Node;
import jast.ast.visitor.Visitor;

import java.util.Hashtable;

public class PrimitiveType extends Node
{
    public static final PrimitiveType BOOLEAN_TYPE = new PrimitiveType("boolean", "Z");
    public static final PrimitiveType BYTE_TYPE    = new PrimitiveType("byte", "B");
    public static final PrimitiveType CHAR_TYPE    = new PrimitiveType("char", "C");
    public static final PrimitiveType DOUBLE_TYPE  = new PrimitiveType("double", "D");
    public static final PrimitiveType FLOAT_TYPE   = new PrimitiveType("float", "F");
    public static final PrimitiveType INT_TYPE     = new PrimitiveType("int", "I");
    public static final PrimitiveType LONG_TYPE    = new PrimitiveType("long", "J");
    public static final PrimitiveType SHORT_TYPE   = new PrimitiveType("short", "S");

    private static final Hashtable PREDEFINED_TYPES = new Hashtable();

    static
    {
        PREDEFINED_TYPES.put(BOOLEAN_TYPE._typeName, BOOLEAN_TYPE);
        PREDEFINED_TYPES.put(BYTE_TYPE._typeName,    BYTE_TYPE);
        PREDEFINED_TYPES.put(CHAR_TYPE._typeName,    CHAR_TYPE);
        PREDEFINED_TYPES.put(DOUBLE_TYPE._typeName,  DOUBLE_TYPE);
        PREDEFINED_TYPES.put(FLOAT_TYPE._typeName,   FLOAT_TYPE);
        PREDEFINED_TYPES.put(INT_TYPE._typeName,     INT_TYPE);
        PREDEFINED_TYPES.put(LONG_TYPE._typeName,    LONG_TYPE);
        PREDEFINED_TYPES.put(SHORT_TYPE._typeName,   SHORT_TYPE);
    }

    private String _typeName;
    private String _signature;

    private PrimitiveType(String typeName, String signature)
    {
        _typeName  = typeName;
        _signature = signature;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitPrimitiveType(this);
    }

    public static PrimitiveType createInstance(String typeName) throws SyntaxException
    {
        PrimitiveType result = (PrimitiveType)PREDEFINED_TYPES.get(typeName);

        if (result == null)
        {
            throw new SyntaxException("Primitive type " + typeName + " is not known");
        }
        return result;
    }

    public Node getContainer()
    {
        return null;
    }

    public String getName()
    {
        return _typeName;
    }

    public String getSignature()
    {
        return _signature;
    }

    public boolean isWideningConvertibleTo(PrimitiveType varType)
    {
        if (this == BYTE_TYPE)
        {
            if ((varType == SHORT_TYPE) ||
                (varType == INT_TYPE) ||
                (varType == LONG_TYPE) ||
                (varType == FLOAT_TYPE) ||
                (varType == DOUBLE_TYPE))
            {
                return true;
            }
        }
        else if ((this == CHAR_TYPE) ||
                 (this == SHORT_TYPE))
        {
            if ((varType == INT_TYPE) ||
                (varType == LONG_TYPE) ||
                (varType == FLOAT_TYPE) ||
                (varType == DOUBLE_TYPE))
            {
                return true;
            }
        }
        else if (this == INT_TYPE)
        {
            if ((varType == LONG_TYPE) ||
                (varType == FLOAT_TYPE) ||
                (varType == DOUBLE_TYPE))
            {
                return true;
            }
        }
        else if (this == LONG_TYPE)
        {
            if ((varType == FLOAT_TYPE) ||
                (varType == DOUBLE_TYPE))
            {
                return true;
            }
        }
        else if (this == FLOAT_TYPE)
        {
            if (varType == DOUBLE_TYPE)
            {
                return true;
            }
        }
        return false;
    }
}
