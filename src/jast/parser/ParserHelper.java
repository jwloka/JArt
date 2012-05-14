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
import jast.Global;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.ParsePosition;
import jast.ast.Project;
import jast.ast.nodes.Comment;
import jast.helpers.SortedArray;

public class ParserHelper extends ScopeStack
{
    private String          _unitName;
    private Project         _project;
    private boolean         _parseInterfaceOnly = false;
    private NodeFactory     _factory            = Global.getFactory();
    private boolean         _ignoreComments     = false;
    private SortedArray     _comments           = new SortedArray();
    private int             _position           = 1;

    public ParserHelper()
    {
        init();
    }

    public void addComment(String  text,
                           boolean isBlockComment,
                           int     line,
                           int     column,
                           int     absolute)
    {
        if (_ignoreComments)
        {
            return;
        }

        Comment comment     = _factory.createComment(text, isBlockComment);
        int     endLine     = line;
        int     endColumn   = column;
        int     endAbsolute = absolute + text.length();

        comment.setStartPosition(new ParsePosition(line, column, absolute));
        // Comments will not have a correct finish position until the lexer
        // is heavily modified; the current approach simply counts
        // the newlines and following characters
        if (text.indexOf('\n') >= 0)
        {
            int pos = text.indexOf('\n');
            while (pos >= 0)
            {
                endLine++;
                pos = text.indexOf('\n', pos+1);
            }
            // We start at column 1 !
            endColumn = text.length() - text.lastIndexOf('\n') - 1;
            if (isBlockComment)
            {
                endColumn   += 2;    // for "*/"
                endAbsolute += 2;
            }
        }
        else
        {
            // Adding 2 for "//" or "/ *"
            endColumn = column + text.length() + 1;
            if (isBlockComment)
            {
                endColumn   += 2;    // for "*/"
                endAbsolute += 2;
            }
        }
        comment.setFinishPosition(new ParsePosition(endLine, endColumn, endAbsolute));
        _comments.add(comment.getStartPosition(), comment);
    }

    public void comment(Node node)
    {
        if (!_ignoreComments)
        {
            (new CommentingVisitor()).comment(node, _comments);
        }
        _comments.clear();
    }

    public NodeFactory getNodeFactory()
    {
        return _factory;
    }

    public Project getProject()
    {
        return _project;
    }

    public String getUnitName()
    {
        return _unitName;
    }

    public void ignoreComments(boolean ignoreThem)
    {
        _ignoreComments = ignoreThem;
    }

    public void init()
    {
        _position = 1;
        _unitName = null;
        _project  = null;
        clear();
        _comments.clear();
    }

    public boolean isParsingInterfaceOnly()
    {
        return _parseInterfaceOnly;
    }

    public void removeComments(ParsePosition start, ParsePosition finish)
    {
        if ((_comments == null) || _comments.isEmpty())
        {
            return;
        }

        ParsePosition commentStart;
        int           idx = 0;

        while (idx < _comments.getCount())
        {
            commentStart = (ParsePosition)_comments.getKey(idx);
            if (start.isLowerOrEqual(commentStart) &&
                commentStart.isLowerOrEqual(finish))
            {
                _comments.remove(idx);
            }
            else
            {
                idx++;
            }
        }
    }

    public void setNodeFactory(NodeFactory factory)
    {
        if (factory != null)
        {
            _factory = factory;
        }
    }

    public void setParsingInterface(boolean onlyInterface)
    {
        _parseInterfaceOnly = onlyInterface;
    }

    public void setProject(Project project)
    {
        _project = project;
    }

    public void setUnitName(String name)
    {
        _unitName = name;
    }
}
