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
package jast;

import jast.ast.Node;

import java.util.Vector;

// Stores messages
public class MessageArray
{
    private Vector _msgs = new Vector();

    public void add(Message msg)
    {
        _msgs.addElement(msg);
    }

    // Convenience method
    public void add(String text, Node origin)
    {
        _msgs.addElement(new Message(text, origin));
    }

    public void clear()
    {
        _msgs.clear();
    }

    public Message get(int idx)
    {
        return (Message)_msgs.elementAt(idx);
    }

    public int getCount()
    {
        return _msgs.size();
    }

    public MessageIterator getMessages()
    {
        return new MessageIterator()
            {
                private int _idx = 0;
                public boolean hasNext()
                {
                    return _idx < _msgs.size();
                }
                public Message getNext()
                {
                    return hasNext() ? (Message)_msgs.elementAt(_idx++) : null;
                }
            };
    }

    public boolean isEmpty()
    {
        return _msgs.isEmpty();
    }
}
