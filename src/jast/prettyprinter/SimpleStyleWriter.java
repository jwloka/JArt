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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class SimpleStyleWriter implements StyleWriter
{
    private PrintWriter _output;

    public SimpleStyleWriter()
    {
        setOutput(System.out);
    }

    public SimpleStyleWriter(OutputStream output)
    {
        setOutput(output);
    }

    public SimpleStyleWriter(Writer output)
    {
        setOutput(output);
    }

    public void setOutput(OutputStream output)
    {
        _output = new PrintWriter(output);
    }

    public void setOutput(Writer output)
    {
        _output = new PrintWriter(output);
    }

    public void startStyle(String name)
    {
    }

    public void stopStyle(String name)
    {
    }

    public void write(String text)
    {
        _output.write(text);
        _output.flush();
    }
}
