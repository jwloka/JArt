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
import jart.netbeans.gui.YesNoDialog;
import jart.restructuring.fieldlevel.EncapsulateField;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.ParsePositionFinder;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.src.FieldElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class EncapsulateFieldAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { FieldElement.class, EditorCookie.class };
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
        FieldElement elem;

        for (int idx = 0; idx < nodes.length; idx++)
            {
            elem = (FieldElement) nodes[idx].getCookie(FieldElement.class);
            if (handleFieldElement(elem))

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

    private boolean handleFieldElement(FieldElement elem)
    {
        if (elem == null)
            {
            return false;
        }

        FieldDeclaration fieldDecl = OpenAPIJastMapper.getFieldDeclarationFor(elem);

        if (fieldDecl == null)
            {
            Global.getOutput().addError("Cannot find field " + elem.getName(), null);
            return false;
        }
        return encapsulateField(fieldDecl);
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

        if (!(node instanceof FieldDeclaration) && !(node instanceof FieldAccess))
            {
            return false;
        }
        return encapsulateField(node);
    }

    private boolean encapsulateField(Node node)
    {
        if (node == null)
            {
            return false;
        }

        FieldDeclaration fieldDecl = null;

        if (node instanceof FieldDeclaration)
            {
            fieldDecl = (FieldDeclaration) node;
        }
        else if (node instanceof FieldAccess)
            {
            fieldDecl = ((FieldAccess) node).getFieldDeclaration();
        }
        if (fieldDecl == null)
            {
            Global.getOutput().addError("No field given", null);
            return false;
        }

        YesNoDialog dlg =
            new YesNoDialog(
                "Self-Encapsulate Field",
                "Also encapsulate all accesses within the type that defines the field");

        dlg.setVisible(true);

        EncapsulateField restructuring = new EncapsulateField();
        RestructuringRunner runner = new RestructuringRunner();

        Global.getOutput().addNote(
            "Encapsulating field " + fieldDecl.getName() + " (self = " + dlg.wasYes() + ")",
            null);
        if (runner
            .perform(
                restructuring,
                new Node[] { fieldDecl },
                new Object[] { new Boolean(dlg.wasYes())}))
            {
            String unitName =
                ASTHelper.getCompilationUnitOf(fieldDecl).getTopLevelQualifiedName();
            int pos = fieldDecl.getStartPosition().getAbsolute();

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
            "LBL_EncapsulateFieldAction");
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