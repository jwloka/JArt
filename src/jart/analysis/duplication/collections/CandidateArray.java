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
import jart.analysis.duplication.Candidate;

import java.util.Vector;


public class CandidateArray
{
    private Vector _data = new Vector();

    public CandidateArray()
    {
        super();
    }

    public void add(Candidate cand)
    {
        if (!contains(cand))
            {
            _data.add(cand);
        }
    }

    public boolean contains(Candidate cand)
    {
        if (_data.contains(cand))
            {
            return true;
        }

        return false;
    }

    public Candidate get(int idx)
    {
        return (Candidate) _data.get(idx);
    }

    public int getCount()
    {
        return _data.size();
    }

    public CandidateIterator getIterator()
    {
        return new CandidateIterator()
        {
            private int _curIndex = 0;

            public boolean hasNext()
            {
                return (_curIndex < getCount());
            }

            public Candidate getNext()
            {
                return get(_curIndex++);
            }
        };
    }

    public void remove(int idx)
    {
        _data.remove(idx);
    }
}
