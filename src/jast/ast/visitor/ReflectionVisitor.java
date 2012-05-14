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
package jast.ast.visitor;
import jast.Global;
import jast.Utility;

import java.lang.reflect.Method;

// A different visitor pattern which allows visiting without any
// hooks in the visited classes (accept-methods)
// Due to the use of reflection this type of visitor can be
// slower, though
public class ReflectionVisitor
{
    // If <code>true</code>, then the visitor issues a warning
    // whenever it tries to call a handler that is missing
    private boolean _warnIfHandlerMissing = false;

    public boolean isWarningIfHandlerMissing()
    {
        return _warnIfHandlerMissing;
    }

    public void postVisit(Object obj)
    {
    }

    public void preVisit(Object obj)
    {
    }

    public void visit(Object obj)
    {
        if (obj == null)
        {
            return;
        }

        preVisit(obj);

        String name = obj.getClass().getName();

        // An array ?
        if (!name.startsWith("["))
        {
            if (name.lastIndexOf(".") >= 0)
            {
                name = name.substring(name.lastIndexOf(".")+1);
            }

            Class[]  params = new Class[1];
            Object[] args   = new Object[1];

            params[0] = obj.getClass();
            args[0]   = obj;

            try
            {
                Method handler = getClass().getMethod("visit"+name,
                                                      params);

                if (handler != null)
                {
                    handler.invoke(this, args);
                }
            }
            catch (NoSuchMethodException noMethodEx)
            {
                // This exception is ok since it means that this visitor
                // does not have a handler for the type of the given object
                if (_warnIfHandlerMissing)
                {
                    Global.getOutput().addWarning("Could not invoke handler for node of type "+name+
                                                  "("+noMethodEx+")", null);
                }
            }
            catch (Exception ex)
            {
                Global.getOutput().addError(Utility.stackTrace(ex), null);
            }
            catch (StackOverflowError stackError)
            {
                // This can happen if the expressions nest too deeply
                // (a prominent example is java.text.Normalizer)
                Global.getOutput().addError(Utility.stackTrace(stackError), null);
            }
        }

        postVisit(obj);
    }

    public void warnIfHandlerMissing(boolean doIt)
    {
        _warnIfHandlerMissing = doIt;
    }
}
