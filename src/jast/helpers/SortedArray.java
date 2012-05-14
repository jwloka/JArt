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
package jast.helpers;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

// Note that the hashcode of the comparables is used - so don't
// use the same object more than once
public class SortedArray implements Serializable
{
    private Vector    _keys = new Vector();
    private Hashtable _data = new Hashtable();

    public void add(Comparable key, Object obj)
    {
        if (!_keys.isEmpty())
        {
            Comparable curKey;

            for (int idx = 0; idx < _keys.size(); idx++)
            {
                curKey = (Comparable)_keys.elementAt(idx);
                if (!curKey.isLowerOrEqual(key))
                {
                    _keys.insertElementAt(key, idx);
                    _data.put(key, obj);
                    return;
                }
            }
        }
        _keys.addElement(key);
        _data.put(key, obj);
    }

    public void clear()
    {
        _keys.removeAllElements();
        _data.clear();
    }

    public Object get(int idx)
    {
        return _data.get((Comparable)_keys.elementAt(idx));
    }

    public int getCount()
    {
        return _keys.size();
    }

    public ObjectIterator getIterator()
    {
        return new ObjectIterator()
        {
            private int _curIdx = 0;

            public boolean hasNext()
            {
                return (_curIdx < _keys.size());
            }

            public Object getNext()
            {
                return _data.get(_keys.elementAt(_curIdx++));
            }
        };
    }

    public Comparable getKey(int idx)
    {
        return (Comparable)_keys.elementAt(idx);
    }

    public boolean isEmpty()
    {
        return _keys.isEmpty();
    }

    public void remove(int idx)
    {
        _data.remove(_keys.elementAt(idx));
        _keys.removeElementAt(idx);
    }
}
