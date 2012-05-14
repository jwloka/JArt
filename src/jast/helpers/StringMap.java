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

public class StringMap implements Serializable
{
    private Hashtable _data  = new Hashtable();

    public StringMap()
    {
    }

    public void clear()
    {
        _data.clear();
    }

    public boolean contains(String key)
    {
        return _data.containsKey(key);
    }

    public boolean containsValue(String value)
    {
        return _data.contains(value);
    }

    public String get(String key)
    {
        return (String)_data.get(key);
    }

    public int getCount()
    {
        return _data.size();
    }

    public StringIterator getIterator()
    {
        return new StringIterator()
        {
            private Enumeration en = _data.elements();

            public boolean hasNext()
            {
                return en.hasMoreElements();
            }

            public String getNext()
            {
                return (String)en.nextElement();
            }
        };
    }

    public StringIterator getKeyIterator()
    {
        return new StringIterator()
        {
            private Enumeration en = _data.keys();

            public boolean hasNext()
            {
                return en.hasMoreElements();
            }

            public String getNext()
            {
                return (String)en.nextElement();
            }
        };
    }

    public void remove(String key)
    {
        _data.remove(key);
    }

    public void set(String key, String value)
    {
        _data.put(key, value);
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();
        String       key;

        result.append("[");
        for (StringIterator it = getKeyIterator(); it.hasNext();)
        {
            key = it.getNext();
            result.append(key);
            result.append("=\"");
            result.append(get(key));
            result.append("\"");
            if (it.hasNext())
            {
                result.append(", ");
            }
        }
        result.append("]");

        return result.toString();
    }
}
