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
package jart.netbeans;

import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;

import org.openide.src.ClassElement;
import org.openide.src.FieldElement;
import org.openide.src.MethodElement;

public class OpenAPIJastMapper
{

    private OpenAPIJastMapper()
    {
    }

    public static TypeDeclaration getTypeDeclarationFor(ClassElement elem)
    {
        String typeName = elem.getName().getFullName();

        return Integration.getInstance().getProject().getJastProject().getType(
            typeName);
    }

    public static FieldDeclaration getFieldDeclarationFor(FieldElement elem)
    {
        TypeDeclaration typeDecl = getTypeDeclarationFor(elem.getDeclaringClass());

        if (typeDecl == null)
            {
            return null;
        }
        else
            {
            return typeDecl.getFields().get(elem.getName().getName());
        }
    }

    public static MethodDeclaration getMethodDeclarationFor(MethodElement elem)
    {
        ClassElement classElem = elem.getDeclaringClass();
        TypeDeclaration typeDecl = getTypeDeclarationFor(classElem);

        if (typeDecl == null)
            {
            return null;
        }
        else
            {
            // We now have the problem that in contrast to fields the name
            // does not suffice; we therefore have to trick a bit using the
            // property of the parser to parse methods in order
            // This means we only have to find the position of the method
            // in the method list of its class element and return the
            // corresponding method of the type decl
            for (int idx = 0; idx < classElem.getMethods().length; idx++)
                {
                if (classElem.getMethods()[idx] == elem)
                    {
                    return typeDecl.getMethods().get(idx);
                }
            }

            return null;
        }
    }
}

