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
package jart.netbeans;

import jast.MessageArray;
import jast.MessageIterator;
import jast.MessageReceiver;
import jast.ast.Node;

// blabla
public class Dummy implements MessageReceiver
{
    private MessageArray _notes = new MessageArray();

    private MessageArray _warnings = new MessageArray();

    private MessageArray _errors = new MessageArray();

    private MessageArray _fatalErrors = new MessageArray();

    public int _test = 0;

    public void addError(String test, Node origin)
    {
        _errors.add(test, origin);
    }

    public void addFatalError(String text, Node origin)
    {
        _fatalErrors.add(text, origin);
    }

    public void addNote(String test, Node origin)
    {
        _notes.add(test, origin);
    }

    public void addWarning(String test, Node origin)
    {
        _warnings.add(test, origin);
    }

    public boolean hasErrors()
    {
        return !_errors.isEmpty();
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

    public void resetMessages()
    {
        _errors.clear();
        _fatalErrors.clear();
        _warnings.clear();
        _notes.clear();
    }

    private void test()
    {
        int val = getTest();

        setTest(0);
    }

    private int getTest()
    {
        return _test;
    }

    private void setTest(int test)
    {
        _test = test;
    }
}

