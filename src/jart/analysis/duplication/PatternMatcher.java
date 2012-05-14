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
package jart.analysis.duplication;
import jart.analysis.duplication.collections.StringPatternArray;


public class PatternMatcher
{
    private int                _matchByteCount = 1;
    private String                _text;
    private String                _pattern;
    private String                _completeMatch;
    private StringPatternArray _matches;

    public PatternMatcher()
    {
        super();
    }

    private String addFollowingChar(String subPattern, int startPos)
        throws IndexOutOfBoundsException
    {
        return _pattern.substring(
            startPos,
            startPos + subPattern.length() + _matchByteCount);
    }

    private boolean contains(String pattern)
    {
        if (_text.indexOf(pattern) > -1)
            {
            return true;
        }

        return false;
    }

    public void findMatches(String text, int minMatchLength)
        throws IllegalArgumentException
    {
        _text = text;
        _matches = new StringPatternArray();

        if (minMatchLength > _pattern.length())
            {
            //throw new IllegalArgumentException("Desired match length is longer as given pattern!");
            return;
        }

        for (int idx = 0;
            idx + minMatchLength <= _pattern.length();
            idx += _matchByteCount)
            {
            String currentSubPattern = _pattern.substring(idx, idx + minMatchLength);

            if (contains(currentSubPattern))
                {
                getCompleteMatch(currentSubPattern, idx);

                int startPos = _text.indexOf(_completeMatch);
                int endPos = startPos + _completeMatch.length();

                _matches.add(new StringPattern(_completeMatch, startPos, endPos));
            }
        }
    }

    private void getCompleteMatch(String subPattern, int idx)
    {
        _completeMatch = subPattern;

        try
            {
            String expandedSubPattern = addFollowingChar(subPattern, idx);

            if (contains(expandedSubPattern))
                {
                getCompleteMatch(expandedSubPattern, idx);
            }
        }
        catch (IndexOutOfBoundsException ignored)
            {
        }
    }

    public int getIndexOfPattern(String subPattern)
    {
        if ((subPattern == null) || (subPattern.length() == 0))
            {
            return -1;
        }

        return _pattern.indexOf(subPattern);
    }

    public int getMatchByteCount()
    {
        return _matchByteCount;
    }

    public StringPatternArray getMatches()
    {
        return _matches;
    }

    public String getPattern()
    {
        return _pattern;
    }

    public void setMatchByteCount(int count)
    {
        _matchByteCount = count;
    }

    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }

    private void setText(String text)
    {
        _text = text;
    }
}
