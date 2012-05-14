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
package jart.restructuring.methodlevel;

import jast.MessageReceiver;
import jast.analysis.InheritanceAnalyzer;
import jast.ast.ASTHelper;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeDeclarationStack;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.DescendingVisitor;

import java.util.Hashtable;

public class MethodSignatureChecker extends DescendingVisitor
{
    private MessageReceiver      _msgReceiver;
    private TypeDeclaration      _targetTypeDecl;
    private String               _name;
    private Type                 _returnType;
    private FormalParameterList  _params;
    private String               _signature;
    private Hashtable            _redefinedMethods = new Hashtable();
    private TypeDeclarationStack _typeDeclStack    = new TypeDeclarationStack();
    private boolean              _inTargetType     = false;

    public MethodSignatureChecker(MessageReceiver msgReceiver, TypeDeclaration targetTypeDecl)
    {
        _msgReceiver    = msgReceiver;
        _targetTypeDecl = targetTypeDecl;
    }

    public void check(String name, Type returnType, FormalParameterList params)
    {
        _name       = name;
        _returnType = returnType;
        _params     = params;
        _signature  = name + "(" + (params != null ? params.getSignature() : "") + ")";
        _redefinedMethods.clear();

        try
        {
            ASTHelper.getCompilationUnitOf(_targetTypeDecl).accept(this);
            checkSubTypes();
        }
        catch (RuntimeException ex)
        {
        }
    }

    private void checkBaseType(TypeDeclaration typeDecl)
    {
        if (typeDecl == null)
        {
            return;
        }

        MethodDeclaration methodDecl;
        Modifiers         mods;

        for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            methodDecl = it.getNext();
            mods       = methodDecl.getModifiers();

            if (mods.isPublic() ||
                mods.isProtected() ||
                (mods.isFriendly() && ASTHelper.isInSamePackage(typeDecl, _targetTypeDecl)))
            {
                checkMethodDeclaration(methodDecl,
                                       true,
                                       true,
                                       "a base type");
                if (_signature.equals(methodDecl.getSignature()))
                {
                    _redefinedMethods.put(methodDecl, methodDecl);
                }
            }
        }
        checkBaseTypes(typeDecl);
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

    private void checkImplementations(TypeDeclarationArray subTypes)
    {
        Hashtable       subTypesByName = new Hashtable();
        Hashtable       typesWithDecl  = new Hashtable();
        TypeDeclaration typeDecl;

        for (TypeDeclarationIterator typeIt = subTypes.getIterator(); typeIt.hasNext();)
        {
            typeDecl = typeIt.getNext();
            subTypesByName.put(typeDecl.getName(), typeDecl);
        }
        for (TypeDeclarationIterator typeIt = subTypes.getIterator(); typeIt.hasNext();)
        {
            checkImplementationsIn(typeIt.getNext(),
                                   subTypesByName,
                                   typesWithDecl);
        }
    }

    private void checkImplementationsIn(TypeDeclaration subTypeDecl,
                                        Hashtable       allSubTypes,
                                        Hashtable       subTypesWithDecl)
    {
        if (subTypeDecl.getMethods().contains(_signature))
        {
            subTypesWithDecl.put(subTypeDecl, subTypeDecl);
            return;
        }
        if (subTypeDecl instanceof InterfaceDeclaration)
        {
            return;
        }

        ClassDeclaration subClassDecl = (ClassDeclaration)subTypeDecl;

        if (!subClassDecl.getModifiers().isAbstract())
        {
            ClassDeclaration baseClass = subClassDecl.hasBaseClass() ?
                                             (ClassDeclaration)subClassDecl.getBaseClass().getReferenceBaseTypeDecl() :
                                             null;

            if ((baseClass != null) && allSubTypes.containsKey(baseClass))
            {
                if (!subTypesWithDecl.containsKey(baseClass))
                {
                    checkImplementationsIn(baseClass, allSubTypes, subTypesWithDecl);
                }
            }
            if ((baseClass == null) || !subTypesWithDecl.containsKey(baseClass))
            {
                _msgReceiver.addError("A non-abstract sub class does not implement the method",
                                      subClassDecl);
                throw new RuntimeException();
            }
        }
    }

    private void checkMethodDeclaration(MethodDeclaration methodDecl,
                                        boolean           allowSameSignature,
                                        boolean           checkReturnType,
                                        String            originText)
    {
        if (!_name.equals(methodDecl.getName()))
        {
            return;
        }
        if (_signature.equals(methodDecl.getSignature()))
        {
            if (allowSameSignature)
            {
                if (checkReturnType)
                {
                    boolean sameReturnType =
                                ((_returnType == null) && !methodDecl.hasReturnType()) ||
                                ((_returnType != null) && _returnType.isEqualTo(methodDecl.getReturnType()));

                    if (!sameReturnType)
                    {
                        _msgReceiver.addError("A method of the same signature but with a different return type is already defined in "+originText,
                                              methodDecl);
                        throw new RuntimeException();
                    }
                    else
                    {
                        _msgReceiver.addWarning("A method of the same signature and return type is already defined in "+originText,
                                                methodDecl);
                    }
                }
                else
                {
                    _msgReceiver.addWarning("A method of the same signature is already defined in "+originText,
                                            methodDecl);
                }
            }
            else
            {
                _msgReceiver.addError("A method of the same signature is already defined in "+originText,
                                      methodDecl);
            }
        }
        else if (methodDecl.hasParameters() &&
                 methodDecl.getParameterList().canBeAmbiguousTo(_params))
        {
            _msgReceiver.addError("A method that may lead to ambiguous invocations is defined in "+originText,
                                  methodDecl);
            throw new RuntimeException();
        }
    }

