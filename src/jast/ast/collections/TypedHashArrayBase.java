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
package jast.ast.collections;
import jast.ast.Contained;
import jast.ast.Node;
import jast.helpers.HashArray;

public abstract class TypedHashArrayBase extends    NodeContainerBase
                                         implements TypedHashArray
{
    private HashArray _data = new HashArray();

    protected Object at(int idx)
    {
        return _data.get(idx);
    }

    protected void insert(String key, Object obj, int pos)
    {
        _data.insert(key, obj, pos);
    }

    protected Object at(String name)
    {
        return _data.get(name);
    }

    public void changeName(String oldName, String newName)
    {
        _data.changeKey(oldName, newName);
    }

    public void clear()
    {
        if (!_data.isEmpty())
        {
            notifyOwner(new ClearEvent(this));
            _data.clear();
        }
    }

    protected boolean contains(Object obj)
    {
        return _data.containsValue(obj);
    }

    public boolean contains(String name)
    {
        return _data.contains(name);
    }

    public int getCount()
    {
        return _data.getCount();
    }

    public String getName(int idx)
    {
        return _data.getKey(idx);
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public void remove(int idx)
    {
        if (_data.get(idx) instanceof Contained)
        {
            notifyOwner(new RemovalEvent((Contained)_data.get(idx), this));
        }
        _data.remove(idx);
    }

    public void remove(String name)
    {
        if (_data.contains(name))
        {
            if (_data.get(name) instanceof Contained)
            {
                notifyOwner(new RemovalEvent((Contained)_data.get(name), this));
            }
            _data.remove(name);
        }
    }

    protected void set(String name, Object obj)
    {
        if (obj instanceof Contained)
        {
            notifyOwner(new AssignmentEvent((Contained)obj, name, this));
        }
        setContainerFor(obj);
        _data.add(name, obj);
    }

    public void setOwner(Node owner)
    {
        super.setOwner(owner);
        for (int idx = 0; idx < _data.getCount(); idx++)
        {
            setContainerFor(_data.get(idx));
        }
    }
}
