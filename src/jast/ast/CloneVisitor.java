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

import jast.Global;
import jast.SyntaxException;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BlockStatement;
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
import jast.ast.nodes.Expression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.Feature;
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
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.Package;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.Primary;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Statement;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableInitializer;
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.CaseBlockIterator;
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.CommentIterator;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.ExpressionArray;
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
import jast.ast.visitor.EmptyVisitor;

import java.util.Hashtable;

public class CloneVisitor extends EmptyVisitor
{
    private NodeFactory _factory      = Global.getFactory();
    private Hashtable   _mappings     = new Hashtable();
    private boolean     _preserveRefs = false;
    private Node        _result;

    private void cloneBaseInterfaces(TypeDeclaration src, TypeDeclaration dest)
    {
        if (src.hasBaseInterfaces())
        {
            for (TypeIterator it = src.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                dest.getBaseInterfaces().add((Type)_result);
            }
        }
    }

    private void cloneComments(Node src, Node dest)
    {
        for (CommentIterator it = src.getComments().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getComments().add((Comment)_result);
        }
    }

    private void cloneConstructors(ClassDeclaration src, ClassDeclaration dest)
    {
        for (ConstructorIterator it = src.getConstructors().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getConstructors().add((ConstructorDeclaration)_result);
        }
    }

    private void cloneExpressionArray(ExpressionArray srcExprs, ExpressionArray destExprs)
    {
        for (ExpressionIterator it = srcExprs.getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            destExprs.add((Expression)_result);
        }
    }

    private void cloneFields(TypeDeclaration src, TypeDeclaration dest)
    {
        for (FieldIterator it = src.getFields().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getFields().add((FieldDeclaration)_result);
        }
    }

    private void cloneInitializers(ClassDeclaration src, ClassDeclaration dest)
    {
        for (InitializerIterator it = src.getInitializers().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getInitializers().add((Initializer)_result);
        }
    }

    private void cloneInnerTypes(TypeDeclaration src, TypeDeclaration dest)
    {
        for (TypeDeclarationIterator it = src.getInnerTypes().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getInnerTypes().add((TypeDeclaration)_result);
        }
    }

    private void cloneMethods(TypeDeclaration src, TypeDeclaration dest)
    {
        for (MethodIterator it = src.getMethods().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getMethods().add((MethodDeclaration)_result);
        }
    }

    private void cloneThrownTypes(InvocableDeclaration src, InvocableDeclaration dest)
    {
        for (TypeIterator it = src.getThrownExceptions().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            dest.getThrownExceptions().add((Type)_result);
        }
    }

    public Node createClone(Node source)
    {
        _mappings.clear();
        _result = null;

        // if we clone a declaration or higher then we
        // have to invalidate all references (i.e. to
        // the method being invoked)
        if ((source instanceof Project) ||
            (source instanceof CompilationUnit) ||
            (source instanceof Feature))
        {
            _preserveRefs = false;
        }
        else
        {
            _preserveRefs = true;
        }
        source.accept(this);

        return _result;
    }

    private void finishClone(Node src, Node dest)
    {
        if (!(src instanceof Comment))
        {
            cloneComments(src, dest);
        }
        if (src.getStartPosition() != null)
        {
            dest.setStartPosition(src.getStartPosition());
        }
        if (src.getFinishPosition() != null)
        {
            dest.setFinishPosition(src.getFinishPosition());
        }

        // note that we ignore properties

        _result = dest;
    }

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
        node.getBaseClass().accept(this);

        Type                      baseClass = (Type)_result;
        AnonymousClassDeclaration result    = _factory.createAnonymousClassDeclaration(baseClass.getQualifiedName());

        try
        {
            result.setBaseClass(baseClass);
        }
        catch (SyntaxException ex)
        {}
        _mappings.put(node, result);

