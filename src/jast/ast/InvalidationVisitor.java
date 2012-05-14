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
package jast.ast;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.Feature;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Package;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.visitor.DescendingVisitor;

import java.util.Hashtable;

// Unresolves all references in the project to types/constructors/methods/fields
// defined in the given compilation unit
public class InvalidationVisitor extends DescendingVisitor
{
    private Hashtable       _invalidDeclarations = new Hashtable();
    private CompilationUnit _targetUnit;
    private boolean         _isValid;

    private void addInvalidDeclarations(TypeDeclaration typeDecl)
    {
        _invalidDeclarations.put(typeDecl, typeDecl);

        Feature feature;

        for (FieldIterator it = typeDecl.getFields().getIterator(); it.hasNext();)
        {
            feature = it.getNext();
            _invalidDeclarations.put(feature, feature);
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            for (ConstructorIterator it = ((ClassDeclaration)typeDecl).getConstructors().getIterator(); it.hasNext();)
            {
                feature = it.getNext();
                _invalidDeclarations.put(feature, feature);
            }
        }
        for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            feature = it.getNext();
            _invalidDeclarations.put(feature, feature);
        }
        for (TypeDeclarationIterator it = typeDecl.getInnerTypes().getIterator(); it.hasNext();)
        {
            addInvalidDeclarations(it.getNext());
        }
    }

    public void invalidate(CompilationUnit invalidUnit)
    {
        _targetUnit = invalidUnit;

        Package         pckg = invalidUnit.getPackage();
        TypeDeclaration type;

        for (TypeDeclarationIterator it = invalidUnit.getTypeDeclarations().getIterator(); it.hasNext();)
        {
            type = it.getNext();
            pckg.getTypes().remove(type);
            addInvalidDeclarations(type);
        }
        visitProject(ASTHelper.getProjectOf(_targetUnit));
    }

    private void invalidate(Type type)
    {
        if ((type == null) || !type.hasReferenceBaseTypeDecl())
        {
            return;
        }
        if (_invalidDeclarations.containsKey(type.getReferenceBaseTypeDecl()))
        {
            type.setReferenceBaseTypeDecl(null);
            _isValid = false;
        }
    }

    public void visitArrayCreation(ArrayCreation node)
    {
        invalidate(node.getCreatedType());
        super.visitArrayCreation(node);
    }

    public void visitClassAccess(ClassAccess node)
    {
        invalidate(node.getType());
        super.visitClassAccess(node);
    }

    public void visitCompilationUnit(CompilationUnit node)
    {
        // not necessary to visit the invalid unit
        if (node != _targetUnit)
        {
            _isValid = true;
            super.visitCompilationUnit(node);
            if (!_isValid)
            {
                node.setResolvingStatus(false);
            }
        }
    }

    public void visitConstructorInvocation(ConstructorInvocation node)
    {
        if (node.getConstructorDeclaration() != null)
        {
            if (_invalidDeclarations.containsKey(node.getConstructorDeclaration()))
            {
                node.setConstructorDeclaration(null);
                _isValid = false;
            }
        }
        super.visitConstructorInvocation(node);
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.getFieldDeclaration() != null)
        {
            if (_invalidDeclarations.containsKey(node.getFieldDeclaration()))
            {
                node.setFieldDeclaration(null);
                _isValid = false;
            }
        }
        super.visitFieldAccess(node);
    }

    public void visitInstantiation(Instantiation node)
    {
        if (node.getInvokedConstructor() != null)
        {
            if (_invalidDeclarations.containsKey(node.getInvokedConstructor()))
            {
                node.setInvokedConstructor(null);
                _isValid = false;
            }
        }
        if (node.withAnonymousClass())
        {
            invalidate(node.getAnonymousClass().getBaseClass());
        }
        invalidate(node.getType());
        super.visitInstantiation(node);
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        if (node.getMethodDeclaration() != null)
        {
            if (_invalidDeclarations.containsKey(node.getMethodDeclaration()))
            {
                node.setMethodDeclaration(null);
                _isValid = false;
            }
        }
        super.visitMethodInvocation(node);
    }

    public void visitSelfAccess(SelfAccess node)
    {
        invalidate(node.getType());
        super.visitSelfAccess(node);
    }

    public void visitStringLiteral(StringLiteral node)
    {
        invalidate(node.getType());
        super.visitStringLiteral(node);
    }

    public void visitType(Type node)
    {
        invalidate(node);
        super.visitType(node);
    }

    public void visitUnaryExpression(UnaryExpression node)
    {
        if (node.getOperator() == UnaryExpression.CAST_OP)
        {
            invalidate(node.getCastType());
        }
        super.visitUnaryExpression(node);
    }
}
