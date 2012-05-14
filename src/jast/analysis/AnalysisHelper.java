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
package jast.analysis;
import jast.ast.ASTHelper;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.TypeDeclaration;

public class AnalysisHelper
{

    public static Usages ensureUsagesPresent(TypeDeclaration typeDecl)
    {
        ensureUsagesPresent(ASTHelper.getCompilationUnitOf(typeDecl));

        return (Usages)typeDecl.getProperty(Usages.PROPERTY_LABEL);
    }


    public static void ensureUsagesPresent(CompilationUnit unit)
    {
        if (!unit.getTypeDeclarations().isEmpty())
        {
            if (!unit.getTypeDeclarations().get(0).hasProperty(Usages.PROPERTY_LABEL))
            {
                unit.accept(new UsageVisitor());
            }
        }
    }
}
