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

public class Options
{
    /* expression/parameter list */

    // uses basic indentation as base
    public static final int LIST_NORMAL_INDENTATION   = 0;
    // indents relative to owning expression start
    public static final int LIST_RELATIVE_INDENTATION = 1;
    // indents relative to the start of the first expr
    public static final int LIST_ALIGN_SUBSEQUENT     = 2;

    /* array creation */

    // the initializer starts on the same line
    public static final int ARRCREATION_NO_BREAK             = 0;
    // uses basic indentation as base for the initalizer
    public static final int ARRCREATION_NORMAL_INDENTATION   = 1;
    // indents the initalizer relative to start of creation expression
    public static final int ARRCREATION_CREATION_INDENTATION = 2;

    /* array initializer */

    // uses basic indentation as base for the sub initializers
    public static final int ARRINIT_NORMAL_INDENTATION      = 0;
    // indents sub initializers relative to the start of the current initializer
    public static final int ARRINIT_INITIALIZER_INDENTATION = 1;
    // indents sub initializers relative to the first sub initializer
    public static final int ARRINIT_ALIGN_SUB_INITIALIZERS  = 2;

    /* number literal */

    // number literals remain unchanged
    public static final int NUMLIT_NOCHANGE        = 0;
    // all letters (a-f,i,l,x for integers, d,e,f for floats) are lowercase
    public static final int NUMLIT_FORCE_LOWERCASE = 1;
    // all letters (a-f,i,l,x for integers, d,e,f for floats) are uppercase
    public static final int NUMLIT_FORCE_UPPERCASE = 2;

    /* labeled statement */

    // the inner statement is always put on the same line
    public static final int LBLDSTMT_FORCE_SAME_LINE       = 0;
    // if the label is short enough, put it on the same line,
    // otherwise put it on the next line, normally indented
    public static final int LBLDSTMT_SAME_LINE_IF_POSSIBLE = 1;
    // the inner statement is always put on the next line,
    // normally indented
    public static final int LBLDSTMT_FORCE_NEXT_LINE       = 2;

    /* type declaration options */

    // both 'extends' and 'implements' come on the same line (if possible)
    public static final int BASETYPES_SAME_LINE        = 0;
    // if both 'extends' and 'implements' are present, then put
    // 'implements' on the next line
    public static final int BASETYPES_SPLIT_INTERFACES = 1;
    // start a new line for the basetypes but do
    /* general options */

    // maximum allowed line length
    private int    _maxLineLength    = 80;
    // the column where eol comments start
    private int    _eolCommentColumn = 60;
    // the indentation token (one is used for each indentation level)
    private String _indentToken      = "    ";

    /* trailing primary options */

    // whether a complex dot-expression is broken at every dot if it
    // exceeds the allowed line length
    private boolean _breakEveryDotIfTooLong   = false;
    // whether before or after a dot is broken
    private boolean _breakBeforeDot           = true;
    // how much the following expression is indented after a dot break
    private int     _dotBreakIndentationLevel = 0;

    /* literal options */

    // whether multiple 'u' are reduced to one in unicode escapes
    private boolean _litSlimUnicode = true;
    // the case mode for number literals
    private int     _numLitCaseMode = NUMLIT_FORCE_LOWERCASE;
    // whether suffixes (d,f) are enforced
    private boolean _floatLitForceSuffix = false;
    // whether a dot is enforced (ie 1f -> 1.0f)
    private boolean _floatLitForceDot    = true;

    /* array expression (access/creation) options */

    // whether before or after the bracket is broken
    private boolean _arrExprBreakBeforeBracket = true;
    // whether index expressions are surrounded by spaces
    private boolean _arrExprSpacesAroundIndex  = false;
    // how much broken index expressions are indented
    private int     _arrExprIndentationLevel   = 0;
    // the break mode for the initializer
    private int     _arrCreationInitBreakMode  = ARRCREATION_CREATION_INDENTATION;

    /* array initializer options*/

    // whether a spaces comes after a comma
    private boolean _arrInitSpaceAfterComma     = true;
    // the indentation mode for broken sub initializers
    private int     _arrInitIndentationMode     = ARRINIT_ALIGN_SUB_INITIALIZERS;
    // the indentation level for broken sub initializers
    private int     _arrInitIndentationLevel    = 0;
    // whether all sub initializers should be broken if the initializer
    // does not fit in the line
    private boolean _arrInitBreakAllIfTooLong   = false;
    // if the initializer list has at least this number of sub initializers
    // then all sub initializers are broken (<= 0 disables the option)
    private int     _arrInitBreakLimit          = 4;

    /* argument list/statement expression list options */

    // whether a space comes after a comma
    private boolean _exprListSpaceAfterComma   = true;
    // the indentation mode for broken arguments
    private int     _exprListIndentationMode   = LIST_ALIGN_SUBSEQUENT;
    // the indentation level for broken arguments
    private int     _exprListIndentationLevel  = 0;
    // whether all expressions should be broken if the list does fit in the line
    private boolean _exprListBreakAllIfTooLong = true;
    // if the list has at least this number of expressions
    // then all are broken (<= 0 disables the option)
    private int     _exprListBreakLimit        = 5;
    // whether the list is surrounded by spaces
    private boolean _exprListSurroundBySpaces  = false;

    /* parameter list options */

    // whether a space comes after a comma
    private boolean _paramListSpaceAfterComma   = true;
    // the indentation mode for broken parameters
    private int     _paramListIndentationMode   = LIST_ALIGN_SUBSEQUENT;
    // the indentation level for broken parameters
    private int     _paramListIndentationLevel  = 0;
    // whether all parameters should be broken if the list does fit in the line
    private boolean _paramListBreakAllIfTooLong = true;
    // if the parameter list has at least this number of parameters
    // then all are broken (<= 0 disables the option)
    private int     _paramListBreakLimit        = 4;
    // whether the list is surrounded by spaces
    private boolean _paramListSurroundBySpaces  = false;
    // whether the names of parameters are aligned (only used if all params are broken)
    private boolean _paramListAlignNames        = true;

    /* other expression options */

    // whether a space comes before the opening parenthesis
    private boolean _invocSpaceBeforeParen = false;
    // whether the inner expression is surrouned by spaces
    private boolean _parenExprSpacesAroundInner  = false;
    // whether the cast type is surrouned by spaces
    private boolean _castSpacesAroundType        = false;
    // whether the closing parenthesis in a cast is followed by a space
    private boolean _castSpaceAfterParen         = false;
    // whether before or after the operator is broken
    private boolean _binExprBreakAfterOperator   = true;
    // whether the operator is surrouned by spaces
    private boolean _binExprSpacesAroundOperator = true;

