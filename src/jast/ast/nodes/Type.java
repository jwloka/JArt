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

import jast.ast.Node;
import jast.ast.visitor.Visitor;

public class Type extends Node
{
    private boolean         _isPrimitive           = false;
    private int             _dimensions            = 0;
    private PrimitiveType   _basePrimitiveType     = null;
    private String          _baseReferenceType     = null;
    private TypeDeclaration _baseReferenceTypeDecl = null;

    // Creates the null type
    public Type()
    {
    }

    public Type(PrimitiveType baseType)
    {
        _isPrimitive       = true;
        _basePrimitiveType = baseType;
    }

    public Type(PrimitiveType baseType, int dimensions)
    {
        this(baseType);
        setDimensions(dimensions);
    }

    public Type(Type src)
    {
        _isPrimitive           = src._isPrimitive;
        _dimensions            = src._dimensions;
        _basePrimitiveType     = src._basePrimitiveType;
        _baseReferenceType     = src._baseReferenceType;
        _baseReferenceTypeDecl = src._baseReferenceTypeDecl;
    }

    public Type(Type src, int newDimensions)
    {
        this(src);
        setDimensions(newDimensions);
    }

    public Type(TypeDeclaration decl)
    {
        setReferenceBaseTypeDecl(decl);
    }

    public Type(TypeDeclaration decl, int dimensions)
    {
        this(decl);
        setDimensions(dimensions);
    }

    public Type(String baseType)
    {
        if ((baseType == null) || (baseType.length() == 0))
        {
            throw new RuntimeException("Illegal type name");
        }
        _baseReferenceType = baseType;
    }

    public Type(String baseType, int dimensions)
    {
        this(baseType);
        setDimensions(dimensions);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitType(this);
    }

    public void decDimensions()
    {
        _dimensions--;
    }

    public String getBaseName()
    {
        return (_basePrimitiveType != null ?
                    _basePrimitiveType.getName() :
                    _baseReferenceType);
    }

    public Type getBaseType()
    {
        if (isArray())
        {
            return new Type(this, 0);
        }
        else
        {
            return this;
        }
    }

    public Type getClone()
    {
        return new Type(this);
    }

    public int getDimensions()
    {
        return _dimensions;
    }

    public PrimitiveType getPrimitiveBaseType()
    {
        return (_isPrimitive ? _basePrimitiveType : null);
    }

    public String getQualifiedName()
    {
        return (_baseReferenceTypeDecl != null ?
                    _baseReferenceTypeDecl.getQualifiedName() :
                    _baseReferenceType);
    }

    public String getReferenceBaseType()
    {
        return (_isPrimitive ? null : _baseReferenceType);
    }

    public TypeDeclaration getReferenceBaseTypeDecl()
    {
        return (_isPrimitive ? null : _baseReferenceTypeDecl);
    }

    // Returns the name in a form similar to java.lang.Class.getName()
    public String getSignature()
    {
        if (isArray())
        {
            StringBuffer result = new StringBuffer();

            for (int idx = 0; idx < _dimensions; idx++)
            {
                result.append("[");
            }
            if (isPrimitive())
            {
                result.append(_basePrimitiveType.getSignature());
            }
            else
            {
                result.append("L");
                result.append(getBaseName());
                result.append(";");
            }
            return result.toString();
        }
        else
        {
            return getBaseName();
        }
    }

    public boolean hasReferenceBaseTypeDecl()
    {
        return (_isPrimitive ? false : _baseReferenceTypeDecl != null);
    }

    public void incDimensions()
    {
        _dimensions++;
    }

    public boolean isArray()
    {
        return (_dimensions > 0);
    }

    public boolean isAssignmentCompatibleTo(Type varType)
    {
        if (_isPrimitive && !isArray())
        {
            if (!varType.isPrimitive() || varType.isArray())
            {
                return false;
            }

            // Now it can only be a narrowing conversion of byte/short/char/int to
            // byte/short/char; However, ensuring that the value is constant
            // and is representable in the variable type, is too difficult since
            // it would involve symbolic evaluation of the expression

            PrimitiveType varPrimType = varType.getPrimitiveBaseType();

            if (((varPrimType == PrimitiveType.BYTE_TYPE) ||
                 (varPrimType == PrimitiveType.SHORT_TYPE) ||
                 (varPrimType == PrimitiveType.CHAR_TYPE)) &&
                ((_basePrimitiveType == PrimitiveType.BYTE_TYPE) ||
                 (_basePrimitiveType == PrimitiveType.SHORT_TYPE) ||
                 (_basePrimitiveType == PrimitiveType.CHAR_TYPE) ||
                 (_basePrimitiveType == PrimitiveType.INT_TYPE)))
            {
                return true;
            }
        }
        return isEqualTo(varType) || isWideningConvertibleTo(varType);
    }

