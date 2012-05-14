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
package jast.ast.nodes;
import jast.ast.ParsePosition;
import jast.ast.collections.ParsePositionArray;
import jast.ast.visitor.Visitor;
import jast.helpers.StringArray;

public class UnresolvedAccess extends TrailingPrimary
{
    private StringArray        _parts   = new StringArray();
    private ParsePositionArray _starts  = new ParsePositionArray();
    private ParsePositionArray _finishs = new ParsePositionArray();

    public UnresolvedAccess(Primary baseExpr)
    {
        setBaseExpression(baseExpr);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitUnresolvedAccess(this);
    }

    public void addPart(String part, ParsePosition start)
    {
        ParsePosition finish = new ParsePosition(start);
        int           pos    = _parts.getCount();

        finish.addColumn(part.length() - 1);
        _parts.add(part);
        _starts.set(pos, new ParsePosition(start));
        _finishs.set(pos, finish);
        setFinishPosition(finish);
    }

    public ParsePosition getFinishPosition(int idx)
    {
        return _finishs.get(idx);
    }

    public ParsePosition getLastFinishPosition()
    {
        return _finishs.getLast();
    }

    public ParsePosition getLastStartPosition()
    {
        return _starts.getLast();
    }

    public StringArray getParts()
    {
        return _parts;
    }

    public ParsePosition getStartPosition(int idx)
    {
        return _starts.get(idx);
    }

    public Type getType()
    {
        return null;
    }

    public void setStartPosition(ParsePosition pos)
    {
        super.setStartPosition(pos);
    }

    public String splitLastPart()
    {
        String part = _parts.getLast();

        _parts.removeLast();
        _starts.removeLast();
        _finishs.removeLast();
        return part;
    }

    public String toString()
    {
        return _parts.asString(".");
    }
}
