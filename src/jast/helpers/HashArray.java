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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class HashArray implements Serializable
{
    private Vector    _orderedObjs = new Vector();
    private Vector    _orderedKeys = new Vector();
    private Hashtable _keyedData   = new Hashtable();

    public void add(String key, Object obj)
    {
        remove(key);
        _orderedKeys.addElement(key);
        _orderedObjs.addElement(obj);
        _keyedData.put(key, obj);
    }

    public void changeKey(String oldKey, String newKey)
    {
        remove(newKey);

        int idx = _orderedKeys.indexOf(oldKey);

        if (idx >= 0)
        {
            _orderedKeys.setElementAt(newKey, idx);
            _keyedData.put(newKey, _keyedData.get(oldKey));
            _keyedData.remove(oldKey);
        }
    }

    public void clear()
    {
        _keyedData.clear();
        _orderedKeys.removeAllElements();
        _orderedObjs.removeAllElements();
    }

    public boolean contains(String key)
    {
        return _keyedData.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return _keyedData.contains(value);
    }

    public Object get(int idx)
    {
        return _orderedObjs.elementAt(idx);
    }

    public Object get(String qualifiedName)
    {
        return _keyedData.get(qualifiedName);
    }

    public int getCount()
    {
        return _orderedObjs.size();
    }

    public Enumeration getElements()
    {
        return _orderedObjs.elements();
    }

    public String getKey(int idx)
    {
        return (String)_orderedKeys.elementAt(idx);
    }

    public StringIterator getKeys()
    {
        return new StringIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < getCount());
            }

            public String getNext()
            {
                return getKey(curIdx++);
            }
        };
    }

    public void insert(String key, Object obj, int pos)
    {
        remove(key);
        _orderedKeys.insertElementAt(key, pos);
        _orderedObjs.insertElementAt(obj, pos);
        _keyedData.put(key, obj);
    }

    public boolean isEmpty()
    {
        return _orderedObjs.isEmpty();
    }

    public void remove(int idx)
    {
        _keyedData.remove(_orderedKeys.elementAt(idx));
        _orderedKeys.removeElementAt(idx);
        _orderedObjs.removeElementAt(idx);
    }

    public void remove(String key)
    {
        int idx = _orderedKeys.indexOf(key);

        if (idx >= 0)
        {
            _orderedKeys.removeElementAt(idx);
            _orderedObjs.removeElementAt(idx);
            _keyedData.remove(key);
        }
    }


    public ObjectIterator getObjects()
    {
        return new ObjectIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < getCount());
            }

            public Object getNext()
            {
                return get(curIdx++);
            }
        };
    }



    public void sort()
    {
        sort(0, getCount()-1);
    }



    public void sort(int startIdx, int endIdx)
    {
        String curKey;
        String otherKey;

        for (int curKeyIdx = startIdx; curKeyIdx <= endIdx-1; curKeyIdx++)
        {
            curKey = getKey(curKeyIdx);
            for (int otherKeyIdx = curKeyIdx + 1; otherKeyIdx <= endIdx; otherKeyIdx++)
            {
                otherKey = getKey(otherKeyIdx);
                if (otherKey.endsWith("*") ||
                    (curKey.compareTo(otherKey) > 0))
                {
                    swap(curKeyIdx, otherKeyIdx);
                    curKey = otherKey;
                }
            }
        }
    }



    public void swap(int pos1, int pos2)
    {
        String key1 = getKey(pos1);
        String key2 = getKey(pos2);
        Object obj1 = get(pos1);
        Object obj2 = get(pos2);

        _orderedKeys.setElementAt(key1, pos2);
        _orderedKeys.setElementAt(key2, pos1);
        _orderedObjs.setElementAt(obj1, pos2);
        _orderedObjs.setElementAt(obj2, pos1);
    }
}
