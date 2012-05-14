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
package jast.ui;

import jast.Global;
import jast.ast.Node;
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
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.LocalVariableIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.nodes.collections.VariableInitializerIterator;

import java.lang.reflect.Method;

// Creates a string representation for a node - something like
// an external toString
// The individual stringify methods can be redefined in subclasses to
// change the representation
public class NodeStringifier
{
    // used to print empty blocks
    // note that we do not store the string representation as
    // the asString(Block) method of a derived class could rely
    // on the initialization done by the constructor
    private Block dummyBlock = Global.getFactory().createBlock();

    public NodeStringifier()
    {}

    public String asString(Node node)
    {
        StringBuffer result = new StringBuffer();

        asString(node, result);
        return result.toString();
    }

    private void asString(Node node, StringBuffer result)
    {
        if (node != null)
        {
            Class[]  params = { node.getClass(), StringBuffer.class };
            Object[] args   = { node, result };

            try
            {
                Method method = getClass().getDeclaredMethod("asString", params);

                if (method != null)
                {
                    method.invoke(this, args);
                }
            }
            catch (Exception ex)
            {}
        }
    }

    protected void asString(AnonymousClassDeclaration node, StringBuffer result)
    {
        result.append(node.getBaseType().toString());
        asString(dummyBlock, result);
    }

    protected void asString(ArgumentList node, StringBuffer result)
    {
        for (ExpressionIterator it = node.getArguments().getIterator(); it.hasNext(); )
        {
            asString(it.getNext(), result);
            if (it.hasNext())
            {
                result.append(", ");
            }
        }
    }

    protected void asString(ArrayAccess node, StringBuffer result)
    {
        asString(node.getBaseExpression(), result);
        result.append("[");
        asString(node.getIndexExpression(), result);
        result.append("]");
    }

    protected void asString(ArrayCreation node, StringBuffer result)
    {
        result.append("new ");
        result.append(node.getCreatedType().getBaseName());

        for (ExpressionIterator it = node.getDimExpressions().getIterator(); it.hasNext(); )
        {
            result.append("[");
            asString(it.getNext(), result);
            result.append("]");
        }
        if (node.isInitialized())
        {
            result.append(" = ");
            asString(node.getInitializer(), result);
        }
    }

    protected void asString(ArrayInitializer node, StringBuffer result)
    {
        result.append("{ ");

        for (VariableInitializerIterator it = node.getInitializers().getIterator(); it.hasNext(); )
        {
            asString(it.getNext(), result);
            if (it.hasNext())
            {
                result.append(", ");
            }
        }
        result.append(" }");
    }

    protected void asString(AssignmentExpression node, StringBuffer result)
    {
        asString(node.getLeftHandSide(), result);
        switch (node.getOperator())
        {
            case AssignmentExpression.ASSIGN_OP :
                result.append(" = ");
                break;
            case AssignmentExpression.MULTIPLY_ASSIGN_OP :
                result.append(" *= ");
                break;
            case AssignmentExpression.DIVIDE_ASSIGN_OP :
                result.append(" /= ");
                break;
            case AssignmentExpression.MOD_ASSIGN_OP :
                result.append(" %= ");
                break;
            case AssignmentExpression.PLUS_ASSIGN_OP :
                result.append(" += ");
                break;
            case AssignmentExpression.MINUS_ASSIGN_OP :
                result.append(" -= ");
                break;
            case AssignmentExpression.SHIFT_LEFT_ASSIGN_OP :
                result.append(" <<= ");
                break;
            case AssignmentExpression.SHIFT_RIGHT_ASSIGN_OP :
                result.append(" >>= ");
                break;
            case AssignmentExpression.ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP :
                result.append(" >>>= ");
                break;
            case AssignmentExpression.BITWISE_AND_ASSIGN_OP :
                result.append(" &= ");
                break;
            case AssignmentExpression.BITWISE_XOR_ASSIGN_OP :
                result.append(" ^= ");
                break;
            case AssignmentExpression.BITWISE_OR_ASSIGN_OP :
                result.append(" |= ");
                break;
        }
        asString(node.getValueExpression(), result);
    }

