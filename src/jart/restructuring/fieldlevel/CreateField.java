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
import jart.restructuring.DefaultValueHelper;
import jart.restructuring.Restructuring;
import jast.Global;
import jast.SyntaxException;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Expression;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableInitializer;

public class CreateField extends Restructuring
{
    /** The type which will contain the new field */
    private TypeDeclaration  _typeDecl = null;
    /** The new field */
    private FieldDeclaration _decl     = null;

    public boolean analyze()
    {
        FieldNameChecker checker = new FieldNameChecker(_results, _typeDecl);

        checker.check(_decl.getName());
        return !_results.hasFatalErrors() &&
               !_results.hasErrors();
    }

    public FieldDeclaration getCreatedField()
    {
        return _decl;
    }

    /**
     * Initializes the restructuring. The input node is the type declaration
     * within which the new field will be declared. The processing data
     * consists of the type and the name as well as the initializerfor the
     * new field declaration (the latter is optional). Note that an initializer
     * is not checked (yet?) for validity.
     *
     * @param  nodes          The type declaration
     * @param  processingData The type, name, and optionally the initializer of the field
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
        if ((processingData.length < 2) || (processingData.length > 3))
        {
            _results.addFatalError("Two or three parameters are required: type, name, (initializer)",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Type))
        {
            _results.addFatalError("The first parameter must be the type of the new field",
                                    null);
            return false;
        }
        if (!(processingData[1] instanceof String))
        {
            _results.addFatalError("The second parameter must be the name of the new field",
                                    null);
            return false;
        }

        NodeFactory         factory = Global.getFactory();
        Modifiers           mods    = factory.createModifiers();
        Type                type    = (Type)processingData[0];
        VariableInitializer init    = null;

        if (processingData.length == 3)
        {
            if (processingData[2] instanceof Expression)
            {
                init = factory.createSingleInitializer((Expression)processingData[2]);
            }
            else if (processingData[2] instanceof VariableInitializer)
            {
                init = (VariableInitializer)processingData[2];
            }
            else
            {
                _results.addFatalError("The third parameter must be the initializing expression of the new field",
                                        null);
                return false;
            }
        }
        if (init == null)
        {
            init = factory.createSingleInitializer(DefaultValueHelper.getDefaultValueFor(type));
        }

        _typeDecl = (TypeDeclaration)nodes[0];
        if (_typeDecl instanceof ClassDeclaration)
        {
            mods.setPrivate();
        }
        else
        {
            mods.setPublic();
            mods.setFinal();
            mods.setStatic();
        }
        _decl = factory.createFieldDeclaration(mods,
                                               type,
                                               (String)processingData[1],
                                               init);
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
