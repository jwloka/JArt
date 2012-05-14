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
package jart.netbeans;

import jart.netbeans.gui.WarningChoiceDialog;
import jart.restructuring.Restructuring;
import jart.restructuring.RestructuringResults;
import jast.ast.Node;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.ast.nodes.collections.PackageIterator;

public class RestructuringRunner
{

    public boolean perform(
        Restructuring restructuring,
        Node[] nodes,
        Object[] data)
    {
        if (restructuring == null)
            {
            return false;
        }

        WarningChoiceDialog dlg = new WarningChoiceDialog();
        RestructuringResults results = restructuring.getResultContainer();
        boolean goOn = restructuring.initialize(nodes, data);

        if (goOn && results.hasWarnings())
            {
            dlg.showMessages(results);
            goOn = dlg.wasOK();
        }
        if (!goOn)
            {
            return false;
        }

        goOn = restructuring.analyze();

        if (goOn && results.hasWarnings())
            {
            dlg.showMessages(results);
            goOn = dlg.wasOK();
        }
        if (!goOn)
            {
            return false;
        }

        goOn = restructuring.perform();

        if (goOn)
            {
            if (results.hasMessage())
                {
                dlg.showMessages(results);
            }

            boolean status = synchronize(results);

            results.reset();
            return status;
        }
        else
            {
            return false;
        }
    }

    private boolean synchronize(RestructuringResults results)
    {
        NetBeansIOHelper helper = new NetBeansIOHelper();
        boolean result = true;

        for (PackageIterator it = results.getNewPackages().getIterator();
            it.hasNext() && result;
            )
            {
            result = result && helper.createPackage(it.getNext().getQualifiedName());
        }
        for (CompilationUnitIterator it = results.getNewUnits().getIterator();
            it.hasNext() && result;
            )
            {
            result = result && helper.createUnit(it.getNext().getName());
        }
        for (CompilationUnitIterator it = results.getChangedUnits().getIterator();
            it.hasNext() && result;
            )
            {
            result = result && helper.updateUnit(it.getNext().getName());
        }
        for (CompilationUnitIterator it = results.getRemovedUnits().getIterator();
            it.hasNext() && result;
            )
            {
            result = result && helper.deleteUnit(it.getNext().getName());
        }
        for (PackageIterator it = results.getRemovedPackages().getIterator();
            it.hasNext() && result;
            )
            {
            result = result && helper.deletePackage(it.getNext().getQualifiedName());
        }
        return result;
    }
}

