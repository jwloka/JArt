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
import jart.restructuring.DefaultValueHelper;
import jart.restructuring.Restructuring;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.BlockStatement;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.Type;

public class CreateLocalVariable extends Restructuring
{
    /** The block which will contain the new variable */
    private Block   _block     = null;
    /** The target position of the new variable within the block */
    private int     _targetPos = 0;
    /** The new variable */
    private LocalVariableDeclaration _decl;

    public LocalVariableDeclaration getCreatedVariable()
    {
        return _decl;
    }

    public boolean analyze()
    {
        return isReachable() &&
               new NameChecker(_results, _block, _targetPos).check(_decl.getName());
    }

    /**
     * Initializes the restructuring. The input node is the block
     * within which the new variable will be declared. The processing data
     * consists of the type and the name as well as the position in the block
     * statement list of the block which will be the resulting position of
     * the new declaration
     *
     * @param  nodes          The block
     * @param  processingData The type, name and position of the variable
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
        if (!(nodes[0] instanceof Block))
        {
            _results.addFatalError("The node must be a block",
                                    null);
            return false;
        }
        if (processingData.length != 3)
        {
            _results.addFatalError("Three parameters are required: type, name, position",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Type))
        {
            _results.addFatalError("The first parameter must be the type of the new variable",
                                    null);
            return false;
        }
        if (!(processingData[1] instanceof String))
        {
            _results.addFatalError("The second parameter must be the name of the new variable",
                                    null);
            return false;
        }
        if (!(processingData[2] instanceof Integer))
        {
            _results.addFatalError("The third parameter must be the position of the new variable within the block",
                                    null);
            return false;
        }

        Type type = (Type)processingData[0];

        _block     = (Block)nodes[0];
        _targetPos = ((Integer)processingData[2]).intValue();
        _decl        = Global.getFactory().createLocalVariableDeclaration(
                         false,
                         type,
                         (String)processingData[1],
                         Global.getFactory().createSingleInitializer(
                             DefaultValueHelper.getDefaultValueFor(type)));
        return true;
    }

    private boolean isReachable()
    {
        int blockLen = _block.getBlockStatements().getCount();

        if ((_targetPos >= blockLen) && (blockLen > 0))
        {
            BlockStatement stmt = _block.getBlockStatements().get(blockLen-1);

            if ((stmt instanceof ReturnStatement) ||
                (stmt instanceof BreakStatement) ||
                (stmt instanceof ContinueStatement))
            {
                _results.addError("The declaration would be unreachable",
                                   (Node)stmt);
                return false;
            }
        }
        return true;
    }

    public boolean perform()
    {
        _block.getBlockStatements().insert(_decl, _targetPos);
        _results.markUnitAsChanged(ASTHelper.getCompilationUnitOf(_block));

        return true;
    }
}