    /* assignment expression options */

    // whether there are spaces around the assignment operator
    private boolean _assignExprSpacesAroundOperator = true;
    // whether broken value expressions are indented relative to the operator
    private boolean _assignExprRelativeIndentation  = true;
    // allows break at the assignment operator if the value expression
    // would fit (indented) on the next line
    private boolean _assignExprAllowOperatorBreak   = true;
    // whether the break is before or after the operator
    private boolean _assignExprBreakAfterOperator   = true;
    // the indentation level for the broken value expression
    // note that is used regardless of whether the indentation is relative
    private int     _assignExprIndentationLevel     = 0;
    // whether subsequent assignment expressions are aligned at the equal sign
    private boolean _assignExprAlignSubsequent      = true;

    /* conditional expression options */

    // whether there are spaces around the operators
    private boolean _condExprSpacesAroundOperators = true;
    // whether both the question mark and the colon are broken if the expression is too long
    private boolean _condExprBreakAllIfTooLong     = true;
    // whether after the question mark/colon is broken
    private boolean _condExprBreakAfterOperator    = true;
    // whether broken parts are indented relative to the start of the conditional expression
    private boolean _condExprRelativeIndentation   = true;
    // the indentation level for the broken parts
    // note that is used regardless of whether the indentation is relative
    private int     _condExprIndentationLevel      = 0;

    /* other statement options */

    // whether short blocks ('{}') are allowed
    private boolean _blockAllowShortForm        = true;
    // the indentation level for inner statements of a block
    private int     _blockIndentationLevel      = 1;
    // whether a broken return value is aligned to the start of the value
    private boolean _returnStmtAlignValue       = true;
    // the indentation level for a broken return value
    private int     _returnStmtIndentationLevel = 0;
    // whether a broken throw value is aligned to the start of the value
    private boolean _throwStmtAlignValue        = true;
    // the indentation level for a broken throw value
    private int     _throwStmtIndentationLevel  = 0;

    /* if statement options */

    // whether a space is inserted after the 'if'
    private boolean _ifStmtSpaceBeforeParen          = true;
    // the (relative) indentation level for broken conditions
    private int     _ifStmtConditionIndentationLevel = 0;
    // the indentation level for inner statements except block
    private int     _ifStmtIndentationLevel          = 1;
    // the indentation level for block as inner statement
    private int     _ifStmtBlockIndentationLevel     = 0;
    // whether inner block/empty statements can start on the same
    // line as the if/else
    private boolean _ifStmtInnerStartOnSameLine      = false;
    // whether the else can start on the same line as the preceeding
    // inner block/empty statements
    private boolean _ifStmtElseStartOnSameLine       = false;
    // whether if can start on the same line as the enclosing else
    private boolean _ifStmtAllowCompactElseIf        = true;

    /* synchronized statement options */

    // whether a space is inserted after the 'synchronized'
    private boolean _syncStmtSpaceBeforeParen          = true;
    // the (relative) indentation level for a broken value
    private int     _syncStmtConditionIndentationLevel = 0;
    // the indentation level for the inner block
    private int     _syncStmtBlockIndentationLevel     = 0;
    // whether the inner block statements can start on the same
    // line as the keyword
    private boolean _syncStmtInnerStartOnSameLine      = false;

    /* while statement options */

    // whether a space is inserted after the 'while'
    private boolean _whileStmtSpaceBeforeParen          = true;
    // the (relative) indentation level for broken conditions
    private int     _whileStmtConditionIndentationLevel = 0;
    // the indentation level for inner statements except block
    private int     _whileStmtIndentationLevel          = 1;
    // the indentation level for block as inner statement
    private int     _whileStmtBlockIndentationLevel     = 0;
    // whether inner block/empty statements can start on the same
    // line as the while keyword
    private boolean _whileStmtInnerStartOnSameLine      = false;

    /* do/while statement options */

    // whether a space is inserted after the 'while'
    private boolean _doStmtSpaceBeforeParen          = true;
    // the (relative) indentation level for broken conditions
    private int     _doStmtConditionIndentationLevel = 0;
    // the indentation level for inner statements except block
    private int     _doStmtIndentationLevel          = 1;
    // the indentation level for block as inner statement
    private int     _doStmtBlockIndentationLevel     = 0;
    // whether inner block/empty statements can start on the same
    // line as the do
    private boolean _doStmtInnerStartOnSameLine      = false;
    // whether the while can start on the same line as the preceeding
    // inner block/empty statements
    private boolean _doStmtWhileStartOnSameLine      = false;

    /* switch statement options */

    // whether a space is inserted after the 'switch'
    private boolean _switchStmtSpaceBeforeParen          = true;
    // the (relative) indentation level for broken conditions
    private int     _switchStmtConditionIndentationLevel = 0;
    // whether the inner block starts on the same line as the keyword
    private boolean _switchStmtInnerStartOnSameLine      = false;
    // whether a short inner block ('{}') is allowed
    private boolean _switchStmtAllowShortForm            = true;
    // the indentation level for the case blocks
    private int     _switchStmtCaseIndentationLevel      = 1;
    // whether a space is before the colon in a case block
    private boolean _switchStmtSpaceBeforeColon          = true;
    // the indentation level for the statements in the case blocks
    private int     _switchStmtCaseStmtsIndentationLevel = 1;

    /* try statement options */

    // the indentation level for the inner blocks
    private int     _tryStmtIndentationLevel          = 0;
    // whether the inner blocks start on the same line as the keywords
    private boolean _tryStmtInnerStartOnSameLine      = false;
    // whether the 'catch'/'finally' starts on the same line as the
    // preceeding inner block
    private boolean _tryStmtCatchStartOnSameLine      = false;
    // whether a space is inserted after the 'catch'
    private boolean _tryStmtSpaceBeforeParen          = true;
    // whether a space around the catch parameter
    private boolean _tryStmtSpaceAroundParam          = false;

    /* labeled statement options */

    // whether the label is indented or starts as pos 0
    private boolean _lbldStmtIndentLabel      = false;
    // indentation level of the label (relative or absolute depending on the
    // previous option); note that it can be negative to deindent the label
    private int     _lbldStmtIndentationLevel = 0;
    // whether a space is inserted before the colon
    private boolean _lbldStmtSpaceBeforeColon = false;
    // the positioning mode for the inner statement
    private int     _lbldStmtPositioningMode  = LBLDSTMT_SAME_LINE_IF_POSSIBLE;

    /* variable declaration (local/field) options */

