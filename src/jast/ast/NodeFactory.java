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
import jast.ast.nodes.Expression;
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

public class NodeFactory
{

    public NodeFactory()
    {
    }

    public AnonymousClassDeclaration createAnonymousClassDeclaration(String baseType)
    {
        return new AnonymousClassDeclaration(createType(baseType, 0));
    }

    public ArgumentList createArgumentList()
    {
        return new ArgumentList();
    }

    public ArrayAccess createArrayAccess(Primary expr, Expression indexExpr)
    {
        return new ArrayAccess(expr, indexExpr);
    }

    public ArrayCreation createArrayCreation(Type baseType, int dimensions)
    {
        return new ArrayCreation(baseType, dimensions);
    }

    public ArrayCreation createArrayCreation(String className, boolean isPrimitive) throws SyntaxException
    {
        return new ArrayCreation(createType(className, isPrimitive));
    }

    public ArrayInitializer createArrayInitializer()
    {
        return new ArrayInitializer();
    }

    public AssignmentExpression createAssignmentExpression(Expression leftHandSide, int operator, Expression value)
    {
        return new AssignmentExpression(leftHandSide, operator, value);
    }

    public BinaryExpression createBinaryExpression(int operator, Expression firstExpr, Expression secondExpr)
    {
        return new BinaryExpression(operator, firstExpr, secondExpr);
    }

    public Block createBlock()
    {
        return new Block();
    }

    public BooleanLiteral createBooleanLiteral(boolean value)
    {
        return new BooleanLiteral(value);
    }

    public BreakStatement createBreakStatement(LabeledStatement target)
    {
        return new BreakStatement(target);
    }

    public CaseBlock createCaseBlock()
    {
        return new CaseBlock();
    }

    public UnaryExpression createCastExpression(Type castType, Expression innerExpr)
    {
        return new UnaryExpression(castType, innerExpr);
    }

    public CatchClause createCatchClause(FormalParameter param, Block block) throws SyntaxException
    {
        return new CatchClause(param, block);
    }

    public CharacterLiteral createCharacterLiteral(String asString) throws SyntaxException
    {
        return new CharacterLiteral(asString);
    }

    public ClassAccess createClassAccess(Type referencedType)
    {
        return new ClassAccess(referencedType);
    }

    public ClassDeclaration createClassDeclaration(Modifiers mods, String name, boolean isLocal)
    {
        return new ClassDeclaration(mods, name, isLocal);
    }

    public Comment createComment(String text, boolean isBlockComment)
    {
        return new Comment(text, isBlockComment);
    }

    public CompilationUnit createCompilationUnit(String filename)
    {
        return new CompilationUnit(filename);
    }

    public ConditionalExpression createConditionalExpression(Expression condition, Expression trueExpression, Expression falseExpression)
    {
        return new ConditionalExpression(condition, trueExpression, falseExpression);
    }

    public ConstructorDeclaration createConstructorDeclaration(Modifiers mods, String name, FormalParameterList params)
    {
        return new ConstructorDeclaration(mods, name, params);
    }

    public ConstructorInvocation createConstructorInvocation(boolean ofBaseClass, Primary base, ArgumentList args)
    {
        return new ConstructorInvocation(ofBaseClass, base, args);
    }

    public ContinueStatement createContinueStatement(LabeledStatement target)
    {
        return new ContinueStatement(target);
    }

    public DoWhileStatement createDoWhileStatement(Expression condition, Statement loopStmt) throws SyntaxException
    {
        return new DoWhileStatement(condition, loopStmt);
    }

    public EmptyStatement createEmptyStatement()
    {
        return new EmptyStatement();
    }

    public ExpressionStatement createExpressionStatement(Expression expression)
    {
        return new ExpressionStatement(expression);
    }

    public FieldAccess createFieldAccess(FieldDeclaration fieldDecl)
    {
        return new FieldAccess(null, fieldDecl);
    }

