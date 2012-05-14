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
package jast.analysis;
import jast.ast.ASTHelper;
import jast.ast.Project;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeDeclarationStack;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.DescendingVisitor;



/**
 * Determines for a given type which types and their features it
 * accesses
 */
public class UsageVisitor extends DescendingVisitor
{
    private Project              _project;
    private UsagesStack          _usages;
    private TypeDeclarationStack _localTypes;
    private boolean              _inLocalClass = false;

    public UsageVisitor()
    {
        _usages     = new UsagesStack();
        _localTypes = new TypeDeclarationStack();
    }

    private void addAccessedConstructor(
        Type accessedType,
        ConstructorDeclaration constructor)
    {
        if ((accessedType != null)
            && accessedType.isReference()
            && (_usages.top() instanceof FeatureUsages))
        {
            ((FeatureUsages) _usages.top()).addUsage(
                accessedType.getReferenceBaseTypeDecl(),
                constructor);
        }
    }

    private void addAccessedField(Type accessedType, FieldDeclaration field)
    {
        if ((accessedType != null) && (_usages.top() instanceof FeatureUsages))
        {
            if (accessedType.isArray())
            {
                ((FeatureUsages) _usages.top()).addUsage(_project.getArrayType(), field);
            }
            else if (accessedType.isReference())
            {
                ((FeatureUsages) _usages.top()).addUsage(
                    accessedType.getReferenceBaseTypeDecl(),
                    field);
            }
        }
    }

    private void addAccessedMethod(Type accessedType, MethodDeclaration method)
    {
        if ((accessedType != null) && (_usages.top() instanceof FeatureUsages))
        {
            if (accessedType.isArray())
            {
                ((FeatureUsages) _usages.top()).addUsage(_project.getArrayType(), method);
            }
            else if (accessedType.isReference())
            {
                ((FeatureUsages) _usages.top()).addUsage(
                    accessedType.getReferenceBaseTypeDecl(),
                    method);
            }
        }
    }

    private void addAccessedType(Type accessedType)
    {
        if ((accessedType != null) && accessedType.isReference())
        {
            _usages.top().addUsage(accessedType.getReferenceBaseTypeDecl());
        }
    }

    private boolean shouldGetUsages(TypeDeclaration type)
    {
        if (_inLocalClass)
        {
            return false;
        }
        else
        {
            return !(type instanceof ClassDeclaration) ||
                   !((ClassDeclaration) type).isLocal();
        }
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        super.visitArrayCreation(node);

        addAccessedType(node.getType());
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        if (shouldGetUsages(node))
        {
            _localTypes.push(node);
            _usages.push(new TypeUsages(_localTypes.top()));
        }

        boolean alreadyInLocal = _inLocalClass;

        if (node.isLocal())
        {
            _inLocalClass = true;
        }
        super.visitClassDeclaration(node);
        if (node.isLocal())
        {
            _inLocalClass = alreadyInLocal;
        }

        if ((node.getBaseClass() != null)
            && !node.getBaseClass().getQualifiedName().equals("java.lang.Object"))
        {
            addAccessedType(node.getBaseClass());
        }

        for (TypeIterator iter = node.getBaseInterfaces().getIterator();
            iter.hasNext();
            )
        {
            addAccessedType(iter.getNext());
        }

        if (shouldGetUsages(node))
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        _project = ASTHelper.getProjectOf(node);
        for (TypeDeclarationIterator iter = node.getTypeDeclarations().getIterator();
            iter.hasNext();
            )
        {
            iter.getNext().accept(this);
        }
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        if (!_inLocalClass)
        {
            _usages.push(new FeatureUsages(_localTypes.top()));
        }

        super.visitConstructorDeclaration(node);

        for (TypeIterator iter = node.getThrownExceptions().getIterator();
            iter.hasNext();
            )
        {
            addAccessedType(iter.getNext());
        }

        if (!_inLocalClass)
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        super.visitConstructorInvocation(node);

        // We can ignore the type as it must be known (the current or
        // a base type)
        addAccessedConstructor(
            _localTypes.top().getCreatedType(),
            node.getConstructorDeclaration());
    }

    public void visitFieldAccess(FieldAccess node)
    {
        super.visitFieldAccess(node);

        if (node.isTrailing())
        {
            addAccessedType(node.getBaseExpression().getType());

            addAccessedField(
                node.getBaseExpression().getType(),
                node.getFieldDeclaration());
        }
        else
        {
            addAccessedField(
                _localTypes.top().getCreatedType(),
                node.getFieldDeclaration());
        }
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        if (!_inLocalClass)
        {
            _usages.push(new FeatureUsages(_localTypes.top()));
        }

        super.visitFieldDeclaration(node);

        addAccessedType(node.getType());

        if (!_inLocalClass)
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitFormalParameter(FormalParameter node)
    {
        addAccessedType(node.getType());
    }

    public void visitInitializer(Initializer node)
    {
        if (!_inLocalClass)
        {
            _usages.push(new FeatureUsages(_localTypes.top()));
        }

        super.visitInitializer(node);

        if (!_inLocalClass)
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitInstanceofExpression(InstanceofExpression node)
    {
        super.visitInstanceofExpression(node);

        addAccessedType(node.getReferencedType());
    }

    public void visitInstantiation(Instantiation node)
    {
        super.visitInstantiation(node);

        // =!= addAccessedType(node.getInstantiatedType());

        addAccessedConstructor(
            node.getInstantiatedType(),
            node.getInvokedConstructor());
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        if (shouldGetUsages(node))
        {
            _localTypes.push(node);
            _usages.push(new TypeUsages(_localTypes.top()));
        }

        super.visitInterfaceDeclaration(node);

        for (TypeIterator it = node.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            addAccessedType(it.getNext());
        }

        if (shouldGetUsages(node))
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        super.visitLocalVariableDeclaration(node);

        addAccessedType(node.getType());
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        if (!_inLocalClass)
        {
            _usages.push(new FeatureUsages(_localTypes.top()));
        }

        super.visitMethodDeclaration(node);

        if (node.hasReturnType())
        {
            addAccessedType(node.getReturnType());
        }
        for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
        {
            addAccessedType(it.getNext());
        }

        if (!_inLocalClass)
        {
            node.setProperty(Usages.PROPERTY_LABEL, _usages.pop());
        }
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        super.visitMethodInvocation(node);

        if (node.isTrailing())
        {
            addAccessedType(node.getBaseExpression().getType());

            addAccessedMethod(
                node.getBaseExpression().getType(),
                node.getMethodDeclaration());
        }
        else
        {
            addAccessedMethod(
                _localTypes.top().getCreatedType(),
                node.getMethodDeclaration());
        }
    }

    public void visitProject(Project node)
    {
        _project = node;

        for (PackageIterator pkgIter = node.getPackages().getIterator();
            pkgIter.hasNext();
            )
        {
            for (TypeDeclarationIterator typeIter =
                pkgIter.getNext().getTypes().getIterator();
                typeIter.hasNext();
                )
            {
                typeIter.getNext().accept(this);
            }
        }
    }

    public void visitTypeAccess(TypeAccess node)
    {
        super.visitTypeAccess(node);

        if (node.isTrailing())
        {
            addAccessedType(node.getBaseExpression().getType());
        }
        addAccessedType(node.getType());
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        super.visitUnaryExpression(node);

        if (node.getOperator() == UnaryExpression.CAST_OP)
        {
            addAccessedType(node.getCastType());
        }
    }
}
