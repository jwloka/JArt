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
import jart.analysis.HasSideEffects;
import jart.analysis.IsGetterMethod;
import jart.analysis.IsSetterMethod;
import jart.restructuring.Restructuring;
import jast.Global;
import jast.SyntaxException;
import jast.ast.ASTHelper;
import jast.ast.CloneVisitor;
import jast.ast.ExpressionReplacementVisitor;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.Expression;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.Primary;
import jast.ast.nodes.Statement;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.collections.MethodIterator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class EncapsulateField extends Restructuring
{
    /** The field to encapsulate */
    private FieldDeclaration     _fieldDecl       = null;
    /** Whether to encapsulate uses within the type itself */
    private boolean              _selfEncapsulate = false;
    /** An existing getter that we can use */
    private MethodDeclaration    _getterMethod    = null;
    /** An existing setter that we can use */
    private MethodDeclaration    _setterMethod    = null;
    /** A helper restructuring for creating a getter */
    private CreateAccessorMethod _getterCreator   = null;
    /** A helper restructuring for creating a setter */
    private CreateAccessorMethod _setterCreator   = null;
    /** All accesses to the field */
    private Vector               _accesses        = null;

    public boolean analyze()
    {
        HasSideEffects sideEffectChecker = new HasSideEffects();
        Project        project           = ASTHelper.getProjectOf(_fieldDecl);
        boolean        needsGet          = false;
        boolean        needsSet          = false;
        FieldAccess    access;
        Node           container;

        _accesses = new FieldAccessGatherer().findAccesses(_fieldDecl, _selfEncapsulate);
        for (int idx = 0; idx < _accesses.size(); idx++)
        {
            access    = (FieldAccess)_accesses.elementAt(idx);
            container = access.getContainer();

            // we can to classify the accesses into:
            // * get
            // * simple set (assignment via =)
            // * other sets (assignment via +=, -=, ...)
            // * increments/decrements
            // Assignments and increments/decrements are only allowed if
            // their value is not used, e.g. if they are the single
            // expression in a statement (expression statement, for statement)
            // Furthermore, the base epression of the field access must
            // be side-effect free for non-simple assignments and increment
            // or decrement

            if (container instanceof AssignmentExpression)
            {
                AssignmentExpression assignExpr = (AssignmentExpression)container;

                // get or set depending on which side of the '='
                if (assignExpr.getLeftHandSide() == access)
                {
                    if (sideEffectChecker.check(project, access.getBaseExpression()))
                    {
                        _results.addError("An assignment to the field cannot be replaced because the base expression of the field access has side effects",
                                          assignExpr);
                        return false;
                    }
                    if (!(assignExpr.getContainer() instanceof Statement))
                    {
                        _results.addError("An assignment to the field cannot be replaced because its value is used",
                                          assignExpr);
                        return false;
                    }
                    needsSet = true;
                    if (assignExpr.getOperator() != AssignmentExpression.ASSIGN_OP)
                    {
                        needsGet = true;
                    }
                }
                else
                {
                    needsGet = true;
                }
            }
            else if (container instanceof UnaryExpression)
            {
                UnaryExpression unaryExpr = (UnaryExpression)container;

                // possibly prefix increment/decrement
                if ((unaryExpr.getOperator() == UnaryExpression.INCREMENT_OP) ||
                    (unaryExpr.getOperator() == UnaryExpression.DECREMENT_OP))
                {
                    if (sideEffectChecker.check(project, access.getBaseExpression()))
                    {
                        _results.addError("A"+
                                          (unaryExpr.getOperator() == UnaryExpression.INCREMENT_OP ?
                                              "n increment" :
                                              " decrement")+
                                          " of the field cannot be replaced because the base expression of the field access has side effects",
                                          unaryExpr);
                        return false;
                    }
                    if (!(unaryExpr.getContainer() instanceof Statement))
                    {
                        _results.addError("A"+
                                          (unaryExpr.getOperator() == UnaryExpression.INCREMENT_OP ?
                                              "n increment" :
                                              " decrement")+
                                          " of the field cannot be replaced because its value is used",
                                          unaryExpr);
                        return false;
                    }
                    needsGet = true;
                    needsSet = true;
                }
            }
            else if (container instanceof PostfixExpression)
            {
                PostfixExpression postfixExpr = (PostfixExpression)container;

                // postfix increment/decrement
                if (sideEffectChecker.check(project, access.getBaseExpression()))
                {
                    _results.addError("A"+
                                      (postfixExpr.isIncrement() ?
                                          "n increment" :
                                          " decrement")+
                                      " of the field cannot be replaced because the base expression of the field access has side effects",
                                      postfixExpr);
                    return false;
                }
                if (!(postfixExpr.getContainer() instanceof Statement))
                {
                    _results.addError("A"+
                                      (postfixExpr.isIncrement() ?
                                          "n increment" :
                                          " decrement")+
                                      " of the field cannot be replaced because its value is used",
                                      postfixExpr);
                    return false;
                }
                needsGet = true;
                needsSet = true;
            }
            else
            {
                needsGet = true;
            }
        }
        if (!needsGet && !needsSet)
        {
            return true;
        }

        TypeDeclaration   typeDecl      = ASTHelper.getTypeDeclarationOf(_fieldDecl);
        IsGetterMethod    getterChecker = new IsGetterMethod(_fieldDecl);
        IsSetterMethod    setterChecker = new IsSetterMethod(_fieldDecl);
        String            getterName    = CreateAccessorMethod.generateDefaultAccessorName(
                                              _fieldDecl.getName(),
                                              _fieldDecl.getType(),
                                              true);
        String            setterName    = CreateAccessorMethod.generateDefaultAccessorName(
                                              _fieldDecl.getName(),
                                              _fieldDecl.getType(),
                                              false);
        MethodDeclaration method;

        for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            method = it.getNext();
            if (needsGet &&
                getterChecker.check(project, method))
            {
                // we can use an existing getter if the return type is ok
                // otherwise we have to create a new one via CreateAccessorMethod
                if (_fieldDecl.getType().isEqualTo(method.getReturnType()))
                {
                    if ((_getterMethod == null) ||
                        getterName.equals(method.getName()))
                    {
                        _getterMethod = method;
                    }
                }
            }
            else if (needsSet &&
                     setterChecker.check(project, method))
            {
                // we can use an existing setter if the parameter type is ok
                // otherwise we have to create a new one via CreateAccessorMethod
                if (_fieldDecl.getType().isEqualTo(
                        method.getParameterList().getParameters().get(0).getType()))
                {
                    if ((_setterMethod == null) ||
                        setterName.equals(method.getName()))
                    {
                        _setterMethod = method;
                    }
                }
            }
        }

        Node[]   helperNodes = { _fieldDecl };
        Object[] helperArgs  = new Object[2];

        if (needsGet)
        {
            if (_getterMethod == null)
            {
                _getterCreator = new CreateAccessorMethod();
                _getterCreator.setResultContainer(_results);

                helperArgs[0] = new Boolean(true);
                helperArgs[1] = getterName;

                if (!_getterCreator.initialize(helperNodes, helperArgs) ||
                    !_getterCreator.analyze())
                {
                    return false;
                }
            }
            else
            {
                _results.addNote("An existing getter method is used",
                                 _getterMethod);
            }
        }
        if (needsSet)
        {
            if (_setterMethod == null)
            {
                _setterCreator = new CreateAccessorMethod();
                _setterCreator.setResultContainer(_results);

                helperArgs[0] = new Boolean(false);
                helperArgs[1] = setterName;

                if (!_setterCreator.initialize(helperNodes, helperArgs) ||
                    !_setterCreator.analyze())
                {
                    return false;
                }
            }
            else
            {
                _results.addNote("An existing setter method is used",
                                 _setterMethod);
            }
        }

        return true;
    }

    private MethodInvocation createGetterInvocation(Primary baseExpr)
    {
        return Global.getFactory().createMethodInvocation(
                   baseExpr,
                   _getterMethod,
                   null);
    }

    private MethodInvocation createSetterInvocation(Primary    baseExpr,
                                                    Expression value,
                                                    int        assignmentOperator)
    {
        ArgumentList args = Global.getFactory().createArgumentList();

        if (assignmentOperator == AssignmentExpression.ASSIGN_OP)
        {
            args.getArguments().add(value);
        }
        else
        {
            int operator = AssignmentExpression.ASSIGN_OP;

            switch (assignmentOperator)
            {
                case AssignmentExpression.MULTIPLY_ASSIGN_OP :
                    operator = BinaryExpression.MULTIPLY_OP;
                    break;
                case AssignmentExpression.DIVIDE_ASSIGN_OP :
                    operator = BinaryExpression.DIVIDE_OP;
                    break;
                case AssignmentExpression.MOD_ASSIGN_OP :
                    operator = BinaryExpression.MOD_OP;
                    break;
                case AssignmentExpression.PLUS_ASSIGN_OP :
                    operator = BinaryExpression.PLUS_OP;
                    break;
                case AssignmentExpression.MINUS_ASSIGN_OP :
                    operator = BinaryExpression.MINUS_OP;
                    break;
                case AssignmentExpression.SHIFT_LEFT_ASSIGN_OP :
                    operator = BinaryExpression.SHIFT_LEFT_OP;
                    break;
                case AssignmentExpression.SHIFT_RIGHT_ASSIGN_OP :
                    operator = BinaryExpression.SHIFT_RIGHT_OP;
                    break;
                case AssignmentExpression.ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP :
                    operator = BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP;
                    break;
                case AssignmentExpression.BITWISE_AND_ASSIGN_OP :
                    operator = BinaryExpression.BITWISE_AND_OP;
                    break;
                case AssignmentExpression.BITWISE_XOR_ASSIGN_OP :
                    operator = BinaryExpression.BITWISE_XOR_OP;
                    break;
                case AssignmentExpression.BITWISE_OR_ASSIGN_OP :
                    operator = BinaryExpression.BITWISE_OR_OP;
                    break;
            }

            BinaryExpression binExpr = Global.getFactory().createBinaryExpression(
                                           operator,
                                           createGetterInvocation(baseExpr),
                                           value);

            if (baseExpr != null)
            {
                // we have to create a clone of the original base expression
                // as it is now used twice (for the getter and for the setter)
                baseExpr = (Primary)new CloneVisitor().createClone(baseExpr);
            }
            args.getArguments().add(binExpr);
        }

        return Global.getFactory().createMethodInvocation(
                   baseExpr,
                   _setterMethod,
                   args);
    }

    private void ensureModifiers(MethodDeclaration accessor)
    {
        if (accessor == null)
        {
            return;
        }

        Modifiers accessorMods = accessor.getModifiers();
        Modifiers fieldMods    = _fieldDecl.getModifiers();

        if (fieldMods.isPublic())
        {
            accessorMods.setPublic();
        }
        else if (fieldMods.isProtected())
        {
            if (!accessorMods.isPublic())
            {
                accessorMods.setProtected();
            }
        }
        else if (fieldMods.isFriendly())
        {
            if (!accessorMods.isPublic() &&
                !accessorMods.isProtected())
            {
                accessorMods.setFriendly();
            }
        }

        if (fieldMods.isStatic())
        {
            accessorMods.setStatic();
        }
    }

    /**
     * Initializes the restructuring. The input node is the field declaration
     * to encapsulate. The processing data optionally consists of one boolean item
     * indicating whether uses within the same type should be encapsulated
     * as well.
     *
     * @param  nodes          The field declaration
     * @param  processingData Optionally whether also encapsulate uses within
     *                        the type itself (default: false)
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        if ((nodes == null) || (nodes.length == 0))
        {
            _results.addFatalError("No valid input data given",
                                    null);
            return false;
        }
        if (processingData != null)
        {
            if (processingData.length > 1)
            {
                _results.addFatalError("No processing data is required",
                                        null);
                return false;
            }
            if (processingData.length == 1)
            {
                if (!(processingData[0] instanceof Boolean))
                {
                    _results.addFatalError("The single processing data item must be a boolean",
                                            null);
                    return false;
                }
                _selfEncapsulate = ((Boolean)processingData[0]).booleanValue();
            }
        }
        if (nodes.length > 1)
        {
            _results.addFatalError("Only one input node is allowed",
                                    null);
            return false;
        }
        if (!(nodes[0] instanceof FieldDeclaration))
        {
            _results.addFatalError("The node must be a field declaration",
                                    null);
            return false;
        }
        _fieldDecl = (FieldDeclaration)nodes[0];
        return true;
    }

    public boolean perform()
    {
        CompilationUnit unit         = ASTHelper.getCompilationUnitOf(_fieldDecl);
        Hashtable       changedUnits = new Hashtable();

        if (_getterCreator != null)
        {
            if (!_getterCreator.perform())
            {
                return false;
            }
            _getterMethod = _getterCreator.getCreatedMethod();
            changedUnits.put(unit, unit);
        }
        ensureModifiers(_getterMethod);
        if (_setterCreator != null)
        {
            if (!_setterCreator.perform())
            {
                if (_getterCreator != null)
                {
                    ASTHelper.getTypeDeclarationOf(_fieldDecl).getMethods().remove(
                        _getterMethod.getName(), null);
                }
                return false;
            }
            _setterMethod = _setterCreator.getCreatedMethod();
            changedUnits.put(unit, unit);
        }
        ensureModifiers(_setterMethod);

        MethodInvocation invoc;
        FieldAccess      access;
        Node             container;

        for (int idx = 0; idx < _accesses.size(); idx++)
        {
            access    = (FieldAccess)_accesses.elementAt(idx);
            container = access.getContainer();

            if (container instanceof AssignmentExpression)
            {
                AssignmentExpression assignExpr = (AssignmentExpression)container;

                // get or set depending on which side of the '='
                if (assignExpr.getLeftHandSide() == access)
                {
                    replace(assignExpr,
                            createSetterInvocation(access.getBaseExpression(),
                                                   assignExpr.getValueExpression(),
                                                   assignExpr.getOperator()));
                }
                else
                {
                    replace(assignExpr.getValueExpression(),
                            createGetterInvocation(access.getBaseExpression()));
                }
            }
            else if (container instanceof UnaryExpression)
            {
                UnaryExpression unaryExpr = (UnaryExpression)container;
                IntegerLiteral  intLit    = null;

                try
                {
                    intLit = Global.getFactory().createIntegerLiteral("1");
                }
                catch (SyntaxException ex)
                {}
                // possibly prefix increment/decrement
                if (unaryExpr.getOperator() == UnaryExpression.INCREMENT_OP)
                {
                    replace(unaryExpr,
                            createSetterInvocation(
                                access.getBaseExpression(),
                                intLit,
                                AssignmentExpression.PLUS_ASSIGN_OP));
                }
                else if (unaryExpr.getOperator() == UnaryExpression.DECREMENT_OP)
                {
                    replace(unaryExpr,
                            createSetterInvocation(
                                access.getBaseExpression(),
                                intLit,
                                AssignmentExpression.MINUS_ASSIGN_OP));
                }
                else
                {
                    replace(access,
                            createGetterInvocation(access.getBaseExpression()));
                }
            }
            else if (container instanceof PostfixExpression)
            {
                PostfixExpression postfixExpr = (PostfixExpression)container;
                IntegerLiteral    intLit      = null;

                try
                {
                    intLit = Global.getFactory().createIntegerLiteral("1");
                }
                catch (SyntaxException ex)
                {}

                replace(postfixExpr,
                        createSetterInvocation(
                            access.getBaseExpression(),
                            intLit,
                            postfixExpr.isIncrement() ?
                                AssignmentExpression.PLUS_ASSIGN_OP :
                                AssignmentExpression.MINUS_ASSIGN_OP));
            }
            else
            {
                replace(access,
                        createGetterInvocation(access.getBaseExpression()));
            }
            unit = ASTHelper.getCompilationUnitOf(access);
            changedUnits.put(unit, unit);
        }
        if (!_fieldDecl.getModifiers().isPrivate())
        {
            _fieldDecl.getModifiers().setPrivate();
            unit = ASTHelper.getCompilationUnitOf(_fieldDecl);
            changedUnits.put(unit, unit);
        }

        for (Enumeration en = changedUnits.keys(); en.hasMoreElements();)
        {
            _results.getChangedUnits().add((CompilationUnit)en.nextElement());
        }

        return true;
    }

    private void replace(Expression originalChild, Expression newChild)
    {
        new ExpressionReplacementVisitor().replace(originalChild, newChild);
    }
}
