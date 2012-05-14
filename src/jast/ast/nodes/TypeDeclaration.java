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
import jast.Global;
import jast.SyntaxException;
import jast.ast.ContainedNode;
import jast.ast.ContainerEvent;
import jast.ast.collections.AdditionEvent;
import jast.ast.collections.AssignmentEvent;
import jast.ast.nodes.collections.FieldArray;
import jast.ast.nodes.collections.FieldArrayImpl;
import jast.ast.nodes.collections.MethodArray;
import jast.ast.nodes.collections.MethodArrayImpl;
import jast.ast.nodes.collections.TypeArray;
import jast.ast.nodes.collections.TypeArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationArrayImpl;
import jast.ast.nodes.collections.TypeDeclarationIterator;

public abstract class TypeDeclaration extends    ContainedNode
                                      implements Modifiable
{
    private Package               _package;
    private TypeDeclaration       _enclosingType;
    private Modifiers             _mods;
    private String                _name;
    private TypeArray             _interfaces = new TypeArrayImpl();
    private TypeDeclarationArray  _innerTypes = new TypeDeclarationArrayImpl();
    private FieldArray            _fields     = new FieldArrayImpl();
    private MethodArray           _methods    = new MethodArrayImpl();

    public TypeDeclaration(Modifiers mods, String name)
    {
        _mods = mods;
        _name = name;
        _innerTypes.setOwner(this);
        _fields.setOwner(this);
        _methods.setOwner(this);
    }

    // Convenience method for adding features
    public void addDeclaration(Feature decl) throws SyntaxException
    {
        if (decl instanceof FieldDeclaration)
        {
            _fields.add((FieldDeclaration)decl);
        }
        else if (decl instanceof MethodDeclaration)
        {
            _methods.add((MethodDeclaration)decl);
        }
        else if (decl instanceof TypeDeclaration)
        {
            _innerTypes.add((TypeDeclaration)decl);
        }
        else
        {
            throw new SyntaxException("Only field/method/type declaration are allowed");
        }
    }

    public TypeArray getBaseInterfaces()
    {
        return _interfaces;
    }

    public Type getCreatedType()
    {
        return Global.getFactory().createType(this, 0);
    }

    public TypeDeclaration getEnclosingType()
    {
        return _enclosingType;
    }

    public FieldArray getFields()
    {
        return _fields;
    }

    public TypeDeclarationArray getInnerTypes()
    {
        return _innerTypes;
    }

    public MethodArray getMethods()
    {
        return _methods;
    }

    public Modifiers getModifiers()
    {
        return _mods;
    }

    public String getName()
    {
        return _name;
    }

    public Package getPackage()
    {
        return (_enclosingType != null ? _enclosingType.getPackage() : _package);
    }

    public String getQualifiedName()
    {
        String result = _name;

        if (isInner() || isNested())
        {
            if (!(_enclosingType instanceof AnonymousClassDeclaration))
            {
                result = _enclosingType.getQualifiedName() + "." + result;
            }
        }
        else if ((_package != null) && !_package.isDefault())
        {
            result = _package.getQualifiedName() + "." + result;
        }
        return result;
    }

    public boolean hasBaseInterfaces()
    {
        return !_interfaces.isEmpty();
    }

    // Checks whether the given type is an inner type of this type
    public boolean hasInnerType(TypeDeclaration other)
    {
        if (_innerTypes.contains(other))
        {
            return true;
        }
        for (TypeDeclarationIterator it = _innerTypes.getIterator(); it.hasNext();)
        {
            if (it.getNext().hasInnerType(other))
            {
                return true;
            }
        }
        return false;
    }

    // In contrast to the java language spec, inner is more strict here
    // it only means real inner classes, that is inner classes which are
    // not static (that would be nested classes) and not local/anonymous
    public boolean isInner()
    {
        return (_enclosingType != null) && !_mods.isStatic();
    }

    public boolean isNested()
    {
        return (_enclosingType != null) && _mods.isStatic();
    }

    // Checks whether this type is a real sub-type (not the same)
    // of the given type
    public abstract boolean isSubTypeOf(Type other);

    // Returns <code>true</code> if the type is not an inner or nested type.
    // Note that local/anonymous types are also top-level in this regard.
    public boolean isTopLevel()
    {
        return (_enclosingType == null);
    }

    public void notify(ContainerEvent event)
    {
        if ((event instanceof AdditionEvent) ||
            (event instanceof AssignmentEvent))
        {
            Feature newFeature = (Feature)event.getObject();

            if (newFeature instanceof TypeDeclaration)
            {
                ((TypeDeclaration)newFeature).setEnclosingType(this);
            }
        }
    }

    public void setEnclosingType(TypeDeclaration enclosingType)
    {
        _enclosingType = enclosingType;
    }

    public void setModifiers(Modifiers mods)
    {
        _mods = mods;
    }

    public void setPackage(Package pckg)
    {
        _package = pckg;
    }
}
