// $ANTLR 2.7.2a2 (20020112-1): "parser.g" -> "JavaLexer.java"$

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


public interface JavaTokenTypes {
    int EOF = 1;
    int NULL_TREE_LOOKAHEAD = 3;
    int BLOCK_COMMENT = 4;
    int FLOATING_POINT_LITERAL = 5;
    int INTEGER_LITERAL = 6;
    int LINE_COMMENT = 7;
    int OP_AND = 8;
    int OP_BITWISE_AND_ASSIGN = 9;
    int OP_BITWISE_OR_ASSIGN = 10;
    int OP_BITWISE_XOR_ASSIGN = 11;
    int OP_DECREMENT = 12;
    int OP_DIVIDE = 13;
    int OP_DIVIDE_ASSIGN = 14;
    int OP_EQUAL = 15;
    int OP_GREATER_OR_EQUAL = 16;
    int OP_INCREMENT = 17;
    int OP_LOWER_OR_EQUAL = 18;
    int OP_MINUS_ASSIGN = 19;
    int OP_MOD_ASSIGN = 20;
    int OP_MULTIPLY_ASSIGN = 21;
    int OP_NOT_EQUAL = 22;
    int OP_OR = 23;
    int OP_PLUS_ASSIGN = 24;
    int OP_SHIFT_LEFT = 25;
    int OP_SHIFT_LEFT_ASSIGN = 26;
    int OP_SHIFT_RIGHT = 27;
    int OP_SHIFT_RIGHT_ASSIGN = 28;
    int OP_ZERO_FILL_SHIFT_RIGHT = 29;
    int OP_ZERO_FILL_SHIFT_RIGHT_ASSIGN = 30;
    int SEP_DOT = 31;
    int FALSE_LITERAL = 32;
    int KEYWORD_ABSTRACT = 33;
    int KEYWORD_BOOLEAN = 34;
    int KEYWORD_BREAK = 35;
    int KEYWORD_BYTE = 36;
    int KEYWORD_CASE = 37;
    int KEYWORD_CATCH = 38;
    int KEYWORD_CHAR = 39;
    int KEYWORD_CLASS = 40;
    int KEYWORD_CONST = 41;
    int KEYWORD_CONTINUE = 42;
    int KEYWORD_DEFAULT = 43;
    int KEYWORD_DO = 44;
    int KEYWORD_DOUBLE = 45;
    int KEYWORD_ELSE = 46;
    int KEYWORD_EXTENDS = 47;
    int KEYWORD_FINAL = 48;
    int KEYWORD_FINALLY = 49;
    int KEYWORD_FLOAT = 50;
    int KEYWORD_FOR = 51;
    int KEYWORD_GOTO = 52;
    int KEYWORD_IF = 53;
    int KEYWORD_IMPLEMENTS = 54;
    int KEYWORD_IMPORT = 55;
    int KEYWORD_INSTANCEOF = 56;
    int KEYWORD_INT = 57;
    int KEYWORD_INTERFACE = 58;
    int KEYWORD_LONG = 59;
    int KEYWORD_NATIVE = 60;
    int KEYWORD_NEW = 61;
    int KEYWORD_PACKAGE = 62;
    int KEYWORD_PRIVATE = 63;
    int KEYWORD_PROTECTED = 64;
    int KEYWORD_PUBLIC = 65;
    int KEYWORD_RETURN = 66;
    int KEYWORD_SHORT = 67;
    int KEYWORD_STATIC = 68;
    int KEYWORD_STRICTFP = 69;
    int KEYWORD_SUPER = 70;
    int KEYWORD_SWITCH = 71;
    int KEYWORD_SYNCHRONIZED = 72;
    int KEYWORD_THIS = 73;
    int KEYWORD_THROW = 74;
    int KEYWORD_THROWS = 75;
    int KEYWORD_TRANSIENT = 76;
    int KEYWORD_TRY = 77;
    int KEYWORD_VOID = 78;
    int KEYWORD_VOLATILE = 79;
    int KEYWORD_WHILE = 80;
    int NULL_LITERAL = 81;
    int TRUE_LITERAL = 82;
    int SEP_SEMICOLON = 83;
    int OP_MULTIPLY = 84;
    int SEP_COMMA = 85;
    int SEP_OPENING_BRACE = 86;
    int SEP_CLOSING_BRACE = 87;
    int SEP_OPENING_PARENTHESIS = 88;
    int SEP_CLOSING_PARENTHESIS = 89;
    int SEP_OPENING_BRACKET = 90;
    int SEP_CLOSING_BRACKET = 91;
    int OP_ASSIGN = 92;
    int OP_COLON = 93;
    int OP_QUESTION = 94;
    int OP_BITWISE_OR = 95;
    int OP_BITWISE_XOR = 96;
    int OP_BITWISE_AND = 97;
    int OP_LOWER = 98;
    int OP_GREATER = 99;
    int OP_PLUS = 100;
    int OP_MINUS = 101;
    int OP_MOD = 102;
    int OP_BITWISE_COMPLEMENT = 103;
    int OP_NOT = 104;
    int CHARACTER_LITERAL = 105;
    int STRING_LITERAL = 106;
    int IDENTIFIER = 107;
    int WHITESPACE = 108;
    int SUB = 109;
    int OP_DIVIDE_OR_COMMENT = 110;
    int INT_OR_FLOAT_LITERAL_OR_DOT = 111;
    int LINE_TERMINATOR = 112;
    int ESCAPE_SEQUENCE = 113;
    int UNICODE_ESCAPE = 114;
    int OCTAL_ESCAPE = 115;
    int OCTAL_DIGIT = 116;
    int DIGIT = 117;
    int HEX_DIGIT = 118;
    int EXPONENT_PART = 119;
    int INTEGER_TYPE_SUFFIX = 120;
    int FLOAT_TYPE_SUFFIX = 121;
    int IDENTIFIER_START = 122;
    int IDENTIFIER_PART = 123;
}
