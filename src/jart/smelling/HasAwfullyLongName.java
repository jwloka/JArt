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
import jast.ast.nodes.MethodDeclaration;



public class HasAwfullyLongName implements AnalysisFunction
{
    private final static int       MAX_NAME_LENGTH       = 15;
    private final static String[] AWFUL_NAME_INDICATORS =
    {
        "from",
        "to",
        "with"
    };

    public HasAwfullyLongName()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((node == null) || !isApplicable(node))
            {
            return false;
        }

        return hasLongName((MethodDeclaration) node)
            && hasAwfulName((MethodDeclaration) node);
    }

    private boolean hasAwfulName(MethodDeclaration method)
    {
        String methodName = method.getName().toLowerCase();
        for (int idx = 0; idx < AWFUL_NAME_INDICATORS.length; idx++)
            {
            if (methodName.startsWith(AWFUL_NAME_INDICATORS[idx])
                || methodName.endsWith(AWFUL_NAME_INDICATORS[idx]))
                {
                return false;
            }
            if (methodName.indexOf(AWFUL_NAME_INDICATORS[idx]) > 0)
                {
                return true;
            }
        }
        return false;
    }

    private boolean hasLongName(MethodDeclaration method)
    {
        return method.getName().length() >= MAX_NAME_LENGTH;
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
        return new Class[] { MethodDeclaration.class };
    }
}
