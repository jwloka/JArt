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
package jast.resolver;

import jast.Global;
import jast.Parser;
import jast.ProgressSourceBase;
import jast.SyntaxException;
import jast.ast.ASTHelper;
import jast.ast.ContainedNode;
import jast.ast.ExpressionReplacementVisitor;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.Project;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Primary;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.ExpressionArray;
import jast.ast.nodes.collections.FormalParameterArray;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.InvocableArrayImpl;
import jast.ast.nodes.collections.InvocableIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.visitor.DescendingVisitor;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;
import jast.parser.CommentingVisitor;
import jast.parser.Scope;
import jast.parser.ScopeIterator;

import java.io.OutputStream;
import java.io.Writer;

import antlr.ANTLRException;

// The resolver supports progress observers, but it does
// not resolve packages but only compilation units
// therefore it does not perform package notification
public class Resolver extends ProgressSourceBase
{
    public class ResolvingVisitor extends DescendingVisitor
    {
        public void visitAnonymousClassDeclaration(AnonymousClassDeclaration node)
        {
            trace("Start of declaration of anonymous class extending type "+node.getBaseType());
            incIndent();

            startTypeScope(node);
            super.visitAnonymousClassDeclaration(node);
            stopTypeScope();

            // the resolving possibly changed the signatures
            node.getMethods().updateSignatures();

            decIndent();
            trace("End of declaration of anonymous class extending type "+node.getBaseType());
        }

        public void visitArrayCreation(ArrayCreation node)
        {
            trace("Creation of array "+node.getType());

            super.visitArrayCreation(node);
            if (!resolve(node.getType(), null, true))
            {
                Global.getOutput().addError("Could not determine type "+node.getType()+" of array", node);
            }
        }

        public void visitBinaryExpression(BinaryExpression node)
        {
            super.visitBinaryExpression(node);
            if (node.getOperator() == BinaryExpression.PLUS_OP)
            {
                if (!resolve(node.getLeftOperand().getType(), null, true))
                {
                    Global.getOutput().addError("Could not determine type "+node.getLeftOperand().getType()+" of left side of binary expression", node);
                }
                if (!resolve(node.getRightOperand().getType(), null, true))
                {
                    Global.getOutput().addError("Could not determine type "+node.getRightOperand().getType()+" of right side of binary expression", node);
                }
            }
        }

        public void visitBlock(Block node)
        {
            startBlockScope();
            super.visitBlock(node);
            stopBlockScope();
        }

        public void visitCatchClause(CatchClause node)
        {
            startBlockScope();
            super.visitCatchClause(node);
            stopBlockScope();
        }

        public void visitClassAccess(ClassAccess node)
        {
            resolve(node);
        }

        public void visitClassDeclaration(ClassDeclaration node)
        {
            trace("Start of declaration of class "+node.getName());
            incIndent();

            startTypeScope(node);
            resolveBaseTypes(node);
            super.visitClassDeclaration(node);
            stopTypeScope();

            // the resolving possibly changed the signatures
            node.getMethods().updateSignatures();
            node.getConstructors().updateSignatures();

            decIndent();
            trace("End of declaration of class "+node.getName());
        }

        public void visitCompilationUnit(CompilationUnit node)
        {
            trace("Start of compilation unit "+node.getName());
            incIndent();

            startUnitScope(node);
            super.visitCompilationUnit(node);
            stopUnitScope();

            decIndent();
            trace("End of compilation unit "+node.getName());
        }

        public void visitConstructorDeclaration(ConstructorDeclaration node)
        {
            trace("Start of declaration of constructor <"+node+">");
            incIndent();

            Type type;

            for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
            {
                type = it.getNext();
                if (!resolve(type, null, true))
                {
                    Global.getOutput().addError("Could not determine exception type "+type+" of constructor", node);
                }
            }
            startBlockScope();
            super.visitConstructorDeclaration(node);
            stopBlockScope();

            decIndent();
            trace("End of declaration of method <"+node+">");
        }

        public void visitConstructorInvocation(ConstructorInvocation node)
        {
            trace("Start of explicit "+
                                (node.ofBaseClass() ? "base class " : "")+
                                " constructor invocation");
            incIndent();

            super.visitConstructorInvocation(node);

            resolve(node);

            decIndent();
            trace("End of explicit "+
                                (node.ofBaseClass() ? "base class " : "")+
                                " constructor invocation");
        }

        public void visitFieldAccess(FieldAccess node)
        {
            trace("Start of access to field "+node.getFieldName());
            incIndent();

            super.visitFieldAccess(node);
            resolve(node);

            decIndent();
            trace("End of access to field "+node.getFieldName());
        }

        public void visitFieldDeclaration(FieldDeclaration node)
        {
            trace("Start of declaration of variable <"+node+">");
            incIndent();

            if (!resolve(node.getType(), null, true))
            {
                Global.getOutput().addError("Could not determine type "+node.getType()+" of field declaration", node);
            }
            super.visitFieldDeclaration(node);

            decIndent();
            trace("End of declaration of variable <"+node+">");
        }

        public void visitFormalParameter(FormalParameter node)
        {
            trace("Declaration of formal parameter "+node);
            resolve(node, null);
            defineVariable(node);
        }

        public void visitForStatement(ForStatement node)
        {
            startBlockScope();
            super.visitForStatement(node);
            stopBlockScope();
        }

        public void visitImportDeclaration(ImportDeclaration node)
        {
            trace("Added import of "+node.getImportedPackageOrType()+
                              (node.isOnDemand() ? ".*" : ""));

            addImport(node);
        }

        public void visitInitializer(Initializer node)
        {
            trace("Start of "+
                                (node.isStatic() ? "static " : "")+
                                " initializer");
            incIndent();

            startBlockScope();
            super.visitInitializer(node);
            stopBlockScope();

            decIndent();
            trace("End of "+
                                (node.isStatic() ? "static " : "")+
                                " initializer");
        }

        public void visitInstanceofExpression(InstanceofExpression node)
        {
            trace("instanceof-check for type "+node.getReferencedType());

            super.visitInstanceofExpression(node);
            if (!resolve(node.getReferencedType(), null, true))
            {
                Global.getOutput().addError("Could not determine referenced type "+node.getReferencedType()+" of instanceof expression", node);
            }
        }

        public void visitInstantiation(Instantiation node)
        {
            trace("Start of instantiation of "+node.getInstantiatedType());
            incIndent();

            // We need the type of the base expression if any
            if (node.isTrailing())
            {
                node.getBaseExpression().accept(this);
            }
            // The arguments are necessary for determining which
            // constructor has been invoked
            if (node.hasArguments())
            {
                node.getArgumentList().accept(this);
            }

            resolve(node);

            // It is necessary to resolve the instantiation before the anonymous
            // class because it defines the base type of the anonymous class
            if (node.withAnonymousClass())
            {
                node.getAnonymousClass().accept(this);
            }

            decIndent();
            trace("End of instantiation of "+node.getInstantiatedType());
        }

        public void visitInterfaceDeclaration(InterfaceDeclaration node)
        {
            trace("Start of declaration of interface "+node.getName());
            incIndent();

            startTypeScope(node);
            resolveBaseTypes(node);
            super.visitInterfaceDeclaration(node);
            stopTypeScope();

            // the resolving possibly changed the signatures
            node.getMethods().updateSignatures();

            decIndent();
            trace("End of declaration of interface "+node.getName());
        }


        public void visitLocalVariableDeclaration(LocalVariableDeclaration node)
        {
            trace("Start of declaration of variable <"+node+">");
            incIndent();

            if (!resolve(node.getType(), null, true))
            {
                Global.getOutput().addError("Could not determine type "+node.getType()+" of local variable declaration", node);
            }
            super.visitLocalVariableDeclaration(node);
            defineVariable(node);

            decIndent();
            trace("End of declaration of variable <"+node+">");
        }

        public void visitMethodDeclaration(MethodDeclaration node)
        {
            trace("Start of declaration of method <"+node+">");
            incIndent();

            if (node.getReturnType() != null)
            {
                if (!resolve(node.getReturnType(), null, true))
                {
                    Global.getOutput().addError("Could not determine return type "+node.getReturnType()+" of method declaration", node);
                }
            }

            Type type;

            for (TypeIterator it = node.getThrownExceptions().getIterator(); it.hasNext();)
            {
                type = it.getNext();
                resolve(type, null, true);
                if (!resolve(type, null, true))
                {
                    Global.getOutput().addError("Could not determine exception type "+type+" of method declaration", node);
                }
            }
            startBlockScope();
            super.visitMethodDeclaration(node);
            stopBlockScope();

            decIndent();
            trace("End of declaration of method <"+node+">");
        }

