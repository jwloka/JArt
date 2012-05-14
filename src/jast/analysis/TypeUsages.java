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
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.InitializerIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;



public class TypeUsages extends Usages
{

    public TypeUsages(TypeDeclaration localType)
    {
        super(localType);
    }

    private FeatureUsagesIterator getFeatureUsages()
    {
        FeatureUsagesArray result = new FeatureUsagesArray();

        for (FieldIterator fieldIter = getLocalType().getFields().getIterator();
            fieldIter.hasNext();
            )
            {
            result.add(
                (FeatureUsages) fieldIter.getNext().getProperty(Usages.PROPERTY_LABEL));
        }
        for (MethodIterator methIter = getLocalType().getMethods().getIterator();
            methIter.hasNext();
            )
            {
            result.add(
                (FeatureUsages) methIter.getNext().getProperty(Usages.PROPERTY_LABEL));
        }

        if (getLocalType() instanceof ClassDeclaration)
            {
            ClassDeclaration clsDecl = (ClassDeclaration) getLocalType();

            for (InitializerIterator initIter = clsDecl.getInitializers().getIterator();
                initIter.hasNext();
                )
                {
                result.add(
                    (FeatureUsages) initIter.getNext().getProperty(Usages.PROPERTY_LABEL));
            }

            for (ConstructorIterator constIter = clsDecl.getConstructors().getIterator();
                constIter.hasNext();
                )
                {
                result.add(
                    (FeatureUsages) constIter.getNext().getProperty(Usages.PROPERTY_LABEL));
            }
        }

        return result.getIterator();
    }

    public int getInheritedUsageCount()
    {
        int result = super.getInheritedUsageCount();

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getInheritedUsageCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getInheritedUsageCount();
        }

        return result;
    }

    private void getInnerTypes(
        TypeDeclaration typeDecl,
        TypeDeclarationArray result)
    {
        if ((typeDecl == null)
            || (typeDecl.getInnerTypes() == null)
            || (result == null))
            {
            return;
        }

        for (TypeDeclarationIterator iter = typeDecl.getInnerTypes().getIterator();
            iter.hasNext();
            )
            {
            TypeDeclaration curInner = iter.getNext();
            result.add(curInner);
            getInnerTypes(curInner, result);
        }
    }

    private TypeUsagesIterator getInnerTypeUsages()
    {
        TypeUsagesArray result = new TypeUsagesArray();
        TypeDeclarationArray innerTypes = new TypeDeclarationArrayImpl();

        getInnerTypes(getLocalType(), innerTypes);

        for (TypeDeclarationIterator iter = innerTypes.getIterator(); iter.hasNext();)
            {
            result.add((TypeUsages) iter.getNext().getProperty(Usages.PROPERTY_LABEL));
        }

        return result.getIterator();
    }

    public int getLocalUsageCount()
    {
        int result = super.getLocalUsageCount();

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getLocalUsageCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getLocalUsageCount();
        }

        return result;
    }

    public int getRemoteUsageCount()
    {
        int result = super.getRemoteUsageCount();

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getRemoteUsageCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getRemoteUsageCount();
        }

        return result;
    }

    public TypeUsage getTypeUsage(TypeDeclaration typeDecl)
    {
        if (super.getUsedTypes().contains(typeDecl))
            {
            return super.getUsedTypes().get(typeDecl);
        }

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            FeatureUsages curFeat = featIter.getNext();
            if (curFeat.getTypeUsage(typeDecl) != null)
                {
                return curFeat.getTypeUsage(typeDecl);
            }
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            TypeUsages curInner = innerIter.getNext();
            if (curInner.getTypeUsage(typeDecl) != null)
                {
                return curInner.getTypeUsage(typeDecl);
            }
        }

        return null;
    }

    public int getUsageCount()
    {
        return getLocalUsageCount() + getInheritedUsageCount() + getRemoteUsageCount();
    }

    public int getUsedFeatureCount()
    {
        return getUsedLocalFeatureCount()
            + getUsedInheritedFeatureCount()
            + getUsedRemoteFeatureCount();
    }

    public int getUsedInheritedFeatureCount()
    {
        int result = 0;

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getUsedInheritedFeatureCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getUsedInheritedFeatureCount();
        }

        return result;

    }

    public int getUsedLocalFeatureCount()
    {
        int result = 0;

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getUsedLocalFeatureCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getUsedLocalFeatureCount();
        }

        return result;

    }

    public int getUsedRemoteFeatureCount()
    {
        int result = 0;

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result += featIter.getNext().getUsedRemoteFeatureCount();
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result += innerIter.getNext().getUsedRemoteFeatureCount();
        }

        return result;

    }

    public TypeUsageArray getUsedTypes()
    {
        TypeUsageArray result = super.getUsedTypes();

        for (FeatureUsagesIterator featIter = getFeatureUsages(); featIter.hasNext();)
            {
            result.add(featIter.getNext().getUsedTypes());
        }

        for (TypeUsagesIterator innerIter = getInnerTypeUsages(); innerIter.hasNext();)
            {
            result.add(innerIter.getNext().getUsedTypes());
        }

        return result;
    }
}
