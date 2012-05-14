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
package jast.ast.nodes;

import jast.SyntaxException;
import jast.ast.nodes.collections.ConstructorArray;
import jast.ast.nodes.collections.ConstructorArrayImpl;
import jast.ast.nodes.collections.InitializerArray;
import jast.ast.nodes.collections.InitializerArrayImpl;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.Visitor;

public class ClassDeclaration extends    TypeDeclaration
                              implements Modifiable, Feature, BlockStatement
{
    private Type             _baseClass;
    private boolean          _isLocal;
    private InitializerArray _initializers = new InitializerArrayImpl();
    private ConstructorArray _constructors = new ConstructorArrayImpl();

    public ClassDeclaration(Modifiers mods, String name, boolean isLocal)
    {
        super(mods, name);
        _isLocal   = isLocal;
        _initializers.setOwner(this);
        _constructors.setOwner(this);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitClassDeclaration(this);
    }

    public void addDeclaration(Feature decl) throws SyntaxException
    {
        if (decl instanceof Initializer)
        {
            _initializers.add((Initializer)decl);
        }
        else if (decl instanceof ConstructorDeclaration)
        {
            _constructors.add((ConstructorDeclaration)decl);
        }
        else
        {
            super.addDeclaration(decl);
        }
    }

    public Type getBaseClass()
    {
        return _baseClass;
    }

    public ConstructorArray getConstructors()
    {
        return _constructors;
    }

    public InitializerArray getInitializers()
    {
        return _initializers;
    }

    public boolean hasBaseClass()
    {
        return _baseClass != null;
    }

    // Qualifies inner classes whose definition occur within a block
    // context (field initializer, instance/static initializer, method,
    // constructor)
    public boolean isLocal()
    {
        return _isLocal;
    }

    public boolean isSubTypeOf(Type other)
    {
        if (other.isPrimitive() || other.isArray())
        {
            return false;
        }

        // We assume (as always) that the types are already
        // resolved, iow that their basenames are qualified
        String       otherName = other.getQualifiedName();
        TypeIterator it;

        // first we check whether we hava a direct base class
        if (_baseClass == null)
        {
            if ("java.lang.Object".equals(otherName))
            {
                return true;
            }
        }
        else
        {
            if (_baseClass.getQualifiedName().equals(otherName))
            {
                return true;
            }
        }

        // next the base interfaces
        for (it = getBaseInterfaces().getIterator(); it.hasNext();)
        {
            if (it.getNext().getQualifiedName().equals(otherName))
            {
                return true;
            }
        }
        // Now we must descend into the base class/interfaces
        if ((_baseClass != null) && _baseClass.isSubTypeOf(other))
        {
            return true;
        }
        for (it = getBaseInterfaces().getIterator(); it.hasNext();)
        {
            if (it.getNext().isSubTypeOf(other))
            {
                return true;
            }
        }
        return false;
    }

    public void setBaseClass(Type type) throws SyntaxException
    {
        if ((type == null) || type.isPrimitive() || (type.getDimensions() > 0))
        {
            throw new SyntaxException("Type " + type.getBaseName() + " is not a valid base class");
        }
        _baseClass = type;
    }
}
