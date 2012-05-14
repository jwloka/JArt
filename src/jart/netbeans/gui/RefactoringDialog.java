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

import jart.netbeans.refactoring.RefactoringHelper;
import jart.smelling.SmellOccurrence;
import jart.smelling.SmellOccurrenceIterator;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.TypeDeclaration;
import jast.ui.ResourceHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RefactoringDialog extends JDialog
{
    private RefactoringHelper _helper;
    private DefaultListModel  _smells         = new DefaultListModel();
    private JList             _smellList      = new JList(_smells);
    private DefaultListModel  _restructurings = new DefaultListModel();
    private JTextArea         _detailsArea    = new JTextArea();

    public RefactoringDialog(RefactoringHelper helper)
    {
        super();
        _helper = helper;
        initialize();
    }

    private void initialize()
    {
        setName("ResultDialog");
        setTitle("Analysis Results");

        JSplitPane listSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        listSplitPanel.setDividerSize(8);
        listSplitPanel.setLastDividerLocation(300);
        listSplitPanel.setDoubleBuffered(true);
        listSplitPanel.setDividerLocation(301);
        listSplitPanel.setOneTouchExpandable(true);
        listSplitPanel.add(initializeRestructuringPanel(), JSplitPane.RIGHT);
        listSplitPanel.add(initializeSmellListPanel(), JSplitPane.LEFT);

        JSplitPane mainSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        mainSplitPanel.setDividerLocation(250);
        mainSplitPanel.setDividerSize(5);
        mainSplitPanel.setOneTouchExpandable(true);
        mainSplitPanel.add(listSplitPanel, JSplitPane.TOP);
        mainSplitPanel.add(initializeDetailsArea(), JSplitPane.BOTTOM);

        JPanel buttonPanel = new JPanel();
        JButton closeButton =
            new JButton("Close", ResourceHelper.loadIcon("/jart-icons/Discard.gif"));

        closeButton.setMnemonic('C');
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                closeDialog();
            }
        });
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton, closeButton.getName());

        JPanel dialogPane = new JPanel();

        dialogPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dialogPane.setLayout(new BorderLayout());
        dialogPane.add(mainSplitPanel, BorderLayout.CENTER);
        dialogPane.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(dialogPane);
        setSize(600, 400);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
    }

    private JPanel initializeDetailsArea()
    {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel detailsLabel =
            new JLabel(
                "Smell Details",
                ResourceHelper.loadIcon("/jart-icons/SmellDetails.gif"),
                JLabel.LEFT);
        JScrollPane detailsScroller = new JScrollPane();

        _detailsArea.setLineWrap(true);
        _detailsArea.setWrapStyleWord(true);
        _detailsArea.setDisabledTextColor(new Color(139, 0, 0));
        _detailsArea.setEnabled(false);

        detailsScroller.setViewportView(_detailsArea);

        detailsLabel.setPreferredSize(new Dimension(90, 25));

        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(detailsScroller, BorderLayout.CENTER);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return detailsPanel;
    }

    private JPanel initializeRestructuringPanel()
    {
        JPanel restructuringPanel = new JPanel(new BorderLayout());
        JPanel restructuringButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton =
            new JButton("Apply", ResourceHelper.loadIcon("/jart-icons/Restructuring.gif"));
        JList restructuringList = new JList(_restructurings);
        JScrollPane restructuringListScroller = new JScrollPane(restructuringList);
        JLabel restructuringLabel = new JLabel("Possible Restructurings");

        applyButton.setMnemonic('A');
        restructuringButtonPanel.add(applyButton, applyButton.getName());

        _restructurings.clear();

        restructuringLabel.setPreferredSize(new Dimension(137, 25));

        restructuringPanel.add(restructuringListScroller, BorderLayout.CENTER);
        restructuringPanel.add(restructuringButtonPanel, BorderLayout.SOUTH);
        restructuringPanel.add(restructuringLabel, BorderLayout.NORTH);
        restructuringPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return restructuringPanel;
    }

    private JPanel initializeSmellListPanel()
    {
        JPanel smellListPanel = new JPanel(new BorderLayout());
        JPanel smellButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton showButton =
            new JButton("Show", ResourceHelper.loadIcon("/jart-icons/ShowInSource.gif"));
        JScrollPane smellListScroller = new JScrollPane(_smellList);
        JLabel smellLabel = new JLabel("Detected Smells");

        showButton.setMnemonic('S');
        showButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                showInEditor();
            }
        });
        smellButtonPanel.add(showButton, showButton.getName());

        _smells.clear();
        for (SmellOccurrenceIterator it = _helper.getOccurrences(); it.hasNext();)
            {
            _smells.addElement(new SmellOccurrenceListItem(it.getNext()));
        }
        _smellList.setCellRenderer(new SmellOccurrenceRenderer());
        _smellList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _smellList.setSelectionBackground(Color.lightGray);
        _smellList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent evt)
            {
                updateDetails();
            }
        });

        smellLabel.setPreferredSize(new Dimension(93, 25));

        smellListPanel.add(smellButtonPanel, BorderLayout.SOUTH);
        smellListPanel.add(smellListScroller, BorderLayout.CENTER);
        smellListPanel.add(smellLabel, BorderLayout.NORTH);
        smellListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return smellListPanel;
    }

    private void updateDetails()
    {
        if (_smellList.getSelectedIndex() >= 0)
            {
            SmellOccurrence occurrence =
                ((SmellOccurrenceListItem) _smellList.getSelectedValue()).getSmellOccurrence();
            CompilationUnit unit = occurrence.getEnclosingUnit();
            TypeDeclaration typeDecl = occurrence.getEnclosingType();
            StringBuffer text = new StringBuffer();

            text.append("The smell ");
            text.append(occurrence.getSmellName());
            text.append(" has been found ");
            if ((occurrence.getPosition().getLine() >= 0)
                && (occurrence.getPosition().getColumn() >= 0))
                {
                text.append("at line ");
                text.append(occurrence.getPosition().getLine());
                text.append(", column ");
                text.append(occurrence.getPosition().getColumn());
                text.append(" ");
            }
            text.append("within the type ");
            text.append(typeDecl.getQualifiedName());
            text.append(" (");
            text.append(unit.getTopLevelQualifiedName().replace('.', '/'));
            text.append(".java).");
            _detailsArea.setText(text.toString());
        }
        else
            {
            _detailsArea.setText("");
        }
    }

    private void updateRefactorings()
    {
        _restructurings.clear();
        if (_smellList.getSelectedIndex() == 0)
            {
            return;
        }

        SmellOccurrence occurrence =
            ((SmellOccurrenceListItem) _smellList.getSelectedValue()).getSmellOccurrence();
        Vector mappings = _helper.getMappingsFor(occurrence);

        if ((mappings == null) || mappings.isEmpty())
            {
            return;
        }
        for (Enumeration en = mappings.elements(); en.hasMoreElements();)
            {
            _restructurings.addElement(en.nextElement());
        }
    }

    private void showInEditor()
    {
        if (_smellList.getSelectedIndex() >= 0)
            {
            _helper.showInEditor(
                ((SmellOccurrenceListItem) _smellList.getSelectedValue()).getSmellOccurrence());
        }
    }

    private void closeDialog()
    {
        setVisible(false);
        dispose();
    }
}

