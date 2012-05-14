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
package jart.restructuring.variablelevel;
import jast.MessageReceiver;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;

public class Context
{
    private String              _name;
    private MessageReceiver     _msgReceiver;
    private VariableDeclaration _decl;
    private VariableDeclaration _found       = null;
    private boolean             _foundTarget = false;
    private boolean             _foundOther  = false;
    private int                 _foundLevel  = 0;
    private int                 _curLevel    = 0;

    public Context(String name, VariableDeclaration decl, MessageReceiver msgReceiver)
    {
        _name        = name;
        _decl        = decl;
        _msgReceiver = msgReceiver;
    }

    public void check(VariableAccess access)
    {
        if ((access.getVariableDeclaration() == _decl) && (_decl != null))
        {
            // an access to the variable that we want to rename within the
            // scope of another variable of that name
            if (_foundOther)
            {
                _msgReceiver.addError("The variable is used within the context of another variable with that name",
                                      _found);
                throw new RuntimeException();
            }
        }
        else if (_name.equals(access.getVariableName()))
        {
            // another variable with that name is accessed
            if (_foundTarget)
            {
                _msgReceiver.addError("Another variable of that name is used within the context",
                                      access);
                throw new RuntimeException();
            }
        }
    }

    public void check(VariableDeclaration decl)
    {
        // did we encounter one variable of that name before ?
        if (_name.equals(decl.getName()) || (decl == _decl))
        {
            if (!_foundOther && !_foundTarget)
            {
                if (decl == _decl)
                {
                    _foundTarget = true;
                }
                else
                {
                    _foundOther = true;
                }
                _found = decl;
                // We increase the level for formal parameters as
                // they are only interesting in the following block
                // (method body or catch block)
                _foundLevel = _curLevel +
                              (decl instanceof FormalParameter ? 1 : 0);
            }
            else
            {
                // if the variable has the same name or is the one
                // which we shall rename than we have a problem
                _msgReceiver.addError("A variable with that name is already defined",
                                      _found == _decl ? decl : _found);
                throw new RuntimeException();
            }
        }
    }

    public void clear()
    {
        if (_foundOther &&
            (_found instanceof LocalVariableDeclaration))
        {
            _msgReceiver.addError("A variable with that name is already defined",
                                  _found);
            throw new RuntimeException();
        }
    }

    public boolean containsDecl()
    {
        return _foundTarget;
    }

    public void decLevel()
    {
        if ((_foundTarget || _foundOther) &&
            (_foundLevel >= _curLevel))
        {
            if (_foundOther)
            {
                // there has been a variable declaration with the same name
                // but it made no problems; therefore we issue a warning
                _msgReceiver.addWarning("A variable with that name is already defined",
                                        _found);
            }
            _found       = null;
            _foundTarget = false;
            _foundOther  = false;
            _foundLevel  = 0;
        }
        _curLevel--;
    }

    public void forceEncounter()
    {
        if (_found != null)
        {
            _msgReceiver.addError("A variable with that name is already defined",
                                  _found);
            throw new RuntimeException();
        }
        else
        {
            _foundTarget = true;
            _foundLevel  = _curLevel;
        }
    }

    public void incLevel()
    {
        _curLevel++;
    }
}