    public FieldAccess createFieldAccess(Primary expr, FieldDeclaration fieldDecl)
    {
        return new FieldAccess(expr, fieldDecl);
    }

    public FieldAccess createFieldAccess(Primary expr, String fieldName)
    {
        return new FieldAccess(expr, fieldName);
    }

    public FieldAccess createFieldAccess(String fieldName)
    {
        return new FieldAccess(fieldName);
    }

    public FieldDeclaration createFieldDeclaration(Modifiers           mods,
                                                   Type                type,
                                                   String              name,
                                                   VariableInitializer init)
    {
        return new FieldDeclaration(mods, type, name, init);
    }

    public FloatingPointLiteral createFloatingPointLiteral(String asString) throws SyntaxException
    {
        return new FloatingPointLiteral(asString);
    }

    public FormalParameter createFormalParameter(boolean isFinal,
                                                 Type    type,
                                                 String  name)
    {
        return new FormalParameter(isFinal, type, name);
    }

    public FormalParameterList createFormalParameterList()
    {
        return new FormalParameterList();
    }

    public ForStatement createForStatement(Expression expr, StatementExpressionList forUpdate, Statement stmt) throws SyntaxException
    {
        return new ForStatement(expr, forUpdate, stmt);
    }

    public ForStatement createForStatement(StatementExpressionList forInit, Expression expr, StatementExpressionList forUpdate, Statement stmt) throws SyntaxException
    {
        return new ForStatement(forInit, expr, forUpdate, stmt);
    }

    public IfThenElseStatement createIfThenElseStatement(Expression condition, Statement trueStmt, Statement falseStmt) throws SyntaxException
    {
        return new IfThenElseStatement(condition, trueStmt, falseStmt);
    }

    public ImportDeclaration createImportDeclaration(String qualifiedName, boolean isOnDemand)
    {
        return new ImportDeclaration(qualifiedName, isOnDemand);
    }

    public Initializer createInitializer(Block body, boolean isStatic)
    {
        return new Initializer(body, isStatic);
    }

    public InstanceofExpression createInstanceofExpression(Expression expr, Type refType)
    {
        return new InstanceofExpression(expr, refType);
    }

    public Instantiation createInstantiation(Primary                   expr,
                                             Type                      instantiatedType,
                                             ArgumentList              args,
                                             AnonymousClassDeclaration classDecl)
    {
        return new Instantiation(expr, instantiatedType, args, classDecl);
    }

    public IntegerLiteral createIntegerLiteral(String asString) throws SyntaxException
    {
        return new IntegerLiteral(asString);
    }

    public InterfaceDeclaration createInterfaceDeclaration(Modifiers mods, String name)
    {
        return new InterfaceDeclaration(mods, name);
    }

    public LabeledStatement createLabeledStatement(String label)
    {
        return new LabeledStatement(label);
    }

    public LocalVariableDeclaration createLocalVariableDeclaration(boolean             isFinal,
                                                                   Type                type,
                                                                   String              name,
                                                                   VariableInitializer init)
    {
        return new LocalVariableDeclaration(isFinal, type, name, init);
    }

    public MethodDeclaration createMethodDeclaration(Modifiers mods, Type returnType, String name, FormalParameterList params)
    {
        return new MethodDeclaration(mods, returnType, name, params);
    }

    public MethodInvocation createMethodInvocation(Primary baseExpr, String methodName, ArgumentList arguments)
    {
        return new MethodInvocation(baseExpr, methodName, arguments);
    }

    public MethodInvocation createMethodInvocation(String methodName, ArgumentList arguments)
    {
        return new MethodInvocation(methodName, arguments);
    }

    public Modifiers createModifiers()
    {
        return new Modifiers();
    }

    public Modifiers createModifiers(int mask)
    {
        return new Modifiers(mask);
    }

    public NullLiteral createNullLiteral()
    {
        return new NullLiteral();
    }

