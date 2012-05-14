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
package jart.netbeans.refactoring;

import jart.analysis.AnalysisFunction;
import jart.netbeans.Integration;
import jart.netbeans.gui.RefactoringDialog;
import jart.smelling.MultipleSmellLocator;
import jart.smelling.SmellOccurrence;
import jart.smelling.SmellOccurrenceIterator;

import java.util.Hashtable;
import java.util.Vector;

public class RefactoringHelper
{
    private MultipleSmellLocator _locator  = new MultipleSmellLocator();
    private Hashtable            _mappings = new Hashtable();

    public void smell(Object[] smellNames)
    {
        for (int idx = 0; idx < smellNames.length; idx++)
            {
            try
                {
                _locator.addAnalysisFunction(
                    (AnalysisFunction) Class
                        .forName("jart.smelling." + smellNames[idx].toString())
                        .newInstance());
            }
            catch (Exception ex)
                {
            }
        }
        _locator.applyTo(Integration.getInstance().getProject().getJastProject());
        determinePossibleRefactorings();
    }

    private RefactoringMapping[] getKnownMappings()
    {
        RefactoringMapping[] result = { new ParameterObjectMapping()};

        return result;
    }

    private void determinePossibleRefactorings()
    {
        RefactoringMapping[] knownMappings = getKnownMappings();
        RefactoringMapping initMapping;
        SmellOccurrence occurrence;
        Vector mappingsFor;

        _mappings.clear();
        for (int idx = 0; idx < knownMappings.length; idx++)
            {
            for (SmellOccurrenceIterator it = getOccurrences(); it.hasNext();)
                {
                occurrence = it.getNext();
                initMapping = knownMappings[idx].getInitializedInstance(occurrence);
                if (initMapping != null)
                    {
                    mappingsFor = (Vector) _mappings.get(occurrence);
                    if (mappingsFor == null)
                        {
                        mappingsFor = new Vector();
                        _mappings.put(occurrence, mappingsFor);
                    }
                    mappingsFor.addElement(initMapping);
                }
            }
        }
    }

    public void showInEditor(SmellOccurrence occurrence)
    {
        String unitName = occurrence.getEnclosingUnit().getTopLevelQualifiedName();
        int pos =
            occurrence.getPosition() != null ? occurrence.getPosition().getAbsolute() : 0;

        Integration.openInEditor(unitName, pos);
    }

    public SmellOccurrenceIterator getOccurrences()
    {
        return _locator.getOccurrences();
    }

    public void showResults()
    {
        new RefactoringDialog(this).setVisible(true);
        ;
    }

    public Vector getMappingsFor(SmellOccurrence occurrence)
    {
        return (Vector) _mappings.get(occurrence);
    }
}

