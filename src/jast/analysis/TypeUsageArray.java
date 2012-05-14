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
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodIterator;

import java.util.Enumeration;
import java.util.Hashtable;



public class TypeUsageArray
{
    private Hashtable _data;

    public TypeUsageArray()
    {
        _data = new Hashtable();
    }

    public void add(TypeUsageArray typeUsages)
    {
        if (typeUsages == null)
            {
            return;
        }

        for (TypeUsageIterator iter = typeUsages.getIterator(); iter.hasNext();)
            {
            TypeUsage curUsage = iter.getNext();

            if (contains(curUsage.getType()))
                {
                merge(get(curUsage.getType()), curUsage);
            }
            _data.put(curUsage.getType(), curUsage);
        }
    }

    public void add(TypeDeclaration typeDecl)
    {
        if ((typeDecl != null) && !contains(typeDecl))
            {
            _data.put(typeDecl, new TypeUsage(typeDecl));
        }
    }

    public boolean contains(TypeDeclaration typeDecl)
    {
        return _data.containsKey((Object) typeDecl);
    }

    public TypeUsage get(TypeDeclaration typeDecl)
    {
        return (TypeUsage) _data.get(typeDecl);
    }

    public int getCount()
    {
        return _data.size();
    }

    public TypeUsageIterator getIterator()
    {
        return new TypeUsageIterator()
        {
            private Enumeration data = (_data == null ? null : _data.elements());

            public boolean hasNext()
            {
                return (data == null ? false : data.hasMoreElements());
            }
            public TypeUsage getNext()
            {
                return (data == null ? null : (TypeUsage) data.nextElement());
            }
        };
    }

    /** Merges srcUsage to dstUsage */
    private static void merge(TypeUsage srcUsage, TypeUsage dstUsage)
    {
        for (FieldIterator fieldIter = srcUsage.getUsedFields().getIterator();
            fieldIter.hasNext();
            )
            {
            dstUsage.addUsage(fieldIter.getNext());
        }
        for (ConstructorIterator constIter =
            srcUsage.getUsedConstructors().getIterator();
            constIter.hasNext();
            )
            {
            dstUsage.addUsage(constIter.getNext());
        }
        for (MethodIterator methIter = srcUsage.getUsedMethods().getIterator();
            methIter.hasNext();
            )
            {
            dstUsage.addUsage(methIter.getNext());
        }

    }
}
