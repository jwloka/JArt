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
import jast.analysis.MethodAnalyzer;
import jast.analysis.UsageAnalyzer;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodArray;
import jast.ast.nodes.collections.MethodArrayImpl;
import jast.ast.nodes.collections.MethodIterator;



public class IsUnnecessaryOpen implements AnalysisFunction
{

    public IsUnnecessaryOpen()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((project == null) || (node == null) || !isApplicable(node))
            {
            return false;
        }

        ClassDeclaration clsDecl = (ClassDeclaration) node;

        return !getUnnecessaryVisibleFields(project, clsDecl).isEmpty()
            || !getUnnecessaryVisibleAccessors(project, clsDecl).isEmpty();
    }

    public MethodArray getUnnecessaryVisibleAccessors(
        Project prj,
        ClassDeclaration clsDecl)
    {
        MethodArray result = new MethodArrayImpl();
        UsageAnalyzer usages = new UsageAnalyzer(prj);

        for (MethodIterator iter = clsDecl.getMethods().getIterator(); iter.hasNext();)
            {
            MethodDeclaration curMeth = iter.getNext();
            if (MethodAnalyzer.isAccessor(curMeth) && usages.isNotRemoteUsed(curMeth))
                {
                result.add(curMeth);
            }
        }

        return result;
    }

    public FieldArray getUnnecessaryVisibleFields(
        Project prj,
        ClassDeclaration clsDecl)
    {
        FieldArray result = new FieldArrayImpl();
        UsageAnalyzer usages = new UsageAnalyzer(prj);

        for (FieldIterator iter = clsDecl.getFields().getIterator(); iter.hasNext();)
            {
            FieldDeclaration curField = iter.getNext();
            if (curField.getModifiers().isPublic() || curField.getModifiers().isFriendly())
                {
                result.add(curField);
            }
        }

        return result;
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
        return new Class[] { ClassDeclaration.class };
    }
}
