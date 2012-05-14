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

import jast.ast.Project;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.QualifiedTypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationIterator;


public class UsageAnalyzer
{
    private Project _project;

    public UsageAnalyzer(Project prj)
    {
        _project = prj;
    }

    public TypeDeclarationArray getTypeDeclarationsUsing(ConstructorDeclaration constructor)
    {
        TypeDeclarationArray result = new QualifiedTypeDeclarationArrayImpl();

        if (constructor == null)
        {
            return result;
        }

        for (PackageIterator pkgIter = _project.getPackages().getIterator(); pkgIter.hasNext(); )
        {
            for (TypeDeclarationIterator typeIter = pkgIter.getNext().getTypes().getIterator(); typeIter.hasNext(); )
            {
                TypeDeclaration curType = typeIter.getNext();

                if (ensureUsages(curType).isUsed(constructor))
                {
                    result.add(curType);
                }
            }
        }

        return result;
    }

    public TypeDeclarationArray getTypeDeclarationsUsing(FieldDeclaration field)
    {
        TypeDeclarationArray result = new QualifiedTypeDeclarationArrayImpl();

        if ((field == null) || (_project == null))
        {
            return result;
        }

        for (PackageIterator pkgIter = _project.getPackages().getIterator(); pkgIter.hasNext(); )
        {
            for (TypeDeclarationIterator typeIter = pkgIter.getNext().getTypes().getIterator(); typeIter.hasNext(); )
            {
                TypeDeclaration curType = typeIter.getNext();

                if (ensureUsages(curType).isUsed(field))
                {
                    result.add(curType);
                }
            }
        }

        return result;
    }

    public TypeDeclarationArray getTypeDeclarationsUsing(MethodDeclaration method)
    {
        TypeDeclarationArray result = new QualifiedTypeDeclarationArrayImpl();

        if (method == null)
        {
            return result;
        }

        for (PackageIterator pkgIter = _project.getPackages().getIterator(); pkgIter.hasNext(); )
        {
            for (TypeDeclarationIterator typeIter = pkgIter.getNext().getTypes().getIterator(); typeIter.hasNext(); )
            {
                TypeDeclaration curType = typeIter.getNext();

                if (ensureUsages(curType).isUsed(method))
                {
                    result.add(curType);
                }
            }
        }

        return result;
    }

    public boolean isNotRemoteUsed(FieldDeclaration field)
    {
        TypeDeclaration fieldOwner = (TypeDeclaration) field.getContainer();

        for (TypeDeclarationIterator iter = getTypeDeclarationsUsing(field).getIterator(); iter.hasNext(); )
        {
            TypeDeclaration curType = iter.getNext();
            if (!curType.equals(fieldOwner) &&
                !InheritanceAnalyzer.areRelated(fieldOwner, curType))
            {
                return false;
            }
        }

        return true;
    }

    public boolean isNotRemoteUsed(MethodDeclaration meth)
    {
        TypeDeclaration methOwner = (TypeDeclaration) meth.getContainer();

        for (TypeDeclarationIterator iter = getTypeDeclarationsUsing(meth).getIterator(); iter.hasNext(); )
        {
            TypeDeclaration curType = iter.getNext();

            if (!curType.equals(methOwner) &&
                !InheritanceAnalyzer.areRelated(methOwner, curType))
            {
                return false;
            }
        }

        return true;
    }

    public boolean isUsed(ConstructorDeclaration constr)
    {
        return !getTypeDeclarationsUsing(constr).isEmpty();
    }

    public boolean isUsed(FieldDeclaration field)
    {
        return !getTypeDeclarationsUsing(field).isEmpty();
    }

    public boolean isUsed(MethodDeclaration method)
    {
        return !getTypeDeclarationsUsing(method).isEmpty();
    }

    public boolean isUsed(TypeDeclaration typeDecl)
    {
        if (typeDecl == null)
        {
            return false;
        }

        for (PackageIterator pkgIter = _project.getPackages().getIterator(); pkgIter.hasNext(); )
        {
            for (TypeDeclarationIterator typeIter = pkgIter.getNext().getTypes().getIterator(); typeIter.hasNext(); )
            {
                TypeDeclaration curType = typeIter.getNext();

                if (ensureUsages(curType).isUsed(typeDecl))
                {
                    return true;
                }
            }
        }

        return false;
    }


    private TypeUsages ensureUsages(TypeDeclaration typeDecl)
    {
        return (TypeUsages)AnalysisHelper.ensureUsagesPresent(typeDecl);
    }
}
