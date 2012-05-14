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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class NameDialog extends JDialog
{
    private JLabel     _noteLabel    = new JLabel();
    private JTextField _valueField   = new JTextField(40);
    private String     _result       = null;

    public NameDialog(String title, String desc, String initialValue)
    {
        super(new Frame(), true);
        setTitle(title);
        _noteLabel.setText(desc);
        _valueField.setText(initialValue);
        _valueField.selectAll();
        initComponents();
    }

    private void initComponents()
    {
        JPanel pane = new JPanel();

        pane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pane.setLayout(new BorderLayout(5, 5));
        pane.add(_noteLabel, BorderLayout.NORTH);
        pane.add(_valueField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");

        okButton.setName("OkButton");
        okButton.setIcon(ResourceHelper.loadIcon("/jart-icons/Confirm.gif"));
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                String result = _valueField.getText();

                if ((result != null) && (result.length() > 0))
                    {
                    _result = result;
                    closeDialog();
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setName("CancelButton");
        cancelButton.setIcon(ResourceHelper.loadIcon("/jart-icons/Discard.gif"));
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                _result = null;
                closeDialog();
            }
        });

        JPanel buttonPane = new JPanel();

        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPane.add(okButton);
        buttonPane.add(cancelButton);
        pane.add(buttonPane, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(pane, BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    public String getText()
    {
        return _result;
    }

    public boolean wasOK()
    {
        return _result != null;
    }
}

