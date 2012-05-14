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

import jart.netbeans.gui.ListDialog;
import jart.netbeans.gui.Output;
import jast.Global;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;

import java.util.Iterator;

import javax.swing.JEditorPane;

import org.openide.TopManager;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;
import org.openide.src.ClassElement;

// Handles interactions in between NetBeans and JArt
public class Integration
{
    public static final String PROPERTY_LASTMODIFIED = "lastModified";

    private static Integration _singleton = null;
    private NetBeansProject _project = new NetBeansProject("integration");

    private Integration()
    {
        // Initializion of NetBeans specific parts
        Global.setParser(new NetBeansParser());
        Global.setOutput(new Output());
        Global.getOutput().addNote("J/Art initialized.", null);
    }

    public static Integration getInstance()
    {
        if (_singleton == null)
            {
            _singleton = new Integration();
        }
        return _singleton;
    }

    public static DataObject getDataObjectForName(String qualifiedName)
    {
        //Global.getOutput().addNote("Trying to acquire the data object for type "+qualifiedName, null);
        ClassElement classElem = ClassElement.forName(qualifiedName);

        if (classElem == null)
            {
            return null;
        }
        else
            {
            DataObject result =
                (DataObject) classElem.getSource().getCookie(DataObject.class);

            //Global.getOutput().addNote("Type "+qualifiedName+(result != null ? " has" : " hasn't")+" a data object", null);
            return result;
        }
    }

    public static boolean isOpenInEditor(ClassElement classElem)
    {
        DataObject dataObj =
            (DataObject) classElem.getSource().getCookie(DataObject.class);

        if (dataObj == null)

            {
            return false;
        }

        EditorCookie editorCookie =
            (EditorCookie) dataObj.getCookie(EditorCookie.class);

        return (editorCookie != null)
            && (editorCookie.getOpenedPanes() != null)
            && (editorCookie.getOpenedPanes().length > 0);
    }

    public static JEditorPane getVisiblePaneOf(EditorCookie cookie)
    {
        JEditorPane[] panes = cookie.getOpenedPanes();

        if ((panes != null) && (panes.length > 0))
            {
            for (int idx = 0; idx < panes.length; idx++)
                {
                if (panes[idx].isShowing())
                    {
                    return panes[idx];
                }
            }
        }
        return null;
    }

    public static void openInEditor(String unitName, int pos)
    {
        ClassElement classElem = ClassElement.forName(unitName);

        if (classElem == null)
            {
            return;
        }

        EditorCookie editorCookie =
            (EditorCookie) classElem.getCookie(EditorCookie.class);

        if (editorCookie == null)

            {
            return;
        }

        editorCookie.open();

        JEditorPane[] panes = editorCookie.getOpenedPanes();

        if ((panes == null) || (panes.length == 0))
            {
            return;
        }

        JEditorPane pane = panes[0];

        for (int idx = 1; idx < panes.length; idx++)
            {
            if (panes[idx].isShowing())
                {
                pane = panes[idx];
            }
        }
        if (!pane.isShowing())
            {
            pane.setVisible(true);
        }
        if (pos >= 0)
            {
            try
                {
                // we cannot use pane.setCaretPosition as this would perhaps create
                // a selection
                pane.getCaret().setDot(pos);
                pane.getCaret().setVisible(true);
            }
            catch (IllegalArgumentException ex)
                {
                Global.getOutput().addWarning(
                    "Could not position caret to " + pos + " in editor for type " + unitName,
                    null);
            }
        }
    }

    public static String getVersion(ClassElement classElem)
    {
        DataObject dataObj =
            (DataObject) classElem.getSource().getCookie(DataObject.class);

        if (dataObj == null)

            {
            return "";
        }

        FileObject fileObj = dataObj.getPrimaryFile();

        return (fileObj == null ? "" : fileObj.lastModified().toString());
    }

    public void createProjectList(ListDialog dlg)
    {
        dlg.allowMultiSelect(true);

        // Only the files of the current project can be selected
        // as these are the files that are parsed completely
        // (of all other only the interface is parsed)
        Node node = TopManager.getDefault().getPlaces().nodes().projectDesktop();
        DataFolder folder = (DataFolder) node.getCookie(DataFolder.class);
        DataObject[] files = folder.getChildren();
        DataShadow link;

        for (int idx = 0; idx < files.length; idx++)

            {
            link = (DataShadow) files[idx].getCookie(DataShadow.class);
            if (link != null)

                {
                addFileObject(link.getOriginal().getPrimaryFile(), dlg);
            }
        }
        // Now that we have all usable files in the list we select
        // all that are already in the project (if any)

        for (Iterator it = _project.getTypeNames(); it.hasNext();)
            {
            dlg.select((String) it.next());
        }
    }

    private void addFileObject(FileObject obj, ListDialog dlg)
    {
        if (obj == null)
            {
            return;
        }
        if (obj.isFolder())
            {
            FileObject[] children = obj.getChildren();
            int idx;

            if ((children != null) && (children.length > 0))
                {
                for (idx = 0; idx < children.length; idx++)
                    {
                    if (!children[idx].isFolder())
                        {
                        addFileObject(children[idx], dlg);
                    }
                }
                for (idx = 0; idx < children.length; idx++)
                    {
                    if (children[idx].isFolder())
                        {
                        addFileObject(children[idx], dlg);
                    }
                }
            }
        }
        else
            {
            if (!obj.isReadOnly() && "java".equalsIgnoreCase(obj.getExt()))
                {
                dlg.addItem(obj.getPackageName('.'));
            }
        }
    }

    public NetBeansProject getProject()
    {
        return _project;
    }

    public Project getJastProject()
    {
        return _project.getJastProject();
    }

    public void updateProject()
    {
        _project.update();
    }

    public void updateProject(Object[] unitNames)
    {
        _project.synchronizeWith(unitNames);
    }

    public CompilationUnit getUnit(String qualifiedName)
    {
        return _project.getJastProject().getCompilationUnits().get(qualifiedName);
    }

    public void finalize()
    {
        TopManager.getDefault().getStdOut().println("[J/Art] Finalizing.");

        _singleton = null;
        _project = null;
        Global.setFactory(null);
        Global.setOutput(null);
        Global.setParser(null);
        Global.setResolver(null);
    }
}