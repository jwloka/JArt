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
import jart.analysis.IsCollectionClass;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.MethodIterator;



public class HasSharedCollection implements AnalysisFunction
{

    public HasSharedCollection()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if (!isApplicable(node))
            {
            return false;
        }

        TypeDeclaration localType = (TypeDeclaration) node;

        return hasPublicMethodSignatureWithCollection(project, localType)
            || hasPublicFieldWithCollection(project, localType);
    }

    private boolean hasPublicFieldWithCollection(Project prj, TypeDeclaration type)
    {
        for (FieldIterator fieldIter = type.getFields().getIterator();
            fieldIter.hasNext();
            )
            {
            FieldDeclaration curField = fieldIter.getNext();

            if (new IsCollectionClass()
                .check(prj, curField.getType().getReferenceBaseTypeDecl())
                && (curField.getModifiers().isPublic()
                    || curField.getModifiers().isFriendly()
                    || curField.getModifiers().isProtected()))
                {
                return true;
            }
        }

        return false;
    }

    private boolean hasPublicMethodSignatureWithCollection(
        Project prj,
        TypeDeclaration type)
    {
        for (MethodIterator methIter = type.getMethods().getIterator();
            methIter.hasNext();
            )
            {
            MethodDeclaration curMethod = methIter.getNext();

            if (curMethod.hasReturnType()
                && new IsCollectionClass().check(
                    prj,
                    curMethod.getReturnType().getReferenceBaseTypeDecl())
                && curMethod.getModifiers().isPublic())
                {
                return true;
            }

            if (!curMethod.hasParameters())
                {
                continue;
            }

            for (FormalParameterIterator paramIter =
                curMethod.getParameterList().getParameters().getIterator();
                paramIter.hasNext();
                )
                {
                if (new IsCollectionClass()
                    .check(prj, paramIter.getNext().getType().getReferenceBaseTypeDecl())
                    && curMethod.getModifiers().isPublic())
                    {
                    return true;
                }
            }
        }

        return false;
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
        return new Class[] { TypeDeclaration.class };
    }
}
