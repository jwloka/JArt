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

import jart.netbeans.gui.ParseProgress;
import jast.Global;
import jast.ParseException;
import jast.analysis.AnalysisHelper;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;

import org.openide.src.ClassElement;

public class NetBeansProject
{
    // The JAst project
    private Project       _project;
    // Contains all types (compilation units) present in the project
    private TreeMap       _items    = new TreeMap();
    // A dialog used to show the progress of parsing/resolving
    private ParseProgress _progress = new ParseProgress("Updating project");

    public NetBeansProject(String name)
    {
        _project = new Project(name);
    }

    public Project getJastProject()
    {
        return _project;
    }

    // Ensures that:
    // * all given types are added to the project and parsed if necessary
    // * only those types that are both in the project and open in the editor
    //   are fully parsed (all other are reduced)
    // * all types are updated if necessary (changed date in the file)
    public void synchronizeWith(Object[] names)
    {
        Hashtable newTypes = new Hashtable();
        CompilationUnit unit;
        String name;

        // We first put all types in the model into the hashtable
        for (int idx = 0; idx < names.length; idx++)
            {
            name = names[idx].toString();
            newTypes.put(name, name);
        }
        //Global.getOutput().addNote("Starting to sync the projects", null);

        // Now we sync the hashtable with the project
        for (Iterator it = _items.keySet().iterator(); it.hasNext();)
            {
            name = (String) it.next();
            // if in hashtable then it stays as it is except
            // if it open in the editor as well (update step below)
            // otherwise it is downsized or simply removed if yet unparsed
            if (!newTypes.containsKey(name))
                {
                unit = (CompilationUnit) _items.get(name);
                if (unit == null)
                    {
                    _items.remove(name);
                    //Global.getOutput().addNote("Removed type "+name+" from project", null);
                }
                else
                    {
                    if (unit.isComplete())
                        {
                        Global.getParser().uncomplete(unit);
                        //Global.getOutput().addNote("Downsized type "+name, null);
                    }
                }
            }
            // we remove the key from the hashtable as it is not new
            newTypes.remove(name);
        }
        // Finally we add the new types to the project; however
        // the parsing is done by the update method
        for (Enumeration en = newTypes.elements(); en.hasMoreElements();)
            {
            name = (String) en.nextElement();
            _items.put(name, null);
            //Global.getOutput().addNote("Scheduled addition of type "+name+" to project", null);
        }
        update();
    }

    // Updates the list, iow
    // * only those types in the project that are open in the editor
    //   are fully parsed (all other are reduced)
    // * all types that are deleted are removed from the project
    // * all types are reparsed if necessary (changed date in the file)
    // * finally the project is completely re-resolved
    public void update()
    {
        String name;
        ClassElement classElem;
        CompilationUnit unit;
        boolean shouldBeComplete;

        // for both parsing and resolving
        _progress.setAmount(_items.size());
        _progress.show();

        Global.getResolver().setProject(_project);

        //Global.getOutput().addNote("Starting to update the project", null);
        for (Iterator it = _items.keySet().iterator();
            it.hasNext();
            _progress.incValue())
            {
            name = (String) it.next();
            unit = (CompilationUnit) _items.get(name);
            classElem = ClassElement.forName(name);

            // a type doesn't exist anymore if there is no class element for it
            if (classElem == null)
                {
                _progress.setMessage("Removing " + name);
                // file deleted, so remove it from the project
                _project.delCompilationUnit(name);
                it.remove();
                //Global.getOutput().addNote("Removed type "+name+" from project", null);
            }
            else
                {
                shouldBeComplete =
                    NetBeansParser.isFromSource(classElem) && Integration.isOpenInEditor(classElem);
                try
                    {
                    if (unit == null)
                        {
                        // yet unparsed
                        _progress.setMessage("Parsing " + name);
                        unit = parseUnit(name);
                        _items.put(name, unit);
                        Global.getOutput().addNote("Parsed", unit);
                    }
                    else if (unit.isProjectUnit())
                        {
                        String unitVersion = "";
                        String fileVersion = Integration.getVersion(classElem);

                        if (unit.hasProperty(Integration.PROPERTY_LASTMODIFIED))
                            {
                            unitVersion = (String) unit.getProperty(Integration.PROPERTY_LASTMODIFIED);
                        }
                        if (!unitVersion.equals(fileVersion))
                            {
                            unit = parseUnit(name);
                            _items.put(name, unit);
                            Global.getOutput().addNote(
                                "Updated (old version = "
                                    + unitVersion
                                    + ", new version = "
                                    + fileVersion
                                    + ")",
                                unit);
                        }
                    }

                    // We resolve the project files now as we want to attach usage
                    // structures to them prior to uncompleting them
                    if (!unit.isResolved())
                        {
                        _progress.setMessage("Resolving " + name);

                        Global.getParser().setParsingInterface(true);
                        Global.getParser().setProjectUnitStatus(false);
                        Global.getResolver().resolve(unit);
                        Global.getOutput().addNote("Resolved", unit);

                        // Next we attach the usage structure
                        AnalysisHelper.ensureUsagesPresent(unit);
                        //unit.accept(new UsageVisitor());
                        Global.getOutput().addNote("Added usage structure", unit);
                    }

                    if (unit.isComplete() && !shouldBeComplete)
                        {
                        _progress.setMessage("Uncompleting " + name);
                        Global.getParser().uncomplete(unit);
                        Global.getOutput().addNote("Uncompleted", unit);
                    }
                }
                catch (ParseException ex)
                    {
                    Global.getOutput().addError("While parsing type " + name + ": " + ex, null);
                }
            }
        }
        _progress.closeDialog();
        _progress.setValue(0);
        _progress.setMessage("");
    }

    private CompilationUnit parseUnit(String name) throws ParseException
    {
        Global.getParser().setParsingInterface(false);
        Global.getParser().setProjectUnitStatus(true);
        Global.getParser().parseType(_project, name);
        Global.getParser().setProjectUnitStatus(false);
        return _project.getCompilationUnits().get(name);
    }

    public Iterator getTypeNames()
    {
        return _items.keySet().iterator();
    }
}

