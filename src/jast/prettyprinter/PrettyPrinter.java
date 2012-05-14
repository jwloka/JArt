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
package jast.prettyprinter;

import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.ParsePosition;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BlockStatement;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Comment;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.Expression;
import jast.ast.nodes.ExpressionList;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.Primary;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Statement;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TrailingPrimary;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.WhileStatement;
import jast.ast.nodes.collections.BlockStatementArray;
import jast.ast.nodes.collections.CaseBlockArray;
import jast.ast.nodes.collections.CaseBlockIterator;
import jast.ast.nodes.collections.CatchClauseIterator;
import jast.ast.nodes.collections.CommentArray;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.ExpressionIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.ImportIterator;
import jast.ast.nodes.collections.InitializerIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;
import jast.ast.nodes.collections.VariableInitializerIterator;
import jast.ast.visitor.DescendingVisitor;
import jast.helpers.StringIterator;
import jast.parser.Scope;
import jast.parser.ScopeIterator;
import jast.parser.ScopeStack;

import java.util.Vector;

public class PrettyPrinter extends DescendingVisitor
{
    private class Context
    {
        // determines whether in the current expression all dots are broken
        public boolean _breakAllDots              = false;

        // gives the relative indentation base column
        public int     _listAlignmentBase         = 0;

        // determines whether a block writes a NL after the closing brace
        public boolean _finishBlockWithNL         = true;

        // alignment column for subsequent assignment expressions
        // or a formal parameter list
        public int     _alignmentColumn           = 0;

        // whether comments should be written for the current node
        // (must be reset by this node)
        public boolean _writeComments             = true;

        // determines whether the abstract modifier shall be written
        public boolean _writeAbstractModifier     = true;

        // determines whether the field access modifiers (visibility, static, final) shall be written
        public boolean _writeFieldAccessModifiers = true;
    }

    private Options         _options = new Options();
    private IndentPrinter   _printer = new IndentPrinter(new SimpleStyleWriter(), _options);
    private CompilationUnit _curUnit;
    private ScopeStack      _scopes  = new ScopeStack();

    // contains contexts, for instance for trailing primaries
    private Vector          _contexts = new Vector();

    public PrettyPrinter()
    {}

    public PrettyPrinter(StyleWriter writer)
    {
        setStyleWriter(writer);
    }

    private void adjustPaddingLength(int additionalAmount)
    {
        _printer.setPadding(_printer.getPadding() + additionalAmount);
    }

    private void appendComments(CommentArray comments)
    {
        if ((comments == null) || comments.isEmpty())
        {
            return;
        }

        // first we check whether comment writing is postponed
        if ((topContext() != null) && !topContext()._writeComments)
        {
            // we reenable it
            topContext()._writeComments = true;
            return;
        }

        boolean isEOL = false;

        if ((comments.getCount() == 1) &&
            !comments.get(0).isBlockComment())
        {
            String comment = comments.get(0).getText();;

            if ((comment.length() <= _options.getMaxLineLength() - _options.getEolCommentColumn()) &&
                (getColumn() < _options.getEolCommentColumn()))
            {
                isEOL = true;
            }
        }
        if (isEOL)
        {
            StringBuffer padding = new StringBuffer();

            for (int curColumn = getColumn(); curColumn < _options.getEolCommentColumn(); curColumn++)
            {
                padding.append(" ");
            }
            _printer.write(padding.toString());
            writeLineComment(comments.get(0));
        }
        else
        {
            writeComments(comments, true);
        }
    }

    private String assignmentOperatorAsText(int operator)
    {
        switch (operator)
        {
            case AssignmentExpression.MULTIPLY_ASSIGN_OP :
                return "*=";
            case AssignmentExpression.DIVIDE_ASSIGN_OP :
                return "/=";
            case AssignmentExpression.MOD_ASSIGN_OP :
                return "%=";
            case AssignmentExpression.PLUS_ASSIGN_OP :
                return "+=";
            case AssignmentExpression.MINUS_ASSIGN_OP :
                return "-=";
            case AssignmentExpression.SHIFT_LEFT_ASSIGN_OP :
                return "<<=";
            case AssignmentExpression.SHIFT_RIGHT_ASSIGN_OP :
                return ">>=";
            case AssignmentExpression.ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP :
                return ">>>=";
            case AssignmentExpression.BITWISE_AND_ASSIGN_OP :
                return "&=";
            case AssignmentExpression.BITWISE_XOR_ASSIGN_OP :
                return "<=";
            case AssignmentExpression.BITWISE_OR_ASSIGN_OP :
                return "|=";
        }
        return "=";
    }

    private String binaryOperatorAsText(int operator)
    {
        switch (operator)
        {
            case BinaryExpression.MULTIPLY_OP :
                return "*";
            case BinaryExpression.DIVIDE_OP :
                return "/";
            case BinaryExpression.MOD_OP :
                return "%";
            case BinaryExpression.PLUS_OP :
                return "+";
            case BinaryExpression.MINUS_OP :
                return "-";
            case BinaryExpression.SHIFT_LEFT_OP :
                return "<<";
            case BinaryExpression.SHIFT_RIGHT_OP :
                return ">>";
            case BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP :
                return ">>>";
            case BinaryExpression.LOWER_OP :
                return "<";
            case BinaryExpression.GREATER_OP :
                return ">";
            case BinaryExpression.LOWER_OR_EQUAL_OP :
                return "<=";
            case BinaryExpression.GREATER_OR_EQUAL_OP :
                return ">=";
            case BinaryExpression.EQUAL_OP :
                return "==";
            case BinaryExpression.NOT_EQUAL_OP :
                return "!=";
            case BinaryExpression.BITWISE_AND_OP :
                return "&";
            case BinaryExpression.BITWISE_XOR_OP :
                return "^";
            case BinaryExpression.BITWISE_OR_OP :
                return "|";
            case BinaryExpression.AND_OP :
                return "&&";
            case BinaryExpression.OR_OP :
                return "||";
            default:
                return "";
        }
    }

    private boolean canUseShortName(TypeDeclaration target)
    {
        String           targetQualifiedName = target.getQualifiedName();
        String           targetName          = target.getName();
        ClassDeclaration targetClass         = null;

        if (target instanceof ClassDeclaration)
        {
            targetClass = (ClassDeclaration)target;
        }

        // local types are always used unqualified
        if ((targetClass != null) && targetClass.isLocal())
        {
            return true;
        }

        // checking local types
        TypeDeclaration foundTypeDecl = findLocalType(targetName);

        if (foundTypeDecl != null)
        {
            return (foundTypeDecl == target);
        }

        // checking direct imports
        boolean           importsPckg = false;
        String            importName;
        int               pos;
        ImportDeclaration curImport;

        for (ImportIterator it = _curUnit.getImportDeclarations().getIterator(); it.hasNext();)
        {
            curImport  = it.getNext();
            importName = curImport.getImportedPackageOrType();
            if (curImport.isOnDemand())
            {
                if (importName.equals(target.getPackage().getQualifiedName()))
                {
                    importsPckg = true;
                }
            }
            else
            {
                pos = importName.lastIndexOf('.');
                if (pos >= 0)
                {
                    importName = importName.substring(pos+1);
                }
                if (targetName.equals(importName))
                {
                    return targetQualifiedName.equals(curImport.getImportedPackageOrType());
                }
            }
        }

        // checking same package
        foundTypeDecl = _curUnit.getPackage().getTypes().get(targetName);

        if (foundTypeDecl != null)
        {
            return (foundTypeDecl == target);
        }

        // checking on-demand imports
        if (importsPckg)
        {
            return true;
        }
        else
        {
            pos = targetQualifiedName.lastIndexOf('.');
            return (pos >= 0) &&
                   "java.lang".equals(targetQualifiedName.substring(0, pos));
        }
    }

    public void decIndent(int amount)
    {
        _printer.decIndent(amount);
    }

    // Determines the textual representation of the given type
    // We can use the unqualified name of the type iff
    // * the type is defined within the current unit or
    //   it is an inherited inner/nested type
    // * it has been imported directly
    // * it is in the same package as the current unit
    // * it has been imported on-demand (including java.lang)
    // These rules are checked in that order. If there is a different
    // type with the same short name for which a higher rule applies
    // then the type in question must be used fully qualified
    // Nested types can use a semi-qualified name by importing its
    // owning toplevel type and using a name like 'TopLevel.Nested'
    private String determineName(TypeDeclaration target)
    {
        TypeDeclaration curTypeDecl = target;
        String          result      = null;

        while ((curTypeDecl != null) && (result == null))
        {
            if (canUseShortName(curTypeDecl))
            {
                result = target.getQualifiedName().substring(
                            curTypeDecl.getQualifiedName().lastIndexOf('.') + 1);
            }
            if (curTypeDecl.isTopLevel())
            {
                break;
            }
            else
            {
                curTypeDecl = curTypeDecl.getEnclosingType();
            }
        }

        if (result == null)
        {
            result = target.getQualifiedName();
        }
        return result;
    }

    private void ensureNewLine()
    {
        if (!_printer.isNewLine())
        {
            _printer.writeln();
        }
    }

    private TypeDeclaration findInheritedInnerType(TypeDeclaration typeDecl, String name)
    {
        if (typeDecl == null)
        {
            return null;
        }

        TypeDeclaration result = null;
        Modifiers       mods   = null;

        for (TypeDeclarationIterator it = typeDecl.getInnerTypes().getIterator();
             (result == null) && it.hasNext();)
        {
            result = it.getNext();
            mods   = result.getModifiers();
            if (!name.equals(result.getName()) ||
                (mods.isPrivate() && (ASTHelper.getCompilationUnitOf(result) != _curUnit)) ||
                (mods.isFriendly() && !result.getPackage().isEqualTo(_curUnit.getPackage())))
            {
                result = null;
            }
        }
        if (result == null)
        {
            if ((typeDecl instanceof ClassDeclaration) &&
                ((ClassDeclaration)typeDecl).hasBaseClass())
            {
                result = findInheritedInnerType(
                            ((ClassDeclaration)typeDecl).getBaseClass().getReferenceBaseTypeDecl(),
                            name);
            }
        }
        for (TypeIterator it = typeDecl.getBaseInterfaces().getIterator();
             (result == null) && it.hasNext();)
        {
            result = findInheritedInnerType(
                        it.getNext().getReferenceBaseTypeDecl(),
                        name);
        }
        return result;
    }

    private TypeDeclaration findLocalType(String name)
    {
        TypeDeclaration result = null;
        Scope           curScope;

        for (ScopeIterator it = _scopes.getIterator(); it.hasNext();)
        {
            curScope = it.getNext();

            if (curScope.isTypeScope())
            {
                result = curScope.getTypeDeclaration();
                if (!name.equals(result.getName()))
                {
                    result = findInheritedInnerType(result, name);
                }
            }
            else
            {
                result = curScope.resolveLocalClass(name);
            }
            if (result != null)
            {
                break;
            }
        }
        return result;
    }

    private String getAllExceptLastPart(String qualifiedName)
    {
        if (qualifiedName == null)
        {
            return null;
        }

        int pos = qualifiedName.lastIndexOf('.');

        return (pos >= 0 ? qualifiedName.substring(0, pos) : qualifiedName);
    }

    private Primary getBaseExpression(Primary primary)
    {
        if ((primary instanceof TrailingPrimary) &&
            ((TrailingPrimary)primary).isTrailing())
        {
            return ((TrailingPrimary)primary).getBaseExpression();
        }
        else if ((primary instanceof SelfAccess) &&
                 ((SelfAccess)primary).isQualified())
        {
            return ((SelfAccess)primary).getTypeAccess();
        }
        return null;
    }

    private int getColumn()
    {
        return _printer.getPosition().getColumn();
    }

    private String getLastPart(String qualifiedName)
    {
        if (qualifiedName == null)
        {
            return null;
        }

        int pos = qualifiedName.lastIndexOf('.');

        return (pos >= 0 ? qualifiedName.substring(pos+1) : qualifiedName);
    }

    private int getLine()
    {
        return _printer.getPosition().getLine();
    }

    public Options getOptions()
    {
        return _options;
    }

    private int getPaddingLength(int targetColumn)
    {
        int result = targetColumn - _printer.getIndentLength() - 1;

        return (result > 0 ? result : 0);
    }

    public void incIndent(int amount)
    {
        _printer.incIndent(amount);
    }

    private boolean isTooLong(int startLine)
    {
        return (getLine()   > startLine) ||
               (getColumn() > _options.getMaxLineLength());
    }

    private boolean isTrailEnd(Primary primary)
    {
        Node father = primary.getContainer();

        return !(father instanceof Primary) ||
               (getBaseExpression((Primary)father) != primary);
    }

