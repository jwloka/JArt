package jast.test.parser;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Type;
import jast.parser.MultipleVariableDeclaration;
import antlr.ANTLRException;

public class TestFieldDeclaration extends TestBase
{

    public TestFieldDeclaration(String name)
    {
        super(name);
    }

    public void testConstantDeclaration1() throws ANTLRException
    {
        setupParser("int attr;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertNull(decl.getInitializer());
        assertEquals("attr",
                     decl.getName());
        assertEquals(decl,
                     _helper.resolveField("attr"));

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("int",
                     fieldType.getBaseName());
    }

    public void testConstantDeclaration2() throws ANTLRException
    {
        setupParser("public static final long CONSTANT = 0l;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.interfaceBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("CONSTANT",
                     decl.getName());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof SingleInitializer);

        Modifiers mods = decl.getModifiers();

        assertTrue(mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("long",
                     fieldType.getBaseName());
    }

    public void testConstantDeclaration3() throws ANTLRException
    {
        setupParser("int count = 0, values[] = { 0, 1 }, other;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(3,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertNotNull(decl.getInitializer());
        assertEquals("count",
                     decl.getName());
        assertEquals(decl,
                     _helper.resolveField("count"));

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("int",
                     fieldType.getBaseName());

        decl = (FieldDeclaration)result.getDeclarators().get(1);

        assertNotNull(decl);
        assertNotNull(decl.getInitializer());
        assertEquals("values",
                     decl.getName());
        assertEquals(1,
                     decl.getType().getDimensions());
        assertEquals(decl,
                     _helper.resolveField("values"));

        decl = (FieldDeclaration)result.getDeclarators().get(2);

        assertNotNull(decl);
        assertNull(decl.getInitializer());
        assertEquals("other",
                     decl.getName());
        assertEquals(0,
                     decl.getType().getDimensions());
        assertEquals(decl,
                     _helper.resolveField("other"));
    }

    public void testConstantDeclaration4()
    {
        setupParser("int idx");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantDeclaration5()
    {
        setupParser("private int idx;");
        try
        {
            _parser.interfaceBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldDeclaration1() throws ANTLRException
    {
        setupParser("int attr;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertNull(decl.getInitializer());
        assertEquals("attr",
                     decl.getName());
        assertEquals(decl,
                     _helper.resolveField("attr"));

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("int",
                     fieldType.getBaseName());
    }

    public void testFieldDeclaration8() throws ANTLRException
    {
        setupParser("static String TEXT; static { TEXT = \"\"; }");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration varDecl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(varDecl);
        assertNull(varDecl.getInitializer());
        assertEquals("TEXT",
                     varDecl.getName());

        Initializer initializer = (Initializer)_parser.classBodyDeclaration();

        assertNotNull(initializer);
        assertTrue(initializer.isStatic());

        Block block = initializer.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(initializer,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals(varDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());
    }

    public void testFieldDeclaration2() throws ANTLRException
    {
        setupParser("public static final long CONSTANT = 0l;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("CONSTANT",
                     decl.getName());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof SingleInitializer);

        Modifiers mods = decl.getModifiers();

        assertTrue(mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("long",
                     fieldType.getBaseName());
    }

    public void testFieldDeclaration3() throws ANTLRException
    {
        setupParser("private volatile String[] names = { \"abc\" };");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("names",
                     decl.getName());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof ArrayInitializer);

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(!fieldType.isPrimitive());
        assertEquals(1,
                     fieldType.getDimensions());
        assertEquals("String",
                     fieldType.getBaseName());
    }

    public void testFieldDeclaration4() throws ANTLRException
    {
        setupParser("protected transient java.lang.String[] names[] = { { \"abc\" } };");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("names",
                     decl.getName());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof ArrayInitializer);

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(!fieldType.isPrimitive());
        assertEquals(2,
                     fieldType.getDimensions());
        assertEquals("java.lang.String",
                     fieldType.getBaseName());
    }

    public void testFieldDeclaration5() throws ANTLRException
    {
        setupParser("int count = 0, values[] = { 0, 1 }, other;");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(3,
                     result.getDeclarators().getCount());

        FieldDeclaration decl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertNotNull(decl.getInitializer());
        assertEquals("count",
                     decl.getName());
        assertEquals(decl,
                     _helper.resolveField("count"));

        Modifiers mods = decl.getModifiers();

        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isTransient());
        assertTrue(!mods.isVolatile());

        Type fieldType = decl.getType();

        assertNotNull(fieldType);
        assertTrue(fieldType.isPrimitive());
        assertEquals(0,
                     fieldType.getDimensions());
        assertEquals("int",
                     fieldType.getBaseName());

        decl = (FieldDeclaration)result.getDeclarators().get(1);

        assertNotNull(decl);
        assertNotNull(decl.getInitializer());
        assertEquals("values",
                     decl.getName());
        assertEquals(1,
                     decl.getType().getDimensions());
        assertEquals(decl,
                     _helper.resolveField("values"));

        decl = (FieldDeclaration)result.getDeclarators().get(2);

        assertNotNull(decl);
        assertNull(decl.getInitializer());
        assertEquals("other",
                     decl.getName());
        assertEquals(0,
                     decl.getType().getDimensions());
        assertEquals(decl,
                     _helper.resolveField("other"));
    }

    public void testFieldDeclaration6()
    {
        setupParser("int idx");
        try
        {
            _parser.classBodyDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldDeclaration7() throws ANTLRException
    {
        setupParser("int attr; { attr = 0; }");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        FieldDeclaration varDecl = (FieldDeclaration)result.getDeclarators().get(0);

        assertNotNull(varDecl);
        assertNull(varDecl.getInitializer());
        assertEquals("attr",
                     varDecl.getName());

        Initializer initializer = (Initializer)_parser.classBodyDeclaration();

        assertNotNull(initializer);
        assertTrue(!initializer.isStatic());

        Block block = initializer.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(initializer,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals(varDecl,
                     fieldAccess.getFieldDeclaration());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());
    }
}
