package jast.test.parser;
import jast.ast.nodes.ArrayInitializer;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.Type;
import jast.ast.nodes.VariableAccess;
import jast.parser.MultipleVariableDeclaration;
import antlr.ANTLRException;

public class TestVariableDeclaration extends TestBase
{

    public TestVariableDeclaration(String name)
    {
        super(name);
    }

    public void testVariableDeclaration1() throws ANTLRException
    {
        setupParser("int idx");

        MultipleVariableDeclaration result = _parser.localVariableDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        LocalVariableDeclaration decl = (LocalVariableDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertTrue(!decl.getModifiers().isFinal());
        assertNull(decl.getInitializer());
        assertEquals("idx",
                     decl.getName());

        Type varType = decl.getType();

        assertNotNull(varType);
        assertTrue(varType.isPrimitive());
        assertEquals(0,
                     varType.getDimensions());
        assertEquals("int",
                     varType.getBaseName());
    }

    public void testVariableDeclaration10() throws ANTLRException
    {
        setupParser("int idx; String idx;");

        _parser.localVariableDeclaration();
        try
        {
            _parser.localVariableDeclaration();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testVariableDeclaration2() throws ANTLRException
    {
        setupParser("final long idx = 0l");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.localVariableDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        LocalVariableDeclaration decl = (LocalVariableDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertTrue(decl.getModifiers().isFinal());
        assertEquals("idx",
                     decl.getName());

        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof SingleInitializer);

        Type varType = decl.getType();

        assertNotNull(varType);
        assertTrue(varType.isPrimitive());
        assertEquals(0,
                     varType.getDimensions());
        assertEquals("long",
                     varType.getBaseName());
    }

    public void testVariableDeclaration3() throws ANTLRException
    {
        setupParser("String[] names = { \"abc\" }");

        MultipleVariableDeclaration result = (MultipleVariableDeclaration)_parser.localVariableDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        LocalVariableDeclaration decl = (LocalVariableDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("names",
                     decl.getName());
        assertTrue(!decl.getModifiers().isFinal());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof ArrayInitializer);

        Type varType = decl.getType();

        assertNotNull(varType);
        assertTrue(!varType.isPrimitive());
        assertEquals(1,
                     varType.getDimensions());
        assertEquals("String",
                     varType.getBaseName());
    }

    public void testVariableDeclaration4() throws ANTLRException
    {
        setupParser("final java.lang.String[] names[] = { { \"abc\" } }");

        MultipleVariableDeclaration result = _parser.localVariableDeclaration();

        assertNotNull(result);
        assertEquals(1,
                     result.getDeclarators().getCount());

        LocalVariableDeclaration decl = (LocalVariableDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertEquals("names",
                     decl.getName());
        assertTrue(decl.getModifiers().isFinal());
        assertNotNull(decl.getInitializer());
        assertTrue(decl.getInitializer() instanceof ArrayInitializer);

        Type varType = decl.getType();

        assertNotNull(varType);
        assertTrue(!varType.isPrimitive());
        assertEquals(2,
                     varType.getDimensions());
        assertEquals("java.lang.String",
                     varType.getBaseName());
    }

    public void testVariableDeclaration5() throws ANTLRException
    {
        setupParser("int idx1 = 0, idx2[] = { 0, 1 }, idx3");

        MultipleVariableDeclaration result = _parser.localVariableDeclaration();

        assertNotNull(result);
        assertEquals(3,
                     result.getDeclarators().getCount());

        LocalVariableDeclaration decl = (LocalVariableDeclaration)result.getDeclarators().get(0);

        assertNotNull(decl);
        assertTrue(!decl.getModifiers().isFinal());
        assertNotNull(decl.getInitializer());
        assertEquals("idx1",
                     decl.getName());
        assertEquals(0,
                     decl.getType().getDimensions());

        Type varType = decl.getType();

        assertNotNull(varType);
        assertTrue(varType.isPrimitive());
        assertEquals(0,
                     varType.getDimensions());
        assertEquals("int",
                     varType.getBaseName());

        decl = (LocalVariableDeclaration)result.getDeclarators().get(1);

        assertNotNull(decl);
        assertNotNull(decl.getInitializer());
        assertEquals("idx2",
                     decl.getName());
        assertEquals(1,
                     decl.getType().getDimensions());

        decl = (LocalVariableDeclaration)result.getDeclarators().get(2);

        assertNotNull(decl);
        assertNull(decl.getInitializer());
        assertEquals("idx3",
                     decl.getName());
        assertEquals(0,
                     decl.getType().getDimensions());
    }

    public void testVariableDeclaration6() throws ANTLRException
    {
        setupParser("{ int var = 0; String val; val = \"abc\"; }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(3,
                     result.getBlockStatements().getCount());

        LocalVariableDeclaration localVarDecl1 = (LocalVariableDeclaration)result.getBlockStatements().get(0);

        assertNotNull(localVarDecl1);
        assertEquals(result,
                     localVarDecl1.getContainer());

        LocalVariableDeclaration localVarDecl2 = (LocalVariableDeclaration)result.getBlockStatements().get(1);

        assertNotNull(localVarDecl2);
        assertEquals(result,
                     localVarDecl2.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(2);

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getLeftHandSide();

        assertNotNull(varAccess);
        assertEquals(localVarDecl2,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testVariableDeclaration7() throws ANTLRException
    {
        setupParser("{ int idx = 1, var = 0; { var = 1; } }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(3,
                     result.getBlockStatements().getCount());

        LocalVariableDeclaration localVarDecl1 = (LocalVariableDeclaration)result.getBlockStatements().get(0);

        assertNotNull(localVarDecl1);
        assertEquals(result,
                     localVarDecl1.getContainer());

        LocalVariableDeclaration localVarDecl2 = (LocalVariableDeclaration)result.getBlockStatements().get(1);

        assertNotNull(localVarDecl2);
        assertEquals(result,
                     localVarDecl2.getContainer());

        Block block = (Block)result.getBlockStatements().get(2);

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

        VariableAccess varAccess = (VariableAccess)assignExpr.getLeftHandSide();

        assertNotNull(varAccess);
        assertEquals(localVarDecl2,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }

    public void testVariableDeclaration8() throws ANTLRException
    {
        setupParser("{ { int var = 0; } var = 1; }");

        Block result = (Block)_parser.statement();

        assertNotNull(result);
        assertEquals(2,
                     result.getBlockStatements().getCount());

        Block block = (Block)result.getBlockStatements().get(0);

        assertNotNull(block);
        assertEquals(1,
                     block.getBlockStatements().getCount());
        assertEquals(result,
                     block.getContainer());

        LocalVariableDeclaration localVarDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertNotNull(localVarDecl);
        assertEquals(block,
                     localVarDecl.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)result.getBlockStatements().get(1);

        assertNotNull(exprStmt);
        assertEquals(result,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertNotNull(assignExpr);
        assertEquals(exprStmt,
                     assignExpr.getContainer());

        FieldAccess fieldAccess = (FieldAccess)assignExpr.getLeftHandSide();

        assertNotNull(fieldAccess);
        assertEquals("var",
                     fieldAccess.getFieldName());
        assertEquals(assignExpr,
                     fieldAccess.getContainer());
    }
}
