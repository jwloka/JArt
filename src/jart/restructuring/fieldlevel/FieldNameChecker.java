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
import jast.MessageReceiver;
import jast.analysis.AnalysisHelper;
import jast.analysis.InheritanceAnalyzer;
import jast.analysis.UsageAnalyzer;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeDeclarationStack;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.DescendingVisitor;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class FieldNameChecker extends DescendingVisitor
{
    private MessageReceiver      _msgReceiver;
    private UsageAnalyzer        _usageAnalyzer;
    private TypeDeclaration      _targetTypeDecl;
    private Hashtable            _targetBaseAndOuterTypes = new Hashtable();
    private String               _name;
    private TypeDeclarationStack _typeDeclStack  = new TypeDeclarationStack();
    private boolean              _isInTargetType = false;
    private Hashtable            _varDecls       = new Hashtable();

    public FieldNameChecker(MessageReceiver msgReceiver, TypeDeclaration targetTypeDecl)
    {
        _msgReceiver    = msgReceiver;
        _targetTypeDecl = targetTypeDecl;
        _usageAnalyzer  = new UsageAnalyzer(ASTHelper.getProjectOf(targetTypeDecl));

        AnalysisHelper.ensureUsagesPresent(targetTypeDecl);
        determineTargetBaseAndOuterTypes();
    }

    public void check(String name)
    {
        _name = name;
        try
        {
            ASTHelper.getCompilationUnitOf(_targetTypeDecl).accept(this);
        }
        catch (RuntimeException ex)
        {
        }
        checkSubTypes();
    }

    private void checkBaseType(TypeDeclaration typeDecl)
    {
        if (typeDecl != null)
        {
            FieldDeclaration fieldDecl = typeDecl.getFields().get(_name);

            if (fieldDecl != null)
            {
                Modifiers mods = fieldDecl.getModifiers();

                if (mods.isPublic() ||
                    mods.isProtected() ||
                    (mods.isFriendly() && ASTHelper.isInSamePackage(typeDecl, _targetTypeDecl)))
                {
                    checkBaseTypeField(fieldDecl);
                }
            }
            else
            {
                checkBaseTypes(typeDecl);
            }
        }
    }

    private void checkBaseTypeField(FieldDeclaration fieldDecl)
    {
        TypeDeclarationArray usingTypes = _usageAnalyzer.getTypeDeclarationsUsing(fieldDecl);
        TypeDeclaration      curTypeDecl;

        for (TypeDeclarationIterator it = usingTypes.getIterator(); it.hasNext();)
        {
            curTypeDecl = it.getNext();
            if (!_targetBaseAndOuterTypes.containsKey(curTypeDecl))
            {
                _msgReceiver.addError("A (base type) field of that name is used in a sub type",
                                      curTypeDecl);
                return;
            }
        }
        _msgReceiver.addWarning("A base type defines a field of that name but which is not used in the type or a sub type",
                                fieldDecl);
    }

    private void checkBaseTypes(TypeDeclaration node)
    {
        if (node instanceof ClassDeclaration)
        {
            ClassDeclaration classDecl = (ClassDeclaration)node;

            if (classDecl.hasBaseClass())
            {
                checkBaseType(classDecl.getBaseClass().getReferenceBaseTypeDecl());
            }
        }
        for (TypeIterator it = node.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            checkBaseType(it.getNext().getReferenceBaseTypeDecl());
        }
    }

    private void checkSubTypes()
    {
        TypeDeclarationArray subTypes = new InheritanceAnalyzer(_targetTypeDecl).getChildren(false);
        FieldDeclaration     fieldDecl;

        for (TypeDeclarationIterator it = subTypes.getIterator(); it.hasNext();)
        {
            fieldDecl = it.getNext().getFields().get(_name);
            if (fieldDecl != null)
            {
                _msgReceiver.addWarning("A sub type defines a field of that name",
                                        fieldDecl);
            }
        }
    }

    private void checkVariableDeclaration(VariableDeclaration node)
    {
        if (node.getName().equals(_name))
        {
            if (_isInTargetType)
            {
                _msgReceiver.addWarning("A local variable of that name shadows the field",
                                        node);
            }
            else
            {
                _varDecls.put(node, node);
            }
        }
    }

    private void determineTargetBaseAndOuterTypes()
    {
        TypeDeclaration curTypeDecl = _targetTypeDecl;
        Vector          queue       = new Vector();
        TypeDeclaration baseTypeDecl;

        queue.addElement(_targetTypeDecl);
        while (!queue.isEmpty())
        {
            curTypeDecl = (TypeDeclaration)queue.firstElement();
            queue.removeElementAt(0);
            if (curTypeDecl != _targetTypeDecl)
            {
                _targetBaseAndOuterTypes.put(curTypeDecl, curTypeDecl);
            }

            if ((curTypeDecl instanceof ClassDeclaration) &&
                ((ClassDeclaration)curTypeDecl).hasBaseClass())
            {
                baseTypeDecl = ((ClassDeclaration)curTypeDecl).getBaseClass().getReferenceBaseTypeDecl();
                if (baseTypeDecl != null)
                {
                    queue.addElement(baseTypeDecl);
                }
            }
            for (TypeIterator it = curTypeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                baseTypeDecl = it.getNext().getReferenceBaseTypeDecl();
                if (baseTypeDecl != null)
                {
                    queue.addElement(baseTypeDecl);
                }
            }
        }
        if (!_targetTypeDecl.isTopLevel())
        {
            curTypeDecl = _targetTypeDecl.getEnclosingType();;
            do
            {
                _targetBaseAndOuterTypes.put(curTypeDecl, curTypeDecl);
            }
            while (!curTypeDecl.isTopLevel());
        }
    }

    private void handleLeftOverDecls()
    {
        for (Enumeration en = _varDecls.elements(); en.hasMoreElements();)
        {
            _msgReceiver.addWarning("A enclosing method defines a variable of that name",
                                    (Node)en.nextElement());
        }
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        _typeDeclStack.push(node);
        if (node == _targetTypeDecl)
        {
            _isInTargetType = true;
        }
        super.visitClassDeclaration(node);
        checkBaseTypes(node);
        if (node == _targetTypeDecl)
        {
            _isInTargetType = false;
            handleLeftOverDecls();
        }
        _typeDeclStack.pop();
    }

    public void visitFieldAccess(FieldAccess node)
    {
        if (node.getFieldName().equals(_name))
        {
            boolean isOK = true;

            if (node.getBaseExpression() != null)
            {
                // if it is a field from a base/outer type then it is important
                // at which type the field is accessed
                // if it is the current type or a sub type then we have a problem
                TypeDeclaration definingType = ASTHelper.getTypeDeclarationOf(node.getFieldDeclaration());
                TypeDeclaration accessedType = node.getBaseExpression().getType().getReferenceBaseTypeDecl();

                if (_targetBaseAndOuterTypes.containsKey(definingType) &&
                    (accessedType != null) &&
                    !_targetBaseAndOuterTypes.containsKey(accessedType))
                {
                    isOK = false;
                }
            }
            else
            {
                // locally accessed field
                isOK = false;
            }
            if (!isOK && _isInTargetType)
            {
                _msgReceiver.addError("A (base type) field of that name is already used in the type",
                                      node);
                throw new RuntimeException();
            }
            else
            {
                _msgReceiver.addWarning("An outer type uses a (base type) field of that name",
                                        node);
            }
        }
        super.visitFieldAccess(node);
    }

    public void visitFieldDeclaration(FieldDeclaration node)
    {
        if (node.getName().equals(_name))
        {
            if (_typeDeclStack.top() == _targetTypeDecl)
            {
                _msgReceiver.addError("A field of that name is already defined in the type",
                                      node);
                throw new RuntimeException();
            }
            else if (_isInTargetType)
            {
                _msgReceiver.addWarning("An inner type defines a field of that name",
                                        node);
            }
            else
            {
                // an error is generated if an access to the field is found
                _msgReceiver.addWarning("An outer type defines a field of that name",
                                        node);
            }
        }
        super.visitFieldDeclaration(node);
    }

    public void visitFormalParameter(FormalParameter node)
    {
        checkVariableDeclaration(node);
        super.visitFormalParameter(node);
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        _typeDeclStack.push(node);
        if (node == _targetTypeDecl)
        {
            _isInTargetType = true;
        }
        super.visitInterfaceDeclaration(node);
        checkBaseTypes(node);
        if (node == _targetTypeDecl)
        {
            _isInTargetType = false;
            handleLeftOverDecls();
        }
        _typeDeclStack.pop();
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
    {
        checkVariableDeclaration(node);
        super.visitLocalVariableDeclaration(node);
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        super.visitMethodDeclaration(node);
    }

    public void visitVariableAccess(VariableAccess node)
    {
        if (node.getVariableName().equals(_name) &&
            _varDecls.containsKey(node.getVariableDeclaration()))
        {
            _msgReceiver.addError("A variable of that name defined outside of the type is used within it",
                                  node);
            throw new RuntimeException();
        }
        super.visitVariableAccess(node);
    }
}
