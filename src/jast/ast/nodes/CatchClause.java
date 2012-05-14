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
import jast.SyntaxException;
import jast.ast.ContainedNode;
import jast.ast.visitor.Visitor;

public class CatchClause extends ContainedNode
{
    private FormalParameter _param;
    private Block             _block;

    public CatchClause(FormalParameter param, Block block) throws SyntaxException
    {
        Type varType = param.getType();

        if ((varType == null) ||
            varType.isPrimitive() ||
            (varType.getDimensions() > 0))
        {
            throw new SyntaxException("The catch clause parameter is of an invalid type (" + varType.toString() + ")");
        }
        setFormalParameter(param);
        setCatchBlock(block);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitCatchClause(this);
    }

    public Block getCatchBlock()
    {
        return _block;
    }

    public FormalParameter getFormalParameter()
    {
        return _param;
    }

    public void setCatchBlock(Block block)
    {
        _block = block;
        _block.setContainer(this);
    }

    public void setFormalParameter(FormalParameter param)
    {
        _param = param;
        _param.setContainer(this);
    }
}
