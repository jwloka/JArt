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
package jast.profiler;
import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.ParsePosition;
import jast.ast.Project;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.ArrayType;
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
import jast.ast.nodes.ConditionalStatement;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.ControlFlowStatement;
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
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.Invocation;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.Literal;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.LoopStatement;
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
import jast.ast.nodes.TrailingPrimary;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.BlockStatementArray;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.CaseBlockArray;
import jast.ast.nodes.collections.CaseBlockIterator;
import jast.ast.nodes.collections.CatchClauseArray;
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.CommentArray;
import jast.ast.nodes.collections.CommentIterator;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.ast.nodes.collections.ConstructorArray;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.ExpressionArray;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterArray;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.ImportArray;
import jast.ast.nodes.collections.ImportIterator;
import jast.ast.nodes.collections.InitializerArray;
import jast.ast.nodes.collections.InitializerIterator;
import jast.ast.nodes.collections.LocalVariableArray;
import jast.ast.nodes.collections.LocalVariableIterator;
import jast.ast.nodes.collections.MethodArray;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.PackageArray;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.TypeArray;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.nodes.collections.VariableInitializerArray;
import jast.ast.nodes.collections.VariableInitializerIterator;
import jast.helpers.PropertySet;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;
import jast.helpers.StringMap;

import java.lang.reflect.Method;
import java.util.Hashtable;

public class Sizer
{
    private Hashtable _sizeInfos = new Hashtable();

    public SizeInfo getSizeInfo(String name)
    {
        SizeInfo result = (SizeInfo)_sizeInfos.get(name);

        if (result == null)
        {
            result = new SizeInfo(name);
            _sizeInfos.put(name, result);
        }
        return result;
    }

    public long getSize(Object obj)
    {
        return sizeOf(obj);
    }

    private String getTypeName(Object obj)
    {
        String name = obj.getClass().getName();

        // An array ?
        if (name.startsWith("["))
        {
            return "[]";
        }
        if (name.lastIndexOf(".") >= 0)
        {
            name = name.substring(name.lastIndexOf(".")+1);
        }
        return name;
    }