    protected void asString(BinaryExpression node, StringBuffer result)
    {
        asString(node.getLeftOperand(), result);

        switch (node.getOperator())
        {
            case BinaryExpression.MULTIPLY_OP :
                result.append(" * ");
                break;
            case BinaryExpression.DIVIDE_OP :
                result.append(" / ");
                break;
            case BinaryExpression.MOD_OP :
                result.append(" % ");
                break;
            case BinaryExpression.PLUS_OP :
                result.append(" + ");
                break;
            case BinaryExpression.MINUS_OP :
                result.append(" - ");
                break;
            case BinaryExpression.SHIFT_LEFT_OP :
                result.append(" << ");
                break;
            case BinaryExpression.SHIFT_RIGHT_OP :
                result.append(" >> ");
                break;
            case BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP :
                result.append(" >>> ");
                break;
            case BinaryExpression.LOWER_OP :
                result.append(" < ");
                break;
            case BinaryExpression.GREATER_OP :
                result.append(" > ");
                break;
            case BinaryExpression.LOWER_OR_EQUAL_OP :
                result.append(" <= ");
                break;
            case BinaryExpression.GREATER_OR_EQUAL_OP :
                result.append(" >= ");
                break;
            case BinaryExpression.BITWISE_AND_OP :
                result.append(" & ");
                break;
            case BinaryExpression.BITWISE_XOR_OP :
                result.append(" ^ ");
                break;
            case BinaryExpression.BITWISE_OR_OP :
                result.append(" | ");
                break;
            case BinaryExpression.AND_OP :
                result.append(" && ");
                break;
            case BinaryExpression.OR_OP :
                result.append(" || ");
                break;
            case BinaryExpression.EQUAL_OP :
                result.append(" == ");
                break;
            case BinaryExpression.NOT_EQUAL_OP :
                result.append(" != ");
                break;
        }
        asString(node.getRightOperand(), result);
    }

    protected void asString(Block node, StringBuffer result)
    {
        result.append(" {}");
    }

    protected void asString(BooleanLiteral node, StringBuffer result)
    {
        result.append(node.getValue() ? "true" : "false");
    }

    protected void asString(BreakStatement node, StringBuffer result)
    {
        result.append("break");

        if (node.hasTarget())
        {
            result.append(" ");
            result.append(node.getTarget().getName());
        }
        result.append(";");
    }

    protected void asString(CaseBlock node, StringBuffer result)
    {
        for (ExpressionIterator it = node.getCases().getIterator(); it.hasNext(); )
        {
            result.append("case ");
            asString(it.getNext(), result);
            result.append(":");
        }
        if (node.hasDefault())
        {
            result.append("default:");
        }
    }

    protected void asString(CatchClause node, StringBuffer result)
    {
        result.append("catch");
        result.append(" (");
        asString(node.getFormalParameter(), result);
        result.append(")");
        asString(node.getCatchBlock(), result);
    }

    protected void asString(CharacterLiteral node, StringBuffer result)
    {
        result.append(node.asString());
    }

    protected void asString(ClassAccess node, StringBuffer result)
    {
        asString(node.getReferencedType(), result);
        result.append(".");
        result.append("class");
    }

        protected void asString(ClassDeclaration node, StringBuffer result)
        {
            asString(node.getModifiers(), result);
            result.append("class ");
            result.append(node.getName());

            if ((node.getBaseClass() != null) && !node.getBaseClass().getQualifiedName().equals("java.lang.Object"))
            {
                result.append(" extends ");
                result.append(node.getBaseClass().getBaseName());
            }
    /*
            if (node.hasBaseInterfaces())
            {
                result.append(" implements ");
                for (TypeIterator it = node.getBaseInterfaces().getIterator(); it.hasNext();)
                {
                    result.append(it.getNext().getBaseName());
                    if (it.hasNext())
                    {
                        result.append(", ");
                    }
                }
            }
    */
            asString(dummyBlock, result);
        }

    protected void asString(Comment node, StringBuffer result)
    {
        result.append(node.getText());
    }

    protected void asString(CompilationUnit node, StringBuffer result)
    {
        result.append("Filename: ");
        result.append(node.getName());
    }

    protected void asString(ConditionalExpression node, StringBuffer result)
    {
        result.append("(");
        asString(node.getCondition(), result);
        result.append(" ? ");
        asString(node.getTrueExpression(), result);
        result.append(" : ");
        asString(node.getFalseExpression(), result);
        result.append(")");
    }

