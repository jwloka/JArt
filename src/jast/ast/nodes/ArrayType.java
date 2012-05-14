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
import jast.ast.visitor.Visitor;

import java.lang.reflect.Modifier;

// According to Java Spec 10.7, array types are equivalent to
// a class 'Array' as specified by:
//
// class Array implements Cloneable, java.io.Serializable {
//      public final int length = X;
//    public Object clone() {
//        try {
//              return super.clone();
//           } catch (CloneNotSupportedException e) {
//                throw new InternalError(e.getMessage());
//          }
//      }
//
// This class serves as a convenience type to integrate the arrays
// into the normal type hierarchy
// Note however that it must not be included in the classes
// collection of projects (each project manages its own array type)
public class ArrayType extends ClassDeclaration
{

    public ArrayType()
    {
        super(Global.getFactory().createModifiers(Modifier.PUBLIC),
              "[]",
              false);
        getFields().add(
            Global.getFactory().createFieldDeclaration(
                Global.getFactory().createModifiers(Modifier.PUBLIC + Modifier.FINAL),
                Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                "length",
                null)
            );
        getMethods().add(
            Global.getFactory().createMethodDeclaration(
                Global.getFactory().createModifiers(Modifier.PUBLIC),
                Global.getFactory().createType("java.lang.Object", 0),
                "clone",
                null)
        );
        try
        {
            setBaseClass(Global.getFactory().createType("java.lang.Object", 0));
            getBaseInterfaces().add(
                Global.getFactory().createType("java.lang.Clonable", 0));
            getBaseInterfaces().add(
                Global.getFactory().createType("java.lang.Serializable", 0));
        }
        catch (SyntaxException ex)
        {}
    }

    public void accept(Visitor visitor)
    {
        visitor.visitClassDeclaration(this);
    }
}
