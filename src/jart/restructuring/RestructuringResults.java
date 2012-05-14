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
package jart.restructuring;

import jast.BasicMessageContainer;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.Package;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.CompilationUnitArrayImpl;
import jast.ast.nodes.collections.PackageArray;
import jast.ast.nodes.collections.PackageArrayImpl;

public class RestructuringResults extends BasicMessageContainer
{
    // The actual (physical) change of units/packages is not
    // performed by the framework but instead by the application
    // Therefore, we keep track of all changes (e.g. additions/removals/changes)
    // which enables the application to perform them physically
    // (i.e. via creating directories or writing units)
    private CompilationUnitArray _newUnits     = new CompilationUnitArrayImpl();
    private CompilationUnitArray _changedUnits = new CompilationUnitArrayImpl();
    private CompilationUnitArray _removedUnits = new CompilationUnitArrayImpl();
    private PackageArray         _newPckgs     = new PackageArrayImpl();
    private PackageArray         _removedPckgs = new PackageArrayImpl();

    public RestructuringResults()
    {
        super();
    }

    public void reset()
    {
        resetMessages();
        _newUnits.clear();
        _changedUnits.clear();
        _removedUnits.clear();
        _newPckgs.clear();
        _removedPckgs.clear();
    }

    public CompilationUnitArray getChangedUnits()
    {
        return _changedUnits;
    }

    public PackageArray getNewPackages()
    {
        return _newPckgs;
    }

    public CompilationUnitArray getNewUnits()
    {
        return _newUnits;
    }

    public PackageArray getRemovedPackages()
    {
        return _removedPckgs;
    }

    public CompilationUnitArray getRemovedUnits()
    {
        return _removedUnits;
    }

    public void markPackageAsNew(Package pckg)
    {
        _newPckgs.add(pckg);
    }

    public void markPackageAsRemoved(Package pckg)
    {
        _removedPckgs.add(pckg);
    }

    public void markUnitAsChanged(CompilationUnit unit)
    {
        _changedUnits.add(unit);
    }

    public void markUnitAsNew(CompilationUnit unit)
    {
        _newUnits.add(unit);
    }

    public void markUnitAsRemoved(CompilationUnit unit)
    {
        _removedUnits.add(unit);
    }


    public boolean hasMessage()
    {
        return hasFatalErrors() ||
               hasErrors() ||
               hasWarnings() ||
               hasNotes();
    }
}
