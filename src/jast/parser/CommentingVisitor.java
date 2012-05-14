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
package jast.parser;
import jast.ast.Node;
import jast.ast.ParsePosition;
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
import jast.ast.nodes.Comment;
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
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.CaseBlockIterator;
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.CommentArray;
import jast.ast.nodes.collections.CommentIterator;
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
import jast.ast.visitor.ReflectionVisitor;
import jast.helpers.Comparable;
import jast.helpers.ObjectIterator;
import jast.helpers.SortedArray;

import java.util.Hashtable;

public class CommentingVisitor extends ReflectionVisitor
{
    // Contains the nodes which are not commented at all
    private static final Hashtable UNCOMMENTED_NODES = new Hashtable();

    static
    {
        UNCOMMENTED_NODES.put("ArgumentList",            "ArgumentList");
        UNCOMMENTED_NODES.put("FormalParameterList",     "FormalParameterList");
        UNCOMMENTED_NODES.put("Modifiers",               "Modifiers");
        UNCOMMENTED_NODES.put("StatementExpressionList", "StatementExpressionList");
    }

    private SortedArray _comments;

    public CommentingVisitor()
    {
        // we do not need handler for all nodes
        warnIfHandlerMissing(false);
    }

        public void comment(Node node, CommentArray comments)
        {
            if ((node == null) || (comments == null) || comments.isEmpty())
            {
                return;
            }

            Comment            comment;
            CommentAssociation assoc;

            _comments = new SortedArray();

            for (CommentIterator it = comments.getIterator(); it.hasNext();)
            {
                comment = it.getNext();
                _comments.add(comment.getStartPosition(),
                              new CommentAssociation(comment));
            }

            visit(node);

            for (ObjectIterator objIt = _comments.getIterator(); objIt.hasNext();)
            {
                assoc = (CommentAssociation)objIt.getNext();
                if (assoc.getOwner() == null)
                {
                    assoc.setOwner(node);
                }
    /*
                Global.getOutput().addNote("[CommentingVisitor.comment] Moving comment ["+
                                            assoc.getComment().getStartPosition()+"-"+
                                            assoc.getComment().getFinishPosition()+
                                            "] to node of type "+assoc.getOwner().getClass().getName()+
                                            " ["+assoc.getOwner().getStartPosition()+"-"+
                                            assoc.getOwner().getFinishPosition()+"]",
                                            null);
    */
                assoc.addToOwner();
            }

            _comments = null;
        }

        public void comment(Node node, SortedArray comments)
        {
            if ((node == null) || (comments == null) || comments.isEmpty())
            {
                return;
            }

            Comparable         key;
            CommentAssociation assoc;

            _comments = new SortedArray();

            for (int idx = 0; idx < comments.getCount(); idx++)
            {
                _comments.add(comments.getKey(idx),
                              new CommentAssociation((Comment)comments.get(idx)));
            }

            visit(node);

            for (ObjectIterator objIt = _comments.getIterator(); objIt.hasNext();)
            {
                assoc = (CommentAssociation)objIt.getNext();
                if (assoc.getOwner() == null)
                {
                    assoc.setOwner(node);
                }
    /*
                Global.getOutput().addNote("[CommentingVisitor.comment] Moving comment ["+
                                            assoc.getComment().getStartPosition()+"-"+
                                            assoc.getComment().getFinishPosition()+
                                            "] to node of type "+assoc.getOwner().getClass().getName()+
                                            " ["+assoc.getOwner().getStartPosition()+"-"+
                                            assoc.getOwner().getFinishPosition()+"]",
                                            null);
    */
                assoc.addToOwner();
            }

            _comments = null;
        }

    private boolean isCommentable(Node node)
    {
        String name = node.getClass().getName();
        int    pos  = name.lastIndexOf(".");

        if (pos >= 0)
        {
            name = name.substring(pos+1);
        }
        return !UNCOMMENTED_NODES.containsKey(name);
    }

    public void postVisit(Object obj)
    {
        if (!(obj instanceof Node))
        {
            return;
        }

        Node node = (Node)obj;

        if (!isCommentable(node))
        {
            return;
        }

        ParsePosition      nodeStartPos  = node.getStartPosition();
        ParsePosition      nodeFinishPos = node.getFinishPosition();
        ParsePosition      commentPos;
        CommentAssociation assoc;

        for (int idx = 0; idx < _comments.getCount(); idx++)
        {
            commentPos = (ParsePosition)_comments.getKey(idx);
            assoc      = (CommentAssociation)_comments.get(idx);
            if (commentPos.isLowerOrEqual(nodeStartPos))
            {
                // Skip preceding comments now
                continue;
            }
            if (nodeFinishPos.isLowerOrEqual(commentPos))
            {
                // Check for following comments which are on the same line
                if (nodeFinishPos.getLine() == commentPos.getLine())
                {
                    if ((assoc.getOwner() == null) ||
                        commentPos.isLowerOrEqual(assoc.getOwner().getStartPosition()) ||
                        assoc.getOwner().getFinishPosition().isLowerOrEqual(nodeFinishPos))
                    {
                        assoc.setOwner(node);
                        continue;
                    }
                }
                break;
            }
            // We gather only leftover comments
            if (assoc.getOwner() == null)
            {
                assoc.setOwner(node);
            }
            else
            {
                // checking for nodes that enclose the comment but not the
                // current owner
                if (commentPos.isLowerOrEqual(nodeFinishPos) &&
                    assoc.getOwner().getFinishPosition().isLowerOrEqual(nodeStartPos))
                {
                    assoc.setOwner(node);
                }
            }
        }
    }

