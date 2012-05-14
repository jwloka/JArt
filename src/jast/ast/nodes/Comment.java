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
package jast.ast.nodes;
import jast.ast.Node;
import jast.ast.visitor.Visitor;
import jast.helpers.StringArray;

import java.util.StringTokenizer;

public class Comment extends Node
{
    private String      _text;
    private StringArray _tags;
    private boolean     _isBlockComment;

    public Comment(String text, boolean isBlockComment)
    {
        _isBlockComment = isBlockComment;
        parseCommentText(text);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitComment(this);
    }

    public StringArray getTags()
    {
        return _tags;
    }

    public String getText()
    {
        return _text;
    }

    public boolean isBlockComment()
    {
        return _isBlockComment;
    }

    public boolean isJavaDocComment()
    {
        return (_tags != null);
    }

    private void parseCommentText(String text)
    {
        if (_isBlockComment)
        {
            boolean withTags = text.startsWith("*");

            _text = withTags ? text.substring(1) : text ;

            // First we remove all '*' and unnecessary whitespace
            // (before the '*' or at the end of the line)
            StringBuffer    parsedText  = new StringBuffer();
            StringBuffer    parsedTags  = new StringBuffer();
            StringTokenizer tokenizer   = new StringTokenizer(_text, "\n");
            boolean         parsingTags  = false;
            String          token;

            while (tokenizer.hasMoreTokens())
            {
                token = tokenizer.nextToken().trim();
                if (token.startsWith("*"))
                {
                    token = token.substring(1).trim();
                }
                if (token.length() > 0)
                {
                    if (!parsingTags && withTags && token.startsWith("@"))
                    {
                        parsingTags = true;
                    }
                }
                if (parsingTags)
                {
                    if (parsedTags.length() > 0)
                    {
                        parsedTags.append('\n');
                    }
                    if (token.length() > 0)
                    {
                        parsedTags.append(token);
                    }
                }
                else
                {
                    if (parsedText.length() > 0)
                    {
                        parsedText.append('\n');
                    }
                    if (token.length() > 0)
                    {
                        parsedText.append(token);
                    }
                }
            }
            _text = parsedText.toString().trim();

            if (withTags)
            {
                _tags = new StringArray();

                // Now we see whether there are some tags
                if (parsedTags.length() > 0)
                {
                    String tagText = parsedTags.toString();
                    String tag;
                    int    prev = 0;
                    int    next;

                    while ((next = tagText.indexOf("@", prev)) >= 0)
                    {
                        tag = tagText.substring(prev, next).trim();
                        if (tag.length() > 0)
                        {
                            _tags.add(tag);
                        }
                        prev = next + 1;
                    }
                    tag = tagText.substring(prev).trim();
                    if (tag.length() > 0)
                    {
                        _tags.add(tag);
                    }
                }
            }
        }
        else
        {
            _text = text.trim();
        }
    }
}
