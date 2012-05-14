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
package jart.smelling;
import jart.analysis.AnalysisFunction;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.Comment;
import jast.ast.nodes.Feature;
import jast.ast.nodes.collections.CommentIterator;



public class IsThicklyCommented implements AnalysisFunction
{
    private final static int MAX_COMMENT_CHAR_COUNT = 100;

    public IsThicklyCommented()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((node == null) || !isApplicable(node))
            {
            return false;
        }

        long commentLength = 0;

        for (CommentIterator iter = node.getComments().getIterator(); iter.hasNext();)
            {
            Comment curComment = iter.getNext();
            if (!curComment.isJavaDocComment())
                {
                commentLength += curComment.getText().length();
            }
        }

        return commentLength > MAX_COMMENT_CHAR_COUNT;
    }

    private boolean isApplicable(Node node)
    {
        Class[] applicableClasses = isApplicableTo();

        for (int idx = 0; idx < applicableClasses.length; idx++)
            {
            if (applicableClasses[idx].isInstance(node))
                {
                return true;
            }
        }

        return false;
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { Feature.class };

    }
}
