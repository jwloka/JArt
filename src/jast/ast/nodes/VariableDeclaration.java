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

public abstract class VariableDeclaration extends    ContainedNode
                                          implements Modifiable
{
    private Modifiers           _mods;
    private Type                _type;
    private String              _name;
    private VariableInitializer _init;

    public VariableDeclaration(Modifiers           mods,
                              Type                type,
                              String              name,
                              VariableInitializer init)
    {
        _mods = mods;
        _type = type;
        _name = name;
        setInitializer(init);
    }

    public VariableInitializer getInitializer()
    {
        return _init;
    }

    public Modifiers getModifiers()
    {
        return _mods;
    }

    public String getName()
    {
        return _name;
    }

    public Type getType()
    {
        return _type;
    }

    public boolean hasInitializer()
    {
        return (_init != null);
    }

    public boolean isSimilarTo(VariableDeclaration other)
    {
        return getType().isEqualTo(other.getType());
    }

    public void setInitializer(VariableInitializer init)
    {
        _init = init;
        if (_init != null)
        {
            _init.setContainer(this);
        }
    }

    public void setModifiers(Modifiers mods)
    {
        _mods = mods;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public void setType(Type type)
    {
        _type = type;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();

        if (getType() != null)
        {
            result.append(getModifiers().toString());
            if (result.length() > 0)
            {
                result.append(" ");
            }
            result.append(getType().toString());
            result.append(" ");
        }
        result.append(getName());

        return result.toString();
    }
}
