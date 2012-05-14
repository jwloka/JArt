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
package jart.restructuring.methodlevel;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.visitor.DescendingVisitor;

import java.util.Vector;

public class ConstructorAccessGatherer extends DescendingVisitor
{
    private ConstructorDeclaration _constructor = null;
    private Vector                 _accesses    = new Vector();

    public Vector getAccesses(TypeDeclaration type, ConstructorDeclaration constructor)
    {
        _accesses.clear();
        _constructor = constructor;

        type.accept(this);

        return _accesses;
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        if (node.getConstructorDeclaration() == _constructor)
        {
            _accesses.addElement(node);
        }
    }


    public void visitInstantiation(Instantiation node)
    {
        if (node.getInvokedConstructor() == _constructor)
        {
            _accesses.addElement(node);
        }
    }
}
