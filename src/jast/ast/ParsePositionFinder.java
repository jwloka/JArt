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
import jast.ast.nodes.CompilationUnit;
import jast.ast.visitor.DescendingReflectionVisitor;

public class ParsePositionFinder extends DescendingReflectionVisitor
{
    private Node _found    = null;
    private int  _absolute = -1;
    private int  _line     = -1;
    private int  _column   = -1;

    public ParsePositionFinder()
    {
        warnIfHandlerMissing(false);
    }

    public Node getNodeAt(CompilationUnit unit, int absolute)
    {
        _found    = null;
        _absolute = absolute;
        _line     = -1;
        _column   = -1;

        visit(unit);
        return _found;
    }

    public Node getNodeAt(CompilationUnit unit, int line, int column)
    {
        _found    = null;
        _absolute = -1;
        _line     = line;
        _column   = column;

        visit(unit);
        return _found;
    }

    public void preVisit(Object obj)
    {
        if (!(obj instanceof Node))
        {
            return;
        }

        Node          node      = (Node)obj;
        ParsePosition startPos  = node.getStartPosition();
        ParsePosition finishPos = node.getFinishPosition();

        if ((startPos == null) || (finishPos == null))
        {
            return;
        }
        // is the position within this node ?
        if (_absolute >= 0)
        {
            if (!startPos.isLowerOrEqual(_absolute) ||
                (finishPos.isLowerOrEqual(_absolute) && !finishPos.isEqual(_absolute)))
            {
                return;
            }
        }
        else
        {
            if (!startPos.isLowerOrEqual(_line, _column) ||
                (finishPos.isLowerOrEqual(_line, _column) && !finishPos.isEqual(_line, _column)))
            {
                return;
            }
        }
        // yes, so check whether this is a better hit if one have been found already
        if (_found != null)
        {
            if ((startPos.isLowerOrEqual(_found.getStartPosition()) &&
                 !startPos.isEqual(_found.getStartPosition())) ||
                !finishPos.isLowerOrEqual(_found.getFinishPosition()))
            {
                return;
            }
        }
        _found = node;
    }
}
