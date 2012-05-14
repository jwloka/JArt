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
package jart.analysis;

import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.Expression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.UnaryExpression;
import jast.ast.visitor.DescendingVisitor;

import java.util.Hashtable;

public class HasSideEffects extends    DescendingVisitor
                            implements AnalysisFunction
{
    private Hashtable _checkedInvocables = new Hashtable();
    private Node      _sideEffectNode    = null;

    public HasSideEffects()
    {}

    public boolean check(Project project, Node node)
    {
        _sideEffectNode = null;
        _checkedInvocables.clear();
        try
        {
            if (node != null)
            {
                node.accept(this);
            }
            return false;
        }
        catch (RuntimeException ex)
        {
            return true;
        }
    }

    public Node getNodeWithSideEffect()
    {
        return _sideEffectNode;
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { Expression.class, InvocableDeclaration.class };
    }

    public void visitAssignmentExpression(AssignmentExpression node)
    {
        if (node.getLeftHandSide() instanceof FieldAccess)
        {
            // this may be overly restrictive, especially when
            // the field is defined in a local class within a method
            // that is been checked
            _sideEffectNode = node;
            throw new RuntimeException();
        }
        super.visitAssignmentExpression(node);
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        ConstructorDeclaration constructor = node.getConstructorDeclaration();

        if ((constructor != null) &&
            !_checkedInvocables.containsKey(constructor))
        {
            // checking the declaration of the constructor
            _checkedInvocables.put(constructor, constructor);
            constructor.accept(this);
        }

        super.visitConstructorInvocation(node);
    }

    public void visitInstantiation(Instantiation node)
    {
        ConstructorDeclaration constructor = node.getInvokedConstructor();

        if ((constructor != null) &&
            !_checkedInvocables.containsKey(constructor))
        {
            // checking the declaration of the constructor
            _checkedInvocables.put(constructor, constructor);
            constructor.accept(this);
        }
        super.visitInstantiation(node);
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        MethodDeclaration method = node.getMethodDeclaration();

        if ((method != null) &&
            !_checkedInvocables.containsKey(method))
        {
            // checking the declaration of the method
            _checkedInvocables.put(method, method);
            method.accept(this);
        }
        super.visitMethodInvocation(node);
    }

    public void visitPostfixExpression(PostfixExpression node)
    {
        if (node.getInnerExpression() instanceof FieldAccess)
        {
            // this may be overly restrictive, especially when
            // the field is defined in a local class within a method
            // that is been checked
            _sideEffectNode = node;
            throw new RuntimeException();
        }
        super.visitPostfixExpression(node);
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        if ((node.getOperator() == UnaryExpression.DECREMENT_OP) ||
            (node.getOperator() == UnaryExpression.INCREMENT_OP))
        {
            if (node.getInnerExpression() instanceof FieldAccess)
            {
                // this may be overly restrictive, especially when
                // the field is defined in a local class within a method
                // that is been checked
                _sideEffectNode = node;
                throw new RuntimeException();
            }
        }
        super.visitUnaryExpression(node);
    }
}
