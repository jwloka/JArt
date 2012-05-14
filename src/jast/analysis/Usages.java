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
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;



public abstract class Usages
{
    public final static String PROPERTY_LABEL = "Usages";

    private int _remoteUsageCount    = 0;
    private int _inheritedUsageCount = 0;
    private int _localUsageCount     = 0;

    private TypeDeclaration _localType;
    private TypeUsageArray  _usedTypes;

    public Usages(TypeDeclaration localType) throws IllegalArgumentException
    {
        if (localType == null)
            {
            throw new IllegalArgumentException("Null value");
        }

        _localType = localType;
        _usedTypes = new TypeUsageArray();
    }

    public void addUsage(TypeDeclaration accessedType)
    {
        if (accessedType == null)
            {
            return;
        }

        if (!_usedTypes.contains(accessedType))
            {
            _usedTypes.add(accessedType);
        }

        incUsageCount(accessedType);
    }

    public int getInheritedUsageCount()
    {
        return _inheritedUsageCount;
    }

    public TypeDeclaration getLocalType()
    {
        return _localType;
    }

    public int getLocalUsageCount()
    {
        return _localUsageCount;
    }

    public int getRemoteUsageCount()
    {
        return _remoteUsageCount;
    }

    public TypeUsage getTypeUsage(TypeDeclaration typeDecl)
    {
        return _usedTypes.get(typeDecl);
    }

    public int getUsageCount()
    {
        return _localUsageCount + _inheritedUsageCount + _remoteUsageCount;
    }

    public abstract int getUsedFeatureCount();

    public abstract int getUsedInheritedFeatureCount();

    public abstract int getUsedLocalFeatureCount();

    public abstract int getUsedRemoteFeatureCount();

    public int getUsedRemoteTypeCount()
    {
        int result = 0;

        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            if (new InheritanceAnalyzer(getLocalType()).isRemote(iter.getNext().getType()))
                {
                result++;
            }
        }

        return result;
    }

    public TypeUsageArray getUsedTypes()
    {
        return _usedTypes;
    }

    private void incUsageCount(TypeDeclaration accessedType)
    {
        if (new InheritanceAnalyzer(getLocalType()).isLocal(accessedType))
            {
            _localUsageCount++;
        }
        else if (new InheritanceAnalyzer(getLocalType()).isParent(accessedType))
            {
            _inheritedUsageCount++;
        }
        else
            {
            _remoteUsageCount++;
        }

        getTypeUsage(accessedType).incUsageCount();
    }

    /** Returns true if # usagesOf(constructor) == 0 */
    public boolean isUsed(ConstructorDeclaration constructor)
    {
        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            if (iter.getNext().isUsed(constructor))
                {
                return true;
            }
        }

        return false;
    }

    /** Returns true if # usagesOf(field) == 0 */
    public boolean isUsed(FieldDeclaration field)
    {
        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            if (iter.getNext().isUsed(field))
                {
                return true;
            }
        }

        return false;
    }

    /** Returns true if # usagesOf(method) == 0 */
    public boolean isUsed(MethodDeclaration method)
    {
        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            if (iter.getNext().isUsed(method))
                {
                return true;
            }
        }

        return false;
    }

    /** Returns true if # usagesOf(type) == 0 */
    public boolean isUsed(TypeDeclaration type)
    {
        return getUsedTypes().contains(type);
    }

    public int getUsageCountOfMaxUsedRemoteType()
    {
        int result = 0;

        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            TypeDeclaration curType = iter.getNext().getType();

            if (new InheritanceAnalyzer(getLocalType()).isRemote(curType)
                && (getTypeUsage(curType).getUsageCount() > result))
                {
                result = getTypeUsage(curType).getUsageCount();
            }
        }

        return result;
    }

    public int getUsedFeatureCountOfMaxUsedRemoteType()
    {
        int result = 0;

        for (TypeUsageIterator iter = getUsedTypes().getIterator(); iter.hasNext();)
            {
            TypeDeclaration curType = iter.getNext().getType();

            if (new InheritanceAnalyzer(getLocalType()).isRemote(curType)
                && (getTypeUsage(curType).getUsedFeatureCount() > result))
                {
                result = getTypeUsage(curType).getUsedFeatureCount();
            }
        }

        return result;
    }
}
