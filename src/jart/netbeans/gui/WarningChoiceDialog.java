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

import jast.BasicMessageContainer;
import jast.Message;
import jast.MessageIterator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WarningChoiceDialog extends JDialog
{
    private JScrollPane _scrollPane   = new JScrollPane();
    private JLabel      _msgLabel     = new JLabel();
    private MessageList _msgList      = new MessageList();
    private JPanel      _buttonPane   = new JPanel();
    private JButton     _okButton     = new JButton("OK");
    private JButton     _cancelButton = new JButton("Cancel");
    private boolean     _wasOK        = false;

    public WarningChoiceDialog()
    {
        super(new Frame(), true);
        initComponents();
    }

    public void showMessages(BasicMessageContainer receiver)
    {
        MessageIterator it;
        Message msg;
        boolean okEnabled = true;

        _msgList.clear();
        it = receiver.getFatalErrors();
        if (it.hasNext())
            {
            okEnabled = false;
            while (it.hasNext())
                {
                msg = it.getNext();
                _msgList.addItem(
                    new MessageListItem(
                        MessageListItem.FATAL_ERROR,
                        msg.getMessage(),
                        msg.getOrigin()));
            }
        }

        it = receiver.getErrors();
        if (it.hasNext())
            {
            okEnabled = false;
            while (it.hasNext())
                {
                msg = it.getNext();
                _msgList.addItem(
                    new MessageListItem(MessageListItem.ERROR, msg.getMessage(), msg.getOrigin()));
            }
        }

        it = receiver.getWarnings();
        while (it.hasNext())
            {
            msg = it.getNext();
            _msgList.addItem(
                new MessageListItem(
                    MessageListItem.WARNING,
                    msg.getMessage(),
                    msg.getOrigin()));
        }

        it = receiver.getNotes();
        while (it.hasNext())
            {
            msg = it.getNext();
            _msgList.addItem(
                new MessageListItem(MessageListItem.NOTE, msg.getMessage(), msg.getOrigin()));
        }

        _okButton.setEnabled(okEnabled);
        setVisible(true);
    }

    private void initComponents()
    {
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(_msgLabel, BorderLayout.NORTH);

        _scrollPane.setViewportView(_msgList);
        getContentPane().add(_scrollPane, BorderLayout.CENTER);

        _okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                _wasOK = true;
                closeDialog();
            }
        });
        _cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog();
            }
        });
        _buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        _buttonPane.add(_okButton);
        _buttonPane.add(_cancelButton);
        getContentPane().add(_buttonPane, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                closeDialog();
            }
        });
        setSize(400, 300);
    }

    public void setVisible(boolean shouldBeVisible)
    {
        super.setVisible(shouldBeVisible);
        if (shouldBeVisible)
            {
            _wasOK = false;
        }
    }

    public void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    public boolean wasOK()
    {
        return _wasOK;
    }
}