    // whether there are spaces around the assignment operator
    private boolean _varDeclSpacesAroundOperator      = true;
    // whether a broken intializer are indented relative to the operator
    private boolean _varDeclRelativeIndentation       = true;
    // the indentation level for the broken initializer
    // note that is used regardless of whether the indentation is relative
    private int     _varDeclIndentationLevel          = 0;
    // whether subsequent declarations are combined if they have
    // the same base type (is forced for 'for' statements)
    private boolean _localVarDeclCombineSubsequent         = false;
    // whether subsequent declarations are aligned at the name
    private boolean _localVarDeclAlignSubsequent           = true;
    // whether a declaration block is surrounded by empty lines
    private boolean _localVarDeclSurroundBlockByEmptyLines = true;
    // whether subsequent field declarations are combined if they have
    // the same base type
    private boolean _fieldDeclCombineSubsequent         = false;
    // whether subsequent field declarations are aligned at the name
    private boolean _fieldDeclAlignSubsequent           = false;

    /* for statement options */

    // whether a space is inserted after the 'for'
    private boolean _forStmtSpaceBeforeParen           = true;
    // whether a space is inserted after each semicolon
    private boolean _forStmtSpaceAfterSemicolon        = true;
    // whether a space is inserted after the 'for'
    private boolean _forStmtBreakAllPartsIfHeadTooLong = true;
    // the (relative) indentation level for broken conditions
    private int     _forStmtConditionIndentationLevel  = 0;
    // the indentation level for inner statements except block
    private int     _forStmtIndentationLevel           = 1;
    // the indentation level for block as inner statement
    private int     _forStmtBlockIndentationLevel      = 0;
    // whether inner block/empty statements can start on the same
    // line as the while keyword
    private boolean _forStmtInnerStartOnSameLine       = false;

    /* invocable declarations (method, constructor) */

    // whether a space is before the opening parenthesis
    private boolean _invocDeclSpaceBeforeParen          = false;
    // whether the throws clause is always on the next line
    private boolean _invocDeclThrowsOnNextLine          = false;
    // indentation level for throws clause if on next line
    // (relative to beginning of invocable declaration)
    private int     _invocDeclThrowsIndentationLevel    = 1;
    // whether each exception is on its own line
    private boolean _invocDeclOneLinePerException       = true;
    // whether the exception types are aligned to each other
    // or normally indented
    private boolean _invocDeclAlignExceptions           = true;
    // the indentation level for broken exceptions (relative
    // either to the alignment position or to the start of the
    // declaration regardless of whether the throws is broken or not)
    private int     _invocDeclExceptionIndentationLevel = 0;
    // whether the body (if any) is started on the same line
    private boolean _invocDeclBodyStartOnSameLine       = false;

    /* initializer options */

    // whether the block starts on the same line as the static keyword
    private boolean _initializerBlockOnSameLine = false;

    /* type declaration options */

    // whether 'extends' is always on a new line
    private boolean _typeDeclExtendsOnNewLine             = false;
    // whether an 'implements' without an 'extends' is always on a new line
    private boolean _typeDeclImplementsOnNewLine          = false;
    // whether all base type names are broken
    private boolean _typeDeclBreakAllBasetypes            = true;
    // whether base type names are aligned (from both 'extends' and
    // 'implements'
    private boolean _typeDeclAlignBasetypes               = true;
    // the indentation level for broken 'extends'/'implements' blocks
    private int     _typeDeclBasetypeIndentationLevel     = 1;
    // whether the opening brace of the body starts on the same line
    private boolean _typeDeclBodyStartOnSameLine          = false;
    // indentation level for features
    private int     _typeDeclIndentationLevel             = 1;
    // number of empty lines before a block of features
    // if not first in type declaration
    private int     _typeDeclEmptyLinesBeforeFeatureBlock = 2;
    // number of empty lines between features within a block
    private int     _typeDeclEmptyLinesBetweenFeatures    = 1;
    // whether the opening brace of the an anonymous class body
    // starts on the same line
    private boolean _anonClassDeclBodyStartOnSameLine     = false;
    // indentation level for the anonymous class body
    private int     _anonClassDeclIndentationLevel        = 0;
    // whether a local class should be surrounded by empty lines
    private boolean _localClassDeclSurroundByEmptyLines   = true;

    /* compilation unit options */

    // number of lines after unit comment
    private int _compUnitNumLinesAfterComment = 1;
    // number of lines after package statement
    private int _compUnitNumLinesAfterPackage = 1;
    // number of lines after import block
    private int _compUnitNumLinesAfterImports = 1;
    // number of lines between top-level types
    private int _compUnitNumLinesBetweenTypes = 2;

    public void doAnonClassDeclBodyStartOnSameLine(boolean doIt)
    {
        _anonClassDeclBodyStartOnSameLine = doIt;
    }



    public void doArrExprBreakBeforeBracket(boolean doIt)
    {
        _arrExprBreakBeforeBracket = doIt;
    }



    public void doArrExprSpacesAroundIndex(boolean doIt)
    {
        _arrExprSpacesAroundIndex = doIt;
    }



    public void doArrInitBreakAllIfTooLong(boolean doIt)
    {
        _arrInitBreakAllIfTooLong = doIt;
    }



    public void doArrInitSpaceAfterComma(boolean doIt)
    {
        _arrInitSpaceAfterComma = doIt;
    }



    public void doAssignExprAlignSubsequent(boolean doIt)
    {
        _assignExprAlignSubsequent = doIt;
    }



    public void doAssignExprAllowOperatorBreak(boolean allowIt)
    {
        _assignExprAllowOperatorBreak = allowIt;
    }



    public void doAssignExprBreakAfterOperator(boolean doIt)
    {
        _assignExprBreakAfterOperator = doIt;
    }



    public void doAssignExprRelativeIndentation(boolean doIt)
    {
        _assignExprRelativeIndentation = doIt;
    }



    public void doAssignExprSpacesAroundOperator(boolean doIt)
    {
        _assignExprSpacesAroundOperator = doIt;
    }



    public void doBinExprBreakAfterOperator(boolean doIt)
    {
        _binExprBreakAfterOperator = doIt;
    }



    public void doBinExprSpacesAroundOperator(boolean doIt)
    {
        _binExprSpacesAroundOperator = doIt;
    }



    public void doBlockAllowShortForm(boolean doIt)
    {
        _blockAllowShortForm = doIt;
    }



    public void doBreakBeforeDot(boolean doIt)
    {
        _breakBeforeDot = doIt;
    }



    public void doBreakEveryDotIfTooLong(boolean doIt)
    {
        _breakEveryDotIfTooLong = doIt;
    }



    public void doCastSpaceAfterParen(boolean doIt)
    {
        _castSpaceAfterParen = doIt;
    }



