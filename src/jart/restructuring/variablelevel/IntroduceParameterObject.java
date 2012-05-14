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
package jart.restructuring.variablelevel;
import jart.analysis.RedefinitionAnalysis;
import jart.restructuring.Restructuring;
import jart.restructuring.fieldlevel.CreateAccessorMethod;
import jart.restructuring.fieldlevel.CreateField;
import jart.restructuring.methodlevel.ConstructorAccessGatherer;
import jart.restructuring.methodlevel.CreateConstructor;
import jart.restructuring.methodlevel.MethodAccessGatherer;
import jart.restructuring.typelevel.CreateTopLevelType;
import jast.Global;
import jast.analysis.AnalysisHelper;
import jast.ast.ASTHelper;
import jast.ast.ExpressionReplacementVisitor;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.Project;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.Invocation;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;

import java.util.Enumeration;
import java.util.Vector;

public class IntroduceParameterObject extends Restructuring
{
    /** The invocable declaration whose parameters will be replaced */
    private InvocableDeclaration          _invocDecl     = null;
    /** If the invocable declaration is a method then its redefinitions */
    private Vector                        _redefinitions = null;
    /** The qualified name of the new parameter object type */
    private String                        _paramTypeName = null;
    /** The name of the new parameter */
    private String                        _paramName     = null;
    /** Helper restructuring to remove all assignments to affected parameters */
    private RemoveAssignmentsToParameters _assignClearer = null;
    /** Helper restructuring to create a top-level type */
    private CreateTopLevelType            _typeCreator   = null;

    public TypeDeclaration getCreatedType()
    {
        return _typeCreator.getCreatedType();
    }

    public boolean analyze()
    {
        if (paramListIsEmpty())
        {
            _results.addNote("Nothing to do because the given "+
                             (_invocDecl instanceof MethodDeclaration ?
                                 "method" :
                                 "constructor")+
                             " has no parameters.", _invocDecl);
            return true;
        }
        if (_invocDecl instanceof MethodDeclaration)
        {
            _redefinitions = new RedefinitionAnalysis().getRedefinitions((MethodDeclaration)_invocDecl);
            if (!_redefinitions.isEmpty())
            {
                _results.addWarning("Other methods that are redefined by the method (base methods) or redefine it or a base method of it, will have to be changed as well",
                                    _invocDecl);
            }
        }

        Node[]   nodes = { ASTHelper.getProjectOf(_invocDecl) };
        Object[] args  = { new Boolean(true), _paramTypeName };

        _typeCreator = new CreateTopLevelType();
        _typeCreator.setResultContainer(_results);
        if (!_typeCreator.initialize(nodes, args))
        {
            return false;
        }
        if (!_typeCreator.analyze())
        {
            return false;
        }
        return true;
    }

