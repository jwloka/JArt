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
import jast.ast.nodes.TypeDeclaration;
import jast.helpers.StringArray;

import java.util.Enumeration;
import java.util.Hashtable;

public class MethodUsage
{
    private TypeDeclaration _localType;
    private StringArray     _usedTypeNames;
    private Hashtable       _usedRemoteTypes;

    private int _usedLocalFreatureCount     = 0;
    private int _usedInheritedFreatureCount = 0;
    private int _usedRemoteFreatureCount    = 0;

    public MethodUsage(TypeDeclaration localType) throws IllegalArgumentException
    {
        super();

        if (localType == null)
        {
            throw new IllegalArgumentException("Null value");
        }

        _localType             = localType;
        _usedTypeNames      = new StringArray();
        _usedRemoteTypes = new Hashtable();
    }

    public void addUsage(TypeDeclaration usedType)
    {
        if (_localType.equals(usedType) ||
            _localType.equals(ASTHelper.getOutermostTypeDeclarationOf(usedType)))
        {
            _usedLocalFreatureCount++;
        }
        else if (InheritanceAnalyzer.areRelated(_localType, usedType))
        {
            _usedInheritedFreatureCount++;
        }
        else
        {
            _usedRemoteFreatureCount++;
            incRemoteFeatureCountOf(usedType);
        }

        if (!_usedTypeNames.contains(usedType.getQualifiedName()))
        {
            _usedTypeNames.add(usedType.getQualifiedName());
        }
    }

    public int getMaxUsedRemoteFeatureCount()
    {
        int result = 0;

        for (Enumeration en = _usedRemoteTypes.keys(); en.hasMoreElements(); )
        {
            int cur = getRemoteFeatureCountOf((TypeDeclaration)en.nextElement());

            if (cur >= result)
            {
                result = cur;
            }
        }

        return result;
    }

    private int getRemoteFeatureCountOf(TypeDeclaration remoteType)
    {
        if (!_usedRemoteTypes.containsKey(remoteType))
        {
            _usedRemoteTypes.put(
                remoteType,
                new Integer(0));

            return 0;
        }

        return ((Integer)_usedRemoteTypes.get(remoteType)).intValue();
    }

    public int getUsedFeatureCount()
    {
        return _usedLocalFreatureCount
                + _usedInheritedFreatureCount
                + _usedRemoteFreatureCount;
    }

    public int getUsedInheritedFeatureCount()
    {
        return _usedInheritedFreatureCount;
    }

    public int getUsedLocalFeatureCount()
    {
        return _usedLocalFreatureCount;
    }

    public int getUsedRemoteFeatureCount()
    {
        return _usedRemoteFreatureCount;
    }

    public int getUsedRemoteTypeCount()
    {
        return _usedRemoteTypes.keySet().size();
    }

    public StringArray getUsedTypes()
    {
        return _usedTypeNames;
    }

    private void incRemoteFeatureCountOf(TypeDeclaration remoteType)
    {
        int newValue = getRemoteFeatureCountOf(remoteType);
        newValue++;

        _usedRemoteTypes.put(remoteType, new Integer(newValue));
    }

    public boolean isUsed(String qualTypeName)
    {
        return _usedTypeNames.contains(qualTypeName);
    }
}
