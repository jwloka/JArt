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
package jast.ast;
import jast.helpers.Comparable;

import java.io.Serializable;

public class ParsePosition implements Serializable, Comparable
{
    private int     _line     = -1;
    private int     _column   = -1;
    private int     _absolute = -1;
    private boolean _isSet    = false;

    public ParsePosition()
    {}

    public ParsePosition(int line, int column, int absolute)
    {
        set(line, column, absolute);
    }

    public ParsePosition(ParsePosition source)
    {
        copyFrom(source);
    }

    public void addColumn(int column)
    {
        _column   += column;
        _absolute += column;
        _isSet     = true;
    }

    public void copyFrom(ParsePosition source)
    {
        if (source != null)
        {
            _line     = source._line;
            _column   = source._column;
            _absolute = source._absolute;
            _isSet    = source._isSet;
        }
    }

    public boolean equals(Object other)
    {
        if ((other == null) || !(other instanceof ParsePosition))
        {
            return false;
        }
        else
        {
            return isEqual((ParsePosition)other);
        }
    }

    public int getAbsolute()
    {
        return _absolute;
    }

    public int getColumn()
    {
        return _column;
    }

    public int getLine()
    {
        return _line;
    }

    public void incColumn()
    {
        _column++;
        _absolute++;
        _isSet = true;
    }

    public void incLine()
    {
        _line++;
        _column = 1;
        _isSet  = true;
    }

    public boolean isEqual(int absolute)
    {
        return absolute == _absolute;
    }

    public boolean isEqual(int line, int column)
    {
        return (line == _line) && (column == _column);
    }

    public boolean isEqual(ParsePosition other)
    {
        return isEqual(other._line, other._column);
    }

    public boolean isLowerOrEqual(int absolute)
    {
        return absolute > _absolute;
    }

    public boolean isLowerOrEqual(int line, int column)
    {
        return (line > _line) || ((line == _line) && (column >= _column));
    }

    public boolean isLowerOrEqual(Comparable other)
    {
        ParsePosition otherPos = (ParsePosition)other;

        return isLowerOrEqual(otherPos._line, otherPos._column);
    }

    public void set(int line, int column, int absolute)
    {
        _line     = line;
        _column   = column;
        _absolute = absolute;
        _isSet    = true;
    }

    public void setIfFirstTime(int line, int column, int absolute)
    {
        if (!_isSet)
        {
            set(line, column, absolute);
        }
    }

    public void setIfFirstTime(ParsePosition source)
    {
        setIfFirstTime(source._line, source._column, source._absolute);
    }

    public String toString()
    {
        return _line+","+_column;
    }
}
