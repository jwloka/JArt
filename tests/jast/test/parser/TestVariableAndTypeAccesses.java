package jast.test.parser;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ArrayAccess;
import jast.ast.nodes.AssignmentExpression;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.MethodInvocation;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.SelfAccess;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.TypeAccess;
import jast.ast.nodes.UnresolvedAccess;
import jast.ast.nodes.VariableAccess;
import antlr.ANTLRException;

public class TestVariableAndTypeAccesses extends TestBase
{

    public TestVariableAndTypeAccesses(String name)
    {
        super(name);
    }

    public void testUnresolvedAccess1() throws ANTLRException
    {
        setupParser("idx");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("idx",
                     result.getFieldName());
        assertTrue(!result.isTrailing());
        assertNull(result.getFieldDeclaration());
    }

    public void testUnresolvedAccess10() throws ANTLRException
    {
        setupParser("obj.abc");
        defineVariable("obj", false);

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testUnresolvedAccess11() throws ANTLRException
    {
        setupParser("obj1.obj2.abc");
        defineVariable("obj1", false);

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(2,
                     result.getParts().getCount());
        assertEquals("obj2",
                     result.getParts().get(0));
        assertEquals("abc",
                     result.getParts().get(1));
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertNotNull(varAccess);
        assertEquals("obj1",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testUnresolvedAccess12() throws ANTLRException
    {
        setupParser("this.abc");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testUnresolvedAccess13() throws ANTLRException
    {
        setupParser("Object.this.abc");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(!selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testUnresolvedAccess14() throws ANTLRException
    {
        setupParser("Object.super.abc");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = (TypeAccess)selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testUnresolvedAccess15()
    {
        setupParser("boolean.super.abc");
        try
        {
            _parser.primary();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testUnresolvedAccess2() throws ANTLRException
    {
        setupParser("idx");
        defineField("idx");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("idx",
                     result.getFieldName());
        assertTrue(!result.isTrailing());
        assertNotNull(result.getFieldDeclaration());
    }

    public void testUnresolvedAccess3() throws ANTLRException
    {
        setupParser("obj.abc");

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(2,
                     result.getParts().getCount());
        assertEquals("obj",
                     result.getParts().get(0));
        assertEquals("abc",
                     result.getParts().get(1));
        assertTrue(!result.isTrailing());
    }

    public void testUnresolvedAccess4() throws ANTLRException
    {
        setupParser("obj1.obj2.abc");

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(3,
                     result.getParts().getCount());
        assertEquals("obj1",
                     result.getParts().get(0));
        assertEquals("obj2",
                     result.getParts().get(1));
        assertEquals("abc",
                     result.getParts().get(2));
        assertTrue(!result.isTrailing());
    }

    public void testUnresolvedAccess5() throws ANTLRException
    {
        setupParser("obj1.obj2.abc");
        defineVariable("obj1", false);

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(2,
                     result.getParts().getCount());
        assertEquals("obj2",
                     result.getParts().get(0));
        assertEquals("abc",
                     result.getParts().get(1));
        assertTrue(result.isTrailing());

        VariableAccess varAccess = (VariableAccess)result.getBaseExpression();

        assertEquals("obj1",
                     varAccess.getVariableName());
        assertEquals(result,
                     varAccess.getContainer());
    }

    public void testUnresolvedAccess6() throws ANTLRException
    {
        setupParser("super.abc");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(!selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());
    }

    public void testUnresolvedAccess7() throws ANTLRException
    {
        setupParser("java.lang.String.super.abc");

        FieldAccess result = (FieldAccess)_parser.primary();

        assertNotNull(result);
        assertEquals("abc",
                     result.getFieldName());
        assertTrue(result.isTrailing());
        assertNull(result.getFieldDeclaration());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = (TypeAccess)selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("java.lang.String",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testUnresolvedAccess8() throws ANTLRException
    {
        setupParser("Object.super.def.abc");

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(2,
                     result.getParts().getCount());
        assertEquals("def",
                     result.getParts().get(0));
        assertEquals("abc",
                     result.getParts().get(1));
        assertTrue(result.isTrailing());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("Object",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }

    public void testUnresolvedAccess9() throws ANTLRException
    {
        setupParser("java.lang.String.super.abc.def.ghi");

        UnresolvedAccess result = (UnresolvedAccess)_parser.primary();

        assertNotNull(result);
        assertEquals(3,
                     result.getParts().getCount());
        assertEquals("abc",
                     result.getParts().get(0));
        assertEquals("def",
                     result.getParts().get(1));
        assertEquals("ghi",
                     result.getParts().get(2));
        assertTrue(result.isTrailing());

        SelfAccess selfAccess = (SelfAccess)result.getBaseExpression();

        assertNotNull(selfAccess);
        assertTrue(selfAccess.isQualified());
        assertTrue(selfAccess.isSuper());
        assertEquals(result,
                     selfAccess.getContainer());

        TypeAccess typeAccess = selfAccess.getTypeAccess();

        assertNotNull(typeAccess);
        assertEquals("java.lang.String",
                     typeAccess.getType().getBaseName());
        assertEquals(selfAccess,
                     typeAccess.getContainer());
    }


    public void testAnonymousClass1() throws ANTLRException
    {
        setupParser("private Object doSomething(final Object[] args) {\n"+
                    "  return new Object() {" +
                    "    public String toString() {\n"+
                    "      return args[0].toString();\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        MethodDeclaration methodDecl1   = _parser.methodDeclaration();
        ReturnStatement   returnStmt1   = (ReturnStatement)methodDecl1.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl1.getBody(),
                     returnStmt1.getContainer());

        Instantiation instantiation = (Instantiation)returnStmt1.getReturnValue();

        assertNotNull(instantiation);
        assertEquals("Object",
                     instantiation.getInstantiatedType().getBaseName());
        assertTrue(instantiation.withAnonymousClass());
        assertEquals(returnStmt1,
                     instantiation.getContainer());

        AnonymousClassDeclaration classDecl = instantiation.getAnonymousClass();

        assertNotNull(classDecl);
        assertEquals("Object",
                     classDecl.getBaseType().getBaseName());
        assertNull(classDecl.getName());
        assertEquals(1,
                     classDecl.getMethods().getCount());
        assertEquals(instantiation,
                     classDecl.getContainer());

        MethodDeclaration methodDecl2 = classDecl.getMethods().get(0);

        assertEquals(classDecl,
                     methodDecl2.getContainer());

        ReturnStatement returnStmt2 = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     returnStmt2.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)returnStmt2.getReturnValue();

        assertEquals(returnStmt2,
                     methodInvoc.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)methodInvoc.getBaseExpression();

        assertEquals(methodInvoc,
                     arrayAccess.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)arrayAccess.getBaseExpression();

        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("args",
                     unrslvdAccess.getParts().get(0));
        assertEquals(arrayAccess,
                     unrslvdAccess.getContainer());
    }



    public void testAnonymousClass2() throws ANTLRException
    {
        setupParser("private void doSomething(final int var) {\n"+
                    "  class Local {\n" +
                    "    public String test() {\n"+
                    "      return new Object() {\n"+
                    "        public int getValue() {\n"+
                    "          return var;\n"+
                    "        }\n"+
                    "      };\n"+
                    "    }\n"+
                    "  }\n"+
                    "}");

        MethodDeclaration methodDecl1    = _parser.methodDeclaration();
        ClassDeclaration  localClassDecl = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(0);

        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(methodDecl1.getBody(),
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl2 = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl2.getContainer());

        ReturnStatement returnStmt1 = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     returnStmt1.getContainer());

        Instantiation instantiation = (Instantiation)returnStmt1.getReturnValue();

        assertNotNull(instantiation);
        assertEquals("Object",
                     instantiation.getInstantiatedType().getBaseName());
        assertTrue(instantiation.withAnonymousClass());
        assertEquals(returnStmt1,
                     instantiation.getContainer());

        AnonymousClassDeclaration anonClassDecl = instantiation.getAnonymousClass();

        assertNotNull(anonClassDecl);
        assertEquals("Object",
                     anonClassDecl.getBaseType().getBaseName());
        assertNull(anonClassDecl.getName());
        assertEquals(1,
                     anonClassDecl.getMethods().getCount());
        assertEquals(instantiation,
                     anonClassDecl.getContainer());

        MethodDeclaration methodDecl3 = anonClassDecl.getMethods().get(0);

        assertEquals(anonClassDecl,
                     methodDecl3.getContainer());

        ReturnStatement returnStmt2 = (ReturnStatement)methodDecl3.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl3.getBody(),
                     returnStmt2.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)returnStmt2.getReturnValue();

        assertNotNull(unrslvdAccess);
        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("var",
                     unrslvdAccess.getParts().get(0));
        assertEquals(returnStmt2,
                     unrslvdAccess.getContainer());
    }



    public void testAnonymousClass3() throws ANTLRException
    {
        setupParser("{\n"+
                    "  final Object obj1 = new Object();\n"+
                    "  Object obj2 = new Object() {" +
                    "    public String toString() {\n"+
                    "      return obj1.toString();\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        Block                    block    = _parser.block();
        LocalVariableDeclaration varDecl1 = (LocalVariableDeclaration)block.getBlockStatements().get(0);
        LocalVariableDeclaration varDecl2 = (LocalVariableDeclaration)block.getBlockStatements().get(1);

        assertEquals(block,
                     varDecl1.getContainer());
        assertEquals(block,
                     varDecl2.getContainer());

        SingleInitializer initializer = (SingleInitializer)varDecl2.getInitializer();

        assertEquals(varDecl2,
                     initializer.getContainer());

        Instantiation instantiation = (Instantiation)initializer.getInitEpression();

        assertNotNull(instantiation);
        assertEquals("Object",
                     instantiation.getInstantiatedType().getBaseName());
        assertTrue(instantiation.withAnonymousClass());
        assertEquals(initializer,
                     instantiation.getContainer());

        AnonymousClassDeclaration anonClassDecl = instantiation.getAnonymousClass();

        assertNotNull(anonClassDecl);
        assertEquals("Object",
                     anonClassDecl.getBaseType().getBaseName());
        assertNull(anonClassDecl.getName());
        assertEquals(1,
                     anonClassDecl.getMethods().getCount());
        assertEquals(instantiation,
                     anonClassDecl.getContainer());

        MethodDeclaration methodDecl2 = anonClassDecl.getMethods().get(0);

        assertEquals(anonClassDecl,
                     methodDecl2.getContainer());

        ReturnStatement returnStmt2 = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     returnStmt2.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)returnStmt2.getReturnValue();

        assertEquals(returnStmt2,
                     methodInvoc.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj1",
                     unrslvdAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrslvdAccess.getContainer());
    }



    public void testFormalParameter1() throws ANTLRException
    {
        setupParser("private Object doSomething(Object[] args) {\n"+
                    "  return args[0].toString();\n"+
                    "}");

        MethodDeclaration methodDecl = _parser.methodDeclaration();
        ReturnStatement   returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl.getBody(),
                     returnStmt.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)returnStmt.getReturnValue();

        assertEquals(returnStmt,
                     methodInvoc.getContainer());

        ArrayAccess arrayAccess = (ArrayAccess)methodInvoc.getBaseExpression();

        assertEquals(methodInvoc,
                     arrayAccess.getContainer());

        VariableAccess varAccess = (VariableAccess)arrayAccess.getBaseExpression();

        assertEquals(methodDecl.getParameterList().getParameters().get(0),
                     varAccess.getVariableDeclaration());
        assertEquals(arrayAccess,
                     varAccess.getContainer());
    }



    public void testLocalClass1() throws ANTLRException
    {
        setupParser("public void doSomething(final Object obj) {\n"+
                    "  class Local {" +
                    "    public String toString() {\n"+
                    "      return obj.toString();\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        MethodDeclaration methodDecl1    = _parser.methodDeclaration();
        ClassDeclaration  localClassDecl = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(0);

        assertNotNull(localClassDecl);
        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(methodDecl1.getBody(),
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl2 = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl2.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     returnStmt.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)returnStmt.getReturnValue();

        assertEquals(returnStmt,
                     methodInvoc.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj",
                     unrslvdAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrslvdAccess.getContainer());
    }



    public void testLocalClass2() throws ANTLRException
    {
        setupParser("public void doSomething(final Object obj) {\n"+
                    "  class Local {" +
                    "    public String toString() {\n"+
                    "      String tst = obj.toString();\n"+
                    "      Object obj = tst;\n"+
                    "      return obj.toString();\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        MethodDeclaration methodDecl1    = _parser.methodDeclaration();
        ClassDeclaration  localClassDecl = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(0);

        assertNotNull(localClassDecl);
        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(methodDecl1.getBody(),
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl2 = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl2.getContainer());

        LocalVariableDeclaration varDecl1 = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     varDecl1.getContainer());

        SingleInitializer initializer1 = (SingleInitializer)varDecl1.getInitializer();

        assertEquals(varDecl1,
                     initializer1.getContainer());

        MethodInvocation methodInvoc1 = (MethodInvocation)initializer1.getInitEpression();

        assertEquals(initializer1,
                     methodInvoc1.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)methodInvoc1.getBaseExpression();

        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj",
                     unrslvdAccess.getParts().get(0));
        assertEquals(methodInvoc1,
                     unrslvdAccess.getContainer());

        LocalVariableDeclaration varDecl2 = (LocalVariableDeclaration)methodDecl2.getBody().getBlockStatements().get(1);

        assertEquals(methodDecl2.getBody(),
                     varDecl2.getContainer());

        SingleInitializer initializer2 = (SingleInitializer)varDecl2.getInitializer();

        assertEquals(varDecl2,
                     initializer2.getContainer());

        VariableAccess varAccess2 = (VariableAccess)initializer2.getInitEpression();

        assertEquals(varDecl1,
                     varAccess2.getVariableDeclaration());
        assertEquals(initializer2,
                     varAccess2.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(2);

        assertEquals(methodDecl2.getBody(),
                     returnStmt.getContainer());

        MethodInvocation methodInvoc3 = (MethodInvocation)returnStmt.getReturnValue();

        assertEquals(returnStmt,
                     methodInvoc3.getContainer());

        VariableAccess varAccess3 = (VariableAccess)methodInvoc3.getBaseExpression();

        assertEquals(varDecl2,
                     varAccess3.getVariableDeclaration());
        assertEquals(methodInvoc3,
                     varAccess3.getContainer());
    }



    public void testLocalClass3() throws ANTLRException
    {
        setupParser("public void doSomething(final Object obj) {\n"+
                    "  class Local {" +
                    "    private String obj;\n"+
                    "    public String toString() {\n"+
                    "      return obj;\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        MethodDeclaration methodDecl1    = _parser.methodDeclaration();
        ClassDeclaration  localClassDecl = (ClassDeclaration)methodDecl1.getBody().getBlockStatements().get(0);

        assertNotNull(localClassDecl);
        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(methodDecl1.getBody(),
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl2 = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl2.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)methodDecl2.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl2.getBody(),
                     returnStmt.getContainer());

        FieldAccess fieldAccess = (FieldAccess)returnStmt.getReturnValue();

        assertEquals(localClassDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(returnStmt,
                     fieldAccess.getContainer());
    }



    public void testLocalClass4() throws ANTLRException
    {
        setupParser("{\n"+
                    "  final Object obj = new Object();\n"+
                    "  class Local {" +
                    "    public String toString() {\n"+
                    "      return obj.toString();\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        Block block = _parser.block();

        LocalVariableDeclaration varDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals(block,
                     varDecl.getContainer());

        ClassDeclaration localClassDecl = (ClassDeclaration)block.getBlockStatements().get(1);

        assertNotNull(localClassDecl);
        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(block,
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl.getBody(),
                     returnStmt.getContainer());

        MethodInvocation methodInvoc = (MethodInvocation)returnStmt.getReturnValue();

        assertEquals(returnStmt,
                     methodInvoc.getContainer());

        UnresolvedAccess unrslvdAccess = (UnresolvedAccess)methodInvoc.getBaseExpression();

        assertEquals(1,
                     unrslvdAccess.getParts().getCount());
        assertEquals("obj",
                     unrslvdAccess.getParts().get(0));
        assertEquals(methodInvoc,
                     unrslvdAccess.getContainer());
    }



    public void testLocalClass5() throws ANTLRException
    {
        setupParser("{\n"+
                    "  final Object obj = new Object();\n"+
                    "  class Local {" +
                    "    private String obj;\n"+
                    "    public String toString() {\n"+
                    "      return obj;\n"+
                    "    }\n"+
                    "  };\n"+
                    "}");

        Block block = _parser.block();

        LocalVariableDeclaration varDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals(block,
                     varDecl.getContainer());

        ClassDeclaration localClassDecl = (ClassDeclaration)block.getBlockStatements().get(1);

        assertNotNull(localClassDecl);
        assertEquals("Local",
                     localClassDecl.getName());
        assertEquals(1,
                     localClassDecl.getMethods().getCount());
        assertEquals(block,
                     localClassDecl.getContainer());

        MethodDeclaration methodDecl = localClassDecl.getMethods().get(0);

        assertEquals(localClassDecl,
                     methodDecl.getContainer());

        ReturnStatement returnStmt = (ReturnStatement)methodDecl.getBody().getBlockStatements().get(0);

        assertEquals(methodDecl.getBody(),
                     returnStmt.getContainer());

        FieldAccess fieldAccess = (FieldAccess)returnStmt.getReturnValue();

        assertEquals(localClassDecl.getFields().get(0),
                     fieldAccess.getFieldDeclaration());
        assertEquals(returnStmt,
                     fieldAccess.getContainer());
    }



    public void testLocalVariable1() throws ANTLRException
    {
        setupParser("{\n"+
                    "  int idx;\n"+
                    "  idx = 0;\n"+
                    "}");

        Block block = _parser.block();

        assertNotNull(block);
        assertEquals(2,
                     block.getBlockStatements().getCount());

        LocalVariableDeclaration varDecl = (LocalVariableDeclaration)block.getBlockStatements().get(0);

        assertEquals("idx",
                     varDecl.getName());
        assertEquals(block,
                     varDecl.getContainer());

        ExpressionStatement exprStmt = (ExpressionStatement)block.getBlockStatements().get(1);

        assertEquals(block,
                     exprStmt.getContainer());

        AssignmentExpression assignExpr = (AssignmentExpression)exprStmt.getExpression();

        assertEquals(exprStmt,
                     assignExpr.getContainer());

        VariableAccess varAccess = (VariableAccess)assignExpr.getLeftHandSide();

        assertEquals("idx",
                     varAccess.getVariableName());
        assertEquals(varDecl,
                     varAccess.getVariableDeclaration());
        assertEquals(assignExpr,
                     varAccess.getContainer());
    }
}