    protected void asString(ConstructorInvocation node, StringBuffer result)
    {
        if (node.isTrailing())
        {
            asString(node.getBaseExpression(), result);
            result.append(".");
        }

        if (node.ofBaseClass())
        {
            result.append("super");
        }
        else
        {
            result.append("this");
        }

        result.append("(");
        if (node.hasArguments())
        {
            asString(node.getArgumentList(), result);
        }
        result.append(")");
    }

    protected void asString(ContinueStatement node, StringBuffer result)
    {
        result.append("continue");

        if (node.hasTarget())
        {
            result.append(" ");
            result.append(node.getTarget().getName());
        }

        result.append(";");
    }

    protected void asString(DoWhileStatement node, StringBuffer result)
    {
        result.append("do");

        if (node.getLoopStatement() instanceof Block)
        {
            asString(node.getLoopStatement(), result);
        }

        result.append(" while (");
        asString(node.getCondition(), result);
        result.append(");");
    }

    protected void asString(EmptyStatement node, StringBuffer result)
    {
        result.append(";");
    }

    protected void asString(ExpressionStatement node, StringBuffer result)
    {
        asString(node.getExpression(), result);
        result.append(";");
    }

    protected void asString(FieldAccess node, StringBuffer result)
    {
        if (node.isTrailing())
        {
            asString(node.getBaseExpression(), result);
            result.append(".");
        }
        result.append(node.getFieldName());
    }

    protected void asString(FieldDeclaration node, StringBuffer result)
    {
        asString(node.getModifiers(), result);
        asString(node.getType(), result);
        result.append(" ");
        result.append(node.getName());

        if (node.hasInitializer())
        {
            result.append(" = ");
            asString(node.getInitializer(), result);
        }

        result.append(";");
    }

    protected void asString(FloatingPointLiteral node, StringBuffer result)
    {
        result.append(node.asString());
    }

    protected void asString(FormalParameter node, StringBuffer result)
    {
        asString(node.getModifiers(), result);
        asString(node.getType(), result);
        result.append(" ");
        result.append(node.getName());
    }

    protected void asString(FormalParameterList node, StringBuffer result)
    {
        if (node != null)
        {
            for (FormalParameterIterator it = node.getParameters().getIterator(); it.hasNext(); )
            {
                asString(it.getNext(), result);
                if (it.hasNext())
                {
                    result.append(", ");
                }
            }
        }
    }

    protected void asString(ForStatement node, StringBuffer result)
    {
        result.append("for (");

        if (node.hasInitDeclarations())
        {
            for (LocalVariableIterator it = node.getInitDeclarations().getIterator(); it.hasNext(); )
            {
                asString(it.getNext(), result);
            }
        }
        else if (node.hasInitList())
        {
            asString(node.getInitList(), result);
        }
        else
        {
            result.append("; ");
        }

        if (node.hasCondition())
        {
            asString(node.getCondition(), result);
        }
        result.append("; ");

        if (node.hasUpdateList())
        {
            asString(node.getUpdateList(), result);
        }
        result.append(")");

        if (node.getLoopStatement() instanceof Block)
        {
            asString(node.getLoopStatement(), result);
        }
        else
        {
            result.append(";");
        }
    }

    protected void asString(IfThenElseStatement node, StringBuffer result)
    {
        result.append("if (");
        asString(node.getCondition(), result);
        result.append(")");

        if (node.getTrueStatement() instanceof Block)
        {
            asString(node.getTrueStatement(), result);
        }
        else
        {
            result.append("  ");
        }

        if (node.hasElse())
        {
            result.append(" else");
            if (node.getFalseStatement() instanceof Block)
            {
                asString(node.getFalseStatement(), result);
            }
            else
            {
                result.append("   ");
            }
        }
    }

    protected void asString(ImportDeclaration node, StringBuffer result)
    {
        result.append("import ");
        result.append(node.getImportedPackageOrType());

        if (node.isOnDemand())
        {
            result.append(".*;");
        }
        else
        {
            result.append(";");
        }
    }

    protected void asString(Initializer node, StringBuffer result)
    {
        if (node.isStatic())
        {
            result.append("static");
        }
        asString(node.getBody(), result);
    }

