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
package jart.analysis;
import jast.analysis.FeatureUsages;
import jast.analysis.Usages;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.MethodDeclaration;



public class IsUtilityMethod implements AnalysisFunction
{
    private static final int MANY_PARAMETERS     = 4;
    private static final int FEW_REMOTE_CLASSES = 2;

    public IsUtilityMethod()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((node == null) || !isApplicable(node))
            {
            return false;
        }

        int indicatorCount = 0;
        MethodDeclaration method = (MethodDeclaration) node;

        if (!method.hasBody())
            {
            return false;
        }

        if (usesNoLocalFeature(method))
            {
            indicatorCount++;
        }
        if (usesManyParameters(method))
            {
            indicatorCount++;
        }
        if (usesOnlyFewRemoteClasses(method))
            {
            indicatorCount++;
        }
        if (method.getModifiers().isPublic())
            {
            indicatorCount++;
        }
        if (method.getModifiers().isStatic())
            {
            indicatorCount++;
        }
        if (method.getModifiers().isFinal())
            {
            indicatorCount++;
        }

        return indicatorCount >= 3;
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

    private boolean usesManyParameters(MethodDeclaration method)
    {
        return (method.getParameterList() == null)
            ? false
            : method.getParameterList().getParameters().getCount() >= MANY_PARAMETERS;
    }

    private boolean usesNoLocalFeature(MethodDeclaration method)
    {
        FeatureUsages usages =
            (FeatureUsages) method.getProperty(Usages.PROPERTY_LABEL);

        return (usages == null)
            || (usages.getUsedFeatureCount() == 0)
                ? false
                : usages.getUsedLocalFeatureCount() == 0;
    }

    private boolean usesOnlyFewRemoteClasses(MethodDeclaration method)
    {
        FeatureUsages usages =
            (FeatureUsages) method.getProperty(Usages.PROPERTY_LABEL);

        return (usages == null)
            || (usages.getUsedTypes().getCount() == 0)
                ? false
                : usages.getUsedRemoteTypeCount() <= FEW_REMOTE_CLASSES;
    }
}
