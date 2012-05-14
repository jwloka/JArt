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
package jart.restructuring.tidyimports;

import jart.restructuring.Restructuring;
import jast.Global;
import jast.analysis.UsageVisitor;
import jast.ast.Node;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.collections.ImportArray;
import jast.ast.nodes.collections.ImportIterator;
import jast.helpers.HashArray;
import jast.helpers.ObjectIterator;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;

import java.util.Enumeration;
import java.util.Hashtable;

public class TidyImports extends Restructuring
{
    private CompilationUnit      _unit;
    private int                  _threshold;
    private UsedPackagesAndTypes _types;
    private HashArray            _importedPackages = new HashArray();

    /**
     * Determines whether tidy imports is possible. As we assume that
     * the code is syntactically correct, this restructuring is always
     * possible
     *
     * @return <code>true</code>
     */
    public boolean analyze()
    {
        return true;
    }

    private void createImports()
    {
        ImportArray imports = _unit.getImportDeclarations();
        StringArray pckgImports;
        String      importStmt;
        boolean     isOnDemand;

        imports.clear();

        for (ObjectIterator pckgIt = _importedPackages.getObjects(); pckgIt.hasNext();)
        {
            pckgImports = (StringArray)pckgIt.getNext();
            for (StringIterator typeIt = pckgImports.getIterator(); typeIt.hasNext();)
            {
                importStmt = typeIt.getNext();
                isOnDemand = importStmt.endsWith("*");
                if (isOnDemand)
                {
                    importStmt = importStmt.substring(0, importStmt.length()-2);
                }
                imports.add(
                    Global.getFactory().createImportDeclaration(
                        importStmt,
                        isOnDemand));
            }
        }
    }

    /**
     * Initializes the restructuring which requires a compilation unit
     * but no processing data (is ignored).<br>
     * In addition, a threshold (<code>Integer</code>) for on-demand import
     * is necessary. If more types from the same package are used, then an
     * on-demand import is generated instead of direct imports.
     *
     * @param  nodes          Contains the node of the
     *                        compilation unit that is to be tidyied
     * @param  processingData Contains the project and the on-demand threshold
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[]   nodes,
                              Object[] processingData)
    {
        if ((nodes == null) || (nodes.length != 1) ||
            (processingData == null) || (processingData.length != 1))
        {
            _results.addFatalError("No valid input data given",
                                    null);
            return false;
        }

        if (!(nodes[0] instanceof CompilationUnit))
        {
            _results.addFatalError("Can only operate on compilation units",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Integer))
        {
            _results.addFatalError("TidyImports requires the project and the on-demand threshold",
                                    null);
            return false;
        }
        _unit = (CompilationUnit)nodes[0];
        if (!_unit.isComplete())
        {
            Global.getParser().uncomplete(_unit);
            Global.getResolver().resolve(_unit);
            _unit.accept(new UsageVisitor());
        }
        _threshold = ((Integer)processingData[0]).intValue();
        if (_threshold < 0)
        {
            _results.addWarning("The on-demand threshold must be non-negative; using 0",
                                 null);
            _threshold = 0;
        }
        return true;
    }

    /**
     * Performs the restructuring. Note that it does not check whether
     * the restructuring is applicable. Therefore it should only be
     * performed if <code>analyze</code> returned <code>true</code>.
     *
     * @return <code>false</code> if the restructuring couldn't be
     *         performed
     */
    public boolean perform()
    {
        _types = new UsedPackagesAndTypes(_unit, _threshold);

        sortImportedPckgs();
        createImports();

        _results.markUnitAsChanged(_unit);

        return true;
    }

    private void sortImportedPckgs()
    {
        Hashtable   usedPckgs = new Hashtable();
        StringArray imports;
        String      pckg;
        String      type;
        String      curImport;
        int         pos;

        _importedPackages.clear();
        // we first determine all packages used within the type
        for (StringIterator it = _types.getDirectImports().getIterator(); it.hasNext();)
        {
            type = it.getNext();
            pos  = type.lastIndexOf('.');
            if (pos < 0)
            {
                pckg = "";
            }
            else
            {
                pckg = type.substring(0, pos);
            }
            imports = (StringArray)usedPckgs.get(pckg);
            if (imports == null)
            {
                imports = new StringArray();
                usedPckgs.put(pckg, imports);
            }
            imports.add(type);
        }
        for (StringIterator it = _types.getOnDemandImports().getIterator(); it.hasNext();)
        {
            pckg    = it.getNext();
            imports = (StringArray)usedPckgs.get(pckg);
            if (imports == null)
            {
                imports = new StringArray();
                usedPckgs.put(pckg, imports);
            }
            imports.add(pckg+".*");
        }

        // now we order them according to the original imports
        for (ImportIterator it = _unit.getImportDeclarations().getIterator(); it.hasNext();)
        {
            curImport = it.getNext().getImportedPackageOrType();
            pos       = -1;
            while ((pos = curImport.indexOf('.', pos+1)) >= 0)
            {
                pckg = curImport.substring(0, pos);
                if (usedPckgs.containsKey(pckg))
                {
                    _importedPackages.add(pckg, usedPckgs.get(pckg));
                    usedPckgs.remove(pckg);
                }
            }
            if (usedPckgs.containsKey(curImport))
            {
                _importedPackages.add(curImport, usedPckgs.get(curImport));
                usedPckgs.remove(curImport);
            }
        }
        pos = _importedPackages.getCount();

        // left are those packages that weren't in the original imports
        for (Enumeration en = usedPckgs.keys(); en.hasMoreElements();)
        {
            pckg = (String)en.nextElement();
            _importedPackages.add(pckg, usedPckgs.get(pckg));
        }
        _importedPackages.sort(pos, _importedPackages.getCount()-1);

        // finally we sort the types within the imported packages
        for (ObjectIterator it = _importedPackages.getObjects(); it.hasNext();)
        {
            ((StringArray)it.getNext()).sort();
        }
    }
}
