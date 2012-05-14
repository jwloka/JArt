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
package jast.profiler;
public class SizeInfo
{
    private String      _name;
    private DoubleArray _sizes   = new DoubleArray();
    private double      _minSize = 0l;
    private double      _maxSize = 0l;

    public SizeInfo(String name)
    {
        _name = name;
    }

    public void addSize(double size)
    {
        _sizes.add(size);
        if (size < _minSize)
        {
            _minSize = size;
        }
        else if (size > _maxSize)
        {
            _maxSize = size;
        }
    }

    public double getAverageSize()
    {
        return _sizes.isEmpty() ? 0.0 : getTotalSize() / (double)getNum();
    }

    public double getMaxSize()
    {
        return _maxSize;
    }

    public double getMinSize()
    {
        return _minSize;
    }

    public String getName()
    {
        return _name;
    }

    public int getNum()
    {
        return _sizes.getCount();
    }

    public double getTotalSize()
    {
        double sum = 0.0;

        for (int idx = 0; idx < _sizes.getCount(); idx++)
        {
            sum += _sizes.get(idx);
        }
        return sum;
    }
}
