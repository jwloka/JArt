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
package jart.restructuring.variablelevel;
import jart.restructuring.Restructuring;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.ExpressionReplacementVisitor;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.collections.FormalParameterIterator;

import java.util.Enumeration;
import java.util.Vector;

public class RemoveAssignmentsToParameters extends Restructuring
{
    private class ParameterHandler
    {
        /** The parameter in question */
        private FormalParameter     _param      = null;
        /** All accesses to the parameter */
        private Vector              _accesses   = null;
        /** Helper restructuring to actually create the variable */
        private CreateLocalVariable _varCreator = null;

        public ParameterHandler(FormalParameter param)
        {
            _param = param;
        }

        public boolean analyzeParameterAssignments()
        {
            VariableAccess access;
            Node           container;
            int            operator;
            boolean        needVar = false;

            _accesses = new ParameterAccessGatherer().getAccesses(_param);
            for (Enumeration en = _accesses.elements(); en.hasMoreElements(); )
            {
                access    = (VariableAccess)en.nextElement();
                container = access.getContainer();

                if (container instanceof AssignmentExpression)
                {
                    if (((AssignmentExpression)container).getLeftHandSide() == access)
                    {
                        needVar = true;
                        break;
                    }
                }
                else if (container instanceof PostfixExpression)
                {
                    needVar = true;
                    break;
                }
                else if (container instanceof UnaryExpression)
                {
                    operator = ((UnaryExpression)container).getOperator();
                    if ((operator == UnaryExpression.INCREMENT_OP) ||
                        (operator == UnaryExpression.DECREMENT_OP))
                    {
                        needVar = true;
                        break;
                    }
                }
            }
            if (needVar)
            {
                _varCreator = new CreateLocalVariable();
                _varCreator.setResultContainer(getResultContainer());

                Node[]   helperNodes = new Node[1];
                Object[] helperArgs  = new Object[3];

                container = _param.getContainer();

                // determining block to add the variable to
                if (container instanceof FormalParameterList)
                {
                    // parameter of constructor or method
                    container      = ((FormalParameterList)container).getContainer();
                    helperNodes[0] = ((InvocableDeclaration)container).getBody();
                }
                else
                {
                    // parameter of catch
                    helperNodes[0] = ((CatchClause)container).getCatchBlock();
                }
                helperArgs[0] = _param.getType().getClone();
                helperArgs[1] = _prefix +
                                Character.toUpperCase(_param.getName().charAt(0)) +
                                _param.getName().substring(1);
                helperArgs[2] = new Integer(0);

                if (!_varCreator.initialize(helperNodes, helperArgs))
                {
                    return false;
                }
                if (!_varCreator.analyze())
                {
                    return false;
                }
            }
            return true;
        }

        public boolean requiresNewVariable()
        {
            return _varCreator != null;
        }

        public boolean insertNewVariable()
        {
            return _varCreator.perform();
        }
        public void removeNewVariable()
        {
            Block block = (Block)_varCreator.getCreatedVariable().getContainer();

            block.getBlockStatements().remove(0);
        }
        public void replaceAssignments()
        {
            ExpressionReplacementVisitor replacer = new ExpressionReplacementVisitor();
            LocalVariableDeclaration     newVar   = _varCreator.getCreatedVariable();
            NodeFactory                  factory  = Global.getFactory();

            ((SingleInitializer)newVar.getInitializer()).setInitExpression(
                factory.createVariableAccess(_param));
            for (Enumeration en = _accesses.elements(); en.hasMoreElements();)
            {
                replacer.replace((VariableAccess)en.nextElement(),
                                 factory.createVariableAccess(newVar));
            }

            getResultContainer().getChangedUnits().add(
                ASTHelper.getCompilationUnitOf(_param));
        }

    }

    /** The handler for the parameter(s) in question */
    private Vector _handler = new Vector();
    /** The prefix to use for new variable */
    private String _prefix  = null;

    public boolean analyze()
    {
        for (Enumeration en = _handler.elements(); en.hasMoreElements();)
        {
            if (!((ParameterHandler)en.nextElement()).analyzeParameterAssignments())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Initializes the restructuring. The input node can be
     * - a parameter
     * - a parameter list
     * The processing data consists of the prefix to use for a new variable.
     *
     * @param  nodes          The parameter(s)
     * @param  processingData The prefix to use for the name of a new variable(s)
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        if ((nodes == null) ||
            (nodes.length == 0) ||
            (processingData == null) ||
            (processingData.length == 0))
        {
            _results.addFatalError("No valid input data given",
                                    null);
            return false;
        }
        if (processingData.length > 1)
        {
            _results.addFatalError("Only one processing data item is required",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof String))
        {
            _results.addFatalError("The processing data item must be a string",
                                    null);
            return false;
        }
        _prefix  = (String)processingData[0];

        if (nodes.length > 1)
        {
            _results.addFatalError("Only one input node is allowed",
                                    null);
            return false;
        }
        if (nodes[0] instanceof FormalParameter)
        {
            _handler.addElement(new ParameterHandler((FormalParameter)nodes[0]));
        }
        else if (nodes[0] instanceof FormalParameterList)
        {
            for (FormalParameterIterator it = ((FormalParameterList)nodes[0]).getParameters().getIterator(); it.hasNext();)
            {
                _handler.addElement(new ParameterHandler(it.getNext()));
            }
        }
        else
        {
            _results.addFatalError("The input node must be a parameter or parameter array",
                                    null);
            return false;
        }

        return true;
    }

    public boolean perform()
    {
        ParameterHandler handler;
        int              num = 0;

        for (int idx = _handler.size()-1; idx >= 0; idx--, num++)
        {
            handler = (ParameterHandler)_handler.elementAt(idx);
            if (handler.requiresNewVariable() && !handler.insertNewVariable())
            {
                // we have to remove all created variables
                for (; num > 0; num--)
                {
                    handler = (ParameterHandler)_handler.elementAt(num-1);
                    if (handler.requiresNewVariable())
                    {
                        handler.removeNewVariable();
                    }
                }
                return false;
            }
        }
        for (Enumeration en = _handler.elements(); en.hasMoreElements();)
        {
            handler = (ParameterHandler)en.nextElement();
            if (handler.requiresNewVariable())
            {
                handler.replaceAssignments();
            }
        }
        return true;
    }
}