        public void visitMethodInvocation(MethodInvocation node)
        {
            trace("Start of access to method "+node.getMethodName());
            incIndent();

            super.visitMethodInvocation(node);
            resolve(node);

            decIndent();
            trace("End of access to method "+node.getMethodName());
        }

        public void visitSelfAccess(SelfAccess node)
        {
            super.visitSelfAccess(node);
            resolve(node);
        }

        public void visitStringLiteral(StringLiteral node)
        {
            if (!resolve(node.getType(), null, true))
            {
                Global.getOutput().addError("Could not determine type "+node.getType()+" of literal", node);
            }
        }

        public void visitSwitchStatement(SwitchStatement node)
        {
            startBlockScope();
            super.visitSwitchStatement(node);
            stopBlockScope();
        }

        public void visitTypeAccess(TypeAccess node)
        {
            trace("Type access to "+node.getType());

            super.visitTypeAccess(node);
            if (!resolve(node.getType(),
                         node.isTrailing() ? ASTHelper.getTypeDeclarationOf(node.getBaseExpression()) : null,
                         true))
            {
                Global.getOutput().addError("Could not determine type "+node.getType()+" of type access", node);
            }
        }

        public void visitUnaryExpression(UnaryExpression node)
        {
            // for the inner expression
            super.visitUnaryExpression(node);

            if ((node.getOperator() == UnaryExpression.CAST_OP) &&
                node.getCastType().isReference())
            {
                if (!resolve(node.getCastType(), null, true))
                {
                    Global.getOutput().addError("Could not determine type "+node.getCastType()+" of reference cast", node);
                }
                trace("Cast expression to type "+node.getCastType());
            }
        }
        public void visitUnresolvedAccess(UnresolvedAccess node)
        {
            trace("Unresolved access "+node);

            super.visitUnresolvedAccess(node);
            resolve(node);
        }
    }


    private Parser                    _parser;
    private Project                   _project;
    private CompilationUnitScopeStack _scopes             = new CompilationUnitScopeStack();

    // For tracing
    private Tracer                 _tracer             = new Tracer();
    private StringBuffer           _indent             = new StringBuffer();

    public Resolver(Parser parser)
    {
        setParser(parser);
    }

    public void addImport(ImportDeclaration node)
    {
        if (!node.isOnDemand())
        {
            // We preload all direct imports of the current type
            TypeDeclaration typeDecl = retrieveDirectImport(node.getImportedPackageOrType());

            if (typeDecl != null)
            {
                _scopes.top().setDirectImport(typeDecl.getName(), typeDecl);
            }
            else
            {
                Global.getOutput().addError("Could not retrieve directly imported type "+node.getImportedPackageOrType(), node);
            }
        }
    }

    // Step 1 (a-g) of algorithm 6.5.2 in Java Spec
    private Primary algorithm652step1(String part)
    {
        Node node = resolveIdentifier(part, _scopes.top(), true, true);

        if (node != null)
        {
            if (node instanceof LocalVariableDeclaration)
            {
                return Global.getFactory().createVariableAccess((LocalVariableDeclaration)node);
            }
            else if (node instanceof FieldDeclaration)
            {
                return Global.getFactory().createFieldAccess((FieldDeclaration)node);
            }
            else
            {
                return Global.getFactory().createTypeAccess(null, ((TypeDeclaration)node).getCreatedType());
            }
        }
        return null;
    }

    private Primary algorithm652step2(Primary baseExpr, String part)
    {
        NodeFactory      factory     = Global.getFactory();
        FieldDeclaration fieldDecl   = null;
        FieldAccess      fieldAccess = null;
        TypeDeclaration  typeDecl    = null;
        TypeAccess       typeAccess  = null;
        boolean          checkPckg   = false;

        if (!resolve(baseExpr.getType(), null, true))
        {
            return null;
        }
        if (baseExpr.getType().isArray())
        {
            typeDecl = _project.getArrayType();
        }
        else
        {
            typeDecl = baseExpr.getType().getReferenceBaseTypeDecl();
        }
        if (typeDecl == null)
        {
            return null;
        }

        fieldDecl = findField(typeDecl, part);
        if (fieldDecl != null)
        {
            fieldAccess = factory.createFieldAccess(baseExpr, fieldDecl);
            resolve(fieldDecl.getType(), typeDecl, true);
            trace("Found access to field "+fieldDecl+
                                " at type "+baseExpr.getType());
            return fieldAccess;
        }
        else
        {
            typeDecl = findInnerType(_scopes.top(), typeDecl, part);
            if (typeDecl == null)
            {
                // This can happen on systems where the case of filenames
                // does not matter (like Windows)
                // For instance a access like 'java.awt.font.OpenType' would
                // first lead to the type 'java.awt.Font' which of course
                // has no inner type 'OpenType'
                // Therefore at this point we switch back to the package name
                checkPckg = true;
            }
            else
            {
                typeAccess = factory.createTypeAccess(baseExpr, typeDecl.getCreatedType());
                trace("Found access to inner type "+typeDecl.getQualifiedName()+
                                    " of type "+typeAccess.getType());
            }
            return typeAccess;
        }
    }

    /*
     * Determines whether the target type is accessible from the source type.
     * Both can be inner/local types. Note that the check does not test
     * whether the target type is actually referencable from the source type
     * (this would involve the imports), but only whether the source type
     * could access the target type (if imported or used qualified) in terms
     * of their packages and modifiers.
     */
    public static boolean canAccess(TypeDeclaration sourceType, TypeDeclaration targetType)
    {
        // These are the rules :
        // * We can access a public type if it is top-level or we can
        //   access its enclosing type
        // * We can access a friendly/protected type if we are in the
        //   same package and it is top-level or in an accessible type
        // * We can access a protected type if the current type
        //   inherited it
        // * We can access a private type only within the same top-level
        //   type - another top-level type in the same compilation unit
        //   cannot access private inner types
        // * Inner/Nested types can access all types their
        //   enclosing types can access;
        //   Similar for local types with their owning type
        // * Local types themselves are accessible only within their
        //   defining context (ie method/constructor) and within themselves
        if (targetType.isInner() || targetType.isNested())
        {
            if (!canAccess(sourceType, targetType.getEnclosingType()))
            {
                return false;
            }
        }
        return canAccessInternal(sourceType, targetType);
    }

    /*
     * Extended access check which incorporates top-level access check
     * (iow without a source type).
     */
    public static boolean canAccess(CompilationUnitScope context, TypeDeclaration targetType)
    {
        if (targetType != null)
        {
            if (context.getCurrentType() == null)
            {
                if (isLocal(targetType))
                {
                    return false;
                }

                // top level access only depends on the involved packages
                Modifiers mods = targetType.getModifiers();

                if (mods.isPrivate())
                {
                    return false;
                }
                else if ((mods.isProtected() || mods.isFriendly()) &&
                         !ASTHelper.isInSamePackage(context.getCompilationUnit().getPackage(), targetType))
                {
                    return false;
                }
                else
                {
                    return (targetType.isInner() || targetType.isNested() ?
                                canAccess(context, targetType.getEnclosingType()) :
                                true);
                }
            }
            else
            {
                return canAccessInternal(context.getCurrentType(), targetType);
            }
        }
        return false;
    }

    // Checks whether we can access the given target type.
    // In contrast to the other canAccess method here we
    // assume for inner/nested types that we can access its
    // enclosing type (as is true in the resolve mechanism)
    private static boolean canAccessInternal(TypeDeclaration sourceType, TypeDeclaration targetType)
    {
        Modifiers mods = targetType.getModifiers();

        if (isLocal(targetType))
        {
            // We can access a local class only within itself
            // This however can mean access from an inner or
            // even local class
            if (isLocal(sourceType))
            {
                return (sourceType == targetType) ||
                       canAccessInternal(ASTHelper.getTypeDeclarationOf((ContainedNode)sourceType.getContainer()),
                                         targetType);
            }
            else if (sourceType.isInner() || sourceType.isNested())
            {
                return canAccessInternal(sourceType.getEnclosingType(), targetType);
            }
            else
            {
                return ASTHelper.isDefinedInSameType(sourceType, targetType);
            }
        }
        if (mods.isPublic())
        {
            return true;
        }
        if (mods.isProtected() &&
            (targetType.isInner() || targetType.isNested()) &&
            hasInherited(sourceType, targetType))
        {
            return true;
        }
        if (isLocal(sourceType))
        {
            // We inherit the access rights from the type where
            // the local class is defined in (owning type)
            // We derive the owning type declaration via the container
            // of the local class (which is an invocable)
            return canAccessInternal(ASTHelper.getTypeDeclarationOf((ContainedNode)sourceType.getContainer()),
                                     targetType);
        }
        if (sourceType.isInner() || sourceType.isNested())
        {
            // We inherit the access rights from the enclosing type
            return canAccessInternal(sourceType.getEnclosingType(), targetType);
        }
        if (!mods.isPrivate() && ASTHelper.isInSamePackage(sourceType, targetType))
        {
            return true;
        }
        if (ASTHelper.isDefinedInSameType(sourceType, targetType))
        {
            return true;
        }
        return false;
    }