    public boolean isEqualTo(Type other)
    {
        if ((other == null) ||
            (_isPrimitive != other._isPrimitive) ||
            (_dimensions  != other._dimensions))
        {
            return false;
        }
        else if (isNullType())
        {
            return other.isNullType();
        }
        else
        {
            if (_isPrimitive)
            {
                return (_basePrimitiveType == other._basePrimitiveType);
            }
            else
            {
                // we don't see "String" and "java.lang.String"
                // as equal, this can only be remedied by resolving
                // (where "String" becomes "java.lang.String")
                return getQualifiedName().equals(other.getQualifiedName());
            }
        }
    }

    public boolean isMethodInvocationConvertibleTo(Type varType)
    {
        if (_isPrimitive && !isArray() &&
            (!varType.isPrimitive() || varType.isArray()))
        {
            return false;
        }
        else
        {
            return isEqualTo(varType) || isWideningConvertibleTo(varType);
        }
    }

    public boolean isNullType()
    {
        return (!_isPrimitive && (_baseReferenceType == null));
    }

    // Determines whether this type is a primitive type scalar or array
    public boolean isPrimitive()
    {
        return _isPrimitive;
    }

    // Determines whether this type is a reference type scalar or array
    public boolean isReference()
    {
        return !_isPrimitive && (_baseReferenceType != null);
    }

    public boolean isSubTypeOf(Type other)
    {
        if (isArray() && other.isReference() && !other.isArray())
        {
            return "java.lang.Object".equals(other.getQualifiedName()) ||
                   "java.lang.Clonable".equals(other.getQualifiedName()) ||
                   "java.io.Serializable".equals(other.getQualifiedName());
        }
        else if (isReference() && !"java.lang.Object".equals(getQualifiedName()))
        {
            if (_baseReferenceTypeDecl != null)
            {
                return _baseReferenceTypeDecl.isSubTypeOf(other);
            }
            else
            {
                return other.isReference() && !other.isArray() &&
                       "java.lang.Object".equals(other.getQualifiedName());
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isWideningConvertibleTo(Type varType)
    {
        if (isPrimitive() && !isArray())
        {
            if (varType.isPrimitive() && !varType.isArray())
            {
                return _basePrimitiveType.isWideningConvertibleTo(varType.getPrimitiveBaseType());
            }
        }
        else if (isArray())
        {
            if (isReference() && varType.isArray())
            {
                if (varType.getDimensions() == _dimensions)
                {
                    Type varBaseType = varType.getClone();
                    Type valBaseType = getClone();

                    varBaseType.setDimensions(0);
                    valBaseType.setDimensions(0);
                    return valBaseType.isWideningConvertibleTo(varBaseType);
                }
            }
            else
            {
                return isSubTypeOf(varType);
            }
        }
        else if (isReference())
        {
            if ("java.lang.Object".equals(getQualifiedName()))
            {
                return false;
            }
            else if (!varType.isPrimitive() && !varType.isArray())
            {
                if (isEqualTo(varType) || isSubTypeOf(varType) ||
                    "java.lang.Object".equals(varType.getQualifiedName()))
                {
                    return true;
                }
            }
        }
        else if (isNullType() && (!varType.isPrimitive() || varType.isArray()))
        {
            return true;
        }
        return false;
    }

    public void setDimensions(int dimensions)
    {
        if (dimensions >= 0)
        {
            _dimensions = dimensions;
        }
    }

    public void setReferenceBaseTypeDecl(TypeDeclaration decl)
    {
        _isPrimitive = false;
        if (decl == null)
        {
            _baseReferenceTypeDecl = null;
        }
        else
        {
            _baseReferenceType     = decl.getQualifiedName();
            _baseReferenceTypeDecl = decl;
        }
    }

    public String toString()
    {
        String base = getBaseName();

        for (int dim = 0; dim < _dimensions; dim++)
        {
            base += "[]";
        }
        return base;
    }
}
