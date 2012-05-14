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
import jast.ast.nodes.Block;
import jast.ast.nodes.Expression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.UnaryExpression;

public class IsGetterMethod implements AnalysisFunction
{
    private FieldDeclaration _target = null;

    public IsGetterMethod()
    {}

    public IsGetterMethod(FieldDeclaration target)
    {
        _target = target;
    }

    public boolean check(Project project, Node node)
    {
        MethodDeclaration methodDecl = (MethodDeclaration)node;

        if (!methodDecl.hasReturnType() ||
            methodDecl.hasParameters() ||
            !methodDecl.hasBody())
        {
            return false;
        }

        Block body = methodDecl.getBody();

        if (body.getBlockStatements().getCount() != 1)
        {
            return false;
        }
        if (!(body.getBlockStatements().get(0) instanceof ReturnStatement))
        {
            return false;
        }

        ReturnStatement returnStmt = (ReturnStatement)body.getBlockStatements().get(0);

        if (!returnStmt.hasReturnValue())
        {
            return false;
        }

        Expression expr = returnStmt.getReturnValue();

        while (expr != null)
        {
            if ((expr instanceof UnaryExpression) &&
                ((UnaryExpression)expr).getOperator() == UnaryExpression.CAST_OP)
            {
                expr = ((UnaryExpression)expr).getInnerExpression();
            }
            else if (expr instanceof ParenthesizedExpression)
            {
                expr = ((ParenthesizedExpression)expr).getInnerExpression();
            }
            else
            {
                if (expr instanceof FieldAccess)
                {
                    return (_target == null) ||
                           ((FieldAccess)expr).getFieldDeclaration() == _target;
                }
                else
                {
                    return false;
                }
            }
        }
        return false;
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { MethodDeclaration.class };
    }
}
