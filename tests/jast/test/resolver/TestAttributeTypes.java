package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FieldAccess;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.NullLiteral;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.StringLiteral;
import jast.ast.nodes.TypeAccess;
import antlr.ANTLRException;

public class TestAttributeTypes extends TestBase
{

    public TestAttributeTypes(String name) throws ParseException
    {
        super(name);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        addType(
            "test.Test1",
            "package test;\n"+
            "import java.io.Serializable;\n"+
            "public class Test1 extends test2.Test1 {\n"+
            "  private   Object             _attr1 = new Object();\n"+
            "  private   java.lang.String   _attr2 = \"abc\";\n"+
            "  protected Serializable       _attr3 = _attr2;\n"+
            "  protected Test1              _attr4 = null;\n"+
            "  protected Test2              _attr5 = test.Test2.CONST_1;\n"+
            "  protected Test2.Inner1       _attr6;\n"+
            "  protected Inner1             _attr7 = new Test1.Inner1();\n"+
            "  protected Test1.Inner1.Deep1 _attr8 = new Inner1().new Deep1();\n"+
            "  protected Inner              _attr9;\n"+
            "\n"+
            "  private class Inner1 {\n"+
            "    private Inner _attr1;\n"+
            "    public class Deep1 {\n"+
            "    }\n"+
            "  }\n"+
            "}",
            true);
        addType(
            "test.Test2",
            "package test;\n"+
            "public class Test2 {\n"+
            "  public static final Test2 CONST_1 = new Test2();\n"+
            "\n"+
            "  interface Inner1 {}\n"+
            "}",
            true);
        addType(
            "test2.Test1",
            "package test2;\n"+
            "public class Test1 {\n"+
            "  public static final Test1 CONST_1 = new Test1();\n"+
            "\n"+
            "  protected interface Inner {}\n"+
            "}",
            true);

        resolve();
    }

    public void testAttributeType1() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr1");

        assertEquals(_project.getType("java.lang.Object"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType10() throws ANTLRException
    {
        ClassDeclaration  classDecl   = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl   = classDecl.getFields().get("_attr5");
        SingleInitializer init        = (SingleInitializer)fieldDecl.getInitializer();
        FieldAccess       fieldAccess = (FieldAccess)init.getInitEpression();

        assertEquals(_project.getType("test.Test2"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());

        TypeAccess typeAccess = (TypeAccess)fieldAccess.getBaseExpression();

        assertEquals(_project.getType("test.Test2"),
                     typeAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType11() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr6");

        assertEquals(_project.getType("test.Test2.Inner1"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType12() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr7");

        assertEquals(_project.getType("test.Test1.Inner1"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType13() throws ANTLRException
    {
        ClassDeclaration  classDecl     = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl     = classDecl.getFields().get("_attr7");
        SingleInitializer init          = (SingleInitializer)fieldDecl.getInitializer();
        Instantiation     instantiation = (Instantiation)init.getInitEpression();

        assertEquals(_project.getType("test.Test1.Inner1"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType14() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr9");

        assertEquals(_project.getType("test2.Test1.Inner"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType15() throws ANTLRException
    {
        ClassDeclaration innerDecl = (ClassDeclaration)_project.getType("test.Test1.Inner1");
        FieldDeclaration fieldDecl = innerDecl.getFields().get("_attr1");

        assertEquals(_project.getType("test2.Test1.Inner"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType2() throws ANTLRException
    {
        ClassDeclaration  classDecl     = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl     = classDecl.getFields().get("_attr1");
        SingleInitializer init          = (SingleInitializer)fieldDecl.getInitializer();
        Instantiation     instantiation = (Instantiation)init.getInitEpression();

        assertEquals(_project.getType("java.lang.Object"),
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType3() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr2");

        assertEquals(_project.getType("java.lang.String"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType4() throws ANTLRException
    {
        ClassDeclaration  classDecl     = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl     = classDecl.getFields().get("_attr2");
        SingleInitializer init          = (SingleInitializer)fieldDecl.getInitializer();
        StringLiteral     literal       = (StringLiteral)init.getInitEpression();

        assertEquals(_project.getType("java.lang.String"),
                     literal.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType5() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr3");

        assertEquals(_project.getType("java.io.Serializable"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType6() throws ANTLRException
    {
        ClassDeclaration  classDecl   = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl   = classDecl.getFields().get("_attr3");
        SingleInitializer init        = (SingleInitializer)fieldDecl.getInitializer();
        FieldAccess       fieldAccess = (FieldAccess)init.getInitEpression();

        assertEquals(_project.getType("java.lang.String"),
                     fieldAccess.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType7() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr4");

        assertEquals(_project.getType("test.Test1"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }

    public void testAttributeType8() throws ANTLRException
    {
        ClassDeclaration  classDecl   = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration  fieldDecl   = classDecl.getFields().get("_attr4");
        SingleInitializer init        = (SingleInitializer)fieldDecl.getInitializer();
        NullLiteral       literal     = (NullLiteral)init.getInitEpression();

        assertTrue(literal.getType().isNullType());
    }

    public void testAttributeType9() throws ANTLRException
    {
        ClassDeclaration classDecl = (ClassDeclaration)_project.getType("test.Test1");
        FieldDeclaration fieldDecl = classDecl.getFields().get("_attr5");

        assertEquals(_project.getType("test.Test2"),
                     fieldDecl.getType().getReferenceBaseTypeDecl());
    }
}
