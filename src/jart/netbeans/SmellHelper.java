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

import jart.analysis.AnalysisFunction;
import jart.smelling.MultipleSmellLocator;
import jart.smelling.SmellOccurrence;
import jart.smelling.SmellOccurrenceIterator;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.nodes.CompilationUnit;

import org.openide.TopManager;

public class SmellHelper
{

    public static void smell(Object[] smellNames)
    {
        MultipleSmellLocator locator = new MultipleSmellLocator();

        for (int idx = 0; idx < smellNames.length; idx++)
            {
            try
                {
                locator.addAnalysisFunction(
                    (AnalysisFunction) Class
                        .forName("jart.smelling." + smellNames[idx].toString())
                        .newInstance());
            }
            catch (Exception ex)
                {
            }
        }
        locator.applyTo(Integration.getInstance().getProject().getJastProject());

        SmellOccurrence occurrence;
        CompilationUnit unit;
        String msg;

        for (SmellOccurrenceIterator it = locator.getOccurrences(); it.hasNext();)
            {
            occurrence = it.getNext();
            unit = ASTHelper.getCompilationUnitOf((ContainedNode) occurrence.getNode());

            msg =
                unit.getTopLevelQualifiedName().replace('.', '/')
                    + ".java "
                    + "["
                    + occurrence.getPosition().getLine()
                    + ":"
                    + occurrence.getPosition().getColumn()
                    + "] "
                    + occurrence.getSmellName();

            TopManager.getDefault().getStdOut().println(msg);
        }
    }
}

