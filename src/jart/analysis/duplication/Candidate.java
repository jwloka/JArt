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


import jast.ast.nodes.InvocableDeclaration;

public class Candidate
{
    private String                  _match;
    private InvocableDeclaration _node;
    private int                  _startPos;

    public Candidate(String match, InvocableDeclaration node, int startPos)
    {
        _match = match;
        _node = node;
        _startPos = startPos;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            {
            return false;
        }

        if (!(obj instanceof Candidate))
            {
            return false;
        }

        Candidate cand = (Candidate) obj;

        if (getMatch().equals(cand.getMatch())
            && (getStartPos() == cand.getStartPos())
            && getNode().equals(cand.getNode()))
            {
            return true;
        }

        return false;
    }

    public String getMatch()
    {
        return _match;
    }

    public InvocableDeclaration getNode()
    {
        return _node;
    }

    public int getStartPos()
    {
        return _startPos;
    }
}
