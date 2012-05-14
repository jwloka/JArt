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

import jart.netbeans.RestructuringRunner;
import jart.netbeans.gui.TypeNameDialog;
import jart.restructuring.variablelevel.IntroduceParameterObject;
import jart.smelling.HasLongParameterList;
import jart.smelling.SmellOccurrence;
import jast.ast.Node;
import jast.ast.nodes.InvocableDeclaration;

public class ParameterObjectMapping implements RefactoringMapping
{
    private SmellOccurrence          _occurrence;
    private IntroduceParameterObject _restructuring;

    public boolean perform()
    {
        TypeNameDialog dlg =
            new TypeNameDialog(
                "Introduce Parameter Object",
                "Enter the name of the new parameter type",
                "Enter the name of the new parameter",
                false);

        dlg.setVisible(true);
        if (!dlg.wasOK())
            {
            return false;
        }

        RestructuringRunner runner = new RestructuringRunner();
        Node[] nodes = { _occurrence.getNode()};
        Object[] args = { dlg.getType(), dlg.getName()};

        return runner.perform(_restructuring, nodes, args);
    }

    private boolean isApplicableTo(SmellOccurrence occurrence)
    {
        return (occurrence.getSmell() == HasLongParameterList.class)
            && (occurrence.getNode() instanceof InvocableDeclaration);
    }

    public RefactoringMapping getInitializedInstance(SmellOccurrence occurrence)
    {
        if (!isApplicableTo(occurrence))
            {
            return null;
        }

        ParameterObjectMapping result = new ParameterObjectMapping();

        result._occurrence = occurrence;
        result._restructuring = new IntroduceParameterObject();
        return result;
    }

}

