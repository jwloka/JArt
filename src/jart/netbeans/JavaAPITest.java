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
package jart.netbeans;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.openide.src.ClassElement;
import org.openide.src.ConstructorElement;
import org.openide.src.FieldElement;
import org.openide.src.MethodElement;
import org.openide.src.MethodParameter;

public class JavaAPITest
{

    private static void printField(FieldElement fieldElem, PrintWriter output)
    {
        String mods = java.lang.reflect.Modifier.toString(fieldElem.getModifiers());

        output.print(mods);
        if (mods.length() > 0)
            {
            output.print(" ");
        }
        output.print(fieldElem.getType().getFullString());
        output.print(" ");
        output.print(fieldElem.getName());
    }

    private static void printConstructor(
        ConstructorElement constructorElem,
        PrintWriter output)
    {
        String mods =
            java.lang.reflect.Modifier.toString(constructorElem.getModifiers());
        MethodParameter[] params = constructorElem.getParameters();

        output.print(mods);
        if (mods.length() > 0)
            {
            output.print(" ");
        }
        output.print(constructorElem.getName());
        output.print("(");
        if ((params != null) && (params.length > 0))
            {
            for (int idx = 0; idx < params.length; idx++)
                {
                if (idx > 0)
                    {
                    output.print(", ");
                }
                if (params[idx].isFinal())
                    {
                    output.print("final ");
                }
                output.print(params[idx].getType());
            }
        }
        output.print(")");
    }

    private static void printMethod(MethodElement methodElem, PrintWriter output)
    {
        String mods = java.lang.reflect.Modifier.toString(methodElem.getModifiers());
        MethodParameter[] params = methodElem.getParameters();

        output.print(mods);
        if (mods.length() > 0)
            {
            output.print(" ");
        }
        output.print(methodElem.getReturn());
        output.print(" ");
        output.print(methodElem.getName());
        output.print("(");
        if ((params != null) && (params.length > 0))
            {
            for (int idx = 0; idx < params.length; idx++)
                {
                if (idx > 0)
                    {
                    output.print(", ");
                }
                if (params[idx].isFinal())
                    {
                    output.print("final ");
                }
                output.print(params[idx].getType());
            }
        }
        output.print(")");
    }

    public static void printClasses(ClassElement[] classElems)
    {
        if ((classElems == null) || (classElems.length == 0))
            {
            return;
        }

        StringWriter stringOutput = new StringWriter();
        PrintWriter output = new PrintWriter(stringOutput);

        for (int idx = 0; idx < classElems.length; idx++)
            {
            printClass(classElems[idx], output);
            output.print("\n");
        }
        TestView.show(stringOutput.getBuffer().toString());
    }

    public static void printClass(ClassElement classElem, PrintWriter output)
    {
        StringBuffer result = new StringBuffer();
        ClassElement[] innerTypes = classElem.getClasses();
        FieldElement[] fieldElems = classElem.getFields();
        ConstructorElement[] constructorElem = classElem.getConstructors();
        MethodElement[] methodElems = classElem.getMethods();
        int idx;

        output.print("Class ");
        output.print(classElem.getName().getName());
        output.print(" of package ");
        output.print(classElem.getName().getQualifier());
        output.print("\n");
        if (innerTypes != null)
            {
            for (idx = 0; idx < innerTypes.length; idx++)
                {
                output.print("Inner Type ");
                printClass(innerTypes[idx], output);
                output.print("\n");
            }
        }
        output.print("\n");
        if (fieldElems != null)
            {
            for (idx = 0; idx < fieldElems.length; idx++)
                {
                output.print("Field ");
                printField(fieldElems[idx], output);
                output.print("\n");
            }
        }
        if (constructorElem != null)
            {
            for (idx = 0; idx < constructorElem.length; idx++)
                {
                output.print("Constructor ");
                printConstructor(constructorElem[idx], output);
                output.print("\n");
            }
        }
        if (methodElems != null)
            {
            for (idx = 0; idx < methodElems.length; idx++)
                {
                output.print("Method ");
                printMethod(methodElems[idx], output);
                output.print("\n");
            }
        }
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
            {
            System.err.println("Usage : " + JavaAPITest.class.getName() + " SomeFile.java");
            return;
        }
        ClassElement classElem = ClassElement.forName(args[0]);

        if (classElem == null)

            {
            System.out.println("[JavaAPITest.main] Could not find class '" + args[0] + "'");
            return;
        }
        printClasses(new ClassElement[] { classElem });
    }
}