        cloneInnerTypes(node, result);
        cloneFields(node, result);
        cloneMethods(node, result);
        finishClone(node, result);
    }

    public void visitArgumentList(ArgumentList node)
    {
        ArgumentList argList = _factory.createArgumentList();

        cloneExpressionArray(node.getArguments(), argList.getArguments());
        finishClone(node, argList);
    }

    public void visitArrayAccess(ArrayAccess node)
    {
        Primary baseExpr = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }
        node.getIndexExpression().accept(this);

        finishClone(node, _factory.createArrayAccess(baseExpr, (Expression)_result));
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        ArrayCreation result = _factory.createArrayCreation(node.getCreatedType(), node.getDimensions());

        if (node.isInitialized())
        {
            node.getInitializer().accept(this);
            result.setInitializer((ArrayInitializer)_result);
        }
        finishClone(node, result);
    }

    public void visitArrayInitializer(ArrayInitializer node)
    {
        ArrayInitializer result = _factory.createArrayInitializer();

        for (VariableInitializerIterator it = node.getInitializers().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getInitializers().add((VariableInitializer)_result);
        }
        finishClone(node, result);
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        node.getLeftHandSide().accept(this);

        Expression leftHandSide = (Expression)_result;

        node.getValueExpression().accept(this);

        finishClone(node,
                    _factory.createAssignmentExpression(
                        leftHandSide,
                        node.getOperator(),
                        (Expression)_result));
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
        node.getLeftOperand().accept(this);

        Expression leftOperand = (Expression)_result;

        node.getRightOperand().accept(this);

        finishClone(node,
                    _factory.createBinaryExpression(
                        node.getOperator(),
                        leftOperand,
                        (Expression)_result));
    }

    public void visitBlock(Block node)
    {
        Block result = _factory.createBlock();

        for (BlockStatementIterator it = node.getBlockStatements().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getBlockStatements().add((BlockStatement)_result);
        }
        finishClone(node, result);
    }

    public void visitBooleanLiteral(BooleanLiteral node)
    {
        finishClone(node,
                    _factory.createBooleanLiteral(node.getValue()));
    }

    public void visitBreakStatement(BreakStatement node)
    {
        LabeledStatement target = null;

        if (node.hasTarget())
        {
            // if only the break statement is copied, then the
            // resulting break statement will not have a target
            target = (LabeledStatement)_mappings.get(node.getTarget());
        }

        finishClone(node,
                    _factory.createBreakStatement(target));
    }

    public void visitCaseBlock(CaseBlock node)
    {
        CaseBlock result = _factory.createCaseBlock();

        cloneExpressionArray(node.getCases(), result.getCases());
        for (BlockStatementIterator it = node.getBlockStatements().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getBlockStatements().add((BlockStatement)_result);
        }
        finishClone(node, result);
    }

    public void visitCatchClause(CatchClause node)
    {
        node.getFormalParameter().accept(this);

        FormalParameter param = (FormalParameter)_result;

        node.getCatchBlock().accept(this);

        try
        {
            finishClone(node,
                        _factory.createCatchClause(param, (Block)_result));
        }
        catch (SyntaxException ex)
        {}
    }

    public void visitCharacterLiteral(CharacterLiteral node)
    {
        try
        {
            finishClone(node,
                        _factory.createCharacterLiteral(node.asString()));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }

    public void visitClassAccess(ClassAccess node)
    {
        Type baseType = null;

        if (node.isQualified())
        {
            node.getReferencedType().accept(this);
            baseType = (Type)_result;
        }
        finishClone(node,
                    _factory.createClassAccess(baseType));
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        node.getModifiers().accept(this);

        ClassDeclaration result = _factory.createClassDeclaration(
                                      (Modifiers)_result,
                                      node.getName(),
                                      node.isLocal());

        if (node.hasBaseClass())
        {
            node.getBaseClass().accept(this);
            try
            {
                result.setBaseClass((Type)_result);
            }
            catch (SyntaxException ex)
            {}
        }
        cloneBaseInterfaces(node, result);
        _mappings.put(node, result);

        cloneInnerTypes(node, result);
        cloneFields(node, result);
        cloneConstructors(node, result);
        cloneMethods(node, result);
        cloneInitializers(node, result);

        finishClone(node, result);
    }

    public void visitComment(Comment node)
    {
        finishClone(node,
                    _factory.createComment(
                        node.getText(),
                        node.isBlockComment()));
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        CompilationUnit result  = _factory.createCompilationUnit(node.getName());

        // we do not clone the package; if the project is cloned then the
        // package is automatically cloned and set in all affected units
        result.setPackage(node.getPackage());
        for (ImportIterator it = node.getImportDeclarations().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getImportDeclarations().add((ImportDeclaration)_result);
        }
        for (TypeDeclarationIterator it = node.getTypeDeclarations().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getTypeDeclarations().add((TypeDeclaration)_result);
        }
        finishClone(node, result);
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
        node.getCondition().accept(this);

        Expression condition = (Expression)_result;

        node.getTrueExpression().accept(this);

        Expression trueExpr = (Expression)_result;

        node.getFalseExpression().accept(this);

        finishClone(node,
                    _factory.createConditionalExpression(
                        condition,
                        trueExpr,
                        (Expression)_result));
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        node.getModifiers().accept(this);

        Modifiers           mods   = (Modifiers)_result;
        FormalParameterList params = null;

        if (node.hasParameters())
        {
            node.getParameterList().accept(this);
            params = (FormalParameterList)_result;
        }

        ConstructorDeclaration result = _factory.createConstructorDeclaration(
                                            mods,
                                            node.getName(),
                                            params);

        cloneThrownTypes(node, result);
        _mappings.put(node, result);

        node.getBody().accept(this);
        result.setBody((Block)_result);

        finishClone(node, result);
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        Primary      baseExpr = null;
        ArgumentList args     = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }
        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
            args = (ArgumentList)_result;
        }

        ConstructorInvocation  result             = _factory.createConstructorInvocation(
                                                        node.ofBaseClass(),
                                                        baseExpr,
                                                        args);
        ConstructorDeclaration invokedConstructor = node.getConstructorDeclaration();

        if (invokedConstructor != null)
        {
            if (_preserveRefs)
            {
                result.setConstructorDeclaration(invokedConstructor);
            }
            else if (_mappings.containsKey(invokedConstructor))
            {
                result.setConstructorDeclaration(
                    (ConstructorDeclaration)_mappings.get(invokedConstructor));
            }
        }
        finishClone(node, result);
    }

    public void visitContinueStatement(ContinueStatement node)
    {
        LabeledStatement target = null;

        if (node.hasTarget())
        {
            // if only the break statement is copied, then the
            // resulting break statement will not have a target
            target = (LabeledStatement)_mappings.get(node.getTarget());
        }

        finishClone(node,
                    _factory.createContinueStatement(target));
    }

    public void visitDoWhileStatement(DoWhileStatement node)
    {
        node.getCondition().accept(this);

        Expression condition = (Expression)_result;

        node.getLoopStatement().accept(this);

        try
        {
            finishClone(node,
                        _factory.createDoWhileStatement(
                            condition,
                            (Statement)_result));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }

    public void visitEmptyStatement(EmptyStatement node)
    {
        finishClone(node,
                    _factory.createEmptyStatement());
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        node.getExpression().accept(this);
        finishClone(node,
                    _factory.createExpressionStatement((Expression)_result));
    }

    public void visitFieldAccess(FieldAccess node)
    {
        Primary baseExpr = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }

        FieldAccess      result        = _factory.createFieldAccess(
                                             baseExpr,
                                             node.getFieldName());
        FieldDeclaration accessedField = node.getFieldDeclaration();

        if (accessedField != null)
        {
            if (_preserveRefs)
            {
                result.setFieldDeclaration(accessedField);
            }
            else if (_mappings.containsKey(accessedField))
            {
                result.setFieldDeclaration(
                    (FieldDeclaration)_mappings.get(accessedField));
            }
        }
        finishClone(node, result);
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        node.getModifiers().accept(this);

        Modifiers mods = (Modifiers)_result;

        node.getType().accept(this);

        Type                type        = (Type)_result;
        VariableInitializer initializer = null;

        if (node.hasInitializer())
        {
            node.getInitializer().accept(this);
            initializer = (VariableInitializer)_result;
        }

        FieldDeclaration result = _factory.createFieldDeclaration(
                                      mods,
                                      type,
                                      node.getName(),
                                      initializer);

        finishClone(node, result);
        _mappings.put(node, result);
    }

    public void visitFloatingPointLiteral(FloatingPointLiteral node)
    {
        try
        {
            finishClone(node,
                        _factory.createFloatingPointLiteral(node.asString()));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }

    public void visitFormalParameter(FormalParameter node)
    {
        node.getType().accept(this);

        FormalParameter result = _factory.createFormalParameter(
                                     node.getModifiers().isFinal(),
                                     (Type)_result,
                                     node.getName());

        finishClone(node, result);
        _mappings.put(node, result);
    }

    public void visitFormalParameterList(FormalParameterList node)
    {
        FormalParameterList result = _factory.createFormalParameterList();

        for (FormalParameterIterator it = node.getParameters().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getParameters().add((FormalParameter)_result);
        }
    }

    public void visitForStatement(ForStatement node)
    {
        Expression              condition = null;
        StatementExpressionList updList   = null;

        if (node.hasCondition())
        {
            node.getCondition().accept(this);
            condition = (Expression)_result;
        }
        if (node.hasUpdateList())
        {
            node.getUpdateList().accept(this);
            updList = (StatementExpressionList)_result;
        }
        node.getLoopStatement().accept(this);

        Statement    loopStmt = (Statement)_result;
        ForStatement result   = null;

        if (node.hasInitList())
        {
            node.getInitList().accept(this);

            try
            {
                result = _factory.createForStatement(
                             (StatementExpressionList)_result,
                             condition,
                             updList,
                             loopStmt);
            }
            catch (SyntaxException ex)
            {}
        }
        else
        {
            try
            {
                result = _factory.createForStatement(
                             condition,
                             updList,
                             loopStmt);
            }
            catch (SyntaxException ex)
            {}
            if (node.hasInitDeclarations())
            {
                for (LocalVariableIterator it = node.getInitDeclarations().getIterator(); it.hasNext();)
                {
                    it.getNext().accept(this);
                    result.addInitDeclaration((LocalVariableDeclaration)_result);
                }
            }
        }
        finishClone(node, result);
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        node.getCondition().accept(this);

        Expression condition = (Expression)_result;

        node.getTrueStatement().accept(this);

        Statement trueStmt  = (Statement)_result;
        Statement falseStmt = null;

        if (node.hasElse())
        {
            node.getFalseStatement().accept(this);
            falseStmt = (Statement)_result;
        }

        try
        {
            finishClone(node,
                        _factory.createIfThenElseStatement(
                            condition,
                            trueStmt,
                            falseStmt));
        }
        catch (SyntaxException ex)
        {}
    }

    public void visitImportDeclaration(ImportDeclaration node)
    {
        finishClone(node,
                    _factory.createImportDeclaration(
                        node.getImportedPackageOrType(),
                        node.isOnDemand()));
    }

    public void visitInitializer(Initializer node)
    {
        node.getBody().accept(this);

        Initializer result = _factory.createInitializer(
                                 (Block)_result,
                                 node.isStatic());

        finishClone(node, result);
        _mappings.put(node, result);
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        node.getInnerExpression().accept(this);

        Expression innerExpr = (Expression)_result;

        node.getReferencedType().accept(this);

        finishClone(node,
                    _factory.createInstanceofExpression(
                        innerExpr,
                        (Type)_result));
    }

    public void visitInstantiation(Instantiation node)
    {
        Primary                   baseExpr  = null;
        ArgumentList              args      = null;
        AnonymousClassDeclaration anonClass = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }
        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
            args = (ArgumentList)_result;
        }
        if (node.withAnonymousClass())
        {
            node.getAnonymousClass().accept(this);
            anonClass = (AnonymousClassDeclaration)_result;
        }
        node.getInstantiatedType().accept(this);

        Instantiation          result             = _factory.createInstantiation(
                                                        baseExpr,
                                                        (Type)_result,
                                                        args,
                                                        anonClass);
        ConstructorDeclaration invokedConstructor = node.getInvokedConstructor();

        if (invokedConstructor != null)
        {
            if (_preserveRefs)
            {
                result.setInvokedConstructor(invokedConstructor);
            }
            else if (_mappings.containsKey(invokedConstructor))
            {
                result.setInvokedConstructor(
                    (ConstructorDeclaration)_mappings.get(invokedConstructor));
            }
        }
        finishClone(node, result);
    }

    public void visitIntegerLiteral(IntegerLiteral node)
    {
        try
        {
            finishClone(node,
                        _factory.createIntegerLiteral(node.asString()));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        node.getModifiers().accept(this);

        InterfaceDeclaration result = _factory.createInterfaceDeclaration(
                                          (Modifiers)_result,
                                          node.getName());

        cloneBaseInterfaces(node, result);
        _mappings.put(node, result);

        cloneInnerTypes(node, result);
        cloneFields(node, result);
        cloneMethods(node, result);

        finishClone(node, result);
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
        LabeledStatement result = _factory.createLabeledStatement(node.getName());

        _mappings.put(node, result);

        node.getStatement().accept(this);
        result.setStatement((Statement)_result);

        finishClone(node, result);
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        node.getType().accept(this);

        Type                type        = (Type)_result;
        VariableInitializer initializer = null;

        if (node.hasInitializer())
        {
            node.getInitializer().accept(this);
            initializer = (VariableInitializer)_result;
        }

        LocalVariableDeclaration result = _factory.createLocalVariableDeclaration(
                                      node.getModifiers().isFinal(),
                                      type,
                                      node.getName(),
                                      initializer);

        finishClone(node, result);
        _mappings.put(node, result);
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        node.getModifiers().accept(this);

        Modifiers           mods       = (Modifiers)_result;
        Type                returnType = null;
        FormalParameterList params     = null;

        if (node.hasReturnType())
        {
            node.getReturnType().accept(this);
            returnType = (Type)_result;
        }
        if (node.hasParameters())
        {
            node.getParameterList().accept(this);
            params = (FormalParameterList)_result;
        }

        MethodDeclaration result = _factory.createMethodDeclaration(
                                            mods,
                                            returnType,
                                            node.getName(),
                                            params);

        cloneThrownTypes(node, result);
        _mappings.put(node, result);

        node.getBody().accept(this);
        result.setBody((Block)_result);

        finishClone(node, result);
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        Primary      baseExpr = null;
        ArgumentList args     = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }
        if (node.hasArguments())
        {
            node.getArgumentList().accept(this);
            args = (ArgumentList)_result;
        }

        MethodInvocation  result        = _factory.createMethodInvocation(
                                              baseExpr,
                                              node.getMethodName(),
                                              args);
        MethodDeclaration invokedMethod = node.getMethodDeclaration();

        if (invokedMethod != null)
        {
            if (_preserveRefs)
            {
                result.setMethodDeclaration(invokedMethod);
            }
            else if (_mappings.containsKey(invokedMethod))
            {
                result.setMethodDeclaration(
                    (MethodDeclaration)_mappings.get(invokedMethod));
            }
        }
        finishClone(node, result);
    }

    public void visitModifiers(Modifiers node)
    {
        finishClone(node,
                    _factory.createModifiers(node.getMask()));
    }

    public void visitNullLiteral(NullLiteral node)
    {
        finishClone(node,
                    _factory.createNullLiteral());
    }

    public void visitPackage(Package node)
    {
        // cloning a package object is not really useful without
        // a target project (other than the original one)
    }

    public void visitParenthesizedExpression(ParenthesizedExpression node)
    {
        node.getInnerExpression().accept(this);
        finishClone(node,
                    _factory.createParenthesizedExpression(
                        (Expression)_result));
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        node.getInnerExpression().accept(this);
        finishClone(node,
                    _factory.createPostfixExpression(
                        (Primary)_result,
                        node.isIncrement()));
    }

    public void visitPrimitiveType(PrimitiveType node)
    {
        // primitive types are not cloned since they
        // are singletons anyway
        _result = node;
    }

    public void visitProject(Project node)
    {
        Project result = _factory.createProject();

        _mappings.put(node, result);

        CompilationUnit unit;

        // note that packages are automatically created
        for (CompilationUnitIterator it = node.getCompilationUnits().getIterator(); it.hasNext();)
        {
            unit = it.getNext();
            unit.accept(this);
            result.addCompilationUnit((CompilationUnit)_result,
                                      unit.getPackage().getQualifiedName());
        }
        finishClone(node, result);
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        Expression returnValue = null;

        if (node.hasReturnValue())
        {
            node.getReturnValue().accept(this);
            returnValue = (Expression)_result;
        }
        finishClone(node,
                    _factory.createReturnStatement(returnValue));
    }

    public void visitSelfAccess(SelfAccess node)
    {
        TypeAccess accessedType = null;

        if (node.isQualified())
        {
            node.getTypeAccess().accept(this);
            accessedType = (TypeAccess)_result;
        }

        SelfAccess result = _factory.createSelfAccess(
                                accessedType,
                                node.isSuper());

        if (node.getType() != null)
        {
            node.getType().accept(this);
            result.setType((Type)_result);
        }
        finishClone(node, result);
    }

    public void visitSingleInitializer(SingleInitializer node)
    {
        node.getInitEpression().accept(this);
        finishClone(node,
                    _factory.createSingleInitializer(
                        (Expression)_result));
    }

    public void visitStatementExpressionList(StatementExpressionList node)
    {
        StatementExpressionList result = _factory.createStatementExpressionList();

        cloneExpressionArray(node.getExpressions(),
                             result.getExpressions());
        finishClone(node, result);
    }

    public void visitStringLiteral(StringLiteral node)
    {
        try
        {
            finishClone(node,
                        _factory.createStringLiteral(node.asString()));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }

    public void visitSwitchStatement(SwitchStatement node)
    {
        node.getSwitchExpression().accept(this);

        SwitchStatement result = _factory.createSwitchStatement(
                                     (Expression)_result);

        for (CaseBlockIterator it = node.getCaseBlocks().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getCaseBlocks().add((CaseBlock)_result);
        }
        finishClone(node, result);
    }

    public void visitSynchronizedStatement(SynchronizedStatement node)
    {
        node.getLockExpression().accept(this);

        Expression lockExpr = (Expression)_result;

        node.getBlock().accept(this);
        finishClone(node,
                    _factory.createSynchronizedStatement(
                        lockExpr,
                        (Block)_result));
    }

    public void visitThrowStatement(ThrowStatement node)
    {
        node.getThrowExpression().accept(this);
        finishClone(node,
                    _factory.createThrowStatement((Expression)_result));
    }

    public void visitTryStatement(TryStatement node)
    {
        node.getTryBlock().accept(this);

        TryStatement result = _factory.createTryStatement(
                                  (Block)_result);

        for (CatchClauseIterator it = node.getCatchClauses().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            result.getCatchClauses().add((CatchClause)_result);
        }
        if (node.hasFinallyClause())
        {
            node.getFinallyClause().accept(this);
            result.setFinallyClause((Block)_result);
        }
        finishClone(node, result);
    }

    public void visitType(Type node)
    {
        Type result = null;

        if (node.isPrimitive())
        {
            node.getPrimitiveBaseType().accept(this);
            result = _factory.createType(
                         (PrimitiveType)_result,
                         node.getDimensions());
        }
        else
        {
            result = _factory.createType(
                         node.getBaseName(),
                         node.getDimensions());

            TypeDeclaration typeDecl = node.getReferenceBaseTypeDecl();

            if (typeDecl != null)
            {
                if (_preserveRefs)
                {
                    result.setReferenceBaseTypeDecl(typeDecl);
                }
                else if (_mappings.containsKey(typeDecl))
                {
                    result.setReferenceBaseTypeDecl(
                        (TypeDeclaration)_mappings.get(typeDecl));
                }
            }
        }
        finishClone(node, result);
    }

    public void visitTypeAccess(TypeAccess node)
    {
        Primary baseExpr = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }
        node.getType().accept(this);

        finishClone(node,
                    _factory.createTypeAccess(
                        baseExpr,
                        (Type)_result));
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        node.getInnerExpression().accept(this);

        Expression innerExpr = (Expression)_result;

        if (node.getOperator() == UnaryExpression.CAST_OP)
        {
            node.getCastType().accept(this);
            finishClone(node,
                        _factory.createCastExpression(
                            (Type)_result,
                            innerExpr));
        }
        else
        {
            finishClone(node,
                        _factory.createUnaryExpression(
                            node.getOperator(),
                            innerExpr));
        }
    }

    public void visitUnresolvedAccess(UnresolvedAccess node)
    {
        Primary baseExpr = null;

        if (node.isTrailing())
        {
            node.getBaseExpression().accept(this);
            baseExpr = (Primary)_result;
        }

        UnresolvedAccess result = _factory.createUnresolvedAccess(baseExpr);

        for (int idx = 0; idx < node.getParts().getCount(); idx++)
        {
            result.addPart(node.getParts().get(idx),
                           node.getStartPosition(idx));
        }
        finishClone(node, result);
    }

    public void visitVariableAccess(VariableAccess node)
    {
        if (_preserveRefs)
        {
            finishClone(node,
                        _factory.createVariableAccess(
                            node.getVariableDeclaration()));
        }
        else
        {
            finishClone(node,
                        _factory.createVariableAccess(
                            (LocalVariableDeclaration)_mappings.get(
                                node.getVariableDeclaration())));
        }
    }

    public void visitWhileStatement(WhileStatement node)
    {
        node.getCondition().accept(this);

        Expression condition = (Expression)_result;

        node.getLoopStatement().accept(this);

        try
        {
            finishClone(node,
                        _factory.createWhileStatement(
                            condition,
                            (Statement)_result));
        }
        catch (SyntaxException ex)
        {
            _result = null;
        }
    }
}
