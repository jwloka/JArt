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

import java.util.Hashtable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class NodeTree extends JTree
{
    private NodeTreeHelper         _helper = new NodeTreeHelper();
    private DefaultMutableTreeNode _selected;

    public NodeTree()
    {
        this(null);
    }

    public NodeTree(Project project)
    {
        super();
        setProject(project);
        setRootVisible(false);
        setScrollsOnExpand(true);
    }

    public void setIconAssociation(NodeTreeIconAssociation assoc)
    {
        _helper.setIconAssociation(assoc);
    }

    public String convertValueToText(Object  value,
                                     boolean isSelected,
                                     boolean isExpanded,
                                     boolean isLeaf,
                                     int      row,
                                     boolean hasFocus)
    {
        if (value instanceof NodeTreeItem)
        {
            return ((NodeTreeItem)value).getText();
        }
        else
        {
            return super.convertValueToText(value,
                                            isSelected,
                                            isExpanded,
                                            isLeaf,
                                            row,
                                            hasFocus);
        }
    }

    private void findTreeNode(MutableTreeNode root, Node node)
    {
        NodeTreeItem item = (NodeTreeItem)root;

        if (item.getNode().equals(node))
        {
            _selected = (DefaultMutableTreeNode)item;
        }

        for (int idx = 0; idx < root.getChildCount(); idx++)
        {
            if (_selected == null)
            {
                findTreeNode((MutableTreeNode)root.getChildAt(idx), node);
            }
        }
    }

    public void refresh()
    {
        _helper.refresh();
        updateUI();
    }

    public void selectNode(Node node)
    {
        _selected = null;

        findTreeNode((MutableTreeNode)getModel().getRoot(), node);

        if (_selected != null)
        {
            setSelectionPath(new TreePath(_selected.getPath()));
            expandPath(getSelectionPath());
            scrollPathToVisible(getSelectionPath());
        }
    }

    public void setProject(Project project)
    {
        setCellRenderer(new NodeTreeCellRenderer());
        setRootVisible(false);
        _helper.setProject(project);
        setModel(_helper.getModel());
    }

    public void setTagList(Hashtable taggedObjs)
    {
        _helper.setTaggedObjects(taggedObjs);
    }
}
