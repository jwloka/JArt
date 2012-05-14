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
package jart.restructuring.tidyimports;
import jast.analysis.TypeUsageIterator;
import jast.analysis.Usages;
import jast.ast.Node;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Type;
import jast.ast.visitor.DescendingVisitor;

public class TypeGatherer extends DescendingVisitor
{
    private UsedPackagesAndTypes _types;

    public TypeGatherer(UsedPackagesAndTypes results)
    {
        _types = results;
    }

    private void evaluateUsages(Node node)
    {
        if (node.hasProperty(Usages.PROPERTY_LABEL))
        {
            Usages usages = (Usages)node.getProperty(Usages.PROPERTY_LABEL);

            for (TypeUsageIterator it = usages.getUsedTypes().getIterator(); it.hasNext();)
            {
                _types.addType(it.getNext().getType());
            }
        }
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        evaluateUsages(node);
        super.visitClassDeclaration(node);
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        evaluateUsages(node);
        super.visitConstructorDeclaration(node);
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        evaluateUsages(node);
        super.visitFieldDeclaration(node);
    }

    public void visitInitializer(Initializer node)
    {
        evaluateUsages(node);
        super.visitInitializer(node);
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        evaluateUsages(node);
        super.visitInterfaceDeclaration(node);
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        evaluateUsages(node);
        super.visitMethodDeclaration(node);
    }

    public void visitType(Type node)
    {
        _types.addType(node);
    }
}
