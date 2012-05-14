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

import jast.Global;
import jast.ParseException;
import jast.ast.ParsePositionFinder;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.prettyprinter.PrettyPrinter;
import jast.prettyprinter.SimpleStyleWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.cookies.SourceCookie;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

public class PrettyPrintHelper
{

    public static void prettyPrint(String unitName)
    {
        if ((unitName == null) || (unitName.length() == 0))
            {
            return;
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
            return;
        }

        SourceCookie srcCookie = (SourceCookie) dataObj.getCookie(SourceCookie.class);

        if ((srcCookie == null) || (srcCookie.getSource() == null))

            {
            // No source file
            return;
        }

        Project project = new Project("dummy");
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
                return;
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
        }
        System.gc();
        if (pos >= 0)
            {
            Integration.openInEditor(name, pos);
        }
        org.openide.TopManager.getDefault().setStatusText(
            "Pretty-printing of " + name + " done");
    }
}

