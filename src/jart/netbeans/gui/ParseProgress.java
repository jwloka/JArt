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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ParseProgress extends JDialog
{
    JLabel       _msgLabel    = new JLabel();
    JProgressBar _progressBar = new JProgressBar();

    public ParseProgress(String title)
    {
        super(new Frame(), false);
        setTitle(title);
        initComponents();
    }

    private void initComponents()
    {
        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(_msgLabel, BorderLayout.NORTH);
        getContentPane().add(_progressBar, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                closeDialog();
            }
        });
        setSize(300, 90);
    }

    public void closeDialog()
    {
        setVisible(false);
        dispose();
    }

    public void setAmount(int amount)
    {
        _progressBar.setMinimum(0);
        _progressBar.setMaximum(amount);
    }

    public void setValue(int value)
    {
        _progressBar.setValue(value);
    }

    public void incValue()
    {
        _progressBar.setValue(_progressBar.getValue() + 1);
    }

    public void setMessage(String msg)
    {
        _msgLabel.setText(msg);
    }
}