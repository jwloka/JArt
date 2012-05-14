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

import jast.Message;
import jast.ast.Node;

import java.awt.Color;

import javax.swing.JLabel;

// A label which contains an error/warning message
public class MessageLabel extends JLabel
{
    public static final int FATAL_ERROR = 0;
    public static final int ERROR       = 1;
    public static final int WARNING     = 2;
    public static final int NOTE        = 3;

    private static final Color[] COLORS = { Color.red, Color.orange, Color.green, Color.black };

    private Color   _levelColor;
    private Message _msg;

    public MessageLabel(int errorLevel, String msg, Node origin)
    {
        _levelColor = COLORS[errorLevel];
        _msg = new Message(msg, origin);
        setText(_msg.toString());
    }

    public Message getMessage()
    {
        return _msg;
    }

    public Color getLevelColor()
    {
        return _levelColor;
    }
}