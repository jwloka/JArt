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
import jast.Global;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.Package;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.PackageIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;

import java.util.Hashtable;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class NodeTreeHelper
{
    private NodeTreeIconAssociation _assoc          = new NodeTreeIconAssociation();
    private Project                 _project;
    private NodeTreeItem            _root;
    private TreeModel               _model;
    private Hashtable               _taggedObjs     = new Hashtable();
    private boolean                 _showModifiers  = false;
    private boolean                 _showParamNames = true;

    public NodeTreeHelper()
    {
        setProject(null);
    }

    private NodeTreeItem adapt(Node node)
    {
        NodeTreeItem result = null;
        Class[]      params = { node.getClass() };
        Object[]     args   = { node };

        try
        {
            java.lang.reflect.Method method = getClass().getDeclaredMethod("adapt", params);

            if (method != null)
            {
                result = (NodeTreeItem)method.invoke(this, args);
            }
        }
        catch (Exception ex)
        {}
        return result;
    }

    private NodeTreeItem adapt(InvocableDeclaration invocDecl)
    {
        NodeTreeItem result = new NodeTreeItem();
        StringBuffer desc   = new StringBuffer();

        result.setNode(invocDecl);

        if (_showModifiers)
        {
            desc.append(invocDecl.getModifiers().toString());
        }
        desc.append(invocDecl.getName());
        desc.append("(");
        if (invocDecl.hasParameters())
        {
            FormalParameter param;

            for (FormalParameterIterator it = invocDecl.getParameterList().getParameters().getIterator(); it.hasNext();)
            {
                param = it.getNext();
                desc.append(param.getType().toString());
                if (_showParamNames)
                {
                    desc.append(" ");
                    desc.append(param.getName());
                }
                if (it.hasNext())
                {
                    desc.append(", ");
                }
            }
        }
        desc.append(")");

        result.setText(desc.toString());
        finishItem(result);

        return result;
    }

    private NodeTreeItem adapt(Package pckg)
    {
        NodeTreeItem result = new NodeTreeItem();

        result.setNode(pckg);

        for (TypeDeclarationIterator it = pckg.getTypes().getIterator(); it.hasNext();)
        {
            result.add(adapt(it.getNext()));
        }

        result.setText(pckg.getQualifiedName());
        finishItem(result);

        return result;
    }

    private NodeTreeItem adapt(TypeDeclaration typeDecl)
    {
        NodeTreeItem result = new NodeTreeItem();
        StringBuffer desc   = new StringBuffer();

        result.setNode(typeDecl);

        for (TypeDeclarationIterator it = typeDecl.getInnerTypes().getIterator(); it.hasNext();)
        {
            result.add(adapt(it.getNext()));
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            for (ConstructorIterator it = ((ClassDeclaration)typeDecl).getConstructors().getIterator(); it.hasNext();)
            {
                result.add(adapt(it.getNext()));
            }
        }
        for (FieldIterator it = typeDecl.getFields().getIterator(); it.hasNext();)
        {
            result.add(adapt(it.getNext()));
        }
        for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            result.add(adapt(it.getNext()));
        }

        if (_showModifiers)
        {
            desc.append(typeDecl.getModifiers().toString());
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            desc.append("class ");
        }
        else
        {
            desc.append("interface ");
        }
        desc.append(typeDecl.getName());

        result.setText(desc.toString());
        finishItem(result);

        return result;
    }

    private NodeTreeItem adapt(Project project)
    {
        NodeTreeItem result = new NodeTreeItem();

        result.setNode(project);

        for (PackageIterator it = project.getPackages().getIterator(); it.hasNext();)
        {
            result.add(adapt(it.getNext()));
        }

        result.setText(project.getName());
        finishItem(result);

        return result;
    }

    private void finishItem(NodeTreeItem item)
    {
        tagIfNecessary(item);
        item.setIcon(_assoc.getIconFor(item));
    }

    public TreeModel getModel()
    {
        return _model;
    }

    public void refresh()
    {
        Project project = (Project)_root.getNode();

        _root.removeAllChildren();
        for (PackageIterator it = project.getPackages().getIterator(); it.hasNext();)
        {
            _root.add(adapt(it.getNext()));
        }
    }

    public void setIconAssociation(NodeTreeIconAssociation assoc)
    {
        _assoc = (assoc == null ?
                     new NodeTreeIconAssociation() :
                     assoc);
    }

    public void setProject(Project project)
    {
        _project = project;
        if (_project == null)
        {
            _project = Global.getFactory().createProject("<unspecified project>");
        }
        _root  = adapt(_project);
        _model = new DefaultTreeModel(_root);
    }

    public void setTaggedObjects(Hashtable taggedObjs)
    {
        _taggedObjs = taggedObjs;
    }

    private void tagIfNecessary(NodeTreeItem item)
    {
        Node node = (item == null ? null : item.getNode());

        item.tag((node != null) && _taggedObjs.containsKey(node));
    }


    private NodeTreeItem adapt(FieldDeclaration fieldDecl)
    {
        NodeTreeItem result = new NodeTreeItem();
        StringBuffer desc   = new StringBuffer();

        result.setNode(fieldDecl);

        if (_showModifiers)
        {
            desc.append(fieldDecl.getModifiers().toString());
        }
        desc.append(fieldDecl.getType().toString());
        desc.append(" ");
        desc.append(fieldDecl.getName());

        result.setText(desc.toString());
        finishItem(result);

        return result;
    }
}
