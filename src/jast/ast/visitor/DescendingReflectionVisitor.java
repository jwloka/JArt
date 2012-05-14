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
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.CaseBlockIterator;
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.ImportIterator;
import jast.ast.nodes.collections.InitializerIterator;
import jast.ast.nodes.collections.LocalVariableIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.nodes.collections.VariableInitializerIterator;

public class DescendingReflectionVisitor extends ReflectionVisitor
{

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
        if (node.getBaseClass() != null)
        {
            visit(node.getBaseClass());
        }

        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (InitializerIterator iter = node.getInitializers().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitArgumentList(ArgumentList node)
    {
        for (ExpressionIterator it = node.getArguments().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitArrayAccess(ArrayAccess node)
    {
        // visit expression
        visit(node.getBaseExpression());
        visit(node.getIndexExpression());
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        // visit expressions
        for (ExpressionIterator it = node.getDimExpressions().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }

        // visit initializer
        if (node.isInitialized())
        {
            visit(node.getInitializer());
        }
    }

    public void visitArrayInitializer(ArrayInitializer node)
    {
        // visit variable initializer
        for (VariableInitializerIterator it = node.getInitializers().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        // visit expression
        visit(node.getLeftHandSide());
        visit(node.getValueExpression());
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
        // visit expression
        visit(node.getLeftOperand());
        visit(node.getRightOperand());
    }

    public void visitBlock(Block node)
    {
        for (BlockStatementIterator it = node.getBlockStatements().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitCaseBlock(CaseBlock node)
    {
        for (ExpressionIterator exprIt = node.getCases().getIterator(); exprIt.hasNext();)
        {
            visit(exprIt.getNext());
        }

        for (BlockStatementIterator stmtIt = node.getBlockStatements().getIterator(); stmtIt.hasNext();)
        {
            visit(stmtIt.getNext());
        }
    }

    public void visitCatchClause(CatchClause node)
    {
        // visit formal parameter
        visit(node.getFormalParameter());

        // visit block
        visit(node.getCatchBlock());
    }

    public void visitClassAccess(ClassAccess node)
    {
        if (node.isQualified())
        {
            visit(node.getReferencedType());
        }
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        // visit modifiers
        visit(node.getModifiers());
        if (node.getBaseClass() != null)
        {
            // visit extended class (type)
            visit(node.getBaseClass());
        }

        // visit implemented interfaces (types)
        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit inner classes (types)
        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit field declarations
        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit initializers
        for (InitializerIterator iter = node.getInitializers().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit constructor declarations
        for (ConstructorIterator iter = node.getConstructors().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit method declarations
        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        // visit package
        visit(node.getPackage());

        // visit imports
        for (ImportIterator iter = node.getImportDeclarations().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit interfaces and Classes
        for (TypeDeclarationIterator iter = node.getTypeDeclarations().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
        // visit expression
        visit(node.getCondition());
        visit(node.getTrueExpression());
        visit(node.getFalseExpression());
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        visit(node.getModifiers());

        if (node.hasParameters())
        {
            visit(node.getParameterList());
        }

        for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }

        if (node.hasBody())
        {
            visit(node.getBody());
        }
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }
        if (node.hasArguments())
        {
            visit(node.getArgumentList());
        }
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        // visit statement
        visit(node.getLoopStatement());

        // visit expression
        visit(node.getCondition());
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        // visit expression
        visit(node.getExpression());
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.isTrailing())
        {
            // visit expression
            visit(node.getBaseExpression());
        }
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        if (node.getInitializer() != null)
        {
            // visit initializer
            visit(node.getInitializer());
        }
    }

    public void visitFormalParameter(FormalParameter node)
    {
        // visit type
        visit(node.getType());
    }

    public void visitFormalParameterList(FormalParameterList node)
    {
        for (FormalParameterIterator it = node.getParameters().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitForStatement(ForStatement node)
    {
        if (node.hasInitDeclarations())
        {
            // visit local variable declaration
            for (LocalVariableIterator it = node.getInitDeclarations().getIterator(); it.hasNext();)
            {
                visit(it.getNext());
            }
        }
        else if (node.getInitList() != null)
        {
            // visit statement expression list
            visit(node.getInitList());
        }
        if (node.getCondition() != null)
        {
            // visit expression
            visit(node.getCondition());
        }
        if (node.getUpdateList() != null)
        {
            // visit statement expression list
            visit(node.getUpdateList());
        }

        // visit statement
        visit(node.getLoopStatement());
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        // visit expression
        visit(node.getCondition());

        // visit statement
        visit(node.getTrueStatement());
        if (node.hasElse())
        {
            // visit statement
            visit(node.getFalseStatement());
        }
    }

    public void visitInitializer(Initializer node)
    {
        // visit block
        visit(node.getBody());
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        // visit expression
        visit(node.getInnerExpression());

        // visit type
        visit(node.getReferencedType());
    }

    public void visitInstantiation(Instantiation node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }

        visit(node.getInstantiatedType());

        if (node.hasArguments())
        {
            visit(node.getArgumentList());
        }

        if (node.withAnonymousClass())
        {
            visit(node.getAnonymousClass());
        }
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        // visit modifiers
        visit(node.getModifiers());

        // visit implemented interfaces (types)
        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit inner classes (types)
        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit field declarations
        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        // visit methods declarations
        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
        // visit statement
        visit(node.getStatement());
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        if (node.getInitializer() != null)
        {
            // visit initializer
            visit(node.getInitializer());
        }
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        visit(node.getModifiers());

        if (node.getReturnType() != null)
        {
            visit(node.getReturnType());
        }

        if (node.hasParameters())
        {
            visit(node.getParameterList());
        }

        for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }

        if (node.hasBody())
        {
            visit(node.getBody());
        }
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }
        if (node.hasArguments())
        {
            visit(node.getArgumentList());
        }
    }

    public void visitParenthesizedExpression(ParenthesizedExpression node)
    {
        // visit expression
        visit(node.getInnerExpression());
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        // visit primary (expression)
        visit(node.getInnerExpression());
    }

    public void visitProject(Project node)
    {
        for (CompilationUnitIterator iter = node.getCompilationUnits().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        if (node.getReturnValue() != null)
        {
            // visit expression
            visit(node.getReturnValue());
        }
    }

    public void visitSelfAccess(SelfAccess node)
    {
        if (node.isQualified())
        {
            visit(node.getTypeAccess());
        }
    }

    public void visitSingleInitializer(SingleInitializer node)
    {
        // expression can be null if type is not complete (only interface)
        if (node.getInitEpression() != null)
        {
            visit(node.getInitEpression());
        }
    }

    public void visitStatementExpressionList(StatementExpressionList node)
    {
        for (ExpressionIterator it = node.getExpressions().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
        visit(node.getSwitchExpression());

        for (CaseBlockIterator it = node.getCaseBlocks().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        // visit expression
        visit(node.getLockExpression());

        // visit block
        visit(node.getBlock());
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        // visit expression
        visit(node.getThrowExpression());
    }

    public void visitTryStatement(TryStatement node)
    {
        visit(node.getTryBlock());

        for (CatchClauseIterator it = node.getCatchClauses().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
        if (node.hasFinallyClause())
        {
            visit(node.getFinallyClause());
        }
    }

    public void visitTypeAccess(TypeAccess node)
    {
        if (node.getBaseExpression() != null)
        {
            visit(node.getBaseExpression());
        }
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        if (node.getOperator() == UnaryExpression.CAST_OP)
        {
            // visit type
            visit(node.getCastType());
        }

        // visit expression
        visit(node.getInnerExpression());
    }

    public void visitUnresolvedAccess(UnresolvedAccess node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }
    }

    public void visitWhileStatement(WhileStatement node)
    {
        // visit expression
        visit(node.getCondition());

        // visit statement
        visit(node.getLoopStatement());
    }
}
