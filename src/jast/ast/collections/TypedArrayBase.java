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

import java.util.Enumeration;
import java.util.Vector;

public abstract class TypedArrayBase extends    NodeContainerBase
                                     implements TypedArray
{
    private Vector _data = new Vector();

    protected void append(Object obj)
    {
        if (obj instanceof Contained)
        {
            notifyOwner(new AdditionEvent((Contained)obj, this));
        }
        setContainerFor(obj);
        _data.addElement(obj);
    }

    protected Object at(int idx)
    {
        return _data.elementAt(idx);
    }

    protected boolean contains(Object obj)
    {
        return _data.contains(obj);
    }

    public void clear()
    {
        if (!_data.isEmpty())
        {
            notifyOwner(new ClearEvent(this));
            _data.removeAllElements();
        }
    }

    protected void copyFrom(TypedArrayBase source, boolean clearBefore)
    {
        if (clearBefore)
        {
            clear();
        }
        if ((source != null) && !source.isEmpty())
        {
            Object obj;

            for (Enumeration en = source._data.elements(); en.hasMoreElements();)
            {
                append(en.nextElement());
            }
        }
    }

    public int getCount()
    {
        return _data.size();
    }

    protected void insert(Object obj, int pos)
    {
        if (obj instanceof Contained)
        {
            notifyOwner(new AdditionEvent((Contained)obj, this));
        }
        setContainerFor(obj);
        if (pos >= _data.size())
        {
            _data.addElement(obj);
        }
        else
        {
            _data.insertElementAt(obj,
                                  pos < 0 ? 0 : pos);
        }
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public void remove(int idx)
    {
        if (_data.elementAt(idx) instanceof Contained)
        {
            notifyOwner(new RemovalEvent((Contained)_data.elementAt(idx), this));
        }
        _data.removeElementAt(idx);
    }

    protected void set(int idx, Object obj)
    {
        if (obj instanceof Contained)
        {
            notifyOwner(new AssignmentEvent((Contained)obj, idx, this));
        }
        setContainerFor(obj);
        _data.setElementAt(obj, idx);
    }

    public void setOwner(Node owner)
    {
        super.setOwner(owner);
        for (int idx = 0; idx < _data.size(); idx++)
        {
            setContainerFor(_data.elementAt(idx));
        }
    }
}
