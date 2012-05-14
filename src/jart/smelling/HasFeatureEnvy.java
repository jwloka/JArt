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
import jart.analysis.IsUtilityClass;
import jart.analysis.IsUtilityMethod;
import jast.analysis.Usages;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.MethodDeclaration;



public class HasFeatureEnvy implements AnalysisFunction
{
    private final static float PERCENTAGE_OF_ALLOWED_USED_REMOTE_FEATURES = 0.6f;
    private final static int MIN_USED_REMOTE_FEATURE_COUNT                 = 5;

    public HasFeatureEnvy()
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

        return (node instanceof MethodDeclaration)
            ? checkMethod(project, (MethodDeclaration) node)
            : checkType(project, (ClassDeclaration) node);

    }

    private boolean checkMethod(Project prj, MethodDeclaration method)
    {
        if (new IsUtilityMethod().check(prj, method))
            {
            return false;
        }

        return (new HasAwfullyLongName().check(null, method))
            || (usesEnoughRemoteFeatures(method) && usesMainlyRemoteFeatures(method));
    }

    private boolean checkType(Project prj, ClassDeclaration clsDecl)
    {
        if (new IsUtilityClass().check(prj, clsDecl))
            {
            return false;
        }

        return usesEnoughRemoteFeatures(clsDecl) && usesMainlyRemoteFeatures(clsDecl);
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
        return new Class[] { MethodDeclaration.class, ClassDeclaration.class };
    }

    private boolean usesEnoughRemoteFeatures(Node node)
    {
        Usages usages = (Usages) node.getProperty(Usages.PROPERTY_LABEL);

        return (usages == null)
            ? false
            : usages.getUsedRemoteFeatureCount() >= MIN_USED_REMOTE_FEATURE_COUNT;
    }

    private boolean usesMainlyRemoteFeatures(Node node)
    {
        Usages usages = (Usages) node.getProperty(Usages.PROPERTY_LABEL);

        if (usages == null)
            {
            return false;
        }

        int usedNonRemoteFeatureCount =
            usages.getUsedLocalFeatureCount() + usages.getUsedInheritedFeatureCount();

        return usages.getUsedFeatureCountOfMaxUsedRemoteType()
            > (usedNonRemoteFeatureCount * PERCENTAGE_OF_ALLOWED_USED_REMOTE_FEATURES);
    }
}
