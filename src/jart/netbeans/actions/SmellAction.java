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

import jart.netbeans.gui.ListDialog;
import jart.netbeans.refactoring.RefactoringHelper;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public class SmellAction extends CallableSystemAction
{

    public String getName()
    {
        return NbBundle.getMessage(SmellAction.class, "LBL_SmellAction");
    }

    protected String iconResource()
    {
        return "SmellActionIcon.gif";
    }

    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    public void performAction()
    {
        ListDialog dlg = new ListDialog("Please select the smells to check", true);

        dlg.allowMultiSelect(true);
        dlg.addItem("HasAwfullyLongName");
        dlg.addItem("HasDataClumps");
        dlg.addItem("HasFeatureEnvy");
        dlg.addItem("HasLongParameterList");
        dlg.addItem("HasSharedCollection");
        dlg.addItem("IsDataClass");
        dlg.addItem("IsSpeculativeGeneral");
        dlg.addItem("IsUnnecessaryOpen");
        dlg.setSize(400, 400);
        dlg.selectAll();
        dlg.show();
        if (dlg.wasOK())
            {
            RefactoringHelper helper = new RefactoringHelper();

            helper.smell(dlg.getSelections());
            helper.showResults();
        }
    }

}