    protected void asString(InstanceofExpression node, StringBuffer result)
    {
        asString(node.getInnerExpression(), result);
        result.append(" instanceof ");
        asString(node.getReferencedType(), result);
    }

    protected void asString(Instantiation node, StringBuffer result)
    {
        result.append("new ");
        asString(node.getType(), result);
        result.append("(");

        if (node.hasArguments())
        {
            asString(node.getArgumentList(), result);
        }
        result.append(")");

        if (node.withAnonymousClass())
        {
            asString(node.getAnonymousClass(), result);
        }
    }

    protected void asString(IntegerLiteral node, StringBuffer result)
    {
        result.append(node.asString());
    }

        protected void asString(InterfaceDeclaration node, StringBuffer result)
        {
            asString(node.getModifiers(), result);
            result.append("interface ");
            result.append(node.getName());
    /*
            if (node.hasBaseInterfaces())
            {
                result.append(" extends ");

                for (TypeIterator it = node.getBaseInterfaces().getIterator(); it.hasNext();)
                {
                    result.append(it.getNext().getBaseName());
                    if (it.hasNext())
                    {
                        result.append(", ");
                    }
                }
            }
    */
            asString(dummyBlock, result);
        }

    protected void asString(LabeledStatement node, StringBuffer result)
    {
        result.append(node.getName());
        result.append(": ");
        asString(node.getStatement(), result);
    }

    protected void asString(LocalVariableDeclaration node, StringBuffer result)
    {
        asString(node.getModifiers(), result);
        asString(node.getType(), result);
        result.append(" ");
        result.append(node.getName());

        if (node.hasInitializer())
        {
            result.append(" = ");
            asString(node.getInitializer(), result);
        }

        result.append(";");
    }

    protected void asString(MethodDeclaration node, StringBuffer result)
    {
        asString(node.getModifiers(), result);

        if (node.hasReturnType())
        {
            asString(node.getReturnType(), result);
        }
        else
        {
            result.append("void");
        }
        result.append(" ");
        result.append(node.getName());
        result.append("(");

        if (node.getParameterList() != null)
        {
            asString(node.getParameterList(), result);
        }
        result.append(")");

        if (!node.getThrownExceptions().isEmpty())
        {
            result.append(" throws ");

            for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext(); )
            {
                asString(it.getNext(), result);
                if (it.hasNext())
                {
                    result.append(", ");
                }
            }
        }

