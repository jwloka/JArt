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
import jast.Global;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.Package;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.TypeIterator;

// Contains some functionality that is useful for working
// with ast's
public class ASTHelper
{

    // Retrieves the compilation unit owning the given node
    public static CompilationUnit getCompilationUnitOf(ContainedNode node)
    {
        ContainedNode curNode   = node;
        Node          container = null;

        while (!(curNode instanceof CompilationUnit))
        {
            container = curNode.getContainer();
            if ((container == null) ||
                !(container instanceof ContainedNode))
            {
                return null;
            }
            curNode = (ContainedNode)container;
        }
        return (CompilationUnit)curNode;
    }

    // Retrieves the (nearest) invocable declaration owning the given node
    public static InvocableDeclaration getInvocableDeclarationOf(ContainedNode node)
    {
        ContainedNode curNode   = node;
        Node          container = null;

        while (!(curNode instanceof InvocableDeclaration))
        {
            container = curNode.getContainer();
            if ((container == null) ||
                !(container instanceof ContainedNode))
            {
                return null;
            }
            curNode = (ContainedNode)container;
        }
        return (InvocableDeclaration)curNode;
    }

    // Retrieves the (outermost) type declaration owning the given node,
    // The returned type declaration is always a top-level type and never
    // a local class
    public static TypeDeclaration getOutermostTypeDeclarationOf(ContainedNode node)
    {
        ContainedNode curNode   = node;
        Node          container = null;
        Node          result    = null;

        while ((curNode != null) && !(curNode instanceof CompilationUnit))
        {
            if (curNode instanceof TypeDeclaration)
            {
                result = curNode;
            }
            container = curNode.getContainer();
            if ((container == null) ||
                !(container instanceof ContainedNode))
            {
                break;
            }
            curNode = (ContainedNode)container;
        }
        return (TypeDeclaration)result;
    }

    // Retrieves the project in which the given node is contained in
    public static Project getProjectOf(ContainedNode node)
    {
        Node curNode   = node;
        Node container = null;

        while (curNode instanceof ContainedNode)
        {
            container = ((ContainedNode)curNode).getContainer();
            if (container == null)
            {
                return null;
            }
            if (container instanceof Project)
            {
                return (Project)container;
            }
            curNode = (ContainedNode)container;
        }
        return null;
    }

    // Retrieves the (nearest) type declaration owning the given node
    public static TypeDeclaration getTypeDeclarationOf(ContainedNode node)
    {
        ContainedNode curNode   = node;
        Node          container = null;

        while ((curNode != null) && !(curNode instanceof TypeDeclaration))
        {
            container = curNode.getContainer();
            if ((container == null) ||
                !(container instanceof ContainedNode))
            {
                return null;
            }
            curNode = (ContainedNode)container;
        }
        return (TypeDeclaration)curNode;
    }

    // Checks whether the given type is a sub type of the
    // given base type
    public static boolean hasBaseType(TypeDeclaration typeDecl, TypeDeclaration baseTypeDecl)
    {
        if ((typeDecl == null) || (baseTypeDecl == null))
        {
            return false;
        }
        if ((typeDecl instanceof InterfaceDeclaration) &&
            (baseTypeDecl instanceof ClassDeclaration))
        {
            return false;
        }

        Type         curBaseType;
        TypeIterator it;

        if (baseTypeDecl instanceof InterfaceDeclaration)
        {
            for (it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                if (it.getNext().getReferenceBaseTypeDecl() == baseTypeDecl)
                {
                    return true;
                }
            }
        }
        else if (typeDecl instanceof ClassDeclaration)
        {
            curBaseType = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((curBaseType != null) &&
                (curBaseType.getReferenceBaseTypeDecl() == baseTypeDecl))
            {
                return true;
            }
        }
        // Descending
        if (baseTypeDecl instanceof InterfaceDeclaration)
        {
            for (it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                if (hasBaseType(it.getNext().getReferenceBaseTypeDecl(), baseTypeDecl))
                {
                    return true;
                }
            }
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            curBaseType = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((curBaseType != null) &&
                hasBaseType(curBaseType.getReferenceBaseTypeDecl(), baseTypeDecl))
            {
                return true;
            }
        }
        return false;
    }

    // Determines whether the two nodes are defined within the same top-level type
    // that is not a local type
    public static boolean isDefinedInSameNonLocalType(ContainedNode nodeA, ContainedNode nodeB)
    {
        TypeDeclaration typeA = getOutermostTypeDeclarationOf(nodeA);
        TypeDeclaration typeB = getOutermostTypeDeclarationOf(nodeB);

        return (typeA != null) && (typeA == typeB);
    }

    // Determines whether the two nodes are defined within the same top-level or
    // local type
    // Note that in the case of local types and their inner types only the innermost
    // local type is relevant - if nodeA is defined in local type A and nodeB in
    // local type B which is defined within A, then they are not defined in the
    // same type (type B is the outermost type for nodeB)
    public static boolean isDefinedInSameType(ContainedNode nodeA, ContainedNode nodeB)
    {
        TypeDeclaration typeA = getTypeDeclarationOf(nodeA);
        TypeDeclaration typeB = getTypeDeclarationOf(nodeB);

        // If they are defined in the same type than at least the enclosing
        // top level type must be the same
        while ((typeA != null) && (typeA.isInner() || typeA.isNested()))
        {
            typeA = typeA.getEnclosingType();
        }
        while ((typeB != null) && (typeB.isInner() || typeB.isNested()))
        {
            typeB = typeB.getEnclosingType();
        }
        if ((typeA == null) || (typeB == null))
        {
            return false;
        }
        return typeA == typeB;
    }

    public static boolean isInSameCompilationUnit(ContainedNode nodeA, ContainedNode nodeB)
    {
        return getCompilationUnitOf(nodeA) == getCompilationUnitOf(nodeB);
    }

    public static boolean isInSamePackage(ContainedNode nodeA, ContainedNode nodeB)
    {
        CompilationUnit unit = getCompilationUnitOf(nodeA);

        return unit == null ? false : isInSamePackage(unit.getPackage(), nodeB);
    }

    public static boolean isInSamePackage(Package sourcePckg, ContainedNode node)
    {
        return sourcePckg.isEqualTo(getCompilationUnitOf(node).getPackage());
    }

    public static boolean isQualified(String typeName)
    {
        return (typeName != null) && (typeName.indexOf(".") >= 0);
    }


    public static boolean existsType(Project project, String qualifiedName)
    {
        return (project.getType(qualifiedName) != null) ||
               Global.getParser().existsType(qualifiedName);
    }
}