    private boolean createParameterType(Vector getterMappings)
    {
        if (!_typeCreator.perform())
        {
            return false;
        }

        ClassDeclaration     paramType            = (ClassDeclaration)_typeCreator.getCreatedType();
        CreateField          fieldCreator         = new CreateField();
        CreateAccessorMethod accessorCreator      = new CreateAccessorMethod();
        Node[]               fieldCreatorNodes    = { paramType };
        Object[]             fieldCreatorArgs     = new Object[2];
        Node[]               accessorCreatorNodes = new Node[1];
        Object[]             accessorCreatorArgs  = new Object[2];
        FieldArray           createdFields        = new FieldArrayImpl();
        FormalParameter      param;

        fieldCreator.setResultContainer(_results);
        accessorCreator.setResultContainer(_results);
        for (FormalParameterIterator it = _invocDecl.getParameterList().getParameters().getIterator(); it.hasNext();)
        {
            param = it.getNext();

            fieldCreatorArgs[0] = param.getType().getClone();
            fieldCreatorArgs[1] = param.getName();

            if (!fieldCreator.initialize(fieldCreatorNodes, fieldCreatorArgs) ||
                !fieldCreator.analyze() ||
                !fieldCreator.perform())
            {
                return false;
            }
            createdFields.add(fieldCreator.getCreatedField());

            accessorCreatorNodes[0] = fieldCreator.getCreatedField();
            accessorCreatorArgs[0]  = new Boolean(true);
            accessorCreatorArgs[1]  = CreateAccessorMethod.generateDefaultAccessorName(
                                          param.getName(),
                                          param.getType(),
                                          true);

            if (!accessorCreator.initialize(accessorCreatorNodes, accessorCreatorArgs) ||
                !accessorCreator.analyze() ||
                !accessorCreator.perform())
            {
                return false;
            }
            accessorCreator.getCreatedMethod().getModifiers().setPublic();
            getterMappings.addElement(accessorCreator.getCreatedMethod());

            accessorCreatorArgs[0]  = new Boolean(false);
            accessorCreatorArgs[1]  = CreateAccessorMethod.generateDefaultAccessorName(
                                          param.getName(),
                                          param.getType(),
                                          false);

            if (!accessorCreator.initialize(accessorCreatorNodes, accessorCreatorArgs) ||
                !accessorCreator.analyze() ||
                !accessorCreator.perform())
            {
                return false;
            }
            accessorCreator.getCreatedMethod().getModifiers().setPublic();
        }

        CreateConstructor constructorCreator = new CreateConstructor();
        Node[]            nodes              = { paramType };
        Object[]          args               = new Object[2*createdFields.getCount()];
        int               idx                = 0;
        FieldDeclaration  field;

        for (FieldIterator it = createdFields.getIterator(); it.hasNext();)
        {
            field       = it.getNext();
            args[idx++] = field;
            args[idx++] = field.getName();
        }
        constructorCreator.setResultContainer(_results);
        if (!constructorCreator.initialize(nodes, args) ||
            !constructorCreator.analyze() ||
            !constructorCreator.perform())
        {
            return false;
        }
        // the CreateConstructor restructuring will have created an explicit
        // default constructor but which we don't want
        paramType.getConstructors().remove(0);

        // we remove the parameter type from the list of changed types
        // as from the perspective of the whole restructuring it is new
        _results.getChangedUnits().remove(
            ASTHelper.getCompilationUnitOf(paramType).getName());

        return true;
    }

