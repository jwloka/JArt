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

import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BlockStatement;
import jast.ast.nodes.Expression;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.FormalParameterIterator;

public class MethodAnalyzer
{
    private InvocableDeclaration _method;

    public MethodAnalyzer()
    {
        super();
    }

    public MethodAnalyzer(InvocableDeclaration method)
    {
        _method = method;
    }

    public static FieldDeclaration getFieldOfAccessor(MethodDeclaration accessor)
    {
        FieldDeclaration result = getFieldOfGetterMethod(accessor);

        if (result == null)
            {
            result = getFieldOfSetterMethod(accessor);
        }

        return result;
    }

    private static FieldDeclaration getFieldOfGetterMethod(MethodDeclaration getterMethod)
    {
        if ((getterMethod.getBody() == null)
            || (getterMethod.getBody().getBlockStatements().isEmpty()))
            {
            return null;
        }

        BlockStatement stmt = getterMethod.getBody().getBlockStatements().get(0);
        if (!(stmt instanceof ReturnStatement))
            {
            return null;
        }

        Expression returnValue = ((ReturnStatement) stmt).getReturnValue();

        while (returnValue instanceof ArrayAccess)
            {
            returnValue = ((ArrayAccess) returnValue).getBaseExpression();
        }

        if (!(returnValue instanceof FieldAccess))
            {
            return null;
        }

        return ((FieldAccess) returnValue).getFieldDeclaration();

    }

    private static FieldDeclaration getFieldOfSetterMethod(MethodDeclaration setterMethod)
    {
        for (BlockStatementIterator iter =
            setterMethod.getBody().getBlockStatements().getIterator();
            iter.hasNext();
            )
            {
            BlockStatement stmt = iter.getNext();
            if (!(stmt instanceof ExpressionStatement))
                {
                return null;
            }

            Expression expr = ((ExpressionStatement) stmt).getExpression();

            if (!(expr instanceof AssignmentExpression)
                || (((AssignmentExpression) expr).getOperator()
                    != AssignmentExpression.ASSIGN_OP))
                {
                return null;
            }

            expr = ((AssignmentExpression) expr).getLeftHandSide();

            while (expr instanceof ArrayAccess)
                {
                expr = ((ArrayAccess) expr).getBaseExpression();
            }

            if (!(expr instanceof FieldAccess))
                {
                return null;
            }

            return ((FieldAccess) expr).getFieldDeclaration();
        }

        return null;
    }

    public static boolean isAccessor(MethodDeclaration method)
    {
        return isGetMethod(method) || isSetMethod(method);
    }

    private static boolean isContainer(TypeDeclaration container, Node node)
    {
        if (!(node instanceof ContainedNode))
            {
            return false;
        }

        ContainedNode contained = (ContainedNode) node;
        Node containerNode = contained.getContainer();

        while (containerNode != null)
            {
            if (containerNode.equals(container))
                {
                return true;
            }

            if (containerNode instanceof ContainedNode)
                {
                containerNode = ((ContainedNode) containerNode).getContainer();
            }
            else
                {
                return false;
            }
        }

        return false;
    }

    protected boolean isFormalParameter(String name)
    {
        if ((_method == null)
            || (name == null)
            || (name.length() == 0)
            || (_method.getParameterList() == null)
            || (_method.getParameterList().getParameters() == null)
            || (_method.getParameterList().getParameters().isEmpty()))
            {
            return false;
        }

        for (FormalParameterIterator iter =
            _method.getParameterList().getParameters().getIterator();
            iter.hasNext();
            )
            {
            if (iter.getNext().getName().equals(name))
                {
                return true;
            }
        }

        return false;
    }

    protected boolean isFormalParameterUsed(String name)
    {
        if ((_method == null)
            || (_method.getBody() == null)
            || !isFormalParameter(name))
            {
            return false;
        }

        return new UsedParameters(_method).getNames().contains(name);
    }

    public static boolean isGetMethod(MethodDeclaration method)
    {
        if (method == null)
            {
            return false;
        }

        //if (!(_method instanceof MethodDeclaration))
        //{
        //return false;
        //}

        //MethodDeclaration method = (MethodDeclaration)_method;

        if (!method.getModifiers().isPublic()
            || method.getModifiers().isAbstract()
            || (method.getBody() == null))
            {
            //field = false;
            return false;
        }

        if (method.getReturnType() == null)
            {
            return false;
        }

        if (method.getBody().getBlockStatements().getCount() != 1)
            {
            return false;
        }

        BlockStatement stmt = method.getBody().getBlockStatements().get(0);
        if (!(stmt instanceof ReturnStatement))
            {
            return false;
        }

        Expression returnValue = ((ReturnStatement) stmt).getReturnValue();
        while (returnValue instanceof ArrayAccess)
            {
            returnValue = ((ArrayAccess) returnValue).getBaseExpression();
        }

        if (!(returnValue instanceof FieldAccess))
            {
            return false;
        }

        if (((FieldAccess) returnValue).isTrailing())
            {
            returnValue = ((FieldAccess) returnValue).getBaseExpression();

            if (!(returnValue instanceof SelfAccess)
                || ((SelfAccess) returnValue).isSuper())
                {
                return false;
            }
        }

        //    is field declared in the same type as the method
        if (!isContainer((TypeDeclaration) method.getContainer(), returnValue))
            {
            return false;
        }

        return true;
    }

    public static boolean isSetMethod(MethodDeclaration method)
    {
        if (method == null)
            {
            return false;
        }

        //if (!(_method instanceof MethodDeclaration))
        //{
        //return false;
        //}

        //MethodDeclaration method = (MethodDeclaration)_method;

        if (!method.getModifiers().isPublic()
            || method.getModifiers().isAbstract()
            || (method.getBody() == null))
            {
            //field = false;
            return false;
        }

        if (method.getReturnType() != null)
            {
            return false;
        }

        if (method.getBody().getBlockStatements().getCount() == 0)
            {
            return false;
        }

        for (BlockStatementIterator iter =
            method.getBody().getBlockStatements().getIterator();
            iter.hasNext();
            )
            {
            BlockStatement stmt = iter.getNext();
            if (!(stmt instanceof ExpressionStatement))
                {
                return false;
            }

            Expression expr = ((ExpressionStatement) stmt).getExpression();

            if (!(expr instanceof AssignmentExpression)
                || (((AssignmentExpression) expr).getOperator()
                    != AssignmentExpression.ASSIGN_OP))
                {
                return false;
            }

            expr = ((AssignmentExpression) expr).getLeftHandSide();

            while (expr instanceof ArrayAccess)
                {
                expr = ((ArrayAccess) expr).getBaseExpression();
            }

            if (!(expr instanceof FieldAccess))
                {
                return false;
            }

            if (((FieldAccess) expr).isTrailing())
                {
                expr = ((FieldAccess) expr).getBaseExpression();

                if (!(expr instanceof SelfAccess) || ((SelfAccess) expr).isSuper())
                    {
                    return false;
                }
            }

            //    is field declared in the same type as the method
            if (!isContainer((TypeDeclaration) method.getContainer(), expr))
                {
                return false;
            }
        }

        return true;
    }

    public void setMethod(InvocableDeclaration method)
    {
        _method = method;
    }
}