    public void preVisit(Object obj)
    {
        if (!(obj instanceof Node))
        {
            return;
        }

        Node node = (Node)obj;

        if (!isCommentable(node))
        {
            return;
        }

        ParsePosition nodePos   = node.getStartPosition();
        int           lastIdx   = _comments.getCount() - 1;
        int           startIdx  = -1;
        int           finishIdx = -1;
        Comment       curComment, nextComment;
        int           idx;

        for (idx = 0; idx <= lastIdx; idx++)
        {
            curComment = ((CommentAssociation)_comments.get(idx)).getComment();
            if (nodePos.isLowerOrEqual(curComment.getStartPosition()))
            {
                break;
            }
            nextComment = (idx < lastIdx ? ((CommentAssociation)_comments.get(idx+1)).getComment()
                                         : null);
            if (((idx < lastIdx) &&
                 (nextComment.getStartPosition().getLine() -
                  curComment.getFinishPosition().getLine() <= 2)) ||
                (nodePos.getLine() - curComment.getFinishPosition().getLine() <= 2))
            {
                if (startIdx == -1)
                {
                    startIdx = idx;
                }
                finishIdx = idx;

            }
            else
            {
                startIdx = -1;
            }
        }
        if (startIdx >= 0)
        {
            CommentAssociation assoc;

            for (idx = finishIdx; idx >= startIdx; idx--)
            {
                assoc = (CommentAssociation)_comments.get(idx);
                if ((assoc.getOwner() == null) ||
                    !assoc.getOwner().getStartPosition().isLowerOrEqual(nodePos))
                {
                    assoc.setOwner(node);
                }
                else
                {
                    // the comment already has an owner which is nearer
                    // therefore any comments before this comment
                    // cannot be associated to the node
                    break;
                }
            }
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
        visit(node.getBaseExpression());
        visit(node.getIndexExpression());
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        for (ExpressionIterator it = node.getDimExpressions().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }

        if (node.isInitialized())
        {
            visit(node.getInitializer());
        }
    }

    public void visitArrayInitializer(ArrayInitializer node)
    {
        for (VariableInitializerIterator it = node.getInitializers().getIterator(); it.hasNext();)
        {
            visit(it.getNext());
        }
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        visit(node.getLeftHandSide());
        visit(node.getValueExpression());
    }

    public void visitBinaryExpression(BinaryExpression node)
    {
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
        visit(node.getFormalParameter());
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

        for (ConstructorIterator iter = node.getConstructors().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        for (ImportIterator iter = node.getImportDeclarations().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }

        for (TypeDeclarationIterator iter = node.getTypeDeclarations().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitConditionalExpression(ConditionalExpression node)
    {
        visit(node.getCondition());
        visit(node.getTrueExpression());
        visit(node.getFalseExpression());
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        if (node.hasParameters())
        {
            visit(node.getParameterList());
        }

        visit(node.getBody());
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
        visit(node.getLoopStatement());
        visit(node.getCondition());
    }

    public void visitExpressionStatement(ExpressionStatement node)
    {
        visit(node.getExpression());
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        if (node.getInitializer() != null)
        {
            visit(node.getInitializer());
        }
    }

    public void visitFormalParameter(FormalParameter node)
    {
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
            for (LocalVariableIterator it = node.getInitDeclarations().getIterator(); it.hasNext();)
            {
                visit(it.getNext());
            }
        }
        else if (node.getInitList() != null)
        {
            visit(node.getInitList());
        }
        if (node.getCondition() != null)
        {
            visit(node.getCondition());
        }
        if (node.getUpdateList() != null)
        {
            visit(node.getUpdateList());
        }

        visit(node.getLoopStatement());
    }

    public void visitIfThenElseStatement(IfThenElseStatement node)
    {
        visit(node.getCondition());
        visit(node.getTrueStatement());

        if (node.hasElse())
        {
            visit(node.getFalseStatement());
        }
    }

    public void visitInitializer(Initializer node)
    {
        visit(node.getBody());
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        visit(node.getInnerExpression());
    }

    public void visitInstantiation(Instantiation node)
    {
        if (node.isTrailing())
        {
            visit(node.getBaseExpression());
        }
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

        for (MethodIterator iter = node.getMethods().getIterator(); iter.hasNext();)
        {
            visit(iter.getNext());
        }
    }

    public void visitLabeledStatement(LabeledStatement node)
    {
        visit(node.getStatement());
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        if (node.getInitializer() != null)
        {
            visit(node.getInitializer());
        }
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        if (node.hasParameters())
        {
            visit(node.getParameterList());
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
        visit(node.getInnerExpression());
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        visit(node.getInnerExpression());
    }

    public void visitReturnStatement(ReturnStatement node)
    {
        if (node.getReturnValue() != null)
        {
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
        visit(node.getInitEpression());
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
        visit(node.getLockExpression());
        visit(node.getBlock());
    }

    public void visitThrowStatement(ThrowStatement node)
    {
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
            visit(node.getCastType());
        }
        visit(node.getInnerExpression());
    }

    public void visitWhileStatement(WhileStatement node)
    {
        visit(node.getCondition());
        visit(node.getLoopStatement());
    }
}