    private CompilationUnitScope contextFor(Node node)
    {
        CompilationUnitScope result  = null;

        if (node instanceof CompilationUnit)
        {
            result = new CompilationUnitScope((CompilationUnit)node);
        }
        else if (node instanceof ContainedNode)
        {
            ContainedNode curNode = (ContainedNode)node;

            result = contextFor(curNode.getContainer());
            if (curNode instanceof InvocableDeclaration)
            {
                result.pushBlockScope();
                result.topBlockScope().defineVariables(((InvocableDeclaration)curNode).getParameterList());
            }
            else if (curNode instanceof CatchClause)
            {
                result.pushBlockScope();
                result.topBlockScope().defineVariable(((CatchClause)curNode).getFormalParameter());
            }
            else if (curNode instanceof ForStatement)
            {
                result.pushBlockScope();
                if (((ForStatement)curNode).hasInitDeclarations())
                {
                    result.topBlockScope().defineVariables(((ForStatement)curNode).getInitDeclarations());
                }
            }
            else if (curNode instanceof Block)
            {
                result.pushBlockScope();
            }
            else if (curNode instanceof SwitchStatement)
            {
                result.pushBlockScope();
            }
            else if (curNode instanceof TypeDeclaration)
            {
                result.pushTypeScope((TypeDeclaration)curNode);
            }
        }
        return result;
    }

    // Returns the currently active compilation unit
    private CompilationUnit currentCompilationUnit()
    {
        return _scopes.top().getCompilationUnit();
    }

    // Returns the currently active type (iow the type in which
    // the resolver currently is working)
    private TypeDeclaration currentTypeDeclaration()
    {
        return _scopes.isEmpty() ? null : _scopes.top().getCurrentType();
    }

    private void decIndent()
    {
        if (_indent.length() > 0)
        {
            _indent.delete(_indent.length() - 2, _indent.length());
        }
    }

    private void defineVariable(VariableDeclaration decl)
    {
        _scopes.top().defineVariable(decl);
    }

    // Adds all methods/constructors from the source iterator to the list
    // if the name is equal to the given one and if the argument
    // list is applicable to the given parameter list
    // Returns true if there is at least one invocable of the
    // given name which is accessible (but not necessarily applicable)
    private boolean filterInvocables(InvocableIterator  src,
                                     String             name,
                                     ArgumentList       args,
                                     boolean            canBePrivate,
                                     boolean            canBeProtected,
                                     boolean            canBeFriendly,
                                     InvocableArrayImpl results)
    {
        InvocableDeclaration invoc;
        Modifiers            mods;
        boolean              result = false;

        while (src.hasNext())
        {
            invoc = src.getNext();
            if (invoc.getName().equals(name))
            {
                mods   = invoc.getModifiers();
                if (canBePrivate ||
                    mods.isPublic() ||
                    (mods.isProtected() && canBeProtected) ||
                    (!mods.isPrivate()  && canBeFriendly))
                {
                    result = true;
                    if (isApplicable(args, invoc.getParameterList()))
                    {
                        results.add(invoc);
                    }
                }
            }
        }
        return result;
    }

    public ConstructorDeclaration findConstructor(ClassDeclaration classDecl,
                                                  ArgumentList     args)
    {
        InvocableArrayImpl constructors = new InvocableArrayImpl();
        boolean            canBePrivate = ASTHelper.isDefinedInSameType(classDecl, currentTypeDeclaration());

        // The types of the parameters of the constructors must be
        // resolved
        for (ConstructorIterator it = classDecl.getConstructors().getIterator(); it.hasNext();)
        {
            resolve(it.getNext().getParameterList(), classDecl);
        }
        filterInvocables(classDecl.getConstructors().getInvocableIterator(),
                         classDecl.getName(),
                         args,
                         canBePrivate,
                         canBePrivate || ASTHelper.hasBaseType(currentTypeDeclaration(), classDecl),
                         canBePrivate || ASTHelper.isInSamePackage(classDecl, currentTypeDeclaration()),
                         constructors);
        return (ConstructorDeclaration)getMostSpecific(constructors);
    }

    // Tries to find an accessible field in the given type
    // Relevant here is the accessibility relationship
    // between the current type and the given type and
    // the visibility of the fields themselves
    public FieldDeclaration findField(TypeDeclaration typeDecl,
                                      String          fieldName)
    {
        // The steps are :
        // * defined in the given type and
        //   - private if defined in the same top-level type
        //     as the current (including local types)
        //   - protected if can be private of defined in a
        //     base type of current type (eg super/this)
        //   - friendly if defined in the same package as
        //     the current type
        // * defined in its base types and
        //   - protected if the given type has private/protected
        //     access
        //   - friendly only if all previous types in the chain
        //     (given type/base types) had private/friendly access
        //
        // Since we can only find fields defined in the given
        // type or its base types, it is not necessary to
        // determine whether the given type can access the
        // defining type of the field - private fields from
        // base types are not visible anyway and friendly
        // access is restricted to the given type
        // Similarily, we cannot indirectly access enclosing/inner
        // types of the given type (for instance with 'this.x')
        // due to the qualified access (a qualified outer-instance
        // access 'Outer.this.x' is necessary for this)
        //
        // To avoid unneccessary computation for protected/friendly access
        // we allow it if private access (which is in the same top-level type)
        // is allowed; however protected access does not automatically
        // allow friendly access since the base type is not necessarily
        // from the same package

        boolean canBePrivate = ASTHelper.isDefinedInSameType(typeDecl, currentTypeDeclaration());

        return findField(typeDecl,
                         fieldName,
                         canBePrivate,
                         canBePrivate || ASTHelper.hasBaseType(currentTypeDeclaration(), typeDecl),
                         canBePrivate || ASTHelper.isInSamePackage(typeDecl, currentTypeDeclaration()));
    }

    private FieldDeclaration findField(TypeDeclaration typeDecl,
                                       String          fieldName,
                                       boolean         canBePrivate,
                                       boolean         canBeProtected,
                                       boolean         canBeFriendly)
    {
        FieldDeclaration decl = typeDecl.getFields().get(fieldName);

        if (decl != null)
        {
            Modifiers mods = decl.getModifiers();

            if (canBePrivate ||
                mods.isPublic() ||
                (mods.isProtected() && canBeProtected) ||
                (!mods.isPrivate()  && canBeFriendly))
            {
                return decl;
            }
        }
        return findFieldInBaseTypes(typeDecl,
                                    fieldName,
                                    canBeProtected,
                                    canBeFriendly);
    }

    private FieldDeclaration findFieldInBaseTypes(TypeDeclaration typeDecl,
                                                  String          fieldName,
                                                  boolean         canBeProtected,
                                                  boolean         canBeFriendly)
    {
        FieldDeclaration decl = null;
        Modifiers        mods = null;
        Type             type = null;

        if (typeDecl instanceof ClassDeclaration)
        {
            type = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((type != null) && resolve(type, typeDecl, false))
            {
                decl = findField(type.getReferenceBaseTypeDecl(),
                                 fieldName,
                                 false,
                                 canBeProtected,
                                 canBeFriendly && ASTHelper.isInSamePackage(currentTypeDeclaration(), type.getReferenceBaseTypeDecl()));

                if (decl != null)
                {
                    return decl;
                }
            }
        }
        for (TypeIterator typeIt = typeDecl.getBaseInterfaces().getIterator(); typeIt.hasNext();)
        {
            type = typeIt.getNext();
            if (resolve(type, typeDecl, false))
            {
                decl = findField(type.getReferenceBaseTypeDecl(),
                                 fieldName,
                                 false,
                                 canBeProtected,
                                 canBeFriendly && ASTHelper.isInSamePackage(currentTypeDeclaration(), type.getReferenceBaseTypeDecl()));
                if (decl != null)
                {
                    return decl;
                }
            }
        }
        return null;
    }