        if (node.hasBody())
        {
            asString(node.getBody(), result);
        }
        else
        {
            result.append(";");
        }
    }

    protected void asString(MethodInvocation node, StringBuffer result)
    {
        if (node.isTrailing())
        {
            asString(node.getBaseExpression(), result);
            result.append(".");
        }

        result.append(node.getMethodName());
        result.append("(");

        if (node.hasArguments())
        {
            asString(node.getArgumentList(), result);
        }
        result.append(")");
    }

    protected void asString(Modifiers node, StringBuffer result)
    {
        if (node != null)
        {
            result.append(node.toString());
        }
    }

    protected void asString(NullLiteral node, StringBuffer result)
    {
        result.append("null");
    }

    protected void asString(jast.ast.nodes.Package node, StringBuffer result)
    {
        result.append("package ");
        result.append(node.getQualifiedName());
        result.append(";");
    }

    protected void asString(ParenthesizedExpression node, StringBuffer result)
    {
        result.append("(");
        asString(node.getInnerExpression(), result);
        result.append(")");
    }

    protected void asString(PostfixExpression node, StringBuffer result)
    {
        asString(node.getInnerExpression(), result);

        if (node.isIncrement())
        {
            result.append("++");
        }
        else
        {
            result.append("--");
        }
    }

    protected void asString(PrimitiveType node, StringBuffer result)
    {
        result.append(node.getName());
    }

    protected void asString(ReturnStatement node, StringBuffer result)
    {
        result.append("return");

        if (node.hasReturnValue())
        {
            result.append(" ");
            asString(node.getReturnValue(), result);
        }
        result.append(";");
    }

    protected void asString(SelfAccess node, StringBuffer result)
    {
        if (node.isQualified())
        {
            asString(node.getTypeAccess(), result);
            result.append(".");
        }

        if (node.isSuper())
        {
            result.append("super");
        }
        else
        {
            result.append("this");
        }
    }

    protected void asString(SingleInitializer node, StringBuffer result)
    {
        asString(node.getInitEpression(), result);
    }

    protected void asString(StatementExpressionList node, StringBuffer result)
    {
        for (ExpressionIterator it = node.getExpressions().getIterator(); it.hasNext(); )
        {
            asString(it.getNext(), result);
            if (it.hasNext())
            {
                result.append(", ");
            }
        }
    }

    protected void asString(StringLiteral node, StringBuffer result)
    {
        result.append(node.asString());
    }

    protected void asString(SwitchStatement node, StringBuffer result)
    {
        result.append("switch (");
        asString(node.getSwitchExpression(), result);
        result.append(")");
        asString(dummyBlock, result);
    }

    protected void asString(SynchronizedStatement node, StringBuffer result)
    {
        result.append("synchronized (");
        asString(node.getLockExpression(), result);
        result.append(")");

        if (node.getBlock() != null)
        {
            asString(node.getBlock(), result);
        }
    }

    protected void asString(ThrowStatement node, StringBuffer result)
    {
        result.append("throw ");
        asString(node.getThrowExpression(), result);
        result.append(";");
    }

    protected void asString(TryStatement node, StringBuffer result)
    {
        result.append("try");
        asString(node.getTryBlock(), result);
        result.append(" ");

        for (CatchClauseIterator it = node.getCatchClauses().getIterator(); it.hasNext(); )
        {
            asString(it.getNext(), result);
        }
        if (node.hasFinallyClause())
        {
            result.append("finally");
            asString(node.getFinallyClause(), result);
        }
    }

    protected void asString(Type node, StringBuffer result)
    {
        if (node != null)
        {
            if (node.toString().indexOf(".") < 0)
            {
                result.append(node.toString());
            }
            else
            {
                String str = node.toString();

                result.append(str.substring(str.lastIndexOf(".")+1, str.length()));
            }
        }
    }

    protected void asString(TypeAccess node, StringBuffer result)
    {
        if (node.isTrailing())
        {
            asString(node.getBaseExpression(), result);
            result.append(".");
        }
        asString(node.getType(), result);
    }

    protected void asString(UnaryExpression node, StringBuffer result)
    {
        switch (node.getOperator())
        {
            case UnaryExpression.PLUS_OP :
                result.append("+");
                break;
            case UnaryExpression.MINUS_OP :
                result.append("-");
                break;
            case UnaryExpression.INCREMENT_OP :
                result.append("++");
                break;
            case UnaryExpression.DECREMENT_OP :
                result.append("--");
                break;
            case UnaryExpression.COMPLEMENT_OP :
                result.append("~");
                break;
            case UnaryExpression.NEGATION_OP :
                result.append("!");
                break;
            case UnaryExpression.CAST_OP :
                result.append("(");
                asString(node.getCastType(), result);
                result.append(")");
                break;
        }
        asString(node.getInnerExpression(), result);
    }

    protected void asString(UnresolvedAccess node, StringBuffer result)
    {
        result.append(node.toString());
    }

    protected void asString(VariableAccess node, StringBuffer result)
    {
        result.append(node.getVariableName());
    }

    protected void asString(WhileStatement node, StringBuffer result)
    {
        result.append("while (");
        asString(node.getCondition(), result);
        result.append(")");
        if (node.getLoopStatement() instanceof Block)
        {
            asString(node.getLoopStatement(), result);
        }
        else
        {
            result.append(";");
        }
    }

    protected void asString(Project node, StringBuffer result)
    {
        result.append("Project: ");
        result.append(node.getName());
    }

    protected void asString(ConstructorDeclaration node, StringBuffer result)
    {
        asString(node.getModifiers(), result);
        result.append(node.getName());
        result.append("(");

        if (node.hasParameters())
        {
            asString(node.getParameterList(), result);
        }

        result.append(")");

        if (node.getThrownExceptions().getCount() > 0)
        {
            result.append(" throws ");
            for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext(); )
            {
                asString(it.getNext(), result);
                if (it.hasNext())
                {
                    result.append(", ");
                }
            }
        }

        asString(node.getBody(), result);
    }
}