    public void doCastSpacesAroundType(boolean doIt)
    {
        _castSpacesAroundType = doIt;
    }



    public void doCondExprBreakAfterOperator(boolean doIt)
    {
        _condExprBreakAfterOperator = doIt;
    }



    public void doCondExprBreakAllIfTooLong(boolean doIt)
    {
        _condExprBreakAllIfTooLong = doIt;
    }



    public void doCondExprRelativeIndentation(boolean doIt)
    {
        _condExprRelativeIndentation = doIt;
    }



    public void doCondExprSpacesAroundOperators(boolean doIt)
    {
        _condExprSpacesAroundOperators = doIt;
    }



    public void doDoStmtInnerStartOnSameLine(boolean doIt)
    {
        _doStmtInnerStartOnSameLine = doIt;
    }



    public void doDoStmtSpaceBeforeParen(boolean doIt)
    {
        _doStmtSpaceBeforeParen = doIt;
    }



    public void doDoStmtWhileStartOnSameLine(boolean doIt)
    {
        _doStmtWhileStartOnSameLine = doIt;
    }



    public void doExprListBreakAllIfTooLong(boolean doIt)
    {
        _exprListBreakAllIfTooLong = doIt;
    }



    public void doExprListSpaceAfterComma(boolean doIt)
    {
        _exprListSpaceAfterComma = doIt;
    }



    public void doExprListSurroundBySpaces(boolean doIt)
    {
        _exprListSurroundBySpaces = doIt;
    }



    public void doFieldDeclAlignSubsequent(boolean doIt)
    {
        _fieldDeclAlignSubsequent = doIt;
    }



    public void doFieldDeclCombineSubsequent(boolean doIt)
    {
        _fieldDeclCombineSubsequent = doIt;
    }



    public void doFloatLitForceDot(boolean forceIt)
    {
        _floatLitForceDot = forceIt;
    }



    public void doFloatLitForceSuffix(boolean forceIt)
    {
        _floatLitForceSuffix = forceIt;
    }



    public void doForStmtBreakAllPartsIfHeadTooLong(boolean doIt)
    {
        _forStmtBreakAllPartsIfHeadTooLong = doIt;
    }



    public void doForStmtInnerStartOnSameLine(boolean doIt)
    {
        _forStmtInnerStartOnSameLine = doIt;
    }



    public void doForStmtSpaceAfterSemicolon(boolean doIt)
    {
        _forStmtSpaceAfterSemicolon = doIt;
    }



    public void doForStmtSpaceBeforeParen(boolean doIt)
    {
        _forStmtSpaceBeforeParen = doIt;
    }



    public void doIfStmtAllowCompactElseIf(boolean doIt)
    {
        _ifStmtAllowCompactElseIf = doIt;
    }



    public void doIfStmtElseStartOnSameLine(boolean doIt)
    {
        _ifStmtElseStartOnSameLine = doIt;
    }



    public void doIfStmtInnerStartOnSameLine(boolean doIt)
    {
        _ifStmtInnerStartOnSameLine = doIt;
    }



    public void doIfStmtSpaceBeforeParen(boolean doIt)
    {
        _ifStmtSpaceBeforeParen = doIt;
    }



    public void doInitializerBlockOnSameLine(boolean doIt)
    {
        _initializerBlockOnSameLine = doIt;
    }



    public void doInvocDeclAlignExceptions(boolean doIt)
    {
        _invocDeclAlignExceptions = doIt;
    }



    public void doInvocDeclBodyStartOnSameLine(boolean doIt)
    {
        _invocDeclBodyStartOnSameLine = doIt;
    }



    public void doInvocDeclOneLinePerException(boolean doIt)
    {
        _invocDeclOneLinePerException = doIt;
    }



    public void doInvocDeclSpaceBeforeParen(boolean doIt)
    {
        _invocDeclSpaceBeforeParen = doIt;
    }



    public void doInvocDeclThrowsOnNextLine(boolean doIt)
    {
        _invocDeclThrowsOnNextLine = doIt;
    }



    public void doInvocSpaceBeforeParen(boolean doIt)
    {
        _invocSpaceBeforeParen = doIt;
    }



    public void doLbldStmtIndentLabel(boolean doIt)
    {
        _lbldStmtIndentLabel = doIt;
    }



    public void doLbldStmtSpaceBeforeColon(boolean doIt)
    {
        _lbldStmtSpaceBeforeColon = doIt;
    }



    public void doLitSlimUnicode(boolean doIt)
    {
        _litSlimUnicode = doIt;
    }



    public void doLocalClassDeclSurroundByEmptyLines(boolean doIt)
    {
        _localClassDeclSurroundByEmptyLines = doIt;
    }



    public void doLocalVarDeclAlignSubsequent(boolean doIt)
    {
        _localVarDeclAlignSubsequent = doIt;
    }



    public void doLocalVarDeclCombineSubsequent(boolean doIt)
    {
        _localVarDeclCombineSubsequent = doIt;
    }



    public void doLocalVarDeclSurroundBlockByEmptyLines(boolean doIt)
    {
        _localVarDeclSurroundBlockByEmptyLines = doIt;
    }



    public void doParamListAlignNames(boolean doIt)
    {
        _paramListAlignNames = doIt;
    }



    public void doParamListBreakAllIfTooLong(boolean doIt)
    {
        _paramListBreakAllIfTooLong = doIt;
    }



    public void doParamListSpaceAfterComma(boolean doIt)
    {
        _paramListSpaceAfterComma = doIt;
    }



    public void doParamListSurroundBySpaces(boolean doIt)
    {
        _paramListSurroundBySpaces = doIt;
    }



    public void doParenExprSpacesAroundInner(boolean doIt)
    {
        _parenExprSpacesAroundInner = doIt;
    }



    public void doReturnStmtAlignValue(boolean doIt)
    {
        _returnStmtAlignValue = doIt;
    }



    public void doSwitchStmtAllowShortForm(boolean doIt)
    {
        _switchStmtAllowShortForm = doIt;
    }



    public void doSwitchStmtInnerStartOnSameLine(boolean doIt)
    {
        _switchStmtInnerStartOnSameLine = doIt;
    }



    public void doSwitchStmtSpaceBeforeColon(boolean doIt)
    {
        _switchStmtSpaceBeforeColon = doIt;
    }



    public void doSwitchStmtSpaceBeforeParen(boolean doIt)
    {
        _switchStmtSpaceBeforeParen = doIt;
    }



    public void doSyncStmtInnerStartOnSameLine(boolean doIt)
    {
        _syncStmtInnerStartOnSameLine = doIt;
    }



