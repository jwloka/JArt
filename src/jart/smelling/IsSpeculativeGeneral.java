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
import jast.analysis.InheritanceAnalyzer;
import jast.analysis.UsageAnalyzer;
import jast.analysis.Usages;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;



public class IsSpeculativeGeneral implements AnalysisFunction
{

    public IsSpeculativeGeneral()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((project == null) || (node == null) || !isApplicable(node))
            {
            return false;
        }

        if (node instanceof TypeDeclaration)
            {
            return checkType(project, (TypeDeclaration) node);
        }
        if (node instanceof InvocableDeclaration)
            {
            return checkInvocable(project, (InvocableDeclaration) node)
                || checkParameter(project, (InvocableDeclaration) node);
        }
        if (node instanceof FieldDeclaration)
            {
            return checkField(project, (FieldDeclaration) node);
        }

        return false;
    }

    private boolean checkField(Project prj, FieldDeclaration field)
    {
        if (field.getModifiers().isPrivate())
            {
            Usages usages =
                (Usages) ASTHelper.getTypeDeclarationOf(field).getProperty(
                    Usages.PROPERTY_LABEL);
            return (usages == null) ? true : usages.isUsed(field);
        }

        return !(new UsageAnalyzer(prj).isUsed(field));
    }

    private boolean checkInvocable(Project prj, InvocableDeclaration invocable)
    {
        return (invocable instanceof MethodDeclaration)
            ? !(new UsageAnalyzer(prj).isUsed((MethodDeclaration) invocable))
            : !(new UsageAnalyzer(prj).isUsed((ConstructorDeclaration) invocable));
    }

    private boolean checkParameter(Project project, InvocableDeclaration invocable)
    {
        return false;
    }

    private boolean checkType(Project prj, TypeDeclaration type)
    {
        if ((type instanceof ClassDeclaration)
            && (!type.getModifiers().isAbstract() || !(type instanceof InterfaceDeclaration)))
            {
            return false;
        }

        return !(new InheritanceAnalyzer(type).hasChildrenIn(prj))
            || !(new UsageAnalyzer(prj).isUsed(type));
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
        return new Class[] {
            TypeDeclaration.class,
            MethodDeclaration.class,
            FieldDeclaration.class,
            FormalParameter.class };
    }
}
