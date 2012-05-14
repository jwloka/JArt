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
package jart.restructuring.packagelevel;

import jart.restructuring.Restructuring;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.Package;
import jast.helpers.StringArray;

public class CreatePackage extends Restructuring
{
    /** The project to add the package to */
    private Project _project  = null;
    /** The name of the new package */
    private String  _pckgName = null;

    public Package getCreatedPackage()
    {
        return _project.getPackage(_pckgName);
    }

    public boolean analyze()
    {
        if (_project.getPackage(_pckgName) != null)
        {
            _results.addError("A package with the name "+_pckgName+" alrady exists",
                               null);
            return false;
        }

        StringArray parts   = StringArray.fromString(_pckgName, ".");
        String      partial = "";

        for (int idx = 1; idx <= parts.getCount(); idx++)
        {
            partial = parts.asString(".", idx);
            if (_project.getType(partial) != null)
            {
                _results.addError("A type with the name "+partial+" alrady exists",
                                   null);
                return false;
            }
        }
        return true;
    }

    /**
     * Initializes the restructuring. The input node is the project to which
     * the package shall be addded. The processing data solely consists of the
     * name of the new package.
     *
     * @param  nodes          The project
     * @param  processingData The package name
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        if ((nodes == null)          || (nodes.length == 0) ||
            (processingData == null) || (processingData.length == 0))
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
        if (!(nodes[0] instanceof Project))
        {
            _results.addFatalError("The node must be a project",
                                    null);
            return false;
        }
        if (processingData.length > 1)
        {
            _results.addFatalError("Only one input processing data item is allowed",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof String))
        {
            _results.addFatalError("The processing data item must be a string denoting the qualified package name",
                                    null);
            return false;
        }

        _project  = (Project)nodes[0];
        _pckgName = (String)processingData[0];
        return true;
    }

    public boolean perform()
    {
        _results.markPackageAsNew(_project.ensurePackage(_pckgName));

        return true;
    }
}
