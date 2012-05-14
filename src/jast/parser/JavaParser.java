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
// $ANTLR 2.7.2a2 (20020112-1): "parser.g" -> "JavaParser.java"$
package jast.parser;

import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.ParsePosition;
import jast.ast.Project;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.BlockStatement;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CaseBlock;
import jast.ast.nodes.CatchClause;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.Expression;
import jast.ast.nodes.Feature;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LabeledStatement;
import jast.ast.nodes.Literal;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.Primary;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Statement;
import jast.ast.nodes.StatementExpressionList;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableDeclaration;
import jast.ast.nodes.VariableInitializer;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public class JavaParser extends antlr.LLkParser
       implements JavaTokenTypes
 {

    public static final String PROPERTY_BODY_START = "bodyStart";

    private ParserHelper _helper  = null;
    private NodeFactory  _factory = null;

    public void setHelper(ParserHelper helper)
    {
        if (helper != null)
            {
            _helper = helper;
            _factory = _helper.getNodeFactory();
        }
    }

    private void setStartPos(ParsePosition start, Token token)
    {
        start.setIfFirstTime(
            token.getLine(),
            token.getColumn(),
            ((ExtendedToken) token).getProcessedPosition());
    }

    private void setFinishPos(ParsePosition finish, Token token)
    {
        int offset = token.getText().length() - 1;

        finish.set(
            token.getLine(),
            token.getColumn() + offset,
            ((ExtendedToken) token).getProcessedPosition() + offset);
    }

    private RecognitionException generateException(
        String msg,
        int line,
        int column)
    {
        RecognitionException ex = new RecognitionException(msg, getFilename(), line);

        ex.column = column;

        return ex;
    }

    private Type fixType(Type source, int addedDimensions)
    {
        Type result = source.getClone();

        result.setDimensions(result.getDimensions() + addedDimensions);
        return result;
    }

    // if we have an unresolved access with only one part, then it is
    // possible that we identify it as a field/variable access
    private Primary finalizeUnresolvedAccess(UnresolvedAccess access)
    {
        if (access.getParts().getCount() != 1)
            {
            return access;
        }

        Primary result = null;
        ParsePosition start = access.getLastStartPosition();
        ParsePosition finish = access.getLastFinishPosition();
        String part = access.getParts().getLast();

        if (!access.isTrailing())
            {
            // it can be a variable access
            LocalVariableDeclaration localVarDecl = _helper.resolveLocalVariable(part);

            if (localVarDecl != null)
                {
                result = _factory.createVariableAccess(localVarDecl);
            }
            else
                {
                // if we're somewhere in a local class we cannot know whether it is
                // an access to a field or a local variable
                if (_helper.isInLocalClass())
                    {
                    return access;
                }
                result = _factory.createFieldAccess(part);
            }
        }
        else
            {
            // can only be a field access
            result = _factory.createFieldAccess(access.getBaseExpression(), part);
            // we must use the absolute start position (including the base expression)
            start = access.getStartPosition();
        }
        access.splitLastPart();
        result.setStartPosition(start);
        result.setFinishPosition(finish);
        return result;
    }

    protected JavaParser(TokenBuffer tokenBuf, int k)
    {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public JavaParser(TokenBuffer tokenBuf)
    {
        this(tokenBuf, 2);
    }

    protected JavaParser(TokenStream lexer, int k)
    {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public JavaParser(TokenStream lexer)
    {
        this(lexer, 2);
    }

    public JavaParser(ParserSharedInputState state)
    {
        super(state, 2);
        tokenNames = _tokenNames;
    }

    public final void testVariableDeclarator()
        throws RecognitionException, TokenStreamException
    {

        VariableDeclaration dummy = null;

        dummy = variableDeclarator(null, null, false);
    }

    public final VariableDeclaration variableDeclarator(
        Modifiers mods,
        Type type,
        boolean isField)
        throws RecognitionException, TokenStreamException
    {
        VariableDeclaration result;

        VariableDeclaratorId id = null;
        VariableInitializer init = null;

        result = null;

        id = variableDeclaratorId();
        {
            switch (LA(1))
                {
                case OP_ASSIGN :
                    {
                        match(OP_ASSIGN);
                        init = variableInitializer();
                        break;
                    }
                case EOF :
                case SEP_SEMICOLON :
                case SEP_COMMA :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            // Since we assume that the code is correct we do not have to
            // check whether there is already a field/variable of that name
            if (isField)
                {
                if ((init != null) && _helper.isParsingInterfaceOnly())
                    {
                    // as we're already parsed the initializer, we
                    // only have to reduce it
                    // even though there is no need to set the position property
                    // as we retain the initializer object itself, we do so
                    // nonetheless as it indicates that the initializer is not
                    // parsed
                    if (init instanceof SingleInitializer)
                        {
                        ((SingleInitializer) init).setInitExpression(null);
                    }
                    else
                        {
                        ((ArrayInitializer) init).getInitializers().clear();
                    }
                    init.setProperty(PROPERTY_BODY_START, init.getStartPosition());
                }
                result =
                    _factory.createFieldDeclaration(
                        mods.getClone(),
                        fixType(type, id.getDimensions()),
                        id.getName(),
                        init);
            }
            else
                {
                result =
                    _factory.createLocalVariableDeclaration(
                        mods.isFinal(),
                        fixType(type, id.getDimensions()),
                        id.getName(),
                        init);
            }
            _helper.defineVariable(result);
            result.setStartPosition(id.getStartPosition());
            result.setFinishPosition(
                init != null ? init.getFinishPosition() : id.getFinishPosition());

        }
        return result;
    }

    public final void testStatement()
        throws RecognitionException, TokenStreamException
    {

        BlockStatement dummy = null;

        dummy = blockStatement();
    }

    public final BlockStatement blockStatement()
        throws RecognitionException, TokenStreamException
    {
        BlockStatement result;

        result = null;

        boolean synPredMatched126 = false;
        if (((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2)))))
            {
            int _m126 = mark();
            synPredMatched126 = true;
            inputState.guessing++;
            try
                {
                {
                    localVariableDeclaration();
                }
            }
            catch (RecognitionException pe)
                {
                synPredMatched126 = false;
            }
            rewind(_m126);
            inputState.guessing--;
        }
        if (synPredMatched126)
            {
            result = localVariableDeclaration();
            match(SEP_SEMICOLON);
        }
        else if ((_tokenSet_2.member(LA(1))) && (_tokenSet_3.member(LA(2))))
            {
            result = statement();
        }
        else if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2))))
            {
            result = classDeclaration(true);
        }
        else
            {
            throw new NoViableAltException(LT(1), getFilename());
        }

        return result;
    }

    public final void testExpression()
        throws RecognitionException, TokenStreamException
    {

        Expression dummy = null;

        dummy = expression();
    }

    public final Expression expression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        result = assignmentExpression();
        return result;
    }

    public final void testPrimary()
        throws RecognitionException, TokenStreamException
    {

        Expression dummy = null;

        dummy = primary();
    }

    public final Primary primary()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        result = null;

        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case FALSE_LITERAL :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                    {
                        result = literal();
                        break;
                    }
                case SEP_OPENING_PARENTHESIS :
                    {
                        result = parenthesizedExpression();
                        break;
                    }
                case KEYWORD_VOID :
                    {
                        result = voidClassAccess();
                        break;
                    }
                default :
                    boolean synPredMatched233 = false;
                    if (((LA(1) == KEYWORD_NEW) && (_tokenSet_6.member(LA(2)))))
                        {
                        int _m233 = mark();
                        synPredMatched233 = true;
                        inputState.guessing++;
                        try
                            {
                            {
                                match(KEYWORD_NEW);
                                {
                                    switch (LA(1))
                                        {
                                        case IDENTIFIER :
                                            {
                                                qualifiedName(null);
                                                break;
                                            }
                                        case KEYWORD_BOOLEAN :
                                        case KEYWORD_BYTE :
                                        case KEYWORD_CHAR :
                                        case KEYWORD_DOUBLE :
                                        case KEYWORD_FLOAT :
                                        case KEYWORD_INT :
                                        case KEYWORD_LONG :
                                        case KEYWORD_SHORT :
                                            {
                                                primitiveTypeName(null);
                                                break;
                                            }
                                        default :
                                            {
                                                throw new NoViableAltException(LT(1), getFilename());
                                            }
                                    }
                                }
                                match(SEP_OPENING_BRACKET);
                            }
                        }
                        catch (RecognitionException pe)
                            {
                            synPredMatched233 = false;
                        }
                        rewind(_m233);
                        inputState.guessing--;
                    }
                    if (synPredMatched233)
                        {
                        result = arrayCreation();
                    }
                    else if ((LA(1) == KEYWORD_NEW) && (LA(2) == IDENTIFIER))
                        {
                        result = instantiation(null);
                    }
                    else
                        {
                        boolean synPredMatched235 = false;
                        if (((LA(1) == KEYWORD_THIS) && (LA(2) == SEP_OPENING_PARENTHESIS)))
                            {
                            int _m235 = mark();
                            synPredMatched235 = true;
                            inputState.guessing++;
                            try
                                {
                                {
                                    match(KEYWORD_THIS);
                                    match(SEP_OPENING_PARENTHESIS);
                                }
                            }
                            catch (RecognitionException pe)
                                {
                                synPredMatched235 = false;
                            }
                            rewind(_m235);
                            inputState.guessing--;
                        }
                        if (synPredMatched235)
                            {
                            result = alternateConstructorInvocation();
                        }
                        else
                            {
                            boolean synPredMatched237 = false;
                            if (((LA(1) == KEYWORD_SUPER) && (LA(2) == SEP_OPENING_PARENTHESIS)))
                                {
                                int _m237 = mark();
                                synPredMatched237 = true;
                                inputState.guessing++;
                                try
                                    {
                                    {
                                        match(KEYWORD_SUPER);
                                        match(SEP_OPENING_PARENTHESIS);
                                    }
                                }
                                catch (RecognitionException pe)
                                    {
                                    synPredMatched237 = false;
                                }
                                rewind(_m237);
                                inputState.guessing--;
                            }
                            if (synPredMatched237)
                                {
                                result = superClassConstructorInvocation(null);
                            }
                            else
                                {
                                boolean synPredMatched240 = false;
                                if (((LA(1) == KEYWORD_THIS || LA(1) == IDENTIFIER)
                                    && (_tokenSet_7.member(LA(2)))))
                                    {
                                    int _m240 = mark();
                                    synPredMatched240 = true;
                                    inputState.guessing++;
                                    try
                                        {
                                        {
                                            {
                                                switch (LA(1))
                                                    {
                                                    case IDENTIFIER :
                                                        {
                                                            qualifiedName(null);
                                                            match(SEP_DOT);
                                                            break;
                                                        }
                                                    case KEYWORD_THIS :
                                                        {
                                                            break;
                                                        }
                                                    default :
                                                        {
                                                            throw new NoViableAltException(LT(1), getFilename());
                                                        }
                                                }
                                            }
                                            match(KEYWORD_THIS);
                                        }
                                    }
                                    catch (RecognitionException pe)
                                        {
                                        synPredMatched240 = false;
                                    }
                                    rewind(_m240);
                                    inputState.guessing--;
                                }
                                if (synPredMatched240)
                                    {
                                    result = selfAccess();
                                }
                                else
                                    {
                                    boolean synPredMatched243 = false;
                                    if (((LA(1) == KEYWORD_SUPER || LA(1) == IDENTIFIER) && (LA(2) == SEP_DOT)))
                                        {
                                        int _m243 = mark();
                                        synPredMatched243 = true;
                                        inputState.guessing++;
                                        try
                                            {
                                            {
                                                {
                                                    switch (LA(1))
                                                        {
                                                        case IDENTIFIER :
                                                            {
                                                                qualifiedName(null);
                                                                match(SEP_DOT);
                                                                break;
                                                            }
                                                        case KEYWORD_SUPER :
                                                            {
                                                                break;
                                                            }
                                                        default :
                                                            {
                                                                throw new NoViableAltException(LT(1), getFilename());
                                                            }
                                                    }
                                                }
                                                match(KEYWORD_SUPER);
                                                match(SEP_DOT);
                                            }
                                        }
                                        catch (RecognitionException pe)
                                            {
                                            synPredMatched243 = false;
                                        }
                                        rewind(_m243);
                                        inputState.guessing--;
                                    }
                                    if (synPredMatched243)
                                        {
                                        result = baseClassFeatureAccess();
                                    }
                                    else
                                        {
                                        boolean synPredMatched245 = false;
                                        if (((_tokenSet_6.member(LA(1)))
                                            && (LA(2) == SEP_DOT || LA(2) == SEP_OPENING_BRACKET)))
                                            {
                                            int _m245 = mark();
                                            synPredMatched245 = true;
                                            inputState.guessing++;
                                            try
                                                {
                                                {
                                                    type();
                                                    match(SEP_DOT);
                                                    match(KEYWORD_CLASS);
                                                }
                                            }
                                            catch (RecognitionException pe)
                                                {
                                                synPredMatched245 = false;
                                            }
                                            rewind(_m245);
                                            inputState.guessing--;
                                        }
                                        if (synPredMatched245)
                                            {
                                            result = classAccess();
                                        }
                                        else
                                            {
                                            boolean synPredMatched247 = false;
                                            if (((LA(1) == IDENTIFIER) && (LA(2) == SEP_OPENING_PARENTHESIS)))
                                                {
                                                int _m247 = mark();
                                                synPredMatched247 = true;
                                                inputState.guessing++;
                                                try
                                                    {
                                                    {
                                                        identifier(null);
                                                        match(SEP_OPENING_PARENTHESIS);
                                                    }
                                                }
                                                catch (RecognitionException pe)
                                                    {
                                                    synPredMatched247 = false;
                                                }
                                                rewind(_m247);
                                                inputState.guessing--;
                                            }
                                            if (synPredMatched247)
                                                {
                                                result = directMethodAccess();
                                            }
                                            else if ((LA(1) == IDENTIFIER) && (_tokenSet_7.member(LA(2))))
                                                {
                                                result = unresolvedAccess();
                                            }
                                            else
                                                {
                                                throw new NoViableAltException(LT(1), getFilename());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
        {
            _loop251 : do {
                if ((LA(1) == SEP_DOT || LA(1) == SEP_OPENING_BRACKET))
                    {
                    {
                        switch (LA(1))
                            {
                            case SEP_DOT :
                                {
                                    match(SEP_DOT);
                                    {
                                        switch (LA(1))
                                            {
                                            case KEYWORD_NEW :
                                                {
                                                    result = instantiation(result);
                                                    break;
                                                }
                                            case IDENTIFIER :
                                                {
                                                    result = trailingFeatureAccess(result);
                                                    break;
                                                }
                                            case KEYWORD_SUPER :
                                                {
                                                    result = superClassConstructorInvocation(result);
                                                    break;
                                                }
                                            default :
                                                {
                                                    throw new NoViableAltException(LT(1), getFilename());
                                                }
                                        }
                                    }
                                    break;
                                }
                            case SEP_OPENING_BRACKET :
                                {
                                    result = trailingArrayAccess(result);
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                }
                else
                    {
                    break _loop251;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            if (result instanceof UnresolvedAccess)
                {
                result = finalizeUnresolvedAccess((UnresolvedAccess) result);
            }

        }
        return result;
    }

    public final CompilationUnit compilationUnit(Project project)
        throws RecognitionException, TokenStreamException
    {
        CompilationUnit result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition finish = new ParsePosition();
        ImportDeclaration importDecl = null;
        String pckgName = "";
        String name = null;
        boolean isOnDemand = false;
        TypeDeclaration decl = null;

        result = _factory.createCompilationUnit(_helper.getUnitName());
        result.setComplete(!_helper.isParsingInterfaceOnly());

        {
            switch (LA(1))
                {
                case KEYWORD_PACKAGE :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_PACKAGE);
                        pckgName = qualifiedName(null);
                        tok2 = LT(1);
                        match(SEP_SEMICOLON);
                        if (inputState.guessing == 0)
                            {

                            setStartPos(start, tok1);
                            setFinishPos(finish, tok2);

                        }
                        break;
                    }
                case EOF :
                case KEYWORD_ABSTRACT :
                case KEYWORD_CLASS :
                case KEYWORD_FINAL :
                case KEYWORD_IMPORT :
                case KEYWORD_INTERFACE :
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                case KEYWORD_STRICTFP :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            _loop9 : do {
                if ((LA(1) == KEYWORD_IMPORT))
                    {
                    if (inputState.guessing == 0)
                        {

                        isOnDemand = false;

                    }
                    tok3 = LT(1);
                    match(KEYWORD_IMPORT);
                    name = qualifiedName(null);
                    {
                        switch (LA(1))
                            {
                            case SEP_DOT :
                                {
                                    match(SEP_DOT);
                                    match(OP_MULTIPLY);
                                    if (inputState.guessing == 0)
                                        {

                                        isOnDemand = true;

                                    }
                                    break;
                                }
                            case SEP_SEMICOLON :
                                {
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    tok4 = LT(1);
                    match(SEP_SEMICOLON);
                    if (inputState.guessing == 0)
                        {

                        importDecl = _factory.createImportDeclaration(name, isOnDemand);
                        result.getImportDeclarations().add(importDecl);
                        setStartPos(importDecl.getStartPosition(), tok3);
                        setFinishPos(importDecl.getFinishPosition(), tok4);
                        setStartPos(start, tok3);
                        setFinishPos(finish, tok4);

                    }
                }
                else
                    {
                    break _loop9;
                }

            }
            while (true);
        }
        {
            _loop12 : do {
                if ((_tokenSet_8.member(LA(1))))
                    {
                    {
                        if ((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2))))
                            {
                            decl = classDeclaration(false);
                        }
                        else if ((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2))))
                            {
                            decl = interfaceDeclaration();
                        }
                        else
                            {
                            throw new NoViableAltException(LT(1), getFilename());
                        }

                    }
                    if (inputState.guessing == 0)
                        {

                        result.getTypeDeclarations().add(decl);
                        finish.copyFrom(decl.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop12;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result.setStartPosition(start);
            result.setFinishPosition(finish);

            // Until here the compilation unit has nothing to do at all with the project
            project.addCompilationUnit(result, pckgName);

        }
        return result;
    }

    public final String qualifiedName(ParsePosition start)
        throws RecognitionException, TokenStreamException
    {
        String name;

        Token id1 = null;
        Token id2 = null;

        StringBuffer text = new StringBuffer();

        name = null;

        id1 = LT(1);
        match(IDENTIFIER);
        if (inputState.guessing == 0)
            {

            text.append(id1.getText());
            if (start != null)
                {
                setStartPos(start, id1);
            }

        }
        {
            _loop309 : do {
                if ((LA(1) == SEP_DOT) && (LA(2) == IDENTIFIER))
                    {
                    match(SEP_DOT);
                    id2 = LT(1);
                    match(IDENTIFIER);
                    if (inputState.guessing == 0)
                        {

                        text.append(".");
                        text.append(id2.getText());

                    }
                }
                else
                    {
                    break _loop309;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            name = text.toString();

        }
        return name;
    }

    public final ClassDeclaration classDeclaration(boolean isLocal)
        throws RecognitionException, TokenStreamException
    {
        ClassDeclaration result;

        Token tok1 = null;

        ParsePosition finish = new ParsePosition();
        Modifiers mods = _factory.createModifiers();
        Type base = null;
        String name = null;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_ABSTRACT :
                case KEYWORD_FINAL :
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                case KEYWORD_STRICTFP :
                    {
                        classModifiers(mods);
                        break;
                    }
                case KEYWORD_CLASS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok1 = LT(1);
        match(KEYWORD_CLASS);
        name = identifier(null);
        if (inputState.guessing == 0)
            {

            result = _factory.createClassDeclaration(mods, name, isLocal);
            result.setStartPosition(mods.getStartPosition());
            setStartPos(result.getStartPosition(), tok1);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_EXTENDS :
                    {
                        match(KEYWORD_EXTENDS);
                        base = type();
                        if (inputState.guessing == 0)
                            {

                            result.setBaseClass(base);

                        }
                        break;
                    }
                case KEYWORD_IMPLEMENTS :
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            switch (LA(1))
                {
                case KEYWORD_IMPLEMENTS :
                    {
                        match(KEYWORD_IMPLEMENTS);
                        base = type();
                        if (inputState.guessing == 0)
                            {

                            result.getBaseInterfaces().add(base);

                        }
                        {
                            _loop28 : do {
                                if ((LA(1) == SEP_COMMA))
                                    {
                                    match(SEP_COMMA);
                                    base = type();
                                    if (inputState.guessing == 0)
                                        {

                                        result.getBaseInterfaces().add(base);

                                    }
                                }
                                else
                                    {
                                    break _loop28;
                                }

                            }
                            while (true);
                        }
                        break;
                    }
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            _helper.pushTypeScope(result);

        }
        classBody(result, new ParsePosition(), finish);
        if (inputState.guessing == 0)
            {

            result.setFinishPosition(finish);
            _helper.popTypeScope();

        }
        return result;
    }

    public final InterfaceDeclaration interfaceDeclaration()
        throws RecognitionException, TokenStreamException
    {
        InterfaceDeclaration result;

        Token tok1 = null;
        Token tok2 = null;

        Modifiers mods = _factory.createModifiers();
        Type base = null;
        String name = null;
        Feature decl = null;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_ABSTRACT :
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                case KEYWORD_STRICTFP :
                    {
                        interfaceModifiers(mods);
                        break;
                    }
                case KEYWORD_INTERFACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok1 = LT(1);
        match(KEYWORD_INTERFACE);
        name = identifier(null);
        if (inputState.guessing == 0)
            {

            result = _factory.createInterfaceDeclaration(mods, name);
            result.setStartPosition(mods.getStartPosition());
            setStartPos(result.getStartPosition(), tok1);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_EXTENDS :
                    {
                        match(KEYWORD_EXTENDS);
                        base = type();
                        if (inputState.guessing == 0)
                            {

                            result.getBaseInterfaces().add(base);

                        }
                        {
                            _loop17 : do {
                                if ((LA(1) == SEP_COMMA))
                                    {
                                    match(SEP_COMMA);
                                    base = type();
                                    if (inputState.guessing == 0)
                                        {

                                        result.getBaseInterfaces().add(base);

                                    }
                                }
                                else
                                    {
                                    break _loop17;
                                }

                            }
                            while (true);
                        }
                        break;
                    }
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            _helper.pushTypeScope(result);

        }
        match(SEP_OPENING_BRACE);
        {
            _loop19 : do {
                switch (LA(1))
                    {
                    case KEYWORD_ABSTRACT :
                    case KEYWORD_BOOLEAN :
                    case KEYWORD_BYTE :
                    case KEYWORD_CHAR :
                    case KEYWORD_CLASS :
                    case KEYWORD_DOUBLE :
                    case KEYWORD_FINAL :
                    case KEYWORD_FLOAT :
                    case KEYWORD_INT :
                    case KEYWORD_INTERFACE :
                    case KEYWORD_LONG :
                    case KEYWORD_PRIVATE :
                    case KEYWORD_PROTECTED :
                    case KEYWORD_PUBLIC :
                    case KEYWORD_SHORT :
                    case KEYWORD_STATIC :
                    case KEYWORD_STRICTFP :
                    case KEYWORD_VOID :
                    case IDENTIFIER :
                        {
                            decl = interfaceBodyDeclaration();
                            if (inputState.guessing == 0)
                                {

                                if (decl instanceof MultipleVariableDeclaration)
                                    {
                                    ((MultipleVariableDeclaration) decl).addTo(result);
                                }
                                else
                                    {
                                    result.addDeclaration(decl);
                                }

                            }
                            break;
                        }
                    case SEP_SEMICOLON :
                        {
                            match(SEP_SEMICOLON);
                            break;
                        }
                    default :
                        {
                            break _loop19;
                        }
                }
            }
            while (true);
        }
        tok2 = LT(1);
        match(SEP_CLOSING_BRACE);
        if (inputState.guessing == 0)
            {

            setFinishPos(result.getFinishPosition(), tok2);
            _helper.popTypeScope();

        }
        return result;
    }

    public final void interfaceModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;
        Token tok5 = null;
        Token tok6 = null;

        {
            int _cnt22 = 0;
            _loop22 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_PROTECTED :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_PROTECTED);
                            if (inputState.guessing == 0)
                                {

                                mods.setProtected();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_PRIVATE :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_PRIVATE);
                            if (inputState.guessing == 0)
                                {

                                mods.setPrivate();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    case KEYWORD_ABSTRACT :
                        {
                            tok4 = LT(1);
                            match(KEYWORD_ABSTRACT);
                            if (inputState.guessing == 0)
                                {

                                mods.setAbstract();
                                setStartPos(mods.getStartPosition(), tok4);
                                setFinishPos(mods.getFinishPosition(), tok4);

                            }
                            break;
                        }
                    case KEYWORD_STATIC :
                        {
                            tok5 = LT(1);
                            match(KEYWORD_STATIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setStatic();
                                setStartPos(mods.getStartPosition(), tok5);
                                setFinishPos(mods.getFinishPosition(), tok5);

                            }
                            break;
                        }
                    case KEYWORD_STRICTFP :
                        {
                            tok6 = LT(1);
                            match(KEYWORD_STRICTFP);
                            if (inputState.guessing == 0)
                                {

                                mods.setStrictfp();
                                setStartPos(mods.getStartPosition(), tok6);
                                setFinishPos(mods.getFinishPosition(), tok6);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt22 >= 1)
                                {
                                break _loop22;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt22++;
            }
            while (true);
        }
    }

    public final String identifier(ParsePosition start)
        throws RecognitionException, TokenStreamException
    {
        String result;

        Token id = null;

        result = null;

        id = LT(1);
        match(IDENTIFIER);
        if (inputState.guessing == 0)
            {

            result = id.getText();
            if (start != null)
                {
                setStartPos(start, id);
            }

        }
        return result;
    }

    public final Type type() throws RecognitionException, TokenStreamException
    {
        Type result;

        switch (LA(1))
            {
            case IDENTIFIER :
                {
                    result = referenceType();
                    break;
                }
            case KEYWORD_BOOLEAN :
            case KEYWORD_BYTE :
            case KEYWORD_CHAR :
            case KEYWORD_DOUBLE :
            case KEYWORD_FLOAT :
            case KEYWORD_INT :
            case KEYWORD_LONG :
            case KEYWORD_SHORT :
                {
                    result = primitiveType();
                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return result;
    }

    public final Feature interfaceBodyDeclaration()
        throws RecognitionException, TokenStreamException
    {
        Feature result;

        Modifiers dummy = _factory.createModifiers();

        result = null;

        boolean synPredMatched39 = false;
        if (((_tokenSet_11.member(LA(1))) && (_tokenSet_12.member(LA(2)))))
            {
            int _m39 = mark();
            synPredMatched39 = true;
            inputState.guessing++;
            try
                {
                {
                    {
                        switch (LA(1))
                            {
                            case KEYWORD_ABSTRACT :
                            case KEYWORD_PUBLIC :
                                {
                                    abstractMethodModifiers(dummy);
                                    break;
                                }
                            case KEYWORD_BOOLEAN :
                            case KEYWORD_BYTE :
                            case KEYWORD_CHAR :
                            case KEYWORD_DOUBLE :
                            case KEYWORD_FLOAT :
                            case KEYWORD_INT :
                            case KEYWORD_LONG :
                            case KEYWORD_SHORT :
                            case KEYWORD_VOID :
                            case IDENTIFIER :
                                {
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    {
                        switch (LA(1))
                            {
                            case KEYWORD_VOID :
                                {
                                    match(KEYWORD_VOID);
                                    break;
                                }
                            case KEYWORD_BOOLEAN :
                            case KEYWORD_BYTE :
                            case KEYWORD_CHAR :
                            case KEYWORD_DOUBLE :
                            case KEYWORD_FLOAT :
                            case KEYWORD_INT :
                            case KEYWORD_LONG :
                            case KEYWORD_SHORT :
                            case IDENTIFIER :
                                {
                                    type();
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    identifier(null);
                    match(SEP_OPENING_PARENTHESIS);
                }
            }
            catch (RecognitionException pe)
                {
                synPredMatched39 = false;
            }
            rewind(_m39);
            inputState.guessing--;
        }
        if (synPredMatched39)
            {
            result = abstractMethodDeclaration();
        }
        else
            {
            boolean synPredMatched42 = false;
            if (((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2)))))
                {
                int _m42 = mark();
                synPredMatched42 = true;
                inputState.guessing++;
                try
                    {
                    {
                        {
                            switch (LA(1))
                                {
                                case KEYWORD_ABSTRACT :
                                case KEYWORD_FINAL :
                                case KEYWORD_PRIVATE :
                                case KEYWORD_PROTECTED :
                                case KEYWORD_PUBLIC :
                                case KEYWORD_STATIC :
                                case KEYWORD_STRICTFP :
                                    {
                                        classModifiers(dummy);
                                        break;
                                    }
                                case KEYWORD_CLASS :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        match(KEYWORD_CLASS);
                    }
                }
                catch (RecognitionException pe)
                    {
                    synPredMatched42 = false;
                }
                rewind(_m42);
                inputState.guessing--;
            }
            if (synPredMatched42)
                {
                result = classDeclaration(false);
            }
            else
                {
                boolean synPredMatched45 = false;
                if (((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2)))))
                    {
                    int _m45 = mark();
                    synPredMatched45 = true;
                    inputState.guessing++;
                    try
                        {
                        {
                            {
                                switch (LA(1))
                                    {
                                    case KEYWORD_ABSTRACT :
                                    case KEYWORD_PRIVATE :
                                    case KEYWORD_PROTECTED :
                                    case KEYWORD_PUBLIC :
                                    case KEYWORD_STATIC :
                                    case KEYWORD_STRICTFP :
                                        {
                                            interfaceModifiers(dummy);
                                            break;
                                        }
                                    case KEYWORD_INTERFACE :
                                        {
                                            break;
                                        }
                                    default :
                                        {
                                            throw new NoViableAltException(LT(1), getFilename());
                                        }
                                }
                            }
                            match(KEYWORD_INTERFACE);
                        }
                    }
                    catch (RecognitionException pe)
                        {
                        synPredMatched45 = false;
                    }
                    rewind(_m45);
                    inputState.guessing--;
                }
                if (synPredMatched45)
                    {
                    result = interfaceDeclaration();
                }
                else if ((_tokenSet_13.member(LA(1))) && (_tokenSet_14.member(LA(2))))
                    {
                    result = constantDeclaration();
                }
                else
                    {
                    throw new NoViableAltException(LT(1), getFilename());
                }
            }
        }
        return result;
    }

    public final void classModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;
        Token tok5 = null;
        Token tok6 = null;
        Token tok7 = null;

        {
            int _cnt31 = 0;
            _loop31 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_PROTECTED :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_PROTECTED);
                            if (inputState.guessing == 0)
                                {

                                mods.setProtected();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_PRIVATE :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_PRIVATE);
                            if (inputState.guessing == 0)
                                {

                                mods.setPrivate();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    case KEYWORD_ABSTRACT :
                        {
                            tok4 = LT(1);
                            match(KEYWORD_ABSTRACT);
                            if (inputState.guessing == 0)
                                {

                                mods.setAbstract();
                                setStartPos(mods.getStartPosition(), tok4);
                                setFinishPos(mods.getFinishPosition(), tok4);

                            }
                            break;
                        }
                    case KEYWORD_STATIC :
                        {
                            tok5 = LT(1);
                            match(KEYWORD_STATIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setStatic();
                                setStartPos(mods.getStartPosition(), tok5);
                                setFinishPos(mods.getFinishPosition(), tok5);

                            }
                            break;
                        }
                    case KEYWORD_FINAL :
                        {
                            tok6 = LT(1);
                            match(KEYWORD_FINAL);
                            if (inputState.guessing == 0)
                                {

                                mods.setFinal();
                                setStartPos(mods.getStartPosition(), tok6);
                                setFinishPos(mods.getFinishPosition(), tok6);

                            }
                            break;
                        }
                    case KEYWORD_STRICTFP :
                        {
                            tok7 = LT(1);
                            match(KEYWORD_STRICTFP);
                            if (inputState.guessing == 0)
                                {

                                mods.setStrictfp();
                                setStartPos(mods.getStartPosition(), tok7);
                                setFinishPos(mods.getFinishPosition(), tok7);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt31 >= 1)
                                {
                                break _loop31;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt31++;
            }
            while (true);
        }
    }

    public final void classBody(
        ClassDeclaration classDecl,
        ParsePosition start,
        ParsePosition finish)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;

        Feature decl = null;

        tok1 = LT(1);
        match(SEP_OPENING_BRACE);
        {
            _loop34 : do {
                switch (LA(1))
                    {
                    case KEYWORD_ABSTRACT :
                    case KEYWORD_BOOLEAN :
                    case KEYWORD_BYTE :
                    case KEYWORD_CHAR :
                    case KEYWORD_CLASS :
                    case KEYWORD_DOUBLE :
                    case KEYWORD_FINAL :
                    case KEYWORD_FLOAT :
                    case KEYWORD_INT :
                    case KEYWORD_INTERFACE :
                    case KEYWORD_LONG :
                    case KEYWORD_NATIVE :
                    case KEYWORD_PRIVATE :
                    case KEYWORD_PROTECTED :
                    case KEYWORD_PUBLIC :
                    case KEYWORD_SHORT :
                    case KEYWORD_STATIC :
                    case KEYWORD_STRICTFP :
                    case KEYWORD_SYNCHRONIZED :
                    case KEYWORD_TRANSIENT :
                    case KEYWORD_VOID :
                    case KEYWORD_VOLATILE :
                    case SEP_OPENING_BRACE :
                    case IDENTIFIER :
                        {
                            decl = classBodyDeclaration();
                            if (inputState.guessing == 0)
                                {

                                if (decl instanceof MultipleVariableDeclaration)
                                    {
                                    ((MultipleVariableDeclaration) decl).addTo(classDecl);
                                }
                                else
                                    {
                                    classDecl.addDeclaration(decl);
                                }

                            }
                            break;
                        }
                    case SEP_SEMICOLON :
                        {
                            match(SEP_SEMICOLON);
                            break;
                        }
                    default :
                        {
                            break _loop34;
                        }
                }
            }
            while (true);
        }
        tok2 = LT(1);
        match(SEP_CLOSING_BRACE);
        if (inputState.guessing == 0)
            {

            setStartPos(start, tok1);
            setFinishPos(finish, tok2);

        }
    }

    public final Feature classBodyDeclaration()
        throws RecognitionException, TokenStreamException
    {
        Feature result;

        Modifiers dummy = _factory.createModifiers();

        result = null;

        boolean synPredMatched49 = false;
        if (((LA(1) == KEYWORD_STATIC || LA(1) == SEP_OPENING_BRACE)
            && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART))))
            {
            int _m49 = mark();
            synPredMatched49 = true;
            inputState.guessing++;
            try
                {
                {
                    {
                        switch (LA(1))
                            {
                            case KEYWORD_STATIC :
                                {
                                    match(KEYWORD_STATIC);
                                    break;
                                }
                            case SEP_OPENING_BRACE :
                                {
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    match(SEP_OPENING_BRACE);
                }
            }
            catch (RecognitionException pe)
                {
                synPredMatched49 = false;
            }
            rewind(_m49);
            inputState.guessing--;
        }
        if (synPredMatched49)
            {
            result = initializer();
        }
        else
            {
            boolean synPredMatched52 = false;
            if (((_tokenSet_4.member(LA(1))) && (_tokenSet_5.member(LA(2)))))
                {
                int _m52 = mark();
                synPredMatched52 = true;
                inputState.guessing++;
                try
                    {
                    {
                        {
                            switch (LA(1))
                                {
                                case KEYWORD_ABSTRACT :
                                case KEYWORD_FINAL :
                                case KEYWORD_PRIVATE :
                                case KEYWORD_PROTECTED :
                                case KEYWORD_PUBLIC :
                                case KEYWORD_STATIC :
                                case KEYWORD_STRICTFP :
                                    {
                                        classModifiers(dummy);
                                        break;
                                    }
                                case KEYWORD_CLASS :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        match(KEYWORD_CLASS);
                    }
                }
                catch (RecognitionException pe)
                    {
                    synPredMatched52 = false;
                }
                rewind(_m52);
                inputState.guessing--;
            }
            if (synPredMatched52)
                {
                result = classDeclaration(false);
            }
            else
                {
                boolean synPredMatched55 = false;
                if (((_tokenSet_9.member(LA(1))) && (_tokenSet_10.member(LA(2)))))
                    {
                    int _m55 = mark();
                    synPredMatched55 = true;
                    inputState.guessing++;
                    try
                        {
                        {
                            {
                                switch (LA(1))
                                    {
                                    case KEYWORD_ABSTRACT :
                                    case KEYWORD_PRIVATE :
                                    case KEYWORD_PROTECTED :
                                    case KEYWORD_PUBLIC :
                                    case KEYWORD_STATIC :
                                    case KEYWORD_STRICTFP :
                                        {
                                            interfaceModifiers(dummy);
                                            break;
                                        }
                                    case KEYWORD_INTERFACE :
                                        {
                                            break;
                                        }
                                    default :
                                        {
                                            throw new NoViableAltException(LT(1), getFilename());
                                        }
                                }
                            }
                            match(KEYWORD_INTERFACE);
                        }
                    }
                    catch (RecognitionException pe)
                        {
                        synPredMatched55 = false;
                    }
                    rewind(_m55);
                    inputState.guessing--;
                }
                if (synPredMatched55)
                    {
                    result = interfaceDeclaration();
                }
                else
                    {
                    boolean synPredMatched58 = false;
                    if (((_tokenSet_15.member(LA(1))) && (_tokenSet_16.member(LA(2)))))
                        {
                        int _m58 = mark();
                        synPredMatched58 = true;
                        inputState.guessing++;
                        try
                            {
                            {
                                {
                                    switch (LA(1))
                                        {
                                        case KEYWORD_PRIVATE :
                                        case KEYWORD_PROTECTED :
                                        case KEYWORD_PUBLIC :
                                            {
                                                constructorModifiers(dummy);
                                                break;
                                            }
                                        case IDENTIFIER :
                                            {
                                                break;
                                            }
                                        default :
                                            {
                                                throw new NoViableAltException(LT(1), getFilename());
                                            }
                                    }
                                }
                                identifier(null);
                                match(SEP_OPENING_PARENTHESIS);
                            }
                        }
                        catch (RecognitionException pe)
                            {
                            synPredMatched58 = false;
                        }
                        rewind(_m58);
                        inputState.guessing--;
                    }
                    if (synPredMatched58)
                        {
                        result = constructorDeclaration();
                    }
                    else
                        {
                        boolean synPredMatched62 = false;
                        if (((_tokenSet_17.member(LA(1))) && (_tokenSet_18.member(LA(2)))))
                            {
                            int _m62 = mark();
                            synPredMatched62 = true;
                            inputState.guessing++;
                            try
                                {
                                {
                                    {
                                        switch (LA(1))
                                            {
                                            case KEYWORD_ABSTRACT :
                                            case KEYWORD_FINAL :
                                            case KEYWORD_NATIVE :
                                            case KEYWORD_PRIVATE :
                                            case KEYWORD_PROTECTED :
                                            case KEYWORD_PUBLIC :
                                            case KEYWORD_STATIC :
                                            case KEYWORD_STRICTFP :
                                            case KEYWORD_SYNCHRONIZED :
                                                {
                                                    methodModifiers(dummy);
                                                    break;
                                                }
                                            case KEYWORD_BOOLEAN :
                                            case KEYWORD_BYTE :
                                            case KEYWORD_CHAR :
                                            case KEYWORD_DOUBLE :
                                            case KEYWORD_FLOAT :
                                            case KEYWORD_INT :
                                            case KEYWORD_LONG :
                                            case KEYWORD_SHORT :
                                            case KEYWORD_VOID :
                                            case IDENTIFIER :
                                                {
                                                    break;
                                                }
                                            default :
                                                {
                                                    throw new NoViableAltException(LT(1), getFilename());
                                                }
                                        }
                                    }
                                    {
                                        switch (LA(1))
                                            {
                                            case KEYWORD_VOID :
                                                {
                                                    match(KEYWORD_VOID);
                                                    break;
                                                }
                                            case KEYWORD_BOOLEAN :
                                            case KEYWORD_BYTE :
                                            case KEYWORD_CHAR :
                                            case KEYWORD_DOUBLE :
                                            case KEYWORD_FLOAT :
                                            case KEYWORD_INT :
                                            case KEYWORD_LONG :
                                            case KEYWORD_SHORT :
                                            case IDENTIFIER :
                                                {
                                                    type();
                                                    break;
                                                }
                                            default :
                                                {
                                                    throw new NoViableAltException(LT(1), getFilename());
                                                }
                                        }
                                    }
                                    identifier(null);
                                    match(SEP_OPENING_PARENTHESIS);
                                }
                            }
                            catch (RecognitionException pe)
                                {
                                synPredMatched62 = false;
                            }
                            rewind(_m62);
                            inputState.guessing--;
                        }
                        if (synPredMatched62)
                            {
                            result = methodDeclaration();
                        }
                        else if ((_tokenSet_19.member(LA(1))) && (_tokenSet_20.member(LA(2))))
                            {
                            result = fieldDeclaration();
                        }
                        else
                            {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }
                }
            }
        }
        return result;
    }

    public final void abstractMethodModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;

        {
            int _cnt96 = 0;
            _loop96 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_ABSTRACT :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_ABSTRACT);
                            if (inputState.guessing == 0)
                                {

                                mods.setAbstract();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt96 >= 1)
                                {
                                break _loop96;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt96++;
            }
            while (true);
        }
    }

    public final MethodDeclaration abstractMethodDeclaration()
        throws RecognitionException, TokenStreamException
    {
        MethodDeclaration result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;

        ParsePosition start = new ParsePosition();
        Modifiers mods = _factory.createModifiers();
        Type returnType = null;
        Type exType = null;
        String name = null;
        FormalParameterList params = null;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_ABSTRACT :
                case KEYWORD_PUBLIC :
                    {
                        abstractMethodModifiers(mods);
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case KEYWORD_VOID :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        returnType = type();
                        if (inputState.guessing == 0)
                            {

                            start.copyFrom(returnType.getStartPosition());

                        }
                        break;
                    }
                case KEYWORD_VOID :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_VOID);
                        if (inputState.guessing == 0)
                            {

                            setStartPos(start, tok1);

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        name = identifier(null);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FINAL :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        params = formalParameterList(false);
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        match(SEP_CLOSING_PARENTHESIS);
        {
            _loop92 : do {
                if ((LA(1) == SEP_OPENING_BRACKET))
                    {
                    tok2 = LT(1);
                    match(SEP_OPENING_BRACKET);
                    match(SEP_CLOSING_BRACKET);
                    if (inputState.guessing == 0)
                        {

                        if (returnType == null)
                            {
                            throw generateException(
                                "Brackets are illegal here because method has no return type",
                                tok2.getLine(),
                                tok2.getColumn());
                        }
                        returnType.incDimensions();

                    }
                }
                else
                    {
                    break _loop92;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result = _factory.createMethodDeclaration(mods, returnType, name, params);
            result.setStartPosition(mods.getStartPosition());
            result.getStartPosition().setIfFirstTime(start);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_THROWS :
                    {
                        throwsClause(result);
                        break;
                    }
                case SEP_SEMICOLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok3 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            setFinishPos(result.getFinishPosition(), tok3);

        }
        return result;
    }

    public final MultipleVariableDeclaration constantDeclaration()
        throws RecognitionException, TokenStreamException
    {
        MultipleVariableDeclaration result;

        Modifiers mods = _factory.createModifiers();

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_FINAL :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                    {
                        constantModifiers(mods);
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        result = attributeDeclaration(mods);
        return result;
    }

    public final Initializer initializer()
        throws RecognitionException, TokenStreamException
    {
        Initializer result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition bodyStart = new ParsePosition();
        ParsePosition bodyFinish = new ParsePosition();
        Block body = null;
        boolean isStatic = false;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_STATIC :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_STATIC);
                        if (inputState.guessing == 0)
                            {

                            isStatic = true;
                            setStartPos(start, tok1);

                        }
                        break;
                    }
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            if (((LA(1) == SEP_OPENING_BRACE) && (_tokenSet_21.member(LA(2))))
                && (!_helper.isParsingInterfaceOnly()))
                {
                body = block();
                if (inputState.guessing == 0)
                    {

                    result = _factory.createInitializer(body, isStatic);
                    start.setIfFirstTime(body.getStartPosition());
                    result.setStartPosition(start);
                    result.setFinishPosition(body.getFinishPosition());

                }
            }
            else if (
                (LA(1) == SEP_OPENING_BRACE)
                    && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                {
                skipBody(bodyStart, bodyFinish);
                if (inputState.guessing == 0)
                    {

                    result = _factory.createInitializer(null, isStatic);
                    result.setProperty(PROPERTY_BODY_START, bodyStart);
                    start.setIfFirstTime(bodyStart);
                    result.setStartPosition(start);
                    result.setFinishPosition(bodyFinish);

                }
            }
            else
                {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        return result;
    }

    public final void constructorModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;

        {
            int _cnt86 = 0;
            _loop86 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_PROTECTED :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_PROTECTED);
                            if (inputState.guessing == 0)
                                {

                                mods.setProtected();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_PRIVATE :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_PRIVATE);
                            if (inputState.guessing == 0)
                                {

                                mods.setPrivate();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt86 >= 1)
                                {
                                break _loop86;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt86++;
            }
            while (true);
        }
    }

    public final ConstructorDeclaration constructorDeclaration()
        throws RecognitionException, TokenStreamException
    {
        ConstructorDeclaration result;

        ParsePosition start = new ParsePosition();
        ParsePosition bodyStart = new ParsePosition();
        ParsePosition bodyFinish = new ParsePosition();
        Block body = null;
        Modifiers mods = _factory.createModifiers();
        Type exType = null;
        String name = null;
        FormalParameterList params = null;

        result = null;

        if (inputState.guessing == 0)
            {

            _helper.pushBlockScope();

        }
        {
            switch (LA(1))
                {
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                    {
                        constructorModifiers(mods);
                        break;
                    }
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        name = identifier(start);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FINAL :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        params = formalParameterList(true);
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createConstructorDeclaration(mods, name, params);
            result.setStartPosition(mods.getStartPosition());
            result.getStartPosition().setIfFirstTime(start);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_THROWS :
                    {
                        throwsClause(result);
                        break;
                    }
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            if (((LA(1) == SEP_OPENING_BRACE) && (_tokenSet_21.member(LA(2))))
                && (!_helper.isParsingInterfaceOnly()))
                {
                body = block();
                if (inputState.guessing == 0)
                    {

                    result.setBody(body);
                    result.setFinishPosition(body.getFinishPosition());

                }
            }
            else if (
                (LA(1) == SEP_OPENING_BRACE)
                    && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                {
                skipBody(bodyStart, bodyFinish);
                if (inputState.guessing == 0)
                    {

                    result.setBody(null);
                    result.setProperty(PROPERTY_BODY_START, bodyStart);
                    result.setFinishPosition(bodyFinish);

                }
            }
            else
                {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        if (inputState.guessing == 0)
            {

            _helper.popBlockScope();

        }
        return result;
    }

    public final void methodModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;
        Token tok5 = null;
        Token tok6 = null;
        Token tok7 = null;
        Token tok8 = null;
        Token tok9 = null;

        {
            int _cnt108 = 0;
            _loop108 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_PROTECTED :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_PROTECTED);
                            if (inputState.guessing == 0)
                                {

                                mods.setProtected();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_PRIVATE :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_PRIVATE);
                            if (inputState.guessing == 0)
                                {

                                mods.setPrivate();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    case KEYWORD_ABSTRACT :
                        {
                            tok4 = LT(1);
                            match(KEYWORD_ABSTRACT);
                            if (inputState.guessing == 0)
                                {

                                mods.setAbstract();
                                setStartPos(mods.getStartPosition(), tok4);
                                setFinishPos(mods.getFinishPosition(), tok4);

                            }
                            break;
                        }
                    case KEYWORD_STATIC :
                        {
                            tok5 = LT(1);
                            match(KEYWORD_STATIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setStatic();
                                setStartPos(mods.getStartPosition(), tok5);
                                setFinishPos(mods.getFinishPosition(), tok5);

                            }
                            break;
                        }
                    case KEYWORD_FINAL :
                        {
                            tok6 = LT(1);
                            match(KEYWORD_FINAL);
                            if (inputState.guessing == 0)
                                {

                                mods.setFinal();
                                setStartPos(mods.getStartPosition(), tok6);
                                setFinishPos(mods.getFinishPosition(), tok6);

                            }
                            break;
                        }
                    case KEYWORD_SYNCHRONIZED :
                        {
                            tok7 = LT(1);
                            match(KEYWORD_SYNCHRONIZED);
                            if (inputState.guessing == 0)
                                {

                                mods.setSynchronized();
                                setStartPos(mods.getStartPosition(), tok7);
                                setFinishPos(mods.getFinishPosition(), tok7);

                            }
                            break;
                        }
                    case KEYWORD_NATIVE :
                        {
                            tok8 = LT(1);
                            match(KEYWORD_NATIVE);
                            if (inputState.guessing == 0)
                                {

                                mods.setNative();
                                setStartPos(mods.getStartPosition(), tok8);
                                setFinishPos(mods.getFinishPosition(), tok8);

                            }
                            break;
                        }
                    case KEYWORD_STRICTFP :
                        {
                            tok9 = LT(1);
                            match(KEYWORD_STRICTFP);
                            if (inputState.guessing == 0)
                                {

                                mods.setStrictfp();
                                setStartPos(mods.getStartPosition(), tok9);
                                setFinishPos(mods.getFinishPosition(), tok9);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt108 >= 1)
                                {
                                break _loop108;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt108++;
            }
            while (true);
        }
    }

    public final MethodDeclaration methodDeclaration()
        throws RecognitionException, TokenStreamException
    {
        MethodDeclaration result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition bodyStart = new ParsePosition();
        ParsePosition bodyFinish = new ParsePosition();
        Modifiers mods = _factory.createModifiers();
        Type returnType = null;
        String name = null;
        FormalParameterList params = null;
        Type exType = null;
        Block body = null;

        result = null;

        if (inputState.guessing == 0)
            {

            _helper.pushBlockScope();

        }
        {
            switch (LA(1))
                {
                case KEYWORD_ABSTRACT :
                case KEYWORD_FINAL :
                case KEYWORD_NATIVE :
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                case KEYWORD_STRICTFP :
                case KEYWORD_SYNCHRONIZED :
                    {
                        methodModifiers(mods);
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case KEYWORD_VOID :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        returnType = type();
                        if (inputState.guessing == 0)
                            {

                            start.copyFrom(returnType.getStartPosition());

                        }
                        break;
                    }
                case KEYWORD_VOID :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_VOID);
                        if (inputState.guessing == 0)
                            {

                            setStartPos(start, tok1);

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        name = identifier(null);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FINAL :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        params = formalParameterList(true);
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        match(SEP_CLOSING_PARENTHESIS);
        {
            _loop102 : do {
                if ((LA(1) == SEP_OPENING_BRACKET))
                    {
                    tok2 = LT(1);
                    match(SEP_OPENING_BRACKET);
                    match(SEP_CLOSING_BRACKET);
                    if (inputState.guessing == 0)
                        {

                        if (returnType == null)
                            {
                            throw generateException(
                                "Brackets are illegal here because method has no return type",
                                tok2.getLine(),
                                tok2.getColumn());
                        }
                        returnType.incDimensions();

                    }
                }
                else
                    {
                    break _loop102;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result = _factory.createMethodDeclaration(mods, returnType, name, params);
            result.setStartPosition(mods.getStartPosition());
            result.getStartPosition().setIfFirstTime(start);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_THROWS :
                    {
                        throwsClause(result);
                        break;
                    }
                case SEP_SEMICOLON :
                case SEP_OPENING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            switch (LA(1))
                {
                case SEP_SEMICOLON :
                    {
                        tok3 = LT(1);
                        match(SEP_SEMICOLON);
                        if (inputState.guessing == 0)
                            {

                            setFinishPos(result.getFinishPosition(), tok3);

                        }
                        break;
                    }
                case SEP_OPENING_BRACE :
                    {
                        {
                            if (((LA(1) == SEP_OPENING_BRACE) && (_tokenSet_21.member(LA(2))))
                                && (!_helper.isParsingInterfaceOnly()))
                                {
                                body = block();
                                if (inputState.guessing == 0)
                                    {

                                    result.setBody(body);
                                    result.setFinishPosition(body.getFinishPosition());

                                }
                            }
                            else if (
                                (LA(1) == SEP_OPENING_BRACE)
                                    && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                                {
                                skipBody(bodyStart, bodyFinish);
                                if (inputState.guessing == 0)
                                    {

                                    result.setBody(null);
                                    result.setProperty(PROPERTY_BODY_START, bodyStart);
                                    result.setFinishPosition(bodyFinish);

                                }
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            _helper.popBlockScope();

        }
        return result;
    }

    public final MultipleVariableDeclaration fieldDeclaration()
        throws RecognitionException, TokenStreamException
    {
        MultipleVariableDeclaration result;

        Modifiers mods = _factory.createModifiers();

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_FINAL :
                case KEYWORD_PRIVATE :
                case KEYWORD_PROTECTED :
                case KEYWORD_PUBLIC :
                case KEYWORD_STATIC :
                case KEYWORD_TRANSIENT :
                case KEYWORD_VOLATILE :
                    {
                        fieldModifiers(mods);
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        result = attributeDeclaration(mods);
        return result;
    }

    public final Block block() throws RecognitionException, TokenStreamException
    {
        Block result;

        Token tok1 = null;
        Token tok2 = null;

        BlockStatement inner = null;

        result = null;

        tok1 = LT(1);
        match(SEP_OPENING_BRACE);
        if (inputState.guessing == 0)
            {

            result = _factory.createBlock();
            setStartPos(result.getStartPosition(), tok1);
            _helper.pushBlockScope();

        }
        {
            _loop123 : do {
                if ((_tokenSet_22.member(LA(1))))
                    {
                    inner = blockStatement();
                    if (inputState.guessing == 0)
                        {

                        if (inner instanceof MultipleVariableDeclaration)
                            {
                            ((MultipleVariableDeclaration) inner).addTo(result);
                        }
                        else
                            {
                            result.getBlockStatements().add(inner);
                        }

                    }
                }
                else
                    {
                    break _loop123;
                }

            }
            while (true);
        }
        tok2 = LT(1);
        match(SEP_CLOSING_BRACE);
        if (inputState.guessing == 0)
            {

            setFinishPos(result.getFinishPosition(), tok2);
            _helper.popBlockScope();

        }
        return result;
    }

    public final void skipBody(ParsePosition start, ParsePosition finish)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;

        int level = 0;

        tok1 = LT(1);
        match(SEP_OPENING_BRACE);
        if (inputState.guessing == 0)
            {

            level++;
            setStartPos(start, tok1);

        }
        {
            _loop120 : do {
                if ((LA(1) == SEP_OPENING_BRACE)
                    && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                    {
                    match(SEP_OPENING_BRACE);
                    if (inputState.guessing == 0)
                        {

                        level++;

                    }
                }
                else if (
                    (LA(1) == SEP_CLOSING_BRACE)
                        && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                    {
                    tok2 = LT(1);
                    match(SEP_CLOSING_BRACE);
                    if (inputState.guessing == 0)
                        {

                        level--;
                        if (level == 0)
                            {
                            setFinishPos(finish, tok2);
                            _helper.removeComments(start, finish);
                            return;
                        }

                    }
                }
                else if (
                    (_tokenSet_23.member(LA(1)))
                        && ((LA(2) >= BLOCK_COMMENT && LA(2) <= IDENTIFIER_PART)))
                    {
                    {
                        match(_tokenSet_23);
                    }
                }
                else
                    {
                    break _loop120;
                }

            }
            while (true);
        }
    }

    public final MultipleVariableDeclaration attributeDeclaration(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {
        MultipleVariableDeclaration result;

        Token tok1 = null;

        Type attrType = null;
        VariableDeclaration decl = null;

        result = null;

        attrType = type();
        if (inputState.guessing == 0)
            {

            result = new MultipleVariableDeclaration();
            result.setStartPosition(mods.getStartPosition());
            result.getStartPosition().setIfFirstTime(attrType.getStartPosition());

        }
        decl = variableDeclarator(mods, attrType, true);
        if (inputState.guessing == 0)
            {

            result.getDeclarators().add(decl);

        }
        {
            _loop68 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    decl = variableDeclarator(mods, attrType, true);
                    if (inputState.guessing == 0)
                        {

                        result.getDeclarators().add(decl);

                    }
                }
                else
                    {
                    break _loop68;
                }

            }
            while (true);
        }
        tok1 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final void constantModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;

        {
            int _cnt73 = 0;
            _loop73 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_STATIC :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_STATIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setStatic();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_FINAL :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_FINAL);
                            if (inputState.guessing == 0)
                                {

                                mods.setFinal();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt73 >= 1)
                                {
                                break _loop73;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt73++;
            }
            while (true);
        }
    }

    public final void fieldModifiers(Modifiers mods)
        throws RecognitionException, TokenStreamException
    {

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;
        Token tok5 = null;
        Token tok6 = null;
        Token tok7 = null;

        {
            int _cnt78 = 0;
            _loop78 : do {
                switch (LA(1))
                    {
                    case KEYWORD_PUBLIC :
                        {
                            tok1 = LT(1);
                            match(KEYWORD_PUBLIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setPublic();
                                setStartPos(mods.getStartPosition(), tok1);
                                setFinishPos(mods.getFinishPosition(), tok1);

                            }
                            break;
                        }
                    case KEYWORD_PROTECTED :
                        {
                            tok2 = LT(1);
                            match(KEYWORD_PROTECTED);
                            if (inputState.guessing == 0)
                                {

                                mods.setProtected();
                                setStartPos(mods.getStartPosition(), tok2);
                                setFinishPos(mods.getFinishPosition(), tok2);

                            }
                            break;
                        }
                    case KEYWORD_PRIVATE :
                        {
                            tok3 = LT(1);
                            match(KEYWORD_PRIVATE);
                            if (inputState.guessing == 0)
                                {

                                mods.setPrivate();
                                setStartPos(mods.getStartPosition(), tok3);
                                setFinishPos(mods.getFinishPosition(), tok3);

                            }
                            break;
                        }
                    case KEYWORD_STATIC :
                        {
                            tok4 = LT(1);
                            match(KEYWORD_STATIC);
                            if (inputState.guessing == 0)
                                {

                                mods.setStatic();
                                setStartPos(mods.getStartPosition(), tok4);
                                setFinishPos(mods.getFinishPosition(), tok4);

                            }
                            break;
                        }
                    case KEYWORD_FINAL :
                        {
                            tok5 = LT(1);
                            match(KEYWORD_FINAL);
                            if (inputState.guessing == 0)
                                {

                                mods.setFinal();
                                setStartPos(mods.getStartPosition(), tok5);
                                setFinishPos(mods.getFinishPosition(), tok5);

                            }
                            break;
                        }
                    case KEYWORD_TRANSIENT :
                        {
                            tok6 = LT(1);
                            match(KEYWORD_TRANSIENT);
                            if (inputState.guessing == 0)
                                {

                                mods.setTransient();
                                setStartPos(mods.getStartPosition(), tok6);
                                setFinishPos(mods.getFinishPosition(), tok6);

                            }
                            break;
                        }
                    case KEYWORD_VOLATILE :
                        {
                            tok7 = LT(1);
                            match(KEYWORD_VOLATILE);
                            if (inputState.guessing == 0)
                                {

                                mods.setVolatile();
                                setStartPos(mods.getStartPosition(), tok7);
                                setFinishPos(mods.getFinishPosition(), tok7);

                            }
                            break;
                        }
                    default :
                        {
                            if (_cnt78 >= 1)
                                {
                                break _loop78;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                }
                _cnt78++;
            }
            while (true);
        }
    }

    public final FormalParameterList formalParameterList(boolean defineVars)
        throws RecognitionException, TokenStreamException
    {
        FormalParameterList result;

        FormalParameter param = null;

        result = _factory.createFormalParameterList();

        param = formalParameter(defineVars);
        if (inputState.guessing == 0)
            {

            result.getParameters().add(param);
            result.setStartPosition(param.getStartPosition());
            result.setFinishPosition(param.getFinishPosition());

        }
        {
            _loop114 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    param = formalParameter(defineVars);
                    if (inputState.guessing == 0)
                        {

                        result.getParameters().add(param);
                        result.setFinishPosition(param.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop114;
                }

            }
            while (true);
        }
        return result;
    }

    public final void throwsClause(InvocableDeclaration invocable)
        throws RecognitionException, TokenStreamException
    {

        Type exType = null;

        match(KEYWORD_THROWS);
        exType = type();
        if (inputState.guessing == 0)
            {

            invocable.getThrownExceptions().add(exType);

        }
        {
            _loop111 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    exType = type();
                    if (inputState.guessing == 0)
                        {

                        invocable.getThrownExceptions().add(exType);

                    }
                }
                else
                    {
                    break _loop111;
                }

            }
            while (true);
        }
    }

    public final FormalParameter formalParameter(boolean defineVars)
        throws RecognitionException, TokenStreamException
    {
        FormalParameter result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        VariableDeclaratorId decl = null;
        Type varType = null;
        boolean isFinal = false;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_FINAL :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_FINAL);
                        if (inputState.guessing == 0)
                            {

                            isFinal = true;
                            setStartPos(start, tok1);

                        }
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        varType = type();
        decl = variableDeclaratorId();
        if (inputState.guessing == 0)
            {

            result =
                _factory.createFormalParameter(
                    isFinal,
                    fixType(varType, decl.getDimensions()),
                    decl.getName());
            start.setIfFirstTime(varType.getStartPosition());
            result.setStartPosition(start);
            result.setFinishPosition(decl.getFinishPosition());
            if (defineVars)
                {
                _helper.defineVariable(result);
            }

        }
        return result;
    }

    public final VariableDeclaratorId variableDeclaratorId()
        throws RecognitionException, TokenStreamException
    {
        VariableDeclaratorId result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition finish = new ParsePosition();
        String id = null;
        int dim = 0;

        result = null;

        id = identifier(start);
        if (inputState.guessing == 0)
            {

            finish.set(
                start.getLine(),
                start.getColumn() + id.length() - 1,
                start.getAbsolute() + id.length() - 1);

        }
        {
            _loop133 : do {
                if ((LA(1) == SEP_OPENING_BRACKET))
                    {
                    match(SEP_OPENING_BRACKET);
                    tok1 = LT(1);
                    match(SEP_CLOSING_BRACKET);
                    if (inputState.guessing == 0)
                        {

                        dim++;
                        setFinishPos(finish, tok1);

                    }
                }
                else
                    {
                    break _loop133;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result = new VariableDeclaratorId(id, dim);
            result.setStartPosition(start);
            result.setFinishPosition(finish);

        }
        return result;
    }

    public final MultipleVariableDeclaration localVariableDeclaration()
        throws RecognitionException, TokenStreamException
    {
        MultipleVariableDeclaration result;

        Token tok1 = null;

        Modifiers mods = _factory.createModifiers();
        ParsePosition start = new ParsePosition();
        Type varType = null;
        boolean isFinal = false;
        VariableDeclaration decl = null;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_FINAL :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_FINAL);
                        if (inputState.guessing == 0)
                            {

                            mods.setFinal();
                            setStartPos(start, tok1);

                        }
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                case IDENTIFIER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        varType = type();
        if (inputState.guessing == 0)
            {

            result = new MultipleVariableDeclaration();
            start.setIfFirstTime(varType.getStartPosition());
            result.setStartPosition(start);

        }
        decl = variableDeclarator(mods, varType, false);
        if (inputState.guessing == 0)
            {

            result.getDeclarators().add(decl);
            result.setFinishPosition(decl.getFinishPosition());

        }
        {
            _loop130 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    decl = variableDeclarator(mods, varType, false);
                    if (inputState.guessing == 0)
                        {

                        result.getDeclarators().add(decl);
                        result.setFinishPosition(decl.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop130;
                }

            }
            while (true);
        }
        return result;
    }

    public final Statement statement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        switch (LA(1))
            {
            case SEP_SEMICOLON :
                {
                    result = emptyStatement();
                    break;
                }
            case SEP_OPENING_BRACE :
                {
                    result = block();
                    break;
                }
            case KEYWORD_RETURN :
                {
                    result = returnStatement();
                    break;
                }
            case KEYWORD_BREAK :
                {
                    result = breakStatement();
                    break;
                }
            case KEYWORD_CONTINUE :
                {
                    result = continueStatement();
                    break;
                }
            case KEYWORD_THROW :
                {
                    result = throwStatement();
                    break;
                }
            case KEYWORD_SYNCHRONIZED :
                {
                    result = synchronizedStatement();
                    break;
                }
            case KEYWORD_IF :
                {
                    result = ifThenElseStatement();
                    break;
                }
            case KEYWORD_WHILE :
                {
                    result = whileStatement();
                    break;
                }
            case KEYWORD_DO :
                {
                    result = doWhileStatement();
                    break;
                }
            case KEYWORD_FOR :
                {
                    result = forStatement();
                    break;
                }
            case KEYWORD_TRY :
                {
                    result = tryStatement();
                    break;
                }
            case KEYWORD_SWITCH :
                {
                    result = switchStatement();
                    break;
                }
            default :
                if ((LA(1) == IDENTIFIER) && (LA(2) == OP_COLON))
                    {
                    result = labeledStatement();
                }
                else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_25.member(LA(2))))
                    {
                    result = expressionStatement();
                }
                else
                    {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return result;
    }

    public final VariableInitializer variableInitializer()
        throws RecognitionException, TokenStreamException
    {
        VariableInitializer result;

        Expression expr = null;

        result = null;

        switch (LA(1))
            {
            case FLOATING_POINT_LITERAL :
            case INTEGER_LITERAL :
            case OP_DECREMENT :
            case OP_INCREMENT :
            case FALSE_LITERAL :
            case KEYWORD_BOOLEAN :
            case KEYWORD_BYTE :
            case KEYWORD_CHAR :
            case KEYWORD_DOUBLE :
            case KEYWORD_FLOAT :
            case KEYWORD_INT :
            case KEYWORD_LONG :
            case KEYWORD_NEW :
            case KEYWORD_SHORT :
            case KEYWORD_SUPER :
            case KEYWORD_THIS :
            case KEYWORD_VOID :
            case NULL_LITERAL :
            case TRUE_LITERAL :
            case SEP_OPENING_PARENTHESIS :
            case OP_PLUS :
            case OP_MINUS :
            case OP_BITWISE_COMPLEMENT :
            case OP_NOT :
            case CHARACTER_LITERAL :
            case STRING_LITERAL :
            case IDENTIFIER :
                {
                    expr = expression();
                    if (inputState.guessing == 0)
                        {

                        result = _factory.createSingleInitializer(expr);
                        result.setStartPosition(expr.getStartPosition());
                        result.setFinishPosition(expr.getFinishPosition());

                    }
                    break;
                }
            case SEP_OPENING_BRACE :
                {
                    result = arrayInitializer();
                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return result;
    }

    public final Statement emptyStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        result = null;

        tok1 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            result = _factory.createEmptyStatement();
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final Statement labeledStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        ParsePosition start = new ParsePosition();
        LabeledStatement lblStmt = null;
        String label = null;

        result = null;

        label = identifier(start);
        match(OP_COLON);
        if (inputState.guessing == 0)
            {

            lblStmt = _factory.createLabeledStatement(label);
            lblStmt.setStartPosition(start);
            _helper.defineLabel(lblStmt);

        }
        result = statement();
        if (inputState.guessing == 0)
            {

            _helper.deleteLabel(label);
            lblStmt.setStatement(result);
            lblStmt.setFinishPosition(result.getFinishPosition());
            result = lblStmt;

        }
        return result;
    }

    public final Statement expressionStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        Expression expr = null;

        result = null;

        expr = expression();
        tok1 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            result = _factory.createExpressionStatement(expr);
            result.setStartPosition(expr.getStartPosition());
            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final Statement returnStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;
        Token tok2 = null;

        Expression expr = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_RETURN);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        expr = expression();
                        break;
                    }
                case SEP_SEMICOLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            result = _factory.createReturnStatement(expr);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Statement breakStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition start = new ParsePosition();
        LabeledStatement target = null;
        String label = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_BREAK);
        {
            switch (LA(1))
                {
                case IDENTIFIER :
                    {
                        label = identifier(start);
                        break;
                    }
                case SEP_SEMICOLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            if (label != null)
                {
                target = _helper.resolveLabel(label);
                if (target == null)
                    {
                    throw generateException(
                        "No label with the name " + label + " defined",
                        start.getLine(),
                        start.getColumn());
                }
            }
            result = _factory.createBreakStatement(target);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Statement continueStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition start = new ParsePosition();
        LabeledStatement target = null;
        String label = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_CONTINUE);
        {
            switch (LA(1))
                {
                case IDENTIFIER :
                    {
                        label = identifier(start);
                        break;
                    }
                case SEP_SEMICOLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            if (label != null)
                {
                target = _helper.resolveLabel(label);
                if (target == null)
                    {
                    throw generateException(
                        "No label with the name " + label + " defined",
                        start.getLine(),
                        start.getColumn());
                }
            }
            result = _factory.createContinueStatement(target);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Statement throwStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;
        Token tok2 = null;

        Expression expr = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_THROW);
        expr = expression();
        tok2 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            result = _factory.createThrowStatement(expr);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Statement synchronizedStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        Expression expr = null;
        Block inner = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_SYNCHRONIZED);
        match(SEP_OPENING_PARENTHESIS);
        expr = expression();
        match(SEP_CLOSING_PARENTHESIS);
        inner = block();
        if (inputState.guessing == 0)
            {

            result = _factory.createSynchronizedStatement(expr, inner);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(inner.getFinishPosition());

        }
        return result;
    }

    public final Statement ifThenElseStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        Expression expr = null;
        Statement trueStmt = null;
        Statement falseStmt = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_IF);
        match(SEP_OPENING_PARENTHESIS);
        expr = expression();
        match(SEP_CLOSING_PARENTHESIS);
        trueStmt = statement();
        {
            if ((LA(1) == KEYWORD_ELSE) && (_tokenSet_2.member(LA(2))))
                {
                match(KEYWORD_ELSE);
                falseStmt = statement();
            }
            else if ((_tokenSet_26.member(LA(1))) && (_tokenSet_27.member(LA(2))))
                {
            }
            else
                {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        if (inputState.guessing == 0)
            {

            result = _factory.createIfThenElseStatement(expr, trueStmt, falseStmt);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(
                falseStmt != null
                    ? falseStmt.getFinishPosition()
                    : trueStmt.getFinishPosition());

        }
        return result;
    }

    public final Statement whileStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        Expression expr = null;
        Statement stmt = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_WHILE);
        match(SEP_OPENING_PARENTHESIS);
        expr = expression();
        match(SEP_CLOSING_PARENTHESIS);
        stmt = statement();
        if (inputState.guessing == 0)
            {

            result = _factory.createWhileStatement(expr, stmt);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(stmt.getFinishPosition());

        }
        return result;
    }

    public final Statement doWhileStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;
        Token tok2 = null;

        Expression expr = null;
        Statement stmt = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_DO);
        stmt = statement();
        match(KEYWORD_WHILE);
        match(SEP_OPENING_PARENTHESIS);
        expr = expression();
        match(SEP_CLOSING_PARENTHESIS);
        tok2 = LT(1);
        match(SEP_SEMICOLON);
        if (inputState.guessing == 0)
            {

            result = _factory.createDoWhileStatement(expr, stmt);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Statement forStatement()
        throws RecognitionException, TokenStreamException
    {
        Statement result;

        Token tok1 = null;

        StatementExpressionList forInitList = null;
        MultipleVariableDeclaration forInitDecl = null;
        StatementExpressionList forUpdate = null;
        Expression expr = null;
        Statement stmt = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_FOR);
        match(SEP_OPENING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            _helper.pushBlockScope();

        }
        {
            boolean synPredMatched155 = false;
            if (((_tokenSet_0.member(LA(1))) && (_tokenSet_1.member(LA(2)))))
                {
                int _m155 = mark();
                synPredMatched155 = true;
                inputState.guessing++;
                try
                    {
                    {
                        localVariableDeclaration();
                    }
                }
                catch (RecognitionException pe)
                    {
                    synPredMatched155 = false;
                }
                rewind(_m155);
                inputState.guessing--;
            }
            if (synPredMatched155)
                {
                forInitDecl = localVariableDeclaration();
            }
            else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_28.member(LA(2))))
                {
                forInitList = statementExpressionList();
            }
            else if ((LA(1) == SEP_SEMICOLON))
                {
            }
            else
                {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        match(SEP_SEMICOLON);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        expr = expression();
                        break;
                    }
                case SEP_SEMICOLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        match(SEP_SEMICOLON);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        forUpdate = statementExpressionList();
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        match(SEP_CLOSING_PARENTHESIS);
        stmt = statement();
        if (inputState.guessing == 0)
            {

            if (forInitDecl != null)
                {
                result = _factory.createForStatement(expr, forUpdate, stmt);
                forInitDecl.addTo((ForStatement) result);
            }
            else
                {
                result = _factory.createForStatement(forInitList, expr, forUpdate, stmt);
            }
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(stmt.getFinishPosition());
            _helper.popBlockScope();

        }
        return result;
    }

    public final TryStatement tryStatement()
        throws RecognitionException, TokenStreamException
    {
        TryStatement result;

        Token tok1 = null;

        Block blck = null;
        CatchClause ctch = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_TRY);
        blck = block();
        if (inputState.guessing == 0)
            {

            result = _factory.createTryStatement(blck);
            setStartPos(result.getStartPosition(), tok1);

        }
        {
            switch (LA(1))
                {
                case KEYWORD_FINALLY :
                    {
                        blck = finallyClause();
                        if (inputState.guessing == 0)
                            {

                            result.setFinallyClause(blck);
                            result.setFinishPosition(blck.getFinishPosition());

                        }
                        break;
                    }
                case KEYWORD_CATCH :
                    {
                        {
                            int _cnt164 = 0;
                            _loop164 : do {
                                if ((LA(1) == KEYWORD_CATCH))
                                    {
                                    ctch = catchClause();
                                    if (inputState.guessing == 0)
                                        {

                                        result.getCatchClauses().add(ctch);
                                        result.setFinishPosition(ctch.getFinishPosition());

                                    }
                                }
                                else
                                    {
                                    if (_cnt164 >= 1)
                                        {
                                        break _loop164;
                                    }
                                    else
                                        {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                                }

                                _cnt164++;
                            }
                            while (true);
                        }
                        {
                            switch (LA(1))
                                {
                                case KEYWORD_FINALLY :
                                    {
                                        blck = finallyClause();
                                        if (inputState.guessing == 0)
                                            {

                                            result.setFinallyClause(blck);
                                            result.setFinishPosition(blck.getFinishPosition());

                                        }
                                        break;
                                    }
                                case EOF :
                                case FLOATING_POINT_LITERAL :
                                case INTEGER_LITERAL :
                                case OP_DECREMENT :
                                case OP_INCREMENT :
                                case FALSE_LITERAL :
                                case KEYWORD_ABSTRACT :
                                case KEYWORD_BOOLEAN :
                                case KEYWORD_BREAK :
                                case KEYWORD_BYTE :
                                case KEYWORD_CASE :
                                case KEYWORD_CHAR :
                                case KEYWORD_CLASS :
                                case KEYWORD_CONTINUE :
                                case KEYWORD_DEFAULT :
                                case KEYWORD_DO :
                                case KEYWORD_DOUBLE :
                                case KEYWORD_ELSE :
                                case KEYWORD_FINAL :
                                case KEYWORD_FLOAT :
                                case KEYWORD_FOR :
                                case KEYWORD_IF :
                                case KEYWORD_INT :
                                case KEYWORD_LONG :
                                case KEYWORD_NEW :
                                case KEYWORD_PRIVATE :
                                case KEYWORD_PROTECTED :
                                case KEYWORD_PUBLIC :
                                case KEYWORD_RETURN :
                                case KEYWORD_SHORT :
                                case KEYWORD_STATIC :
                                case KEYWORD_STRICTFP :
                                case KEYWORD_SUPER :
                                case KEYWORD_SWITCH :
                                case KEYWORD_SYNCHRONIZED :
                                case KEYWORD_THIS :
                                case KEYWORD_THROW :
                                case KEYWORD_TRY :
                                case KEYWORD_VOID :
                                case KEYWORD_WHILE :
                                case NULL_LITERAL :
                                case TRUE_LITERAL :
                                case SEP_SEMICOLON :
                                case SEP_OPENING_BRACE :
                                case SEP_CLOSING_BRACE :
                                case SEP_OPENING_PARENTHESIS :
                                case OP_PLUS :
                                case OP_MINUS :
                                case OP_BITWISE_COMPLEMENT :
                                case OP_NOT :
                                case CHARACTER_LITERAL :
                                case STRING_LITERAL :
                                case IDENTIFIER :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final SwitchStatement switchStatement()
        throws RecognitionException, TokenStreamException
    {
        SwitchStatement result;

        Token tok1 = null;
        Token tok2 = null;

        Expression expr = null;
        CaseBlock caseBlck = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_SWITCH);
        match(SEP_OPENING_PARENTHESIS);
        expr = expression();
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createSwitchStatement(expr);
            setStartPos(result.getStartPosition(), tok1);

        }
        match(SEP_OPENING_BRACE);
        if (inputState.guessing == 0)
            {

            _helper.pushBlockScope();

        }
        {
            _loop170 : do {
                if ((LA(1) == KEYWORD_CASE || LA(1) == KEYWORD_DEFAULT))
                    {
                    caseBlck = caseBlock();
                    if (inputState.guessing == 0)
                        {

                        result.getCaseBlocks().add(caseBlck);

                    }
                }
                else
                    {
                    break _loop170;
                }

            }
            while (true);
        }
        tok2 = LT(1);
        match(SEP_CLOSING_BRACE);
        if (inputState.guessing == 0)
            {

            _helper.popBlockScope();
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final StatementExpressionList statementExpressionList()
        throws RecognitionException, TokenStreamException
    {
        StatementExpressionList result;

        Expression expr = null;

        result = _factory.createStatementExpressionList();

        expr = expression();
        if (inputState.guessing == 0)
            {

            result.getExpressions().add(expr);
            result.setStartPosition(expr.getStartPosition());
            result.setFinishPosition(expr.getFinishPosition());

        }
        {
            _loop160 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    expr = expression();
                    if (inputState.guessing == 0)
                        {

                        result.getExpressions().add(expr);
                        result.setFinishPosition(expr.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop160;
                }

            }
            while (true);
        }
        return result;
    }

    public final Block finallyClause()
        throws RecognitionException, TokenStreamException
    {
        Block result;

        match(KEYWORD_FINALLY);
        result = block();
        return result;
    }

    public final CatchClause catchClause()
        throws RecognitionException, TokenStreamException
    {
        CatchClause result;

        Token tok1 = null;

        FormalParameter param = null;
        Block blck = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_CATCH);
        if (inputState.guessing == 0)
            {

            // for the formal parameter
            _helper.pushBlockScope();

        }
        match(SEP_OPENING_PARENTHESIS);
        param = formalParameter(true);
        match(SEP_CLOSING_PARENTHESIS);
        blck = block();
        if (inputState.guessing == 0)
            {

            result = _factory.createCatchClause(param, blck);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(blck.getFinishPosition());
            _helper.popBlockScope();

        }
        return result;
    }

    public final CaseBlock caseBlock()
        throws RecognitionException, TokenStreamException
    {
        CaseBlock result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;

        Expression expr = null;
        BlockStatement stmt = null;

        result = _factory.createCaseBlock();

        {
            {
                int _cnt175 = 0;
                _loop175 : do {
                    if ((LA(1) == KEYWORD_CASE || LA(1) == KEYWORD_DEFAULT)
                        && (_tokenSet_29.member(LA(2))))
                        {
                        if (inputState.guessing == 0)
                            {

                            expr = null;

                        }
                        {
                            switch (LA(1))
                                {
                                case KEYWORD_DEFAULT :
                                    {
                                        tok1 = LT(1);
                                        match(KEYWORD_DEFAULT);
                                        if (inputState.guessing == 0)
                                            {

                                            setStartPos(result.getStartPosition(), tok1);

                                        }
                                        break;
                                    }
                                case KEYWORD_CASE :
                                    {
                                        tok2 = LT(1);
                                        match(KEYWORD_CASE);
                                        expr = expression();
                                        if (inputState.guessing == 0)
                                            {

                                            setStartPos(result.getStartPosition(), tok2);

                                        }
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        tok3 = LT(1);
                        match(OP_COLON);
                        if (inputState.guessing == 0)
                            {

                            if (expr == null)
                                {
                                result.addDefaultCase();
                            }
                            else
                                {
                                result.getCases().add(expr);
                            }
                            setFinishPos(result.getFinishPosition(), tok3);

                        }
                    }
                    else
                        {
                        if (_cnt175 >= 1)
                            {
                            break _loop175;
                        }
                        else
                            {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt175++;
                }
                while (true);
            }
        }
        {
            _loop177 : do {
                if ((_tokenSet_22.member(LA(1))))
                    {
                    stmt = blockStatement();
                    if (inputState.guessing == 0)
                        {

                        if (stmt instanceof MultipleVariableDeclaration)
                            {
                            ((MultipleVariableDeclaration) stmt).addTo(result);
                        }
                        else
                            {
                            result.getBlockStatements().add(stmt);
                        }
                        result.setFinishPosition(((Node) stmt).getFinishPosition());

                    }
                }
                else
                    {
                    break _loop177;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression assignmentExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression leftHandSide = null;
        ParsePosition finish;
        int operator = -1;

        result = null;

        boolean synPredMatched181 = false;
        if (((_tokenSet_30.member(LA(1))) && (_tokenSet_31.member(LA(2)))))
            {
            int _m181 = mark();
            synPredMatched181 = true;
            inputState.guessing++;
            try
                {
                {
                    primary();
                    assignmentOperator();
                }
            }
            catch (RecognitionException pe)
                {
                synPredMatched181 = false;
            }
            rewind(_m181);
            inputState.guessing--;
        }
        if (synPredMatched181)
            {
            leftHandSide = primary();
            operator = assignmentOperator();
            result = assignmentExpression();
            if (inputState.guessing == 0)
                {

                finish = result.getFinishPosition();
                result = _factory.createAssignmentExpression(leftHandSide, operator, result);
                result.setStartPosition(leftHandSide.getStartPosition());
                result.setFinishPosition(finish);

            }
        }
        else if ((_tokenSet_24.member(LA(1))) && (_tokenSet_32.member(LA(2))))
            {
            result = conditionalExpression();
        }
        else
            {
            throw new NoViableAltException(LT(1), getFilename());
        }

        return result;
    }

    public final int assignmentOperator()
        throws RecognitionException, TokenStreamException
    {
        int operator;

        operator = -1;

        switch (LA(1))
            {
            case OP_ASSIGN :
                {
                    match(OP_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.ASSIGN_OP;

                    }
                    break;
                }
            case OP_MULTIPLY_ASSIGN :
                {
                    match(OP_MULTIPLY_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.MULTIPLY_ASSIGN_OP;

                    }
                    break;
                }
            case OP_DIVIDE_ASSIGN :
                {
                    match(OP_DIVIDE_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.DIVIDE_ASSIGN_OP;

                    }
                    break;
                }
            case OP_MOD_ASSIGN :
                {
                    match(OP_MOD_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.MOD_ASSIGN_OP;

                    }
                    break;
                }
            case OP_PLUS_ASSIGN :
                {
                    match(OP_PLUS_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.PLUS_ASSIGN_OP;

                    }
                    break;
                }
            case OP_MINUS_ASSIGN :
                {
                    match(OP_MINUS_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.MINUS_ASSIGN_OP;

                    }
                    break;
                }
            case OP_SHIFT_LEFT_ASSIGN :
                {
                    match(OP_SHIFT_LEFT_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.SHIFT_LEFT_ASSIGN_OP;

                    }
                    break;
                }
            case OP_SHIFT_RIGHT_ASSIGN :
                {
                    match(OP_SHIFT_RIGHT_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.SHIFT_RIGHT_ASSIGN_OP;

                    }
                    break;
                }
            case OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN :
                {
                    match(OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.ZERO_FILL_SHIFT_RIGHT_ASSIGN_OP;

                    }
                    break;
                }
            case OP_BITWISE_AND_ASSIGN :
                {
                    match(OP_BITWISE_AND_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.BITWISE_AND_ASSIGN_OP;

                    }
                    break;
                }
            case OP_BITWISE_XOR_ASSIGN :
                {
                    match(OP_BITWISE_XOR_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.BITWISE_XOR_ASSIGN_OP;

                    }
                    break;
                }
            case OP_BITWISE_OR_ASSIGN :
                {
                    match(OP_BITWISE_OR_ASSIGN);
                    if (inputState.guessing == 0)
                        {

                        operator = AssignmentExpression.BITWISE_OR_ASSIGN_OP;

                    }
                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return operator;
    }

    public final Expression conditionalExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression trueExpression = null;
        Expression falseExpression = null;
        ParsePosition start;

        result = null;

        result = orExpression();
        {
            switch (LA(1))
                {
                case OP_QUESTION :
                    {
                        match(OP_QUESTION);
                        trueExpression = expression();
                        match(OP_COLON);
                        falseExpression = conditionalExpression();
                        if (inputState.guessing == 0)
                            {

                            start = result.getStartPosition();
                            result =
                                _factory.createConditionalExpression(result, trueExpression, falseExpression);
                            result.setStartPosition(start);
                            result.setFinishPosition(falseExpression.getFinishPosition());

                        }
                        break;
                    }
                case EOF :
                case SEP_SEMICOLON :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_CLOSING_BRACKET :
                case OP_COLON :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final Expression orExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;

        result = null;

        result = andExpression();
        {
            _loop186 : do {
                if ((LA(1) == OP_OR))
                    {
                    match(OP_OR);
                    second = andExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result =
                            _factory.createBinaryExpression(BinaryExpression.OR_OP, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop186;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression andExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;

        result = null;

        result = bitwiseOrExpression();
        {
            _loop189 : do {
                if ((LA(1) == OP_AND))
                    {
                    match(OP_AND);
                    second = bitwiseOrExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result =
                            _factory.createBinaryExpression(BinaryExpression.AND_OP, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop189;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression bitwiseOrExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;

        result = null;

        result = bitwiseXorExpression();
        {
            _loop192 : do {
                if ((LA(1) == OP_BITWISE_OR))
                    {
                    match(OP_BITWISE_OR);
                    second = bitwiseXorExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result =
                            _factory.createBinaryExpression(BinaryExpression.BITWISE_OR_OP, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop192;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression bitwiseXorExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;

        result = null;

        result = bitwiseAndExpression();
        {
            _loop195 : do {
                if ((LA(1) == OP_BITWISE_XOR))
                    {
                    match(OP_BITWISE_XOR);
                    second = bitwiseAndExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result =
                            _factory.createBinaryExpression(
                                BinaryExpression.BITWISE_XOR_OP,
                                result,
                                second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop195;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression bitwiseAndExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;

        result = null;

        result = equalityExpression();
        {
            _loop198 : do {
                if ((LA(1) == OP_BITWISE_AND))
                    {
                    match(OP_BITWISE_AND);
                    second = equalityExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result =
                            _factory.createBinaryExpression(
                                BinaryExpression.BITWISE_AND_OP,
                                result,
                                second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop198;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression equalityExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;
        int operator = -1;

        result = null;

        result = relationalExpression();
        {
            _loop202 : do {
                if ((LA(1) == OP_EQUAL || LA(1) == OP_NOT_EQUAL))
                    {
                    {
                        switch (LA(1))
                            {
                            case OP_EQUAL :
                                {
                                    match(OP_EQUAL);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.EQUAL_OP;

                                    }
                                    break;
                                }
                            case OP_NOT_EQUAL :
                                {
                                    match(OP_NOT_EQUAL);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.NOT_EQUAL_OP;

                                    }
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    second = relationalExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result = _factory.createBinaryExpression(operator, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop202;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression relationalExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        Type refType = null;
        ParsePosition start;
        int operator = -1;

        result = null;

        result = shiftExpression();
        {
            switch (LA(1))
                {
                case OP_GREATER_OR_EQUAL :
                case OP_LOWER_OR_EQUAL :
                case OP_LOWER :
                case OP_GREATER :
                    {
                        {
                            {
                                switch (LA(1))
                                    {
                                    case OP_LOWER :
                                        {
                                            match(OP_LOWER);
                                            if (inputState.guessing == 0)
                                                {

                                                operator = BinaryExpression.LOWER_OP;

                                            }
                                            break;
                                        }
                                    case OP_GREATER :
                                        {
                                            match(OP_GREATER);
                                            if (inputState.guessing == 0)
                                                {

                                                operator = BinaryExpression.GREATER_OP;

                                            }
                                            break;
                                        }
                                    case OP_LOWER_OR_EQUAL :
                                        {
                                            match(OP_LOWER_OR_EQUAL);
                                            if (inputState.guessing == 0)
                                                {

                                                operator = BinaryExpression.LOWER_OR_EQUAL_OP;

                                            }
                                            break;
                                        }
                                    case OP_GREATER_OR_EQUAL :
                                        {
                                            match(OP_GREATER_OR_EQUAL);
                                            if (inputState.guessing == 0)
                                                {

                                                operator = BinaryExpression.GREATER_OR_EQUAL_OP;

                                            }
                                            break;
                                        }
                                    default :
                                        {
                                            throw new NoViableAltException(LT(1), getFilename());
                                        }
                                }
                            }
                            second = shiftExpression();
                            if (inputState.guessing == 0)
                                {

                                start = result.getStartPosition();
                                result = _factory.createBinaryExpression(operator, result, second);
                                result.setStartPosition(start);
                                result.setFinishPosition(second.getFinishPosition());

                            }
                        }
                        break;
                    }
                case KEYWORD_INSTANCEOF :
                    {
                        match(KEYWORD_INSTANCEOF);
                        refType = type();
                        if (inputState.guessing == 0)
                            {

                            start = result.getStartPosition();
                            result = _factory.createInstanceofExpression(result, refType);
                            result.setStartPosition(start);
                            result.setFinishPosition(refType.getFinishPosition());

                        }
                        break;
                    }
                case EOF :
                case OP_AND :
                case OP_EQUAL :
                case OP_NOT_EQUAL :
                case OP_OR :
                case SEP_SEMICOLON :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_CLOSING_BRACKET :
                case OP_COLON :
                case OP_QUESTION :
                case OP_BITWISE_OR :
                case OP_BITWISE_XOR :
                case OP_BITWISE_AND :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final Expression shiftExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;
        int operator = -1;

        result = null;

        result = additiveExpression();
        {
            _loop210 : do {
                if ((_tokenSet_33.member(LA(1))))
                    {
                    {
                        switch (LA(1))
                            {
                            case OP_SHIFT_LEFT :
                                {
                                    match(OP_SHIFT_LEFT);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.SHIFT_LEFT_OP;

                                    }
                                    break;
                                }
                            case OP_SHIFT_RIGHT :
                                {
                                    match(OP_SHIFT_RIGHT);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.SHIFT_RIGHT_OP;

                                    }
                                    break;
                                }
                            case OP_ZERO_FILL_SHIFT_RIGHT :
                                {
                                    match(OP_ZERO_FILL_SHIFT_RIGHT);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.ZERO_FILL_SHIFT_RIGHT_OP;

                                    }
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    second = additiveExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result = _factory.createBinaryExpression(operator, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop210;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression additiveExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;
        int operator = -1;

        result = null;

        result = multiplicativeExpression();
        {
            _loop214 : do {
                if ((LA(1) == OP_PLUS || LA(1) == OP_MINUS))
                    {
                    {
                        switch (LA(1))
                            {
                            case OP_PLUS :
                                {
                                    match(OP_PLUS);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.PLUS_OP;

                                    }
                                    break;
                                }
                            case OP_MINUS :
                                {
                                    match(OP_MINUS);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.MINUS_OP;

                                    }
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    second = multiplicativeExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result = _factory.createBinaryExpression(operator, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop214;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression multiplicativeExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Expression second = null;
        ParsePosition start;
        int operator = -1;

        result = null;

        result = unaryExpression();
        {
            _loop218 : do {
                if ((_tokenSet_34.member(LA(1))))
                    {
                    {
                        switch (LA(1))
                            {
                            case OP_MULTIPLY :
                                {
                                    match(OP_MULTIPLY);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.MULTIPLY_OP;

                                    }
                                    break;
                                }
                            case OP_DIVIDE :
                                {
                                    match(OP_DIVIDE);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.DIVIDE_OP;

                                    }
                                    break;
                                }
                            case OP_MOD :
                                {
                                    match(OP_MOD);
                                    if (inputState.guessing == 0)
                                        {

                                        operator = BinaryExpression.MOD_OP;

                                    }
                                    break;
                                }
                            default :
                                {
                                    throw new NoViableAltException(LT(1), getFilename());
                                }
                        }
                    }
                    second = unaryExpression();
                    if (inputState.guessing == 0)
                        {

                        start = result.getStartPosition();
                        result = _factory.createBinaryExpression(operator, result, second);
                        result.setStartPosition(start);
                        result.setFinishPosition(second.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop218;
                }

            }
            while (true);
        }
        return result;
    }

    public final Expression unaryExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;

        ParsePosition finish;

        result = null;

        switch (LA(1))
            {
            case OP_INCREMENT :
                {
                    tok1 = LT(1);
                    match(OP_INCREMENT);
                    result = primary();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.INCREMENT_OP, result);
                        setStartPos(result.getStartPosition(), tok1);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            case OP_DECREMENT :
                {
                    tok2 = LT(1);
                    match(OP_DECREMENT);
                    result = primary();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.DECREMENT_OP, result);
                        setStartPos(result.getStartPosition(), tok2);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            case OP_PLUS :
                {
                    tok3 = LT(1);
                    match(OP_PLUS);
                    result = unaryExpression();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.PLUS_OP, result);
                        setStartPos(result.getStartPosition(), tok3);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            case OP_MINUS :
                {
                    tok4 = LT(1);
                    match(OP_MINUS);
                    result = unaryExpression();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.MINUS_OP, result);
                        setStartPos(result.getStartPosition(), tok4);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            case FLOATING_POINT_LITERAL :
            case INTEGER_LITERAL :
            case FALSE_LITERAL :
            case KEYWORD_BOOLEAN :
            case KEYWORD_BYTE :
            case KEYWORD_CHAR :
            case KEYWORD_DOUBLE :
            case KEYWORD_FLOAT :
            case KEYWORD_INT :
            case KEYWORD_LONG :
            case KEYWORD_NEW :
            case KEYWORD_SHORT :
            case KEYWORD_SUPER :
            case KEYWORD_THIS :
            case KEYWORD_VOID :
            case NULL_LITERAL :
            case TRUE_LITERAL :
            case SEP_OPENING_PARENTHESIS :
            case OP_BITWISE_COMPLEMENT :
            case OP_NOT :
            case CHARACTER_LITERAL :
            case STRING_LITERAL :
            case IDENTIFIER :
                {
                    result = unaryExpressionNotPlusMinus();
                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return result;
    }

    public final Expression unaryExpressionNotPlusMinus()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition finish;

        result = null;

        switch (LA(1))
            {
            case OP_BITWISE_COMPLEMENT :
                {
                    tok1 = LT(1);
                    match(OP_BITWISE_COMPLEMENT);
                    result = unaryExpression();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.COMPLEMENT_OP, result);
                        setStartPos(result.getStartPosition(), tok1);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            case OP_NOT :
                {
                    tok2 = LT(1);
                    match(OP_NOT);
                    result = unaryExpression();
                    if (inputState.guessing == 0)
                        {

                        finish = result.getFinishPosition();
                        result = _factory.createUnaryExpression(UnaryExpression.NEGATION_OP, result);
                        setStartPos(result.getStartPosition(), tok2);
                        result.setFinishPosition(finish);

                    }
                    break;
                }
            default :
                boolean synPredMatched222 = false;
                if (((LA(1) == SEP_OPENING_PARENTHESIS) && (_tokenSet_35.member(LA(2)))))
                    {
                    int _m222 = mark();
                    synPredMatched222 = true;
                    inputState.guessing++;
                    try
                        {
                        {
                            match(SEP_OPENING_PARENTHESIS);
                            primitiveType();
                            match(SEP_CLOSING_PARENTHESIS);
                        }
                    }
                    catch (RecognitionException pe)
                        {
                        synPredMatched222 = false;
                    }
                    rewind(_m222);
                    inputState.guessing--;
                }
                if (synPredMatched222)
                    {
                    result = primitiveCast();
                }
                else
                    {
                    boolean synPredMatched224 = false;
                    if (((LA(1) == SEP_OPENING_PARENTHESIS) && (LA(2) == IDENTIFIER)))
                        {
                        int _m224 = mark();
                        synPredMatched224 = true;
                        inputState.guessing++;
                        try
                            {
                            {
                                match(SEP_OPENING_PARENTHESIS);
                                referenceType();
                                match(SEP_CLOSING_PARENTHESIS);
                                unaryExpressionNotPlusMinus();
                            }
                        }
                        catch (RecognitionException pe)
                            {
                            synPredMatched224 = false;
                        }
                        rewind(_m224);
                        inputState.guessing--;
                    }
                    if (synPredMatched224)
                        {
                        result = referenceCast();
                    }
                    else if ((_tokenSet_30.member(LA(1))) && (_tokenSet_32.member(LA(2))))
                        {
                        result = postfixExpression();
                    }
                    else
                        {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }
        }
        return result;
    }

    public final Type primitiveType()
        throws RecognitionException, TokenStreamException
    {
        Type result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        String name = null;
        int dimensions = 0;

        result = null;

        name = primitiveTypeName(start);
        if (inputState.guessing == 0)
            {

            result = _factory.createType(name, true);
            result.setStartPosition(start);
            start.addColumn(name.length() - 1);
            result.setFinishPosition(start);

        }
        {
            _loop318 : do {
                if ((LA(1) == SEP_OPENING_BRACKET))
                    {
                    match(SEP_OPENING_BRACKET);
                    tok1 = LT(1);
                    match(SEP_CLOSING_BRACKET);
                    if (inputState.guessing == 0)
                        {

                        dimensions++;
                        setFinishPos(result.getFinishPosition(), tok1);

                    }
                }
                else
                    {
                    break _loop318;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result.setDimensions(dimensions);

        }
        return result;
    }

    public final Expression primitiveCast()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Token tok1 = null;

        Type castType = null;
        ParsePosition finish;

        result = null;

        tok1 = LT(1);
        match(SEP_OPENING_PARENTHESIS);
        castType = primitiveType();
        match(SEP_CLOSING_PARENTHESIS);
        result = unaryExpression();
        if (inputState.guessing == 0)
            {

            finish = result.getFinishPosition();
            result = _factory.createCastExpression(castType, result);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(finish);

        }
        return result;
    }

    public final Type referenceType()
        throws RecognitionException, TokenStreamException
    {
        Type result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        String name = null;
        int dimensions = 0;

        result = null;

        name = qualifiedName(start);
        if (inputState.guessing == 0)
            {

            result = _factory.createType(name, false);
            result.setStartPosition(start);
            start.addColumn(name.length() - 1);
            result.setFinishPosition(start);

        }
        {
            _loop315 : do {
                if ((LA(1) == SEP_OPENING_BRACKET))
                    {
                    match(SEP_OPENING_BRACKET);
                    tok1 = LT(1);
                    match(SEP_CLOSING_BRACKET);
                    if (inputState.guessing == 0)
                        {

                        dimensions++;
                        setFinishPos(result.getFinishPosition(), tok1);

                    }
                }
                else
                    {
                    break _loop315;
                }

            }
            while (true);
        }
        if (inputState.guessing == 0)
            {

            result.setDimensions(dimensions);

        }
        return result;
    }

    public final Expression referenceCast()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Token tok1 = null;

        Type castType = null;
        ParsePosition finish;

        result = null;

        tok1 = LT(1);
        match(SEP_OPENING_PARENTHESIS);
        castType = referenceType();
        match(SEP_CLOSING_PARENTHESIS);
        result = unaryExpressionNotPlusMinus();
        if (inputState.guessing == 0)
            {

            finish = result.getFinishPosition();
            result = _factory.createCastExpression(castType, result);
            setStartPos(result.getStartPosition(), tok1);
            result.setFinishPosition(finish);

        }
        return result;
    }

    public final Expression postfixExpression()
        throws RecognitionException, TokenStreamException
    {
        Expression result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition start;

        result = null;

        result = primary();
        {
            switch (LA(1))
                {
                case OP_INCREMENT :
                    {
                        tok1 = LT(1);
                        match(OP_INCREMENT);
                        if (inputState.guessing == 0)
                            {

                            start = result.getStartPosition();
                            result = _factory.createPostfixExpression((Primary) result, true);
                            result.setStartPosition(start);
                            setFinishPos(result.getFinishPosition(), tok1);

                        }
                        break;
                    }
                case OP_DECREMENT :
                    {
                        tok2 = LT(1);
                        match(OP_DECREMENT);
                        if (inputState.guessing == 0)
                            {

                            start = result.getStartPosition();
                            result = _factory.createPostfixExpression((Primary) result, false);
                            result.setStartPosition(start);
                            setFinishPos(result.getFinishPosition(), tok2);

                        }
                        break;
                    }
                case EOF :
                case OP_AND :
                case OP_DIVIDE :
                case OP_EQUAL :
                case OP_GREATER_OR_EQUAL :
                case OP_LOWER_OR_EQUAL :
                case OP_NOT_EQUAL :
                case OP_OR :
                case OP_SHIFT_LEFT :
                case OP_SHIFT_RIGHT :
                case OP_ZERO_FILL_SHIFT_RIGHT :
                case KEYWORD_INSTANCEOF :
                case SEP_SEMICOLON :
                case OP_MULTIPLY :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_CLOSING_BRACKET :
                case OP_COLON :
                case OP_QUESTION :
                case OP_BITWISE_OR :
                case OP_BITWISE_XOR :
                case OP_BITWISE_AND :
                case OP_LOWER :
                case OP_GREATER :
                case OP_PLUS :
                case OP_MINUS :
                case OP_MOD :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final Literal literal()
        throws RecognitionException, TokenStreamException
    {
        Literal result;

        result = null;

        switch (LA(1))
            {
            case INTEGER_LITERAL :
                {
                    result = integerLiteral();
                    break;
                }
            case FLOATING_POINT_LITERAL :
                {
                    result = floatingPointLiteral();
                    break;
                }
            case FALSE_LITERAL :
            case TRUE_LITERAL :
                {
                    result = booleanLiteral();
                    break;
                }
            case CHARACTER_LITERAL :
                {
                    result = characterLiteral();
                    break;
                }
            case STRING_LITERAL :
                {
                    result = stringLiteral();
                    break;
                }
            case NULL_LITERAL :
                {
                    result = nullLiteral();
                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return result;
    }

    public final Primary parenthesizedExpression()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;
        Token tok2 = null;

        Expression innerExpr = null;

        result = null;

        tok1 = LT(1);
        match(SEP_OPENING_PARENTHESIS);
        innerExpr = expression();
        tok2 = LT(1);
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createParenthesizedExpression(innerExpr);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final String primitiveTypeName(ParsePosition start)
        throws RecognitionException, TokenStreamException
    {
        String result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;
        Token tok5 = null;
        Token tok6 = null;
        Token tok7 = null;
        Token tok8 = null;

        result = null;

        {
            switch (LA(1))
                {
                case KEYWORD_BOOLEAN :
                    {
                        tok1 = LT(1);
                        match(KEYWORD_BOOLEAN);
                        if (inputState.guessing == 0)
                            {

                            result = tok1.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok1);
                            }

                        }
                        break;
                    }
                case KEYWORD_BYTE :
                    {
                        tok2 = LT(1);
                        match(KEYWORD_BYTE);
                        if (inputState.guessing == 0)
                            {

                            result = tok2.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok2);
                            }

                        }
                        break;
                    }
                case KEYWORD_SHORT :
                    {
                        tok3 = LT(1);
                        match(KEYWORD_SHORT);
                        if (inputState.guessing == 0)
                            {

                            result = tok3.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok3);
                            }

                        }
                        break;
                    }
                case KEYWORD_CHAR :
                    {
                        tok4 = LT(1);
                        match(KEYWORD_CHAR);
                        if (inputState.guessing == 0)
                            {

                            result = tok4.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok4);
                            }

                        }
                        break;
                    }
                case KEYWORD_INT :
                    {
                        tok5 = LT(1);
                        match(KEYWORD_INT);
                        if (inputState.guessing == 0)
                            {

                            result = tok5.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok5);
                            }

                        }
                        break;
                    }
                case KEYWORD_LONG :
                    {
                        tok6 = LT(1);
                        match(KEYWORD_LONG);
                        if (inputState.guessing == 0)
                            {

                            result = tok6.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok6);
                            }

                        }
                        break;
                    }
                case KEYWORD_FLOAT :
                    {
                        tok7 = LT(1);
                        match(KEYWORD_FLOAT);
                        if (inputState.guessing == 0)
                            {

                            result = tok7.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok7);
                            }

                        }
                        break;
                    }
                case KEYWORD_DOUBLE :
                    {
                        tok8 = LT(1);
                        match(KEYWORD_DOUBLE);
                        if (inputState.guessing == 0)
                            {

                            result = tok8.getText();
                            if (start != null)
                                {
                                setStartPos(start, tok8);
                            }

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final ArrayCreation arrayCreation()
        throws RecognitionException, TokenStreamException
    {
        ArrayCreation result;

        Token tok1 = null;
        Token tok2 = null;
        Token tok3 = null;
        Token tok4 = null;

        String typeName = null;
        boolean isPrimitive = false;
        Expression innerExpr = null;
        ArrayInitializer initializer = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_NEW);
        {
            switch (LA(1))
                {
                case IDENTIFIER :
                    {
                        typeName = qualifiedName(null);
                        break;
                    }
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_SHORT :
                    {
                        typeName = primitiveTypeName(null);
                        if (inputState.guessing == 0)
                            {

                            isPrimitive = true;

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            result = _factory.createArrayCreation(typeName, isPrimitive);
            setStartPos(result.getStartPosition(), tok1);

        }
        {
            boolean synPredMatched265 = false;
            if (((LA(1) == SEP_OPENING_BRACKET) && (_tokenSet_24.member(LA(2)))))
                {
                int _m265 = mark();
                synPredMatched265 = true;
                inputState.guessing++;
                try
                    {
                    {
                        match(SEP_OPENING_BRACKET);
                        expression();
                    }
                }
                catch (RecognitionException pe)
                    {
                    synPredMatched265 = false;
                }
                rewind(_m265);
                inputState.guessing--;
            }
            if (synPredMatched265)
                {
                {
                    int _cnt267 = 0;
                    _loop267 : do {
                        if ((LA(1) == SEP_OPENING_BRACKET) && (_tokenSet_24.member(LA(2))))
                            {
                            match(SEP_OPENING_BRACKET);
                            innerExpr = expression();
                            tok2 = LT(1);
                            match(SEP_CLOSING_BRACKET);
                            if (inputState.guessing == 0)
                                {

                                result.addDimension(innerExpr);
                                setFinishPos(result.getFinishPosition(), tok2);

                            }
                        }
                        else
                            {
                            if (_cnt267 >= 1)
                                {
                                break _loop267;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }

                        _cnt267++;
                    }
                    while (true);
                }
                {
                    _loop269 : do {
                        if ((LA(1) == SEP_OPENING_BRACKET) && (LA(2) == SEP_CLOSING_BRACKET))
                            {
                            match(SEP_OPENING_BRACKET);
                            tok3 = LT(1);
                            match(SEP_CLOSING_BRACKET);
                            if (inputState.guessing == 0)
                                {

                                result.addDimension();
                                setFinishPos(result.getFinishPosition(), tok3);

                            }
                        }
                        else
                            {
                            break _loop269;
                        }

                    }
                    while (true);
                }
            }
            else if ((LA(1) == SEP_OPENING_BRACKET) && (LA(2) == SEP_CLOSING_BRACKET))
                {
                {
                    int _cnt271 = 0;
                    _loop271 : do {
                        if ((LA(1) == SEP_OPENING_BRACKET) && (LA(2) == SEP_CLOSING_BRACKET))
                            {
                            match(SEP_OPENING_BRACKET);
                            tok4 = LT(1);
                            match(SEP_CLOSING_BRACKET);
                            if (inputState.guessing == 0)
                                {

                                result.addDimension();
                                setFinishPos(result.getFinishPosition(), tok4);

                            }
                        }
                        else
                            {
                            if (_cnt271 >= 1)
                                {
                                break _loop271;
                            }
                            else
                                {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }

                        _cnt271++;
                    }
                    while (true);
                }
                {
                    switch (LA(1))
                        {
                        case SEP_OPENING_BRACE :
                            {
                                initializer = arrayInitializer();
                                if (inputState.guessing == 0)
                                    {

                                    result.setInitializer(initializer);
                                    result.setFinishPosition(initializer.getFinishPosition());

                                }
                                break;
                            }
                        case EOF :
                        case OP_AND :
                        case OP_BITWISE_AND_ASSIGN :
                        case OP_BITWISE_OR_ASSIGN :
                        case OP_BITWISE_XOR_ASSIGN :
                        case OP_DECREMENT :
                        case OP_DIVIDE :
                        case OP_DIVIDE_ASSIGN :
                        case OP_EQUAL :
                        case OP_GREATER_OR_EQUAL :
                        case OP_INCREMENT :
                        case OP_LOWER_OR_EQUAL :
                        case OP_MINUS_ASSIGN :
                        case OP_MOD_ASSIGN :
                        case OP_MULTIPLY_ASSIGN :
                        case OP_NOT_EQUAL :
                        case OP_OR :
                        case OP_PLUS_ASSIGN :
                        case OP_SHIFT_LEFT :
                        case OP_SHIFT_LEFT_ASSIGN :
                        case OP_SHIFT_RIGHT :
                        case OP_SHIFT_RIGHT_ASSIGN :
                        case OP_ZERO_FILL_SHIFT_RIGHT :
                        case OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN :
                        case SEP_DOT :
                        case KEYWORD_INSTANCEOF :
                        case SEP_SEMICOLON :
                        case OP_MULTIPLY :
                        case SEP_COMMA :
                        case SEP_CLOSING_BRACE :
                        case SEP_CLOSING_PARENTHESIS :
                        case SEP_OPENING_BRACKET :
                        case SEP_CLOSING_BRACKET :
                        case OP_ASSIGN :
                        case OP_COLON :
                        case OP_QUESTION :
                        case OP_BITWISE_OR :
                        case OP_BITWISE_XOR :
                        case OP_BITWISE_AND :
                        case OP_LOWER :
                        case OP_GREATER :
                        case OP_PLUS :
                        case OP_MINUS :
                        case OP_MOD :
                            {
                                break;
                            }
                        default :
                            {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                    }
                }
            }
            else
                {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        return result;
    }

    public final Primary instantiation(Primary baseExpr)
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition finish = new ParsePosition();
        String className = null;
        Type type = null;
        ArgumentList args = null;
        AnonymousClassDeclaration classDecl = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_NEW);
        className = qualifiedName(start);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        args = argumentList();
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            type = _factory.createType(className, false);
            type.setStartPosition(start);
            start.addColumn(className.length() - 1);
            type.setFinishPosition(start);
            setFinishPos(finish, tok2);

        }
        {
            switch (LA(1))
                {
                case SEP_OPENING_BRACE :
                    {
                        if (inputState.guessing == 0)
                            {

                            classDecl = _factory.createAnonymousClassDeclaration(className);
                            start = new ParsePosition();
                            _helper.pushTypeScope(classDecl);

                        }
                        classBody(classDecl, start, finish);
                        if (inputState.guessing == 0)
                            {

                            classDecl.setStartPosition(start);
                            classDecl.setFinishPosition(finish);
                            _helper.popTypeScope();

                        }
                        break;
                    }
                case EOF :
                case OP_AND :
                case OP_BITWISE_AND_ASSIGN :
                case OP_BITWISE_OR_ASSIGN :
                case OP_BITWISE_XOR_ASSIGN :
                case OP_DECREMENT :
                case OP_DIVIDE :
                case OP_DIVIDE_ASSIGN :
                case OP_EQUAL :
                case OP_GREATER_OR_EQUAL :
                case OP_INCREMENT :
                case OP_LOWER_OR_EQUAL :
                case OP_MINUS_ASSIGN :
                case OP_MOD_ASSIGN :
                case OP_MULTIPLY_ASSIGN :
                case OP_NOT_EQUAL :
                case OP_OR :
                case OP_PLUS_ASSIGN :
                case OP_SHIFT_LEFT :
                case OP_SHIFT_LEFT_ASSIGN :
                case OP_SHIFT_RIGHT :
                case OP_SHIFT_RIGHT_ASSIGN :
                case OP_ZERO_FILL_SHIFT_RIGHT :
                case OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN :
                case SEP_DOT :
                case KEYWORD_INSTANCEOF :
                case SEP_SEMICOLON :
                case OP_MULTIPLY :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_OPENING_BRACKET :
                case SEP_CLOSING_BRACKET :
                case OP_ASSIGN :
                case OP_COLON :
                case OP_QUESTION :
                case OP_BITWISE_OR :
                case OP_BITWISE_XOR :
                case OP_BITWISE_AND :
                case OP_LOWER :
                case OP_GREATER :
                case OP_PLUS :
                case OP_MINUS :
                case OP_MOD :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            result = _factory.createInstantiation(baseExpr, type, args, classDecl);
            if (baseExpr == null)
                {
                setStartPos(result.getStartPosition(), tok1);
            }
            else
                {
                result.setStartPosition(baseExpr.getStartPosition());
            }
            result.setFinishPosition(finish);

        }
        return result;
    }

    public final ConstructorInvocation alternateConstructorInvocation()
        throws RecognitionException, TokenStreamException
    {
        ConstructorInvocation result;

        Token tok1 = null;
        Token tok2 = null;

        ArgumentList args = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_THIS);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        args = argumentList();
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createConstructorInvocation(false, null, args);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final ConstructorInvocation superClassConstructorInvocation(Primary baseExpr)
        throws RecognitionException, TokenStreamException
    {
        ConstructorInvocation result;

        Token tok1 = null;
        Token tok2 = null;

        ArgumentList args = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_SUPER);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        args = argumentList();
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createConstructorInvocation(true, baseExpr, args);
            if (baseExpr == null)
                {
                setStartPos(result.getStartPosition(), tok1);
            }
            else
                {
                result.setStartPosition(baseExpr.getStartPosition());
            }
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Primary selfAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        TypeAccess typeAccess = null;
        String className = null;

        result = null;

        {
            switch (LA(1))
                {
                case IDENTIFIER :
                    {
                        className = qualifiedName(start);
                        match(SEP_DOT);
                        if (inputState.guessing == 0)
                            {

                            typeAccess = _factory.createTypeAccess(null, className);
                            typeAccess.setStartPosition(start);
                            typeAccess.setFinishPosition(start);
                            typeAccess.getFinishPosition().addColumn(className.length() - 1);

                        }
                        break;
                    }
                case KEYWORD_THIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok1 = LT(1);
        match(KEYWORD_THIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createSelfAccess(typeAccess, false);
            setStartPos(start, tok1);
            result.setStartPosition(start);
            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final Primary baseClassFeatureAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;
        Token tok2 = null;

        ParsePosition start = new ParsePosition();
        ParsePosition finish = new ParsePosition();
        TypeAccess typeAccess = null;
        SelfAccess selfAccess = null;
        String className = null;
        String featureName = null;
        ArgumentList args = null;

        result = null;

        {
            switch (LA(1))
                {
                case IDENTIFIER :
                    {
                        className = qualifiedName(start);
                        match(SEP_DOT);
                        if (inputState.guessing == 0)
                            {

                            typeAccess = _factory.createTypeAccess(null, className);
                            typeAccess.setStartPosition(start);
                            typeAccess.setFinishPosition(start);
                            typeAccess.getFinishPosition().addColumn(className.length() - 1);

                        }
                        break;
                    }
                case KEYWORD_SUPER :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok1 = LT(1);
        match(KEYWORD_SUPER);
        if (inputState.guessing == 0)
            {

            selfAccess = _factory.createSelfAccess(typeAccess, true);
            setStartPos(start, tok1);
            selfAccess.setStartPosition(start);
            setFinishPos(selfAccess.getFinishPosition(), tok1);

        }
        match(SEP_DOT);
        featureName = identifier(finish);
        {
            switch (LA(1))
                {
                case SEP_OPENING_PARENTHESIS :
                    {
                        match(SEP_OPENING_PARENTHESIS);
                        {
                            switch (LA(1))
                                {
                                case FLOATING_POINT_LITERAL :
                                case INTEGER_LITERAL :
                                case OP_DECREMENT :
                                case OP_INCREMENT :
                                case FALSE_LITERAL :
                                case KEYWORD_BOOLEAN :
                                case KEYWORD_BYTE :
                                case KEYWORD_CHAR :
                                case KEYWORD_DOUBLE :
                                case KEYWORD_FLOAT :
                                case KEYWORD_INT :
                                case KEYWORD_LONG :
                                case KEYWORD_NEW :
                                case KEYWORD_SHORT :
                                case KEYWORD_SUPER :
                                case KEYWORD_THIS :
                                case KEYWORD_VOID :
                                case NULL_LITERAL :
                                case TRUE_LITERAL :
                                case SEP_OPENING_PARENTHESIS :
                                case OP_PLUS :
                                case OP_MINUS :
                                case OP_BITWISE_COMPLEMENT :
                                case OP_NOT :
                                case CHARACTER_LITERAL :
                                case STRING_LITERAL :
                                case IDENTIFIER :
                                    {
                                        args = argumentList();
                                        break;
                                    }
                                case SEP_CLOSING_PARENTHESIS :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        tok2 = LT(1);
                        match(SEP_CLOSING_PARENTHESIS);
                        if (inputState.guessing == 0)
                            {

                            result = _factory.createMethodInvocation(selfAccess, featureName, args);
                            setFinishPos(finish, tok2);

                        }
                        break;
                    }
                case EOF :
                case OP_AND :
                case OP_BITWISE_AND_ASSIGN :
                case OP_BITWISE_OR_ASSIGN :
                case OP_BITWISE_XOR_ASSIGN :
                case OP_DECREMENT :
                case OP_DIVIDE :
                case OP_DIVIDE_ASSIGN :
                case OP_EQUAL :
                case OP_GREATER_OR_EQUAL :
                case OP_INCREMENT :
                case OP_LOWER_OR_EQUAL :
                case OP_MINUS_ASSIGN :
                case OP_MOD_ASSIGN :
                case OP_MULTIPLY_ASSIGN :
                case OP_NOT_EQUAL :
                case OP_OR :
                case OP_PLUS_ASSIGN :
                case OP_SHIFT_LEFT :
                case OP_SHIFT_LEFT_ASSIGN :
                case OP_SHIFT_RIGHT :
                case OP_SHIFT_RIGHT_ASSIGN :
                case OP_ZERO_FILL_SHIFT_RIGHT :
                case OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN :
                case SEP_DOT :
                case KEYWORD_INSTANCEOF :
                case SEP_SEMICOLON :
                case OP_MULTIPLY :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_OPENING_BRACKET :
                case SEP_CLOSING_BRACKET :
                case OP_ASSIGN :
                case OP_COLON :
                case OP_QUESTION :
                case OP_BITWISE_OR :
                case OP_BITWISE_XOR :
                case OP_BITWISE_AND :
                case OP_LOWER :
                case OP_GREATER :
                case OP_PLUS :
                case OP_MINUS :
                case OP_MOD :
                    {
                        if (inputState.guessing == 0)
                            {

                            // can be field or type access
                            result = _factory.createUnresolvedAccess(selfAccess);
                            ((UnresolvedAccess) result).addPart(featureName, finish);
                            finish.addColumn(featureName.length() - 1);

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        if (inputState.guessing == 0)
            {

            result.setStartPosition(start);
            result.setFinishPosition(finish);

        }
        return result;
    }

    public final Primary voidClassAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;
        Token tok2 = null;

        result = null;

        tok1 = LT(1);
        match(KEYWORD_VOID);
        match(SEP_DOT);
        tok2 = LT(1);
        match(KEYWORD_CLASS);
        if (inputState.guessing == 0)
            {

            result = _factory.createClassAccess(null);
            setStartPos(result.getStartPosition(), tok1);
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final Primary classAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;

        Type refType = null;

        result = null;

        refType = type();
        match(SEP_DOT);
        tok1 = LT(1);
        match(KEYWORD_CLASS);
        if (inputState.guessing == 0)
            {

            result = _factory.createClassAccess(refType);
            result.setStartPosition(refType.getStartPosition());
            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final Primary directMethodAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        String featureName = null;
        ArgumentList args = null;

        result = null;

        featureName = identifier(start);
        match(SEP_OPENING_PARENTHESIS);
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        args = argumentList();
                        break;
                    }
                case SEP_CLOSING_PARENTHESIS :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok1 = LT(1);
        match(SEP_CLOSING_PARENTHESIS);
        if (inputState.guessing == 0)
            {

            result = _factory.createMethodInvocation(featureName, args);
            result.setStartPosition(start);
            setFinishPos(result.getFinishPosition(), tok1);

        }
        return result;
    }

    public final Primary unresolvedAccess()
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        ParsePosition start = new ParsePosition();
        String part = null;

        result = null;

        part = identifier(start);
        if (inputState.guessing == 0)
            {

            FieldDeclaration fieldDecl = null;
            LocalVariableDeclaration localVarDecl = null;

            if ((localVarDecl = _helper.resolveLocalVariable(part)) != null)
                {
                result = _factory.createVariableAccess(localVarDecl);
            }
            else if ((fieldDecl = _helper.resolveField(part)) != null)
                {
                result = _factory.createFieldAccess(part);
                ((FieldAccess) result).setFieldDeclaration(fieldDecl);
            }
            else
                {
                result = _factory.createUnresolvedAccess(null);
                ((UnresolvedAccess) result).addPart(part, start);
            }
            result.setStartPosition(start);
            start.addColumn(part.length() - 1);
            result.setFinishPosition(start);

        }
        return result;
    }

    public final Primary trailingFeatureAccess(Primary baseExpr)
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok1 = null;

        ParsePosition start = new ParsePosition();
        String featureName = null;
        ArgumentList args = null;

        result = null;

        featureName = identifier(start);
        {
            switch (LA(1))
                {
                case SEP_OPENING_PARENTHESIS :
                    {
                        match(SEP_OPENING_PARENTHESIS);
                        {
                            switch (LA(1))
                                {
                                case FLOATING_POINT_LITERAL :
                                case INTEGER_LITERAL :
                                case OP_DECREMENT :
                                case OP_INCREMENT :
                                case FALSE_LITERAL :
                                case KEYWORD_BOOLEAN :
                                case KEYWORD_BYTE :
                                case KEYWORD_CHAR :
                                case KEYWORD_DOUBLE :
                                case KEYWORD_FLOAT :
                                case KEYWORD_INT :
                                case KEYWORD_LONG :
                                case KEYWORD_NEW :
                                case KEYWORD_SHORT :
                                case KEYWORD_SUPER :
                                case KEYWORD_THIS :
                                case KEYWORD_VOID :
                                case NULL_LITERAL :
                                case TRUE_LITERAL :
                                case SEP_OPENING_PARENTHESIS :
                                case OP_PLUS :
                                case OP_MINUS :
                                case OP_BITWISE_COMPLEMENT :
                                case OP_NOT :
                                case CHARACTER_LITERAL :
                                case STRING_LITERAL :
                                case IDENTIFIER :
                                    {
                                        args = argumentList();
                                        break;
                                    }
                                case SEP_CLOSING_PARENTHESIS :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        tok1 = LT(1);
                        match(SEP_CLOSING_PARENTHESIS);
                        if (inputState.guessing == 0)
                            {

                            result = _factory.createMethodInvocation(baseExpr, featureName, args);
                            result.setStartPosition(baseExpr.getStartPosition());
                            setFinishPos(result.getFinishPosition(), tok1);

                        }
                        break;
                    }
                case EOF :
                case OP_AND :
                case OP_BITWISE_AND_ASSIGN :
                case OP_BITWISE_OR_ASSIGN :
                case OP_BITWISE_XOR_ASSIGN :
                case OP_DECREMENT :
                case OP_DIVIDE :
                case OP_DIVIDE_ASSIGN :
                case OP_EQUAL :
                case OP_GREATER_OR_EQUAL :
                case OP_INCREMENT :
                case OP_LOWER_OR_EQUAL :
                case OP_MINUS_ASSIGN :
                case OP_MOD_ASSIGN :
                case OP_MULTIPLY_ASSIGN :
                case OP_NOT_EQUAL :
                case OP_OR :
                case OP_PLUS_ASSIGN :
                case OP_SHIFT_LEFT :
                case OP_SHIFT_LEFT_ASSIGN :
                case OP_SHIFT_RIGHT :
                case OP_SHIFT_RIGHT_ASSIGN :
                case OP_ZERO_FILL_SHIFT_RIGHT :
                case OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN :
                case SEP_DOT :
                case KEYWORD_INSTANCEOF :
                case SEP_SEMICOLON :
                case OP_MULTIPLY :
                case SEP_COMMA :
                case SEP_CLOSING_BRACE :
                case SEP_CLOSING_PARENTHESIS :
                case SEP_OPENING_BRACKET :
                case SEP_CLOSING_BRACKET :
                case OP_ASSIGN :
                case OP_COLON :
                case OP_QUESTION :
                case OP_BITWISE_OR :
                case OP_BITWISE_XOR :
                case OP_BITWISE_AND :
                case OP_LOWER :
                case OP_GREATER :
                case OP_PLUS :
                case OP_MINUS :
                case OP_MOD :
                    {
                        if (inputState.guessing == 0)
                            {

                            if (baseExpr instanceof UnresolvedAccess)
                                {
                                result = (UnresolvedAccess) baseExpr;
                            }
                            else
                                {
                                result = _factory.createUnresolvedAccess(baseExpr);
                                result.setStartPosition(baseExpr.getStartPosition());
                            }
                            ((UnresolvedAccess) result).addPart(featureName, start);

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final Primary trailingArrayAccess(Primary baseExpr)
        throws RecognitionException, TokenStreamException
    {
        Primary result;

        Token tok2 = null;

        Expression innerExpr = null;

        result = null;

        match(SEP_OPENING_BRACKET);
        innerExpr = expression();
        tok2 = LT(1);
        match(SEP_CLOSING_BRACKET);
        if (inputState.guessing == 0)
            {

            if (baseExpr.containsArrayCreation())
                {
                throw new RecognitionException();
            }
            if (baseExpr instanceof UnresolvedAccess)
                {
                baseExpr = finalizeUnresolvedAccess((UnresolvedAccess) baseExpr);
            }
            result = _factory.createArrayAccess(baseExpr, innerExpr);
            result.setStartPosition(baseExpr.getStartPosition());
            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final ArgumentList argumentList()
        throws RecognitionException, TokenStreamException
    {
        ArgumentList result;

        Expression expr = null;

        result = _factory.createArgumentList();

        expr = expression();
        if (inputState.guessing == 0)
            {

            result.getArguments().add(expr);
            result.setStartPosition(expr.getStartPosition());
            result.setFinishPosition(expr.getFinishPosition());

        }
        {
            _loop296 : do {
                if ((LA(1) == SEP_COMMA))
                    {
                    match(SEP_COMMA);
                    expr = expression();
                    if (inputState.guessing == 0)
                        {

                        result.getArguments().add(expr);
                        result.setFinishPosition(expr.getFinishPosition());

                    }
                }
                else
                    {
                    break _loop296;
                }

            }
            while (true);
        }
        return result;
    }

    public final ArrayInitializer arrayInitializer()
        throws RecognitionException, TokenStreamException
    {
        ArrayInitializer result;

        Token tok1 = null;
        Token tok2 = null;

        VariableInitializer initializer = null;

        result = null;

        tok1 = LT(1);
        match(SEP_OPENING_BRACE);
        if (inputState.guessing == 0)
            {

            result = _factory.createArrayInitializer();
            setStartPos(result.getStartPosition(), tok1);

        }
        {
            switch (LA(1))
                {
                case FLOATING_POINT_LITERAL :
                case INTEGER_LITERAL :
                case OP_DECREMENT :
                case OP_INCREMENT :
                case FALSE_LITERAL :
                case KEYWORD_BOOLEAN :
                case KEYWORD_BYTE :
                case KEYWORD_CHAR :
                case KEYWORD_DOUBLE :
                case KEYWORD_FLOAT :
                case KEYWORD_INT :
                case KEYWORD_LONG :
                case KEYWORD_NEW :
                case KEYWORD_SHORT :
                case KEYWORD_SUPER :
                case KEYWORD_THIS :
                case KEYWORD_VOID :
                case NULL_LITERAL :
                case TRUE_LITERAL :
                case SEP_OPENING_BRACE :
                case SEP_OPENING_PARENTHESIS :
                case OP_PLUS :
                case OP_MINUS :
                case OP_BITWISE_COMPLEMENT :
                case OP_NOT :
                case CHARACTER_LITERAL :
                case STRING_LITERAL :
                case IDENTIFIER :
                    {
                        initializer = variableInitializer();
                        if (inputState.guessing == 0)
                            {

                            result.getInitializers().add(initializer);

                        }
                        {
                            _loop292 : do {
                                if ((LA(1) == SEP_COMMA) && (_tokenSet_36.member(LA(2))))
                                    {
                                    match(SEP_COMMA);
                                    initializer = variableInitializer();
                                    if (inputState.guessing == 0)
                                        {

                                        result.getInitializers().add(initializer);

                                    }
                                }
                                else
                                    {
                                    break _loop292;
                                }

                            }
                            while (true);
                        }
                        {
                            switch (LA(1))
                                {
                                case SEP_COMMA :
                                    {
                                        match(SEP_COMMA);
                                        break;
                                    }
                                case SEP_CLOSING_BRACE :
                                    {
                                        break;
                                    }
                                default :
                                    {
                                        throw new NoViableAltException(LT(1), getFilename());
                                    }
                            }
                        }
                        break;
                    }
                case SEP_CLOSING_BRACE :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        tok2 = LT(1);
        match(SEP_CLOSING_BRACE);
        if (inputState.guessing == 0)
            {

            setFinishPos(result.getFinishPosition(), tok2);

        }
        return result;
    }

    public final IntegerLiteral integerLiteral()
        throws RecognitionException, TokenStreamException
    {
        IntegerLiteral result;

        Token lit = null;

        result = null;

        lit = LT(1);
        match(INTEGER_LITERAL);
        if (inputState.guessing == 0)
            {

            result = _factory.createIntegerLiteral(lit.getText());
            setStartPos(result.getStartPosition(), lit);
            setFinishPos(result.getFinishPosition(), lit);

        }
        return result;
    }

    public final FloatingPointLiteral floatingPointLiteral()
        throws RecognitionException, TokenStreamException
    {
        FloatingPointLiteral result;

        Token lit = null;

        result = null;

        lit = LT(1);
        match(FLOATING_POINT_LITERAL);
        if (inputState.guessing == 0)
            {

            result = _factory.createFloatingPointLiteral(lit.getText());
            setStartPos(result.getStartPosition(), lit);
            setFinishPos(result.getFinishPosition(), lit);

        }
        return result;
    }

    public final BooleanLiteral booleanLiteral()
        throws RecognitionException, TokenStreamException
    {
        BooleanLiteral result;

        Token lit1 = null;
        Token lit2 = null;

        result = null;

        {
            switch (LA(1))
                {
                case TRUE_LITERAL :
                    {
                        lit1 = LT(1);
                        match(TRUE_LITERAL);
                        if (inputState.guessing == 0)
                            {

                            result = _factory.createBooleanLiteral(true);
                            setStartPos(result.getStartPosition(), lit1);
                            setFinishPos(result.getFinishPosition(), lit1);

                        }
                        break;
                    }
                case FALSE_LITERAL :
                    {
                        lit2 = LT(1);
                        match(FALSE_LITERAL);
                        if (inputState.guessing == 0)
                            {

                            result = _factory.createBooleanLiteral(false);
                            setStartPos(result.getStartPosition(), lit2);
                            setFinishPos(result.getFinishPosition(), lit2);

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return result;
    }

    public final CharacterLiteral characterLiteral()
        throws RecognitionException, TokenStreamException
    {
        CharacterLiteral result;

        Token lit = null;

        result = null;

        lit = LT(1);
        match(CHARACTER_LITERAL);
        if (inputState.guessing == 0)
            {

            result = _factory.createCharacterLiteral(lit.getText());
            setStartPos(result.getStartPosition(), lit);
            setFinishPos(result.getFinishPosition(), lit);

        }
        return result;
    }

    public final StringLiteral stringLiteral()
        throws RecognitionException, TokenStreamException
    {
        StringLiteral result;

        Token lit = null;

        result = null;

        lit = LT(1);
        match(STRING_LITERAL);
        if (inputState.guessing == 0)
            {

            result = _factory.createStringLiteral(lit.getText());
            setStartPos(result.getStartPosition(), lit);
            setFinishPos(result.getFinishPosition(), lit);

        }
        return result;
    }

    public final NullLiteral nullLiteral()
        throws RecognitionException, TokenStreamException
    {
        NullLiteral result;

        Token lit = null;

        result = null;

        lit = LT(1);
        match(NULL_LITERAL);
        if (inputState.guessing == 0)
            {

            result = _factory.createNullLiteral();
            setStartPos(result.getStartPosition(), lit);
            setFinishPos(result.getFinishPosition(), lit);

        }
        return result;
    }


    public static final String[] _tokenNames = {
        "<0>",
        "EOF",
        "<2>",
        "NULL_TREE_LOOKAHEAD",
        "BLOCK_COMMENT",
        "FLOATING_POINT_LITERAL",
        "INTEGER_LITERAL",
        "LINE_COMMENT",
        "OP_AND",
        "OP_BITWISE_AND_ASSIGN",
        "OP_BITWISE_OR_ASSIGN",
        "OP_BITWISE_XOR_ASSIGN",
        "OP_DECREMENT",
        "OP_DIVIDE",
        "OP_DIVIDE_ASSIGN",
        "OP_EQUAL",
        "OP_GREATER_OR_EQUAL",
        "OP_INCREMENT",
        "OP_LOWER_OR_EQUAL",
        "OP_MINUS_ASSIGN",
        "OP_MOD_ASSIGN",
        "OP_MULTIPLY_ASSIGN",
        "OP_NOT_EQUAL",
        "OP_OR",
        "OP_PLUS_ASSIGN",
        "OP_SHIFT_LEFT",
        "OP_SHIFT_LEFT_ASSIGN",
        "OP_SHIFT_RIGHT",
        "OP_SHIFT_RIGHT_ASSIGN",
        "OP_ZERO_FILL_SHIFT_RIGHT",
        "OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN",
        "SEP_DOT",
        "\"false\"",
        "\"abstract\"",
        "\"boolean\"",
        "\"break\"",
        "\"byte\"",
        "\"case\"",
        "\"catch\"",
        "\"char\"",
        "\"class\"",
        "\"const\"",
        "\"continue\"",
        "\"default\"",
        "\"do\"",
        "\"double\"",
        "\"else\"",
        "\"extends\"",
        "\"final\"",
        "\"finally\"",
        "\"float\"",
        "\"for\"",
        "\"goto\"",
        "\"if\"",
        "\"implements\"",
        "\"import\"",
        "\"instanceof\"",
        "\"int\"",
        "\"interface\"",
        "\"long\"",
        "\"native\"",
        "\"new\"",
        "\"package\"",
        "\"private\"",
        "\"protected\"",
        "\"public\"",
        "\"return\"",
        "\"short\"",
        "\"static\"",
        "\"strictfp\"",
        "\"super\"",
        "\"switch\"",
        "\"synchronized\"",
        "\"this\"",
        "\"throw\"",
        "\"throws\"",
        "\"transient\"",
        "\"try\"",
        "\"void\"",
        "\"volatile\"",
        "\"while\"",
        "\"null\"",
        "\"true\"",
        "SEP_SEMICOLON",
        "OP_MULTIPLY",
        "SEP_COMMA",
        "SEP_OPENING_BRACE",
        "SEP_CLOSING_BRACE",
        "SEP_OPENING_PARENTHESIS",
        "SEP_CLOSING_PARENTHESIS",
        "SEP_OPENING_BRACKET",
        "SEP_CLOSING_BRACKET",
        "OP_ASSIGN",
        "OP_COLON",
        "OP_QUESTION",
        "OP_BITWISE_OR",
        "OP_BITWISE_XOR",
        "OP_BITWISE_AND",
        "OP_LOWER",
        "OP_GREATER",
        "OP_PLUS",
        "OP_MINUS",
        "OP_MOD",
        "OP_BITWISE_COMPLEMENT",
        "OP_NOT",
        "CHARACTER_LITERAL",
        "STRING_LITERAL",
        "IDENTIFIER",
        "WHITESPACE",
        "SUB",
        "OP_DIVIDE_OR_COMMENT",
        "INT_OR_FLOAT_LITERAL_OR_DOT",
        "LINE_TERMINATOR",
        "ESCAPE_SEQUENCE",
        "UNICODE_ESCAPE",
        "OCTAL_ESCAPE",
        "OCTAL_DIGIT",
        "DIGIT",
        "HEX_DIGIT",
        "EXPONENT_PART",
        "INTEGER_TYPE_SUFFIX",
        "FLOAT_TYPE_SUFFIX",
        "IDENTIFIER_START",
        "IDENTIFIER_PART"
    };

    private static final long[] mk_tokenSet_0()
    {
        long[] data = { 722019135290081280L, 8796093022216L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());

    private static final long[] mk_tokenSet_1()
    {
        long[] data = { 721737662460854272L, 8796160131080L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());

    private static final long[] mk_tokenSet_2()
    {
        long[] data = { 3038861697482887264L, 17248610641868L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());

    private static final long[] mk_tokenSet_3()
    {
        long[] data = { -6112161224428880030L, 17592016136191L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());

    private static final long[] mk_tokenSet_4()
    {
        long[] data = { -9223089453776502784L, 51L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());

    private static final long[] mk_tokenSet_5()
    {
        long[] data = { -9223089453776502784L, 8796093022259L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());

    private static final long[] mk_tokenSet_6()
    {
        long[] data = { 721737660313370624L, 8796093022216L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());

    private static final long[] mk_tokenSet_7()
    {
        long[] data = { 72057598332894978L, 549734318080L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());

    private static final long[] mk_tokenSet_8()
    {
        long[] data = { -8934859077624791040L, 51L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());

    private static final long[] mk_tokenSet_9()
    {
        long[] data = { -8935141652113129472L, 51L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());

    private static final long[] mk_tokenSet_10()
    {
        long[] data = { -8935141652113129472L, 8796093022259L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());

    private static final long[] mk_tokenSet_11()
    {
        long[] data = { 721737668903305216L, 8796093038602L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());

    private static final long[] mk_tokenSet_12()
    {
        long[] data = { 721737671050788864L, 8796160147466L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());

    private static final long[] mk_tokenSet_13()
    {
        long[] data = { 722019135290081280L, 8796093022234L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());

    private static final long[] mk_tokenSet_14()
    {
        long[] data = { 722019137437564928L, 8796160131098L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());

    private static final long[] mk_tokenSet_15()
    {
        long[] data = { -9223372036854775808L, 8796093022211L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());

    private static final long[] mk_tokenSet_16()
    {
        long[] data = { -9223372036854775808L, 8796109799427L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());

    private static final long[] mk_tokenSet_17()
    {
        long[] data = { -7348431388367912960L, 8796093038907L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());

    private static final long[] mk_tokenSet_18()
    {
        long[] data = { -7348431386220429312L, 8796160147771L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

    private static final long[] mk_tokenSet_19()
    {
        long[] data = { -8501352901564694528L, 8796093059099L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());

    private static final long[] mk_tokenSet_20()
    {
        long[] data = { -8501352899417210880L, 8796160167963L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());

    private static final long[] mk_tokenSet_21()
    {
        long[] data = { -6184227756293615520L, 17248619030527L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());

    private static final long[] mk_tokenSet_22()
    {
        long[] data = { -6184227756293615520L, 17248610641919L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());

    private static final long[] mk_tokenSet_23()
    {
        long[] data = new long[8];
        data[0] = -16L;
        data[1] = 1152921504594264063L;
        for (int i = 2; i <= 7; i++)
            {
            data[i] = 0L;
        }
        return data;
    }
    public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());

    private static final long[] mk_tokenSet_24()
    {
        long[] data = { 3027580673822167136L, 17248605848136L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());

    private static final long[] mk_tokenSet_25()
    {
        long[] data = { 3099638272154926944L, 17591466607176L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());

    private static final long[] mk_tokenSet_26()
    {
        long[] data = { -6184148454017462174L, 17248619030527L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());

    private static final long[] mk_tokenSet_27()
    {
        long[] data = { -4670375750094815390L, 17592016173055L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());

    private static final long[] mk_tokenSet_28()
    {
        long[] data = { 3099638272154926944L, 17591468704328L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());

    private static final long[] mk_tokenSet_29()
    {
        long[] data = { 3027580673822167136L, 17249142719048L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());

    private static final long[] mk_tokenSet_30()
    {
        long[] data = { 3027580673822031968L, 15393179976264L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());

    private static final long[] mk_tokenSet_31()
    {
        long[] data = { 3027580677399404128L, 17248941392456L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());

    private static final long[] mk_tokenSet_32()
    {
        long[] data = { 3099638270725173602L, 17591913300552L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());

    private static final long[] mk_tokenSet_33()
    {
        long[] data = { 704643072L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());

    private static final long[] mk_tokenSet_34()
    {
        long[] data = { 8192L, 274878955520L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());

    private static final long[] mk_tokenSet_35()
    {
        long[] data = { 721737660313370624L, 8L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());

    private static final long[] mk_tokenSet_36()
    {
        long[] data = { 3027580673822167136L, 17248610042440L, 0L, 0L };
        return data;
    }
    public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());

}
