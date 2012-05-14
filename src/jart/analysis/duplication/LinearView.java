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

import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import jast.ast.visitor.DescendingVisitor;

public class LinearView extends DescendingVisitor
{
    public static final String PROPERTY_LABEL = "LinearView";
    public static final int    BYTE_COUNT      = 2;

    public static final String ABRV_CLASS_DECL_OPEN         = "C{";
    public static final String ABRV_CLASS_DECL_CLOSE         = "C}";
    public static final String ABRV_ANNON_CLASS_DECL_OPEN     = "A{";
    public static final String ABRV_ANNON_CLASS_DECL_CLOSE     = "A}";
    public static final String ABRV_BLOCK_STATEMENT_OPEN     = "B{";
    public static final String ABRV_BLOCK_STATEMENT_CLOSE     = "B}";

    public static final String ABRV_LOCAL_VARIABLE_DECL        = "VD";
    public static final String ABRV_BREAK_STATEMENT         = "BS";
    public static final String ABRV_CONTINUE_STATEMENT         = "CS";
    public static final String ABRV_DO_STATEMENT_OPEN         = "D[";
    public static final String ABRV_DO_STATEMENT_CLOSE         = "D]";
    public static final String ABRV_EMPTY_STATEMENT         = "NS";
    public static final String ABRV_EXPRESSION_STATEMENT     = "ES";
    public static final String ABRV_FOR_STATEMENT_OPEN         = "F[";
    public static final String ABRV_FOR_STATEMENT_CLOSE     = "F]";
    public static final String ABRV_IF_STATEMENT_OPEN         = "I[";
    public static final String ABRV_ELSE_PART                 = "][";
    public static final String ABRV_IF_STATEMENT_CLOSE         = "I]";
    public static final String ABRV_RETURN_STATEMENT         = "RS";
    public static final String ABRV_SWITCH_STATEMENT_OPEN     = "S[";
    public static final String ABRV_CASE_BRANCH             = "cS";
    public static final String ABRV_CASE_BRANCH_OPEN         = "c[";
    public static final String ABRV_CASE_BRANCH_CLOSE         = "c]";
    public static final String ABRV_SWITCH_STATEMENT_CLOSE  = "S]";
    public static final String ABRV_SYNCHRONIZED_STATEMENT  = "SS";
    public static final String ABRV_THROW_STATEMENT         = "TS";
    public static final String ABRV_TRY_STATEMENT_OPEN         = "T[";
    public static final String ABRV_CATCH_CLAUSE_OPEN         = "e[";
    public static final String ABRV_CATCH_CLAUSE_CLOSE         = "e]";
    public static final String ABRV_FINALLY_OPEN             = "f[";
    public static final String ABRV_FINALLY_CLOSE             = "f]";
    public static final String ABRV_TRY_STATEMENT_CLOSE     = "T]";
    public static final String ABRV_WHILE_STATEMENT_OPEN     = "W[";
    public static final String ABRV_WHILE_STATEMENT_CLOSE     = "W]";

    private StringBuffer _view            = new StringBuffer();
    private int          _abortIndex;
    private Node          _currentNode;

    private boolean _isCreatingView = true;

    public LinearView()
    {
    }

    private void addAbbreviation(String abrv)
    {
        _view.append(abrv);
        _abortIndex -= BYTE_COUNT;
    }

    public Node getNode(InvocableDeclaration meth, int linearViewPos)
    {
        _abortIndex = linearViewPos;
        _currentNode = null;
        _isCreatingView = false;

        visitInvocableDeclaration(meth);

        _isCreatingView = true;

        return _currentNode;
    }

    public String getView()
    {
        return _view.toString();
    }

    private boolean isAbortIndexReached(Node curNode)
    {
        if (_isCreatingView)
            {
            return false;
        }

        if (_abortIndex <= 0)
            {
            if (_currentNode == null)
                {
                _currentNode = curNode;
            }
            return true;
        }

        return false;
    }

    private static boolean isContainedByInvocableDeclaration(Node node)
    {
        if (!(node instanceof ContainedNode))
            {
            return false;
        }

        ContainedNode contained = (ContainedNode) node;
        Node container = contained.getContainer();

        while (container != null)
            {
            if (container instanceof InvocableDeclaration)
                {
                return true;
            }

            if (container instanceof ContainedNode)
                {
                container = ((ContainedNode) container).getContainer();
            }
            else
                {
                return false;
            }
        }

        return false;
    }

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        if (isContainedByInvocableDeclaration(node))
            {
            addAbbreviation(ABRV_ANNON_CLASS_DECL_OPEN);
        }

