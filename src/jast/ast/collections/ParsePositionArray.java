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
import jast.ast.ParsePosition;

import java.util.Vector;

public class ParsePositionArray
{
    private Vector _data  = new Vector();

    public ParsePositionArray()
    {
    }

    public void add(ParsePosition pos)
    {
        if (pos != null)
        {
            _data.addElement(pos);
        }
    }

    public void clear()
    {
        _data.removeAllElements();
    }

    public ParsePosition get(int idx)
    {
        return (ParsePosition)_data.elementAt(idx);
    }

    public int getCount()
    {
        return _data.size();
    }

    public ParsePositionIterator getIterator()
    {
        return new ParsePositionIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < getCount());
            }

            public ParsePosition getNext()
            {
                return get(curIdx++);
            }
        };
    }

    public boolean isEmpty()
    {
        return _data.isEmpty();
    }

    public void remove(int idx)
    {
        _data.removeElementAt(idx);
    }


    public ParsePosition getLast()
    {
        return (ParsePosition)_data.elementAt(_data.size() - 1);
    }



    public void removeLast()
    {
        _data.removeElementAt(_data.size() - 1);
    }



    public void set(int idx, ParsePosition pos)
    {
        if (pos != null)
        {
            while (getCount() <= idx)
            {
                add(new ParsePosition());
            }
            _data.setElementAt(pos, idx);
        }
    }
}
