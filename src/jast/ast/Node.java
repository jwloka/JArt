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
package jast.ast;
import jast.ast.nodes.Comment;
import jast.ast.nodes.collections.CommentArray;
import jast.ast.nodes.collections.CommentArrayImpl;
import jast.ast.visitor.Acceptor;
import jast.helpers.PropertySet;

import java.io.Serializable;

public abstract class Node implements Serializable, Acceptor
{
    private ParsePosition _startPos  = new ParsePosition();
    private ParsePosition _finishPos = new ParsePosition();
    private CommentArray  _comments;
    private PropertySet   _properties;

    public void addComment(Comment comment)
    {
        getComments().add(comment);
    }

    public void addComments(CommentArray comments)
    {
        getComments().copyFrom(comments, false);
    }

    public boolean equals(Object otherNode)
    {
        if (otherNode instanceof Node)
        {
            return (getIdentifier().equals(((Node)otherNode).getIdentifier()));
        }
        else
        {
            return false;
        }
    }

    public CommentArray getComments()
    {
        if (_comments == null)
        {
            _comments = new CommentArrayImpl();
        }
        return _comments;
    }

    public ParsePosition getFinishPosition()
    {
        return _finishPos;
    }

    private String getIdentifier()
    {
        // This is the original toStrigng implementation from
        // java.lang.Object
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public PropertySet getProperties()
    {
        return _properties;
    }

    public Object getProperty(String name)
    {
        return (_properties == null ? null : _properties.getProperty(name));
    }

    public ParsePosition getStartPosition()
    {
        return _startPos;
    }

    public boolean hasComments()
    {
        return (_comments != null) && !_comments.isEmpty();
    }

    public boolean hasProperty(String name)
    {
        return (_properties == null ?
                    false :
                    _properties.getProperty(name) != null);
    }

    public void notify(ContainerEvent event)
    {
    }

    public void setComments(CommentArray comments)
    {
        getComments().copyFrom(comments, true);
    }

    public void setFinishPosition(ParsePosition pos)
    {
        _finishPos.copyFrom(pos);
    }

    public void setProperty(String name, Object value)
    {
        if (_properties == null)
        {
            _properties = new PropertySet();
        }
        _properties.setProperty(name, value);
    }

    public void setStartPosition(ParsePosition pos)
    {
        _startPos.copyFrom(pos);
    }
}
