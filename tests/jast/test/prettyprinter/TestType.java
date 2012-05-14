package jast.test.prettyprinter;
import jast.ParseException;
import jast.ast.ParsePosition;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.TypeDeclaration;

public class TestType extends TestPrettyPrinterBase
{

    public TestType(String name)
    {
        super(name);
    }

    public void testType_DirectImport1() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A {\n"+
                "  B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_DirectImport2() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A {\n"+
                "  test2.B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_ImplicitImport1() throws ParseException
    {
        addType("A",
                "public class A {\n"+
                "  Object attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("java.lang.Object",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("Object",
                     getText());
    }

    public void testType_ImplicitImport2() throws ParseException
    {
        addType("A",
                "public class A {\n"+
                "  java.lang.Object attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("java.lang.Object",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("Object",
                     getText());
    }

    public void testType_Inherited1() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected class C {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A extends B {\n"+
                "  C attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B.C",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("C",
                     getText());
    }

    public void testType_Inherited2() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected static class C {\n"+
                "    public static class D {}\n"+
                "  }\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A extends B {\n"+
                "  C.D attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B.C.D",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("C.D",
                     getText());
    }

    public void testType_InheritedInner1() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected class C {};\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  C attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B.C",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("C",
                     getText());
    }

    public void testType_InheritedInner2() throws ParseException
    {
        addType("test3.C",
                "package test3;\n"+
                "public class C {\n"+
                "  protected class D {};\n"+
                "}\n",
                false);
        addType("test2.B",
                "package test2;\n"+
                "public class B extends test3.C {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A extends test2.B {\n"+
                "  D attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test3.C.D",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("D",
                     getText());
    }

    public void testType_OnDemandImport1() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_OnDemandImport2() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.*;\n"+
                "public class A {\n"+
                "  test2.B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_Qualified1() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  test2.B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("test2.B",
                     getText());
    }

    public void testType_Qualified2() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  test1.A attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test1.A",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("A",
                     getText());
    }

    public void testType_Qualified3() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  test1.A.B.C attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test1.A.B.C",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B.C",
                     getText());
    }

    public void testType_SameUnit5() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    A attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A.B");
        FieldDeclaration fieldDecl  = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.A",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("A",
                     getText());
    }

    public void testType_SameUnit6() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  B.C attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test1.A.B.C",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B.C",
                     getText());
    }

    public void testType_SameUnit1() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  A attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.A",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("A",
                     getText());
    }

    public void testType_SameUnit2() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  B attr;\n"+
                "}\n"+
                "class B {}",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_SameUnit3() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    B attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A.B");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.A.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_SameUnit4() throws ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    A.B attr;\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A.B");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.A.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }

    public void testType_Unresolved() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  test2.B attr;\n"+
                "}\n",
                true);

        FieldDeclaration fieldDecl = _project.getType("test1.A").getFields().get("attr");

        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B",
                     getText());
    }


    public void testType_DirectImport3() throws ParseException
    {
        addType("test2.Object",
                "package test2;\n"+
                "public class Object {}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.Object;\n"+
                "public class A {\n"+
                "  Object attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.Object",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("Object",
                     getText());
    }



    public void testType_Inherited3() throws ParseException
    {
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  protected class Object {}\n"+
                "}\n",
                false);
        addType("test1.A",
                "package test1;\n"+
                "import test2.B;\n"+
                "public class A extends B {\n"+
                "  Object attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test2.B.Object",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("Object",
                     getText());
    }



    public void testType_Position1() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  test2.B attr;\n"+
                "}\n",
                true);

        FieldDeclaration fieldDecl = _project.getType("test1.A").getFields().get("attr");

        _prettyPrinter.incIndent(1);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldDecl.getType().getStartPosition());
        assertEquals(new ParsePosition(1, 11, 10),
                     fieldDecl.getType().getFinishPosition());
        assertEquals("    test2.B",
                     getText());
    }



    public void testType_Position2() throws ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  java.lang.String attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test1.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        _prettyPrinter.incIndent(1);
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals(new ParsePosition(1, 5, 4),
                     fieldDecl.getType().getStartPosition());
        assertEquals(new ParsePosition(1, 10, 9),
                     fieldDecl.getType().getFinishPosition());
        assertEquals("    String",
                     getText());
    }



    public void testType_SamePackage1() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }



    public void testType_SamePackage2() throws ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  test.B attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.B",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("B",
                     getText());
    }



    public void testType_SamePackage3() throws ParseException
    {
        addType("test.Object",
                "package test;\n"+
                "public class Object {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  Object attr;\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration  typeDecl  = _project.getType("test.A");
        FieldDeclaration fieldDecl = typeDecl.getFields().get("attr");

        // we must set up the pretty printer for the test
        _prettyPrinter.setCurrentTypeDeclaration(typeDecl);
        _prettyPrinter.visitType(fieldDecl.getType());

        assertEquals("test.Object",
                     fieldDecl.getType().getQualifiedName());
        assertEquals("Object",
                     getText());
    }
}
