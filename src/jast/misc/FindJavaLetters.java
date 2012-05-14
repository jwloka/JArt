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
package jast.misc;

/**
 * Test class used to determine all character codes who are
 * letters accrding to java.
 */
public class FindJavaLetters
{

    /**
     * Finds all characters (more precisely their unicode codes) which can
     * be in a java identifier but not as the first character.
     *
     * @return The list of codes
     */
    public static String getIdentifierNotStartChars()
    {
        StringBuffer result = new StringBuffer();
        long firstCode = -1l;
        long num = 0;
        String unicode;
        for (long code = 0l; code < 0xFFFFl; code++)
        {
            if (!Character.isJavaIdentifierStart((char) code) && Character.isJavaIdentifierPart((char) code))
            {
                if (firstCode < 0l)
                {
                    firstCode = code;
                }
            }
            else
                if (firstCode >= 0l)
                {
                    result.append(num > 0 ? "| " : "  ");
                    if (code - firstCode > 1)
                    {
                        unicode = Long.toHexString(firstCode);
                        while (unicode.length() < 4)
                        {
                            unicode = "0" + unicode;
                        }
                        result.append("'\\u");
                        result.append(unicode);
                        result.append("'..");
                    }
                    unicode = Long.toHexString(code - 1);
                    while (unicode.length() < 4)
                    {
                        unicode = "0" + unicode;
                    }
                    result.append("'\\u");
                    result.append(unicode);
                    result.append("'\n");
                    firstCode = -1l;
                    num++;
                }
        }
        result.append("\nnum = ");
        result.append(num);
        result.append("\n");
        return result.toString();
    }

    /**
     * Finds all characters (more precisely their unicode codes) which can
     * start a java identifier.
     *
     * @return The list of codes
     */
    public static String getIdentifierStartChars()
    {
        StringBuffer result = new StringBuffer();
        long firstCode = -1l;
        long num = 0;
        String unicode;
        for (long code = 0l; code < 0xFFFFl; code++)
        {
            if (Character.isJavaIdentifierStart((char) code))
            {
                if (firstCode < 0l)
                {
                    firstCode = code;
                }
            }
            else
                if (firstCode >= 0l)
                {
                    result.append(num > 0 ? "| " : "  ");
                    if (code - firstCode > 1)
                    {
                        unicode = Long.toHexString(firstCode);
                        while (unicode.length() < 4)
                        {
                            unicode = "0" + unicode;
                        }
                        result.append("'\\u");
                        result.append(unicode);
                        result.append("'..");
                    }
                    unicode = Long.toHexString(code - 1);
                    while (unicode.length() < 4)
                    {
                        unicode = "0" + unicode;
                    }
                    result.append("'\\u");
                    result.append(unicode);
                    result.append("'\n");
                    firstCode = -1l;
                    num++;
                }
        }
        result.append("\nnum = ");
        result.append(num);
        result.append("\n");
        return result.toString();
    }

    /**
     *
     * @param args java.lang.String[]
     */
    public static void main(String[] args)
    {
        System.out.println(getIdentifierStartChars());
    }
}