    /*
     * Searches the given type for an locally defined inner type of the
     * given name. If none was found, all base types (classes and
     * interfaces where applicable) are searched for an inner type of
     * that name. Note that the base types themselves are not checked.
     */
    private TypeDeclaration findInnerType(CompilationUnitScope scope,
                                          TypeDeclaration      accessed,
                                          String               name)
    {
        TypeDeclaration decl = accessed.getInnerTypes().get(name);
        Type            type = null;

        if ((decl != null) && canAccess(scope, decl))
        {
            return decl;
        }
        if (accessed instanceof ClassDeclaration)
        {
            type = ((ClassDeclaration)accessed).getBaseClass();
            // if the current type declaration (which does not have to be
            // the one which currently is being resolved) is not resolved
            // (or if it is java.lang.Object), then the base class is null
            // this is no problem, however, since java.lang.Object does not
            // have any inner types
            if ((type != null) && resolve(type, accessed, false))
            {
                decl = findInnerType(scope,
                                     type.getReferenceBaseTypeDecl(),
                                     name);
                if (decl != null)
                {
                    return decl;
                }
            }
        }
        for (TypeIterator typeIt = accessed.getBaseInterfaces().getIterator(); typeIt.hasNext();)
        {
            type = typeIt.getNext();
            if (resolve(type, accessed, false))
            {
                decl = findInnerType(scope,
                                     type.getReferenceBaseTypeDecl(),
                                     name);
                if (decl != null)
                {
                    return decl;
                }
            }
        }
        return null;
    }

    // Searches for an accessible local field (iow the field
    // access was not trailing and therefore at the current
    // type)
    public FieldDeclaration findLocalField(String fieldName)
    {
        // The steps are :
        // * the current type and its base types
        // * the enclosing types and their base types
        // To the current type and its enclosing types
        // we have full access (private), to base classes
        // we have protected access

        TypeDeclaration  type = currentTypeDeclaration();
        FieldDeclaration decl = findField(type, fieldName, true, true, true);

        while ((decl == null) &&
               (!type.isTopLevel() ||
                ((type instanceof ClassDeclaration) && ((ClassDeclaration)type).isLocal())))
        {
            // local/anonymous type ?
            if (type.isTopLevel())
            {
                type = ASTHelper.getTypeDeclarationOf((ContainedNode)type.getContainer());
            }
            else
            {
                type = type.getEnclosingType();
            }
            decl = findField(type, fieldName, true, true, true);
        }
        return decl;
    }

    // Searches for an accessible local method which is
    // the most specific method for the given arguments
    public MethodDeclaration findLocalMethod(String       methodName,
                                             ArgumentList args)
    {
        // The steps are similar to the field search
        TypeDeclaration    type    = currentTypeDeclaration();
        InvocableArrayImpl methods = new InvocableArrayImpl();

        if (!findMethods(type, methodName, args, true, true, true, methods))
        {
            while (!type.isTopLevel() ||
                   ((type instanceof ClassDeclaration) && ((ClassDeclaration)type).isLocal()))
            {
                // local/anonymous type ?
                if (type.isTopLevel())
                {
                    type = ASTHelper.getTypeDeclarationOf((ContainedNode)type.getContainer());
                }
                else
                {
                    type = type.getEnclosingType();
                }
                if (findMethods(type, methodName, args, true, true, true, methods))
                {
                    break;
                }
            }
        }
        return (MethodDeclaration)getMostSpecific(methods);
    }

    // Tries to find the accessible method in the given type
    // which is most specific for the given arguments
    // Relevant here is the accessibility relationship
    // between the current type and the given type and
    // the visibility of the methods themselves
    public MethodDeclaration findMethod(TypeDeclaration typeDecl,
                                        String          methodName,
                                        ArgumentList    args)
    {
        // The steps are similar to the field search
        // with the difference that the search does not stop
        // one an accessible and appropiaze method has been
        // found but rather all these methods are gathered and
        // used to determine the most specific method

        InvocableArrayImpl methods      = new InvocableArrayImpl();
        boolean            canBePrivate = ASTHelper.isDefinedInSameType(typeDecl, currentTypeDeclaration());

        findMethods(typeDecl,
                    methodName,
                    args,
                    canBePrivate,
                    canBePrivate || ASTHelper.hasBaseType(currentTypeDeclaration(), typeDecl),
                    canBePrivate || ASTHelper.isInSamePackage(typeDecl, currentTypeDeclaration()),
                    methods);

        return (MethodDeclaration)getMostSpecific(methods);
    }

    // Returns true if there are any methods in the given
    // type or its base types with that name (regardless of
    // whether the methods are applicable or not)
    private boolean findMethods(TypeDeclaration    typeDecl,
                                String             methodName,
                                ArgumentList       args,
                                boolean            canBePrivate,
                                boolean            canBeProtected,
                                boolean            canBeFriendly,
                                InvocableArrayImpl methods)
    {
        // The types of the parameters must be resolved
        for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
        {
            resolve(it.getNext().getParameterList(), typeDecl);
        }

        boolean result = false;

        if (filterInvocables(typeDecl.getMethods().getInvocableIterator(),
                             methodName,
                             args,
                             canBePrivate,
                             canBeProtected,
                             canBeFriendly,
                             methods))
        {
            result = true;
        }
        if (findMethodsInBaseTypes(typeDecl,
                                   methodName,
                                   args,
                                   canBePrivate,
                                   canBeProtected,
                                   canBeFriendly,
                                   methods))
        {
            result = true;
        }
        return result;
    }

    private boolean findMethodsInBaseTypes(TypeDeclaration    typeDecl,
                                           String             methodName,
                                           ArgumentList       args,
                                           boolean            canBePrivate,
                                           boolean            canBeProtected,
                                           boolean            canBeFriendly,
                                           InvocableArrayImpl methods)
    {
        boolean result = false;
        Type    type   = null;

        if (typeDecl instanceof ClassDeclaration)
        {
            type = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((type != null) && resolve(type, typeDecl, false))
            {
                if (findMethods(type.getReferenceBaseTypeDecl(),
                                methodName,
                                args,
                                false,
                                canBeProtected,
                                canBeFriendly && ASTHelper.isInSamePackage(currentTypeDeclaration(), type.getReferenceBaseTypeDecl()),
                                methods))
                {
                    result = true;
                }
            }
        }
        for (TypeIterator typeIt = typeDecl.getBaseInterfaces().getIterator(); typeIt.hasNext();)
        {
            type = typeIt.getNext();
            if (resolve(type, typeDecl, false))
            {
                if (findMethods(type.getReferenceBaseTypeDecl(),
                                methodName,
                                args,
                                false,
                                canBeProtected,
                                canBeFriendly && ASTHelper.isInSamePackage(currentTypeDeclaration(), type.getReferenceBaseTypeDecl()),
                                methods))
                {
                    result = true;
                }
            }
        }
        return result;
    }

    /*
     * Searches for a visible type in the compilation unit of the
     * given type. Recognized are (in that order):
     * local classes (if within a local class context), the
     * current class, (possibly inherited) inner classes of
     * the current class, outer classes and their (possibly
     * inherited) inner classes, other top-level classes in
     * the current compilation unit.
     * <code>canBeInner</code> states whether the type
     * can be an inner class (as opposed to outer classes).
     * Examples where inner classes are not allowed are
     * for instance finding a base class of a type declaration
     * (where inner classes are not yet visible to the resolution).<br>
     * Note that imports are ignored (they are searched within
     * <code>resolve</code>).
     */
    public TypeDeclaration findTypeDeclaration(CompilationUnitScope scope,
                                               TypeDeclaration      accessed,
                                               String               name,
                                               boolean              canBeInner)
    {
        TypeDeclaration current = accessed;
        TypeDeclaration decl    = null;

        // First the current type or its outer classes or their direct
        // inner classes (defined or inherited)
        // Note that the unqualified name is used here
        if (name.equals(current.getName()))
        {
            decl = current;
        }
        if ((decl == null) && canBeInner)
        {
            // We can access private/protected inherited inner types
            // in the current type
            decl = findInnerType(scope, current, name);
        }
        while ((decl == null) && (current.isInner() || current.isNested()))
        {
            current = current.getEnclosingType();
            if (name.equals(current.getName()))
            {
                decl = current;
                break;
            }
            // We cannot access private/protected inherited inner types
            // of outer classes of the current type
            decl = findInnerType(scope, current, name);
        }
        // current now must be a top-level or local class
        if (decl == null)
        {
            // is local ?
            if ((current instanceof ClassDeclaration) &&
                ((ClassDeclaration)current).isLocal())
            {
                decl = findTypeDeclaration(scope,
                                           ASTHelper.getTypeDeclarationOf((ContainedNode)current.getContainer()),
                                           name,
                                           canBeInner);
            }
            else
            {
                // current is top-level, so we search for other top level classes
                decl = ((CompilationUnit)current.getContainer()).getTypeDeclarations().get(name);
            }
        }
        return decl;
    }

