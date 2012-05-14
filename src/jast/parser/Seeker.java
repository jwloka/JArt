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
import jast.ast.ParsePosition;

import java.io.IOException;
import java.io.Reader;

// The sole purpose of this class is to advance in the
// input stream as given by a unicode reader or similar
public class Seeker
{

    public static int advance(Reader input, ParsePosition curPos, ParsePosition targetPos) throws IOException
    {
        int curLine      = curPos.getLine();
        int curColumn    = curPos.getColumn();
        int targetLine   = targetPos.getLine();
        int targetColumn = targetPos.getColumn();
        int absolute     = 0;
        int c;

        while ((curLine < targetLine) || (curColumn < targetColumn))
        {
            c = input.read();
            switch (c)
            {
                case 10:
                    curLine++;
                    curColumn = 1;
                    break;
                default:
                    curColumn++;
                    break;
            }
            absolute++;
        }
        return absolute;
    }
}
