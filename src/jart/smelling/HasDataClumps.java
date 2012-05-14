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
package jart.smelling;
import jart.analysis.AnalysisFunction;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.collections.FieldIterator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


// =???= Wrong Description
public class HasDataClumps implements AnalysisFunction
{
    private static final int MIN_CLUMP_SIZE  = 4;
    private static final int MIN_CLUMP_USAGE = 4;

    private ClassDeclaration _clsDecl;
    private Hashtable          _fieldUsages;

    public HasDataClumps()
    {
        _fieldUsages = new Hashtable();
    }

    public HasDataClumps(ClassDeclaration clsDecl)
    {
        super();
        _clsDecl = clsDecl;
        _fieldUsages = new Hashtable();
    }

    public boolean apply()
    {
        if (!areEnoughFieldsUsedSufficientlyTogether())
            {
            return false;
        }

        return true;
    }

    private boolean areEnoughFieldsUsedSufficientlyTogether()
    {
        Vector validUsages = new Vector();

        countLocalFieldUsages();

        if (_fieldUsages.size() < MIN_CLUMP_SIZE)
            {
            return false;
        }

        for (Enumeration en = _fieldUsages.keys(); en.hasMoreElements();)
            {
            Integer cur = (Integer) en.nextElement();
            if (cur.intValue() >= MIN_CLUMP_USAGE)
                {
                validUsages.add(cur);
            }
        }

        return validUsages.size() >= MIN_CLUMP_SIZE;
    }

    public boolean check(Project project, Node node)
    {
        _clsDecl = (ClassDeclaration) node;
        _fieldUsages = new Hashtable();
        return apply();
    }

    private void countLocalFieldUsages()
    {
        for (FieldIterator iter = _clsDecl.getFields().getIterator(); iter.hasNext();)
            {
            FieldDeclaration cur = iter.getNext();
            _fieldUsages.put(new Integer(getLocalUsageCount(cur)), cur.getName());
        }
    }

    private int getLocalUsageCount(FieldDeclaration field)
    {
        // THIS WONT WORK PROPERLY

        //Usages       usages        = (Usages)_clsDecl.getProperty(Usages.PROPERTY_LABEL);
        //TypeUsage localUsages = usages.getTypeUsage(_clsDecl.getQualifiedName());

        //if (localUsages == null)
        //{
        //return 0;
        //}

        //return localUsages.getFieldUsageCount(_clsDecl.getQualifiedName(), field.getName());
        return 0;
    }

    public Class[] isApplicableTo()
    {
        Class[] result = { ClassDeclaration.class };

        return result;
    }
}
