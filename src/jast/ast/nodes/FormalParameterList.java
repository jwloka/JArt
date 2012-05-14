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
import jast.ast.ContainedNode;
import jast.ast.nodes.collections.ExpressionArray;
import jast.ast.nodes.collections.FormalParameterArray;
import jast.ast.nodes.collections.FormalParameterArrayImpl;
import jast.ast.visitor.Visitor;

public class FormalParameterList extends ContainedNode
{
    private FormalParameterArray _params = new FormalParameterArrayImpl();

    public FormalParameterList()
    {
        _params.setOwner(this);
    }

    public void accept(Visitor visitor)
    {
        visitor.visitFormalParameterList(this);
    }

    public boolean canBeAmbiguousTo(FormalParameterList other)
    {
        FormalParameterArray otherParams = other == null? null : other.getParameters();

        if ((_params == null) || _params.isEmpty())
        {
            return (otherParams == null) || otherParams.isEmpty();
        }
        if ((otherParams == null) || (_params.getCount() != otherParams.getCount()))
        {
            return false;
        }

        Type localParamType;
        Type otherParamType;

        for (int idx = 0; idx < _params.getCount(); idx++)
        {
            localParamType = _params.get(idx).getType();
            otherParamType = otherParams.get(idx).getType();

            if (localParamType.isPrimitive() && !localParamType.isArray())
            {
                if (!localParamType.isAssignmentCompatibleTo(otherParamType) &&
                    !otherParamType.isAssignmentCompatibleTo(localParamType))
                {
                    return false;
                }
            }
            else if (otherParamType.isPrimitive() && !otherParamType.isArray())
            {
                return false;
            }
        }
        return true;
    }

    public FormalParameterArray getParameters()
    {
        return _params;
    }

    public Type[] getParameterTypes()
    {
        Type[] result = new Type[_params.getCount()];

        for (int idx = 0; idx < result.length; idx++)
        {
            result[idx] = _params.get(idx).getType();
        }

        return result;
    }

    public String getSignature()
    {
        StringBuffer result = new StringBuffer();

        for (int idx = 0; idx < _params.getCount(); idx++)
            {
            if (idx > 0)
                {
                result.append(",");
            }
            result.append(_params.get(idx).getType().getSignature());
        }
        return result.toString();
    }

    // We assume that all types are already resolved
    // (in contrast to the similar method in the Resolver)
    public boolean isApplicableTo(ArgumentList args)
    {
        if (((args    == null) || (args.getArguments().getCount() == 0)) &&
            ((_params == null) || (_params.getCount() == 0)))
        {
            return true;
        }
        if ((args == null) || (_params == null))
        {
            return false;
        }

        ExpressionArray argExprs  = args.getArguments();
        Type            varType  = null;
        Type            valType  = null;

        if (argExprs.getCount() != _params.getCount())
        {
            return false;
        }
        for (int idx = 0; idx < argExprs.getCount(); idx++)
        {
            varType = _params.get(idx).getType();
            valType = argExprs.get(idx).getType();
            if (valType == null)
            {
                return false;
            }
            if (varType == null)
            {
                return false;
            }
            if (!valType.isMethodInvocationConvertibleTo(varType))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isAssignmentCompatibleTo(FormalParameterArray other)
    {
        if ((_params == null) || _params.isEmpty())
        {
            return (other == null) || other.isEmpty();
        }
        if ((other == null) || (_params.getCount() != other.getCount()))
        {
            return false;
        }

        Type localParamType;
        Type otherParamType;

        for (int idx = 0; idx < _params.getCount(); idx++)
        {
            localParamType = _params.get(idx).getType();
            otherParamType = other.get(idx).getType();
            if (!localParamType.isAssignmentCompatibleTo(otherParamType) &&
                !otherParamType.isAssignmentCompatibleTo(localParamType))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isEqualTo(Type[] types)
    {
        if ((types == null) || (types.length != _params.getCount()))
            {
            return false;
        }

        for (int idx = 0; idx < types.length; idx++)
            {
            if (!_params.get(idx).getType().isEqualTo(types[idx]))
                {
                return false;
            }
        }
        return false;
    }

    public boolean isSimilarTo(FormalParameterList other)
    {
        if (_params.getCount() != other._params.getCount())
            {
            return false;
        }
        for (int idx = 0; idx < _params.getCount(); idx++)
            {
            if (!_params.get(idx).isSimilarTo(other._params.get(idx)))
                {
                return false;
            }
        }
        return true;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer();

        for (int idx = 0; idx < _params.getCount(); idx++)
            {
            if (idx > 0)
                {
                result.append(", ");
            }
            result.append(_params.get(idx).toString());
        }
        return result.toString();
    }
}
