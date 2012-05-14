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
import jart.netbeans.OpenAPIJastMapper;
import jart.netbeans.RestructuringRunner;
import jart.netbeans.gui.NameDialog;
import jart.restructuring.variablelevel.RemoveAssignmentsToParameters;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.ParsePositionFinder;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.VariableAccess;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.src.MethodElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class RemoveParamAssignmentsAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { MethodElement.class, EditorCookie.class };
        return result;
    }

    protected int mode()
    {
        return MODE_EXACTLY_ONE;
    }

    protected void performAction(org.openide.nodes.Node[] nodes)
    {
        Integration.getInstance().updateProject();

        EditorCookie cookie;
        MethodElement elem;

        for (int idx = 0; idx < nodes.length; idx++)
            {
            elem = (MethodElement) nodes[idx].getCookie(MethodElement.class);
            if (handleMethodElement(elem))

                {
                return;
            }
            cookie = (EditorCookie) nodes[idx].getCookie(EditorCookie.class);
            if (handleEditorCookie(cookie,
                (DataObject) nodes[idx].getCookie(DataObject.class)))

                {
                return;
            }
        }
    }

    private boolean handleMethodElement(MethodElement elem)
    {
        if (elem == null)
            {
            return false;
        }

        MethodDeclaration methodDecl = OpenAPIJastMapper.getMethodDeclarationFor(elem);

        if (methodDecl == null)
            {
            Global.getOutput().addError("Cannot find method " + elem.getName(), null);
            return false;
        }
        return removeAssignments(methodDecl);
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

        Node node = new ParsePositionFinder().getNodeAt(unit, pane.getCaretPosition());

        if (!(node instanceof MethodDeclaration)
            && !(node instanceof FormalParameterList)
            && !(node instanceof FormalParameter)
            && !(node instanceof VariableAccess))
            {
            return false;
        }
        if (node instanceof VariableAccess)
            {
            VariableAccess access = (VariableAccess) node;

            if ((access.getVariableDeclaration() == null)
                || !(access.getVariableDeclaration() instanceof FormalParameter))
                {
                return false;
            }
        }
        return removeAssignments(node);
    }

    private boolean removeAssignments(Node node)
    {
        if (node == null)
            {
            Global.getOutput().addError("No parameter(s) given", null);
            return false;
        }
        if (node instanceof MethodDeclaration)
            {
            node = ((MethodDeclaration) node).getParameterList();
            if (node == null)
                {
                return false;
            }
        }

        NameDialog dlg =
            new NameDialog(
                "Remove Assignments to Parameter(s)",
                "Enter the prefix for the new local variables",
                "var");

        dlg.setVisible(true);
        if (!dlg.wasOK())
            {
            return false;
        }

        RemoveAssignmentsToParameters restructuring =
            new RemoveAssignmentsToParameters();
        RestructuringRunner runner = new RestructuringRunner();

        if (runner
            .perform(restructuring, new Node[] { node }, new Object[] { dlg.getText()}))
            {
            String unitName =
                ASTHelper.getCompilationUnitOf((ContainedNode) node).getTopLevelQualifiedName();
            int pos = node.getStartPosition().getAbsolute();

            Integration.getInstance().updateProject();
            Integration.openInEditor(unitName, pos);
            return true;
        }
        else
            {
            return false;
        }
    }

    public String getName()
    {
        return NbBundle.getMessage(
            CreateTopLevelTypeAction.class,
            "LBL_RemoveParamAssignmentsAction");
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