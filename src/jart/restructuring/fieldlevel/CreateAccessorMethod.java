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
import jart.analysis.AnalysisFunction;
import jart.analysis.IsGetterMethod;
import jart.analysis.IsSetterMethod;
import jart.restructuring.Restructuring;
import jart.restructuring.methodlevel.CreateMethod;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.Project;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Primary;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.MethodIterator;

public class CreateAccessorMethod extends Restructuring
{
    /** The type which will contain the new field */
    private TypeDeclaration   _typeDecl     = null;
    /** The field to create the accessor for */
    private FieldDeclaration  _fieldDecl    = null;
    /** The name of the new method */
    private String            _methodName   = null;
    /** Helper restructuring */
    private CreateMethod      _createMethod = null;
    /** Whether we create a getter method */
    private boolean           _createGetter = true;

    public boolean analyze()
    {
        if (!_createGetter && _fieldDecl.getModifiers().isFinal())
        {
            _results.addError("Cannot create a setter method for a final field",
                              _fieldDecl);
            return false;
        }
        if (!_createMethod.analyze())
        {
            return false;
        }

        AnalysisFunction  checker = _createGetter ?
                                        (AnalysisFunction)(new IsGetterMethod(_fieldDecl)) :
                                        (AnalysisFunction)(new IsSetterMethod(_fieldDecl));
        Project           project = ASTHelper.getProjectOf(_typeDecl);
        MethodDeclaration methodDecl;

        for (MethodIterator it = _typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            methodDecl = it.getNext();
            if (checker.check(project, methodDecl))
            {
                _results.addWarning("The field already has a "+
                                    (_createGetter ? "getter" : "setter")+
                                    " method",
                                    methodDecl);
            }
        }
        return true;
    }

    public static String generateAccessorName(String fieldName,
                                              String prefix)
    {
        int pos = 0;

        while (!Character.isLetter(fieldName.charAt(pos)))
        {
            pos++;
        }

        StringBuffer result = new StringBuffer();

        result.append(prefix);
        if (fieldName.substring(pos).toLowerCase().startsWith(prefix))
        {
            pos += prefix.length();
        }
        else
        {
            result.append(Character.toUpperCase(fieldName.charAt(pos)));
            pos++;
        }
        if (pos < fieldName.length())
        {
            result.append(fieldName.substring(pos));
        }
        return result.toString();
    }

    public MethodDeclaration getCreatedMethod()
    {
        return _createMethod.getCreatedMethod();
    }

    /**
     * Initializes the restructuring. The input node is the field declaration
     * for which a accessor method shall be created. The processing data
     * consists of a boolean indicating whether to create a getter method,
     * and the name of the accessor method (possibly generated via the
     * supplied helper method).
     *
     * @param  nodes          The field declaration
     * @param  processingData Whether to create a getter method, and the name of
     *                        the new method
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
        if (!(nodes[0] instanceof FieldDeclaration))
        {
            _results.addFatalError("The node must be a field declaration",
                                    null);
            return false;
        }
        if (processingData.length != 2)
        {
            _results.addFatalError("Exactly two processing data item are allowed",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Boolean))
        {
            _results.addFatalError("The first processing data item must be a boolean",
                                    null);
            return false;
        }
        if (!(processingData[1] instanceof String))
        {
            _results.addFatalError("The second processing data item must be the name of the new method",
                                    null);
            return false;
        }
        _fieldDecl    = (FieldDeclaration)nodes[0];
        _typeDecl     = ASTHelper.getTypeDeclarationOf(_fieldDecl);
        _createGetter = ((Boolean)processingData[0]).booleanValue();
        _methodName   = (String)processingData[1];
        _createMethod = new CreateMethod();
        _createMethod.setResultContainer(_results);

        Node[]   helperNodes = { _typeDecl };
        Object[] helperData  = null;

        if (_createGetter)
        {
            helperData    = new Object[2];
            helperData[0] = _fieldDecl.getType().getClone();
            helperData[1] = _methodName;
        }
        else
        {
            helperData    = new Object[4];
            helperData[0] = Global.getFactory().createNullType();
            helperData[1] = _methodName;
            helperData[2] = _fieldDecl.getType().getClone();
            helperData[3] = stripLeadingNonAlphaChars(_fieldDecl.getName());
        }

        return _createMethod.initialize(helperNodes, helperData);
    }

    public boolean perform()
    {
        if (!_createMethod.perform())
        {
            return false;
        }

        NodeFactory       factory    = Global.getFactory();
        Modifiers         fieldMods  = _fieldDecl.getModifiers();
        MethodDeclaration methodDecl;

        if (_createGetter)
        {
            // we now have to
            // * replace the default value in the return statement with an
            //   access to the field
            // * change the method visibility to match the field
            methodDecl = _typeDecl.getMethods().get(_methodName, null);

            ReturnStatement returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);

            returnStmt.setReturnValue(Global.getFactory().createFieldAccess(_fieldDecl));
        }
        else
        {
            // we now have to
            // * add an assignment to the field
            // * change the method visibility to match the field
            Type[] paramTypes = { _fieldDecl.getType() };

            methodDecl = _typeDecl.getMethods().get(_methodName, paramTypes);

            FormalParameter param    = methodDecl.getParameterList().getParameters().get(0);
            Primary         baseExpr = null;

            if (param.getName().equals(_fieldDecl.getName()))
            {
                baseExpr = factory.createSelfAccess(null, false);
            }
            methodDecl.getBody().getBlockStatements().add(
                factory.createExpressionStatement(
                    factory.createAssignmentExpression(
                        factory.createFieldAccess(baseExpr, _fieldDecl),
                        AssignmentExpression.ASSIGN_OP,
                        factory.createVariableAccess(param))));
        }

        // we ignore final, transient, volatile
        if (fieldMods.isPublic())
        {
            methodDecl.getModifiers().setPublic();
        }
        else if (fieldMods.isProtected())
        {
            methodDecl.getModifiers().setProtected();
        }
        else if (fieldMods.isFriendly())
        {
            methodDecl.getModifiers().setFriendly();
        }
        if (fieldMods.isStatic())
        {
            methodDecl.getModifiers().setStatic();
        }

        return true;
    }

    private static String stripLeadingNonAlphaChars(String name)
    {
        int pos = 0;

        while (!Character.isLetter(name.charAt(pos)))
        {
            pos++;
        }
        return name.substring(pos);
    }


    public static String generateDefaultAccessorName(String  fieldName,
                                                     Type    fieldType,
                                                     boolean isGetter)
    {
        if (isGetter)
        {
            if (!fieldType.isArray() && fieldType.isPrimitive() &&
                (fieldType.getPrimitiveBaseType() == PrimitiveType.BOOLEAN_TYPE))
            {
                return generateAccessorName(fieldName, "is");
            }
            else
            {
                return generateAccessorName(fieldName, "get");
            }
        }
        else
        {
            return generateAccessorName(fieldName, "set");
        }
    }
}
