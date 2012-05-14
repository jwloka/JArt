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

import jast.ui.ResourceHelper;

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

public class YesNoDialog extends JDialog
{
    private JLabel  _msgLabel = new JLabel();
    private boolean _wasYes   = false;

    public YesNoDialog(String title, String text)
    {
        super(new Frame(title), true);
        _msgLabel.setText(text);
        initComponents();
    }

    private void initComponents()
    {
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(_msgLabel, BorderLayout.CENTER);

        JButton yesButton =
            new JButton("Yes", ResourceHelper.loadIcon("/jart-icons/Yes.gif"));

        yesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                _wasYes = true;
                closeDialog();
            }
        });

        JButton noButton =
            new JButton("No", ResourceHelper.loadIcon("/jart-icons/No.gif"));

        noButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                _wasYes = false;
                closeDialog();
            }
        });

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        buttonPane.add(yesButton);
        buttonPane.add(noButton);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                closeDialog();
            }
        });
        pack();
    }

    public void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    public boolean wasYes()
    {
        return _wasYes;
    }
}

