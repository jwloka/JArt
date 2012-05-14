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
import jast.Global;
import jast.ParseException;
import jast.ast.ASTHelper;
import jast.ast.ParsePositionFinder;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.Package;
import jast.helpers.StringArray;
import jast.prettyprinter.PrettyPrinter;
import jast.prettyprinter.SimpleStyleWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JEditorPane;

import org.openide.TopManager;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.SourceCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;

public class NetBeansIOHelper
{

    public boolean createPackage(String packageName)
    {
        if ((packageName == null) || (packageName.length() == 0))
            {
            return false;
        }
        if (TopManager.getDefault().getRepository().find(packageName, null, null)
            != null)
            {
            return true;
        }
        return ensurePackage(packageName) != null;
    }

    private FileObject ensurePackage(String pckgName)
    {
        Repository repository = TopManager.getDefault().getRepository();
        StringArray pckgs = StringArray.fromString(pckgName, ".");
        FileObject folder = null;
        String name;
        int idx;

        for (idx = pckgs.getCount(); idx > 0; idx--)
            {
            name = pckgs.asString(".", idx);
            folder = repository.find(name, null, null);
            if (folder != null)
                {
                break;
            }
        }
        // we possibly have to create sub folders
        for (int num = idx; num < pckgs.getCount(); num++)
            {
            folder = createSubFolder(folder, pckgs.get(num));
            if (folder == null)
                {
                break;
            }
        }
        if (folder == null)
            {
            Global.getOutput().addFatalError(
                "Cannot create folder for package " + pckgName,
                null);
        }
        return folder;
    }

    private FileObject createSubFolder(FileObject parentFolder, String name)
    {
        FileObject subFolder = null;

        try
            {
            if (parentFolder == null)
                {
                ListDialog dlg = new ListDialog("Please choose the filesystem", true);
                Hashtable sysNames = new Hashtable();
                FileSystem fileSys = null;

                for (Enumeration en =
                    TopManager.getDefault().getRepository().getFileSystems();
                    en.hasMoreElements();
                    )
                    {
                    fileSys = (FileSystem) en.nextElement();
                    if (!fileSys.isReadOnly()
                        && !fileSys.isHidden()
                        && !fileSys.isDefault()
                        && fileSys.isValid())
                        {
                        sysNames.put(fileSys.getDisplayName(), fileSys);
                        dlg.addItem(fileSys.getDisplayName());
                    }
                }
                dlg.show();
                if (!dlg.wasOK())
                    {
                    return null;
                }
                fileSys = (FileSystem) sysNames.get(dlg.getSelection());
                if (fileSys != null)
                    {
                    subFolder = fileSys.getRoot().createFolder(name);
                }
            }
            else
                {
                subFolder = parentFolder.createFolder(name);
            }
        }
        catch (Exception ex)
            {
            Global.getOutput().addFatalError(
                "Cannot create sub folder "
                    + name
                    + " of folder "
                    + parentFolder.getPackageName(java.io.File.separatorChar)
                    + " because of "
                    + ex,
                null);
        }
        return subFolder;
    }

    public boolean deletePackage(String packageName)
    {
        if ((packageName == null) || (packageName.length() == 0))
            {
            return false;
        }

        Package pckg =
            Integration.getInstance().getProject().getJastProject().getPackage(packageName);

        if (pckg == null)
            {
            Global.getOutput().addError(
                "Cannot delete folder for nonexisting package " + packageName,
                null);
            return false;
        }

        Repository repository = TopManager.getDefault().getRepository();
        FileObject folder = repository.find(packageName, null, null);

        if (folder == null)
            {
            Global.getOutput().addError(
                "The package " + packageName + " does not have a folder",
                null);
            return false;
        }

        if (folder.isReadOnly() || !folder.isValid())
            {
            Global.getOutput().addWarning(
                "NetBeans prevents the removal of the folder for the package " + packageName,
                null);
            return true;
        }
        if (folder.getChildren().length > 0)
            {
            Global.getOutput().addWarning(
                "The folder for the package " + packageName + " is not empty",
                null);
            return true;
        }

        try
            {
            FileLock fileLock = folder.lock();

            folder.delete(fileLock);
            fileLock.releaseLock();
            return true;
        }
        catch (Exception ex)
            {
            Global.getOutput().addFatalError(
                "Cannot delete the folder for the package " + packageName,
                null);
            return false;
        }
    }

    public boolean createUnit(String unitName)
    {
        if ((unitName == null) || (unitName.length() == 0))
            {
            return false;
        }

        CompilationUnit unit = Integration.getInstance().getUnit(unitName);

        if (unit == null)
            {
            // no unit of that name within the AST
            Global.getOutput().addError(
                "Cannot create file for nonexisting unit " + unitName,
                null);
            return false;
        }

        DataObject dataObj = Integration.getDataObjectForName(unitName);

        if (dataObj != null)
            {
            // already existing
            Global.getOutput().addWarning(
                "File for unit " + unitName + " already exists",
                null);
            return updateUnit(unitName);
        }

        FileObject pckgFolder = ensurePackage(unit.getPackage().getQualifiedName());

        if (pckgFolder == null)
            {
            Global.getOutput().addFatalError(
                "Cannot create file for unit " + unitName,
                null);
            return false;
        }
        try
            {
            FileObject file = pckgFolder.createData(unit.getTopLevelName(), "java");
            FileLock fileLock = file.lock();
            StringWriter writer = new StringWriter();
            PrettyPrinter printer = new PrettyPrinter(new SimpleStyleWriter(writer));

            printer.visitCompilationUnit(unit);

            PrintWriter output = new PrintWriter(file.getOutputStream(fileLock));

            output.print(writer.toString());
            output.close();

            file.refresh(true);
            fileLock.releaseLock();
            return true;
        }
        catch (Exception ex)
            {
            Global.getOutput().addFatalError(
                "Cannot create file for unit " + unitName,
                null);
            return false;
        }
    }

