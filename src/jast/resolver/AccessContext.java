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
package jast.resolver;
import jast.ast.nodes.Package;

public class AccessContext
{
    private Package _pckg;
    private boolean _canBeInnerType;
    private boolean _canBeProtected;
    private boolean _canBePrivate;

    public AccessContext(Package pckg,
                         boolean canBeInnerType,
                         boolean canBeProtected,
                         boolean canBePrivate)
    {
        _pckg           = pckg;
        _canBeInnerType = canBeInnerType;
        _canBeProtected = canBeProtected;
        _canBePrivate   = canBePrivate;
    }

    public Package getPackage()
    {
        return _pckg;
    }

    public boolean canBeInnerType()
    {
        return _canBeInnerType;
    }

    public boolean canBeProtected()
    {
        return _canBeProtected;
    }

    private boolean canBePrivate()
    {
        return _canBePrivate;
    }
}
