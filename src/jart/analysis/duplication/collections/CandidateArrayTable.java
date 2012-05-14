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
import jast.helpers.StringArray;
import jast.helpers.StringIterator;

import java.util.Hashtable;


public class CandidateArrayTable
{
    private Hashtable _data = new Hashtable();

    public CandidateArrayTable()
    {
        super();
    }

    public void add(Candidate item)
    {
        if (!contains(item.getMatch()))
            {
            _data.put(item.getMatch(), new CandidateArray());
        }

        get(item.getMatch()).add(item);
    }

    public boolean contains(String match)
    {
        return _data.containsKey(match);
    }

    public CandidateArray get(String match)
    {
        return (CandidateArray) _data.get(match);
    }

    public int getCount()
    {
        return _data.size();
    }

    public CandidateArrayIterator getIterator()
    {
        return new CandidateArrayIterator()
        {
            private StringIterator _matches = getMatches().getIterator();

            public boolean hasNext()
            {
                return _matches.hasNext();
            }

            public CandidateArray getNext()
            {
                return get(_matches.getNext());
            }
        };
    }

    public StringArray getMatches()
    {
        StringArray matches = new StringArray();
        matches.add(_data.keySet());

        return matches;
    }
}