    public void doSyncStmtSpaceBeforeParen(boolean doIt)
    {
        _syncStmtSpaceBeforeParen = doIt;
    }



    public void doThrowStmtAlignValue(boolean doIt)
    {
        _throwStmtAlignValue = doIt;
    }



    public void doTryStmtCatchStartOnSameLine(boolean doIt)
    {
        _tryStmtCatchStartOnSameLine = doIt;
    }



    public void doTryStmtInnerStartOnSameLine(boolean doIt)
    {
        _tryStmtInnerStartOnSameLine = doIt;
    }



    public void doTryStmtSpaceAroundParam(boolean doIt)
    {
        _tryStmtSpaceAroundParam = doIt;
    }



    public void doTryStmtSpaceBeforeParen(boolean doIt)
    {
        _tryStmtSpaceBeforeParen = doIt;
    }



    public void doTypeDeclAlignBasetypes(boolean doIt)
    {
        _typeDeclAlignBasetypes = doIt;
    }



    public void doTypeDeclBodyStartOnSameLine(boolean doIt)
    {
        _typeDeclBodyStartOnSameLine = doIt;
    }



    public void doTypeDeclBreakAllBasetypes(boolean doIt)
    {
        _typeDeclBreakAllBasetypes = doIt;
    }



    public void doTypeDeclExtendsOnNewLine(boolean doIt)
    {
        _typeDeclExtendsOnNewLine = doIt;
    }



    public void doTypeDeclImplementsOnNewLine(boolean doIt)
    {
        _typeDeclImplementsOnNewLine = doIt;
    }



    public void doVarDeclRelativeIndentation(boolean doIt)
    {
        _varDeclRelativeIndentation = doIt;
    }



    public void doVarDeclSpacesAroundOperator(boolean doIt)
    {
        _varDeclSpacesAroundOperator = doIt;
    }



    public void doWhileStmtInnerStartOnSameLine(boolean doIt)
    {
        _whileStmtInnerStartOnSameLine = doIt;
    }



    public void doWhileStmtSpaceBeforeParen(boolean doIt)
    {
        _whileStmtSpaceBeforeParen = doIt;
    }



    public int getAnonClassDeclIndentationLevel()
    {
        return _anonClassDeclIndentationLevel;
    }



    public int getArrCreationInitBreakMode()
    {
        return _arrCreationInitBreakMode;
    }



    public int getArrExprIndentationLevel()
    {
        return _arrExprIndentationLevel;
    }



    public int getArrInitBreakLimit()
    {
        return _arrInitBreakLimit;
    }



    public int getArrInitIndentationLevel()
    {
        return _arrInitIndentationLevel;
    }



    public int getArrInitIndentationMode()
    {
        return _arrInitIndentationMode;
    }



    public int getAssignExprIndentationLevel()
    {
        return _assignExprIndentationLevel;
    }



    public int getBlockIndentationLevel()
    {
        return _blockIndentationLevel;
    }



    public int getCompUnitNumLinesAfterComment()
    {
        return _compUnitNumLinesAfterComment;
    }



    public int getCompUnitNumLinesAfterImports()
    {
        return _compUnitNumLinesAfterImports;
    }



    public int getCompUnitNumLinesAfterPackage()
    {
        return _compUnitNumLinesAfterPackage;
    }



    public int getCompUnitNumLinesBetweenTypes()
    {
        return _compUnitNumLinesBetweenTypes;
    }



    public int getCondExprIndentationLevel()
    {
        return _condExprIndentationLevel;
    }



    public int getDoStmtBlockIndentationLevel()
    {
        return _doStmtBlockIndentationLevel;
    }



    public int getDoStmtConditionIndentationLevel()
    {
        return _doStmtConditionIndentationLevel;
    }



    public int getDoStmtIndentationLevel()
    {
        return _doStmtIndentationLevel;
    }



    public int getDotBreakIndentationLevel()
    {
        return _dotBreakIndentationLevel;
    }



    public int getEolCommentColumn()
    {
        return _eolCommentColumn;
    }



    public int getExprListBreakLimit()
    {
        return _exprListBreakLimit;
    }



    public int getExprListIndentationLevel()
    {
        return _exprListIndentationLevel;
    }



    public int getExprListIndentationMode()
    {
        return _exprListIndentationMode;
    }



    public int getForStmtBlockIndentationLevel()
    {
        return _forStmtBlockIndentationLevel;
    }



    public int getForStmtConditionIndentationLevel()
    {
        return _forStmtConditionIndentationLevel;
    }



    public int getForStmtIndentationLevel()
    {
        return _forStmtIndentationLevel;
    }



    public int getIfStmtBlockIndentationLevel()
    {
        return _ifStmtBlockIndentationLevel;
    }



    public int getIfStmtConditionIndentationLevel()
    {
        return _ifStmtConditionIndentationLevel;
    }



    public int getIfStmtIndentationLevel()
    {
        return _ifStmtIndentationLevel;
    }



    public String getIndentToken()
    {
        return _indentToken;
    }



    public int getInvocDeclExceptionIndentationLevel()
    {
        return _invocDeclExceptionIndentationLevel;
    }



    public int getInvocDeclThrowsIndentationLevel()
    {
        return _invocDeclThrowsIndentationLevel;
    }



    public int getLbldStmtIndentationLevel()
    {
        return _lbldStmtIndentationLevel;
    }



    public int getLbldStmtPositioningMode()
    {
        return _lbldStmtPositioningMode;
    }



    public int getMaxLineLength()
    {
        return _maxLineLength;
    }



    public int getNumLitCaseMode()
    {
        return _numLitCaseMode;
    }



    public int getParamListBreakLimit()
    {
        return _paramListBreakLimit;
    }



    public int getParamListIndentationLevel()
    {
        return _paramListIndentationLevel;
    }



    public int getParamListIndentationMode()
    {
        return _paramListIndentationMode;
    }



    public int getReturnStmtIndentationLevel()
    {
        return _returnStmtIndentationLevel;
    }



    public int getSwitchStmtCaseIndentationLevel()
    {
        return _switchStmtCaseIndentationLevel;
    }



    public int getSwitchStmtCaseStmtsIndentationLevel()
    {
        return _switchStmtCaseStmtsIndentationLevel;
    }



    public int getSwitchStmtConditionIndentationLevel()
    {
        return _switchStmtConditionIndentationLevel;
    }



    public int getSyncStmtBlockIndentationLevel()
    {
        return _syncStmtBlockIndentationLevel;
    }



    public int getSyncStmtConditionIndentationLevel()
    {
        return _syncStmtConditionIndentationLevel;
    }



    public int getThrowStmtIndentationLevel()
    {
        return _throwStmtIndentationLevel;
    }