        public InvocableDeclaration getMostSpecific(InvocableArrayImpl invocables)
        {
            if ((invocables == null) || invocables.isEmpty())
            {
                return null;
            }

            InvocableDeclaration curInvoc;
            int                  cur = invocables.getCount()-1;

            // We're first checking for every method if there are
            // any more-specific methods earlier in the list;
            // if so, it will be removed from the list
            while (cur > 0)
            {
                curInvoc = invocables.get(cur);
                for (int idx = 0; idx < cur; idx++)
                {
                    if (isMoreSpecificThan(invocables.get(idx), curInvoc))
                    {
                        invocables.remove(cur);
                        break;
                    }
                }
                cur--;
            }
            // The second step does the same, but from the beginning
            cur = 0;

    outer:  while (cur < invocables.getCount()-1)
            {
                curInvoc = invocables.get(cur);
                for (int idx = cur+1; idx < invocables.getCount(); idx++)
                {
                    if (isMoreSpecificThan(invocables.get(idx), curInvoc))
                    {
                        invocables.remove(cur);
                        continue outer;
                    }
                }
                cur++;
            }

            // We return the first most-specific method because
            // it is the nearest one (from the accessed type)
            // according to the algorithm
            return (invocables.isEmpty() ? null : invocables.get(0));
        }

    // Returns a stored type or null if the type has not (yet) been parsed
    private TypeDeclaration getStoredType(String qualifiedName)
    {
        if (qualifiedName == null)
        {
            return null;
        }
        return _project.getType(qualifiedName);
    }

