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
import jast.analysis.TypeUsageIterator;
import jast.analysis.TypeUsages;
import jast.analysis.Usages;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.TypeDeclaration;



public class IsInappropriatelyIntimate implements AnalysisFunction
{
    private final static int MAX_ALLOWED_FEATURE_INTERCHANGE_COUNT = 20;

    public IsInappropriatelyIntimate()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        Usages usages = (Usages) node.getProperty(Usages.PROPERTY_LABEL);

        if (!isApplicable(node) || (usages == null))
            {
            return false;
        }

        TypeDeclaration localType = (TypeDeclaration) node;

        for (TypeUsageIterator iter = usages.getUsedTypes().getIterator();
            iter.hasNext();
            )
            {
            TypeDeclaration curType = iter.getNext().getType();
            if ((getUsageCountOf(localType, curType)
                > MAX_ALLOWED_FEATURE_INTERCHANGE_COUNT)
                && (getUsageCountOf(curType, localType) > MAX_ALLOWED_FEATURE_INTERCHANGE_COUNT))
                {
                return true;
            }
        }

        return false;
    }

    private int getUsageCountOf(TypeDeclaration typeA, TypeDeclaration typeB)
    {
        TypeUsages usages = (TypeUsages) typeA.getProperty(Usages.PROPERTY_LABEL);

        // =?= usage count or better number of used features?
        return usages.isUsed(typeB) ? usages.getTypeUsage(typeB).getUsageCount() : 0;
    }

    private boolean isApplicable(Node node)
    {
        return (node instanceof ClassDeclaration);
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { ClassDeclaration.class };
    }
}
