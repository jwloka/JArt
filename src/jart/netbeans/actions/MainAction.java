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
import jart.netbeans.gui.ListDialog;
import jart.netbeans.refactoring.RefactoringHelper;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class MainAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { EditorCookie.class, DataObject.class };
        return result;
    }

    protected int mode()
    {
        return MODE_EXACTLY_ONE;
    }

    public String getName()
    {
        return NbBundle.getMessage(MainAction.class, "LBL_MainAction");
    }

    protected String iconResource()
    {
        return "MainActionIcon.gif";
    }

    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;
    }

    protected void performAction(Node[] nodes)
    {
        ListDialog dlg = new ListDialog("Please select the action to perform", true);
        int idx;

        dlg.addItem("Project files");
        dlg.addItem("Smelling");
        dlg.addItem("Editor Integration");
        dlg.addItem("Test");
        dlg.show();

        if (!dlg.wasOK())
            {
            return;
        }
        switch (dlg.getSelectedIndex())
            {
            case 0 :
                showProjectFiles();
                break;
            case 1 :
                showSmellDialog();
                break;
            case 2 :
                for (idx = 0; idx < nodes.length; idx++)
                    {
                    if (handleEditorCookie((EditorCookie) nodes[idx]
                        .getCookie(EditorCookie.class)))

                        {
                        return;
                    }
                }
                break;
            case 3 :
                test(nodes);
                break;
        }
    }

    private void test(Node[] nodes)
    {
        jart.netbeans.gui.ProjectTreeView view =
            new jart.netbeans.gui.ProjectTreeView("Project packages & types", true);

        view.setProject(Integration.getInstance().getProject().getJastProject());
        view.setVisible(true);
        /*
                java.util.Iterator types = Integration.getInstance().getProject().getTypeNames();
                String             name;
                CompilationUnit    unit;
                Usages             usages;

                TopManager.getDefault().getStdOut().println("Usages:");
                if (!types.hasNext())
                {
                    TopManager.getDefault().getStdOut().println("  [No types in project]");
                }
                while (types.hasNext())
                {
                    name = (String)types.next();
                    unit = Integration.getInstance().getUnit(name);

                    for (TypeDeclarationIterator typeIt = unit.getTypeDeclarations().getIterator(); typeIt.hasNext();)
                    {
                        usages = (Usages)typeIt.getNext().getProperty(Usages.PROPERTY_LABEL);
                        TopManager.getDefault().getStdOut().println("  Usages of type " + name);
                        if (usages == null)
                        {
                            TopManager.getDefault().getStdOut().println("  [None]");
                            continue;
                        }
                        for (TypeUsageIterator it = usages.getTypeUsages(); it.hasNext();)
                        {
                            TypeUsage usage = it.getNext();

                            TopManager.getDefault().getStdOut().println("    Type " + usage.getTypeName());
                            if (usage.getUsedFields() != null)
                            {
                                for (FieldIterator fieldIt = usage.getUsedFields().getIterator(); fieldIt.hasNext();)
                                {
                                    TopManager.getDefault().getStdOut().println("      Field       " + fieldIt.getNext().getName());
                                }
                            }
                            if (usage.getUsedConstructors() != null)
                            {
                                for (ConstructorIterator constructorIt = usage.getUsedConstructors().getIterator(); constructorIt.hasNext();)
                                {
                                    TopManager.getDefault().getStdOut().println("      Constructor " + constructorIt.getNext().getSignature());
                                }
                            }
                            if (usage.getUsedMethods() != null)
                            {
                                for (MethodIterator methodIt = usage.getUsedMethods().getIterator(); methodIt.hasNext();)
                                {
                                    TopManager.getDefault().getStdOut().println("      Method      " + methodIt.getNext().getSignature());
                                }
                            }
                        }
                    }
                }
         */
    }

    private boolean handleEditorCookie(EditorCookie cookie)
    {
        if (cookie == null)
            {
            return false;
        }

        JEditorPane[] panes = cookie.getOpenedPanes();

        if ((panes == null) || (panes.length == 0))
            {
            return false;
        }

        JEditorPane pane = null;

        for (int idx = 0; idx < panes.length; idx++)
            {
            if (panes[idx].hasFocus())
                {
                pane = panes[idx];
                break;
            }
        }
        if (pane == null)
            {
            return false;
        }

        int pos = pane.getCaretPosition();
        String txt = pane.getText().substring(pos, pos + 4);

        org.openide.TopManager.getDefault().setStatusText(
            "MainAction : caret is at " + pos + " (text = '" + txt + "')");
        return true;
    }

    private void showProjectFiles()
    {
        ListDialog dlg = new ListDialog("Please select the project files", true);

        dlg.allowMultiSelect(true);
        Integration.getInstance().createProjectList(dlg);
        dlg.setSize(400, 400);
        dlg.show();
        if (dlg.wasOK())
            {
            Integration.getInstance().updateProject(dlg.getSelections());
        }
    }

    private void showSmellDialog()
    {
        ListDialog dlg = new ListDialog("Please select the smells to check", true);

        dlg.allowMultiSelect(true);
        dlg.addItem("HasAwfullyLongName");
        dlg.addItem("HasDataClumps");
        dlg.addItem("HasFeatureEnvy");
        dlg.addItem("HasLongParameterList");
        dlg.addItem("HasSharedCollection");
        dlg.addItem("IsCollectionClass");
        dlg.addItem("IsDataClass");
        dlg.addItem("IsSpeculativeGeneral");
        dlg.addItem("IsTestClass");
        dlg.addItem("IsUnnecessaryOpen");
        dlg.addItem("IsUtilityClass");
        dlg.addItem("IsUtilityMethod");
        dlg.setSize(400, 400);
        dlg.selectAll();
        dlg.show();
        if (dlg.wasOK())
            {
            new RefactoringHelper().smell(dlg.getSelections());
        }
    }
}