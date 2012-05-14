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
package jast;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.ParsePosition;

// Class used for error/warning messages from the parser/resolver
// It extends Exception such that it also can be 'thrown'
public class Message extends Exception
{
    private Node _origin;

    public Message(String msg)
    {
        super(msg);
    }

    public Message(String msg, Node origin)
    {
        super(msg);
        _origin = origin;
    }

    // Convenience method to retrieve the compilation unit of the problem
    public String getCompilationUnit()
    {
        return ((_origin == null) || !(_origin instanceof ContainedNode)) ?
                    null :
                    ASTHelper.getCompilationUnitOf((ContainedNode)_origin).getName();
    }

    public Node getOrigin()
    {
        return _origin;
    }

    // Convenience method to retrieve the position of the problem
    public ParsePosition getPosition()
    {
        return (_origin == null ? null : _origin.getStartPosition());
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();

        result.append("[");
        if (getCompilationUnit() != null)
        {
            result.append(getCompilationUnit());
            if (getPosition() != null)
            {
                result.append(", ");
                result.append(getPosition());
            }
        }
        else
        {
            result.append("<unknown location>");
        }
        result.append("] ");
        result.append(getMessage());

        return result.toString();
    }
}
