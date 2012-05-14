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
import java.util.Collection;
import java.util.Vector;

public class StringArray implements Serializable
{
    private Vector _data  = new Vector();

    public StringArray()
    {
    }

    public StringArray getClone()
    {
        StringArray result = new StringArray();

        result._data = (Vector)_data.clone();
        return result;
    }

    public void add(String text)
    {
        if (text != null)
        {
            _data.addElement(text);
        }
    }

    // will only work in >= jdk1.2
    public void add(Collection elements)
    {
        _data.addAll(elements);
    }

    public String asString(String delimiter)
    {
        return asString(delimiter, getCount());
    }

    public String asString(String delimiter, int numParts)
    {
        if ((numParts <= 0) || (delimiter == null))
        {
            return "";
        }

        StringBuffer result = new StringBuffer();
        int          next   = getCount();

        if (numParts < next)
        {
            next = numParts;
        }
        for (int idx = 0; idx < next; idx++)
        {
            if (idx > 0)
            {
                result.append(delimiter);
            }
            result.append(get(idx));
        }
        return result.toString();
    }

    public void clear()
    {
        _data.removeAllElements();
    }

    public boolean contains(String text)
    {
        return _data.contains(text);
    }

    // Creates a string array from splitting the given source string
    // Useful for instance for package names or similar
    // Note that the array can contain empty strings which occur
    // if two delimters come one after the other without text inbetween
    public static StringArray fromString(String source, String delimiter)
    {
        StringArray result = new StringArray();

        if ((source == null)       || (delimiter == null) ||
            (source.length() == 0) || (delimiter.length() == 0))
        {
            return result;
        }

        String text = source;
        int    pos;

        while ((pos = text.indexOf(delimiter)) >= 0)
        {
            result.add(text.substring(0, pos));
            text = text.substring(pos+delimiter.length());
        }
        result.add(text);

        return result;
    }

    public String get(int idx)
    {
        return (String) _data.elementAt(idx);
    }

    public int getCount()
    {
        return _data.size();
    }

    public StringIterator getIterator()
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
                return get(curIdx++);
            }
        };
    }

    public String getLast()
    {
        return (String) _data.elementAt(_data.size() - 1);
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public void remove(int idx)
    {
        _data.removeElementAt(idx);
    }

    public void removeLast()
    {
        _data.removeElementAt(_data.size() - 1);
    }

    public void set(int idx, String text)
    {
        _data.setElementAt(text, idx);
    }

    public void sort()
    {
        sort(0, getCount()-1);
    }

    public void sort(int startIdx, int endIdx)
    {
        String curString;
        String otherString;

        for (int curStringIdx = startIdx; curStringIdx < endIdx; curStringIdx++)
        {
            curString = get(curStringIdx);
            for (int otherStringIdx = curStringIdx + 1; otherStringIdx <= endIdx; otherStringIdx++)
            {
                otherString = get(otherStringIdx);
                if (curString.compareTo(otherString) > 0)
                {
                    set(curStringIdx, otherString);
                    set(otherStringIdx, curString);
                    curString = otherString;
                }
            }
        }
    }
}
