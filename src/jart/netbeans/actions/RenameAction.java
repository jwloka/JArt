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
import jart.netbeans.gui.NameDialog;
import jart.restructuring.variablelevel.RenameLocalVariable;
import jast.Global;
import jast.ast.Node;
import jast.ast.ParsePositionFinder;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class RenameAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { EditorCookie.class };
        return result;
    }

    protected int mode()
    {
        return MODE_EXACTLY_ONE;
    }

    protected void performAction(org.openide.nodes.Node[] nodes)
    {
        EditorCookie editorCookie;
        DataObject dataObj;

        for (int idx = 0; idx < nodes.length; idx++)
            {
            editorCookie = (EditorCookie) nodes[idx].getCookie(EditorCookie.class);
            dataObj = (DataObject) nodes[idx].getCookie(DataObject.class);
            if (handleEditorCookie(editorCookie, dataObj))

                {
                return;
            }
        }
    }

    private boolean handleEditorCookie(EditorCookie cookie, DataObject dataObj)
    {
        if ((cookie == null) || (dataObj == null))
            {
            return false;
        }

        FileObject fileObj = dataObj.getPrimaryFile();

        if (fileObj == null)
            {
            Global.getOutput().addNote("No file object", null);
            return false;
        }

        JEditorPane pane = Integration.getVisiblePaneOf(cookie);

        if (pane == null)
            {
            Global.getOutput().addNote("No pane has focus", null);
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
            "Rename : caret is at " + pane.getCaretPosition());

        Node node = new ParsePositionFinder().getNodeAt(unit, pane.getCaretPosition());
        String oldName = "";

        if ((node instanceof LocalVariableDeclaration)
            || (node instanceof FormalParameter)
            || (node instanceof VariableAccess))
            {
            if (node instanceof VariableAccess)
                {
                oldName = ((VariableAccess) node).getVariableName();
            }
            else
                {
                oldName = ((VariableDeclaration) node).getName();
            }
            renameLocalVariable(node, oldName);
        }
        // finally we show the changed unit in the editor and set the caret accordingly
        int pos =
            node.getStartPosition() != null ? node.getStartPosition().getAbsolute() : -1;

        Integration.getInstance().openInEditor(unitName, pos);

        return true;
    }

    private void renameLocalVariable(Node node, String oldName)
    {
        NameDialog dlg =
            new NameDialog(
                "Rename variable",
                "Please enter the new name of the variable",
                oldName);

        dlg.setVisible(true);
        if (dlg.wasOK() && !oldName.equals(dlg.getText()))
            {
            new RestructuringRunner().perform(
                new RenameLocalVariable(),
                new Node[] { node },
                new Object[] { dlg.getText()});
        }
    }

    public String getName()
    {
        return NbBundle.getMessage(RenameAction.class, "LBL_RenameAction");
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