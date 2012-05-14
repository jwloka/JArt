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

import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.visitor.DescendingVisitor;
import jast.helpers.StringArray;


public class UsedParameters extends DescendingVisitor
{
    private StringArray _usedParameters;
    private StringArray _parameters;

    public UsedParameters()
    {
    }

    public UsedParameters(InvocableDeclaration meth)
    {
        if (meth instanceof ConstructorDeclaration)
            {
            visitConstructorDeclaration((ConstructorDeclaration) meth);
        }
        else
            {
            visitMethodDeclaration((MethodDeclaration) meth);
        }
    }

    private void addParameters(InvocableDeclaration node)
    {
        for (FormalParameterIterator iter =
            node.getParameterList().getParameters().getIterator();
            iter.hasNext();
            )
            {
            String name = iter.getNext().getName();

            if (!_parameters.contains(name))
                {
                _parameters.add(name);
            }
        }

    }

    private void addUsedParameter(String name)
    {
        if (!_usedParameters.contains(name))
            {
            _usedParameters.add(name);
        }
    }

    public StringArray getNames()
    {
        return _usedParameters;
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        _parameters = new StringArray();
        _usedParameters = new StringArray();

        if ((node == null)
            || (node.getBody() == null)
            || (node.getParameterList() == null)
            || (node.getParameterList().getParameters() == null)
            || (node.getParameterList().getParameters().isEmpty()))
            {
            return;
        }

        addParameters(node);

        super.visitConstructorDeclaration(node);
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        _parameters = new StringArray();
        _usedParameters = new StringArray();

        if ((node == null)
            || (node.getBody() == null)
            || (node.getParameterList() == null)
            || (node.getParameterList().getParameters() == null)
            || (node.getParameterList().getParameters().isEmpty()))
            {
            return;
        }

        addParameters(node);

        super.visitMethodDeclaration(node);
    }

    public void visitVariableAccess(VariableAccess node)
    {
        if (_parameters.contains(node.getVariableName()))
            {
            addUsedParameter(node.getVariableName());
        }
    }
}
