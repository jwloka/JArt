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
package jast.analysis;
import jast.ast.Project;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import jast.ast.visitor.DescendingVisitor;


public class StatementCounter extends DescendingVisitor
{
    private int  _count = 0;

    public StatementCounter(Block node)
    {
        node.accept(this);
    }

    public StatementCounter(ClassDeclaration node)
    {
        node.accept(this);
    }

    public StatementCounter(CompilationUnit node)
    {
        node.accept(this);
    }

    public StatementCounter(Initializer node)
    {
        node.accept(this);
    }

    public StatementCounter(InvocableDeclaration node)
    {
        node.accept(this);
    }

    public StatementCounter(Project node)
    {
        node.accept(this);
    }

    public int getCount()
    {
        return _count;
    }

    public void visitBlock(Block node)
    {
        _count++;
        super.visitBlock(node);
    }

    public void visitBreakStatement(BreakStatement node)
    {
        _count++;
        super.visitBreakStatement(node);
    }

    public void visitContinueStatement(ContinueStatement node)
    {
        _count++;
        super.visitContinueStatement(node);
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        _count++;
        super.visitDoWhileStatement(node);
    }

    public void visitEmptyStatement(EmptyStatement node)
    {
        _count++;
        super.visitEmptyStatement(node);
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        _count++;
        super.visitExpressionStatement(node);
    }

    public void visitForStatement(ForStatement node)
    {
        _count++;
        super.visitForStatement(node);
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        _count++;
        super.visitIfThenElseStatement(node);
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
        _count++;
        super.visitLabeledStatement(node);
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        _count++;
        super.visitReturnStatement(node);
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        _count++;
        super.visitSynchronizedStatement(node);
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        _count++;
        super.visitThrowStatement(node);
    }

    public void visitTryStatement(TryStatement node)
    {
        _count++;
        super.visitTryStatement(node);
    }

    public void visitWhileStatement(WhileStatement node)
    {
        _count++;
        super.visitWhileStatement(node);
    }
}