    /**
     * Initializes the restructuring. The input node is the constructor or
     * method whose parameters shall be replaced by a parameter object.
     * The processing data consists of the qualified name of the new
     * parameter object type and the name of the new parameter.
     *
     * @param  nodes          The invocable declaration
     * @param  processingData The qualified name of the new parameter object type
     *                        and the name of the new parameter
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        if ((nodes == null) ||
            (nodes.length == 0) ||
            (processingData == null) ||
            (processingData.length == 0))
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
        if (!(nodes[0] instanceof InvocableDeclaration))
        {
            _results.addFatalError("The input node must be a method or constructor declaration",
                                    null);
            return false;
        }
        if (processingData.length != 2)
        {
            _results.addFatalError("Only one processing data item is required",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof String))
        {
            _results.addFatalError("The first processing data item must be a string",
                                    null);
            return false;
        }
        if (!(processingData[1] instanceof String))
        {
            _results.addFatalError("The second processing data item must be a string",
                                    null);
            return false;
        }
        _invocDecl     = (InvocableDeclaration)nodes[0];
        _paramTypeName = (String)processingData[0];
        _paramName     = (String)processingData[1];

        return true;
    }

    private void markUnitOf(TypeDeclaration typeDecl)
    {
        if (typeDecl.getProperties() != null)
        {
            typeDecl.getProperties().deleteProperty(jast.analysis.Usages.PROPERTY_LABEL);
        }
        _results.getChangedUnits().add(ASTHelper.getCompilationUnitOf(typeDecl));
    }

    private boolean paramListIsEmpty()
    {
        return (_invocDecl.getParameterList() == null) ||
               _invocDecl.getParameterList().getParameters().isEmpty();
    }

    public boolean perform()
    {
        if (paramListIsEmpty())
        {
            return true;
        }

        Vector getterMappings = new Vector();

        if (!createParameterType(getterMappings))
        {
            return false;
        }

        if (_invocDecl instanceof MethodDeclaration)
        {
            MethodDeclaration method = (MethodDeclaration)_invocDecl;

            if (!updateInvocationsOf(method) ||
                !updateInvocable(method, getterMappings))
            {
                return false;
            }
            for (Enumeration en = _redefinitions.elements(); en.hasMoreElements();)
            {
                method = (MethodDeclaration)en.nextElement();
                if (!updateInvocationsOf(method) ||
                    !updateInvocable(method, getterMappings))
                {
                    return false;
                }
                _results.getChangedUnits().add(ASTHelper.getCompilationUnitOf(method));
            }
        }
        else
        {
            ConstructorDeclaration constructor = (ConstructorDeclaration)_invocDecl;

            if (!updateInvocationsOf(constructor) ||
                !updateInvocable(constructor, getterMappings))
            {
                return false;
            }
        }
        _results.getChangedUnits().add(ASTHelper.getCompilationUnitOf(_invocDecl));
        return true;
    }

    private boolean updateInvocable(InvocableDeclaration invocable,
                                    Vector               getterMappings)
    {
        RemoveAssignmentsToParameters helper      = new RemoveAssignmentsToParameters();
        Node[]                        helperNodes = { invocable.getParameterList() };
        Object[]                      helperArgs  = { "var" };

        // first we remove all assignments to the parameters
        if (!helper.initialize(helperNodes, helperArgs) ||
            !helper.analyze() ||
            !helper.perform())
        {
            return false;
        }

        NodeFactory     factory  = Global.getFactory();
        FormalParameter newParam = factory.createFormalParameter(
                                       false,
                                       factory.createType(_typeCreator.getCreatedType(), 0),
                                       _paramName);

        // next we change the remaining accesses to invocations of
        // the appropriate getter methods
        ParameterAccessGatherer      gatherer = new ParameterAccessGatherer();
        ExpressionReplacementVisitor replacer = new ExpressionReplacementVisitor();
        int                          idx      = 0;
        FormalParameter              param;
        MethodDeclaration            getter;
        Vector                       accesses;

        for (FormalParameterIterator it = invocable.getParameterList().getParameters().getIterator(); it.hasNext(); idx++)
        {
            param    = it.getNext();
            getter   = (MethodDeclaration)getterMappings.elementAt(idx);
            accesses = gatherer.getAccesses(param);
            for (Enumeration en = accesses.elements(); en.hasMoreElements();)
            {
                replacer.replace(
                    (VariableAccess)en.nextElement(),
                    factory.createMethodInvocation(
                        factory.createVariableAccess(newParam),
                        getter,
                        null));

            }
        }

        // finally we replace the parameter list
        invocable.getParameterList().getParameters().clear();
        invocable.getParameterList().getParameters().add(newParam);

        return true;
    }

    private boolean updateInvocations(Vector accesses)
    {
        NodeFactory  factory = Global.getFactory();
        Invocation   invoc;
        ArgumentList args;

        if (accesses.isEmpty())
        {
            return false;
        }
        for (Enumeration en = accesses.elements(); en.hasMoreElements();)
        {
            invoc = (Invocation)en.nextElement();
            args  = factory.createArgumentList();
            args.getArguments().add(
                factory.createInstantiation(
                    null,
                    factory.createType(_typeCreator.getCreatedType(), 0),
                    invoc.getArgumentList(),
                    null));
            invoc.setArgumentList(args);
        }
        return true;
    }

    private boolean updateInvocationsOf(ConstructorDeclaration constructor)
    {
        ConstructorAccessGatherer gatherer = new ConstructorAccessGatherer();
        Project                   project  = ASTHelper.getProjectOf(constructor);
        TypeDeclaration           curType;

        for (PackageIterator pckgIt = project.getPackages().getIterator(); pckgIt.hasNext();)
        {
            for (TypeDeclarationIterator typeIt = pckgIt.getNext().getTypes().getIterator(); typeIt.hasNext();)
            {
                curType = typeIt.getNext();
                if (curType == _typeCreator.getCreatedType())
                {
                    continue;
                }
                if (AnalysisHelper.ensureUsagesPresent(curType).isUsed(constructor))
                {
                    if (updateInvocations(gatherer.getAccesses(curType, constructor)))
                    {
                        markUnitOf(curType);
                    }
                }
            }
        }
        return true;
    }

    private boolean updateInvocationsOf(MethodDeclaration method)
    {
        MethodAccessGatherer gatherer = new MethodAccessGatherer();
        Project              project  = ASTHelper.getProjectOf(method);
        TypeDeclaration      curType;

        for (PackageIterator pckgIt = project.getPackages().getIterator(); pckgIt.hasNext();)
        {
            for (TypeDeclarationIterator typeIt = pckgIt.getNext().getTypes().getIterator(); typeIt.hasNext();)
            {
                curType = typeIt.getNext();
                if (curType == _typeCreator.getCreatedType())
                {
                    continue;
                }
                if (AnalysisHelper.ensureUsagesPresent(curType).isUsed(method))
                {
                    if (updateInvocations(gatherer.getAccesses(curType, method)))
                    {
                        markUnitOf(curType);
                    }
                }
            }
        }
        return true;
    }
}
