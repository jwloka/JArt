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
import jart.restructuring.Restructuring;
import jast.Global;
import jast.SyntaxException;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Primary;
import jast.ast.nodes.Type;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FormalParameterIterator;

import java.util.Hashtable;

public class CreateConstructor extends Restructuring
{
    /** The class which will contain the new field */
    private ClassDeclaration       _classDecl = null;
    /** The new constructor */
    private ConstructorDeclaration _decl      = null;

    public ConstructorDeclaration getCreatedConstructor()
    {
        return _decl;
    }

    public boolean analyze()
    {
        ConstructorDeclaration constructor;

        for (ConstructorIterator it = _classDecl.getConstructors().getIterator(); it.hasNext();)
        {
            constructor = it.getNext();
            if (_decl.getSignature().equals(constructor.getSignature()))
            {
                _results.addError("A constructor of the same signature is already defined in the class",
                                   constructor);
                break;
            }
            else if (constructor.hasParameters() &&
                     constructor.getParameterList().canBeAmbiguousTo(_decl.getParameterList()))
            {
                _results.addError("A constructor that may lead to ambiguous instantiations is already defined in the class",
                                   constructor);
                break;
            }
        }
        return !_results.hasFatalErrors() &&
               !_results.hasErrors();
    }

    /**
     * Initializes the restructuring. The input node is the type declaration
     * within which the new constructor will be declared. The processing data
     * consists of the return types and the names field declarations of the
     * parameters. If field declarations are given, appropiate initializing
     * assignment expressions will be automatically created.
     *
     * @param  nodes          The type declaration
     * @param  processingData For each parameter the type and the name of it
     *                        or a field declaration
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        if ((nodes == null) ||
            (nodes.length == 0))
        {
            _results.addFatalError("No valid input data given",
                                    null);
            return false;
        }
        if (nodes.length > 1)
        {
            _results.addFatalError("Only one input node is allowed",
                                    null);
            return false;
        }
        if (!(nodes[0] instanceof ClassDeclaration))
        {
            _results.addFatalError("The node must be a class declaration",
                                    null);
            return false;
        }
        if (nodes[0] instanceof AnonymousClassDeclaration)
        {
            _results.addFatalError("The node must not be an anonymous class declaration",
                                    null);
            return false;
        }

        NodeFactory         factory    = Global.getFactory();
        Modifiers           mods       = factory.createModifiers();
        FormalParameterList params     = null;
        Hashtable           initFields = new Hashtable();

        _classDecl = (ClassDeclaration)nodes[0];
        mods.setPublic();

        if ((processingData != null) && (processingData.length > 0))
        {
            FieldDeclaration fieldDecl;
            FormalParameter  param;
            String           name;
            int              idx = 0;

            params = factory.createFormalParameterList();

            for (; idx < processingData.length-1; idx += 2)
            {
                if (!(processingData[idx+1] instanceof String))
                {
                    _results.addFatalError("The parameter nr. "+(idx+1)+" must be the name of a parameter",
                                            null);
                    return false;
                }
                name = (String)processingData[idx+1];

                if (processingData[idx] instanceof FieldDeclaration)
                {
                    fieldDecl = (FieldDeclaration)processingData[idx];
                    if (fieldDecl.getContainer() != _classDecl)
                    {
                        _results.addFatalError("The field used as parameter nr. "+idx+" is not defined in the target type",
                                                null);
                        return false;
                    }
                    param = factory.createFormalParameter(
                                false,
                                fieldDecl.getType().getClone(),
                                name);
                    initFields.put(param, fieldDecl);
                }
                else
                {
                    if (!(processingData[idx] instanceof Type))
                    {
                        _results.addFatalError("The parameter nr. "+idx+" must be the type of a parameter",
                                                null);
                        return false;
                    }
                    param = factory.createFormalParameter(
                                false,
                                (Type)processingData[idx],
                                name);
                }
                if (!Global.getResolver().resolveExternal(param.getType(), _classDecl))
                {
                    _results.addFatalError("Could not determine the actual type of parameter nr. "+(idx/2),
                                            null);
                    return false;
                }
                params.getParameters().add(param);
            }
            if (idx != processingData.length)
            {
                _results.addFatalError("Too much parameters",
                                        null);
                return false;
            }
        }

        _decl = factory.createConstructorDeclaration(
                    mods,
                    _classDecl.getName(),
                    params);

        Block body = factory.createBlock();

        if (!initFields.isEmpty())
        {
            FieldDeclaration fieldDecl;
            FormalParameter  param;
            Primary          baseExpr;

            for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
            {
                param     = it.getNext();
                fieldDecl = (FieldDeclaration)initFields.get(param);

                if (fieldDecl == null)
                {
                    continue;
                }

                baseExpr = null;
                if (param.getName().equals(fieldDecl.getName()))
                {
                    baseExpr = factory.createSelfAccess(null, false);
                }

                body.getBlockStatements().add(
                    factory.createExpressionStatement(
                        factory.createAssignmentExpression(
                            factory.createFieldAccess(baseExpr,fieldDecl),
                            AssignmentExpression.ASSIGN_OP,
                            factory.createVariableAccess(param))));
            }
        }
        _decl.setBody(body);

        return true;
    }

    public boolean perform()
    {
        try
        {
            if (_decl.getParameterList() != null)
            {
                // do we have to create a default constructor
                if (_classDecl.getConstructors().isEmpty())
                {
                    Modifiers mods = Global.getFactory().createModifiers();

                    mods.setPublic();

                    ConstructorDeclaration defaultConstructor =
                        Global.getFactory().createConstructorDeclaration(
                            mods,
                            _classDecl.getName(),
                            null);

                    defaultConstructor.setBody(
                        Global.getFactory().createBlock());
                    _classDecl.addDeclaration(defaultConstructor);

                    _results.addNote("Added explicit default constructor",
                                      defaultConstructor);
                }
            }
            _classDecl.addDeclaration(_decl);
            _results.markUnitAsChanged(ASTHelper.getCompilationUnitOf(_classDecl));
            return true;
        }
        catch (SyntaxException ex)
        {
            return false;
        }
    }
}
