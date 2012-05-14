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
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableDeclaration;

import java.util.Vector;

public class ScopeStack
{
    private Vector _scopes = new Vector();

    public ScopeStack()
    {
    }

    public void clear()
    {
        _scopes.removeAllElements();
    }

    public void defineLabel(LabeledStatement label)
    {
        topBlockScope().defineLabel(label);
    }

    public void defineLocalClass(ClassDeclaration classDecl)
    {
        topBlockScope().defineLocalClass(classDecl);
    }

    public void defineVariable(VariableDeclaration decl)
    {
        if (decl instanceof FieldDeclaration)
        {
            topTypeScope().defineField((FieldDeclaration)decl);
        }
        else
        {
            topBlockScope().defineVariable((LocalVariableDeclaration)decl);
        }
    }

    public void deleteLabel(String name)
    {
        Scope curScope = null;

        for (ScopeIterator it = getBlockIterator(); it.hasNext();)
        {
            curScope = it.getNext();
            if (curScope.resolveLabel(name) != null)
            {
                curScope.deleteLabel(name);
                return;
            }
        }
    }

    public ScopeIterator getBlockIterator()
    {
        return new ScopeIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < _scopes.size()) &&
                       !((Scope)_scopes.elementAt(curIdx)).isTypeScope();
            }

            public Scope getNext()
            {
                return (Scope)_scopes.elementAt(curIdx++);
            }
        };
    }

    public ScopeIterator getIterator()
    {
        return new ScopeIterator()
        {
            private int curIdx = 0;

            public boolean hasNext()
            {
                return (curIdx < _scopes.size());
            }

            public Scope getNext()
            {
                return (Scope)_scopes.elementAt(curIdx++);
            }
        };
    }

    public boolean isEmpty()
    {
        return _scopes.isEmpty();
    }

    public boolean isInLocalClass()
    {
        Scope           curScope;
        TypeDeclaration typeDecl;

        for (ScopeIterator it = getIterator(); it.hasNext();)
        {
            curScope = it.getNext();
            if (curScope.isTypeScope())
            {
                typeDecl = curScope.getTypeDeclaration();
                if (typeDecl instanceof ClassDeclaration)
                {
                    if (((ClassDeclaration)typeDecl).isLocal())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void popBlockScope()
    {
        if (!_scopes.isEmpty())
        {
            _scopes.removeElementAt(0);
        }
    }

    public void popTypeScope()
    {
        if (!_scopes.isEmpty())
        {
            _scopes.removeElementAt(0);
        }
    }

    public void pushBlockScope()
    {
        _scopes.insertElementAt(new Scope(), 0);
    }

    public void pushTypeScope(TypeDeclaration typeDecl)
    {
        _scopes.insertElementAt(new Scope(typeDecl), 0);
    }

    public FieldDeclaration resolveField(String name)
    {
        return isEmpty() ? null : topTypeScope().resolveField(name);
    }

    public LabeledStatement resolveLabel(String name)
    {
        LabeledStatement result = null;

        for (ScopeIterator it = getBlockIterator(); it.hasNext();)
        {
            result = it.getNext().resolveLabel(name);
            if (result != null)
            {
                break;
            }
        }
        return result;
    }

    public ClassDeclaration resolveLocalClass(String name)
    {
        ClassDeclaration result = null;

        for (ScopeIterator it = getBlockIterator(); it.hasNext();)
        {
            result = it.getNext().resolveLocalClass(name);
            if (result != null)
            {
                break;
            }
        }
        return result;
    }

    public LocalVariableDeclaration resolveLocalVariable(String name)
    {
        LocalVariableDeclaration result = null;

        for (ScopeIterator it = getBlockIterator(); it.hasNext();)
        {
            result = it.getNext().resolveVariable(name);
            if (result != null)
            {
                break;
            }
        }
        return result;
    }

    public Scope topBlockScope()
    {
        Scope result = (Scope)_scopes.firstElement();

        return result.isTypeScope() ? null : result;
    }

    public Scope topTypeScope()
    {
        Scope curScope = null;

        for (ScopeIterator it = getIterator(); it.hasNext();)
        {
            curScope = it.getNext();
            if (curScope.isTypeScope())
            {
                return curScope;
            }
        }
        return null;
    }


    public TypeDeclaration getCurrentType()
    {
        Scope typeScope = topTypeScope();
        return typeScope == null ? null : typeScope.getTypeDeclaration();
    }
}
