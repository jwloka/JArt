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

import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;

public class CommentFilter implements TokenStream
{
    private TokenStream  _input;
    private ParserHelper _helper;

    public CommentFilter(TokenStream input, ParserHelper helper)
    {
        _input  = input;
        _helper = helper;
    }

    public Token nextToken() throws TokenStreamException
    {
        Token tok = _input.nextToken();

        while (tok != null)
        {
            if ((tok.getType() == JavaTokenTypes.BLOCK_COMMENT) ||
                (tok.getType() == JavaTokenTypes.LINE_COMMENT))
            {
                _helper.addComment(tok.getText(),
                                   tok.getType() == JavaTokenTypes.BLOCK_COMMENT,
                                   tok.getLine(),
                                   tok.getColumn(),
                                   tok instanceof ExtendedToken ? ((ExtendedToken)tok).getProcessedPosition() : 0);
                // System.out.println("[CommentFilter::nextToken] Comment in line "+tok.getLine());
                tok = _input.nextToken();
            }
            else
            {
                break;
            }
        }
        return tok;
    }
}
