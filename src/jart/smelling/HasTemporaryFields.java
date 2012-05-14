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

import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;

public class HasTemporaryFields
{
    private final static int LOW_USAGE = 1;

    private ClassDeclaration _clsDecl;

    public HasTemporaryFields(ClassDeclaration clsDecl)
        throws IllegalArgumentException
    {
        super();

        if (clsDecl == null)
            {
            throw new IllegalArgumentException("Null value");
        }
    }

    public boolean apply()
    {
        return false;
    }

    private int getLocalUsageCount(FieldDeclaration field)
    {
        //TypeUsages usages = (TypeUsages)_clsDecl.getProperty(Usages.PROPERTY_LABEL);
        //usages1.
        //UsageAnalyzer usages = new UsageAnalyzer(_project);
        //TypeDeclarationArray usingTypes = usages.getTypeDeclarationsUsing(field);

        return 0;
    }

    private int getRemoteUsageCount(FieldDeclaration field)
    {
        return 0;
    }
}
