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
import jast.ast.ContainerEvent;
import jast.ast.collections.AdditionEvent;
import jast.ast.nodes.collections.CaseBlockArray;
import jast.ast.nodes.collections.CaseBlockArrayImpl;
import jast.ast.visitor.Visitor;

public class SwitchStatement extends Statement
{
    private Expression     _expr;
    private CaseBlockArray _caseBlocks = new CaseBlockArrayImpl();
    private boolean        _hasDefault = false;

    public SwitchStatement(Expression expr)
    {
        _caseBlocks.setOwner(this);
        setSwitchExpression(expr);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitSwitchStatement(this);
    }

    public CaseBlockArray getCaseBlocks()
    {
        return _caseBlocks;
    }

    public Expression getSwitchExpression()
    {
        return _expr;
    }

    public boolean hasDefault()
    {
        return _hasDefault;
    }

    public void notify(ContainerEvent event)
    {
        if (event instanceof AdditionEvent)
        {
            CaseBlock newBlock = (CaseBlock)event.getObject();

            _hasDefault = _hasDefault || newBlock.hasDefault();
        }
    }

    public void setSwitchExpression(Expression expr)
    {
        _expr = expr;
        _expr.setContainer(this);
    }
}
