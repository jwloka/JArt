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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListDialog extends JDialog
{
    private JScrollPane _scrollPane = new JScrollPane();

    private JList _actionsList = new JList();

    private DefaultListModel _model = new DefaultListModel();

    private JPanel _rightPane = new JPanel();

    private JPanel _buttonPane = new JPanel();

    private JButton _okButton = new JButton();

    private JButton _cancelButton = new JButton();

    private boolean _wasOK = false;

    public ListDialog(String title, boolean isModal)
    {
        this(title, new Frame(), isModal);
    }

    public ListDialog(String title, Frame parent, boolean isModal)
    {
        super(parent, isModal);
        setTitle(title);
        initComponents();
    }

    private void initComponents()
    {
        getContentPane().setLayout(new BorderLayout(5, 5));
        _actionsList.setModel(_model);
        _actionsList.setCellRenderer(new CheckedListItemRenderer());
        _actionsList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent evt)
            {
                checkSelectionStatus();
            }
        });
        _scrollPane.setViewportView(_actionsList);
        getContentPane().add(_scrollPane, BorderLayout.CENTER);
        _okButton.setText("OK");
        _okButton.setEnabled(false);
        _okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog(true);
            }
        });
        _cancelButton.setText("Cancel");
        _cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog(false);
            }
        });
        _buttonPane.setLayout(new GridLayout(2, 0, 5, 5));
        _buttonPane.add(_okButton);
        _buttonPane.add(_cancelButton);
        _rightPane.add(_buttonPane);
        getContentPane().add(_rightPane, BorderLayout.EAST);
        allowMultiSelect(false);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void allowMultiSelect(boolean allow)
    {
        _actionsList.setSelectionMode(
            allow
                ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                : ListSelectionModel.SINGLE_SELECTION);
    }

    private void checkSelectionStatus()
    {
        _okButton.setEnabled(_actionsList.getSelectedIndex() >= 0);
    }

    private void closeDialog(boolean wasOK)
    {
        _wasOK = wasOK;
        setVisible(false);
        dispose();
    }

    public boolean wasOK()
    {
        return _wasOK;
    }

    public void addItem(Object obj)
    {
        addItem(obj, false);
    }

    public void addItem(Object obj, boolean isSelected)
    {
        _model.addElement(new CheckedListItem(obj, isSelected));
    }

    public Object getSelection()
    {
        CheckedListItem item = (CheckedListItem) _actionsList.getSelectedValue();

        return item != null ? item.getObject() : null;
    }

    public Object[] getSelections()
    {
        Object[] items = _actionsList.getSelectedValues();

        if ((items == null) || (items.length == 0))
            {
            return new Object[0];
        }

        Object[] results = new Object[items.length];

        for (int idx = 0; idx < items.length; idx++)
            {
            results[idx] = ((CheckedListItem) items[idx]).getObject();
        }
        return results;
    }

    public int getSelectedIndex()
    {
        return _actionsList.getSelectedIndex();
    }

    public int[] getSelectedIndices()
    {
        return _actionsList.getSelectedIndices();
    }

    public void select(Object obj)
    {
        CheckedListItem item;

        for (int idx = 0; idx < _model.getSize(); idx++)
            {
            item = (CheckedListItem) _model.getElementAt(idx);
            if (item.getObject().equals(obj))
                {
                _actionsList.addSelectionInterval(idx, idx);
            }
        }
    }

    public void selectAll()
    {
        _actionsList.addSelectionInterval(0, _model.size() - 1);
    }

    public static void main(String[] args)
    {
        ListDialog pane = new ListDialog("test", true);

        pane.addItem("test1");
        pane.addItem("test2");
        pane.addItem("test3");
        pane.allowMultiSelect(true);
        pane.show();
        System.out.println("Selected ? " + pane.getSelection());
    }
}

