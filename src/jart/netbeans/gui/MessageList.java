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

import jart.netbeans.Integration;
import jast.Message;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MessageList extends JList
{
    private DefaultListModel _model = new DefaultListModel();

    public MessageList()
    {
        super();
        setModel(_model);
        setCellRenderer(new MessageRenderer());
        addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt)
            {
                if (evt.getClickCount() > 1)
                    {
                    openMessageSource(locationToIndex(evt.getPoint()));
                }
            }
        });
    }

    public void addItem(MessageListItem msg)
    {
        _model.addElement(msg);
    }

    private void openMessageSource(int itemIdx)
    {
        MessageListItem item = (MessageListItem) _model.getElementAt(itemIdx);
        Message msg = item.getMessage();
        String unitName = msg.getCompilationUnit();

        if (unitName == null)
            {
            return;
        }

        int pos = 0;

        if (msg.getPosition() != null)
            {
            pos = msg.getPosition().getAbsolute();
        }
        if (pos < 0)
            {
            return;
        }

        Integration.getInstance().openInEditor(unitName, pos);
    }

    public void clear()
    {
        _model.clear();
    }
}