    public int getTryStmtIndentationLevel()
    {
        return _tryStmtIndentationLevel;
    }



    public int getTypeDeclBasetypeIndentationLevel()
    {
        return _typeDeclBasetypeIndentationLevel;
    }



    public int getTypeDeclEmptyLinesBeforeFeatureBlock()
    {
        return _typeDeclEmptyLinesBeforeFeatureBlock;
    }



    public int getTypeDeclEmptyLinesBetweenFeatures()
    {
        return _typeDeclEmptyLinesBetweenFeatures;
    }



    public int getTypeDeclIndentationLevel()
    {
        return _typeDeclIndentationLevel;
    }



    public int getVarDeclIndentationLevel()
    {
        return _varDeclIndentationLevel;
    }



    public int getWhileStmtBlockIndentationLevel()
    {
        return _whileStmtBlockIndentationLevel;
    }



    public int getWhileStmtConditionIndentationLevel()
    {
        return _whileStmtConditionIndentationLevel;
    }



    public int getWhileStmtIndentationLevel()
    {
        return _whileStmtIndentationLevel;
    }



    public boolean isAnonClassDeclBodyStartOnSameLine()
    {
        return _anonClassDeclBodyStartOnSameLine;
    }



    public boolean isArrExprBreakBeforeBracket()
    {
        return _arrExprBreakBeforeBracket;
    }



    public boolean isArrExprSpacesAroundIndex()
    {
        return _arrExprSpacesAroundIndex;
    }



    public boolean isArrInitBreakAllIfTooLong()
    {
        return _arrInitBreakAllIfTooLong;
    }



    public boolean isArrInitSpaceAfterComma()
    {
        return _arrInitSpaceAfterComma;
    }



    public boolean isAssignExprAlignSubsequent()
    {
        return _assignExprAlignSubsequent;
    }



    public boolean isAssignExprAllowOperatorBreak()
    {
        return _assignExprAllowOperatorBreak;
    }



    public boolean isAssignExprBreakAfterOperator()
    {
        return _assignExprBreakAfterOperator;
    }



    public boolean isAssignExprRelativeIndentation()
    {
        return _assignExprRelativeIndentation;
    }



    public boolean isAssignExprSpacesAroundOperator()
    {
        return _assignExprSpacesAroundOperator;
    }



    public boolean isBinExprBreakAfterOperator()
    {
        return _binExprBreakAfterOperator;
    }



    public boolean isBinExprSpacesAroundOperator()
    {
        return _binExprSpacesAroundOperator;
    }



    public boolean isBlockAllowShortForm()
    {
        return _blockAllowShortForm;
    }



    public boolean isBreakBeforeDot()
    {
        return _breakBeforeDot;
    }



    public boolean isBreakEveryDotIfTooLong()
    {
        return _breakEveryDotIfTooLong;
    }



    public boolean isCastSpaceAfterParen()
    {
        return _castSpaceAfterParen;
    }



    public boolean isCastSpacesAroundType()
    {
        return _castSpacesAroundType;
    }



    public boolean isCondExprBreakAfterOperator()
    {
        return _condExprBreakAfterOperator;
    }



    public boolean isCondExprBreakAllIfTooLong()
    {
        return _condExprBreakAllIfTooLong;
    }



    public boolean isCondExprRelativeIndentation()
    {
        return _condExprRelativeIndentation;
    }



    public boolean isCondExprSpacesAroundOperators()
    {
        return _condExprSpacesAroundOperators;
    }



    public boolean isDoStmtInnerStartOnSameLine()
    {
        return _doStmtInnerStartOnSameLine;
    }



    public boolean isDoStmtSpaceBeforeParen()
    {
        return _doStmtSpaceBeforeParen;
    }



    public boolean isDoStmtWhileStartOnSameLine()
    {
        return _doStmtWhileStartOnSameLine;
    }



    public boolean isExprListBreakAllIfTooLong()
    {
        return _exprListBreakAllIfTooLong;
    }



    public boolean isExprListSpaceAfterComma()
    {
        return _exprListSpaceAfterComma;
    }



    public boolean isExprListSurroundBySpaces()
    {
        return _exprListSurroundBySpaces;
    }



    public boolean isFieldDeclAlignSubsequent()
    {
        return _fieldDeclAlignSubsequent;
    }



    public boolean isFieldDeclCombineSubsequent()
    {
        return _fieldDeclCombineSubsequent;
    }



    public boolean isFloatLitForceDot()
    {
        return _floatLitForceDot;
    }



    public boolean isFloatLitForceSuffix()
    {
        return _floatLitForceSuffix;
    }



    public boolean isForStmtBreakAllPartsIfHeadTooLong()
    {
        return _forStmtBreakAllPartsIfHeadTooLong;
    }



    public boolean isForStmtInnerStartOnSameLine()
    {
        return _forStmtInnerStartOnSameLine;
    }



    public boolean isForStmtSpaceAfterSemicolon()
    {
        return _forStmtSpaceAfterSemicolon;
    }



    public boolean isForStmtSpaceBeforeParen()
    {
        return _forStmtSpaceBeforeParen;
    }



    public boolean isIfStmtAllowCompactElseIf()
    {
        return _ifStmtAllowCompactElseIf;
    }



    public boolean isIfStmtElseStartOnSameLine()
    {
        return _ifStmtElseStartOnSameLine;
    }



    public boolean isIfStmtInnerStartOnSameLine()
    {
        return _ifStmtInnerStartOnSameLine;
    }



    public boolean isIfStmtSpaceBeforeParen()
    {
        return _ifStmtSpaceBeforeParen;
    }



    public boolean isInitializerBlockOnSameLine()
    {
        return _initializerBlockOnSameLine;
    }



    public boolean isInvocDeclAlignExceptions()
    {
        return _invocDeclAlignExceptions;
    }



    public boolean isInvocDeclBodyStartOnSameLine()
    {
        return _invocDeclBodyStartOnSameLine;
    }



    public boolean isInvocDeclOneLinePerException()
    {
        return _invocDeclOneLinePerException;
    }



    public boolean isInvocDeclSpaceBeforeParen()
    {
        return _invocDeclSpaceBeforeParen;
    }



    public boolean isInvocDeclThrowsOnNextLine()
    {
        return _invocDeclThrowsOnNextLine;
    }



    public boolean isInvocSpaceBeforeParen()
    {
        return _invocSpaceBeforeParen;
    }



    public boolean isLbldStmtIndentLabel()
    {
        return _lbldStmtIndentLabel;
    }



    public boolean isLbldStmtSpaceBeforeColon()
    {
        return _lbldStmtSpaceBeforeColon;
    }



