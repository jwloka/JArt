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
package jart.netbeans.gui;

import jart.smelling.SmellOccurrence;
import jast.ast.nodes.TypeDeclaration;

import javax.swing.JLabel;

// A label which contains a smell occurrence
public class SmellOccurrenceListItem extends JLabel
{
    private SmellOccurrence _occurrence;

    public SmellOccurrenceListItem(SmellOccurrence occurrence)
    {
        _occurrence = occurrence;

        TypeDeclaration typeDecl = occurrence.getEnclosingType();
        StringBuffer msg = new StringBuffer();

        msg.append(occurrence.getSmellName());
        msg.append(" in ");
        msg.append(typeDecl.getQualifiedName());
        if ((occurrence.getPosition().getLine() >= 0)
            && (occurrence.getPosition().getColumn() >= 0))
            {
            msg.append(" [");
            msg.append(occurrence.getPosition().getLine());
            msg.append(":");
            msg.append(occurrence.getPosition().getColumn());
            msg.append("]");
        }
        setText(msg.toString());
    }

    public SmellOccurrence getSmellOccurrence()
    {
        return _occurrence;
    }
}