    private long sizeOf(ContainedNode node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOfReference() +
                   sizeOf((Node)node);
        }
    }

    private long sizeOf(Node node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getStartPosition()) +
                   sizeOf(node.getFinishPosition()) +
                   sizeOf(node.getComments()) +
                   sizeOf(node.getProperties());
        }
    }

    private long sizeOf(AnonymousClassDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getBaseType()) +
                          sizeOf((ClassDeclaration)node);

            getSizeInfo("AnonymousClassDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(ArgumentList node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getArguments()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ArgumentList").addSize(result);
            return result;
        }
    }

    private long sizeOf(ArrayAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getIndexExpression()) +
                          sizeOf((TrailingPrimary)node);

            getSizeInfo("ArrayAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(ArrayCreation node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getCreatedType()) +
                          sizeOf(node.getDimExpressions()) +
                          sizeOf(node.getInitializer()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ArrayCreation").addSize(result);
            return result;
        }
    }

    private long sizeOf(ArrayInitializer node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInitializers()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ArrayInitializer").addSize(result);
            return result;
        }
    }

    private long sizeOf(ArrayType node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((ClassDeclaration)node);

            getSizeInfo("ArrayType").addSize(result);
            return result;
        }
    }

    private long sizeOf(AssignmentExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getLeftHandSide()) +
                          sizeOfInt() +
                          sizeOf(node.getValueExpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("AssignmentExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(BinaryExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getLeftOperand()) +
                          sizeOfInt() +
                          sizeOf(node.getRightOperand()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("BinaryExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(Block node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getBlockStatements()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("Block").addSize(result);
            return result;
        }
    }

    private long sizeOf(BooleanLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfBoolean() +
                          sizeOf((Literal)node);

            getSizeInfo("BooleanLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(BreakStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((ControlFlowStatement)node);

            getSizeInfo("BreakStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(CaseBlock node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getCases()) +
                          sizeOfBoolean() +
                          sizeOf((Block)node);

            getSizeInfo("CaseBlock").addSize(result);
            return result;
        }
    }

    private long sizeOf(CatchClause node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getFormalParameter()) +
                          sizeOf(node.getCatchBlock()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("CatchClause").addSize(result);
            return result;
        }
    }

    private long sizeOf(CharacterLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfChar() +
                          sizeOf((Literal)node);

            getSizeInfo("CharacterLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(ClassAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getReferencedType()) +
                          sizeOf(node.getType()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ClassAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(ClassDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getBaseClass()) +
                          sizeOfBoolean() +
                          sizeOf(node.getInitializers()) +
                          sizeOf(node.getConstructors()) +
                          sizeOf((TypeDeclaration)node);

            getSizeInfo("ClassDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(BlockStatementArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (BlockStatementIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(CaseBlockArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (CaseBlockIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(CatchClauseArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (CatchClauseIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(CommentArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (CommentIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(CompilationUnitArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (CompilationUnitIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(ConstructorArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (ConstructorIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(ExpressionArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (ExpressionIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(FieldArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (FieldIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(FormalParameterArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (FormalParameterIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(ImportArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (ImportIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(InitializerArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (InitializerIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(LocalVariableArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (LocalVariableIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(MethodArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (MethodIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(PackageArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (PackageIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(TypeArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (TypeIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(TypeDeclarationArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (TypeDeclarationIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(VariableInitializerArray node)
    {
        long result = 0l;

        if (node != null)
        {
            for (VariableInitializerIterator it = node.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(Comment node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getText()) +
                          sizeOfBoolean() +
                          sizeOf(node.getTags()) +
                          sizeOf((Node)node);

            getSizeInfo("Comment").addSize(result);
            return result;
        }
    }

    private long sizeOf(CompilationUnit node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getName()) +
                          sizeOfReference() +
                          sizeOf(node.getImportDeclarations()) +
                          3*sizeOfBoolean() +
                          sizeOf(node.getTypeDeclarations()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("CompilationUnit").addSize(result);
            return result;
        }
    }

    private long sizeOf(ConditionalExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getCondition()) +
                          sizeOf(node.getTrueExpression()) +
                          sizeOf(node.getFalseExpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ConditionalExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(ConditionalStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getCondition()) +
                   sizeOfBoolean() +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(ConstructorDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((InvocableDeclaration)node);

            getSizeInfo("ConstructorDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(ConstructorInvocation node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfBoolean() +
                          sizeOfReference() +
                          sizeOf((Invocation)node);

            getSizeInfo("ConstructorInvocation").addSize(result);
            return result;
        }
    }

    private long sizeOf(ContinueStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((ControlFlowStatement)node);

            getSizeInfo("ContinueStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(ControlFlowStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOfReference() +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(DoWhileStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((LoopStatement)node);

            getSizeInfo("DoWhileStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(EmptyStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((ContainedNode)node);

            getSizeInfo("EmptyStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(ExpressionStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getExpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ExpressionStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(FieldAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getFieldName()) +
                          sizeOfReference() +
                          sizeOf((TrailingPrimary)node);

            getSizeInfo("FieldAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(FieldDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((VariableDeclaration)node);

            getSizeInfo("FieldDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(FloatingPointLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfDouble() +
                          sizeOfBoolean() +
                          sizeOf((Literal)node);

            getSizeInfo("FloatingPointLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(FormalParameter node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((VariableDeclaration)node);

            getSizeInfo("FormalParameter").addSize(result);
            return result;
        }
    }

    private long sizeOf(FormalParameterList node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getParameters()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("FormalParameterList").addSize(result);
            return result;
        }
    }

    private long sizeOf(ForStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInitList()) +
                          sizeOf(node.getInitDeclarations()) +
                          sizeOf(node.getUpdateList()) +
                          sizeOf((LoopStatement)node);

            getSizeInfo("ForStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(IfThenElseStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getTrueStatement()) +
                          sizeOf(node.getFalseStatement()) +
                          sizeOf((ConditionalStatement)node);

            getSizeInfo("IfThenElseStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(ImportDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getImportedPackageOrType()) +
                          sizeOfBoolean() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ImportDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(Initializer node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getBody()) +
                          sizeOfBoolean() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("Initializer").addSize(result);
            return result;
        }
    }

    private long sizeOf(InstanceofExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInnerExpression()) +
                          sizeOf(node.getReferencedType()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("InstanceofExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(Instantiation node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInstantiatedType()) +
                          sizeOfReference() +
                          sizeOf(node.getAnonymousClass()) +
                          sizeOf((Invocation)node);

            getSizeInfo("Instantiation").addSize(result);
            return result;
        }
    }

    private long sizeOf(IntegerLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfLong() +
                          3*sizeOfBoolean() +
                          sizeOf((Literal)node);

            getSizeInfo("IntegerLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(InterfaceDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((TypeDeclaration)node);

            getSizeInfo("InterfaceDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(InvocableDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getModifiers()) +
                   sizeOf(node.getName()) +
                   sizeOf(node.getParameterList()) +
                   sizeOf(node.getThrownExceptions()) +
                   sizeOf(node.getBody()) +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(Invocation node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getArgumentList()) +
                   sizeOf((TrailingPrimary)node);
        }
    }

    private long sizeOf(LabeledStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getName()) +
                          sizeOf(node.getStatement()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("LabeledStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(Literal node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.asString()) +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(LocalVariableDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((VariableDeclaration)node);

            getSizeInfo("LocalVariableDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(LoopStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getLoopStatement()) +
                   sizeOf((ConditionalStatement)node);
        }
    }

    private long sizeOf(MethodDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getReturnType()) +
                          sizeOf((InvocableDeclaration)node);

            getSizeInfo("MethodDeclaration").addSize(result);
            return result;
        }
    }

    private long sizeOf(MethodInvocation node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfReference() +
                          sizeOf(node.getMethodName()) +
                          sizeOf((Invocation)node);

            getSizeInfo("MethodInvocation").addSize(result);
            return result;
        }
    }

    private long sizeOf(Modifiers node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = 11*sizeOfBoolean() +
                          sizeOf((Node)node);

            getSizeInfo("Modifiers").addSize(result);
            return result;
        }
    }

    private long sizeOf(NullLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((Literal)node);

            getSizeInfo("NullLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(Package node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getQualifiedName()) +
                          node.getTypes().getCount()*sizeOfReference() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("Package").addSize(result);
            return result;
        }
    }

    private long sizeOf(ParenthesizedExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInnerExpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ParenthesizedExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(PostfixExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInnerExpression()) +
                          sizeOfBoolean() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("PostfixExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(PrimitiveType node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getName()) +
                          sizeOf(node.getSignature()) +
                          sizeOf((Node)node);

            getSizeInfo("PrimitiveType").addSize(result);
            return result;
        }
    }

    private long sizeOf(ReturnStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getReturnValue()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ReturnStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(SelfAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getTypeAccess()) +
                          sizeOf(node.getType()) +
                          sizeOfBoolean() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("SelfAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(SingleInitializer node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getInitEpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("SingleInitializer").addSize(result);
            return result;
        }
    }

    private long sizeOf(StatementExpressionList node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getExpressions()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("StatementExpressionList").addSize(result);
            return result;
        }
    }

    private long sizeOf(StringLiteral node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getValue()) +
                          sizeOf(node.getType()) +
                          sizeOf((Literal)node);

            getSizeInfo("StringLiteral").addSize(result);
            return result;
        }
    }

    private long sizeOf(SwitchStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getSwitchExpression()) +
                          sizeOf(node.getCaseBlocks()) +
                          sizeOfBoolean() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("SwitchStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(SynchronizedStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getLockExpression()) +
                          sizeOf(node.getBlock()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("SynchronizedStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(ThrowStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getThrowExpression()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("ThrowStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(TrailingPrimary node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getBaseExpression()) +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(TryStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getTryBlock()) +
                          sizeOf(node.getCatchClauses()) +
                          sizeOf(node.getFinallyClause()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("TryStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(Type node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfBoolean() +
                          sizeOfInt() +
                          sizeOf(node.getPrimitiveBaseType()) +
                          sizeOf(node.getReferenceBaseType()) +
                          sizeOfReference() +
                          sizeOf((Node)node);

            getSizeInfo("Type").addSize(result);
            return result;
        }
    }

    private long sizeOf(TypeAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getType()) +
                          sizeOf((TrailingPrimary)node);

            getSizeInfo("TypeAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(TypeDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return 2*sizeOfReference() +
                   sizeOf(node.getModifiers()) +
                   sizeOf(node.getName()) +
                   sizeOf(node.getBaseInterfaces()) +
                   sizeOf(node.getInnerTypes()) +
                   sizeOf(node.getFields()) +
                   sizeOf(node.getMethods()) +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(UnaryExpression node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfInt() +
                          sizeOf(node.getInnerExpression()) +
                          sizeOf(node.getCastType()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("UnaryExpression").addSize(result);
            return result;
        }
    }

    private long sizeOf(UnresolvedAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getParts()) +
                          sizeOf((ContainedNode)node);

            getSizeInfo("UnresolvedAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(VariableAccess node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOfReference() +
                          sizeOf((ContainedNode)node);

            getSizeInfo("VariableAccess").addSize(result);
            return result;
        }
    }

    private long sizeOf(VariableDeclaration node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            return sizeOf(node.getModifiers()) +
                   sizeOf(node.getType()) +
                   sizeOf(node.getName()) +
                   sizeOf(node.getInitializer()) +
                   sizeOf((ContainedNode)node);
        }
    }

    private long sizeOf(WhileStatement node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf((LoopStatement)node);

            getSizeInfo("WhileStatement").addSize(result);
            return result;
        }
    }

    private long sizeOf(ParsePosition pos)
    {
        if (pos == null)
        {
            return 0l;
        }
        else
        {
            return 3*sizeOfInt() + sizeOfBoolean();
        }
    }

    private long sizeOf(Project node)
    {
        if (node == null)
        {
            return 0l;
        }
        else
        {
            long result = sizeOf(node.getName()) +
                          sizeOf(node.getPackages()) +
                          sizeOf(node.getCompilationUnits()) +
                          sizeOf(node.getArrayType()) +
                          sizeOf((Node)node);

            getSizeInfo("Project").addSize(result);
            return result;
        }
    }

    private long sizeOf(PropertySet props)
    {
        long result = 0l;

        if (props != null)
        {
            String key;

            for (StringIterator it = props.getKeyIterator(); it.hasNext();)
            {
                key     = it.getNext();
                result += sizeOf(key) + sizeOf(props.getProperty(key));
            }
        }
        return result;
    }

    private long sizeOf(StringArray arr)
    {
        long result = 0l;

        if (arr != null)
        {
            for (StringIterator it = arr.getIterator(); it.hasNext();)
            {
                result += sizeOf(it.getNext());
            }
        }
        return result;
    }

    private long sizeOf(StringMap map)
    {
        long result = 0l;

        if (map != null)
        {
            String key;

            for (StringIterator it = map.getKeyIterator(); it.hasNext();)
            {
                key     = it.getNext();
                result += sizeOf(key) + sizeOf(map.get(key));
            }
        }
        return result;
    }

    private long sizeOf(Object obj)
    {
        if (obj == null)
        {
            return 0l;
        }

        String name = getTypeName(obj);

        // An array ?
        if (!name.equals("[]"))
        {
            Class[]  params = new Class[1];
            Object[] args   = new Object[1];

            params[0] = obj.getClass();
            args[0]   = obj;

            try
            {
                Method handler = getClass().getDeclaredMethod("sizeOf", params);

                if (handler != null)
                {
                    Long result = (Long)handler.invoke(this, args);

                    return result.longValue();
                }
            }
            catch (Exception ex)
            {
                System.out.println("Error while getting size of "+name);
                ex.printStackTrace();
            }
        }
        return 0l;
    }

    private long sizeOf(String str)
    {
        return str == null ? 0l : str.getBytes().length;
    }

    private long sizeOfBoolean()
    {
        return 1l;
    }

    private long sizeOfChar()
    {
        // assuming unicode
        return 2l;
    }

    private long sizeOfDouble()
    {
        return 6l;
    }

    private long sizeOfFloat()
    {
        return 6l;
    }

    private long sizeOfInt()
    {
        return 2l;
    }

    private long sizeOfLong()
    {
        return 4l;
    }

    private long sizeOfNull()
    {
        return 1l;
    }

    private long sizeOfReference()
    {
        return 4l;
    }
}
