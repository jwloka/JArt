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
import jart.netbeans.gui.TypeNameDialog;
import jart.restructuring.fieldlevel.CreateField;
import jast.Global;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;

import org.openide.loaders.DataObject;
import org.openide.src.ClassElement;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public class CreateFieldAction extends CookieAction
{

    protected Class[] cookieClasses()
    {
        Class[] result = { DataObject.class, ClassElement.class };
        return result;
    }

    protected int mode()
    {
        return MODE_EXACTLY_ONE;
    }

    protected void performAction(org.openide.nodes.Node[] nodes)
    {
        DataObject obj;
        ClassElement elem;

        for (int idx = 0; idx < nodes.length; idx++)
            {
            elem = (ClassElement) nodes[idx].getCookie(ClassElement.class);
            if (createField(elem))

                {
                return;
            }
            obj = (DataObject) nodes[idx].getCookie(DataObject.class);
            if (obj != null)

                {
                elem = ClassElement.forName(obj.getPrimaryFile().getPackageName('.'));
                if (createField(elem))
                    {
                    return;
                }
            }
        }
    }

    private boolean createField(ClassElement elem)
    {
        if (elem == null)
            {
            return false;
        }

        TypeDeclaration typeDecl = OpenAPIJastMapper.getTypeDeclarationFor(elem);

        if (typeDecl == null)
            {
            Global.getOutput().addError(
                "Cannot find type declaration of type " + elem.getName().getFullName(),
                null);
            return false;
        }

        TypeNameDialog dlg = new TypeNameDialog("Create New Field", true);

        dlg.setVisible(true);
        if (!dlg.wasOK())
            {
            return false;
        }

        Integration.getInstance().updateProject();

        CreateField restructuring = new CreateField();
        RestructuringRunner runner = new RestructuringRunner();
        String name = dlg.getName();
        Type type = Global.getFactory().createType(dlg.getType(), dlg.getDimensions());

        if (runner
            .perform(restructuring, new Node[] { typeDecl }, new Object[] { type, name }))
            {
            Integration.getInstance().updateProject();
            Global.getOutput().addNote(
                "Created new field " + name,
                restructuring.getCreatedField());
            Integration.openInEditor(
                ASTHelper.getCompilationUnitOf(typeDecl).getTopLevelQualifiedName(),
                typeDecl.getStartPosition().getAbsolute());
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
            "LBL_CreateFieldAction");
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