    public boolean isLitSlimUnicode()
    {
        return _litSlimUnicode;
    }



    public boolean isLocalClassDeclSurroundByEmptyLines()
    {
        return _localClassDeclSurroundByEmptyLines;
    }



    public boolean isLocalVarDeclAlignSubsequent()
    {
        return _localVarDeclAlignSubsequent;
    }



    public boolean isLocalVarDeclCombineSubsequent()
    {
        return _localVarDeclCombineSubsequent;
    }



    public boolean isLocalVarDeclSurroundBlockByEmptyLines()
    {
        return _localVarDeclSurroundBlockByEmptyLines;
    }



    public boolean isParamListAlignNames()
    {
        return _paramListAlignNames;
    }



    public boolean isParamListBreakAllIfTooLong()
    {
        return _paramListBreakAllIfTooLong;
    }



    public boolean isParamListSpaceAfterComma()
    {
        return _paramListSpaceAfterComma;
    }



    public boolean isParamListSurroundBySpaces()
    {
        return _paramListSurroundBySpaces;
    }



    public boolean isParenExprSpacesAroundInner()
    {
        return _parenExprSpacesAroundInner;
    }



    public boolean isReturnStmtAlignValue()
    {
        return _returnStmtAlignValue;
    }



    public boolean isSwitchStmtAllowShortForm()
    {
        return _switchStmtAllowShortForm;
    }



    public boolean isSwitchStmtInnerStartOnSameLine()
    {
        return _switchStmtInnerStartOnSameLine;
    }



    public boolean isSwitchStmtSpaceBeforeColon()
    {
        return _switchStmtSpaceBeforeColon;
    }



    public boolean isSwitchStmtSpaceBeforeParen()
    {
        return _switchStmtSpaceBeforeParen;
    }



    public boolean isSyncStmtInnerStartOnSameLine()
    {
        return _syncStmtInnerStartOnSameLine;
    }



    public boolean isSyncStmtSpaceBeforeParen()
    {
        return _syncStmtSpaceBeforeParen;
    }



    public boolean isThrowStmtAlignValue()
    {
        return _throwStmtAlignValue;
    }



    public boolean isTryStmtCatchStartOnSameLine()
    {
        return _tryStmtCatchStartOnSameLine;
    }



    public boolean isTryStmtInnerStartOnSameLine()
    {
        return _tryStmtInnerStartOnSameLine;
    }



    public boolean isTryStmtSpaceAroundParam()
    {
        return _tryStmtSpaceAroundParam;
    }



    public boolean isTryStmtSpaceBeforeParen()
    {
        return _tryStmtSpaceBeforeParen;
    }



    public boolean isTypeDeclAlignBasetypes()
    {
        return _typeDeclAlignBasetypes;
    }



    public boolean isTypeDeclBodyStartOnSameLine()
    {
        return _typeDeclBodyStartOnSameLine;
    }



    public boolean isTypeDeclBreakAllBasetypes()
    {
        return _typeDeclBreakAllBasetypes;
    }



    public boolean isTypeDeclExtendsOnNewLine()
    {
        return _typeDeclExtendsOnNewLine;
    }



    public boolean isTypeDeclImplementsOnNewLine()
    {
        return _typeDeclImplementsOnNewLine;
    }



    public boolean isVarDeclRelativeIndentation()
    {
        return _varDeclRelativeIndentation;
    }



    public boolean isVarDeclSpacesAroundOperator()
    {
        return _varDeclSpacesAroundOperator;
    }



    public boolean isWhileStmtInnerStartOnSameLine()
    {
        return _whileStmtInnerStartOnSameLine;
    }



    public boolean isWhileStmtSpaceBeforeParen()
    {
        return _whileStmtSpaceBeforeParen;
    }



    public void setAnonClassDeclIndentationLevel(int anonClassDeclIndentationLevel)
    {
        _anonClassDeclIndentationLevel = anonClassDeclIndentationLevel;
    }



    public void setArrCreationInitBreakMode(int arrCreationInitBreakMode)
    {
        _arrCreationInitBreakMode = arrCreationInitBreakMode;
    }



    public void setArrExprIndentationLevel(int arrExprIndentationLevel)
    {
        _arrExprIndentationLevel = arrExprIndentationLevel;
    }



    public void setArrInitBreakLimit(int arrInitBreakLimit)
    {
        _arrInitBreakLimit = arrInitBreakLimit;
    }



    public void setArrInitIndentationLevel(int arrInitIndentationLevel)
    {
        _arrInitIndentationLevel = arrInitIndentationLevel;
    }



    public void setArrInitIndentationMode(int arrInitIndentationMode)
    {
        _arrInitIndentationMode = arrInitIndentationMode;
    }



    public void setAssignExprIndentationLevel(int assignExprIndentationLevel)
    {
        _assignExprIndentationLevel = assignExprIndentationLevel;
    }



    public void setBlockIndentationLevel(int blockIndentationLevel)
    {
        _blockIndentationLevel = blockIndentationLevel;
    }



    public void setCompUnitNumLinesAfterComment(int compUnitNumLinesAfterComment)
    {
        _compUnitNumLinesAfterComment = compUnitNumLinesAfterComment;
    }



    public void setCompUnitNumLinesAfterImports(int compUnitNumLinesAfterImports)
    {
        _compUnitNumLinesAfterImports = compUnitNumLinesAfterImports;
    }



    public void setCompUnitNumLinesAfterPackage(int compUnitNumLinesAfterPackage)
    {
        _compUnitNumLinesAfterPackage = compUnitNumLinesAfterPackage;
    }



    public void setCompUnitNumLinesBetweenTypes(int compUnitNumLinesBetweenTypes)
    {
        _compUnitNumLinesBetweenTypes = compUnitNumLinesBetweenTypes;
    }



    public void setCondExprIndentationLevel(int condExprIndentationLevel)
    {
        _condExprIndentationLevel = condExprIndentationLevel;
    }



    public void setDoStmtBlockIndentationLevel(int doStmtBlockIndentationLevel)
    {
        _doStmtBlockIndentationLevel = doStmtBlockIndentationLevel;
    }



    public void setDoStmtConditionIndentationLevel(int doStmtConditionIndentationLevel)
    {
        _doStmtConditionIndentationLevel = doStmtConditionIndentationLevel;
    }



    public void setDoStmtIndentationLevel(int doStmtIndentationLevel)
    {
        _doStmtIndentationLevel = doStmtIndentationLevel;
    }



    public void setDotBreakIndentationLevel(int dotBreakIndentationLevel)
    {
        _dotBreakIndentationLevel = dotBreakIndentationLevel > 0 ?
                                        dotBreakIndentationLevel :
                                        0;
    }



