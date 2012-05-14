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
import jart.restructuring.DefaultValueHelper;
import jart.restructuring.Restructuring;
import jast.Global;
import jast.SyntaxException;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;

public class CreateMethod extends Restructuring
{
    /** The type which will contain the new field */
    private TypeDeclaration   _typeDecl = null;
    /** The new method */
    private MethodDeclaration _decl     = null;

    public boolean analyze()
    {
        new MethodSignatureChecker(_results, _typeDecl).
                check(_decl.getName(),
                      _decl.getReturnType(),
                      _decl.getParameterList());
        return !_results.hasFatalErrors() &&
               !_results.hasErrors();
    }

    public MethodDeclaration getCreatedMethod()
    {
        return _decl;
    }

    /**
     * Initializes the restructuring. The input node is the type declaration
     * within which the new method will be declared. The processing data
     * consists of the return type and the name of the new method as well
     * as all parameters as type and name.
     *
     * @param  nodes          The type declaration
     * @param  processingData In that order:<br>
     *                        The return type and the method name
     *                        for each parameter the type and the name of it
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
        if (!(nodes[0] instanceof TypeDeclaration))
        {
            _results.addFatalError("The node must be a type declaration",
                                    null);
            return false;
        }
        if (processingData.length < 2)
        {
            _results.addFatalError("At least two parameters are required: return type and method name",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Type))
        {
            _results.addFatalError("The first parameter must be the return type of the method",
                                    null);
            return false;
        }
        if (!(processingData[1] instanceof String))
        {
            _results.addFatalError("The second parameter must be the name of the new method",
                                    null);
            return false;
        }

        NodeFactory         factory    = Global.getFactory();
        Modifiers           mods       = factory.createModifiers();
        Type                returnType = (Type)processingData[0];
        FormalParameterList params     = null;

        _typeDecl = (TypeDeclaration)nodes[0];
        if (_typeDecl instanceof ClassDeclaration)
        {
            mods.setPrivate();
        }
        else
        {
            mods.setPublic();
        }

        if (returnType.isNullType())
        {
            returnType = null;
        }
        if ((returnType != null) &&
            !Global.getResolver().resolveExternal(returnType, _typeDecl))
        {
            _results.addFatalError("Could not determine the actual return type",
                                    null);
            return false;
        }

        if (processingData.length > 2)
        {
            int idx = 2;

            params = factory.createFormalParameterList();

            for (; idx + 1 < processingData.length; idx += 2)
            {
                if (!(processingData[idx] instanceof Type))
                {
                    _results.addFatalError("The parameter nr. "+idx+" must be the type of a parameter",
                                            null);
                    return false;
                }
                if (!Global.getResolver().resolveExternal((Type)processingData[idx], _typeDecl))
                {
                    _results.addFatalError("Could not determine the actual type of parameter nr. "+(idx/2),
                                            null);
                    return false;
                }
                if (!(processingData[idx + 1] instanceof String))
                {
                    _results.addFatalError("The parameter nr. "+(idx+1)+" must be the name of a parameter",
                                            null);
                    return false;
                }
                params.getParameters().add(
                    factory.createFormalParameter(
                        false,
                        (Type)processingData[idx],
                        (String)processingData[idx+1]));
            }
            if (idx != processingData.length)
            {
                _results.addFatalError("Too much parameters",
                                        null);
                return false;
            }
        }

        _decl = factory.createMethodDeclaration(
                    mods,
                    returnType,
                    (String)processingData[1],
                    params);
        if (_typeDecl instanceof ClassDeclaration)
        {
            Block body = factory.createBlock();

            if (_decl.hasReturnType())
            {
                body.getBlockStatements().add(
                    factory.createReturnStatement(
                        DefaultValueHelper.getDefaultValueFor(
                            _decl.getReturnType())));
            }
            _decl.setBody(body);
        }
        return true;
    }

    public boolean perform()
    {
        try
        {
            _typeDecl.addDeclaration(_decl);
            _results.markUnitAsChanged(ASTHelper.getCompilationUnitOf(_typeDecl));
            return true;
        }
        catch (SyntaxException ex)
        {
            return false;
        }
    }
}
