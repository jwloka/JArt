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
package jast.prettyprinter;
import jast.ast.ParsePosition;

import java.util.Enumeration;
import java.util.Vector;

public class IndentPrinter implements StyleWriter
{
    private class StyledText
    {
        private StringBuffer _text  = new StringBuffer();
        private String       _style = Style.NORMAL;

        public StyledText()
        {}

        public StyledText(String style)
        {
            _style = style;
        }

        public String getStyle()
        {
            return _style;
        }

        public boolean hasStyle(String style)
        {
            return _style.equalsIgnoreCase(style);
        }

        public void append(String text)
        {
            _text.append(text);
        }

        public String toString()
        {
            return _text.toString();
        }
    }

    private class TryState
    {
        private Vector        _texts        = null;
        private ParsePosition _pos          = null;
        private boolean       _doIndent     = true;
        private boolean       _isNew        = false;
        private boolean       _isNL         = true;
        private int           _indent       = 0;
        private StringBuffer  _indentStr    = new StringBuffer();
        private int           _padding      = 0;
        private StringBuffer  _paddingStr   = new StringBuffer();
        private String        _curStyle     = Style.NORMAL;
        private char          _lastChar     = '\0';
        private boolean       _getMaxColumn = false;
        private int           _maxColumn    = 0;

        public TryState(TryState parent)
        {
            if (parent == null)
            {
                _pos = new ParsePosition(1, 1, 0);
            }
            else
            {
                _texts = new Vector();
                setStyle(parent.getStyle());
                _texts.addElement(new StyledText(_curStyle));
                _pos   = new ParsePosition(parent.getPosition());
                // indentation is done by the parent
                _isNL  = false;
                _isNew = parent.isNewLine();
                doIndent(parent.isIndenting());
                setIndent(parent.getIndent());
                setPadding(parent.getPadding());
            }
        }

        private StyledText getStyledText()
        {
            return (StyledText)_texts.elementAt(_texts.size() - 1);
        }

        public void setIndent(int amount)
        {
            _indent = amount;
            _indentStr.setLength(0);
            for (int idx = 0; idx < amount; idx++)
            {
                // using the indent token of the printer
                _indentStr.append(_options.getIndentToken());
            }
        }

        public void setPadding(int amount)
        {
            _padding = amount;
            _paddingStr.setLength(0);
            for (int idx = 0; idx < amount; idx++)
            {
                _paddingStr.append(' ');
            }
        }

        public int getIndentLength()
        {
            return _indentStr.length() + _padding;
        }

        public void setStyle(String style)
        {
            if ((style != null) && !_curStyle.equalsIgnoreCase(style))
            {
                if (_texts == null)
                {
                    _writer.stopStyle(_curStyle);
                    _writer.startStyle(style);
                }
                else
                {
                    _texts.addElement(new StyledText(style));
                }
                _curStyle = style;
            }
        }

        public String getStyle()
        {
            return _curStyle;
        }

        public int getIndent()
        {
            return _indent;
        }
        public int getMaxColumn()
        {
            return (_maxColumn > 0 ? _maxColumn : getPosition().getColumn());
        }
        // Commands the printer state to measure the maximum
        // column used at the next newline
        public void doGetMaxColumn()
        {
            _getMaxColumn = true;
            _maxColumn    = 0;
        }
        public boolean isIndenting()
        {
            return _doIndent;
        }
        public void doIndent(boolean doIt)
        {
            _doIndent = doIt;
            if (_isNew && (_texts != null) && !doIt)
            {
                // we have to remove the indent from the position
                _pos.addColumn(-_padding);
                _pos.addColumn(-_indentStr.length());
                _isNew = false;
            }
        }

        public char getLastChar()
        {
            return _lastChar;
        }

        public int getPadding()
        {
            return _padding;
        }

        // gives the position where the next text would be printed
        // this is different from the current position if we are
        // at the beginning of a new line (due to the indent)
        public ParsePosition getPosition()
        {
            // we have to include the indent
            // if we're at the beginning of a new line
            int ofs = (_isNL && _doIndent ? _indentStr.length() + _padding : 0);

            return new ParsePosition(_pos.getLine(),
                                     _pos.getColumn() + ofs,
                                     _pos.getAbsolute() + ofs);
        }

        public boolean isNewLine()
        {
            return _isNL;
        }