    public void setEolCommentColumn(int eolCommentColumn)
    {
        _eolCommentColumn = eolCommentColumn > 0 ?
                                eolCommentColumn :
                                0;
    }



    public void setExprListBreakLimit(int argListBreakLimit)
    {
        _exprListBreakLimit = argListBreakLimit;
    }



    public void setExprListIndentationLevel(int argListIndentationLevel)
    {
        _exprListIndentationLevel = argListIndentationLevel;
    }



    public void setExprListIndentationMode(int argListIndentationMode)
    {
        _exprListIndentationMode = argListIndentationMode;
    }



    public void setForStmtBlockIndentationLevel(int forStmtBlockIndentationLevel)
    {
        _forStmtBlockIndentationLevel = forStmtBlockIndentationLevel;
    }



    public void setForStmtConditionIndentationLevel(int forStmtConditionIndentationLevel)
    {
        _forStmtConditionIndentationLevel = forStmtConditionIndentationLevel;
    }



    public void setForStmtIndentationLevel(int forStmtIndentationLevel)
    {
        _forStmtIndentationLevel = forStmtIndentationLevel;
    }



    public void setIfStmtBlockIndentationLevel(int ifStmtBlockIndentationLevel)
    {
        _ifStmtBlockIndentationLevel = ifStmtBlockIndentationLevel;
    }



    public void setIfStmtConditionIndentationLevel(int ifStmtConditionIndentationLevel)
    {
        _ifStmtConditionIndentationLevel = ifStmtConditionIndentationLevel;
    }



    public void setIfStmtIndentationLevel(int ifStmtIndentationLevel)
    {
        _ifStmtIndentationLevel = ifStmtIndentationLevel;
    }



    public void setIndentToken(String indentToken)
    {
        _indentToken = indentToken;
    }



    public void setInvocDeclExceptionIndentationLevel(int invocDeclExceptionIndentationLevel)
    {
        _invocDeclExceptionIndentationLevel = invocDeclExceptionIndentationLevel;
    }



    public void setInvocDeclThrowsIndentationLevel(int invocDeclThrowsIndentationLevel)
    {
        _invocDeclThrowsIndentationLevel = invocDeclThrowsIndentationLevel;
    }



    public void setLbldStmtIndentationLevel(int lbldStmtIndentationLevel)
    {
        _lbldStmtIndentationLevel = lbldStmtIndentationLevel;
    }



    public void setLbldStmtPositioningMode(int lbldStmtPositioningMode)
    {
        _lbldStmtPositioningMode = lbldStmtPositioningMode;
    }



    public void setMaxLineLength(int maxLineLength)
    {
        _maxLineLength = maxLineLength > 0 ? maxLineLength : 0;
    }



    public void setNumLitCaseMode(int mode)
    {
        if ((mode >= NUMLIT_NOCHANGE) &&
            (mode <= NUMLIT_FORCE_UPPERCASE))
        {
            _numLitCaseMode = mode;
        }
    }



    public void setParamListBreakLimit(int paramListBreakLimit)
    {
        _paramListBreakLimit = paramListBreakLimit;
    }



    public void setParamListIndentationLevel(int paramListIndentationLevel)
    {
        _paramListIndentationLevel = paramListIndentationLevel;
    }



    public void setParamListIndentationMode(int paramListIndentationMode)
    {
        _paramListIndentationMode = paramListIndentationMode;
    }



    public void setReturnStmtIndentationLevel(int returnStmtIndentationLevel)
    {
        _returnStmtIndentationLevel = returnStmtIndentationLevel;
    }



    public void setSwitchStmtCaseIndentationLevel(int switchStmtCaseIndentationLevel)
    {
        _switchStmtCaseIndentationLevel = switchStmtCaseIndentationLevel;
    }



    public void setSwitchStmtCaseStmtsIndentationLevel(int switchStmtCaseStmtsIndentationLevel)
    {
        _switchStmtCaseStmtsIndentationLevel = switchStmtCaseStmtsIndentationLevel;
    }



    public void setSwitchStmtConditionIndentationLevel(int switchStmtConditionIndentationLevel)
    {
        _switchStmtConditionIndentationLevel = switchStmtConditionIndentationLevel;
    }



    public void setSyncStmtBlockIndentationLevel(int syncStmtBlockIndentationLevel)
    {
        _syncStmtBlockIndentationLevel = syncStmtBlockIndentationLevel;
    }



    public void setSyncStmtConditionIndentationLevel(int syncStmtConditionIndentationLevel)
    {
        _syncStmtConditionIndentationLevel = syncStmtConditionIndentationLevel;
    }



    public void setThrowStmtIndentationLevel(int throwStmtIndentationLevel)
    {
        _throwStmtIndentationLevel = throwStmtIndentationLevel;
    }



    public void setTryStmtIndentationLevel(int tryStmtIndentationLevel)
    {
        _tryStmtIndentationLevel = tryStmtIndentationLevel;
    }



    public void setTypeDeclBasetypeIndentationLevel(int typeDeclBasetypeIndentationLevel)
    {
        _typeDeclBasetypeIndentationLevel = typeDeclBasetypeIndentationLevel;
    }



    public void setTypeDeclEmptyLinesBeforeFeatureBlock(int typeDeclEmptyLinesBeforeFeatureBlock)
    {
        _typeDeclEmptyLinesBeforeFeatureBlock = typeDeclEmptyLinesBeforeFeatureBlock;
    }



    public void setTypeDeclEmptyLinesBetweenFeatures(int typeDeclEmptyLinesBetweenFeatures)
    {
        _typeDeclEmptyLinesBetweenFeatures = typeDeclEmptyLinesBetweenFeatures;
    }



    public void setTypeDeclIndentationLevel(int typeDeclIndentationLevel)
    {
        _typeDeclIndentationLevel = typeDeclIndentationLevel;
    }



    public void setVarDeclIndentationLevel(int varDeclIndentationLevel)
    {
        _varDeclIndentationLevel = varDeclIndentationLevel;
    }



    public void setWhileStmtBlockIndentationLevel(int whileStmtBlockIndentationLevel)
    {
        _whileStmtBlockIndentationLevel = whileStmtBlockIndentationLevel;
    }



    public void setWhileStmtConditionIndentationLevel(int whileStmtConditionIndentationLevel)
    {
        _whileStmtConditionIndentationLevel = whileStmtConditionIndentationLevel;
    }



    public void setWhileStmtIndentationLevel(int whileStmtIndentationLevel)
    {
        _whileStmtIndentationLevel = whileStmtIndentationLevel;
    }
}