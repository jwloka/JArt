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

import jast.ast.Node;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Output extends JDialog implements jast.MessageReceiver
{
    private JScrollPane _scrollPane   = new JScrollPane();
    private MessageList _actionsList  = new MessageList();
    private JPanel      _buttonPane   = new JPanel();
    private JButton     _closeButton  = new JButton();

    public Output()
    {
        super(new Frame(), false);
        initComponents();
    }

    public void addError(String text, Node origin)
    {
        addItem(new MessageListItem(MessageListItem.ERROR, text, origin));
    }

    public void addFatalError(String text, Node origin)
    {
        addItem(new MessageListItem(MessageListItem.FATAL_ERROR, text, origin));
    }

    private void addItem(MessageListItem msg)
    {
        _actionsList.addItem(msg);
    }

    public void addNote(String text, Node origin)
    {
        addItem(new MessageListItem(MessageListItem.NOTE, text, origin));
    }

    public void addWarning(String text, Node origin)
    {
        addItem(new MessageListItem(MessageListItem.WARNING, text, origin));
    }

    private void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    private void initComponents()
    {
        getContentPane().setLayout(new BorderLayout(5, 5));
        _scrollPane.setViewportView(_actionsList);
        getContentPane().add(_scrollPane, BorderLayout.CENTER);

        _closeButton.setText("Close");
        _closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog();
            }
        });
        _buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        _buttonPane.add(_closeButton);
        getContentPane().add(_buttonPane, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                closeDialog();
            }
        });
        setSize(400, 400);
    }
}