        public void print(String text, boolean asIs)
        {
            int prev = -1;

            if (!asIs)
            {
                int next = text.indexOf('\n');

                if (next >= 0)
                {
                    prev = 0;
                    while (next >= 0)
                    {
                        if (prev < next)
                        {
                            printLine(text.substring(prev, next));
                        }
                        printLine("\n");
                        _pos.incLine();
                        _isNL = true;

                        prev  = next;
                        next  = text.indexOf('\n', prev+1);
                    }
                }
            }
            if (prev < text.length() - 1)
            {
                printLine(text.substring(prev+1));
            }
        }

        public void append(TryState state)
        {
            StyledText styledText;
            String     text;

            for (Enumeration en = state._texts.elements(); en.hasMoreElements();)
            {
                styledText = (StyledText)en.nextElement();
                text       = styledText.toString();
                if (text.length() > 0)
                {
                    setStyle(styledText.getStyle());
                    printLine(text);
                }
            }
            _pos  = state._pos;
            _isNL = state._isNL;
            // we do not inherit styles
            setStyle(Style.NORMAL);
        }

        private void printLine(String text)
        {
            if (text.equals("\n"))
            {
                if (_isNew && (_texts != null) && _doIndent)
                {
                    // we have to remove the indent from the position
                    _pos.addColumn(-_padding);
                    _pos.addColumn(-_indentStr.length());
                }
                if (_getMaxColumn)
                {
                    _maxColumn = _pos.getColumn();
                    // we only get the first one
                    _getMaxColumn = false;
                }
            }
            else
            {
                // we ignore the indent if this is a empty line
                if (_isNL && _doIndent)
                {
                    int len = getIndentLength();

                    if (len > 0)
                    {
                        _pos.addColumn(len);
                        // indent is always normal
                        String curStyle = _curStyle;

                        setStyle(Style.NORMAL);
                        write(_indentStr.toString());
                        write(_paddingStr.toString());
                        setStyle(curStyle);
                    }
                }
            }
            _isNL  = false;
            _isNew = false;
            _pos.addColumn(text.length());
            write(text);
        }

        private void write(String text)
        {
            if (_texts == null)
            {
                _writer.write(text);
            }
            else
            {
                getStyledText().append(text);
            }
            if (text.length() > 0)
            {
                _lastChar = text.charAt(text.length() - 1);
            }
        }
    }

    private StyleWriter _writer;
    private Options     _options;
    private Vector      _tryLevels   = new Vector();

    public IndentPrinter(StyleWriter writer, Options options)
    {
        _writer  = writer;
        _options = options;
        pushState();
    }



    public void decIndent(int amount)
    {
        getState().setIndent(getState().getIndent() - amount);
    }



    public void doGetMaxColumn()
    {
        getState().doGetMaxColumn();
    }



    public void doIndent(boolean doIt)
    {
        getState().doIndent(doIt);
    }



    public int getIndent()
    {
        return getState().getIndent();
    }



    public int getIndentLength()
    {
        return getState().getIndentLength();
    }



    public char getLastChar()
    {
        return getState().getLastChar();
    }



    public int getMaxColumn()
    {
        return getState().getMaxColumn();
    }



    public Options getOptions()
    {
        return _options;
    }



    public int getPadding()
    {
        return getState().getPadding();
    }



    public ParsePosition getPosition()
    {
        return getState().getPosition();
    }



    private TryState getState()
    {
        return _tryLevels.isEmpty() ?
                   null :
                   (TryState)_tryLevels.firstElement();
    }



    public void incIndent(int amount)
    {
        getState().setIndent(getState().getIndent() + amount);
    }



    public boolean isNewLine()
    {
        return getState().isNewLine();
    }



    private void pushState()
    {
        _tryLevels.insertElementAt(new TryState(getState()), 0);
    }



    public void setPadding(int amount)
    {
        getState().setPadding(amount);
    }



    public void startStyle(String name)
    {
        getState().setStyle(name);
    }



    public void startTryMode()
    {
        pushState();
    }



    public void stopStyle(String name)
    {
        getState().setStyle(Style.NORMAL);
    }



    public void stopTryMode(boolean makePermanent)
    {
        TryState state = getState();

        _tryLevels.removeElementAt(0);
        if (makePermanent)
        {
            getState().append(state);
        }
    }



    public void write(String text)
    {
        getState().print(text, false);
    }



    public void writeln()
    {
        getState().print("\n", false);
    }



    public void writeln(String text)
    {
        write(text);
        writeln();
    }
}