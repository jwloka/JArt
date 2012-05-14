package jast.test.parser;
import jast.FileParser;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Package;
import jast.ast.nodes.ReturnStatement;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;
import antlr.ANTLRException;

public class TestFileParser extends TestCase
{
    private Project    _project;
    private FileParser _parser;

    public TestFileParser(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _project = new Project("Test");
        _parser  = new FileParser();
    }

    protected void tearDown()
    {
        _parser  = null;
        _project = null;
    }

    public void testFileParser1() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java", "");
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(0,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(2,
                     _project.getPackages().getCount());
        assertEquals(_project,
                     compilationUnit.getContainer());

        // first package is java.lang
        Package pckg = _project.getPackages().get(1);

        assertEquals("",
                     pckg.getQualifiedName());
        assertEquals(0,
                     pckg.getTypes().getCount());
        assertEquals(compilationUnit.getPackage(),
                     pckg);
        assertEquals(_project,
                     pckg.getContainer());
    }

    public void testFileParser2() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "package test.other;" +
                    "import java.io.*;" +
                    "import test.AnotherClass;" +
                    "class SomeClass {}");
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(2,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(_project,
                     compilationUnit.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals(2,
                     _project.getPackages().getCount());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        // first package is java.lang
        Package pckg = _project.getPackages().get(1);

        assertEquals("test.other",
                     pckg.getQualifiedName());
        assertEquals(1,
                     pckg.getTypes().getCount());
        assertEquals(compilationUnit.getPackage(),
                     pckg);
        assertEquals(classDecl.getPackage(),
                     pckg);
        assertEquals(_project,
                     pckg.getContainer());
    }

    public void testFileParser3() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "package test.other;" +
                    "interface SomeInterface {}" +
                    "class SomeClass {}");
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(2,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(2,
                     _project.getPackages().getCount());
        assertEquals(_project,
                     compilationUnit.getContainer());

        // first package is java.lang
        Package pckg = _project.getPackages().get(1);

        assertEquals("test.other",
                     pckg.getQualifiedName());
        assertEquals(2,
                     pckg.getTypes().getCount());
        assertEquals(compilationUnit.getPackage(),
                     pckg);
        assertEquals(_project,
                     pckg.getContainer());

        InterfaceDeclaration interfaceDecl = (InterfaceDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeInterface",
                     interfaceDecl.getName());
        assertEquals(interfaceDecl.getPackage(),
                     pckg);
        assertEquals(interfaceDecl,
                     pckg.getTypes().get(0));
        assertEquals(compilationUnit,
                     interfaceDecl.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(1);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals(classDecl.getPackage(),
                     pckg);
        assertEquals(classDecl,
                     pckg.getTypes().get(1));
        assertEquals(compilationUnit,
                     classDecl.getContainer());
    }

    public void testFileParser4() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "package test.other;" +
                    "class SomeClass {}");
        writeToFile("SomeInterface.java",
                    "package test.other;" +
                    "interface SomeInterface {}");

        _parser.parseFile(_project, "SomeClass.java");
        _parser.parseFile(_project, "SomeInterface.java");

        assertEquals(2,
                     _project.getPackages().getCount());

        // first package is java.lang
        Package pckg = _project.getPackages().get(1);

        assertEquals("test.other",
                     pckg.getQualifiedName());
        assertEquals(2,
                     pckg.getTypes().getCount());
        assertEquals(3,
                     _project.getCompilationUnits().getCount());
        assertEquals(_project,
                     pckg.getContainer());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(pckg,
                     compilationUnit.getPackage());
        assertEquals(_project,
                     compilationUnit.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals(classDecl.getPackage(),
                     pckg);
        assertEquals(classDecl,
                     pckg.getTypes().get(0));
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        compilationUnit = _project.getCompilationUnits().get(2);

        assertEquals("SomeInterface.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(pckg,
                     compilationUnit.getPackage());
        assertEquals(_project,
                     compilationUnit.getContainer());

        InterfaceDeclaration interfaceDecl = (InterfaceDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeInterface",
                     interfaceDecl.getName());
        assertEquals(interfaceDecl.getPackage(),
                     pckg);
        assertEquals(interfaceDecl,
                     pckg.getTypes().get(1));
        assertEquals(compilationUnit,
                     interfaceDecl.getContainer());
    }

    public void testFileParser5() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "package test;" +
                    "class SomeClass {}");
        writeToFile("SomeInterface.java",
                    "package test.other;" +
                    "interface SomeInterface {}");

        _parser.parseFile(_project, "SomeClass.java");
        _parser.parseFile(_project, "SomeInterface.java");

        assertEquals(3,
                     _project.getPackages().getCount());

        // first package is java.lang
        Package pckg1 = _project.getPackages().get(1);

        assertEquals("test",
                     pckg1.getQualifiedName());
        assertEquals(1,
                     pckg1.getTypes().getCount());
        assertEquals(_project,
                     pckg1.getContainer());

        Package pckg2 = _project.getPackages().get(2);

        assertEquals("test.other",
                     pckg2.getQualifiedName());
        assertEquals(1,
                     pckg2.getTypes().getCount());
        assertEquals(_project,
                     pckg2.getContainer());

        assertEquals(3,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(pckg1,
                     compilationUnit.getPackage());
        assertEquals(_project,
                     compilationUnit.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals(pckg1,
                     classDecl.getPackage());
        assertEquals(classDecl,
                     pckg1.getTypes().get(0));
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        compilationUnit = _project.getCompilationUnits().get(2);

        assertEquals("SomeInterface.java",
                     compilationUnit.getName());
        assertEquals(0,
                     compilationUnit.getImportDeclarations().getCount());
        assertEquals(1,
                     compilationUnit.getTypeDeclarations().getCount());
        assertEquals(pckg2,
                     compilationUnit.getPackage());
        assertEquals(_project,
                     compilationUnit.getContainer());

        InterfaceDeclaration interfaceDecl = (InterfaceDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeInterface",
                     interfaceDecl.getName());
        assertEquals(pckg2,
                     interfaceDecl.getPackage());
        assertEquals(interfaceDecl,
                     pckg2.getTypes().get(0));
        assertEquals(compilationUnit,
                     interfaceDecl.getContainer());
    }

    public void testFileParser6() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  int someMethod() {\n"+
                    "    int var = 0;\n"+
                    "    return var;\n"+
                    "  }\n"+
                    "}");
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        MethodDeclaration method = classDecl.getMethods().get(0);

        assertEquals("someMethod",
                     method.getName());
        assertEquals(classDecl,
                     method.getContainer());

        assertTrue(method.getBody().getBlockStatements().get(0) instanceof LocalVariableDeclaration);
        assertTrue(method.getBody().getBlockStatements().get(1) instanceof ReturnStatement);
    }

    public void testFileParser7() throws IOException, ANTLRException
    {
        // one unit is already present because of the array type
        assertEquals(1,
                     _project.getCompilationUnits().getCount());
        // java.lang is already present because of the array type
        assertEquals(1,
                     _project.getPackages().getCount());

        writeToFile("SomeClass.java",
                    "class SomeClass {\n"+
                    "  int someMethod() {\n"+
                    "    int var = 0;\n"+
                    "    {\n"+
                    "      var++;\n"+
                    "    }\n"+
                    "    return var; }\n"+
                    "  }\n"+
                    "}");
        _parser.setParsingInterface(true);
        _parser.parseFile(_project, "SomeClass.java");

        assertEquals(2,
                     _project.getCompilationUnits().getCount());

        // first unit is that of the array type
        CompilationUnit compilationUnit = _project.getCompilationUnits().get(1);

        assertEquals("SomeClass.java",
                     compilationUnit.getName());
        assertEquals(_project,
                     compilationUnit.getContainer());

        ClassDeclaration classDecl = (ClassDeclaration)compilationUnit.getTypeDeclarations().get(0);

        assertEquals("SomeClass",
                     classDecl.getName());
        assertEquals("",
                     classDecl.getPackage().getQualifiedName());
        assertEquals(compilationUnit,
                     classDecl.getContainer());

        MethodDeclaration method = classDecl.getMethods().get(0);

        assertEquals("someMethod",
                     method.getName());
        assertNull(method.getBody());
        assertEquals(classDecl,
                     method.getContainer());

        _parser.complete(compilationUnit);
    }

    private void writeToFile(String filename, String text) throws IOException
    {
        PrintWriter output = new PrintWriter(new FileWriter(filename));
        output.println(text);
        output.close();
    }
}
