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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TypeNameDialog extends JDialog
{
    private JLabel     _typeLabel = new JLabel();
    private JTextField _typeField = new JTextField(40);
    private JTextField _dimField  = new JTextField(2);
    private JLabel     _nameLabel = new JLabel();
    private JTextField _nameField = new JTextField(40);
    private String     _type      = null;
    private String     _name      = null;
    private boolean    _showDims  = true;
    private int        _dims      = 0;

    public TypeNameDialog(String title, boolean showDimensions)
    {
        this(
            title,
            "Enter the qualified name and number of dimensions of the type",
            "Enter the name",
            showDimensions);
    }

    public TypeNameDialog(
        String title,
        String typeText,
        String nameText,
        boolean showDimensions)
    {
        super(new Frame(), title, true);
        _showDims = showDimensions;
        _typeLabel.setText(typeText);
        _nameLabel.setText(nameText);
        initComponents();
    }

    private void initComponents()
    {
        JPanel typePane = new JPanel(new BorderLayout(5, 5));

        typePane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        typePane.add(_typeLabel, BorderLayout.NORTH);
        typePane.add(_typeField, BorderLayout.CENTER);
        if (_showDims)
            {
            typePane.add(_dimField, BorderLayout.EAST);
            _dimField.setDocument(new PlainDocument()
            {
                public void insertString(int offset, String text, AttributeSet attrs)
                    throws BadLocationException
                {
                    char[] source = text.toCharArray();
                    char[] result = new char[source.length];
                    int len = 0;

                    for (int idx = 0; idx < result.length; idx++)
                        {
                        if (Character.isDigit(source[idx]))
                            {
                            result[len++] = source[idx];
                        }
                    }
                    super.insertString(offset, new String(result, 0, len), attrs);
                }
            });
        }
        _dimField.setText("0");

        JPanel namePane = new JPanel(new BorderLayout(5, 5));

        namePane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        namePane.add(_nameLabel, BorderLayout.NORTH);
        namePane.add(_nameField, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");

        okButton.setName("OkButton");
        okButton.setIcon(ResourceHelper.loadIcon("/jart-icons/Confirm.gif"));
        okButton.setMnemonic('O');
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                String type = _typeField.getText();
                String name = _nameField.getText();
                String dims = _dimField.getText();

                if ((type != null)
                    && (type.length() > 0)
                    && (name != null)
                    && (name.length() > 0)
                    && (dims != null)
                    && (dims.length() > 0))
                    {
                    _type = type;
                    _name = name;
                    _dims = Integer.parseInt(dims);
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
                _type = null;
                _name = null;
                _dims = 0;
                closeDialog();
            }
        });

        JPanel buttonPane = new JPanel();

        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPane.add(okButton);
        buttonPane.add(cancelButton);

        JPanel pane = new JPanel(new GridLayout(3, 1, 5, 5));

        pane.add(typePane);
        pane.add(namePane);
        pane.add(buttonPane);

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

    public String getType()
    {
        return _type;
    }

    public String getName()
    {
        return _name;
    }

    public int getDimensions()
    {
        return _dims;
    }

    public boolean wasOK()
    {
        return (_name != null) && (_type != null);
    }
}

