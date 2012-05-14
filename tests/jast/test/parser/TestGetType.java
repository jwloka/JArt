package jast.test.parser;
import jast.Global;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.ArrayCreation;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.BinaryExpression;
import jast.ast.nodes.BooleanLiteral;
import jast.ast.nodes.CharacterLiteral;
import jast.ast.nodes.ClassAccess;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ConditionalExpression;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FloatingPointLiteral;
import jast.ast.nodes.InstanceofExpression;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.ParenthesizedExpression;
import jast.ast.nodes.PostfixExpression;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnaryExpression;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestGetType extends TestBase
{

    public TestGetType(String name)
    {
        super(name);
    }

    public void testGetType1() throws ANTLRException
    {
        setupParser("null");

        NullLiteral nullLit = (NullLiteral)_parser.expression();

        assertNotNull(nullLit);

        Type type = nullLit.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertNull(type.getBaseName());
    }

    public void testGetType10() throws ANTLRException
    {
        setupParser("name");
        defineVariable("name", false); // defines a new variable of String type

        VariableAccess varAccess = (VariableAccess)_parser.expression();

        assertNotNull(varAccess);

        Type type = varAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType11() throws ANTLRException
    {
        setupParser("new int[2]");

        ArrayCreation arrayCreation = (ArrayCreation)_parser.expression();

        assertNotNull(arrayCreation);

        Type type = arrayCreation.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(type.isArray());
        assertEquals(1,
                     type.getDimensions());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType12() throws ANTLRException
    {
        setupParser("new java.io.URL[2][3][]");

        ArrayCreation arrayCreation = (ArrayCreation)_parser.expression();

        assertNotNull(arrayCreation);

        Type type = arrayCreation.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(type.isArray());
        assertEquals(3,
                     type.getDimensions());
        assertEquals("java.io.URL",
                     type.getBaseName());
    }

    public void testGetType13() throws ANTLRException
    {
        setupParser("new java.io.URL(\"localhost\")");

        Instantiation instantiation = (Instantiation)_parser.expression();

        assertNotNull(instantiation);

        assertTrue(!instantiation.withAnonymousClass());

        Type type = instantiation.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.io.URL",
                     type.getBaseName());
    }

    public void testGetType14() throws ANTLRException
    {
        setupParser("new java.io.URL(\"localhost\") { void test() {} }");

        Instantiation instantiation = (Instantiation)_parser.expression();

        assertNotNull(instantiation);
        assertTrue(instantiation.withAnonymousClass());

        Type type = instantiation.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.io.URL",
                     type.getBaseName());
    }

    public void testGetType15() throws ANTLRException
    {
        setupParser("Object.this");

        SelfAccess selfAccess = (SelfAccess)_parser.expression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());

        Type type = selfAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("Object",
                     type.getBaseName());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertTrue(!typeAccess.isTrailing());
        assertEquals(selfAccess,
                     typeAccess.getContainer());

        type = typeAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("Object",
                     type.getBaseName());
    }

    public void testGetType16() throws ANTLRException
    {
        setupParser("String.super.test");

        FieldAccess fieldAccess = (FieldAccess)_parser.expression();

        assertNotNull(fieldAccess);

        // We can get the type of the field access only after resolving

        SelfAccess selfAccess = (SelfAccess)fieldAccess.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(fieldAccess,
                     selfAccess.getContainer());

        // The same for the self access since we must determine
        // the baseclass of "String"

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertTrue(!typeAccess.isTrailing());
        assertEquals(selfAccess,
                     typeAccess.getContainer());

        Type type = typeAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("String",
                     type.getBaseName());
    }

    public void testGetType17() throws ANTLRException
    {
        setupParser("String.class");

        ClassAccess classAccess = (ClassAccess)_parser.expression();

        assertNotNull(classAccess);

        Type type = classAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Class",
                     type.getBaseName());
    }

    public void testGetType18() throws ANTLRException
    {
        setupParser("(0)");

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)_parser.expression();

        assertNotNull(parenExpr);

        Type type = parenExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType19() throws ANTLRException
    {
        setupParser("(var)");
        defineVariable("var", false);

        ParenthesizedExpression parenExpr = (ParenthesizedExpression)_parser.expression();

        assertNotNull(parenExpr);

        Type type = parenExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType2() throws ANTLRException
    {
        setupParser("\"abc\"");

        StringLiteral stringLit = (StringLiteral)_parser.expression();

        assertNotNull(stringLit);

        Type type = stringLit.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType20() throws ANTLRException
    {
        setupParser("this();");

        ConstructorInvocation invoc = (ConstructorInvocation)_parser.expression();

        assertNotNull(invoc);
        assertNull(invoc.getType());
    }

    public void testGetType21() throws ANTLRException
    {
        setupParser("super(2);");

        ConstructorInvocation invoc = (ConstructorInvocation)_parser.expression();

        assertNotNull(invoc);
        assertNull(invoc.getType());
    }

    public void testGetType22() throws ANTLRException
    {
        setupParser("toString()");

        MethodInvocation methodInvoc = (MethodInvocation)_parser.expression();

        assertNotNull(methodInvoc);
        // No type yet ...
        assertNull(methodInvoc.getType());

        MethodDeclaration decl = Global.getFactory().createMethodDeclaration(
                                    Global.getFactory().createModifiers(),
                                    Global.getFactory().createType("java.lang.String", 0),
                                    "toString",
                                    null);
        methodInvoc.setMethodDeclaration(decl);

        // But now: java.lang.String
        Type type = methodInvoc.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType23() throws ANTLRException
    {
        setupParser("doSomething()");

        MethodInvocation methodInvoc = (MethodInvocation)_parser.expression();

        assertNotNull(methodInvoc);
        // No type yet ...
        assertNull(methodInvoc.getType());

        MethodDeclaration decl = Global.getFactory().createMethodDeclaration(
                                    Global.getFactory().createModifiers(),
                                    null,
                                    "doSomething",
                                    null);
        methodInvoc.setMethodDeclaration(decl);

        // Still no type
        assertNull(methodInvoc.getType());
    }

    public void testGetType24() throws ANTLRException
    {
        setupParser("(toString())");

        ParenthesizedExpression methodInvoc = (ParenthesizedExpression)_parser.expression();

        assertNotNull(methodInvoc);
        // No type yet for the method invocation !
        assertNull(methodInvoc.getType());
    }

    public void testGetType25() throws ANTLRException
    {
        Type varType = Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 2);

        setupParser("arr");
        defineVariable("arr", varType, false);

        VariableAccess varAccess = (VariableAccess)_parser.expression();

        assertNotNull(varAccess);

        Type type = varAccess.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(type.isArray());
        assertEquals(2,
                     type.getDimensions());
        assertEquals(PrimitiveType.BYTE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType26() throws ANTLRException
    {
        Type fieldType = Global.getFactory().createType("java.lang.String", 2);

        setupParser("arr");
        defineField("arr", fieldType);

        FieldAccess fieldAccess = (FieldAccess)_parser.expression();

        assertNotNull(fieldAccess);

        Type type = fieldAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(type.isArray());
        assertEquals(2,
                     type.getDimensions());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType27() throws ANTLRException
    {
        Type varType = Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 2);

        setupParser("arr[2]");
        defineVariable("arr", varType, false);

        ArrayAccess arrayAccess = (ArrayAccess)_parser.expression();

        assertNotNull(arrayAccess);

        Type type = arrayAccess.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(type.isArray());
        assertEquals(1,
                     type.getDimensions());
        assertEquals(PrimitiveType.BYTE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType28() throws ANTLRException
    {
        Type fieldType = Global.getFactory().createType("java.lang.String", 1);

        setupParser("arr[getIndex()]");
        defineField("arr", fieldType);

        ArrayAccess arrayAccess = (ArrayAccess)_parser.expression();

        assertNotNull(arrayAccess);

        Type type = arrayAccess.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType29() throws ANTLRException
    {
        setupParser("idx--");
        defineField("idx"); // defines a new field of int type

        PostfixExpression postfixExpr = (PostfixExpression)_parser.expression();

        assertNotNull(postfixExpr);

        Type type = postfixExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType3() throws ANTLRException
    {
        setupParser("'a'");

        CharacterLiteral charLit = (CharacterLiteral)_parser.expression();

        assertNotNull(charLit);

        Type type = charLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.CHAR_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType30() throws ANTLRException
    {
        setupParser("(String)null");

        UnaryExpression unaryExpr = (UnaryExpression)_parser.expression();

        assertNotNull(unaryExpr);

        Type type = unaryExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("String",
                     type.getBaseName());
    }

    public void testGetType31() throws ANTLRException
    {
        setupParser("(float)getValue()");

        UnaryExpression unaryExpr = (UnaryExpression)_parser.expression();

        assertNotNull(unaryExpr);

        Type type = unaryExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.FLOAT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType32() throws ANTLRException
    {
        setupParser("(int[])getValues()");

        UnaryExpression unaryExpr = (UnaryExpression)_parser.expression();

        assertNotNull(unaryExpr);

        Type type = unaryExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(type.isArray());
        assertEquals(1, type.getDimensions());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType33() throws ANTLRException
    {
        setupParser("++idx");
        defineField("idx");

        UnaryExpression unaryExpr = (UnaryExpression)_parser.expression();

        assertNotNull(unaryExpr);

        Type type = unaryExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType34() throws ANTLRException
    {
        Type fieldType = Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 1);

        setupParser("~arr[0]");
        defineField("arr", fieldType);

        UnaryExpression unaryExpr = (UnaryExpression)_parser.expression();

        assertNotNull(unaryExpr);

        // Promoted from short to int
        Type type = unaryExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType35() throws ANTLRException
    {
        setupParser("idx + 2");
        defineField("idx");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType36() throws ANTLRException
    {
        setupParser("idx * 2.0");
        defineField("idx");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.DOUBLE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType37() throws ANTLRException
    {
        setupParser("1 % 2.0f");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.FLOAT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType38() throws ANTLRException
    {
        setupParser("1l < 2");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType39() throws ANTLRException
    {
        setupParser("name + 2");
        defineVariable("name", false);

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType4() throws ANTLRException
    {
        setupParser("false");

        BooleanLiteral boolLit = (BooleanLiteral)_parser.expression();

        assertNotNull(boolLit);

        Type type = boolLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType40() throws ANTLRException
    {
        setupParser("name + null");
        defineVariable("name", false);

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType41() throws ANTLRException
    {
        setupParser("new int[2] + name");
        defineVariable("name", false);

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType42() throws ANTLRException
    {
        setupParser("idx >> 2l");
        defineField("idx");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType43() throws ANTLRException
    {
        setupParser("value << 2");
        defineField("value",
                    Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 0));

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        // Promoted from byte to int
        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType44() throws ANTLRException
    {
        setupParser("obj instanceof String");
        defineVariable("obj", false);

        InstanceofExpression instanceofExpr = (InstanceofExpression)_parser.expression();

        assertNotNull(instanceofExpr);

        Type type = instanceofExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType45() throws ANTLRException
    {
        setupParser("1 == 2");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType46() throws ANTLRException
    {
        setupParser("true || false");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType47() throws ANTLRException
    {
        setupParser("true ^ false");

        BinaryExpression binExpr = (BinaryExpression)_parser.expression();

        assertNotNull(binExpr);

        Type type = binExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType48() throws ANTLRException
    {
        setupParser("true ? 0 : 1");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType49() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.SHORT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType5() throws ANTLRException
    {
        setupParser("2.0f");

        FloatingPointLiteral floatLit = (FloatingPointLiteral)_parser.expression();

        assertNotNull(floatLit);

        Type type = floatLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.FLOAT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType50() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType51() throws ANTLRException
    {
        setupParser("false ? 1 : 2.0");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.DOUBLE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType52() throws ANTLRException
    {
        setupParser("false ? 1.0f : 'a'");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.FLOAT_TYPE, type.getPrimitiveBaseType());
    }

    public void testGetType53() throws ANTLRException
    {
        setupParser("false ? 'a' : 10l");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.LONG_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType54() throws ANTLRException
    {
        setupParser("false ? var1 : 'a'");
        defineVariable("var1",
                       Global.getFactory().createType(PrimitiveType.INT_TYPE, 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType55() throws ANTLRException
    {
        setupParser("true ? var : null");
        defineVariable("var",
                       Global.getFactory().createType("Object", 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("Object",
                     type.getBaseName());
    }

    public void testGetType56() throws ANTLRException
    {
        setupParser("true ? null : new String[2][]");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(type.isArray());
        assertEquals(2,
                     type.getDimensions());
        assertEquals("String",
                     type.getBaseName());
    }

    public void testGetType57() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType("java.lang.Object", 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 1),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Object",
                     type.getBaseName());
    }

    public void testGetType58() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType("java.lang.String", 2),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType("java.lang.Clonable", 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Clonable",
                     type.getBaseName());
    }

    public void testGetType59() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 2),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType("java.io.Serializable", 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.io.Serializable",
                     type.getBaseName());
    }

    public void testGetType6() throws ANTLRException
    {
        setupParser("2.0");

        FloatingPointLiteral floatLit = (FloatingPointLiteral)_parser.expression();

        assertNotNull(floatLit);

        Type type = floatLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.DOUBLE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType60() throws ANTLRException
    {
        setupParser("false ? var1 : var2");
        defineVariable("var1",
                       Global.getFactory().createType("java.lang.Object", 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType("java.io.Serializable", 0),
                       false);

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Object",
                     type.getBaseName());
    }

    public void testGetType61() throws ANTLRException
    {
        ClassDeclaration decl = Global.getFactory().createClassDeclaration(Global.getFactory().createModifiers(),
                                                                           "SomeClass",
                                                                           false);

        decl.setBaseClass(Global.getFactory().createType("AnotherClass", 0));
        defineVariable("var1",
                       Global.getFactory().createType(decl, 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType("AnotherClass", 0),
                       false);

        setupParser("false ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("AnotherClass",
                     type.getBaseName());
    }

    public void testGetType62() throws ANTLRException
    {
        ClassDeclaration decl = Global.getFactory().createClassDeclaration(Global.getFactory().createModifiers(),
                                                                           "SomeClass",
                                                                           false);

        decl.getBaseInterfaces().add(Global.getFactory().createType("SomeInterface", 0));
        defineVariable("var1",
                       Global.getFactory().createType("SomeInterface", 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(decl, 0),
                       false);

        setupParser("true ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("SomeInterface",
                     type.getBaseName());
    }

    public void testGetType63() throws ANTLRException
    {
        InterfaceDeclaration decl = Global.getFactory().createInterfaceDeclaration(Global.getFactory().createModifiers(),
                                                                                   "SomeInterface");

        decl.getBaseInterfaces().add(Global.getFactory().createType("AnotherInterface", 0));
        defineVariable("var1",
                       Global.getFactory().createType("AnotherInterface", 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(decl, 0),
                       false);

        setupParser("true ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("AnotherInterface",
                     type.getBaseName());
    }

    public void testGetType64() throws ANTLRException
    {
        ClassDeclaration decl = Global.getFactory().createClassDeclaration(Global.getFactory().createModifiers(),
                                                                           "SomeClass",
                                                                           false);

        defineVariable("var1",
                       Global.getFactory().createType(decl, 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType("java.lang.Object", 0),
                       false);

        setupParser("false ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Object",
                     type.getBaseName());
    }

    public void testGetType65() throws ANTLRException
    {
        InterfaceDeclaration decl = Global.getFactory().createInterfaceDeclaration(Global.getFactory().createModifiers(),
                                                                                   "SomeInterface");

        defineVariable("var1",
                       Global.getFactory().createType("java.lang.Object", 0),
                       false);
        defineVariable("var2",
                       Global.getFactory().createType(decl, 0),
                       false);

        setupParser("true ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.Object",
                     type.getBaseName());
    }

    public void testGetType66() throws ANTLRException
    {
        Type varType = Global.getFactory().createType(PrimitiveType.BOOLEAN_TYPE, 2);

        defineVariable("var1", varType, false);
        defineVariable("var2", varType, false);

        setupParser("false ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(type.isArray());
        assertEquals(2,
                     type.getDimensions());
        assertEquals(PrimitiveType.BOOLEAN_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType67() throws ANTLRException
    {
        Type var1Type = Global.getFactory().createType("java.lang.String", 2);
        Type var2Type = Global.getFactory().createType("java.lang.Object", 2);

        defineVariable("var1", var1Type, false);
        defineVariable("var2", var2Type, false);

        setupParser("false ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(type.isArray());
        assertEquals(2,
                     type.getDimensions());
        assertEquals("java.lang.Object",
                     type.getBaseName());
    }

    public void testGetType68() throws ANTLRException
    {
        ClassDeclaration decl = Global.getFactory().createClassDeclaration(Global.getFactory().createModifiers(),
                                                                           "SomeClass",
                                                                           false);
        decl.getBaseInterfaces().add(Global.getFactory().createType("SomeInterface", 0));

        Type var2Type = Global.getFactory().createType(decl, 1);
        Type var1Type = Global.getFactory().createType("SomeInterface", 1);

        defineVariable("var1", var1Type, false);
        defineVariable("var2", var2Type, false);

        setupParser("true ? var1 : var2");

        ConditionalExpression condExpr = (ConditionalExpression)_parser.expression();

        assertNotNull(condExpr);

        Type type = condExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(type.isArray());
        assertEquals(1,
                     type.getDimensions());
        assertEquals("SomeInterface",
                     type.getBaseName());
    }

    public void testGetType69() throws ANTLRException
    {
        setupParser("val = \"abc\"");
        defineVariable("val",
                       Global.getFactory().createType("java.lang.String", 0),
                       false);

        AssignmentExpression assignExpr = (AssignmentExpression)_parser.expression();

        assertNotNull(assignExpr);

        Type type = assignExpr.getType();

        assertNotNull(type);
        assertTrue(!type.isPrimitive());
        assertTrue(type.isReference());
        assertTrue(!type.isArray());
        assertEquals("java.lang.String",
                     type.getBaseName());
    }

    public void testGetType7() throws ANTLRException
    {
        setupParser("0xFF");

        IntegerLiteral intLit = (IntegerLiteral)_parser.expression();

        assertNotNull(intLit);

        Type type = intLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType70() throws ANTLRException
    {
        setupParser("val += 'a'");
        defineField("val",
                    Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 0));

        AssignmentExpression assignExpr = (AssignmentExpression)_parser.expression();

        assertNotNull(assignExpr);

        Type type = assignExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.SHORT_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType71() throws ANTLRException
    {
        Type varType = Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 1);

        setupParser("arr[1] *= getValue()");
        defineVariable("arr", varType, false);

        AssignmentExpression assignExpr = (AssignmentExpression)_parser.expression();

        assertNotNull(assignExpr);

        Type type = assignExpr.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.BYTE_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType8() throws ANTLRException
    {
        setupParser("100L");

        IntegerLiteral intLit = (IntegerLiteral)_parser.expression();

        assertNotNull(intLit);

        Type type = intLit.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.LONG_TYPE,
                     type.getPrimitiveBaseType());
    }

    public void testGetType9() throws ANTLRException
    {
        setupParser("idx");
        defineField("idx"); // defines a new field of int type

        FieldAccess fieldAccess = (FieldAccess)_parser.expression();

        assertNotNull(fieldAccess);

        Type type = fieldAccess.getType();

        assertNotNull(type);
        assertTrue(type.isPrimitive());
        assertTrue(!type.isReference());
        assertTrue(!type.isArray());
        assertEquals(PrimitiveType.INT_TYPE,
                     type.getPrimitiveBaseType());
    }
}
