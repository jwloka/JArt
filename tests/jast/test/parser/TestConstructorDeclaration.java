package jast.test.parser;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ConstructorInvocation;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestConstructorDeclaration extends TestBase
{

    public TestConstructorDeclaration(String name)
    {
        super(name);
    }

    public void testConstructorDeclaration1() throws ANTLRException
    {
        setupParser("SomeClass() {}");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());
        assertNotNull(result.getBody());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
    }

    public void testConstructorDeclaration10() throws ANTLRException
    {
        setupParser("SomeClass() { int value = 0; }");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertNotNull(localVarDecl);
        assertTrue(!localVarDecl.getModifiers().isFinal());
        assertEquals("int",
                     localVarDecl.getType().getBaseName());
        assertEquals("value",
                     localVarDecl.getName());
        assertEquals(block,
                     localVarDecl.getContainer());
    }

    public void testConstructorDeclaration2() throws ANTLRException
    {
        setupParser("public SomeClass(String text) throws NullPointerException {}");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(1,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertNotNull(result.getBody());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
    }

    public void testConstructorDeclaration3() throws ANTLRException
    {
        setupParser("SomeClass(String value) throws NullPointerException, FormatException " +
                    "{ attr = value; }");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(2,
                     result.getThrownExceptions().getCount());
        assertEquals("NullPointerException",
                     result.getThrownExceptions().get(0).getBaseName());
        assertEquals("FormatException",
                     result.getThrownExceptions().get(1).getBaseName());

        FormalParameterList params = result.getParameterList();

        assertNotNull(params);
        assertEquals(1,
                     params.getParameters().getCount());
        assertEquals(result,
                     params.getContainer());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getValueExpression();

        assertNotNull(varAccess);
        assertEquals(params.getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testConstructorDeclaration4()
    {
        setupParser("AnotherClass();");
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

    public void testConstructorDeclaration5()
    {
        setupParser("static AndAnotherClass() {}");
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

    public void testConstructorDeclaration6() throws ANTLRException
    {
        setupParser("SomeClass() { super(); }");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(!constructorInvoc.isTrailing());
        assertTrue(constructorInvoc.ofBaseClass());
        assertEquals(exprStmt,
                     constructorInvoc.getContainer());
    }

    public void testConstructorDeclaration7() throws ANTLRException
    {
        setupParser("SomeClass() { this(0); }");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(!constructorInvoc.isTrailing());
        assertTrue(!constructorInvoc.ofBaseClass());
        assertEquals(exprStmt,
                     constructorInvoc.getContainer());
    }

    public void testConstructorDeclaration8() throws ANTLRException
    {
        setupParser("SomeClass() { (new OuterClass()).super(0); }");

        ConstructorDeclaration result = (ConstructorDeclaration)_parser.classBodyDeclaration();

        assertNotNull(result);
        assertEquals("SomeClass",
                     result.getName());
        assertEquals(0,
                     result.getThrownExceptions().getCount());
        assertNull(result.getParameterList());

        Modifiers mods = result.getModifiers();

        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());

        Block block = result.getBody();

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(0);

        assertNotNull(exprStmt);
        assertEquals(block,
                     exprStmt.getContainer());

        ConstructorInvocation constructorInvoc = (ConstructorInvocation)exprStmt.getExpression();

        assertNotNull(constructorInvoc);
        assertTrue(constructorInvoc.isTrailing());
        assertTrue(constructorInvoc.ofBaseClass());
        assertEquals(exprStmt,
                     constructorInvoc.getContainer());
    }
}
