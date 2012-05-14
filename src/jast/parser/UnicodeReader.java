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
package jast.parser;
import java.io.IOException;
import java.io.Reader;

public class UnicodeReader extends Reader
{
    private Reader  _input;
    private boolean _wasSlash;
    private boolean _isEscaped;
    private boolean _artificialEOF;
    private long    _numChars;
    private char    _lastChar = '\0';

    public UnicodeReader(Reader input)
    {
        super();
        _input         = input;
        _wasSlash      = false;
        _artificialEOF = false;
        _numChars      = 0l;
    }

    public void close() throws IOException
    {
        _input.close();
        _wasSlash = false;
    }

    public long getNumReadChars()
    {
        return _numChars;
    }

    public void maintainUnicode(boolean doIt)
    {
        _isEscaped = doIt;
    }

    public int read(char cbuf[], int off, int len) throws IOException
    {
        int num = _input.read(cbuf, off, len);

        _artificialEOF = false;
        if (num > 0)
        {
            if (_isEscaped || _wasSlash || (cbuf[0] != '\\'))
            {
                if (cbuf[0] == '\uFFFF')
                {
                    cbuf[0] = (char)-1;
                    _lastChar = '\0';
                }
                else
                {
                    _lastChar = cbuf[0];
                }
                _wasSlash = false;
            }
            else
            {
                _input.mark(5);
                try
                {
                    int chr = _input.read();
                    if (chr == 'u')
                    {
                        int result = 0;
                        while (chr == 'u')
                        {
                            chr = _input.read();
                        }
                        //result = 4096 * Character.digit((char) chr, 16) + 256 * Character.digit((char) _input.read(), 16) + 16 * Character.digit((char) _input.read(), 16) + Character.digit((char) _input.read(), 16);
                        result = 4096 * Character.digit((char) chr, 16) + 256 * Character.digit((char) _input.read(), 16) + 16 * Character.digit((char) _input.read(), 16);
                        _lastChar = (char) _input.read();
                        result += Character.digit(_lastChar, 16);

                        cbuf[0] = (char) result;
                        // Checking for artificial (unicode-escaped) EOF
                        if (cbuf[0] == '\uFFFF')
                        {
                            // We ignoring such EOFs and read the next character
                            num            = read(cbuf, off, len);
                            _artificialEOF = true;
                        }
                        else
                        {
                            num = 1;
                        }
                    }
                    else
                    {
                        _wasSlash = true;
                        _lastChar = '\\';
                        _input.reset();
                    }
                }
                catch (IOException ex)
                {
                    _input.reset();
                }
            }
        }
        _numChars += num;

        return num;
    }

    public char getLast()
    {
        return _lastChar;
    }

    public boolean wasArtificialEOF()
    {
        return _artificialEOF;
    }
}
