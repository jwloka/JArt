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
package jart.analysis.duplication.collections;
import jart.analysis.duplication.StringPattern;

import java.util.Vector;


public class StringPatternArray
{
    private Vector _data  = new Vector();

    private int _minStartPos = Integer.MAX_VALUE;
    private int _maxEndPos   = 0;

    public StringPatternArray()
    {
    }

    public void add(StringPattern pattern)
    {
        if ((pattern != null) && (!isAlreadyFound(pattern)))
            {
            if (pattern.getStartPosition() < _minStartPos)
                {
                _minStartPos = pattern.getStartPosition();
            }
            if (pattern.getEndPosition() > _maxEndPos)
                {
                _maxEndPos = pattern.getEndPosition();
            }

            _data.addElement(pattern);
        }
    }

    public void clear()
    {
        _data.removeAllElements();
    }

    public boolean contains(StringPattern pattern)
    {
        return _data.contains(pattern);
    }

    public StringPattern get(int idx)
    {
        return (StringPattern) _data.elementAt(idx);
    }

    public int getCount()
    {
        return _data.size();
    }

    public StringPatternIterator getIterator()
    {
        return new StringPatternIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < getCount());
            }

            public StringPattern getNext()
            {
                return get(curIdx++);
            }
        };
    }

    public StringPattern getLast()
    {
        return (StringPattern) _data.elementAt(_data.size() - 1);
    }

    private boolean isAlreadyFound(StringPattern pattern)
    {
        if ((pattern.getStartPosition() >= _minStartPos)
            && (pattern.getEndPosition() <= _maxEndPos))
            {
            return true;
        }

        return false;
    }

    public void remove(int idx)
    {
        _data.removeElementAt(idx);
    }

    public void removeLast()
    {
        _data.removeElementAt(_data.size() - 1);
    }
}