    private String modifiersToString(Modifiers mods)
    {
        StringBuffer result         = new StringBuffer();
        boolean      writeAbstract  = (topContext() != null ?
                                           topContext()._writeAbstractModifier :
                                           true);
        boolean      writeFieldMods = (topContext() != null ?
                                           topContext()._writeFieldAccessModifiers :
                                           true);

        if (mods.isPublic() && writeFieldMods)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("public");
        }
        if (mods.isProtected() && writeFieldMods)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("protected");
        }
        if (mods.isPrivate() && writeFieldMods)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("private");
        }
        if (mods.isStatic() && writeFieldMods)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("static");
        }
        if (mods.isAbstract() && writeAbstract)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("abstract");
        }
        if (mods.isFinal() && writeFieldMods)
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("final");
        }
        if (mods.isNative())
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("native");
        }
        if (mods.isSynchronized())
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("synchronized");
        }
        if (mods.isTransient())
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("transient");
        }
        if (mods.isVolatile())
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("volatile");
        }
        if (mods.isStrictfp())
        {
            if (result.length() > 0)
            {
                result.append(' ');
            }
            result.append("strictfp");
        }
        return result.toString();
    }

    private void popContext()
    {
        _contexts.removeElementAt(_contexts.size() - 1);
    }

    private void postWritePrimary(CommentArray comments, boolean unIndent)
    {
        appendComments(comments);
        if (unIndent)
        {
            decIndent(_options.getDotBreakIndentationLevel());
        }
    }

    private void prependComments(CommentArray comments)
    {
        if ((comments != null) && !comments.isEmpty())
        {
            // first we check whether comment writing is postponed
            if ((topContext() != null) && !topContext()._writeComments)
            {
                // we reenable it
                topContext()._writeComments = true;
                return;
            }
            writeComments(comments, false);
            ensureNewLine();
        }
    }

    private void preWritePrimary(Primary baseExpr, boolean withBreak)
    {
        baseExpr.accept(this);
        if (!withBreak)
        {
            _printer.write(".");
        }
        else if (_printer.isNewLine())
        {
            // we only break if not already on a new line
            // (because of an eol comment for instance)
            // in that case we only adjust the indentation level
            incIndent(_options.getDotBreakIndentationLevel());
            _printer.write(".");
        }
        else
        {
            if (_options.isBreakBeforeDot())
            {
                incIndent(_options.getDotBreakIndentationLevel());
                _printer.writeln();
            }
            _printer.write(".");
            if (!_options.isBreakBeforeDot())
            {
                incIndent(_options.getDotBreakIndentationLevel());
                _printer.writeln();
            }
        }
    }

    private void pushContext()
    {
        _contexts.addElement(new Context());
    }

    private int separateFeatureBlock(int startLine)
    {
        ensureNewLine();
        if (getLine() > startLine)
        {
            writeEmptyLines(_options.getTypeDeclEmptyLinesBeforeFeatureBlock());
            return getLine();
        }
        else
        {
            return startLine;
        }
    }

    public void setCurrentCompilationUnit(CompilationUnit unit)
    {
        _curUnit = unit;
    }

    public void setCurrentTypeDeclaration(TypeDeclaration typeDecl)
    {
        startTypeScope(typeDecl);
        if (_curUnit == null)
        {
            setCurrentCompilationUnit(ASTHelper.getCompilationUnitOf(typeDecl));
        }
    }

    private void setFinishPosition(Node node)
    {
        ParsePosition pos = _printer.getPosition();

        node.getFinishPosition().set(pos.getLine(), pos.getColumn()-1, pos.getAbsolute()-1);
    }

    private void setStartPosition(Node node)
    {
        node.setStartPosition(_printer.getPosition());
    }

    public void setStyleWriter(StyleWriter writer)
    {
        _printer = new IndentPrinter(writer, _options);
    }

    private String slimUnicodeEscape(String source)
    {
        StringBuffer result   = new StringBuffer();
        boolean      isEscape = false;
        char         chr;

        for (int idx = 0; idx < source.length(); idx++)
        {
            chr = source.charAt(idx);
            if (chr == '\\')
            {
                isEscape = !isEscape;
            }
            else if ((chr == 'u') && isEscape)
            {
                // skipping
                while (source.charAt(idx + 1) == 'u')
                {
                    idx++;
                }
                isEscape = false;
            }
            result.append(chr);
        }
        return result.toString();
    }

    private void startBlockScope()
    {
        _scopes.pushBlockScope();
    }

    private void startTypeScope(TypeDeclaration decl)
    {
        if (decl instanceof ClassDeclaration)
        {
            ClassDeclaration classDecl = (ClassDeclaration)decl;

            if (classDecl.isLocal() &&
                !(classDecl instanceof AnonymousClassDeclaration))
            {
                _scopes.defineLocalClass(classDecl);
            }
        }
        _scopes.pushTypeScope(decl);
    }

    private void stopBlockScope()
    {
        _scopes.popBlockScope();
    }

    private void stopTypeScope()
    {
        _scopes.popTypeScope();
    }

    private Context topContext()
    {
        return _contexts.isEmpty() ? null : (Context)_contexts.lastElement();
    }

        private String typeToString(Type type)
        {
            StringBuffer text = new StringBuffer();

            if (!type.hasReferenceBaseTypeDecl())
            {
                text.append(type.getBaseName());
            }
            else
            {
                // we derive the local name if applicable
                text.append(determineName(type.getReferenceBaseTypeDecl()));
    /*
                TypeDeclaration typeDecl   = type.getReferenceBaseTypeDecl();
                String          targetName = type.getQualifiedName();
                String          sourceName = _curTypeDecl.getQualifiedName();
                String          name       = null;

                if (typeDecl.isTopLevel())
                {
                    if (sourceName.startsWith(getAllExceptLastPart(targetName)))
                    {
                        // current is inner of target or
                        // in same package as target
                        name = getLastPart(targetName);
                    }
                }
                else
                {
                    // we can access it locally if it is defined in
                    // the same compilation unit or if we inherited it
                    name = checkLocal(_curTypeDecl,
                                      typeDecl,
                                      false);
                }
                if (name == null)
                {
                    ImportDeclaration curImport;

                    // checking imports
                    sourceName = "java.lang";
                    if (targetName.startsWith(sourceName))
                    {
                        name = targetName.substring(sourceName.length() + 1);
                    }
                    else
                    {
                        for (ImportIterator it = _curUnit.getImportDeclarations().getIterator(); it.hasNext();)
                        {
                            curImport  = it.getNext();
                            sourceName = curImport.getImportedPackageOrType();
                            if (targetName.startsWith(sourceName))
                            {
                                if (curImport.isOnDemand())
                                {
                                    name = targetName.substring(sourceName.length() + 1);
                                }
                                else
                                {
                                    name = getLastPart(sourceName);
                                }
                                break;
                            }
                        }
                    }
                }
                if (name != null)
                {
                    text.append(name);
                }
                else
                {
                    text.append(type.getQualifiedName());
                }
    */
            }
            for (int idx = 0; idx < type.getDimensions(); idx++)
            {
                text.append("[]");
            }
            return text.toString();
        }

    public void visitAnonymousClassDeclaration(AnonymousClassDeclaration anonClassDecl)
    {
        if (_options.isAnonClassDeclBodyStartOnSameLine())
        {
            writeSpaceIfNecessary();
        }
        else
        {
            ensureNewLine();
        }
        incIndent(_options.getAnonClassDeclIndentationLevel());
        startTypeScope(anonClassDecl);
        writeTypeBody(anonClassDecl);
        stopTypeScope();
        decIndent(_options.getAnonClassDeclIndentationLevel());
    }

    public void visitArgumentList(ArgumentList args)
    {
        writeExpressionList(args);
    }

    public void visitArrayAccess(ArrayAccess arrayAccess)
    {
        setStartPosition(arrayAccess);

        if (isTrailEnd(arrayAccess))
        {
            pushContext();
        }
        getBaseExpression(arrayAccess).accept(this);

        int startLine = getLine();

        _printer.startTryMode();
        _printer.write("[");
        if (_options.isArrExprSpacesAroundIndex())
        {
            _printer.write(" ");
        }

        arrayAccess.getIndexExpression().accept(this);

        boolean isTooLong = isTooLong(startLine);

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            if (_options.isArrExprBreakBeforeBracket())
            {
                _printer.writeln();
                incIndent(_options.getArrExprIndentationLevel());
            }
            _printer.write("[");
            if (!_options.isArrExprBreakBeforeBracket())
            {
                _printer.writeln();
                incIndent(_options.getArrExprIndentationLevel());
            }
            else if (_options.isArrExprSpacesAroundIndex())
            {
                _printer.write(" ");
            }
            arrayAccess.getIndexExpression().accept(this);
            decIndent(_options.getArrExprIndentationLevel());
        }
        if (_options.isArrExprSpacesAroundIndex())
        {
            writeSpaceIfNecessary();
        }
        _printer.write("]");
        setFinishPosition(arrayAccess);
        if (isTrailEnd(arrayAccess))
        {
            popContext();
        }
        appendComments(arrayAccess.getComments());
    }

    public void visitArrayCreation(ArrayCreation arrayCreation)
    {
        setStartPosition(arrayCreation);

        int     startLine   = getLine();
        int     startColumn = getColumn();
        int     dim         = 0;
        boolean isTooLong;

        _printer.startStyle(Style.KEYWORD);
        _printer.write("new");
        _printer.stopStyle(Style.KEYWORD);
        _printer.write(" ");
        visitType(arrayCreation.getCreatedType().getBaseType());

        for (; dim < arrayCreation.getDimExpressions().getCount(); dim++)
        {
            _printer.startTryMode();
            _printer.write("[");
            if (_options.isArrExprSpacesAroundIndex())
            {
                _printer.write(" ");
            }
            arrayCreation.getDimExpressions().get(dim).accept(this);

            isTooLong = isTooLong(startLine);
            _printer.stopTryMode(!isTooLong);
            if (isTooLong)
            {
                if (_options.isArrExprBreakBeforeBracket())
                {
                    _printer.writeln();
                    incIndent(_options.getArrExprIndentationLevel());
                }
                _printer.write("[");
                if (!_options.isArrExprBreakBeforeBracket())
                {
                    _printer.writeln();
                    incIndent(_options.getArrExprIndentationLevel());
                }
                else if (_options.isArrExprSpacesAroundIndex())
                {
                    _printer.write(" ");
                }
                arrayCreation.getDimExpressions().get(dim).accept(this);
                decIndent(_options.getArrExprIndentationLevel());
                startLine = getLine();
            }
            if (_options.isArrExprSpacesAroundIndex())
            {
                writeSpaceIfNecessary();
            }
            _printer.write("]");
        }
        for (; dim < arrayCreation.getDimensions(); dim++)
        {
            // we dont break unspecified dimensions
            _printer.write("[]");
        }
        setFinishPosition(arrayCreation);

        if (arrayCreation.getInitializer() != null)
        {
            boolean shouldTry = (_options.getArrCreationInitBreakMode() != Options.ARRCREATION_NO_BREAK);

            if (shouldTry)
            {
                pushContext();
                topContext()._listAlignmentBase = startColumn;
                _printer.startTryMode();
            }
            writeSpaceIfNecessary();
            arrayCreation.getInitializer().accept(this);

            if (shouldTry)
            {
                isTooLong = isTooLong(startLine);
                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    _printer.writeln();
                    arrayCreation.getInitializer().accept(this);
                }
                popContext();
            }
            arrayCreation.setFinishPosition(arrayCreation.getInitializer().getFinishPosition());
        }
        appendComments(arrayCreation.getComments());
    }

    public void visitArrayInitializer(ArrayInitializer initializer)
    {
        boolean breakAll   = false;
        int     startLine  = getLine();

        // we have to determine the amount of padding characters
        // before we indent
        boolean pad        = (_options.getArrInitIndentationMode() != Options.ARRINIT_NORMAL_INDENTATION);
        int     padColumns = getPaddingLength(
                                 _options.getArrInitIndentationMode() == Options.ARRINIT_ALIGN_SUB_INITIALIZERS ?
                                     getColumn() + 2 :
                                     topContext()._listAlignmentBase);

        incIndent(_options.getArrInitIndentationLevel());

        if ((_options.getArrInitBreakLimit() > 0) &&
            (initializer.getInitializers().getCount() >= _options.getArrInitBreakLimit()))
        {
            breakAll = true;
        }

        pushContext();
        topContext()._listAlignmentBase = getColumn();

        setStartPosition(initializer);
        _printer.write("{ ");
        if (pad)
        {
            adjustPaddingLength(padColumns);
        }

        if (!breakAll)
        {
            _printer.startTryMode();

            VariableInitializerIterator it = initializer.getInitializers().getIterator();

            it.getNext().accept(this);
            for (; it.hasNext();)
            {
                _printer.write(",");
                if (isTooLong(startLine))
                {
                    if (_options.isArrInitBreakAllIfTooLong())
                    {
                        breakAll = true;
                        break;
                    }
                    else
                    {
                        _printer.writeln();
                        startLine = getLine();
                    }
                }
                else
                {
                    if (_options.isArrInitSpaceAfterComma())
                    {
                        _printer.write(" ");
                    }
                }
                it.getNext().accept(this);
            }
            decIndent(_options.getArrInitIndentationLevel());
            _printer.stopTryMode(!breakAll);
        }
        // break-all mode (possibly retry)
        if (breakAll)
        {
            for (VariableInitializerIterator it = initializer.getInitializers().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    _printer.writeln(",");
                }
            }
            decIndent(_options.getArrInitIndentationLevel());
        }
        if (pad)
        {
            adjustPaddingLength(-padColumns);
        }
        _printer.write(" }");
        popContext();

        setFinishPosition(initializer);
        appendComments(initializer.getComments());
    }

    public void visitAssignmentExpression(AssignmentExpression assignExpr)
    {
        int          startLine   = getLine();
        int          startColumn = getColumn();
        int          padColumns  = 0;
        boolean      isTrying    = _options.isAssignExprAllowOperatorBreak();
        String       operator    = assignmentOperatorAsText(assignExpr.getOperator());
        StringBuffer alignment   = new StringBuffer();

        assignExpr.getLeftHandSide().accept(this);

        if (isTrying)
        {
            _printer.startTryMode();
        }
        incIndent(_options.getAssignExprIndentationLevel());
        if (_options.isAssignExprSpacesAroundOperator())
        {
            writeSpaceIfNecessary();
        }
        if (_options.isAssignExprAlignSubsequent() &&
            (topContext() != null) &&
            (topContext()._alignmentColumn > 0))
        {
            for (int column = getColumn() + operator.length() - 1;
                 column < topContext()._alignmentColumn;
                 column++)
            {
                alignment.append(' ');
            }
            _printer.write(alignment.toString());
        }
        _printer.startStyle(Style.OPERATOR);
        _printer.write(operator);
        _printer.stopStyle(Style.OPERATOR);
        _printer.doGetMaxColumn();
        if (_options.isAssignExprSpacesAroundOperator())
        {
            _printer.write(" ");
        }
        if (_options.isAssignExprRelativeIndentation())
        {
            padColumns = getPaddingLength(getColumn());
            adjustPaddingLength(padColumns);
        }
        assignExpr.getValueExpression().accept(this);
        decIndent(_options.getAssignExprIndentationLevel());
        if (_options.isAssignExprRelativeIndentation())
        {
            adjustPaddingLength(-padColumns);
        }

        if (isTrying)
        {
            boolean isTooLong = (_printer.getMaxColumn() > _options.getMaxLineLength());

            _printer.stopTryMode(!isTooLong);
            if (isTooLong)
            {
                incIndent(_options.getAssignExprIndentationLevel());
                if (_options.isAssignExprBreakAfterOperator())
                {
                    writeSpaceIfNecessary();
                    _printer.write(alignment.toString());
                }
                else
                {
                    _printer.writeln();
                }
                _printer.startStyle(Style.OPERATOR);
                _printer.write(operator);
                _printer.stopStyle(Style.OPERATOR);
                if (_options.isAssignExprBreakAfterOperator())
                {
                    _printer.writeln();
                }
                else
                {
                    writeSpaceIfNecessary();
                }
                startLine = getLine();
                assignExpr.getValueExpression().accept(this);

                decIndent(_options.getAssignExprIndentationLevel());
            }
        }

        assignExpr.setStartPosition(assignExpr.getLeftHandSide().getStartPosition());
        assignExpr.setFinishPosition(assignExpr.getValueExpression().getFinishPosition());

        appendComments(assignExpr.getComments());
    }

    public void visitBinaryExpression(BinaryExpression binaryExpr)
    {
        binaryExpr.getLeftOperand().accept(this);

        String operator  = binaryOperatorAsText(binaryExpr.getOperator());
        int    startLine = getLine();

        _printer.startTryMode();

        if (_options.isBinExprSpacesAroundOperator())
        {
            writeSpaceIfNecessary();
        }
        _printer.startStyle(Style.OPERATOR);
        _printer.write(operator);
        _printer.stopStyle(Style.OPERATOR);
        if (_options.isBinExprSpacesAroundOperator())
        {
            _printer.write(" ");
        }
        binaryExpr.getRightOperand().accept(this);

        boolean isTooLong = isTooLong(startLine);

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            if (!_options.isBinExprBreakAfterOperator())
            {
                _printer.writeln();
            }
            else if (_options.isBinExprSpacesAroundOperator())
            {
                writeSpaceIfNecessary();
            }
            _printer.startStyle(Style.OPERATOR);
            _printer.write(operator);
            _printer.stopStyle(Style.OPERATOR);
            if (_options.isBinExprBreakAfterOperator())
            {
                _printer.writeln();
            }
            else if (_options.isBinExprSpacesAroundOperator())
            {
                _printer.write(" ");
            }
            binaryExpr.getRightOperand().accept(this);
        }

        binaryExpr.setStartPosition(binaryExpr.getLeftOperand().getStartPosition());
        binaryExpr.setFinishPosition(binaryExpr.getRightOperand().getFinishPosition());
        appendComments(binaryExpr.getComments());
    }

    public void visitBlock(Block block)
    {
        startBlockScope();
        // note that the comments of a block come last in it
        // (as opposed to other statements)
        setStartPosition(block);
        _printer.write("{");

        BlockStatementArray stmts = block.getBlockStatements();

        incIndent(_options.getBlockIndentationLevel());
        if (!_options.isBlockAllowShortForm() ||
            !stmts.isEmpty())
        {
            ensureNewLine();
            writeBlockStatements(stmts);
        }
        if (block.hasComments())
        {
            ensureNewLine();
            prependComments(block.getComments());
        }
        decIndent(_options.getBlockIndentationLevel());
        _printer.write("}");
        setFinishPosition(block);

        if ((topContext() == null) || topContext()._finishBlockWithNL)
        {
            _printer.writeln();
        }
        stopBlockScope();
    }

    public void visitBooleanLiteral(BooleanLiteral literal)
    {
        writePrimary(literal,
                     literal.asString(),
                     Style.BOOLEAN_LITERAL);
    }

    public void visitBreakStatement(BreakStatement breakStmt)
    {
        prependComments(breakStmt.getComments());

        setStartPosition(breakStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("break");
        _printer.stopStyle(Style.KEYWORD);
        if (breakStmt.hasTarget())
        {
            _printer.write(" ");
            _printer.startStyle(Style.LABEL);
            _printer.write(breakStmt.getTarget().getName());
            _printer.stopStyle(Style.LABEL);
        }
        _printer.write(";");
        setFinishPosition(breakStmt);
        _printer.writeln();
    }

    public void visitCaseBlock(CaseBlock caseBlock)
    {
        prependComments(caseBlock.getComments());
        setStartPosition(caseBlock);

        Expression expr;
        int        padColumns = -1;

        for (ExpressionIterator it = caseBlock.getCases().getIterator(); it.hasNext();)
        {
            ensureNewLine();
            expr = it.getNext();
            _printer.startStyle(Style.KEYWORD);
            _printer.write("case");
            _printer.stopStyle(Style.KEYWORD);
            _printer.write(" ");
            if (padColumns == -1)
            {
                padColumns = getPaddingLength(getColumn());
            }
            adjustPaddingLength(padColumns);

            pushContext();
            if (expr.hasComments())
            {
                topContext()._writeComments = false;
            }
            expr.accept(this);
            popContext();

            adjustPaddingLength(-padColumns);

            if (_options.isSwitchStmtSpaceBeforeColon())
            {
                writeSpaceIfNecessary();
            }
            _printer.write(":");
            // in the case we do not have any block statements
            setFinishPosition(caseBlock);
            appendComments(expr.getComments());
        }
        if (caseBlock.hasDefault())
        {
            ensureNewLine();
            _printer.startStyle(Style.KEYWORD);
            _printer.write("default");
            _printer.stopStyle(Style.KEYWORD);
            if (_options.isSwitchStmtSpaceBeforeColon())
            {
                _printer.write(" ");
            }
            _printer.write(":");
            // in the case we do not have any block statements
            setFinishPosition(caseBlock);
        }
        ensureNewLine();

        BlockStatementArray stmts = caseBlock.getBlockStatements();

        incIndent(_options.getSwitchStmtCaseStmtsIndentationLevel());
        if (!stmts.isEmpty())
        {
            writeBlockStatements(stmts);

            BlockStatement last = stmts.get(stmts.getCount()-1);

            caseBlock.setFinishPosition(((Node)last).getFinishPosition());
        }
        decIndent(_options.getSwitchStmtCaseStmtsIndentationLevel());
    }

    public void visitCatchClause(CatchClause catchClause)
    {
        startBlockScope();
        super.visitCatchClause(catchClause);
        stopBlockScope();
    }

    public void visitCharacterLiteral(CharacterLiteral literal)
    {
        String result = literal.asString();

        if (_options.isLitSlimUnicode())
        {
            result = slimUnicodeEscape(result);
        }

        writePrimary(literal,
                     result,
                     Style.CHARACTER_LITERAL);
    }

    public void visitClassAccess(ClassAccess classAccess)
    {
        setStartPosition(classAccess);

        visitType(classAccess.getReferencedType());

        _printer.startTryMode();
        _printer.write(".");
        _printer.startStyle(Style.KEYWORD);
        _printer.write("class");
        _printer.stopStyle(Style.KEYWORD);

        boolean isTooLong = isTooLong(classAccess.getStartPosition().getLine());

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            if (_options.isBreakBeforeDot())
            {
                _printer.writeln();
                incIndent(_options.getDotBreakIndentationLevel());
            }
            _printer.write(".");
            if (!_options.isBreakBeforeDot())
            {
                _printer.writeln();
                incIndent(_options.getDotBreakIndentationLevel());
            }
            _printer.startStyle(Style.KEYWORD);
            _printer.write("class");
            _printer.stopStyle(Style.KEYWORD);
            decIndent(_options.getDotBreakIndentationLevel());
        }
        setFinishPosition(classAccess);
    }

    public void visitClassDeclaration(ClassDeclaration classDecl)
    {
        writeTypeDeclaration(classDecl);
    }

    public void visitCompilationUnit(CompilationUnit unit)
    {
        _curUnit = unit;

        int startLine = getLine();

        prependComments(unit.getComments());

        if (getLine() != startLine)
        {
            writeEmptyLines(_options.getCompUnitNumLinesAfterComment());
        }

        if (unit.getPackage() != null)
        {
            String pckgName = unit.getPackage().getQualifiedName();

            // default package ?
            if ((pckgName != null) && (pckgName.length() > 0))
            {
                _printer.startStyle(Style.KEYWORD);
                _printer.write("package");
                _printer.stopStyle(Style.KEYWORD);
                _printer.writeln(" "+pckgName+";");
                if (!unit.getImportDeclarations().isEmpty() ||
                    !unit.getTypeDeclarations().isEmpty())
                {
                    writeEmptyLines(_options.getCompUnitNumLinesAfterPackage());
                }
            }
        }

        if (!unit.getImportDeclarations().isEmpty())
        {
            for (ImportIterator it = unit.getImportDeclarations().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
            }
            if (!unit.getTypeDeclarations().isEmpty())
            {
                writeEmptyLines(_options.getCompUnitNumLinesAfterImports());
            }
        }

        for (TypeDeclarationIterator it = unit.getTypeDeclarations().getIterator(); it.hasNext();)
        {
            it.getNext().accept(this);
            if (it.hasNext())
            {
                writeEmptyLines(_options.getCompUnitNumLinesBetweenTypes());
            }
        }
    }

    public void visitConditionalExpression(ConditionalExpression condExpr)
    {
        condExpr.getCondition().accept(this);

        int startLine  = getLine();
        int padColumns = getPaddingLength(condExpr.getCondition().getStartPosition().getColumn());

        incIndent(_options.getCondExprIndentationLevel());

        _printer.startTryMode();

        // we first try to write all
        if (_options.isCondExprSpacesAroundOperators())
        {
            writeSpaceIfNecessary();
        }
        _printer.startStyle(Style.OPERATOR);
        _printer.write("?");
        _printer.stopStyle(Style.OPERATOR);
        if (_options.isCondExprSpacesAroundOperators())
        {
            _printer.write(" ");
        }
        condExpr.getTrueExpression().accept(this);
        if (_options.isCondExprSpacesAroundOperators())
        {
            writeSpaceIfNecessary();
        }
        _printer.startStyle(Style.OPERATOR);
        _printer.write(":");
        _printer.stopStyle(Style.OPERATOR);
        if (_options.isCondExprSpacesAroundOperators())
        {
            _printer.write(" ");
        }
        condExpr.getFalseExpression().accept(this);

        boolean shouldBreak = isTooLong(startLine);

        _printer.stopTryMode(!shouldBreak);
        if (shouldBreak)
        {
            boolean mustPad = true;

            _printer.startTryMode();

            if (_options.isCondExprSpacesAroundOperators())
            {
                _printer.write(" ");
            }
            _printer.startStyle(Style.OPERATOR);
            _printer.write("?");
            _printer.stopStyle(Style.OPERATOR);
            if (_options.isCondExprSpacesAroundOperators())
            {
                _printer.write(" ");
            }
            condExpr.getTrueExpression().accept(this);

            shouldBreak = _options.isCondExprBreakAllIfTooLong() ||
                          isTooLong(startLine);

            _printer.stopTryMode(!shouldBreak);
            if (shouldBreak)
            {
                if (!_options.isCondExprBreakAfterOperator())
                {
                    _printer.writeln();
                    adjustPaddingLength(padColumns);
                    mustPad   = false;
                    startLine = getLine();
                }
                else if (_options.isCondExprSpacesAroundOperators())
                {
                    writeSpaceIfNecessary();
                }
                _printer.startStyle(Style.OPERATOR);
                _printer.write("?");
                _printer.stopStyle(Style.OPERATOR);
                if (_options.isCondExprBreakAfterOperator())
                {
                    _printer.writeln();
                    adjustPaddingLength(padColumns);
                    mustPad   = false;
                    startLine = getLine();
                }
                else if (_options.isCondExprSpacesAroundOperators())
                {
                    _printer.write(" ");
                }
                condExpr.getTrueExpression().accept(this);
            }

            _printer.startTryMode();

            if (_options.isCondExprSpacesAroundOperators())
            {
                writeSpaceIfNecessary();
            }
            _printer.startStyle(Style.OPERATOR);
            _printer.write(":");
            _printer.stopStyle(Style.OPERATOR);
            if (_options.isCondExprSpacesAroundOperators())
            {
                _printer.write(" ");
            }
            condExpr.getFalseExpression().accept(this);

            // too long after the true expression ?
            shouldBreak = _options.isCondExprBreakAllIfTooLong() ||
                          isTooLong(startLine);

            _printer.stopTryMode(!shouldBreak);
            if (shouldBreak)
            {
                if (!_options.isCondExprBreakAfterOperator())
                {
                    _printer.writeln();
                    if (mustPad)
                    {
                        adjustPaddingLength(padColumns);
                    }
                }
                else if (_options.isCondExprSpacesAroundOperators())
                {
                    writeSpaceIfNecessary();
                }
                _printer.startStyle(Style.OPERATOR);
                _printer.write(":");
                _printer.stopStyle(Style.OPERATOR);
                if (_options.isCondExprBreakAfterOperator())
                {
                    _printer.writeln();
                    if (mustPad)
                    {
                        adjustPaddingLength(padColumns);
                    }
                }
                else if (_options.isCondExprSpacesAroundOperators())
                {
                    _printer.write(" ");
                }
                condExpr.getFalseExpression().accept(this);
            }
            adjustPaddingLength(-padColumns);
        }

        condExpr.setStartPosition(condExpr.getCondition().getStartPosition());
        condExpr.setFinishPosition(condExpr.getFalseExpression().getFinishPosition());
        appendComments(condExpr.getComments());
    }

    public void visitConstructorDeclaration(ConstructorDeclaration constructorDecl)
    {
        startBlockScope();
        writeInvocableDeclaration(constructorDecl);
        stopBlockScope();
    }

    public void visitConstructorInvocation(ConstructorInvocation constructorInvoc)
    {
        boolean isTrying = writePrimaryPrepare(constructorInvoc);
        boolean retry    = false;

        do
        {
            pushContext();
            topContext()._listAlignmentBase = getColumn();

            _printer.startStyle(Style.KEYWORD);
            _printer.write(constructorInvoc.ofBaseClass() ? "super" : "this");
            _printer.stopStyle(Style.KEYWORD);
            if (_options.isInvocSpaceBeforeParen())
            {
                _printer.write(" ");
            }
            _printer.write("(");
            if (constructorInvoc.hasArguments())
            {
                _printer.startTryMode();
                constructorInvoc.getArgumentList().accept(this);

                boolean isTooLong = constructorInvoc.getArgumentList().getArguments().get(0).getFinishPosition().getColumn() >
                                        _options.getMaxLineLength();

                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    // we break after the parenthesis
                    _printer.writeln();
                    constructorInvoc.getArgumentList().accept(this);
                }
            }
            _printer.write(")");

            popContext();
            retry = !(retry || writePrimaryFinish(constructorInvoc, isTrying && !retry));
        }
        while (retry);
    }

    public void visitContinueStatement(ContinueStatement continueStmt)
    {
        prependComments(continueStmt.getComments());

        setStartPosition(continueStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("continue");
        _printer.stopStyle(Style.KEYWORD);
        if (continueStmt.hasTarget())
        {
            _printer.write(" ");
            _printer.startStyle(Style.LABEL);
            _printer.write(continueStmt.getTarget().getName());
            _printer.stopStyle(Style.LABEL);
        }
        _printer.write(";");
        setFinishPosition(continueStmt);
        _printer.writeln();
    }

    public void visitDoWhileStatement(DoWhileStatement doWhileStmt)
    {
        prependComments(doWhileStmt.getComments());

        setStartPosition(doWhileStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("do");
        _printer.stopStyle(Style.KEYWORD);

        pushContext();
        topContext()._finishBlockWithNL = !_options.isDoStmtWhileStartOnSameLine();

        Statement stmt       = doWhileStmt.getLoopStatement();
        boolean   canBeShort = _options.isDoStmtInnerStartOnSameLine() &&
                               (((stmt instanceof EmptyStatement) && !stmt.hasComments()) ||
                                (stmt instanceof Block));

        if (canBeShort)
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(stmt instanceof Block ?
                     _options.getDoStmtBlockIndentationLevel() :
                     _options.getDoStmtIndentationLevel());
        stmt.accept(this);
        decIndent(stmt instanceof Block ?
                     _options.getDoStmtBlockIndentationLevel() :
                     _options.getDoStmtIndentationLevel());

        popContext();

        if (canBeShort)
        {
            writeSpaceIfNecessary();
        }
        else
        {
            ensureNewLine();
        }

        _printer.startStyle(Style.KEYWORD);
        _printer.write("while");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isDoStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getDoStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        doWhileStmt.getCondition().accept(this);

        adjustPaddingLength(-padColumns);
        decIndent(_options.getDoStmtConditionIndentationLevel());

        _printer.write(");");

        setFinishPosition(doWhileStmt);
        _printer.writeln();
    }

    public void visitEmptyStatement(EmptyStatement emptyStmt)
    {
        prependComments(emptyStmt.getComments());
        setStartPosition(emptyStmt);
        _printer.writeln(";");

        emptyStmt.setFinishPosition(emptyStmt.getStartPosition());
    }

    public void visitExpressionStatement(ExpressionStatement exprStmt)
    {
        prependComments(exprStmt.getComments());
        exprStmt.getExpression().accept(this);
        _printer.writeln(";");

        exprStmt.setStartPosition(exprStmt.getExpression().getStartPosition());
        setFinishPosition(exprStmt);
    }

    public void visitFieldAccess(FieldAccess access)
    {
        writePrimary(access,
                     access.getFieldName(),
                     Style.FIELD_IDENTIFIER);
    }

    public void visitFloatingPointLiteral(FloatingPointLiteral literal)
    {
        String result = literal.asString();
        String tmp    = result.toLowerCase();

        if (literal.isDouble() && _options.isFloatLitForceSuffix())
        {
            if (!tmp.endsWith("d"))
            {
                result += "d";
                tmp    += "d";
            }
        }
        if (_options.isFloatLitForceDot() && (result.indexOf('.') == -1))
        {
            int pos = tmp.indexOf('e');

            if (pos == -1)
            {
                pos = tmp.indexOf('f');
            }
            if (pos == -1)
            {
                pos = tmp.indexOf('d');
            }

            if (pos == -1)
            {
                result += ".0";
            }
            else
            {
                result = result.substring(0, pos) + ".0"+ result.substring(pos);
            }
        }

        if (_options.getNumLitCaseMode() == Options.NUMLIT_FORCE_LOWERCASE)
        {
            result = result.toLowerCase();
        }
        else if (_options.getNumLitCaseMode() == Options.NUMLIT_FORCE_UPPERCASE)
        {
            result = result.toUpperCase();
        }

        writePrimary(literal,
                     result,
                     Style.NUMBER_LITERAL);
    }

    public void visitFormalParameter(FormalParameter param)
    {
        String modifiers = modifiersToString(param.getModifiers());

        if (modifiers.length() > 0)
        {
            _printer.write(modifiers + " ");
        }
        visitType(param.getType());
        writeSpaceIfNecessary();

        if (_options.isParamListAlignNames() && (topContext() != null))
        {
            for (int curColumn = getColumn(); curColumn < topContext()._alignmentColumn; curColumn++)
            {
                _printer.write(" ");
            }
        }
        _printer.startStyle(Style.VARIABLE_IDENTIFIER);
        _printer.write(param.getName());
        _printer.stopStyle(Style.VARIABLE_IDENTIFIER);
        setFinishPosition(param);

        appendComments(param.getComments());

        param.setStartPosition(param.getType().getStartPosition());
    }

    public void visitFormalParameterList(FormalParameterList params)
    {
        if ((params == null) || (params.getParameters().getCount() == 0))
        {
            return;
        }

        // the surrounding spaces are not part of the param list
        // therefore we print them in advance
        if (!_printer.isNewLine() && _options.isParamListSurroundBySpaces())
        {
            writeSpaceIfNecessary();
        }

        boolean breakAll   = false;
        int     startLine  = getLine();

        // we have to determine the amount of padding characters
        // before we indent
        boolean pad        = (_options.getParamListIndentationMode() != Options.LIST_NORMAL_INDENTATION);
        int     padColumns = getPaddingLength(
                                _options.getParamListIndentationMode() == Options.LIST_RELATIVE_INDENTATION ?
                                    topContext()._listAlignmentBase :
                                    getColumn());

        incIndent(_options.getParamListIndentationLevel());

        if ((_options.getParamListBreakLimit() > 0) &&
            (params.getParameters().getCount() >= _options.getParamListBreakLimit()))
        {
            breakAll = true;
        }

        // when we start on a newline, then the invocation has a break
        // right after the opening parenthesis (or an eol comment)
        // which causes us to indent the first param as well
        if (pad)
        {
            adjustPaddingLength(padColumns);
        }
        setStartPosition(params);
        if (!breakAll)
        {
            _printer.startTryMode();

            FormalParameterIterator it = params.getParameters().getIterator();

            visitFormalParameter(it.getNext());
            for (; it.hasNext();)
            {
                _printer.write(",");
                if (isTooLong(startLine))
                {
                    if (_options.isParamListBreakAllIfTooLong())
                    {
                        breakAll = true;
                        break;
                    }
                    else
                    {
                        _printer.writeln();
                        startLine = getLine();
                    }
                }
                else
                {
                    if (_options.isParamListSpaceAfterComma())
                    {
                        _printer.write(" ");
                    }
                }
                visitFormalParameter(it.getNext());
            }
            decIndent(_options.getParamListIndentationLevel());
            _printer.stopTryMode(!breakAll);
        }
        // break-all mode (possibly retry)
        if (breakAll)
        {
            if (_options.isParamListAlignNames())
            {
                int maxLength = 0;
                int curLength;

                for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
                {
                    curLength = typeToString(it.getNext().getType()).length();
                    if (curLength > maxLength)
                    {
                        maxLength = curLength;
                    }
                }
                pushContext();
                topContext()._alignmentColumn = getColumn() + maxLength + 1;
            }
            for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
            {
                visitFormalParameter(it.getNext());
                if (it.hasNext())
                {
                    _printer.writeln(",");
                }
            }
            popContext();
            decIndent(_options.getParamListIndentationLevel());
        }
        setFinishPosition(params);

        if (_options.isParamListSurroundBySpaces())
        {
            writeSpaceIfNecessary();
        }
        appendComments(params.getComments());
        if (pad)
        {
            adjustPaddingLength(-padColumns);
        }
    }

    public void visitForStatement(ForStatement forStmt)
    {
        startBlockScope();
        prependComments(forStmt.getComments());

        setStartPosition(forStmt);
        pushContext();
        topContext()._listAlignmentBase = getColumn();

        _printer.startStyle(Style.KEYWORD);
        _printer.write("for");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isForStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int startLine  = getLine();
        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getForStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        _printer.startTryMode();

        if (forStmt.hasInitDeclarations())
        {
            // includes a semicolon
            writeVariableDeclarations(new VarDeclArray(forStmt.getInitDeclarations()),
                                      true,
                                      _options.isLocalVarDeclAlignSubsequent(),
                                      0,
                                      false);
        }
        else
        {
            if (forStmt.hasInitList())
            {
                forStmt.getInitList().accept(this);
            }
            _printer.write(";");
        }

        if (forStmt.hasCondition())
        {
            if (_options.isForStmtSpaceAfterSemicolon())
            {
                _printer.write(" ");
            }
            forStmt.getCondition().accept(this);
        }
        _printer.write(";");

        if (forStmt.hasUpdateList())
        {
            if (_options.isForStmtSpaceAfterSemicolon())
            {
                _printer.write(" ");
            }
            forStmt.getUpdateList().accept(this);
        }

        boolean isTooLong = isTooLong(startLine);
        boolean breakAll  = isTooLong &&
                            _options.isForStmtBreakAllPartsIfHeadTooLong();

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            // we must break
            if (forStmt.hasInitDeclarations())
            {
                // includes a semicolon
                writeVariableDeclarations(new VarDeclArray(forStmt.getInitDeclarations()),
                                          true,
                                          _options.isLocalVarDeclAlignSubsequent(),
                                          0,
                                          false);
            }
            else
            {
                if (forStmt.hasInitList())
                {
                    forStmt.getInitList().accept(this);
                }
                _printer.write(";");
            }
            if ((breakAll || isTooLong(startLine)) &&
                (forStmt.hasCondition() || forStmt.hasUpdateList()))
            {
                _printer.writeln();
                startLine = getLine();
            }


            if (forStmt.hasCondition())
            {
                if (!_printer.isNewLine() && _options.isForStmtSpaceAfterSemicolon())
                {
                    _printer.write(" ");
                }
                forStmt.getCondition().accept(this);
            }
            _printer.write(";");

            if (forStmt.hasUpdateList())
            {
                if (breakAll || isTooLong(startLine))
                {
                    _printer.writeln();
                }
                if (!_printer.isNewLine() && _options.isForStmtSpaceAfterSemicolon())
                {
                    _printer.write(" ");
                }
                forStmt.getUpdateList().accept(this);
            }
        }

        adjustPaddingLength(-padColumns);
        decIndent(_options.getForStmtConditionIndentationLevel());

        _printer.write(")");
        popContext();

        Statement stmt       = forStmt.getLoopStatement();
        boolean   canBeShort = _options.isForStmtInnerStartOnSameLine() &&
                               (((stmt instanceof EmptyStatement) && !stmt.hasComments()) ||
                                (stmt instanceof Block));

        if (canBeShort)
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(stmt instanceof Block ?
                     _options.getForStmtBlockIndentationLevel() :
                     _options.getForStmtIndentationLevel());
        stmt.accept(this);
        decIndent(stmt instanceof Block ?
                     _options.getForStmtBlockIndentationLevel() :
                     _options.getForStmtIndentationLevel());

        forStmt.setFinishPosition(stmt.getFinishPosition());
        stopBlockScope();
    }

    public void visitIfThenElseStatement(IfThenElseStatement ifStmt)
    {
        prependComments(ifStmt.getComments());

        setStartPosition(ifStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("if");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isIfStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getIfStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        ifStmt.getCondition().accept(this);

        adjustPaddingLength(-padColumns);
        decIndent(_options.getIfStmtConditionIndentationLevel());

        _printer.write(")");
        pushContext();
        topContext()._finishBlockWithNL = !_options.isIfStmtElseStartOnSameLine();

        Statement stmt       = ifStmt.getTrueStatement();
        boolean   canBeShort = _options.isIfStmtInnerStartOnSameLine() &&
                               (((stmt instanceof EmptyStatement) && !stmt.hasComments()) ||
                                (stmt instanceof Block));

        if (canBeShort)
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(stmt instanceof Block ?
                     _options.getIfStmtBlockIndentationLevel() :
                     _options.getIfStmtIndentationLevel());
        stmt.accept(this);
        decIndent(stmt instanceof Block ?
                     _options.getIfStmtBlockIndentationLevel() :
                     _options.getIfStmtIndentationLevel());

        if (ifStmt.hasElse())
        {
            if (!_printer.isNewLine())
            {
                writeSpaceIfNecessary();
            }
            _printer.startStyle(Style.KEYWORD);
            _printer.write("else");
            _printer.stopStyle(Style.KEYWORD);

            stmt = ifStmt.getFalseStatement();

            if (_options.isIfStmtAllowCompactElseIf() &&
                (stmt instanceof IfThenElseStatement) &&
                !stmt.hasComments())
            {
                _printer.write(" ");
                stmt.accept(this);
            }
            else
            {
                canBeShort = _options.isIfStmtInnerStartOnSameLine() &&
                             (((stmt instanceof EmptyStatement) && !stmt.hasComments()) ||
                              (stmt instanceof Block));

                if (canBeShort)
                {
                    _printer.write(" ");
                }
                else
                {
                    _printer.writeln();
                }
                incIndent(stmt instanceof Block ?
                             _options.getIfStmtBlockIndentationLevel() :
                             _options.getIfStmtIndentationLevel());
                stmt.accept(this);
                decIndent(stmt instanceof Block ?
                             _options.getIfStmtBlockIndentationLevel() :
                             _options.getIfStmtIndentationLevel());
            }
        }
        popContext();

        // at this point we must ensure that the statement ends with
        // a newline
        ensureNewLine();

        ifStmt.setFinishPosition(stmt.getFinishPosition());

    }

    public void visitImportDeclaration(ImportDeclaration importDecl)
    {
        prependComments(importDecl.getComments());
        setStartPosition(importDecl);
        _printer.startStyle(Style.KEYWORD);
        _printer.write("import");
        _printer.stopStyle(Style.KEYWORD);
        _printer.write(" ");
        _printer.write(importDecl.getImportedPackageOrType());
        if (importDecl.isOnDemand())
        {
            _printer.write(".*");
        }
        _printer.write(";");
        setFinishPosition(importDecl);
        _printer.writeln();
    }

    public void visitInitializer(Initializer initializer)
    {
        startBlockScope();
        prependComments(initializer.getComments());

        setStartPosition(initializer);

        if (initializer.isStatic())
        {
            _printer.startStyle(Style.MODIFIER);
            _printer.write("static");
            _printer.stopStyle(Style.MODIFIER);
            if (_options.isInitializerBlockOnSameLine())
            {
                _printer.write(" ");
            }
            else
            {
                _printer.writeln();
            }
        }
        visitBlock(initializer.getBody());
        initializer.setFinishPosition(initializer.getBody().getFinishPosition());
        stopBlockScope();
    }

    public void visitInstanceofExpression(InstanceofExpression instanceOfExpr)
    {
        int startLine = getLine();

        instanceOfExpr.getInnerExpression().accept(this);

        _printer.startTryMode();
        _printer.write(" ");
        _printer.startStyle(Style.KEYWORD);
        _printer.write("instanceof");
        _printer.stopStyle(Style.KEYWORD);

        boolean isTooLong = isTooLong(startLine);

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            _printer.writeln();
            _printer.startStyle(Style.KEYWORD);
            _printer.write("instanceof");
            _printer.stopStyle(Style.KEYWORD);
            startLine = getLine();
        }

        _printer.startTryMode();

        _printer.write(" ");
        visitType(instanceOfExpr.getReferencedType());

        isTooLong = isTooLong(startLine);

        _printer.stopTryMode(!isTooLong);
        if (isTooLong)
        {
            _printer.writeln();
            visitType(instanceOfExpr.getReferencedType());
        }

        instanceOfExpr.setStartPosition(instanceOfExpr.getInnerExpression().getStartPosition());
        instanceOfExpr.setFinishPosition(instanceOfExpr.getReferencedType().getFinishPosition());

        appendComments(instanceOfExpr.getComments());
    }

    public void visitInstantiation(Instantiation instantiation)
    {
        boolean isTrying = writePrimaryPrepare(instantiation);
        boolean retry    = false;

        do
        {
            pushContext();
            topContext()._listAlignmentBase = getColumn();

            _printer.startStyle(Style.KEYWORD);
            _printer.write("new");
            _printer.stopStyle(Style.KEYWORD);
            _printer.write(" ");
            visitType(instantiation.getInstantiatedType());
            if (_options.isInvocSpaceBeforeParen())
            {
                writeSpaceIfNecessary();
            }
            _printer.write("(");
            if (instantiation.hasArguments())
            {
                _printer.startTryMode();
                instantiation.getArgumentList().accept(this);

                boolean isTooLong = instantiation.getArgumentList().getArguments().get(0).getFinishPosition().getColumn() >
                                        _options.getMaxLineLength();

                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    // we break after the parenthesis
                    _printer.writeln();
                    instantiation.getArgumentList().accept(this);
                }
            }
            _printer.write(")");
            if (instantiation.withAnonymousClass())
            {
                visitAnonymousClassDeclaration(instantiation.getAnonymousClass());
            }

            popContext();
            retry = !(retry || writePrimaryFinish(instantiation, isTrying && !retry));
        }
        while (retry);
    }

    public void visitIntegerLiteral(IntegerLiteral literal)
    {
        String result = literal.asString();

        if (_options.getNumLitCaseMode() == Options.NUMLIT_FORCE_LOWERCASE)
        {
            result = result.toLowerCase();
        }
        else if (_options.getNumLitCaseMode() == Options.NUMLIT_FORCE_UPPERCASE)
        {
            result = result.toUpperCase();
        }

        writePrimary(literal,
                     result,
                     Style.NUMBER_LITERAL);
    }

    public void visitInterfaceDeclaration(InterfaceDeclaration interfaceDecl)
    {
        writeTypeDeclaration(interfaceDecl);
    }

    public void visitLabeledStatement(LabeledStatement lbldStmt)
    {
        prependComments(lbldStmt.getComments());

        if (!_options.isLbldStmtIndentLabel())
        {
            _printer.doIndent(false);
        }
        incIndent(_options.getLbldStmtIndentationLevel());

        setStartPosition(lbldStmt);
        _printer.startStyle(Style.LABEL);
        _printer.write(lbldStmt.getName());
        _printer.stopStyle(Style.LABEL);
        if (_options.isLbldStmtSpaceBeforeColon())
        {
            _printer.write(" ");
        }
        _printer.write(":");
        // the indent is only for the line with the label in it
        decIndent(_options.getLbldStmtIndentationLevel());

        if (!_options.isLbldStmtIndentLabel())
        {
            _printer.doIndent(true);
        }
        if (_options.getLbldStmtPositioningMode() == Options.LBLDSTMT_SAME_LINE_IF_POSSIBLE)
        {
            if (getColumn()+1 > _printer.getIndentLength())
            {
                _printer.writeln();
            }
        }
        else if (_options.getLbldStmtPositioningMode() == Options.LBLDSTMT_FORCE_NEXT_LINE)
        {
            _printer.writeln();
        }
        if (!_printer.isNewLine())
        {
            _printer.write(" ");
        }
        lbldStmt.getStatement().accept(this);
        lbldStmt.setFinishPosition(lbldStmt.getStatement().getFinishPosition());
    }

    public void visitMethodDeclaration(MethodDeclaration methodDecl)
    {
        startBlockScope();
        writeInvocableDeclaration(methodDecl);
        stopBlockScope();
    }

    public void visitMethodInvocation(MethodInvocation methodInvoc)
    {
        boolean isTrying = writePrimaryPrepare(methodInvoc);
        boolean retry    = false;

        do
        {
            pushContext();
            topContext()._listAlignmentBase = getColumn();

            _printer.startStyle(Style.METHOD_IDENTIFIER);
            _printer.write(methodInvoc.getMethodName());
            _printer.stopStyle(Style.METHOD_IDENTIFIER);
            if (_options.isInvocSpaceBeforeParen())
            {
                _printer.write(" ");
            }
            _printer.write("(");
            if (methodInvoc.hasArguments())
            {
                _printer.startTryMode();
                methodInvoc.getArgumentList().accept(this);

                boolean isTooLong = methodInvoc.getArgumentList().getArguments().get(0).getFinishPosition().getColumn() >
                                        _options.getMaxLineLength();

                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    // we break after the parenthesis
                    _printer.writeln();
                    methodInvoc.getArgumentList().accept(this);
                }
            }
            _printer.write(")");

            popContext();
            retry = !(retry || writePrimaryFinish(methodInvoc, isTrying && !retry));
        }
        while (retry);
    }

    public void visitModifiers(Modifiers mods)
    {
        _printer.startStyle(Style.MODIFIER);
        _printer.write(modifiersToString(mods));
        _printer.stopStyle(Style.MODIFIER);
    }

    public void visitNullLiteral(NullLiteral literal)
    {
        writePrimary(literal,
                     NullLiteral.NULL_KEYWORD,
                     Style.NULL_LITERAL);
    }

    public void visitParenthesizedExpression(ParenthesizedExpression parenExpr)
    {
        setStartPosition(parenExpr);

        _printer.write("(");
        if (_options.isParenExprSpacesAroundInner())
        {
            _printer.write(" ");
        }
        parenExpr.getInnerExpression().accept(this);
        if (_options.isParenExprSpacesAroundInner() &&
            !_printer.isNewLine() && (_printer.getLastChar() != ' '))
        {
            writeSpaceIfNecessary();
        }
        _printer.write(")");

        setFinishPosition(parenExpr);
        appendComments(parenExpr.getComments());
    }

    public void visitPostfixExpression(PostfixExpression postfixExpr)
    {
        postfixExpr.getInnerExpression().accept(this);
        postfixExpr.setStartPosition(postfixExpr.getInnerExpression().getStartPosition());
        _printer.startStyle(Style.OPERATOR);
        _printer.write(postfixExpr.isIncrement() ? "++" : "--");
        _printer.stopStyle(Style.OPERATOR);

        setFinishPosition(postfixExpr);
        appendComments(postfixExpr.getComments());
    }

    public void visitReturnStatement(ReturnStatement returnStmt)
    {
        prependComments(returnStmt.getComments());
        setStartPosition(returnStmt);
        _printer.startStyle(Style.KEYWORD);
        _printer.write("return");
        _printer.stopStyle(Style.KEYWORD);
        if (returnStmt.hasReturnValue())
        {
            _printer.write(" ");

            int padColumns = getPaddingLength(getColumn());
            int startLine  = getLine();

            incIndent(_options.getReturnStmtIndentationLevel());
            if (_options.isReturnStmtAlignValue())
            {
                adjustPaddingLength(padColumns);
            }

            returnStmt.getReturnValue().accept(this);

            _printer.write(";");
            if (_options.isReturnStmtAlignValue())
            {
                adjustPaddingLength(-padColumns);
            }
            decIndent(_options.getReturnStmtIndentationLevel());
        }
        else
        {
            _printer.write(";");
        }
        setFinishPosition(returnStmt);
        _printer.writeln();
    }

    public void visitSelfAccess(SelfAccess selfAccess)
    {
        writePrimary(selfAccess,
                     selfAccess.isSuper() ? "super" : "this",
                     Style.KEYWORD);
    }

    public void visitSingleInitializer(SingleInitializer initializer)
    {
        initializer.getInitEpression().accept(this);
        initializer.setStartPosition(initializer.getInitEpression().getStartPosition());
        initializer.setFinishPosition(initializer.getInitEpression().getFinishPosition());
        appendComments(initializer.getComments());
    }

    public void visitStatementExpressionList(StatementExpressionList exprList)
    {
        writeExpressionList(exprList);
    }

    public void visitStringLiteral(StringLiteral literal)
    {
        String result = literal.asString();

        if (_options.isLitSlimUnicode())
        {
            result = slimUnicodeEscape(result);
        }

        writePrimary(literal,
                     result,
                     Style.STRING_LITERAL);
    }

    public void visitSwitchStatement(SwitchStatement switchStmt)
    {
        startBlockScope();
        prependComments(switchStmt.getComments());

        setStartPosition(switchStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("switch");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isSwitchStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getSwitchStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        switchStmt.getSwitchExpression().accept(this);

        adjustPaddingLength(-padColumns);
        decIndent(_options.getSwitchStmtConditionIndentationLevel());

        _printer.write(")");

        if (_options.isSwitchStmtInnerStartOnSameLine())
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        _printer.writeln("{");
        incIndent(_options.getSwitchStmtCaseIndentationLevel());

        CaseBlockArray cases = switchStmt.getCaseBlocks();

        if (cases.isEmpty())
        {
            if (!_options.isSwitchStmtAllowShortForm())
            {
                _printer.writeln();
            }
        }
        else
        {
            for (CaseBlockIterator it = cases.getIterator(); it.hasNext();)
            {
                visitCaseBlock(it.getNext());
            }
        }

        decIndent(_options.getSwitchStmtCaseIndentationLevel());
        _printer.write("}");

        setFinishPosition(switchStmt);
        _printer.writeln();
        stopBlockScope();
    }

    public void visitSynchronizedStatement(SynchronizedStatement syncStmt)
    {
        prependComments(syncStmt.getComments());

        setStartPosition(syncStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("synchronized");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isSyncStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getSyncStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        syncStmt.getLockExpression().accept(this);

        adjustPaddingLength(-padColumns);
        decIndent(_options.getSyncStmtConditionIndentationLevel());

        _printer.write(")");

        if (_options.isSyncStmtInnerStartOnSameLine())
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(_options.getSyncStmtBlockIndentationLevel());
        syncStmt.getBlock().accept(this);
        decIndent(_options.getSyncStmtBlockIndentationLevel());

        syncStmt.setFinishPosition(syncStmt.getBlock().getFinishPosition());
    }

    public void visitThrowStatement(ThrowStatement throwStmt)
    {
        prependComments(throwStmt.getComments());
        setStartPosition(throwStmt);
        _printer.startStyle(Style.KEYWORD);
        _printer.write("throw");
        _printer.stopStyle(Style.KEYWORD);
        _printer.write(" ");

        int padColumns = getPaddingLength(getColumn());
        int startLine  = getLine();

        incIndent(_options.getThrowStmtIndentationLevel());
        if (_options.isThrowStmtAlignValue())
        {
            adjustPaddingLength(padColumns);
        }

        throwStmt.getThrowExpression().accept(this);

        _printer.write(";");

        setFinishPosition(throwStmt);
        if (_options.isThrowStmtAlignValue())
        {
            adjustPaddingLength(-padColumns);
        }
        decIndent(_options.getThrowStmtIndentationLevel());

        _printer.writeln();
    }

    public void visitTryStatement(TryStatement tryStmt)
    {
        prependComments(tryStmt.getComments());

        setStartPosition(tryStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("try");
        _printer.stopStyle(Style.KEYWORD);

        pushContext();
        topContext()._finishBlockWithNL = !_options.isDoStmtWhileStartOnSameLine();

        if (_options.isDoStmtInnerStartOnSameLine())
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(_options.getTryStmtIndentationLevel());
        tryStmt.getTryBlock().accept(this);
        decIndent(_options.getTryStmtIndentationLevel());

        CatchClause clause = null;

        for (CatchClauseIterator it = tryStmt.getCatchClauses().getIterator(); it.hasNext();)
        {
            clause = it.getNext();
            if (_options.isDoStmtInnerStartOnSameLine() &&
                !clause.hasComments())
            {
                writeSpaceIfNecessary();
            }
            else
            {
                ensureNewLine();
            }
            prependComments(clause.getComments());
            _printer.startStyle(Style.KEYWORD);
            _printer.write("catch");
            _printer.stopStyle(Style.KEYWORD);
            if (_options.isTryStmtSpaceBeforeParen())
            {
                _printer.write(" ");
            }
            _printer.write("(");
            if (_options.isTryStmtSpaceAroundParam())
            {
                _printer.write(" ");
            }
            clause.getFormalParameter().accept(this);
            if (_options.isTryStmtSpaceAroundParam())
            {
                _printer.write(" ");
            }
            _printer.write(")");

            if (_options.isDoStmtInnerStartOnSameLine())
            {
                _printer.write(" ");
            }
            else
            {
                _printer.writeln();
            }
            incIndent(_options.getTryStmtIndentationLevel());
            clause.getCatchBlock().accept(this);
            decIndent(_options.getTryStmtIndentationLevel());
        }

        if (tryStmt.hasFinallyClause())
        {
            if (_options.isDoStmtInnerStartOnSameLine())
            {
                writeSpaceIfNecessary();
            }
            else
            {
                ensureNewLine();
            }
            _printer.startStyle(Style.KEYWORD);
            _printer.write("finally");
            _printer.stopStyle(Style.KEYWORD);

            if (_options.isDoStmtInnerStartOnSameLine())
            {
                _printer.write(" ");
            }
            else
            {
                _printer.writeln();
            }
            incIndent(_options.getTryStmtIndentationLevel());
            tryStmt.getFinallyClause().accept(this);
            decIndent(_options.getTryStmtIndentationLevel());

            tryStmt.setFinishPosition(tryStmt.getFinallyClause().getFinishPosition());
        }
        else
        {
            // we must have at least one catch clause (in the var clause)
            tryStmt.setFinishPosition(clause.getFinishPosition());
        }
        ensureNewLine();

        popContext();
    }

    public void visitType(Type type)
    {
        setStartPosition(type);
        _printer.startStyle(Style.TYPE_IDENTIFIER);
        _printer.write(typeToString(type));
        _printer.stopStyle(Style.TYPE_IDENTIFIER);
        setFinishPosition(type);
        appendComments(type.getComments());
    }

    public void visitTypeAccess(TypeAccess typeAccess)
    {
        boolean isTrying = writePrimaryPrepare(typeAccess);
        boolean retry    = false;

        do
        {
            visitType(typeAccess.getType());
            retry = !(retry || writePrimaryFinish(typeAccess, isTrying && !retry));
        }
        while (retry);
    }

    public void visitUnaryExpression(UnaryExpression unaryExpr)
    {
        setStartPosition(unaryExpr);

        if (unaryExpr.getOperator() == UnaryExpression.CAST_OP)
        {
            _printer.write("(");
            if (_options.isCastSpacesAroundType())
            {
                _printer.write(" ");
            }
            visitType(unaryExpr.getCastType());
            if (_options.isCastSpacesAroundType() &&
                (_printer.getLastChar() != ' '))
            {
                writeSpaceIfNecessary();
            }
            _printer.write(")");
            if (_options.isCastSpaceAfterParen())
            {
                _printer.write(" ");
            }
        }
        else
        {
            _printer.startStyle(Style.OPERATOR);
            switch (unaryExpr.getOperator())
            {
                case UnaryExpression.PLUS_OP :
                    _printer.write("+");
                    break;
                case UnaryExpression.MINUS_OP :
                    _printer.write("-");
                    break;
                case UnaryExpression.INCREMENT_OP :
                    _printer.write("++");
                    break;
                case UnaryExpression.DECREMENT_OP :
                    _printer.write("--");
                    break;
                case UnaryExpression.COMPLEMENT_OP :
                    _printer.write("~");
                    break;
                case UnaryExpression.NEGATION_OP :
                    _printer.write("!");
                    break;
            }
            _printer.stopStyle(Style.OPERATOR);
        }
        unaryExpr.getInnerExpression().accept(this);

        unaryExpr.setFinishPosition(unaryExpr.getInnerExpression().getFinishPosition());
        appendComments(unaryExpr.getComments());
    }

    public void visitUnresolvedAccess(UnresolvedAccess access)
    {
        // we do not break unresolved accesses as we do not
        // break type access (although it is allowed)
        writePrimary(access,
                     access.toString(),
                     Style.UNRESOLVED_ACCESS);
    }

    public void visitVariableAccess(VariableAccess access)
    {
        writePrimary(access,
                     access.getVariableName(),
                     Style.VARIABLE_IDENTIFIER);
    }

    public void visitWhileStatement(WhileStatement whileStmt)
    {
        prependComments(whileStmt.getComments());

        setStartPosition(whileStmt);

        _printer.startStyle(Style.KEYWORD);
        _printer.write("while");
        _printer.stopStyle(Style.KEYWORD);
        if (_options.isWhileStmtSpaceBeforeParen())
        {
            _printer.write(" ");
        }
        _printer.write("(");

        int padColumns = getPaddingLength(getColumn());

        incIndent(_options.getWhileStmtConditionIndentationLevel());
        adjustPaddingLength(padColumns);

        whileStmt.getCondition().accept(this);

        adjustPaddingLength(-padColumns);
        decIndent(_options.getWhileStmtConditionIndentationLevel());

        _printer.write(")");

        Statement stmt       = whileStmt.getLoopStatement();
        boolean   canBeShort = _options.isWhileStmtInnerStartOnSameLine() &&
                               (((stmt instanceof EmptyStatement) && !stmt.hasComments()) ||
                                (stmt instanceof Block));

        if (canBeShort)
        {
            _printer.write(" ");
        }
        else
        {
            _printer.writeln();
        }
        incIndent(stmt instanceof Block ?
                     _options.getWhileStmtBlockIndentationLevel() :
                     _options.getWhileStmtIndentationLevel());
        stmt.accept(this);
        decIndent(stmt instanceof Block ?
                     _options.getWhileStmtBlockIndentationLevel() :
                     _options.getWhileStmtIndentationLevel());

        whileStmt.setFinishPosition(stmt.getFinishPosition());
    }

    private void writeBlockComment(Comment comment)
    {
        String text = comment.getText();
        int    next;
        int    prev;

        setStartPosition(comment);

        _printer.startStyle(Style.BLOCK_COMMENT);
        next = text.indexOf('\n');
        if (next == -1)
        {
            // fits on one line
            _printer.write("/* "+text+" */");
            setFinishPosition(comment);
            _printer.stopStyle(Style.BLOCK_COMMENT);
        }
        else
        {
            // we start on a new line
            ensureNewLine();
            _printer.writeln("/*");
            prev = 0;
            do
            {
                _printer.writeln(" * "+text.substring(prev, next));
                prev = next + 1;
                next = text.indexOf('\n', prev);
            }
            while (next >= 0);
            if (prev < text.length() - 1)
            {
                _printer.writeln(" * "+text.substring(prev));
            }
            _printer.write(" */");
            setFinishPosition(comment);
            _printer.stopStyle(Style.BLOCK_COMMENT);
            _printer.writeln();
        }
    }

    private void writeBlockStatements(BlockStatementArray blockStmts)
    {
        pushContext();

        BlockStatement       curStmt;
        ExpressionStatement  exprStmt;
        AssignmentExpression assignExpr;
        int                  num;
        int                  maxColumn = 0;
        int                  curColumn;
        boolean              alignAssignExprs = false;

        for (int idx = 0; idx < blockStmts.getCount(); idx++)
        {
            curStmt = blockStmts.get(idx);

            if (_options.isAssignExprAlignSubsequent())
            {
                if (alignAssignExprs)
                {
                    if (!(curStmt instanceof ExpressionStatement) ||
                        !(((ExpressionStatement)curStmt).getExpression() instanceof AssignmentExpression))
                    {
                        alignAssignExprs                        = false;
                        topContext()._alignmentColumn = -1;
                    }
                }
                else
                {
                    // we must determine the alignment position if
                    // we encounter subsequent assignment expressions
                    num = idx;
                    while (num < blockStmts.getCount())
                    {
                        if (!(blockStmts.get(num) instanceof ExpressionStatement))
                        {
                            break;
                        }
                        exprStmt = (ExpressionStatement)blockStmts.get(num);
                        if (!(exprStmt.getExpression() instanceof AssignmentExpression))
                        {
                            break;
                        }
                        assignExpr = (AssignmentExpression)exprStmt.getExpression();

                        _printer.startTryMode();
                        assignExpr.getLeftHandSide().accept(this);

                        if (_options.isAssignExprSpacesAroundOperator())
                        {
                            writeSpaceIfNecessary();
                        }
                        _printer.write(assignmentOperatorAsText(assignExpr.getOperator()));
                        curColumn = getColumn() - 1;

                        _printer.stopTryMode(false);
                        if (curColumn > maxColumn)
                        {
                            maxColumn = curColumn;
                        }
                        num++;
                    }
                    if (num > idx + 1)
                    {
                        alignAssignExprs              = false;
                        topContext()._alignmentColumn = maxColumn;
                    }
                }
            }

            // we combine subsequent local variable declarations
            if (curStmt instanceof LocalVariableDeclaration)
            {
                if ((idx > 0) &&
                    _options.isLocalVarDeclSurroundBlockByEmptyLines())
                {
                    _printer.writeln();
                }

                VarDeclArray varDecls = new VarDeclArray();

                varDecls.add((LocalVariableDeclaration)curStmt);
                while ((idx < blockStmts.getCount() - 1) &&
                       (blockStmts.get(idx + 1) instanceof LocalVariableDeclaration))
                {
                    curStmt = blockStmts.get(++idx);
                    varDecls.add((LocalVariableDeclaration)curStmt);
                }
                writeVariableDeclarations(varDecls,
                                          _options.isLocalVarDeclCombineSubsequent(),
                                          _options.isLocalVarDeclAlignSubsequent(),
                                          0,
                                          false);
                ensureNewLine();
                if ((idx < blockStmts.getCount() - 1) &&
                    _options.isLocalVarDeclSurroundBlockByEmptyLines())
                {
                    _printer.writeln();
                }
            }
            else
            {
                if ((curStmt instanceof ClassDeclaration) &&
                    _options.isLocalClassDeclSurroundByEmptyLines())
                {
                    if (idx > 0)
                    {
                        _printer.writeln();
                    }
                }

                curStmt.accept(this);

                if ((curStmt instanceof ClassDeclaration) &&
                    _options.isLocalClassDeclSurroundByEmptyLines())
                {
                    if (idx < blockStmts.getCount()-1)
                    {
                        _printer.writeln();
                    }
                }
            }
        }
        popContext();
    }

    private void writeComments(CommentArray comments, boolean withLeadingSpace)
    {
        // if there is only one comment and it would serve as an eol comment
        // then print it as one (even if it is a block comment)
        if ((comments != null) && !comments.isEmpty())
        {
            Comment curComment;

            if (withLeadingSpace)
            {
                writeSpaceIfNecessary();
            }
            for (int idx = 0; idx < comments.getCount(); idx++)
            {
                curComment = comments.get(idx);
                if (curComment.isBlockComment())
                {
                    if (curComment.isJavaDocComment())
                    {
                        writeJavaDocComment(curComment);
                    }
                    else
                    {
                        writeBlockComment(curComment);
                    }
                    // this could possibly lead to whitespaces at line ends
                    // but is necessary for separating short block comments
                    // from surrounding text (expressions etc.)
                    if (!_printer.isNewLine())
                    {
                        _printer.write(" ");
                    }
                }
                else
                {
                    writeLineComment(curComment);
                }
            }
        }
    }

    private void writeCompositeDeclarations(VarDeclArray varDecls,
                                            int          startIdx,
                                            int          endIdx,
                                            boolean      completeType,
                                            boolean      writeComment)
    {
        VariableDeclaration varDecl;
        boolean             retry;
        int                 startLine = 0;

        for (int idx = startIdx; idx <= endIdx; idx++)
        {
            varDecl = varDecls.get(idx);
            retry   = false;
            do
            {
                if (idx > startIdx)
                {
                    _printer.startTryMode();
                    if (retry)
                    {
                        _printer.writeln(",");
                    }
                    else
                    {
                        _printer.write(", ");
                    }
                    setStartPosition(varDecl);
                    startLine = getLine();
                }
                _printer.startStyle(Style.VARIABLE_IDENTIFIER);
                _printer.write(varDecl.getName());
                _printer.stopStyle(Style.VARIABLE_IDENTIFIER);
                if (!completeType)
                {
                    // we must append the dimensions to the variable names
                    for (int dim = 0; dim < varDecl.getType().getDimensions(); dim++)
                    {
                        _printer.write("[]");
                    }
                }
                if (varDecl.hasInitializer())
                {
                    int padColumns  = 0;

                    incIndent(_options.getVarDeclIndentationLevel());
                    if (_options.isVarDeclSpacesAroundOperator())
                    {
                        _printer.write(" ");
                    }
                    _printer.startStyle(Style.OPERATOR);
                    _printer.write("=");
                    _printer.stopStyle(Style.OPERATOR);
                    _printer.doGetMaxColumn();
                    if (_options.isVarDeclSpacesAroundOperator())
                    {
                        _printer.write(" ");
                    }
                    if (_options.isVarDeclRelativeIndentation())
                    {
                        padColumns = getPaddingLength(getColumn());
                        adjustPaddingLength(padColumns);
                    }
                    varDecl.getInitializer().accept(this);
                    decIndent(_options.getVarDeclIndentationLevel());
                    if (_options.isVarDeclRelativeIndentation())
                    {
                        adjustPaddingLength(-padColumns);
                    }
                }
                setFinishPosition(varDecl);
                if (idx == endIdx)
                {
                    _printer.write(";");
                }
                if (writeComment)
                {
                    appendComments(varDecl.getComments());
                }
                if (idx > startIdx)
                {
                    retry = !retry && isTooLong(startLine);
                    _printer.stopTryMode(!retry);
                }
            }
            while (retry);
        }
    }

    private void writeEmptyLines(int num)
    {
        ensureNewLine();
        for (int idx = 0; idx < num; idx++)
        {
            _printer.writeln();
        }
    }

    private void writeExpressionList(ExpressionList exprs)
    {
        if ((exprs == null) || (exprs.getExpressions().getCount() == 0))
        {
            return;
        }

        // the surrounding spaces are not part of the arg list
        // therefore we print them in advance
        if (!_printer.isNewLine() && _options.isExprListSurroundBySpaces())
        {
            writeSpaceIfNecessary();
        }

        boolean breakAll   = false;
        int     startLine  = getLine();

        // we have to determine the amount of padding characters
        // before we indent
        boolean pad        = (_options.getExprListIndentationMode() != Options.LIST_NORMAL_INDENTATION);
        int     padColumns = getPaddingLength(
                                _options.getExprListIndentationMode() == Options.LIST_RELATIVE_INDENTATION ?
                                    topContext()._listAlignmentBase :
                                    getColumn());

        incIndent(_options.getExprListIndentationLevel());

        if ((_options.getExprListBreakLimit() > 0) &&
            (exprs.getExpressions().getCount() >= _options.getExprListBreakLimit()))
        {
            breakAll = true;
        }

        // when we start on a newline, then the invocation has a break
        // right after the opening parenthesis (or an eol comment)
        // which causes us to indent the first param as well
        if (pad)
        {
            adjustPaddingLength(padColumns);
        }
        setStartPosition(exprs);
        if (!breakAll)
        {
            _printer.startTryMode();

            ExpressionIterator it = exprs.getExpressions().getIterator();

            it.getNext().accept(this);
            for (; it.hasNext();)
            {
                _printer.write(",");
                if (isTooLong(startLine))
                {
                    if (_options.isExprListBreakAllIfTooLong())
                    {
                        breakAll = true;
                        break;
                    }
                    else
                    {
                        _printer.writeln();
                        startLine = getLine();
                    }
                }
                else
                {
                    if (_options.isExprListSpaceAfterComma())
                    {
                        _printer.write(" ");
                    }
                }
                it.getNext().accept(this);
            }
            decIndent(_options.getExprListIndentationLevel());
            _printer.stopTryMode(!breakAll);
        }
        // break-all mode (possibly retry)
        if (breakAll)
        {
            for (ExpressionIterator it = exprs.getExpressions().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    _printer.writeln(",");
                }
            }
            decIndent(_options.getExprListIndentationLevel());
        }
        setFinishPosition(exprs);

        if (_options.isExprListSurroundBySpaces())
        {
            writeSpaceIfNecessary();
        }
        appendComments(exprs.getComments());
        if (pad)
        {
            adjustPaddingLength(-padColumns);
        }
    }

    private void writeInvocableDeclaration(InvocableDeclaration invocDecl)
    {
        prependComments(invocDecl.getComments());

        int column = getColumn();

        setStartPosition(invocDecl);
        visitModifiers(invocDecl.getModifiers());
        if (getColumn() != column)
        {
            writeSpaceIfNecessary();
        }

        if (invocDecl instanceof MethodDeclaration)
        {
            MethodDeclaration methodDecl = (MethodDeclaration)invocDecl;

            if (methodDecl.hasReturnType())
            {
                visitType(methodDecl.getReturnType());
            }
            else
            {
                _printer.startStyle(Style.KEYWORD);
                _printer.write("void");
                _printer.stopStyle(Style.KEYWORD);
            }
            writeSpaceIfNecessary();
        }
        _printer.startStyle(Style.METHOD_IDENTIFIER);
        _printer.write(invocDecl.getName());
        _printer.stopStyle(Style.METHOD_IDENTIFIER);

        _printer.write("(");
        if (invocDecl.hasParameters())
        {
            invocDecl.getParameterList().accept(this);
        }
        _printer.write(")");

        if (!invocDecl.getThrownExceptions().isEmpty())
        {
            boolean breakThrows = _options.isInvocDeclThrowsOnNextLine();
            int     startLine   = getLine();

            if (!breakThrows)
            {
                // we check whether we can write at least one exception
                _printer.startTryMode();
                _printer.write(" ");
                _printer.startStyle(Style.KEYWORD);
                _printer.write("throws");
                _printer.stopStyle(Style.KEYWORD);
                _printer.write(" ");
                invocDecl.getThrownExceptions().get(0).accept(this);

                breakThrows = isTooLong(startLine);
                _printer.stopTryMode(false);
            }
            if (breakThrows)
            {
                _printer.writeln();
                incIndent(_options.getInvocDeclThrowsIndentationLevel());
            }
            else
            {
                writeSpaceIfNecessary();
            }

            TypeIterator it = invocDecl.getThrownExceptions().getIterator();

            _printer.startStyle(Style.KEYWORD);
            _printer.write("throws");
            _printer.stopStyle(Style.KEYWORD);
            _printer.write(" ");
            visitType(it.getNext());

            int padColumns = getPaddingLength(getColumn()+1);

            if (_options.isInvocDeclAlignExceptions())
            {
                adjustPaddingLength(padColumns);
            }
            incIndent(_options.getInvocDeclExceptionIndentationLevel());

            Type    type;
            boolean isTooLong;

            for (; it.hasNext();)
            {
                startLine = getLine();
                type      = it.getNext();

                _printer.startTryMode();
                _printer.write(", ");
                visitType(type);
                isTooLong = isTooLong(startLine);

                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    _printer.writeln(",");
                    visitType(type);
                }
            }
            decIndent(_options.getInvocDeclExceptionIndentationLevel());
            if (_options.isInvocDeclAlignExceptions())
            {
                adjustPaddingLength(-padColumns);
            }
            if (breakThrows)
            {
                decIndent(_options.getInvocDeclThrowsIndentationLevel());
            }
        }

        if (invocDecl.hasBody())
        {
            if (_options.isInvocDeclBodyStartOnSameLine())
            {
                writeSpaceIfNecessary();
            }
            else
            {
                _printer.writeln();
            }
            invocDecl.getBody().accept(this);
            invocDecl.setFinishPosition(invocDecl.getBody().getFinishPosition());
        }
        else
        {
            _printer.write(";");
            setFinishPosition(invocDecl);
            _printer.writeln();
        }
    }

    private void writeJavaDocComment(Comment comment)
    {
        String text = comment.getText();
        int    next;
        int    prev;

        setStartPosition(comment);

        _printer.startStyle(Style.JAVADOC_COMMENT);
        next = text.indexOf('\n');
        if ((next == -1) && (comment.getTags() == null))
        {
            // fits on one line
            _printer.write("/** "+text);
        }
        else
        {
            ensureNewLine();
            _printer.writeln("/**");
            prev = 0;
            while (next >= 0)
            {
                _printer.writeln(" * "+text.substring(prev, next));
                prev = next + 1;
                next = text.indexOf('\n', prev);
            }
            if (prev < text.length() - 1)
            {
                _printer.writeln(" * "+text.substring(prev));
            }
            if (comment.getTags() != null)
            {
                _printer.writeln(" *");

                String value;

                for (StringIterator it = comment.getTags().getIterator(); it.hasNext();)
                {
                    value = it.getNext();
                    _printer.write(" * @");
                    next = value.indexOf('\n');
                    prev = 0;
                    while (next >= 0)
                    {
                        if (prev > 0)
                        {
                            _printer.write(" * ");
                        }
                        _printer.writeln(value.substring(prev, next));
                        prev = next + 1;
                        next = value.indexOf('\n', prev);
                    }
                    if (prev < value.length() - 1)
                    {
                        if (prev > 0)
                        {
                            _printer.write(" * ");
                        }
                        _printer.writeln(value.substring(prev));
                    }
                }
            }
        }
        _printer.write(" */");
        _printer.stopStyle(Style.JAVADOC_COMMENT);
        setFinishPosition(comment);
        _printer.writeln();
    }

    private void writeLineComment(Comment comment)
    {
        setStartPosition(comment);
        _printer.startStyle(Style.LINE_COMMENT);
        _printer.write("// ");
        _printer.write(comment.getText());
        _printer.stopStyle(Style.LINE_COMMENT);
        setFinishPosition(comment);
        _printer.writeln();
    }

    private void writePrimary(Primary primary, String text, String style)
    {
        boolean isTrying = writePrimaryPrepare(primary);

        _printer.startStyle(style);
        _printer.write(text);
        _printer.stopStyle(style);
        if (!writePrimaryFinish(primary, isTrying))
        {
            _printer.startStyle(style);
            _printer.write(text);
            _printer.stopStyle(style);
            writePrimaryFinish(primary, false);
        }
    }

    private boolean writePrimaryFinish(Primary primary, boolean isTrying)
    {
        Primary baseExpr = getBaseExpression(primary);

        setFinishPosition(primary);
        postWritePrimary(primary.getComments(),
                         (baseExpr != null) && topContext()._breakAllDots);

        if (isTrying)
        {
            boolean isTooLong = _printer.getMaxColumn() > _options.getMaxLineLength();

            _printer.stopTryMode(!isTooLong);
            if (isTooLong)
            {
                // we exceeded the allowed line length
                if (_options.isBreakEveryDotIfTooLong())
                {
                    topContext()._breakAllDots = true;
                }
                preWritePrimary(baseExpr, true);
                return false;
            }
        }
        if (isTrailEnd(primary))
        {
            popContext();
        }
        return true;
    }

    private boolean writePrimaryPrepare(Primary primary)
    {
        if (isTrailEnd(primary))
        {
            pushContext();
        }

        Primary baseExpr  = getBaseExpression(primary);
        boolean shouldTry = !topContext()._breakAllDots && (baseExpr != null);

        setStartPosition(primary);
        if (shouldTry)
        {
            _printer.startTryMode();
        }
        if (baseExpr == null)
        {
            _printer.doGetMaxColumn();
        }
        else
        {
            preWritePrimary(baseExpr, topContext()._breakAllDots);
        }
        return shouldTry;
    }

    private void writeSpaceIfNecessary()
    {
        if (_printer.getLastChar() != ' ')
        {
            _printer.write(" ");
        }
    }

    private void writeTypeBody(TypeDeclaration typeDecl)
    {
        ClassDeclaration classDecl = (typeDecl instanceof ClassDeclaration ?
                                         (ClassDeclaration)typeDecl :
                                         null);

        // we separate static final, static and non-static fields
        VarDeclArray     constants      = new VarDeclArray();
        VarDeclArray     staticFields   = new VarDeclArray();
        VarDeclArray     instanceFields = new VarDeclArray();
        FieldDeclaration fieldDecl;

        for (FieldIterator it = typeDecl.getFields().getIterator(); it.hasNext();)
        {
            fieldDecl = it.getNext();
            if (fieldDecl.getModifiers().isStatic())
            {
                if (fieldDecl.getModifiers().isFinal())
                {
                    constants.add(fieldDecl);
                }
                else
                {
                    staticFields.add(fieldDecl);
                }
            }
            else
            {
                instanceFields.add(fieldDecl);
            }
        }

        _printer.writeln("{");
        incIndent(_options.getTypeDeclIndentationLevel());

        // We determine whether specific feature blocks exists in order to avoid
        // useless empty lines
        boolean hasInnerTypes     = !typeDecl.getInnerTypes().isEmpty();
        boolean hasConstants      = !constants.isEmpty();
        boolean hasStaticFields   = !staticFields.isEmpty();
        boolean hasInstanceFields = !instanceFields.isEmpty();
        boolean hasInitializers   = (classDecl != null) && !classDecl.getInitializers().isEmpty();
        boolean hasConstructors   = (classDecl != null) && !classDecl.getConstructors().isEmpty();
        boolean hasMethods        = !typeDecl.getMethods().isEmpty();
        int     startLine         = getLine();

        if (hasInnerTypes)
        {
            for (TypeDeclarationIterator it = typeDecl.getInnerTypes().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    writeEmptyLines(_options.getTypeDeclEmptyLinesBetweenFeatures());
                }
            }
        }

        if (hasConstants)
        {
            startLine = separateFeatureBlock(startLine);
            writeVariableDeclarations(constants,
                                      _options.isFieldDeclCombineSubsequent(),
                                      _options.isFieldDeclAlignSubsequent(),
                                      _options.getTypeDeclEmptyLinesBetweenFeatures(),
                                      true);
        }

        if (hasStaticFields)
        {
            startLine = separateFeatureBlock(startLine);
            writeVariableDeclarations(staticFields,
                                      _options.isFieldDeclCombineSubsequent(),
                                      _options.isFieldDeclAlignSubsequent(),
                                      _options.getTypeDeclEmptyLinesBetweenFeatures(),
                                      true);
        }

        if (hasInstanceFields)
        {
            startLine = separateFeatureBlock(startLine);
            writeVariableDeclarations(instanceFields,
                                      _options.isFieldDeclCombineSubsequent(),
                                      _options.isFieldDeclAlignSubsequent(),
                                      _options.getTypeDeclEmptyLinesBetweenFeatures(),
                                      true);
        }

        if (hasInitializers)
        {
            startLine = separateFeatureBlock(startLine);
            for (InitializerIterator it = classDecl.getInitializers().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    writeEmptyLines(_options.getTypeDeclEmptyLinesBetweenFeatures());
                }
            }
        }

        if (hasConstructors)
        {
            startLine = separateFeatureBlock(startLine);
            for (ConstructorIterator it = classDecl.getConstructors().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    writeEmptyLines(_options.getTypeDeclEmptyLinesBetweenFeatures());
                }
            }
        }

        // note that we do not separate static and non-static methods

        if (hasMethods)
        {
            startLine = separateFeatureBlock(startLine);
            for (MethodIterator it = typeDecl.getMethods().getIterator(); it.hasNext();)
            {
                it.getNext().accept(this);
                if (it.hasNext())
                {
                    writeEmptyLines(_options.getTypeDeclEmptyLinesBetweenFeatures());
                }
            }
        }

        decIndent(_options.getTypeDeclIndentationLevel());
        ensureNewLine();
        _printer.write("}");
        setFinishPosition(typeDecl);
    }

    private void writeTypeDeclaration(TypeDeclaration typeDecl)
    {
        ClassDeclaration classDecl = (typeDecl instanceof ClassDeclaration ?
                                         (ClassDeclaration)typeDecl :
                                         null);

        prependComments(typeDecl.getComments());
        setStartPosition(typeDecl);

        int startColumn = getColumn();
        int startLine   = getLine();

        pushContext();
        if (classDecl == null)
        {
            topContext()._writeAbstractModifier = false;
        }
        visitModifiers(typeDecl.getModifiers());
        if (getColumn() != startColumn)
        {
            writeSpaceIfNecessary();
        }
        _printer.startStyle(Style.KEYWORD);
        if (classDecl != null)
        {
            _printer.write("class");
        }
        else
        {
            _printer.write("interface");
        }
        _printer.stopStyle(Style.KEYWORD);
        _printer.write(" ");
        _printer.startStyle(Style.TYPE_IDENTIFIER);
        _printer.write(typeDecl.getName());
        _printer.stopStyle(Style.TYPE_IDENTIFIER);

        startTypeScope(typeDecl);
        if (classDecl == null)
        {
            topContext()._writeFieldAccessModifiers = false;
        }

        int     keywordPadColumns = 0;
        boolean hasBaseClass      = (classDecl != null) &&
                                    classDecl.hasBaseClass() &&
                                    !"java.lang.Object".equals(
                                        classDecl.getBaseClass().getQualifiedName());
        boolean breakImplements   = false;

        incIndent(_options.getTypeDeclBasetypeIndentationLevel());

        if (hasBaseClass)
        {
            breakImplements = _options.isTypeDeclAlignBasetypes();

            String padding = (_options.isTypeDeclAlignBasetypes() &&
                              classDecl.hasBaseInterfaces() ?
                                  "   " :
                                  "");

            if (_options.isTypeDeclExtendsOnNewLine())
            {
                breakImplements = true;
                _printer.writeln();
            }
            else
            {
                _printer.startTryMode();
                writeSpaceIfNecessary();
            }
            keywordPadColumns = getPaddingLength(getColumn());
            _printer.startStyle(Style.KEYWORD);
            _printer.write("extends");
            _printer.stopStyle(Style.KEYWORD);
            _printer.write(" ");
            _printer.write(padding);
            visitType(classDecl.getBaseClass());
            if (!_options.isTypeDeclExtendsOnNewLine())
            {
                boolean isTooLong = isTooLong(startLine);

                _printer.stopTryMode(!isTooLong);
                if (isTooLong)
                {
                    breakImplements = true;
                    _printer.writeln();
                    keywordPadColumns = getPaddingLength(getColumn());
                    _printer.startStyle(Style.KEYWORD);
                    _printer.write("extends");
                    _printer.stopStyle(Style.KEYWORD);
                    _printer.write(" ");
                    _printer.write(padding);
                    visitType(classDecl.getBaseClass());
                }
            }
        }
        else
        {
            breakImplements = _options.isTypeDeclImplementsOnNewLine();
        }

        if (typeDecl.hasBaseInterfaces())
        {
            startLine = getLine();

            if (!breakImplements)
            {
                // we check whether we can write at least one basetype
                _printer.startTryMode();
                if (_options.isTypeDeclAlignBasetypes())
                {
                    adjustPaddingLength(keywordPadColumns);
                }
                writeSpaceIfNecessary();
                _printer.startStyle(Style.KEYWORD);
                if (classDecl != null)
                {
                    _printer.write("implements");
                }
                else
                {
                    _printer.write("extends");
                }
                _printer.stopStyle(Style.KEYWORD);
                _printer.write(" ");
                visitType(typeDecl.getBaseInterfaces().get(0));

                breakImplements = isTooLong(startLine);
                _printer.stopTryMode(false);
            }
            if (breakImplements)
            {
                _printer.writeln();
            }
            else
            {
                writeSpaceIfNecessary();
            }

            TypeIterator it = typeDecl.getBaseInterfaces().getIterator();

            if (_options.isTypeDeclAlignBasetypes())
            {
                if (keywordPadColumns == 0)
                {
                    keywordPadColumns = getPaddingLength(getColumn());
                }
                adjustPaddingLength(keywordPadColumns);
            }
            _printer.startStyle(Style.KEYWORD);
            if (classDecl != null)
            {
                _printer.write("implements");
            }
            else
            {
                _printer.write("extends");
            }
            _printer.stopStyle(Style.KEYWORD);
            _printer.write(" ");

            int padColumns = getPaddingLength(getColumn()+1);

            visitType(it.getNext());

            if (_options.isTypeDeclAlignBasetypes())
            {
                adjustPaddingLength(11);
            }

            Type    type;
            boolean breakType;

            for (; it.hasNext();)
            {
                type      = it.getNext();
                breakType = _options.isTypeDeclBreakAllBasetypes();

                if (!breakType)
                {
                    startLine = getLine();

                    _printer.startTryMode();
                    _printer.write(", ");
                    visitType(type);

                    breakType = isTooLong(startLine);

                    _printer.stopTryMode(!breakType);
                }
                if (breakType)
                {
                    _printer.writeln(",");
                    visitType(type);
                }
            }
            if (_options.isTypeDeclAlignBasetypes())
            {
                adjustPaddingLength(-keywordPadColumns - 11);
            }
        }
        decIndent(_options.getTypeDeclBasetypeIndentationLevel());

        if (_options.isTypeDeclBodyStartOnSameLine())
        {
            writeSpaceIfNecessary();
        }
        else
        {
            _printer.writeln();
        }
        writeTypeBody(typeDecl);
        stopTypeScope();
        popContext();
        ensureNewLine();
    }

    // writes an array of variables
    private void writeVariableDeclarations(VarDeclArray varDecls,
                                           boolean      combine,
                                           boolean      align,
                                           int          numSepLines,
                                           boolean      prependComments)
    {
        if (varDecls.isEmpty())
        {
            return;
        }

        VariableDeclaration varDecl;
        int[]               starts      = new int[varDecls.getCount()];
        int[]               ends        = new int[starts.length];
        boolean[]           useComplete = new boolean[starts.length];
        int                 last        = -1;
        int                 idx         = 0;
        Type                baseType    = null;
        Type                varType;
        String              signature;
        int                 alignColumn = 0;
        int                 curAlignColumn;
        int                 padColumns;
        int                 curColumn   = getColumn();

        // We first have to split the declarations such that
        // we only condense declarations with the same base type
        // Note that any declarations where the type has a comment come
        // first in such an array (iow, all other declarations in the
        // array have types without comments)
        while (idx < varDecls.getCount())
        {
            varDecl = varDecls.get(idx);
            varType = varDecl.getType();

            starts[++last]    = idx;
            useComplete[last] = true;
            signature         = modifiersToString(varDecl.getModifiers());
            if (signature.length() > 0)
            {
                signature += " ";
            }
            if (combine)
            {
                Type curType;

                baseType = varType.getBaseType();
                while (++idx < varDecls.getCount())
                {
                    varDecl = varDecls.get(idx);
                    curType = varDecl.getType();
                    if (!baseType.isEqualTo(curType.getBaseType()) ||
                        curType.hasComments() ||
                        (prependComments && varDecl.hasComments()))
                    {
                        break;
                    }
                    if (!varType.isEqualTo(curType))
                    {
                        useComplete[last] = false;
                    }
                }
                ends[last] = idx - 1;
            }
            else
            {
                ends[last] = idx;
                idx++;
            }
            if (useComplete[last])
            {
                signature += typeToString(varType);
            }
            else
            {
                baseType.setComments(varType.getComments());
                signature += typeToString(baseType);
            }
            signature = signature.trim();
            if (align)
            {
                // we determine the alignment column if necessary
                curAlignColumn = curColumn + signature.length() + 1;
                if (curAlignColumn > alignColumn)
                {
                    alignColumn = curAlignColumn;
                }
            }
        }
        // now we print the decls
        for (idx = 0; idx <= last; idx++)
        {
            if (idx > 0)
            {
                writeEmptyLines(numSepLines);
            }
            varDecl = varDecls.get(starts[idx]);
            varType = varDecl.getType();

            if (prependComments)
            {
                prependComments(varDecl.getComments());
            }
            setStartPosition(varDecl);
            curColumn = getColumn();
            visitModifiers(varDecl.getModifiers());
            if (curColumn != getColumn())
            {
                writeSpaceIfNecessary();
            }
            if (useComplete[idx])
            {
                visitType(varType);
            }
            else
            {
                baseType = varType.getBaseType();
                visitType(baseType);
                varType.setStartPosition(baseType.getStartPosition());
                varType.setFinishPosition(baseType.getFinishPosition());
            }
            writeSpaceIfNecessary();

            if (align)
            {
                for (curColumn = getColumn(); curColumn < alignColumn; curColumn++)
                {
                    _printer.write(" ");
                }
            }

            padColumns = getPaddingLength(getColumn());

            adjustPaddingLength(padColumns);

            // now we write all 'name = initializer' expressions
            writeCompositeDeclarations(varDecls,
                                       starts[idx],
                                       ends[idx],
                                       useComplete[idx],
                                       !prependComments);

            adjustPaddingLength(-padColumns);
        }
    }
}
