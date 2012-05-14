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



/**
 * Represents the accessed features inside of another
 * feature. The number of remote, inherited, and
 * local accesses as well as the accessed features
 * are stored.
 */
public class FeatureUsages extends Usages
{
    private int _usedRemoteFeatureCount    = 0;
    private int _usedInheritedFeatureCount = 0;
    private int _usedLocalFeatureCount     = 0;

    public FeatureUsages(TypeDeclaration localType)
    {
        super(localType);
    }

    public void addUsage(
        TypeDeclaration accessedType,
        ConstructorDeclaration accessedConstructor)
    {
        addUsage(accessedType);

        if ((accessedConstructor != null)
            && !getTypeUsage(accessedType).isUsed(accessedConstructor))
            {
            incFeatureCount(accessedType);
            getUsedTypes().get(accessedType).addUsage(accessedConstructor);
        }
    }

    public void addUsage(
        TypeDeclaration accessedType,
        FieldDeclaration accessedField)
    {
        addUsage(accessedType);

        if ((accessedField != null)
            && !getTypeUsage(accessedType).isUsed(accessedField))
            {
            incFeatureCount(accessedType);
            getUsedTypes().get(accessedType).addUsage(accessedField);
        }
    }

    public void addUsage(
        TypeDeclaration accessedType,
        MethodDeclaration accessedMethod)
    {
        addUsage(accessedType);

        if ((accessedMethod != null)
            && !getTypeUsage(accessedType).isUsed(accessedMethod))
            {
            incFeatureCount(accessedType);
            getUsedTypes().get(accessedType).addUsage(accessedMethod);
        }
    }

    public int getUsedFeatureCount()
    {
        return _usedLocalFeatureCount
            + _usedInheritedFeatureCount
            + _usedRemoteFeatureCount;
    }

    public int getUsedInheritedFeatureCount()
    {
        return _usedInheritedFeatureCount;
    }

    public int getUsedLocalFeatureCount()
    {
        return _usedLocalFeatureCount;
    }

    public int getUsedRemoteFeatureCount()
    {
        return _usedRemoteFeatureCount;
    }

    private void incFeatureCount(TypeDeclaration accessedType)
    {
        if (new InheritanceAnalyzer(getLocalType()).isLocal(accessedType))
            {
            _usedLocalFeatureCount++;
        }
        else if (new InheritanceAnalyzer(getLocalType()).isParent(accessedType))
            {
            _usedInheritedFeatureCount++;
        }
        else
            {
            _usedRemoteFeatureCount++;
        }
    }
}
