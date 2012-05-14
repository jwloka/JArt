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
import jast.BasicMessageContainer;
import jast.ast.ContainedNode;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FeatureWithBody;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.collections.BlockStatementIterator;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.DescendingVisitor;

import java.util.Vector;

public class NameChecker extends DescendingVisitor
{
    private BasicMessageContainer _msgReceiver;
    private VariableDeclaration   _decl;
    private String                _name;
    private Block                 _block;
    private int                   _pos = -1;
    private Vector                _contexts = new Vector();

    public NameChecker(BasicMessageContainer msgReceiver,
                       Block                 newDeclContainer,
                       int                   newDeclPos)
    {
        _msgReceiver = msgReceiver;
        _block       = newDeclContainer;
        _pos         = newDeclPos;
    }

    public NameChecker(BasicMessageContainer msgReceiver,
                       VariableDeclaration   renamingTarget)
    {
        _msgReceiver = msgReceiver;
        _decl        = renamingTarget;
    }

    public boolean check(String newName)
    {
        // We first go to the outermost invocable definition
        // (recognizing local classes)
        FeatureWithBody decl    = null;
        Node            current = (_decl != null ? (Node)_decl : (Node)_block);
        Node            parent  = null;

        while (current != null)
        {
            parent = ((ContainedNode)current).getContainer();
            if (parent == null)
            {
                _msgReceiver.addFatalError("Invalid AST",
                                           current);
                return false;
            }
            if (current instanceof FeatureWithBody)
            {
                if ((parent instanceof ClassDeclaration) &&
                    ((ClassDeclaration)parent).isLocal())
                {
                    decl = null;
                }
                else
                {
                    decl = (FeatureWithBody)current;
                    if (!((TypeDeclaration)parent).isInner() &&
                        !((TypeDeclaration)parent).isNested())
                    {
                        break;
                    }
                }
            }
            current = parent;
        }
        if (decl == null)
        {
            _msgReceiver.addFatalError("Invalid AST",
                                       null);
            return false;
        }

        // From there we check the name
        _name = newName;

        // We check for fields in the owning type decl
        // (we currently exclude inherited fields)
        isFieldDefined((TypeDeclaration)parent);
        try
        {
            ((Node)decl).accept(this);

        }
        catch (RuntimeException ex)
        {
            // We use the exception to back out immediately if an error
            // has been found
        }
        return !_msgReceiver.hasErrors() &&
               !_msgReceiver.hasFatalErrors();
    }

    private Context getContext()
    {
        return (Context)_contexts.firstElement();
    }

    private boolean isFieldDefined(TypeDeclaration typeDecl)
    {
        if (typeDecl == null)
        {
            return false;
        }

        FieldDeclaration field = typeDecl.getFields().get(_name);
        Type             type  = null;

        if (field != null)
        {
            _msgReceiver.addWarning("There is a field with the same name",
                                    field);
            return true;
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            type = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((type != null) && isFieldDefined(type.getReferenceBaseTypeDecl()))
            {
                return true;
            }
        }
        for (TypeIterator it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            type = it.getNext();
            if (isFieldDefined(type.getReferenceBaseTypeDecl()))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isInDeclScope()
    {
        for (int idx = 0; idx < _contexts.size(); idx++)
        {
            if (((Context)_contexts.elementAt(idx)).containsDecl())
            {
                return true;
            }
        }
        return false;
    }

    private void popContext()
    {
        getContext().clear();
        _contexts.removeElementAt(0);
    }

    private void pushContext()
    {
        _contexts.insertElementAt(new Context(_name, _decl, _msgReceiver), 0);
    }

    public void visitBlock(Block block)
    {
        getContext().incLevel();

        boolean isTargetBlock = (block == _block);
        int     pos = 0;

        if (isTargetBlock && (pos == _pos))
        {
            getContext().forceEncounter();
        }
        for (BlockStatementIterator it = block.getBlockStatements().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            if (isTargetBlock)
            {
                pos++;
                if (pos == _pos)
                {
                    getContext().forceEncounter();
                }
            }
        }
        getContext().decLevel();
    }

    public void visitCaseBlock(CaseBlock block)
    {
        for (ExpressionIterator exprIt = block.getCases().getIterator(); exprIt.hasNext();)
        {
            exprIt.getNext().accept(this);
        }

        boolean isTargetBlock = (block == _block);
        int     pos = 0;

        if (isTargetBlock && (pos == _pos))
        {
            getContext().forceEncounter();
        }
        for (BlockStatementIterator it = block.getBlockStatements().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            if (isTargetBlock)
            {
                pos++;
                if (pos == _pos)
                {
                    getContext().forceEncounter();
                }
            }
        }
    }

    public void visitConstructorDeclaration(ConstructorDeclaration decl)
    {
        pushContext();
        super.visitConstructorDeclaration(decl);
        popContext();
    }

    public void visitFieldAccess(FieldAccess access)
    {
        if (!access.isTrailing() && _name.equals(access.getFieldName()))
        {
            // a field with that name is accessed
            // it is an error if it is within the context of our variable
            if (isInDeclScope())
            {
                _msgReceiver.addError("A local field of that name is used within the context of the variable which shall be renamed",
                                      access);
                throw new RuntimeException();
            }
        }
        super.visitFieldAccess(access);
    }

    public void visitFieldDeclaration(FieldDeclaration decl)
    {
        if (_name.equals(decl.getName()))
        {
            _msgReceiver.addWarning("A local class declares a field with that name",
                                    decl);
        }
        pushContext();
        getContext().check(decl);
        super.visitFieldDeclaration(decl);
        popContext();
    }

    public void visitFormalParameter(FormalParameter param)
    {
        getContext().check(param);
    }

    public void visitForStatement(ForStatement stmt)
    {
        // the for statement has its own block scope which includes
        // the init declaration/list, condition and update list
        getContext().incLevel();
        super.visitForStatement(stmt);
        getContext().decLevel();
    }

    public void visitInitializer(Initializer initializer)
    {
        pushContext();
        super.visitInitializer(initializer);
        popContext();
    }

    public void visitLocalVariableDeclaration(LocalVariableDeclaration varDecl)
    {
        getContext().check(varDecl);
        super.visitLocalVariableDeclaration(varDecl);
    }

    public void visitMethodDeclaration(MethodDeclaration decl)
    {
        pushContext();
        super.visitMethodDeclaration(decl);
        popContext();
    }

    public void visitSwitchStatement(SwitchStatement stmt)
    {
        // switch statements have their own block scope in which
        // all case blocks operate
        getContext().incLevel();
        super.visitSwitchStatement(stmt);
        getContext().decLevel();
    }

    public void visitVariableAccess(VariableAccess varAccess)
    {
        getContext().check(varAccess);
    }
}
