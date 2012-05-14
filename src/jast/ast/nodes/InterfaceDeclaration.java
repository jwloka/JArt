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
import jast.ast.ContainerEvent;
import jast.ast.collections.AdditionEvent;
import jast.ast.collections.AssignmentEvent;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.Visitor;

public class InterfaceDeclaration extends    TypeDeclaration
                                  implements Feature
{

    public InterfaceDeclaration(Modifiers mods, String name)
    {
        super(mods, name);
        getModifiers().setAbstract();
    }

    public void accept(Visitor visitor)
    {
        visitor.visitInterfaceDeclaration(this);
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

        // first we check whether we hava a direct base interface
        for (it = getBaseInterfaces().getIterator(); it.hasNext();)
        {
            if (it.getNext().getQualifiedName().equals(otherName))
            {
                return true;
            }
        }
        // Now we must descend into the base interfaces
        for (it = getBaseInterfaces().getIterator(); it.hasNext();)
        {
            if (it.getNext().isSubTypeOf(other))
            {
                return true;
            }
        }
        return false;
    }

    public void notify(ContainerEvent event)
    {
        if ((event instanceof AdditionEvent) ||
            (event instanceof AssignmentEvent))
        {
            Feature   newFeature = (Feature)event.getObject();
            Modifiers mods       = null;

            if (newFeature instanceof VariableDeclaration)
            {
                mods = ((VariableDeclaration)newFeature).getModifiers();
                mods.setPublic();
                mods.setStatic();
                mods.setFinal();
            }
            else if (newFeature instanceof MethodDeclaration)
            {
                mods = ((MethodDeclaration)newFeature).getModifiers();
                mods.setPublic();
                mods.setAbstract();
            }
        }
        super.notify(event);
    }
}
