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
package jast.export.xml;

import jast.ast.nodes.Package;

import java.util.Enumeration;
import java.util.Hashtable;

public class PackageDescriptor
{
    private Package   _pckg;
    private String    _qualifiedName;
    private Hashtable _subPackages = new Hashtable();

    public PackageDescriptor(Package pckg)
    {
        _pckg = pckg;
    }

    public PackageDescriptor(String qualifiedName)
    {
        _qualifiedName = qualifiedName;
    }

    public void addSubPackage(Package pckg)
    {
        PackageDescriptor desc = new PackageDescriptor(pckg);

        _subPackages.put(desc.getShortName(), desc);
    }

    public void addSubPackage(String qualifiedName)
    {
        PackageDescriptor desc = new PackageDescriptor(qualifiedName);

        _subPackages.put(desc.getShortName(), desc);
    }

    public Package getPackage()
    {
        return _pckg;
    }

    public String getQualifiedName()
    {
        return (_pckg != null ? _pckg.getQualifiedName() : _qualifiedName);
    }

    public String getShortName()
    {
        String qualifiedName = getQualifiedName();

        return qualifiedName.substring(qualifiedName.lastIndexOf(".")+1);
    }

    public PackageDescriptor getSubPackage(String shortName)
    {
        return (PackageDescriptor)_subPackages.get(shortName);
    }

    public Enumeration getSubPackages()
    {
        return _subPackages.elements();
    }

    public boolean hasSubPackage(String shortName)
    {
        return _subPackages.containsKey(shortName);
    }

    public boolean isPresent()
    {
        return _pckg != null;
    }

    public String toString()
    {
        return "PackageDescriptor: "+getQualifiedName();
    }
}
