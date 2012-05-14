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
package jart.netbeans.gui;

import jast.ast.Project;
import jast.ui.NodeTree;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class ProjectTreeView extends JDialog
{
    private JScrollPane _pane        = new JScrollPane();
    private NodeTree    _tree;
    private JButton     _closeButton = new JButton("Close");

    public ProjectTreeView(String title, boolean isModal)
    {
        this(title, new Frame(), isModal);
    }

    public ProjectTreeView(String title, Frame parent, boolean isModal)
    {
        super(parent, isModal);
        setTitle(title);
        initComponents();
    }

    private void initComponents()
    {
        _closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog();
            }
        });
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(_pane, BorderLayout.CENTER);
        getContentPane().add(_closeButton, BorderLayout.SOUTH);
        setSize(300, 400);
    }

    public void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    public void setProject(Project project)
    {
        if (project == null)
            {
            _tree = null;
        }
        else
            {
            _tree = new NodeTree(project);
        }
        _pane.setViewportView(_tree);
    }

    public void refresh()
    {
        if (_tree != null)
            {
            _tree.refresh();
        }
    }
}

