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

public class DescendingVisitor extends EmptyVisitor
{

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
        // Note that base class/base interface is only available after resolving
        if (node.getBaseClass() != null)
        {
            node.getBaseClass().accept(this);
        }

        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        for (InitializerIterator iter = node.getInitializers().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }
    }

    public void visitArgumentList(ArgumentList node)
    {
        for (ExpressionIterator it = node.getArguments().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitArrayAccess(ArrayAccess node)
    {
        // visit expression
        node.getBaseExpression().accept(this);
        node.getIndexExpression().accept(this);
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        node.getCreatedType().accept(this);

        // visit expressions
        for (ExpressionIterator it = node.getDimExpressions().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }

        // visit initializer
        if (node.isInitialized())
        {
            node.getInitializer().accept(this);
        }
    }

    public void visitArrayInitializer(ArrayInitializer node)
    {
        // visit variable initializer
        for (VariableInitializerIterator it = node.getInitializers().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        // visit expression
        node.getLeftHandSide().accept(this);
        node.getValueExpression().accept(this);
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
        // visit expression
        node.getLeftOperand().accept(this);
        node.getRightOperand().accept(this);
    }

    public void visitBlock(Block node)
    {
        for (BlockStatementIterator it = node.getBlockStatements().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitCaseBlock(CaseBlock node)
    {
        for (ExpressionIterator exprIt = node.getCases().getIterator(); exprIt.hasNext();)
        {
            exprIt.getNext().accept(this);
        }

        for (BlockStatementIterator stmtIt = node.getBlockStatements().getIterator(); stmtIt.hasNext();)
        {
            stmtIt.getNext().accept(this);
        }
    }

    public void visitCatchClause(CatchClause node)
    {
        // visit formal parameter
        node.getFormalParameter().accept(this);

        // visit block
        node.getCatchBlock().accept(this);
    }

    public void visitClassAccess(ClassAccess node)
    {
        if (node.isQualified())
        {
            node.getReferencedType().accept(this);
        }
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        // visit modifiers
        node.getModifiers().accept(this);
        if (node.getBaseClass() != null)
        {
            // visit extended class (type)
            node.getBaseClass().accept(this);
        }

        // visit implemented interfaces (types)
        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit inner classes (types)
        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit field declarations
        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit initializers
        for (InitializerIterator iter = node.getInitializers().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit constructor declarations
        for (ConstructorIterator iter = node.getConstructors().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit method declarations
        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        // visit package
        node.getPackage().accept(this);

        // visit imports
        for (ImportIterator iter = node.getImportDeclarations().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit interfaces and Classes
        for (TypeDeclarationIterator iter = node.getTypeDeclarations().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
        // visit expression
        node.getCondition().accept(this);
        node.getTrueExpression().accept(this);
        node.getFalseExpression().accept(this);
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        node.getModifiers().accept(this);

        if (node.hasParameters())
        {
            node.getParameterList().accept(this);
        }

        for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }

        if (node.hasBody())
        {
            node.getBody().accept(this);
        }
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
        }
        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
        }
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        // visit statement
        node.getLoopStatement().accept(this);

        // visit expression
        node.getCondition().accept(this);
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        // visit expression
        node.getExpression().accept(this);
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.isTrailing())
        {
            // visit expression
            node.getBaseExpression().accept(this);
        }
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        // visit modifiers
        node.getModifiers().accept(this);
        // visit type
        node.getType().accept(this);

        if (node.hasInitializer())
        {
            // visit initializer
            node.getInitializer().accept(this);
        }
    }

    public void visitFormalParameter(FormalParameter node)
    {
        // visit type
        node.getType().accept(this);
    }

    public void visitFormalParameterList(FormalParameterList node)
    {
        for (FormalParameterIterator it = node.getParameters().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitForStatement(ForStatement node)
    {
        if (node.hasInitDeclarations())
        {
            // visit local variable declarations
            for (LocalVariableIterator it = node.getInitDeclarations().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
            }
        }
        else if (node.getInitList() != null)
        {
            // visit statement expression list
            node.getInitList().accept(this);
        }
        if (node.getCondition() != null)
        {
            // visit expression
            node.getCondition().accept(this);
        }
        if (node.getUpdateList() != null)
        {
            // visit statement expression list
            node.getUpdateList().accept(this);
        }

        // visit statement
        node.getLoopStatement().accept(this);
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        // visit expression
        node.getCondition().accept(this);

        // visit statement
        node.getTrueStatement().accept(this);
        if (node.hasElse())
        {
            // visit statement
            node.getFalseStatement().accept(this);
        }
    }

    public void visitInitializer(Initializer node)
    {
        // The body could be empty if only the interface
        // was parsed
        if (node.getBody() != null)
        {
            node.getBody().accept(this);
        }
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        // visit expression
        node.getInnerExpression().accept(this);

        // visit type
        node.getReferencedType().accept(this);
    }

    public void visitInstantiation(Instantiation node)
    {
        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
        }

        node.getInstantiatedType().accept(this);

        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
        }

        if (node.withAnonymousClass())
        {
            node.getAnonymousClass().accept(this);
        }
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        // visit modifiers
        node.getModifiers().accept(this);

        // visit implemented interfaces (types)
        for (TypeIterator iter = node.getBaseInterfaces().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit inner classes (types)
        for (TypeDeclarationIterator iter = node.getInnerTypes().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit field declarations
        for (FieldIterator iter = node.getFields().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }

        // visit methods declarations
        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
        // visit statement
        node.getStatement().accept(this);
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        // visit modifiers
        node.getModifiers().accept(this);
        // visit type
        node.getType().accept(this);

        if (node.getInitializer() != null)
        {
            // visit initializer
            node.getInitializer().accept(this);
        }
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        node.getModifiers().accept(this);

        if (node.hasReturnType())
        {
            node.getReturnType().accept(this);
        }

        if (node.hasParameters())
        {
            node.getParameterList().accept(this);
        }

        for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }

        if (node.hasBody())
        {
            node.getBody().accept(this);
        }
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
        }
        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
        }
    }

    public void visitParenthesizedExpression(ParenthesizedExpression node)
    {
        // visit expression
        node.getInnerExpression().accept(this);
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        // visit primary (expression)
        node.getInnerExpression().accept(this);
    }

    public void visitProject(Project node)
    {
        for (CompilationUnitIterator iter = node.getCompilationUnits().getIterator(); iter.hasNext();)
        {
            iter.getNext().accept(this);
        }
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        if (node.getReturnValue() != null)
        {
            // visit expression
            node.getReturnValue().accept(this);
        }
    }

    public void visitSelfAccess(SelfAccess node)
    {
        if (node.isQualified())
        {
            node.getTypeAccess().accept(this);
        }
    }

    public void visitSingleInitializer(SingleInitializer node)
    {
        // expression can be null if type is not complete (only interface)
        if (node.getInitEpression() != null)
        {
            node.getInitEpression().accept(this);
        }
    }

    public void visitStatementExpressionList(StatementExpressionList node)
    {
        for (ExpressionIterator it = node.getExpressions().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
        node.getSwitchExpression().accept(this);

        for (CaseBlockIterator it = node.getCaseBlocks().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        // visit expression
        node.getLockExpression().accept(this);

        // visit block
        node.getBlock().accept(this);
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        // visit expression
        node.getThrowExpression().accept(this);
    }

    public void visitTryStatement(TryStatement node)
    {
        node.getTryBlock().accept(this);

        for (CatchClauseIterator it = node.getCatchClauses().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
        }
        if (node.hasFinallyClause())
        {
            node.getFinallyClause().accept(this);
        }
    }

    public void visitTypeAccess(TypeAccess node)
    {
        if (node.getBaseExpression() != null)
        {
            node.getBaseExpression().accept(this);
        }
        node.getType().accept(this);
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        if (node.getOperator() == UnaryExpression.CAST_OP)
        {
            // visit type
            node.getCastType().accept(this);
        }

        // visit expression
        node.getInnerExpression().accept(this);
    }

    public void visitUnresolvedAccess(UnresolvedAccess node)
    {
        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
        }
    }

    public void visitWhileStatement(WhileStatement node)
    {
        // visit expression
        node.getCondition().accept(this);

        // visit statement
        node.getLoopStatement().accept(this);
    }
}
