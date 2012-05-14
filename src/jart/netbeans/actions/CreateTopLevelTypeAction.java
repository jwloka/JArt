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
import jart.netbeans.RestructuringRunner;
import jart.netbeans.gui.CreateTypeDialog;
import jart.restructuring.typelevel.CreateTopLevelType;
import jast.Global;
import jast.ast.Node;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public class CreateTopLevelTypeAction extends CallableSystemAction
{

    public String getName()
    {
        return NbBundle.getMessage(
            CreateTopLevelTypeAction.class,
            "LBL_CreateTopLevelTypeAction");
    }

    protected String iconResource()
    {
        return "RestructuringIcon.gif";
    }

    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    public void performAction()
    {
        CreateTypeDialog dlg = new CreateTypeDialog("");

        dlg.setVisible(true);
        if (!dlg.wasOK())
            {
            return;
        }

        Integration.getInstance().updateProject();

        CreateTopLevelType restructuring = new CreateTopLevelType();
        RestructuringRunner runner = new RestructuringRunner();
        String unitName = dlg.getText();

        if (runner
            .perform(
                restructuring,
                new Node[] { Integration.getInstance().getProject().getJastProject()},
                new Object[] { new Boolean(dlg.shallBeClass()), unitName }))
            {
            Global.getOutput().addNote(
                "Created new top-level type " + unitName,
                restructuring.getCreatedType());
            Integration.openInEditor(unitName, 0);
        }
    }
}