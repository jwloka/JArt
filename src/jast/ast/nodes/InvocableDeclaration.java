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
import jast.ast.ContainedNode;
import jast.ast.nodes.collections.TypeArray;
import jast.ast.nodes.collections.TypeArrayImpl;

public abstract class InvocableDeclaration extends    ContainedNode
                                           implements Modifiable,
                                                      FeatureWithBody
{
    private Modifiers           _mods;
    private String              _name;
    private FormalParameterList _params;
    private TypeArray           _exceptions = new TypeArrayImpl();
    private Block               _body;

    public InvocableDeclaration(Modifiers mods, String name, FormalParameterList params)
    {
        _mods = mods;
        _name = name;
        setParameterList(params);
        // Types have value semantics; therefore we do not set an
        // owner for the exception type array
    }

    public Block getBody()
    {
        return _body;
    }

    public Modifiers getModifiers()
    {
        return _mods;
    }

    public void setModifiers(Modifiers mods)
    {
        _mods = mods;
    }

    public String getName()
    {
        return _name;
    }

    public FormalParameterList getParameterList()
    {
        return _params;
    }

    public String getSignature()
    {
        StringBuffer result = new StringBuffer();

        result.append(getName());
        result.append("(");
        if (hasParameters())
        {
            result.append(getParameterList().getSignature());
        }
        result.append(")");

        return result.toString();
    }

    public TypeArray getThrownExceptions()
    {
        return _exceptions;
    }

    public boolean hasBody()
    {
        return (_body != null);
    }

    public boolean hasParameters()
    {
        return (_params != null);
    }

    public void setBody(Block body)
    {
        _body = body;
        if (_body != null)
        {
            _body.setContainer(this);
        }
    }

    public void setParameterList(FormalParameterList params)
    {
        _params = params;
        if (_params != null)
        {
            _params.setContainer(this);
        }
    }
}
