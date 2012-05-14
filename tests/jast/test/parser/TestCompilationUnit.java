package jast.test.parser;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

public class TestCompilationUnit extends TestBase
{

    public TestCompilationUnit(String name)
    {
        super(name);
    }

    public void testCompilationUnit1() throws ANTLRException
    {
        setupParser("");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("",
                     result.getPackage().getQualifiedName());
        assertTrue(result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get(""));
        assertEquals(0,
                     result.getImportDeclarations().getCount());
        assertEquals(0,
                     result.getTypeDeclarations().getCount());
    }

    public void testCompilationUnit10()
    {
        setupParser("import test");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCompilationUnit11() throws ANTLRException
    {
        setupParser("import model.*;\n" +
                    "import java.util.*;\n\n" +
                    "public class SomeClass\n" +
                    "{\n" +
                    "    private Vector projects = new Vector();\n" +
                    "    private Vector employees = new Vector();\n" +
                    "\n" +
                    "    public SomeClass()\n" +
                    "    {\n" +
                    "        super();\n" +
                    "    }\n" +
                    "\n" +
                    "    public void assignProject(Project prj, Person prs)\n" +
                    "    {\n" +
                    "        prj.getPersons().add(prs);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("",
                     result.getPackage().getQualifiedName());
        assertTrue(result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get(""));
        assertEquals(2,
                     result.getImportDeclarations().getCount());

        ImportDeclaration importDecl = result.getImportDeclarations().get(0);

        assertEquals("model",
                     importDecl.getImportedPackageOrType());
        assertTrue(importDecl.isOnDemand());

        importDecl = result.getImportDeclarations().get(1);

        assertEquals("java.util",
                     importDecl.getImportedPackageOrType());
        assertTrue(importDecl.isOnDemand());
        assertEquals(1,
                     result.getTypeDeclarations().getCount());

        ClassDeclaration typeDecl = (ClassDeclaration) result.getTypeDeclarations().get(0);

        assertEquals(result.getPackage(),
                     typeDecl.getPackage());
        assertNull(typeDecl.getBaseClass());
        assertEquals(0,
                     typeDecl.getBaseInterfaces().getCount());
        assertEquals("SomeClass",
                     typeDecl.getName());
        assertTrue(!typeDecl.isInner());
        assertTrue(!typeDecl.isLocal());

        Modifiers mods = typeDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(2,
                     typeDecl.getFields().getCount());
        assertEquals(0,
                     typeDecl.getInitializers().getCount());
        assertEquals(1,
                     typeDecl.getConstructors().getCount());
        assertEquals(1,
                     typeDecl.getMethods().getCount());
        assertEquals(0,
                     typeDecl.getInnerTypes().getCount());
    }

    public void testCompilationUnit12() throws ANTLRException
    {
        setupParser("import java.io.*;\n\n" +
                    "public class SomeClass\n" +
                    "{\n" +
                    "    public SomeClass()\n" +
                    "    {\n" +
                    "        super();\n" +
                    "    }\n" +
                    "\n" +
                    "    static class Inner extends java.io.File {}\n" +
                    "\n" +
                    "}\n");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("",
                     result.getPackage().getQualifiedName());
        assertTrue(result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get(""));
        assertEquals(1,
                     result.getImportDeclarations().getCount());

        ImportDeclaration importDecl = result.getImportDeclarations().get(0);

        assertEquals("java.io",
                     importDecl.getImportedPackageOrType());
        assertTrue(importDecl.isOnDemand());
        assertEquals(1,
                     result.getTypeDeclarations().getCount());

        ClassDeclaration typeDecl = (ClassDeclaration) result.getTypeDeclarations().get(0);

        assertEquals(result.getPackage(),
                     typeDecl.getPackage());
        assertNull(typeDecl.getBaseClass());
        assertEquals(0,
                     typeDecl.getBaseInterfaces().getCount());
        assertEquals("SomeClass",
                     typeDecl.getName());
        assertTrue(!typeDecl.isInner());
        assertTrue(!typeDecl.isLocal());

        Modifiers mods = typeDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(mods.isPublic());
        assertTrue(!mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     typeDecl.getFields().getCount());
        assertEquals(0,
                     typeDecl.getInitializers().getCount());
        assertEquals(1,
                     typeDecl.getConstructors().getCount());
        assertEquals(0,
                     typeDecl.getMethods().getCount());
        assertEquals(1,
                     typeDecl.getInnerTypes().getCount());

        typeDecl = (ClassDeclaration) typeDecl.getInnerTypes().get(0);

        assertNotNull(typeDecl.getBaseClass());
        assertEquals(0,
                     typeDecl.getBaseInterfaces().getCount());
        assertEquals("Inner",
                     typeDecl.getName());
        assertTrue(!typeDecl.isInner());
        assertTrue(typeDecl.isNested());
        assertTrue(!typeDecl.isLocal());

        mods = typeDecl.getModifiers();

        assertTrue(!mods.isAbstract());
        assertTrue(!mods.isFinal());
        assertTrue(!mods.isPrivate());
        assertTrue(!mods.isProtected());
        assertTrue(!mods.isPublic());
        assertTrue(mods.isStatic());
        assertTrue(!mods.isStrictfp());

        assertEquals(0,
                     typeDecl.getFields().getCount());
        assertEquals(0,
                     typeDecl.getInitializers().getCount());
        assertEquals(0,
                     typeDecl.getConstructors().getCount());
        assertEquals(0,
                     typeDecl.getMethods().getCount());
        assertEquals(0,
                     typeDecl.getInnerTypes().getCount());
    }

    public void testCompilationUnit13()
    {
        setupParser("import *;");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCompilationUnit2() throws ANTLRException
    {
        setupParser("package test.other;");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("test.other",
                     result.getPackage().getQualifiedName());
        assertTrue(!result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get("test.other"));
        assertEquals(0,
                     result.getImportDeclarations().getCount());
        assertEquals(0,
                     result.getTypeDeclarations().getCount());
    }

    public void testCompilationUnit3() throws ANTLRException
    {
        setupParser("import java.io.IOException;" +
                    "import java.net.*;");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("",
                     result.getPackage().getQualifiedName());
        assertTrue(result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get(""));
        assertEquals(2,
                     result.getImportDeclarations().getCount());

        ImportDeclaration importDecl = result.getImportDeclarations().get(0);

        assertEquals("java.io.IOException",
                     importDecl.getImportedPackageOrType());
        assertTrue(!importDecl.isOnDemand());

        importDecl = result.getImportDeclarations().get(1);

        assertEquals("java.net",
                     importDecl.getImportedPackageOrType());
        assertTrue(importDecl.isOnDemand());
        assertEquals(0,
                     result.getTypeDeclarations().getCount());
    }

    public void testCompilationUnit4() throws ANTLRException
    {
        setupParser("class SomeClass {}" +
                    "interface SomeInterface {}");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("",
                     result.getPackage().getQualifiedName());
        assertTrue(result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get(""));
        assertEquals(0,
                     result.getImportDeclarations().getCount());
        assertEquals(2,
                     result.getTypeDeclarations().getCount());

        TypeDeclaration typeDecl = result.getTypeDeclarations().get(0);

        assertTrue(typeDecl instanceof ClassDeclaration);
        assertEquals("SomeClass",
                     typeDecl.getName());
        assertEquals(result.getPackage(),
                     typeDecl.getPackage());

        typeDecl = result.getTypeDeclarations().get(1);

        assertTrue(typeDecl instanceof InterfaceDeclaration);
        assertEquals("SomeInterface",
                     typeDecl.getName());
        assertEquals(result.getPackage(),
                     typeDecl.getPackage());
    }

    public void testCompilationUnit5() throws ANTLRException
    {
        setupParser("package test.other;" +
                    "import java.io.*;" +
                    "import test.AnotherClass;" +
                    "class SomeClass {}");

        CompilationUnit result = _parser.compilationUnit(_helper.getProject());

        assertNotNull(result);
        assertEquals("SomeClass.java",
                     result.getName());
        assertEquals("test.other",
                     result.getPackage().getQualifiedName());
        assertTrue(!result.getPackage().isDefault());
        // java.lang is already present because of the array type
        assertEquals(2,
                     _helper.getProject().getPackages().getCount());
        assertEquals(result.getPackage(),
                     _helper.getProject().getPackages().get("test.other"));
        assertEquals(2,
                     result.getImportDeclarations().getCount());

        ImportDeclaration importDecl = result.getImportDeclarations().get(0);

        assertEquals("java.io",
                     importDecl.getImportedPackageOrType());
        assertTrue(importDecl.isOnDemand());

        importDecl = result.getImportDeclarations().get(1);

        assertEquals("test.AnotherClass",
                     importDecl.getImportedPackageOrType());
        assertTrue(!importDecl.isOnDemand());
        assertEquals(1,
                     result.getTypeDeclarations().getCount());

        TypeDeclaration typeDecl = result.getTypeDeclarations().get(0);

        assertTrue(typeDecl instanceof ClassDeclaration);
        assertEquals("SomeClass",
                     typeDecl.getName());
        assertEquals(result.getPackage(),
                     typeDecl.getPackage());
    }

    public void testCompilationUnit6()
    {
        setupParser("package ;");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCompilationUnit7()
    {
        setupParser("package test");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCompilationUnit8()
    {
        setupParser("import *;");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testCompilationUnit9()
    {
        setupParser("import;");
        try
        {
            _parser.compilationUnit(_helper.getProject());
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
