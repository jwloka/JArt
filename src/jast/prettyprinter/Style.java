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
public interface Style
{
    public static final String NORMAL                 = "normal";
    public static final String KEYWORD                = "keyword";
    public static final String MODIFIER               = "modifier";
    public static final String LABEL                  = "label";
    public static final String TYPE_IDENTIFIER        = "typeidentifier";
    public static final String FIELD_IDENTIFIER       = "fieldidentifier";
    public static final String CONSTRUCTOR_IDENTIFIER = "constructoridentifier";
    public static final String METHOD_IDENTIFIER      = "constructoridentifier";
    public static final String VARIABLE_IDENTIFIER    = "variableidentifier";
    public static final String LINE_COMMENT           = "linecomment";
    public static final String BLOCK_COMMENT          = "blockcomment";
    public static final String JAVADOC_COMMENT        = "javadoccomment";
    public static final String CHARACTER_LITERAL      = "characterliteral";
    public static final String STRING_LITERAL         = "stringliteral";
    public static final String NUMBER_LITERAL         = "numberliteral";
    public static final String BOOLEAN_LITERAL        = "booleanliteral";
    public static final String NULL_LITERAL           = "nullliteral";
    public static final String OPERATOR               = "operator";
    public static final String UNRESOLVED_ACCESS      = "unresolvedaccess";
}