    private void checkMethodInvocation(MethodInvocation methodInvoc)
    {
        MethodDeclaration methodDecl = methodInvoc.getMethodDeclaration();

        if (methodDecl == null)
        {
            if (!_name.equals(methodInvoc.getMethodName()))
            {
                return;
            }

            ArgumentList args = methodInvoc.getArgumentList();

            if (_params == null)
            {
                if ((args != null) &&
                    !args.getArguments().isEmpty())
                {
                    return;
                }
            }
            else
            {
                if ((args == null) ||
                    (args.getArguments().getCount() != _params.getParameters().getCount()))
                {
                    return;
                }
            }
            _msgReceiver.addWarning("There is an unresolved invocation of a method with the same name and number of parameters",
                                    methodInvoc);
            return;
        }

        if (!_name.equals(methodDecl.getName()))
        {
            return;
        }
        if (_signature.equals(methodDecl.getSignature()))
        {
            if (_redefinedMethods.containsKey(methodDecl))
            {
                _msgReceiver.addWarning("A method that is redefined by the new method is invoked within the type",
                                        methodInvoc);
            }
            else
            {
                _msgReceiver.addError("A method of the same signature is invoked within the type",
                                      methodInvoc);
                throw new RuntimeException();
            }
        }
        else if ((_params != null) &&
                 _params.isApplicableTo(methodInvoc.getArgumentList()))
        {
            _msgReceiver.addError("A method invocation has been found that may be ambiguous in respect to the new method",
                                  methodInvoc);
            throw new RuntimeException();
        }
    }

    private void checkSubTypes()
    {
        TypeDeclarationArray subTypes = new InheritanceAnalyzer(_targetTypeDecl).getChildren(false);

        for (TypeDeclarationIterator typeIt = subTypes.getIterator(); typeIt.hasNext();)
        {
            for (MethodIterator methodIt = typeIt.getNext().getMethods().getIterator(); methodIt.hasNext();)
            {
                checkMethodDeclaration(methodIt.getNext(),
                                       true,
                                       true,
                                       "a sub type");
            }
        }

        // since we cannot provide a default implementation for a method in
        // an interface, we have to ensure that all sub types of the interface
        // are either abstract/interfaces or redefine the method
        if (_targetTypeDecl instanceof InterfaceDeclaration)
        {
            checkImplementations(subTypes);
        }
    }

    private boolean isInLocalClass()
    {
        TypeDeclaration typeDecl;

        for (TypeDeclarationIterator it = _typeDeclStack.getIterator(); it.hasNext();)
        {
            typeDecl = it.getNext();
            if (typeDecl == _targetTypeDecl)
            {
                break;
            }
            if ((typeDecl instanceof ClassDeclaration) &&
                ((ClassDeclaration)typeDecl).isLocal())
            {
                return true;
            }
        }
        return false;
    }

    public void visitClassDeclaration(ClassDeclaration node)
    {
        if (node == _targetTypeDecl)
        {
            _inTargetType = true;
        }
        _typeDeclStack.push(node);
        checkBaseTypes(node);
        super.visitClassDeclaration(node);
        if (node == _targetTypeDecl)
        {
            _inTargetType = false;
        }
        _typeDeclStack.pop();
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration node)
    {
        if (node == _targetTypeDecl)
        {
            _inTargetType = true;
        }
        _typeDeclStack.push(node);
        checkBaseTypes(node);
        super.visitInterfaceDeclaration(node);
        if (node == _targetTypeDecl)
        {
            _inTargetType = false;
        }
        _typeDeclStack.pop();
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        if (_typeDeclStack.top() == _targetTypeDecl)
        {
            checkMethodDeclaration(node,
                                   false,
                                   false,
                                   "the target type");
        }
        else
        {
            checkMethodDeclaration(node,
                                   true,
                                   _inTargetType &&
                                   !isInLocalClass() &&
                                   (_typeDeclStack.top() instanceof ClassDeclaration),
                                   "the compilation unit");
        }
        super.visitMethodDeclaration(node);
    }

    public void visitMethodInvocation(MethodInvocation node)
    {
        if (_inTargetType)
        {
            checkMethodInvocation(node);
        }
        super.visitMethodInvocation(node);
    }
}
