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

// Stores messages of various types from the system (parser, resolver, ...)
public class BasicMessageContainer implements MessageReceiver
{
    private MessageArray _notes       = new MessageArray();
    private MessageArray _warnings    = new MessageArray();
    private MessageArray _errors      = new MessageArray();
    private MessageArray _fatalErrors = new MessageArray();

    // Convenience method
    public void addError(String text, Node origin)
    {
        _errors.add(text, origin);
    }

    public void addErrors(MessageIterator msgs)
    {
        for (; msgs.hasNext(); )
        {
            _errors.add(msgs.getNext());
        }
    }

    // Convenience method
    public void addFatalError(String text, Node origin)
    {
        _fatalErrors.add(text, origin);
    }

    public void addFatalErrors(MessageIterator msgs)
    {
        for (; msgs.hasNext(); )
        {
            _fatalErrors.add(msgs.getNext());
        }
    }

    public void addFrom(BasicMessageContainer msgReceiver)
    {
        addFatalErrors(msgReceiver.getFatalErrors());
        addErrors(msgReceiver.getErrors());
        addWarnings(msgReceiver.getWarnings());
        addNotes(msgReceiver.getNotes());
    }

    // Convenience method
    public void addNote(String text, Node origin)
    {
        _notes.add(text, origin);
    }

    public void addNotes(MessageIterator msgs)
    {
        for (; msgs.hasNext(); )
        {
            _notes.add(msgs.getNext());
        }
    }

    // Convenience method
    public void addWarning(String text, Node origin)
    {
        _warnings.add(text, origin);
    }

    public void addWarnings(MessageIterator msgs)
    {
        for (; msgs.hasNext(); )
        {
            _warnings.add(msgs.getNext());
        }
    }

    public MessageIterator getErrors()
    {
        return _errors.getMessages();
    }

    public MessageIterator getFatalErrors()
    {
        return _fatalErrors.getMessages();
    }

    public MessageIterator getNotes()
    {
        return _notes.getMessages();
    }

    public MessageIterator getWarnings()
    {
        return _warnings.getMessages();
    }

    public boolean hasErrors()
    {
        return !_errors.isEmpty();
    }

    public boolean hasFatalErrors()
    {
        return !_fatalErrors.isEmpty();
    }

    public boolean hasNotes()
    {
        return !_notes.isEmpty();
    }

    public boolean hasWarnings()
    {
        return !_warnings.isEmpty();
    }

    public void printAll()
    {
        printAll(new java.io.PrintWriter(System.out));
    }

    public void printAll(java.io.PrintWriter output)
    {
        MessageIterator it;
        Message         msg;

        if (hasFatalErrors())
        {
            output.println("Fatal Errors:");
            for (it = getFatalErrors(); it.hasNext();)
            {
                output.println("  "+it.getNext().toString());
            }
        }
        if (hasErrors())
        {
            output.println("Errors:");
            for (it = getErrors(); it.hasNext();)
            {
                output.println("  "+it.getNext().toString());
            }
        }
        if (hasWarnings())
        {
            output.println("Warnings:");
            for (it = getWarnings(); it.hasNext();)
            {
                output.println("  "+it.getNext().toString());
            }
        }
        if (hasNotes())
        {
            output.println("Notes:");
            for (it = getNotes(); it.hasNext();)
            {
                output.println("  "+it.getNext().toString());
            }
        }
        output.flush();
    }

    public void resetMessages()
    {
        _errors.clear();
        _fatalErrors.clear();
        _warnings.clear();
        _notes.clear();
    }
}
