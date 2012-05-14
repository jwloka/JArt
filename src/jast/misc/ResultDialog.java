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
package jast.misc;
import jast.ui.ResourceHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
import javax.swing.WindowConstants;

public class ResultDialog extends JDialog
{

    public ResultDialog()
    {
        super();
        initialize();
    }

    private void initialize()
    {
        setName("ResultDialog");
        setTitle("Analysis Results");

        JSplitPane listSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        listSplitPanel.setDividerSize(8);
        listSplitPanel.setLastDividerLocation(200);
        listSplitPanel.setDoubleBuffered(true);
        listSplitPanel.setDividerLocation(201);
        listSplitPanel.setOneTouchExpandable(true);
        listSplitPanel.add(initializeRestructuringPanel(), JSplitPane.RIGHT);
        listSplitPanel.add(initializeSmellListPanel(), JSplitPane.LEFT);

        JSplitPane mainSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        mainSplitPanel.setDividerLocation(250);
        mainSplitPanel.setDividerSize(5);
        mainSplitPanel.setOneTouchExpandable(true);
        mainSplitPanel.add(listSplitPanel, JSplitPane.TOP);
        mainSplitPanel.add(initializeDetailsArea(), JSplitPane.BOTTOM);

        JPanel  buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close",
                                          ResourceHelper.loadIcon("/icons/Discard.gif"));

        closeButton.setMnemonic('C');
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton, closeButton.getName());

        JPanel dialogPane = new JPanel();

        dialogPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dialogPane.setLayout(new BorderLayout());
        dialogPane.add(mainSplitPanel, BorderLayout.CENTER);
        dialogPane.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(dialogPane);
        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
    }

    private JPanel initializeDetailsArea()
    {
        JPanel      detailsPanel      = new JPanel(new BorderLayout());
        JLabel      detailsLabel      = new JLabel("Smell Details",
                                                   ResourceHelper.loadIcon("/icons/SmellDetails.gif"),
                                                   JLabel.LEFT);
        JScrollPane smellListScroller = new JScrollPane();
        JTextArea   smellsDetailsArea = new JTextArea();

        smellsDetailsArea.setLineWrap(true);
        smellsDetailsArea.setWrapStyleWord(true);
        smellsDetailsArea.setText("The method parseFigure() of class Drawing has FeatureEnvy to class Figure.");
        smellsDetailsArea.setDisabledTextColor(new Color(139, 0, 0));
        smellsDetailsArea.setBounds(0, 0, 160, 120);
        smellsDetailsArea.setEnabled(false);

        smellListScroller.setViewportView(smellsDetailsArea);

        detailsLabel.setPreferredSize(new Dimension(90, 25));

        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(smellListScroller, BorderLayout.CENTER);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return detailsPanel;
    }

    private JPanel initializeRestructuringPanel()
    {
        JPanel           restructuringPanel        = new JPanel(new BorderLayout());
        JPanel           restructuringButtonPanel  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton          applyButton               = new JButton("Apply",
                                                                 ResourceHelper.loadIcon("/icons/Restructuring.gif"));
        DefaultListModel model                     = new DefaultListModel();
        JList            restructuringList         = new JList(model);
        JScrollPane      restructuringListScroller = new JScrollPane(restructuringList);
        JLabel           restructuringLabel        = new JLabel("Possible Restructurings");

        applyButton.setMnemonic('A');
        restructuringButtonPanel.add(applyButton, applyButton.getName());

        restructuringList.setBounds(0, 0, 84, 248);
        model.addElement("Inline Class");
        model.addElement("Move Method");
        model.addElement("Remove Constructor");
        model.addElement("Remove Field");

        restructuringLabel.setPreferredSize(new Dimension(137, 25));

        restructuringPanel.setPreferredSize(new Dimension(190, 100));
        restructuringPanel.add(restructuringListScroller, BorderLayout.CENTER);
        restructuringPanel.add(restructuringButtonPanel, BorderLayout.SOUTH);
        restructuringPanel.add(restructuringLabel, BorderLayout.NORTH);
        restructuringPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return restructuringPanel;
    }

    private JPanel initializeSmellListPanel()
    {
        JPanel           smellListPanel    = new JPanel(new BorderLayout());
        JPanel           smellButtonPanel  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton          showButton        = new JButton("Show",
                                                         ResourceHelper.loadIcon("/icons/ShowInSource.gif"));
        DefaultListModel model             = new DefaultListModel();
        JList            smellList         = new JList(model);
        JScrollPane      smellListScroller = new JScrollPane(smellList);
        JLabel           smellLabel        = new JLabel("Detected Smells");

        showButton.setMnemonic('S');
        smellButtonPanel.add(showButton, showButton.getName());

        smellList.setBounds(0, 0, 84, 248);
        model.addElement("Data Class");
        model.addElement("Feature Envy");
        model.addElement("Lazy Class");

        smellLabel.setPreferredSize(new Dimension(93, 25));

        smellListPanel.setPreferredSize(new Dimension(190, 100));
        smellListPanel.add(smellButtonPanel, BorderLayout.SOUTH);
        smellListPanel.add(smellListScroller, BorderLayout.CENTER);
        smellListPanel.add(smellLabel, BorderLayout.NORTH);
        smellListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return smellListPanel;
    }

    public static void main(String[] args)
    {
        ResultDialog dlg = new ResultDialog();

        dlg.show();
    }
}
