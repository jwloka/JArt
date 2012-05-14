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
package jart.restructuring.fieldlevel;
import jart.analysis.IsGetterMethod;
import jart.analysis.IsSetterMethod;
import jast.analysis.AnalysisHelper;
import jast.ast.ASTHelper;
import jast.ast.Project;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeDeclarationStack;
import jast.ast.visitor.DescendingVisitor;

import java.util.Vector;

public class FieldAccessGatherer extends DescendingVisitor
{
    private Project              _project;
    private TypeDeclaration      _definingTypeDecl;
    private FieldDeclaration     _targetField;
    private Vector               _foundAccesses;
    private boolean              _checkDefiningType;
    private TypeDeclarationStack _typeStack = new TypeDeclarationStack();
    private IsGetterMethod       _getterChecker;
    private IsSetterMethod       _setterChecker;

    public Vector findAccesses(FieldDeclaration fieldDecl, boolean checkDefiningType)
    {
        _targetField       = fieldDecl;
        _definingTypeDecl  = ASTHelper.getTypeDeclarationOf(_targetField);
        _project           = ASTHelper.getProjectOf(_definingTypeDecl);
        _foundAccesses     = new Vector();
        _checkDefiningType = checkDefiningType;
        _getterChecker     = new IsGetterMethod(_targetField);
        _setterChecker     = new IsSetterMethod(_targetField);

        for (PackageIterator pckgIt = _project.getPackages().getIterator(); pckgIt.hasNext();)
        {
            for (TypeDeclarationIterator typeIt = pckgIt.getNext().getTypes().getIterator(); typeIt.hasNext();)
            {
                TypeDeclaration curType = typeIt.getNext();
                if (AnalysisHelper.ensureUsagesPresent(curType).isUsed(_targetField))
                {
                    _typeStack.clear();
                    curType.accept(this);
                }
            }
        }

        return _foundAccesses;
    }

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
    {
        if (!_checkDefiningType && (node == _definingTypeDecl))
        {
            return;
        }
        _typeStack.push(node);
        super.visitAnonymousClassDeclaration(node);
        _typeStack.pop();
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        if (!_checkDefiningType && (node == _definingTypeDecl))
        {
            return;
        }
        _typeStack.push(node);
        super.visitClassDeclaration(node);
        _typeStack.pop();
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.getFieldDeclaration() == _targetField)
        {
            _foundAccesses.addElement(node);
        }
        super.visitFieldAccess(node);
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        if (!_checkDefiningType && (node == _definingTypeDecl))
        {
            return;
        }
        _typeStack.push(node);
        super.visitInterfaceDeclaration(node);
        _typeStack.pop();
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        if (_typeStack.top() == _definingTypeDecl)
        {
            // no need to determine field accesses in accessors
            // of the field within its defining type
            if (_getterChecker.check(_project, node) ||
                _setterChecker.check(_project, node))
            {
                return;
            }
        }
        super.visitMethodDeclaration(node);
    }
}
