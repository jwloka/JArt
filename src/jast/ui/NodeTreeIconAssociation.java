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
package jast.ui;

import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.Modifiable;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Package;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class NodeTreeIconAssociation
{
    private static final ImageIcon PROJECT_ICON   = ResourceHelper.loadIcon("/jast-icons/project.gif");
    private static final ImageIcon PACKAGE_ICON   = ResourceHelper.loadIcon("/jast-icons/package.gif");
    private static final ImageIcon CLASS_ICON     = ResourceHelper.loadIcon("/jast-icons/class.gif");
    private static final ImageIcon INTERFACE_ICON = ResourceHelper.loadIcon("/jast-icons/interface.gif");
    private static final ImageIcon PRIVATE_ICON   = ResourceHelper.loadIcon("/jast-icons/private.gif");
    private static final ImageIcon PROTECTED_ICON = ResourceHelper.loadIcon("/jast-icons/protected.gif");
    private static final ImageIcon PUBLIC_ICON    = ResourceHelper.loadIcon("/jast-icons/public.gif");

    public Icon getIconFor(NodeTreeItem item)
    {
        Node node = item.getNode();

        if (node != null)
        {
            if (node instanceof Project)
            {
                return PROJECT_ICON;
            }
            else if (node instanceof Package)
            {
                return PACKAGE_ICON;
            }
            else if (node instanceof ClassDeclaration)
            {
                return CLASS_ICON;
            }
            else if (node instanceof InterfaceDeclaration)
            {
                return INTERFACE_ICON;
            }
            else if (node instanceof Modifiable)
            {
                Modifiers mods = ((Modifiable) node).getModifiers();

                if (mods.isPrivate())
                {
                    return PRIVATE_ICON;
                }
                else if (mods.isPublic())
                {
                    return PUBLIC_ICON;
                }
                else
                {
                    return PROTECTED_ICON;
                }
            }
        }

        return null;
    }
}
