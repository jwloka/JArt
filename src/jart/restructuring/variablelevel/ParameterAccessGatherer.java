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
import jast.ast.Node;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.VariableAccess;
import jast.ast.visitor.DescendingVisitor;

import java.util.Vector;

public class ParameterAccessGatherer extends DescendingVisitor
{
    private FormalParameter _param    = null;
    private Vector          _accesses = new Vector();

    public Vector getAccesses(FormalParameter param)
    {
        _accesses.clear();
        _param = param;

        Node container = param.getContainer();

        if (container instanceof FormalParameterList)
        {
            container = ((FormalParameterList)container).getContainer();
        }
        container.accept(this);

        return _accesses;
    }



    public void visitVariableAccess(VariableAccess node)
    {
        if (node.getVariableDeclaration() == _param)
        {
            _accesses.addElement(node);
        }
    }
}