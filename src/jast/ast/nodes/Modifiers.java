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

import java.lang.reflect.Modifier;

public class Modifiers extends Node
{
    private int _modifierMask;

    public Modifiers()
    {
        _modifierMask = 0;
    }

    // The mask bits are specified in java.lang.reflect.Modifier
    // and are used for instance in java.lang.reflect.Method
    public Modifiers(int mask)
    {
        _modifierMask = mask;
    }

    public void accept(Visitor visitor)
    {
        visitor.visitModifiers(this);
    }

    public int getMask()
    {
        return _modifierMask;
    }

    public boolean checkAccess(boolean canBePrivate,
                               boolean canBeProtected,
                               boolean canBeFriendly,
                               boolean canBePublic)
    {
        return (isPrivate()   && canBePrivate) ||
               (isProtected() && canBeProtected) ||
               (isPublic()    && canBePublic) ||
               (isFriendly()  && canBeFriendly);
    }

    private void delBits(int mask)
    {
        _modifierMask &= ~mask;
    }

    public Modifiers getClone()
    {
        Modifiers result = new Modifiers();

        result._modifierMask = _modifierMask;
        result.setStartPosition(getStartPosition());
        result.setFinishPosition(getFinishPosition());

        return result;
    }

    public boolean isAbstract()
    {
        return Modifier.isAbstract(_modifierMask);
    }

    public boolean isFinal()
    {
        return Modifier.isFinal(_modifierMask);
    }

    public boolean isFriendly()
    {
        return !isPrivate() && !isProtected() && !isPublic();
    }

    public boolean isNative()
    {
        return Modifier.isNative(_modifierMask);
    }

    public boolean isPrivate()
    {
        return Modifier.isPrivate(_modifierMask);
    }

    public boolean isProtected()
    {
        return Modifier.isProtected(_modifierMask);
    }

    public boolean isPublic()
    {
        return Modifier.isPublic(_modifierMask);
    }

    public boolean isStatic()
    {
        return Modifier.isStatic(_modifierMask);
    }

    public boolean isStrictfp()
    {
        return Modifier.isStrict(_modifierMask);
    }

    public boolean isSynchronized()
    {
        return Modifier.isSynchronized(_modifierMask);
    }

    public boolean isTransient()
    {
        return Modifier.isTransient(_modifierMask);
    }

    public boolean isVolatile()
    {
        return Modifier.isVolatile(_modifierMask);
    }

    public void setAbstract()
    {
        setBits(Modifier.ABSTRACT);
    }

    private void setBits(int mask)
    {
        _modifierMask |= mask;
    }

    public void setFinal()
    {
        delBits(Modifier.VOLATILE);
        setBits(Modifier.FINAL);
    }

    public void setFriendly()
    {
        delBits(Modifier.PRIVATE + Modifier.PROTECTED + Modifier.PUBLIC);
    }

    public void setNative()
    {
        setBits(Modifier.NATIVE);
    }

    public void setPrivate()
    {
        delBits(Modifier.PROTECTED + Modifier.PUBLIC);
        setBits(Modifier.PRIVATE);
    }

    public void setProtected()
    {
        delBits(Modifier.PRIVATE + Modifier.PUBLIC);
        setBits(Modifier.PROTECTED);
    }

    public void setPublic()
    {
        delBits(Modifier.PRIVATE + Modifier.PROTECTED);
        setBits(Modifier.PUBLIC);
    }

    public void setStatic()
    {
        setBits(Modifier.STATIC);
    }

    public void setStrictfp()
    {
        setBits(Modifier.STRICT);
    }

    public void setSynchronized()
    {
        setBits(Modifier.SYNCHRONIZED);
    }

    public void setTransient()
    {
        setBits(Modifier.TRANSIENT);
    }

    public void setVolatile()
    {
        delBits(Modifier.FINAL);
        setBits(Modifier.VOLATILE);
    }

    public String toString()
    {
        return Modifier.toString(_modifierMask);
    }
}