        super.visitAnonymousClassDeclaration(node);

        if (isContainedByInvocableDeclaration(node))
            {
            addAbbreviation(ABRV_ANNON_CLASS_DECL_CLOSE);
        }
    }

    public void visitBlock(Block node)
    {
        // =!= causes errors for InvocableDeclarations, For, If, etc.

        //if (isAbortIndexReached(node))
        //{
        //return;
        //}

        //if (isContainedByInvocableDeclaration(node))
        //{
        //addAbbreviation(ABRV_BLOCK_STATEMENT_OPEN);
        //}

        super.visitBlock(node);

        //if (isContainedByInvocableDeclaration(node))
        //{
        //addAbbreviation(ABRV_BLOCK_STATEMENT_CLOSE);
        //}
    }

    public void visitBreakStatement(BreakStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_BREAK_STATEMENT);

        super.visitBreakStatement(node);
    }

    public void visitCaseBlock(CaseBlock node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        if (node.getBlockStatements().isEmpty())
            {
            addAbbreviation(ABRV_CASE_BRANCH);
        }
        else
            {
            addAbbreviation(ABRV_CASE_BRANCH_OPEN);

            super.visitCaseBlock(node);

            addAbbreviation(ABRV_CASE_BRANCH_CLOSE);
        }
    }

    public void visitCatchClause(CatchClause node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_CATCH_CLAUSE_OPEN);

        super.visitCatchClause(node);

        addAbbreviation(ABRV_CATCH_CLAUSE_CLOSE);
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        if (isContainedByInvocableDeclaration(node))
            {
            addAbbreviation(ABRV_CLASS_DECL_OPEN);
        }

        super.visitClassDeclaration(node);

        if (isContainedByInvocableDeclaration(node))
            {
            addAbbreviation(ABRV_CLASS_DECL_CLOSE);
        }
    }

    public void visitContinueStatement(ContinueStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_CONTINUE_STATEMENT);

        super.visitContinueStatement(node);
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_DO_STATEMENT_OPEN);

        super.visitDoWhileStatement(node);

        addAbbreviation(ABRV_DO_STATEMENT_CLOSE);
    }

    public void visitEmptyStatement(EmptyStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_EMPTY_STATEMENT);

        super.visitEmptyStatement(node);
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_EXPRESSION_STATEMENT);

        super.visitExpressionStatement(node);
    }

    public void visitForStatement(ForStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_FOR_STATEMENT_OPEN);

        super.visitForStatement(node);

        addAbbreviation(ABRV_FOR_STATEMENT_CLOSE);
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        node.getCondition().accept(this);

        addAbbreviation(ABRV_IF_STATEMENT_OPEN);

        node.getTrueStatement().accept(this);

        if (node.getFalseStatement() != null)
            {
            addAbbreviation(ABRV_ELSE_PART);

            node.getFalseStatement().accept(this);
        }

        addAbbreviation(ABRV_IF_STATEMENT_CLOSE);
    }

    public void visitInvocableDeclaration(InvocableDeclaration node)
    {
        _view = new StringBuffer();

        if (node instanceof MethodDeclaration)
            {
            super.visitMethodDeclaration((MethodDeclaration) node);
        }
        else
            {
            super.visitConstructorDeclaration((ConstructorDeclaration) node);
        }
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_LOCAL_VARIABLE_DECL);

        super.visitLocalVariableDeclaration(node);
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_RETURN_STATEMENT);

        super.visitReturnStatement(node);
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_SWITCH_STATEMENT_OPEN);

        super.visitSwitchStatement(node);

        addAbbreviation(ABRV_SWITCH_STATEMENT_CLOSE);
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_SYNCHRONIZED_STATEMENT);

        super.visitSynchronizedStatement(node);
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_THROW_STATEMENT);

        super.visitThrowStatement(node);
    }

    public void visitTryStatement(TryStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_TRY_STATEMENT_OPEN);

        super.visitTryStatement(node);

        addAbbreviation(ABRV_TRY_STATEMENT_CLOSE);

        if (node.hasFinallyClause())
            {
            addAbbreviation(ABRV_FINALLY_OPEN);

            super.visitBlock(node.getFinallyClause());

            addAbbreviation(ABRV_FINALLY_CLOSE);
        }
    }

    public void visitWhileStatement(WhileStatement node)
    {
        if (isAbortIndexReached(node))
            {
            return;
        }

        addAbbreviation(ABRV_WHILE_STATEMENT_OPEN);

        super.visitWhileStatement(node);

        addAbbreviation(ABRV_WHILE_STATEMENT_CLOSE);
    }
}
