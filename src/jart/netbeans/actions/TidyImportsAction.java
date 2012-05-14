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
package jart.netbeans.actions;

import jart.netbeans.Integration;
import jart.netbeans.NetBeansIOHelper;
import jart.netbeans.RestructuringRunner;
import jart.restructuring.tidyimports.TidyImports;
import jast.Global;
import jast.ast.Node;
import jast.ast.nodes.CompilationUnit;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class TidyImportsAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { DataObject.class };
        return result;
    }

    protected int mode()
    {
        return MODE_EXACTLY_ONE;
    }

    protected void performAction(org.openide.nodes.Node[] nodes)
    {
        DataObject dataObj;

        for (int idx = 0; idx < nodes.length; idx++)
            {
            dataObj = (DataObject) nodes[idx].getCookie(DataObject.class);
            if (performTidying(dataObj))

                {
                return;
            }
        }
    }

    private boolean performTidying(DataObject dataObj)
    {
        if (dataObj == null)
            {
            return false;
        }

        FileObject fileObj = dataObj.getPrimaryFile();

        if (fileObj == null)
            {
            Global.getOutput().addNote("No file object", null);
            return false;
        }

        Integration.getInstance().updateProject();

        String unitName = fileObj.getPackageName('.');
        CompilationUnit unit = Integration.getInstance().getUnit(unitName);

        if (unit == null)
            {
            // not parsed yet
            Global.getOutput().addNote(
                "Could not retrieve compilation unit for " + unitName,
                null);
            return false;
        }
        org.openide.TopManager.getDefault().setStatusText(
            "Tidying imports of " + unitName);

        RestructuringRunner runner = new RestructuringRunner();
        NetBeansIOHelper helper = new NetBeansIOHelper();

        if (runner
            .perform(new TidyImports(), new Node[] { unit }, new Object[] { new Integer(5)})
            && helper.updateUnit(unitName))
            {
            Integration.getInstance().openInEditor(unitName, 0);
            return true;
        }
        else
            {
            return false;
        }
    }

    public String getName()
    {
        return NbBundle.getMessage(TidyImportsAction.class, "LBL_TidyImportsAction");
    }

    protected String iconResource()
    {
        return "RestructuringIcon.gif";
    }

    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }
}