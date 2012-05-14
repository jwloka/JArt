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
package jast.ast.visitor;

import jast.ast.Project;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Comment;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.Package;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.WhileStatement;

public class EmptyVisitor implements Visitor
{

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
    }

    public void visitArgumentList(ArgumentList node)
    {
    }

    public void visitArrayAccess(ArrayAccess node)
    {
    }

    public void visitArrayCreation(ArrayCreation node)
    {
    }

    public void visitArrayInitializer(ArrayInitializer node)
    {
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
    }

    public void visitBlock(Block node)
    {
    }

    public void visitBooleanLiteral(BooleanLiteral node)
    {
    }

    public void visitBreakStatement(BreakStatement node)
    {
    }

    public void visitCaseBlock(CaseBlock node)
    {
    }

    public void visitCatchClause(CatchClause node)
    {
    }

    public void visitCharacterLiteral(CharacterLiteral node)
    {
    }

    public void visitClassAccess(ClassAccess node)
    {
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
    }

    public void visitComment(Comment node)
    {
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
    }

    public void visitContinueStatement(ContinueStatement node)
    {
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
    }

    public void visitEmptyStatement(EmptyStatement node)
    {
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
    }

    public void visitFieldAccess(FieldAccess node)
    {
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
    }

    public void visitFloatingPointLiteral(FloatingPointLiteral node)
    {
    }

    public void visitFormalParameter(FormalParameter node)
    {
    }

    public void visitFormalParameterList(FormalParameterList node)
    {
    }

    public void visitForStatement(ForStatement node)
    {
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
    }

    public void visitImportDeclaration(ImportDeclaration node)
    {
    }

    public void visitInitializer(Initializer node)
    {
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
    }

    public void visitInstantiation(Instantiation node)
    {
    }

    public void visitIntegerLiteral(IntegerLiteral node)
    {
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
    }

    public void visitModifiers(Modifiers node)
    {
    }

    public void visitNullLiteral(NullLiteral node)
    {
    }

    public void visitPackage(Package node)
    {
    }

    public void visitParenthesizedExpression(ParenthesizedExpression node)
    {
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
    }

    public void visitPrimitiveType(PrimitiveType node)
    {
    }

    public void visitProject(Project node)
    {
    }

    public void visitReturnStatement(ReturnStatement node)
    {
    }

    public void visitSelfAccess(SelfAccess node)
    {
    }

    public void visitSingleInitializer(SingleInitializer node)
    {
    }

    public void visitStatementExpressionList(StatementExpressionList node)
    {
    }

    public void visitStringLiteral(StringLiteral node)
    {
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
    }

    public void visitThrowStatement(ThrowStatement node)
    {
    }

    public void visitTryStatement(TryStatement node)
    {
    }

    public void visitType(Type node)
    {
    }

    public void visitTypeAccess(TypeAccess node)
    {
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
    }

    public void visitUnresolvedAccess(UnresolvedAccess node)
    {
    }

    public void visitVariableAccess(VariableAccess node)
    {
    }

    public void visitWhileStatement(WhileStatement node)
    {
    }
}