    public Type createNullType()
    {
        return new Type();
    }

    public Package createPackage(String qualifiedName)
    {
        return new Package(qualifiedName);
    }

    public ParenthesizedExpression createParenthesizedExpression(Expression innerExpr)
    {
        return new ParenthesizedExpression(innerExpr);
    }

    public PostfixExpression createPostfixExpression(Primary innerExpr, boolean isIncrement)
    {
        return new PostfixExpression(innerExpr, isIncrement);
    }

    public PrimitiveType createPrimitiveType(String typeName) throws SyntaxException
    {
        return PrimitiveType.createInstance(typeName);
    }

    public Project createProject()
    {
        return null;
    }

    public Project createProject(String name)
    {
        return new Project(name);
    }

    public ReturnStatement createReturnStatement(Expression returnValueExpr)
    {
        return new ReturnStatement(returnValueExpr);
    }

    public SelfAccess createSelfAccess(TypeAccess accessedType, boolean isSuper)
    {
        return new SelfAccess(accessedType, isSuper);
    }

    public SingleInitializer createSingleInitializer(Expression initExpr)
    {
        return new SingleInitializer(initExpr);
    }

    public StatementExpressionList createStatementExpressionList()
    {
        return new StatementExpressionList();
    }

    public StringLiteral createStringLiteral(String asString) throws SyntaxException
    {
        return new StringLiteral(asString);
    }

    public SwitchStatement createSwitchStatement(Expression expr)
    {
        return new SwitchStatement(expr);
    }

    public SynchronizedStatement createSynchronizedStatement(Expression lockExpr, Block synchronizedCode)
    {
        return new SynchronizedStatement(lockExpr, synchronizedCode);
    }

    public ThrowStatement createThrowStatement(Expression expr)
    {
        return new ThrowStatement(expr);
    }

    public TryStatement createTryStatement(Block tryBlock)
    {
        return new TryStatement(tryBlock);
    }

    public Type createType(PrimitiveType baseType, int dimensions)
    {
        return new Type(baseType, dimensions);
    }

    public Type createType(TypeDeclaration baseType, int dimensions)
    {
        return new Type(baseType, dimensions);
    }

    public Type createType(String baseType, int dimensions)
    {
        return new Type(baseType, dimensions);
    }

    public Type createType(String name, boolean isPrimitive) throws SyntaxException
    {
        if (name == null)
        {
            return null;
        }
        if (isPrimitive)
        {
            return new Type(createPrimitiveType(name));
        }
        else
        {
            return new Type(name);
        }
    }

    public TypeAccess createTypeAccess(Primary baseExpr, Type type)
    {
        return new TypeAccess(baseExpr, type);
    }

    public TypeAccess createTypeAccess(Primary baseExpr, String typeName) throws SyntaxException
    {
        return new TypeAccess(baseExpr, createType(typeName, 0));
    }

    public UnaryExpression createUnaryExpression(int operator, Expression innerExpr)
    {
        return new UnaryExpression(operator, innerExpr);
    }

    public UnresolvedAccess createUnresolvedAccess(Primary baseExpr)
    {
        return new UnresolvedAccess(baseExpr);
    }

    public VariableAccess createVariableAccess(LocalVariableDeclaration varDecl)
    {
        return new VariableAccess(varDecl);
    }

    public WhileStatement createWhileStatement(Expression condition, Statement loopStmt) throws SyntaxException
    {
        return new WhileStatement(condition, loopStmt);
    }


    public MethodInvocation createMethodInvocation(MethodDeclaration methodDecl, ArgumentList arguments)
    {
        return new MethodInvocation(methodDecl, arguments);
    }



    public MethodInvocation createMethodInvocation(Primary baseExpr, MethodDeclaration methodDecl, ArgumentList arguments)
    {
        return new MethodInvocation(baseExpr, methodDecl, arguments);
    }
}
