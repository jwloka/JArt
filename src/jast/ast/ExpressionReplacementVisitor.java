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
import jast.SyntaxException;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConditionalStatement;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.Expression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.Primary;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TrailingPrimary;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.ExpressionArray;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.visitor.EmptyVisitor;

// Replaces one specific expression with another one
public class ExpressionReplacementVisitor extends EmptyVisitor
{
    private Expression _originalChild;
    private Expression _newChild;

    private void checkConditionalStatement(ConditionalStatement stmt)
    {
        if (stmt.getCondition() == _originalChild)
        {
            try
            {
                stmt.setCondition(_newChild);
            }
            catch (SyntaxException ex)
            {}
        }
    }

    private void checkExpressionArray(ExpressionArray exprs)
    {
        if (exprs == null)
        {
            return;
        }

        int idx = 0;

        for (ExpressionIterator it = exprs.getIterator(); it.hasNext(); idx++)
        {
            if (it.getNext() == _originalChild)
            {
                exprs.set(idx, _newChild);
                return;
            }
        }
    }

    private void checkTrailingPrimary(TrailingPrimary primary)
    {
        if (primary.getBaseExpression() == _originalChild)
        {
            primary.setBaseExpression((Primary)_newChild);
        }
    }

    public void replace(Expression originalChild, Expression newChild)
    {
        _originalChild = originalChild;
        _newChild      = newChild;
        originalChild.getContainer().accept(this);
    }

    public void visitArgumentList(ArgumentList node)
    {
        checkExpressionArray(node.getArguments());
    }

    public void visitArrayAccess(ArrayAccess node)
    {
        if (node.getIndexExpression() == _originalChild)
        {
            node.setIndexExpression(_newChild);
        }
        checkTrailingPrimary(node);
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        checkExpressionArray(node.getDimExpressions());
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        if (node.getLeftHandSide() == _originalChild)
        {
            node.setLeftHandSide(_newChild);
        }
        else if (node.getValueExpression() == _originalChild)
        {
            node.setValueExpression(_newChild);
        }
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
        if (node.getLeftOperand() == _originalChild)
        {
            node.setLeftOperand(_newChild);
        }
        else if (node.getRightOperand() == _originalChild)
        {
            node.setRightOperand(_newChild);
        }
    }

    public void visitCaseBlock(CaseBlock node)
    {
        checkExpressionArray(node.getCases());
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
        if (node.getCondition() == _originalChild)
        {
            node.setCondition(_newChild);
        }
        else if (node.getTrueExpression() == _originalChild)
        {
            node.setTrueExpression(_newChild);
        }
        else if (node.getFalseExpression() == _originalChild)
        {
            node.setFalseExpression(_newChild);
        }
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        checkTrailingPrimary(node);
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        checkConditionalStatement(node);
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        if (node.getExpression() == _originalChild)
        {
            node.setExpression(_newChild);
        }
    }

    public void visitFieldAccess(FieldAccess node)
    {
        checkTrailingPrimary(node);
    }

    public void visitForStatement(ForStatement node)
    {
        checkConditionalStatement(node);
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        checkConditionalStatement(node);
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        if (node.getInnerExpression() == _originalChild)
        {
            node.setInnerExpression(_newChild);
        }
    }

    public void visitInstantiation(Instantiation node)
    {
        checkTrailingPrimary(node);
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        checkTrailingPrimary(node);
    }

    public void visitParenthesizedExpression(ParenthesizedExpression node)
    {
        if (node.getInnerExpression() == _originalChild)
        {
            node.setInnerExpression(_newChild);
        }
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        if (node.getInnerExpression() == _originalChild)
        {
            node.setInnerExpression((Primary)_newChild);
        }
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        if (node.getReturnValue() == _originalChild)
        {
            node.setReturnValue(_newChild);
        }
    }

    public void visitSelfAccess(SelfAccess node)
    {
        if (node.getTypeAccess() == _originalChild)
        {
            node.setTypeAccess((TypeAccess)_newChild);
        }
    }

    public void visitSingleInitializer(SingleInitializer node)
    {
        if (node.getInitEpression() == _originalChild)
        {
            node.setInitExpression(_newChild);
        }
    }

    public void visitStatementExpressionList(StatementExpressionList node)
    {
        checkExpressionArray(node.getExpressions());
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
        if (node.getSwitchExpression() == _originalChild)
        {
            node.setSwitchExpression(_newChild);
        }
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        if (node.getLockExpression() == _originalChild)
        {
            node.setLockExpression(_newChild);
        }
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        if (node.getThrowExpression() == _originalChild)
        {
            node.setThrowExpression(_newChild);
        }
    }

    public void visitTypeAccess(TypeAccess node)
    {
        checkTrailingPrimary(node);
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        if (node.getInnerExpression() == _originalChild)
        {
            node.setInnerExpression(_newChild);
        }
    }

    public void visitUnresolvedAccess(UnresolvedAccess node)
    {
        checkTrailingPrimary(node);
    }

    public void visitWhileStatement(WhileStatement node)
    {
        checkConditionalStatement(node);
    }
}
