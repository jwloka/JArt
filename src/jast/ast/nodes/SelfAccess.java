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

// for "this" or "super"
public class SelfAccess extends Primary
{
    public static final String THIS_KEYWORD  = "this";
    public static final String SUPER_KEYWORD = "super";

    private TypeAccess _typeAccess;
    private Type       _type;
    private boolean    _isSuper;

    public SelfAccess(TypeAccess typeAccess, boolean isSuper)
    {
        _isSuper = isSuper;
        setTypeAccess(typeAccess);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitSelfAccess(this);
    }

    // Node must usually be resolved first
    public Type getType()
    {
        if ((_type == null) && (_typeAccess != null) && !_isSuper)
        {
            _type = _typeAccess.getType();
        }
        return _type;
    }

    public TypeAccess getTypeAccess()
    {
        return _typeAccess;
    }

    public boolean isQualified()
    {
        return (_typeAccess != null);
    }

    public boolean isSuper()
    {
        return _isSuper;
    }

    // Must take "super" into account !
    public void setType(Type type)
    {
        _type = type;
    }

    public void setTypeAccess(TypeAccess typeAccess)
    {
        _typeAccess = typeAccess;
        if (_typeAccess != null)
        {
            _typeAccess.setContainer(this);
            if (!_isSuper)
            {
                _type = _typeAccess.getType();
            }
        }
    }
}
