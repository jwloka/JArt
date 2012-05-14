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
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;

public class RenameLocalVariable extends Restructuring
{
    /** The declaration to modify */
    private VariableDeclaration _decl    = null;
    /** The new name */
    private String              _newName = null;

    public boolean analyze()
    {
        return new NameChecker(_results, _decl).check(_newName);
    }

    /**
     * Initializes the restructuring. It accepts a variable declaration
     * (<code>LocalVariableDeclaration</code> or <code>FormalParameter</code>)
     * or a variable access (<code>VariableAccess</code>) together
     * with the new name as input.
     *
     * @param  nodes          One node which either declares or references
     *                        the variable in question
     * @param  processingData Onw string which gives the new name
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
        if (nodes.length > 1)
        {
            _results.addFatalError("Only one input node is allowed",
                                    null);
            return false;
        }
        if ((processingData.length > 1) ||
            !(processingData[0] instanceof String))
        {
            _results.addFatalError("Exactly one string is required as processing data",
                                    null);
            return false;
        }
        if ((nodes[0] instanceof LocalVariableDeclaration) ||
            (nodes[0] instanceof FormalParameter))
        {
            _decl = (VariableDeclaration)nodes[0];
        }
        else if (nodes[0] instanceof VariableAccess)
        {
            _decl = ((VariableAccess)nodes[0]).getVariableDeclaration();
        }
        if (_decl != null)
        {
            _newName = (String)processingData[0];
            return true;
        }
        else
        {
            _results.addFatalError("Input node does not reference a variable",
                                    nodes[0]);
            return false;
        }
    }

    public boolean perform()
    {
        _decl.setName(_newName);
        _results.markUnitAsChanged(ASTHelper.getCompilationUnitOf(_decl));
        return true;
    }
}
