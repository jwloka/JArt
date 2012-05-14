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

import jast.SyntaxException;
import jast.ast.nodes.collections.LocalVariableArray;
import jast.ast.nodes.collections.LocalVariableArrayImpl;
import jast.ast.visitor.Visitor;

public class ForStatement extends LoopStatement
{
    private StatementExpressionList  _forInitList;
    private LocalVariableArray       _forInitDecls;
    private StatementExpressionList  _forUpdateList;

    public ForStatement(Expression               expr,
                        StatementExpressionList  forUpdateList,
                        Statement                loopStmt) throws SyntaxException
    {
        super(expr, true, loopStmt);
        setUpdateList(forUpdateList);
    }

    public ForStatement(StatementExpressionList forInitList,
                        Expression              expr,
                        StatementExpressionList forUpdateList,
                        Statement               loopStmt) throws SyntaxException
    {
        super(expr, true, loopStmt);
        setInitList(forInitList);
        setUpdateList(forUpdateList);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitForStatement(this);
    }

    public void addInitDeclaration(LocalVariableDeclaration decl)
    {
        if (decl != null)
        {
            if (_forInitDecls == null)
            {
                _forInitDecls = new LocalVariableArrayImpl();
                _forInitDecls.setOwner(this);
            }
            if (_forInitList != null)
            {
                _forInitList = null;
            }
            _forInitDecls.add(decl);
        }
    }

    public LocalVariableArray getInitDeclarations()
    {
        return _forInitDecls;
    }

    public StatementExpressionList getInitList()
    {
        return _forInitList;
    }

    public StatementExpressionList getUpdateList()
    {
        return _forUpdateList;
    }

    public boolean hasInitDeclarations()
    {
        return (_forInitDecls != null) && !_forInitDecls.isEmpty();
    }

    public boolean hasInitList()
    {
        return (_forInitList != null);
    }

    public boolean hasUpdateList()
    {
        return (_forUpdateList != null);
    }

    public void setInitList(StatementExpressionList exprList)
    {
        _forInitList = exprList;
        if (_forInitList != null)
        {
            _forInitDecls = null;
            _forInitList.setContainer(this);
        }
    }

    public void setUpdateList(StatementExpressionList exprList)
    {
        _forUpdateList = exprList;
        if (_forUpdateList != null)
        {
            _forUpdateList.setContainer(this);
        }
    }
}