    public boolean updateUnit(String unitName)
    {
        if ((unitName == null) || (unitName.length() == 0))
            {
            return false;
        }

        CompilationUnit unit = Integration.getInstance().getUnit(unitName);

        if (unit != null)
            {
            // we ignore non-complete units and simply reparse them
            // outside of the project
            if (!unit.isComplete() || !unit.canBeModified())
                {
                unit = null;
            }
        }

        DataObject dataObj = Integration.getDataObjectForName(unitName);

        if (dataObj == null)
            {
            return false;
        }

        SourceCookie srcCookie = (SourceCookie) dataObj.getCookie(SourceCookie.class);

        if ((srcCookie == null) || (srcCookie.getSource() == null))

            {
            // No source file
            return false;
        }

        Project project =
            unit == null ? new Project("dummy") : ASTHelper.getProjectOf(unit);
        FileObject fileObj = dataObj.getPrimaryFile();
        String fileName = fileObj.getName();
        String fileEx = fileObj.getExt();
        String name = fileObj.getPackageName('.');
        EditorCookie editorCookie =
            (EditorCookie) dataObj.getCookie(EditorCookie.class);
        int pos = -1;

        // We first check whether it is open in the editor
        // In that case we close (otherwise some unexpected results could happen)
        // and reopen it after printing

        if (editorCookie != null)

            {
            if ((editorCookie.getOpenedPanes() != null)
                && (editorCookie.getOpenedPanes().length > 0))
                {
                JEditorPane pane = Integration.getVisiblePaneOf(editorCookie);

                // we determine the new position later on
                pos = (pane == null ? 0 : pane.getCaretPosition());
            }
            // we cannot prettyprint it when the user disagrees to close the file
            if (!editorCookie.close())
                {
                return false;
            }
            if (editorCookie.isModified())
                {
                // we force reparse (albeit not in the project) if
                // the file has been changed in the editor
                unit = null;
            }
        }
        org.openide.TopManager.getDefault().setStatusText("Pretty-printing " + name);
        try
            {
            FileLock fileLock = fileObj.lock();

            if (unit == null)
                {
                try
                    {
                    Global.getParser().setParsingInterface(false);
                    Global.getParser().parseType(project, name);
                    unit = (CompilationUnit) project.getCompilationUnits().get(name);
                }
                catch (ParseException ex)
                    {
                    Global.getOutput().addFatalError(
                        "Could not parse file " + name + ":\n" + ex,
                        null);
                    unit = null;
                }
            }

            if (unit == null)
                {
                Global.getOutput().addError("Could not parse " + name, null);
            }
            else if (!unit.canBeModified())
                {
                Global.getOutput().addError(
                    "Compilation unit of " + name + " cannot be modified",
                    null);
            }
            else
                {
                // node contains the node where the cursor will be set to
                jast.ast.Node node =
                    (pos < 0 ? null : new ParsePositionFinder().getNodeAt(unit, pos));
                StringWriter writer = new StringWriter();
                PrettyPrinter printer = new PrettyPrinter(new SimpleStyleWriter(writer));

                printer.visitCompilationUnit(unit);
                if (node != null)
                    {
                    pos = node.getStartPosition().getAbsolute();
                }

                PrintWriter output = new PrintWriter(fileObj.getOutputStream(fileLock));

                output.print(writer.toString());
                output.close();

                fileObj.refresh(true);
            }
            fileLock.releaseLock();
        }
        catch (IOException lockEx)
            {
            Global.getOutput().addError(
                "Could not obtain a lock for the source file:\n" + lockEx,
                null);
            return false;
        }
        System.gc();
        if (pos >= 0)
            {
            Integration.openInEditor(name, pos);
        }
        org.openide.TopManager.getDefault().setStatusText(
            "Pretty-printing of " + name + " done");
        return true;
    }

    public boolean deleteUnit(String unitName)
    {
        if ((unitName == null) || (unitName.length() == 0))
            {
            return false;
        }

        CompilationUnit unit = Integration.getInstance().getUnit(unitName);

        if (unit == null)
            {
            // no unit of that name within the AST
            Global.getOutput().addError(
                "Cannot delete file for nonexisting unit " + unitName,
                null);
            return false;
        }

        DataObject dataObj = Integration.getDataObjectForName(unitName);

        if (dataObj == null)
            {
            Global.getOutput().addError(
                "File for unit " + unitName + " does not exist",
                null);
            return false;
        }

        EditorCookie editorCookie =
            (EditorCookie) dataObj.getCookie(EditorCookie.class);

        if ((editorCookie != null) && !editorCookie.close())

            {
            Global.getOutput().addError(
                "Cannot remove file of unit " + unitName + " when it is open",
                null);
            return false;
        }
        try
            {
            FileObject file = dataObj.getPrimaryFile();
            FileLock fileLock = file.lock();

            file.delete(fileLock);
            fileLock.releaseLock();
            return true;
        }
        catch (Exception ex)
            {
            Global.getOutput().addFatalError(
                "Cannot delete file for unit " + unitName,
                null);
            return false;
        }
    }
}

