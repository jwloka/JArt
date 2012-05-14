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

import jast.SyntaxException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.BlockStatement;
import jast.ast.nodes.Feature;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.BlockStatementArray;
import jast.ast.nodes.collections.VariableDeclarationArray;
import jast.ast.nodes.collections.VariableDeclarationArrayImpl;
import jast.ast.nodes.collections.VariableDeclarationIterator;
import jast.ast.visitor.Visitor;

// Helper class used to parse variable declarations (fields, local
// variables) and to convert them to actual declarations and add
// them to the owners (block, type declaration, for statement)
public class MultipleVariableDeclaration extends    Node
                                         implements Feature, BlockStatement
{
    private VariableDeclarationArray _decls = new VariableDeclarationArrayImpl();

    public void accept(Visitor visitor)
    {}

    public void addTo(Block block)
    {
        modifyDecls();

        BlockStatementArray stmts = block.getBlockStatements();

        for (VariableDeclarationIterator it = _decls.getIterator(); it.hasNext();)
        {
            stmts.add((LocalVariableDeclaration)it.getNext());
        }
    }

    public void addTo(ForStatement forStmt)
    {
        modifyDecls();

        for (VariableDeclarationIterator it = _decls.getIterator(); it.hasNext();)
        {
            forStmt.addInitDeclaration((LocalVariableDeclaration)it.getNext());
        }
    }

    public void addTo(TypeDeclaration typeDecl) throws SyntaxException
    {
        modifyDecls();

        for (VariableDeclarationIterator it = _decls.getIterator(); it.hasNext();)
        {
            typeDecl.getFields().add((FieldDeclaration)it.getNext());
        }
    }

    public Node getContainer()
    {
        return null;
    }

    public VariableDeclarationArray getDeclarators()
    {
        return _decls;
    }

    private void modifyDecls()
    {
        // Only the first declaration will span the modifiers and type
        // (in terms of the parse positions)
        _decls.get(0).setStartPosition(getStartPosition());
    }

    public void setContainer(Node container)
    {
    }
}
