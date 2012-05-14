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
package jast.ast.visitor;

import jast.ast.Node;

import java.util.Enumeration;
import java.util.Hashtable;

public class DebugVisitor extends DescendingReflectionVisitor
{
    private Hashtable    _nodeTypes = new Hashtable();
    private StringBuffer _indent    = new StringBuffer();

    public void postVisit(Object obj)
    {
        _indent.setLength(_indent.length() - 2);
    }

    public void preVisit(Object obj)
    {
        Node   node = (Node)obj;
        String type = node.getClass().getName();
        int    num  = 1;

        if (type.indexOf('.') >= 0)
        {
            type = type.substring(type.lastIndexOf('.')+1);
        }

        if (_nodeTypes.containsKey(type))
        {
            num = ((Integer)_nodeTypes.get(type)).intValue() + 1;
        }
        _nodeTypes.put(type, new Integer(num));

        System.out.println(_indent.toString() + type + " [" +
                           node.getStartPosition() + "-" + node.getFinishPosition() + "]");
        _indent.append("  ");
    }

    public void print(Node node)
    {
        _nodeTypes.clear();
        _indent.setLength(0);

        visit(node);

        String  type;
        Integer num;

        System.out.println();
        for (Enumeration en = _nodeTypes.keys(); en.hasMoreElements();)
        {
            type = (String)en.nextElement();
            num  = (Integer)_nodeTypes.get(type);
            System.out.print(num+" "+type);
            if (num.intValue() > 1)
            {
                if (type.endsWith("s"))
                {
                    System.out.print("e");
                }
                System.out.print("s");
            }
            System.out.println();
        }
    }
}