    // Checks whether the given inner type has been inherited (ie is
    // defined in one of the given type's base types)
    // Note that the modifiers of the inner type are not checked
    public static boolean hasInherited(TypeDeclaration typeDecl, TypeDeclaration innerTypeDecl)
    {
        if ((typeDecl == null) || (innerTypeDecl == null))
        {
            return false;
        }
        if ((typeDecl instanceof InterfaceDeclaration) &&
            (innerTypeDecl instanceof ClassDeclaration))
        {
            return false;
        }

        TypeDeclaration innerBaseDecl = innerTypeDecl.getEnclosingType();
        Type            baseType;
        TypeIterator    it;

        if (innerBaseDecl == null)
        {
            return false;
        }
        if (innerBaseDecl instanceof InterfaceDeclaration)
        {
            for (it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                if (it.getNext().getReferenceBaseTypeDecl() == innerBaseDecl)
                {
                    return true;
                }
            }
        }
        else if (typeDecl instanceof ClassDeclaration)
        {
            baseType = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((baseType != null) &&
                (baseType.getReferenceBaseTypeDecl() == innerBaseDecl))
            {
                return true;
            }
        }
        // Descending
        if (innerTypeDecl instanceof InterfaceDeclaration)
        {
            for (it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
            {
                if (hasInherited(it.getNext().getReferenceBaseTypeDecl(), innerTypeDecl))
                {
                    return true;
                }
            }
        }
        if (typeDecl instanceof ClassDeclaration)
        {
            baseType = ((ClassDeclaration)typeDecl).getBaseClass();
            if ((baseType != null) &&
                hasInherited(baseType.getReferenceBaseTypeDecl(), innerTypeDecl))
            {
                return true;
            }
        }
        return false;
    }

    private void incIndent()
    {
        _indent.append("  ");
    }

    public boolean isApplicable(ArgumentList args, FormalParameterList params)
    {
        if (((args   == null) || (args.getArguments().getCount() == 0)) &&
            ((params == null) || (params.getParameters().getCount() == 0)))
        {
            return true;
        }
        if ((args == null) || (params == null))
        {
            return false;
        }

        ExpressionArray      argExprs  = args.getArguments();
        FormalParameterArray paramObjs = params.getParameters();
        Type                 varType  = null;
        Type                 valType  = null;

        if (argExprs.getCount() != paramObjs.getCount())
        {
            return false;
        }
        for (int idx = 0; idx < argExprs.getCount(); idx++)
        {
            varType = paramObjs.get(idx).getType();
            valType = argExprs.get(idx).getType();
            if (valType == null)
            {
                return false;
            }
            if (varType == null)
            {
                return false;
            }
            // It is necessary to resolve the type declarations
            // of the two types if possible
            // This ensures that isSubTypeOf (used in
            // isMethodInvocationConvertibleTo) works correctly
            resolve(varType.getReferenceBaseTypeDecl());
            resolve(valType.getReferenceBaseTypeDecl());

            if (!valType.isMethodInvocationConvertibleTo(varType))
            {
                return false;
            }
        }
        return true;
    }

    // Little helper method that determines whether the given type
    // is a local class
    private static boolean isLocal(TypeDeclaration type)
    {
        return (type instanceof ClassDeclaration) &&
               ((ClassDeclaration)type).isLocal();
    }

    // Cf. Java Spec 15.12.2.2
    public static boolean isMoreSpecificThan(InvocableDeclaration current, InvocableDeclaration other)
    {
        Type typeT = ((TypeDeclaration)current.getContainer()).getCreatedType();
        Type typeU = ((TypeDeclaration)other.getContainer()).getCreatedType();

        if (!typeT.isMethodInvocationConvertibleTo(typeU))
        {
            return false;
        }
        if ((current.getParameterList() == null) ||
            (other.getParameterList() == null))
        {
            return true;
        }

        FormalParameterArray params      = current.getParameterList().getParameters();
        FormalParameterArray otherParams = other.getParameterList().getParameters();

        for (int idx = 0; idx < params.getCount(); idx++)
        {
            typeT = params.get(idx).getType();
            typeU = otherParams.get(idx).getType();
            if (!typeT.isMethodInvocationConvertibleTo(typeU))
            {
                return false;
            }
        }
        return true;
    }

    private boolean parseType(String qualifiedName)
    {
        try
        {
            if (_parser.parseType(_project, qualifiedName))
            {
                return true;
            }
        }
        catch (ANTLRException ex)
        {}
        return false;
    }

    public void printTraceTo(OutputStream output)
    {
        _tracer.printTo(output);
    }

    public void printTraceTo(Writer writer)
    {
        _tracer.printTo(writer);
    }

    private void resolve(ClassAccess classAccess)
    {
        if (classAccess.isQualified())
        {
            trace("Qualified access to class "+classAccess.getReferencedType());

            if (!resolve(classAccess.getReferencedType(), null, true))
            {
                Global.getOutput().addError("Could not determine type "+classAccess.getReferencedType()+" of class access", classAccess);
            }
        }
        // We resolve the target type (java.lang.Class) as well,
        // however we can ignore local classes now
        if (!resolve(classAccess.getType(), null, false))
        {
            Global.getOutput().addError("Could not determine type "+classAccess.getReferencedType(), classAccess);
        }
    }

    public void resolve(CompilationUnit unit)
    {
        if (!unit.isResolved())
        {
            unit.setResolvingStatus(true);
            notifyStartType(unit.getTopLevelQualifiedName());
            unit.accept(new ResolvingVisitor());
            notifyFinishType(unit.getTopLevelQualifiedName());
        }
    }

    private void resolve(ConstructorInvocation invocation)
    {
        if (invocation.getConstructorDeclaration() == null)
        {
            // note that the base expression does not specify the accessed type
            // but instead give sthe enclosing instance for the creation of an
            // inner class (which is not of interest for determining which
            // constructor is called) - therefore we simply ignore it
            ClassDeclaration       classDecl = (ClassDeclaration)currentTypeDeclaration();
            ConstructorDeclaration decl      = null;

            if (invocation.ofBaseClass())
            {
                if (!classDecl.getBaseClass().hasReferenceBaseTypeDecl())
                {
                    Global.getOutput().addError("Could not access base class "+classDecl.getBaseClass()+" of class "+classDecl.getName(),
                                                invocation);
                    return;
                }
                classDecl = (ClassDeclaration)classDecl.getBaseClass().getReferenceBaseTypeDecl();
            }
            decl = findConstructor(classDecl,
                                   invocation.getArgumentList());
            if (decl != null)
            {
                trace("Found invocation of constructor <"+decl.getSignature()+">");
                invocation.setConstructorDeclaration(decl);
            }
            else if (invocation.ofBaseClass() &&
                     classDecl.getConstructors().isEmpty() &&
                     (invocation.getArgumentList() == null))
            {
                trace("Found invocation of default base class constructor");
            }
            else
            {
                Global.getOutput().addError("Could not find constructor in class "+classDecl.getName()+" for arguments "+
                                            (invocation.getArgumentList() == null ?
                                                "" :
                                                invocation.getArgumentList().getSignature()),
                                            invocation);
            }
        }
    }

    private void resolve(FieldAccess fieldAccess)
    {
        if (fieldAccess.getFieldDeclaration() != null)
        {
            return;
        }

        TypeDeclaration  typeDecl = null;
        FieldDeclaration decl     = null;
        Type             type     = null;

        if (fieldAccess.isTrailing())
        {
            type = fieldAccess.getBaseExpression().getType();
            if ((type == null) ||
                type.isNullType() ||
                (type.isPrimitive() && !type.isArray()))
            {
                Global.getOutput().addError("Found field access at type "+type+" which is neither reference nor array", fieldAccess);
                decIndent();
                trace("End of access to field "+fieldAccess.getFieldName());
                return;
            }
            if (type.isArray())
            {
                if ("length".equals(fieldAccess.getFieldName()))
                {
                    decl = _project.getArrayType().getFields().get("length");
                }
                else
                {
                    Global.getOutput().addError("Found access to undefined field "+fieldAccess.getFieldName()+" of array type", fieldAccess);
                    decIndent();
                    trace("End of access to field "+fieldAccess.getFieldName());
                    return;
                }
            }
            else
            {
                // We should not need to check whether the type of the
                // base expression has been resolved
                // For safety we do it nonetheless ;-)
                if (!type.hasReferenceBaseTypeDecl())
                {
                    Global.getOutput().addError("Could not determine type "+type+" for field access", fieldAccess);
                }
                else
                {
                    decl = findField(type.getReferenceBaseTypeDecl(),
                                     fieldAccess.getFieldName());
                }
            }
        }
        else
        {
            decl = findLocalField(fieldAccess.getFieldName());
        }

        if (decl != null)
        {
            fieldAccess.setFieldDeclaration(decl);
            // We must resolve the type of the field as it could be defined
            // in another class which is not resolved
            trace("Found access to field <"+decl+"> at type "+
                               (fieldAccess.isTrailing() ? type : currentTypeDeclaration().getCreatedType()));
            if (!resolve(decl.getType(), (TypeDeclaration)decl.getContainer(), true))
            {
                Global.getOutput().addError("Could not determine type "+decl.getType()+" of accessed field", fieldAccess);
            }
        }
        else
        {
            Global.getOutput().addError("Could not determine declaration of accessed field "+fieldAccess.getFieldName(), fieldAccess);
        }
    }

    private void resolve(FormalParameter param, TypeDeclaration context)
    {
        if (param == null)
        {
            return;
        }

        if (!resolve(param.getType(), context, true))
        {
            Global.getOutput().addError("Could not determine type "+param.getType()+" of formal parameter", param);
        }
    }

    private void resolve(FormalParameterList params, TypeDeclaration context)
    {
        if (params == null)
        {
            return;
        }

        Type type;

        for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
        {
            resolve(it.getNext(), context);
        }
    }

    private void resolve(Instantiation instantiation)
    {
        Type type = instantiation.getType();

        if (!type.hasReferenceBaseTypeDecl())
        {
            // For the instantiation of inner classes we sometimes
            // encounter constructs like 'new Outer().new Inner()'
            // Therefore we require that the type of the base expression
            // has been resolved first which enables us to use it
            // now as the access source of the type access
            if (instantiation.isTrailing())
            {
                // it must be an inner type of the type of the base expression
                type.setReferenceBaseTypeDecl(
                    findInnerType(_scopes.top(),
                                  instantiation.getBaseExpression().getType().getReferenceBaseTypeDecl(),
                                  type.getQualifiedName()));
            }
            else
            {
                // it has to be a complete type
                if (!resolve(type, null, true))
                {
                    Global.getOutput().addError("Could not determine instantiated type "+type, instantiation);
                }
            }
        }

        TypeDeclaration typeDecl = type.getReferenceBaseTypeDecl();

        if (typeDecl != null)
        {
            if (instantiation.withAnonymousClass())
            {
                AnonymousClassDeclaration anonClassDecl = instantiation.getAnonymousClass();
                Type                      anonBaseType  = anonClassDecl.getBaseType();

                anonBaseType.setReferenceBaseTypeDecl(typeDecl);
                if (typeDecl instanceof InterfaceDeclaration)
                {
                    anonClassDecl.getBaseInterfaces().add(anonBaseType);
                }
                else
                {
                    try
                    {
                        anonClassDecl.setBaseClass(anonBaseType);
                    }
                    catch (SyntaxException ex)
                    {}
                }
                // To catch inheritance from java.lang.Object we resolve the type
                // (which checks for undefined base classes)
                resolveBaseTypes(anonClassDecl);
            }
            // If possible, we seek the invoked constructor
            // (it could be a default constructor)
            if ((typeDecl instanceof ClassDeclaration) &&
                (instantiation.getInvokedConstructor() == null))
            {
                ClassDeclaration       classDecl = (ClassDeclaration)typeDecl;
                ArgumentList           args      = instantiation.getArgumentList();
                ConstructorDeclaration decl      = findConstructor(classDecl, instantiation.getArgumentList());

                if (decl != null)
                {
                    trace("Found invocation of constructor <"+decl.getSignature()+">");
                    instantiation.setInvokedConstructor(decl);
                }
                else if (classDecl.getConstructors().isEmpty() && (args == null))
                {
                    trace("Found invocation of default constructor");
                }
                else
                {
                    Global.getOutput().addError("Could not determine constructor "+type+"("+
                                                (args != null ? args.getSignature() : "")+
                                                ")", instantiation);
                }
            }
        }
    }

    private void resolve(MethodInvocation methodInvoc)
    {
        if (methodInvoc.getMethodDeclaration() != null)
        {
            return;
        }

        TypeDeclaration   typeDecl = null;
        MethodDeclaration decl     = null;
        Type              type     = null;

        if (methodInvoc.isTrailing())
        {
            type = methodInvoc.getBaseExpression().getType();
            if ((type == null) || type.isNullType() || type.isPrimitive())
            {
                trace("Found access at non-reference/array type "+type);
            }
            else if (!type.hasReferenceBaseTypeDecl())
            {
                trace("Could not access type of base expression ("+type+")");
            }
            else
            {
                // We also need methods from the base types
                resolveBaseTypes(type.getReferenceBaseTypeDecl());
                if (type.isArray())
                {
                    decl = findMethod(_project.getArrayType(),
                                      methodInvoc.getMethodName(),
                                      methodInvoc.getArgumentList());
                }
                else
                {
                    decl = findMethod(type.getReferenceBaseTypeDecl(),
                                      methodInvoc.getMethodName(),
                                      methodInvoc.getArgumentList());
                }
            }
        }
        else
        {
            decl = findLocalMethod(methodInvoc.getMethodName(),
                                   methodInvoc.getArgumentList());
        }

        if (decl != null)
        {
            methodInvoc.setMethodDeclaration(decl);
            trace("Found access to method <"+decl.getSignature()+"> at type "+
                    (methodInvoc.isTrailing() ? type : currentTypeDeclaration().getCreatedType()));
            // We must resolve the return type of the method (if necessary) as it could be defined
            // in another class which is not resolved
            if (decl.hasReturnType())
            {
                if (!resolve(decl.getReturnType(),
                             ASTHelper.getTypeDeclarationOf(decl), true))
                {
                    trace("Could not resolve return type <"+decl.getReturnType()+"> of method <"+decl.getSignature()+">");
                }
            }
        }
        else
        {
            Global.getOutput().addError("Could not determine declaration of invoked method "+methodInvoc.getMethodName(), methodInvoc);
        }
    }

    public boolean resolve(SelfAccess node)
    {
        Type accessedType = node.getType();

        if ((accessedType == null) || !accessedType.hasReferenceBaseTypeDecl())
        {
            // Four cases : simple this, simple super, qualified this, qualified super
            if (node.isQualified())
            {
                accessedType = node.getTypeAccess().getType();

                if (node.isSuper())
                {
                    ClassDeclaration decl = (ClassDeclaration)accessedType.getReferenceBaseTypeDecl();

                    accessedType = decl.getBaseClass().getClone();
                }
                else
                {
                    accessedType = accessedType.getClone();
                }
            }
            else if (node.isSuper())
            {
                // simple super
                accessedType = ((ClassDeclaration)currentTypeDeclaration()).getBaseClass();
                if (accessedType != null)
                {
                    accessedType = accessedType.getClone();
                }
            }
            else
            {
                // simple this
                accessedType = Global.getFactory().createType(currentTypeDeclaration(), 0);
            }
        }
        if (accessedType != null)
        {
            if (!resolve(accessedType, null, true))
            {
                Global.getOutput().addError("Could not determine type "+accessedType+" of self access", node);
            }
            node.setType(accessedType);
            trace("Found qualified self access to base class "+node.getType());
            return true;
        }
        else
        {
            Global.getOutput().addError("Could not resolve self access", node);
            return false;
        }
    }

    /*
     * Tries to resolve the given type, that is it finds or retrieves
     * the associated type declaration. The <code>accessSource</code>
     * states at which node the type is accessed. This is for instance
     * used to find in 'a.getObj().getValue()' the return type of the
     * getObj method (the access source is 'a').
     * <code>canBeLocalInner</code> states whether the type can be a
     * local/inner type.<br>
     * The resolving algorithm is similar to Java Spec 6.5.2 except
     * that we already know that it is a type identifier.
     */
    private boolean resolve(Type type, TypeDeclaration accessPoint, boolean canBeLocalInner)
    {
        if (type == null)
        {
            return false;
        }
        if (type.isPrimitive() || type.hasReferenceBaseTypeDecl())
        {
            // Primitive or already resolved
            return true;
        }

        boolean              isCurrent = (accessPoint == null || accessPoint == currentTypeDeclaration());
        CompilationUnitScope scope     =  isCurrent ?
                                          _scopes.top() :
                                          contextFor(accessPoint);
        TypeDeclaration      owner     = scope.getCurrentType();
        TypeDeclaration      typeDecl  = null;
        String               name      = type.getBaseType().getQualifiedName();
        StringArray          nameParts = StringArray.fromString(name, ".");
        StringIterator       nameIt    = nameParts.getIterator();
        String               namePart  = nameIt.getNext();
        String               pckgName  = null;
        boolean              checkPckg = false;

        // Within the scope of the current type we can access types as follows:
        // * all local and inner types
        // * inherited inner types if not private
        // * all directly enclosing types
        // * other (inherited) inner types of the enclosing types if not private
        // * other top-level types if not private
        // * imported/qualified types with public/package-access

        // Step 1.b & 1.c (except direct imports -> only (inner) types in the current
        // compilation unit or inherited ones)
        typeDecl = (TypeDeclaration)resolveIdentifier(namePart, scope, false, canBeLocalInner);
        if (typeDecl == null)
        {
            // Step 1.g (start of qualified type name)
            checkPckg = true;
        }
        pckgName = namePart;

        while (nameIt.hasNext())
        {
            namePart = nameIt.getNext();
            pckgName = pckgName + "." + namePart;
            if (typeDecl != null)
            {
                // Step 2.b : (possibly inherited inner types)
                typeDecl = findInnerType(scope, typeDecl, namePart);
                if (typeDecl == null)
                {
                    checkPckg = true;
                }
                else
                {
                    trace("Found access to inner type "+typeDecl.getQualifiedName());
                }
            }
            if (checkPckg)
            {
                // Step 2.a (qualified type name)
                typeDecl = retrieveType(scope, pckgName);
                if (typeDecl != null)
                {
                    checkPckg = false;
                    trace("Found qualified access to type "+typeDecl.getQualifiedName());
                }
            }
        }
        if (typeDecl != null)
        {
            type.setReferenceBaseTypeDecl(typeDecl);
            return true;
        }
        else
        {
            Global.getOutput().addError("Could not determine type "+type+" within type "+owner.getQualifiedName(), null);
            return false;
        }
    }

    public void resolve(TypeDeclaration typeDecl)
    {
        if (typeDecl == null)
        {
            return;
        }

        CompilationUnit unit = ASTHelper.getCompilationUnitOf(typeDecl);

        if ((unit != null) && (unit != currentCompilationUnit()))
        {
            resolve(unit);
        }
    }

    // Resolves the given UnresolvedAccess, ie replaces it by appropiate field/type accesses
    private void resolve(UnresolvedAccess node)
    {
        // Cf. Java Spec 6.5.2 for the steps
        NodeFactory     factory   = Global.getFactory();
        StringArray     parts     = node.getParts();
        boolean         checkPckg = false;
        Primary         baseExpr  = null;
        Primary         curExpr   = null;
        TypeDeclaration typeDecl  = null;

        if (node.isTrailing())
        {
            curExpr = node.getBaseExpression();
        }
        else
        {
            curExpr = algorithm652step1(parts.get(0));
            if (curExpr != null)
            {
                curExpr.setStartPosition(node.getStartPosition());
                curExpr.setFinishPosition(node.getFinishPosition(0));
            }
            else
            {
                // note that fully qualified type names are only allowed for
                // non-trailing unresolved accesses (obviously)
                checkPckg = true;
            }
        }
        for (int idx = node.isTrailing() ? 0 : 1; idx < parts.getCount(); idx++)
        {
            baseExpr = curExpr;
            if (baseExpr != null)
            {
                curExpr = algorithm652step2(baseExpr, parts.get(idx));
                if (curExpr != null)
                {
                    curExpr.setStartPosition(node.getStartPosition());
                    curExpr.setFinishPosition(node.getFinishPosition(idx));
                    trace("Found access to "+baseExpr+" of type "+baseExpr.getType());
                }
                if (curExpr == null)
                {
                    if (node.isTrailing() || (baseExpr instanceof FieldAccess))
                    {
                        Global.getOutput().addError("Could not resolve unresolved access "+node, node);
                        return;
                    }
                    else
                    {
                        checkPckg = true;
                    }
                }
            }
            if (checkPckg)
            {
                // Step 2.a
                typeDecl = retrieveType(_scopes.top(), parts.asString(".", idx+1));
                if (typeDecl != null)
                {
                    curExpr   = factory.createTypeAccess(null, typeDecl.getCreatedType());
                    if (curExpr != null)
                    {
                        curExpr.setStartPosition(node.getStartPosition());
                        curExpr.setFinishPosition(node.getFinishPosition(idx));
                    }
                    checkPckg = false;
                    trace("Found qualified access to type "+typeDecl.getQualifiedName());
                }
            }
        }
        if (curExpr != null)
        {
            // we have to update the container
            new ExpressionReplacementVisitor().replace(node, curExpr);

            // as we did some sort of parsing by replacing the unresolved access
            // with field/type accesses, we also must reassign its comments
            new CommentingVisitor().comment(curExpr, node.getComments());
        }
    }

    public void resolve(Project project)
    {
        setProject(project);

        // We don't use the iterator because any parsed type
        // would be resolved automatically which is probably
        // not what we want
        CompilationUnitArray compUnits = project.getCompilationUnits();
        int                  num       = compUnits.getCount();

        // num gives only the original (pre-resolving) number
        // of types
        for (int idx = 0; idx < num; idx++)
        {
            resolve(compUnits.get(idx));
        }
    }

    // Resolves the base types of the given type
    // Note that this is a deep resolve, iow all types in
    // the inheritance hierarchy are retrieved and
    // resolved (only their interfaces to be precise)
    // This is necessary since the check isSubTypeOf in
    // the type is important and relies on the hierarchy
    private void resolveBaseTypes(TypeDeclaration typeDecl)
    {
        if (typeDecl instanceof ClassDeclaration)
        {
            ClassDeclaration classDecl = (ClassDeclaration)typeDecl;

            // The only class with no base class is "java.lang.Object"
            // Note that anonymous classes do not have a name (getQualifiedName() == null)
            if (!"java.lang.Object".equals(classDecl.getQualifiedName()))
            {
                // All other classes either have a explicit base class
                if (classDecl.getBaseClass() == null)
                {
                    // or they inherit from "Object"
                    trace("Class "+classDecl.getQualifiedName()+" inherits from java.lang.Object");
                    try
                    {
                        classDecl.setBaseClass(Global.getFactory().createType("java.lang.Object", 0));
                    }
                    catch (SyntaxException ex)
                    {}
                }
                if (classDecl.getBaseClass() != null)
                {
                    if (!resolve(classDecl.getBaseClass(), typeDecl, true))
                    {
                        Global.getOutput().addError("Could not determine base class of "+classDecl.getQualifiedName(), classDecl);
                    }
                    // Descend into the hierarchy
                    resolve(classDecl.getBaseClass().getReferenceBaseTypeDecl());
                }
            }
        }

        Type baseType;

        for (TypeIterator it = typeDecl.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            baseType = it.getNext();
            if (!resolve(baseType, typeDecl, true))
            {
                Global.getOutput().addError("Could not determine base interface of "+typeDecl.getQualifiedName(), typeDecl);
            }
            // Descend into the hierarchy
            resolve(baseType.getReferenceBaseTypeDecl());
        }
    }

    private Node resolveIdentifier(String               name,
                                   CompilationUnitScope scope,
                                   boolean              checkVariables,
                                   boolean              checkLocalsAndInner)
    {
        Node            result  = null;
        TypeDeclaration curType;
        Scope           curScope;

        // Step 1.a/b
        for (ScopeIterator it = scope.getIterator(); it.hasNext() && (result == null);)
        {
            curScope = it.getNext();
            if (curScope.isTypeScope())
            {
                curType = curScope.getTypeDeclaration();
                if (checkVariables)
                {
                    result = findField(curScope.getTypeDeclaration(), name, true, true, true);
                }
                if (result == null)
                {
                    if (name.equals(curType.getName()))
                    {
                        result = curType;
                    }
                    else if (checkLocalsAndInner)
                    {
                        result = findInnerType(scope, curType, name);
                    }
                }
            }
            else
            {
                if (checkVariables)
                {
                    result = curScope.resolveVariable(name);
                }
                if ((result == null) && checkLocalsAndInner)
                {
                    result = curScope.resolveLocalClass(name);
                }
            }
        }
        if (result == null)
        {
            result = scope.getCompilationUnit().getTypeDeclarations().get(name);
        }
        if (result == null)
        {
            // Step 1.c (direct imports)
            result = retrieveDirectlyImportedType(scope, name);
        }
        if (result == null)
        {
            // Step 1.d (types in the same package)
            result = retrieveTypeInSamePackage(scope, name);
        }
        if (result == null)
        {
            // Step 1.e (types in other packages)
            result = retrieveType(scope, name);
        }
        // Step 1.f is implicit in caller
        // Step 1.g is not necessary (Code is assumed to be correct)
        return result;
    }

    private TypeDeclaration retrieveDirectImport(String qualifiedName)
    {
        StringArray     nameParts = StringArray.fromString(qualifiedName, ".");
        TypeDeclaration typeDecl  = null;
        int             idx       = 0;

        for (; idx < nameParts.getCount(); idx++)
        {
            if (typeDecl != null)
            {
                typeDecl = typeDecl.getInnerTypes().get(nameParts.get(idx));
                if (typeDecl == null)
                {
                    // can be ok on systems that have case-insensitive names
                    // on these systems we would have acquired the type in the
                    // previous step (eg java.awt.Font instead of java.awt.font)
                    // and now we would not find an inner type (eg 'OpenType')
                    // in such cases we simply start over with the qualified
                    // name search
                }
            }
            if (typeDecl == null)
            {
                typeDecl = retrieveTypeDirect(nameParts.asString(".", idx+1));
            }
        }
        return typeDecl;
    }

    // Tries to retrieve a find the given type via the direct
    // imports
    private TypeDeclaration retrieveDirectlyImportedType(CompilationUnitScope scope,
                                                         String               name)
    {
        TypeDeclaration result = scope.getDirectImport(name);

        // Most likely it has not been loaded yet
        // (contexts for types other than the current are usually
        // short lived which means that it does not reference
        // any retrieved imports)
        if (result == null)
        {
            ImportDeclaration importDecl = scope.getDirectImportName(name);

            if (importDecl != null)
            {
                // There is a direct import for the name
                result = retrieveDirectImport(importDecl.getImportedPackageOrType());
                if (result != null)
                {
                    scope.setDirectImport(name, result);
                }
                else
                {
                    Global.getOutput().addError("Could not retrieve directly imported type "+importDecl.getImportedPackageOrType(), importDecl);
                }
            }
        }
        // Assuming that the code compiles we does not have to check
        // whether we can access the type (would result in a compile error)
        return result;
    }

    // Retrieves the given top-level type. If the name is not qualified
    // the method uses the imports to determine the actual type
    // Note that this does not handle forms like 'Outer.Inner'
    private TypeDeclaration retrieveType(CompilationUnitScope scope,
                                         String               name)
    {
        TypeDeclaration result = null;

        if (ASTHelper.isQualified(name))
        {
            result = retrieveTypeDirect(name);
            if (!canAccess(scope, result))
            {
                result = null;
            }
        }
        else
        {
            result = retrieveDirectlyImportedType(scope, name);

            if (result == null)
            {
                // No direct import of that name -> check current package
                String qualifiedName = scope.getCompilationUnit().getPackage().getQualifiedName();

                if ((qualifiedName == null) || (qualifiedName.length() == 0))
                {
                    // current compilation unit is in the default package
                    qualifiedName = name;
                }
                else
                {
                    qualifiedName += "." + name;
                }
                result = retrieveTypeDirect(qualifiedName);
            }
            if (!canAccess(scope, result))
            {
                result = null;
            }

            if (result == null)
            {
                // check current on-demand imports
                // Note that classes from the default package cannot
                // be imported this way ('import *;' is illegal)
                for (StringIterator it = scope.getImports(); it.hasNext();)
                {
                    result = retrieveTypeDirect(it.getNext() + "." + name);
                    if (canAccess(scope, result))
                    {
                        break;
                    }
                    else
                    {
                        result = null;
                    }
                }
            }
        }
        return result;
    }

    // Returns a stored type or reads it if it has not been parsed yet
    private TypeDeclaration retrieveTypeDirect(String qualifiedName)
    {
        if (qualifiedName == null)
        {
            return null;
        }

        TypeDeclaration result = getStoredType(qualifiedName);

        if ((result == null) && parseType(qualifiedName))
        {
            result = getStoredType(qualifiedName);
            if (result != null)
            {
                trace("Retrieved type "+result.getQualifiedName());
            }
        }
        return result;
    }

    // Tries to retrieve a type defined the current package (as
    // given by the scope).
    private TypeDeclaration retrieveTypeInSamePackage(CompilationUnitScope scope,
                                                      String               name)
    {
        String qualifiedName = scope.getCompilationUnit().getPackage().getQualifiedName();

        // Default package ?
        if (qualifiedName.length() > 0)
        {
            qualifiedName += ".";
        }
        qualifiedName += name;

        TypeDeclaration result = retrieveType(scope, qualifiedName);

        if ((result!= null) && result.getModifiers().checkAccess(false, false, true, true))
        {
            return result;
        }
        return null;
    }

    public void setParser(Parser parser)
    {
        _parser = parser;
    }

    public void setProject(Project project)
    {
        _project = project;
    }

    private void startBlockScope()
    {
        _scopes.top().pushBlockScope();
    }

    private void startTypeScope(TypeDeclaration decl)
    {
        if (isLocal(decl) &&
            !(decl instanceof AnonymousClassDeclaration))
        {
            _scopes.top().defineLocalClass((ClassDeclaration)decl);
        }
        _scopes.top().pushTypeScope(decl);
    }

    private void startUnitScope(CompilationUnit node)
    {
        _scopes.push(node);
    }

    private void stopBlockScope()
    {
        _scopes.top().popBlockScope();
    }

    private void stopTypeScope()
    {
        _scopes.top().popTypeScope();
    }

    private void stopUnitScope()
    {
        _scopes.pop();
    }

    private void trace(String text)
    {
        //_tracer.write(_indent.toString());
        //_tracer.writeln(text);
    }


    /*
     * Tries to resolve the given type within the given type declaration.
     * Note that the latter states the surrounding type, that means, the
     * type object for instance denotes a field type. In contrast, base
     * types for the type declaration are not within it but within the
     * enclosing type declaration. Note also that a surrounding type
     * declaration has to be given.
     */
    public boolean resolveExternal(Type type, TypeDeclaration surroundingTypeDecl)
    {
        if (surroundingTypeDecl == null)
        {
            return false;
        }
        setProject(ASTHelper.getProjectOf(surroundingTypeDecl));
        return resolve(type, surroundingTypeDecl, true);
    }
}
