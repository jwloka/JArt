package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import antlr.ANTLRException;

public class TestTypesInner extends TestBase
{

    public TestTypesInner(String name)
    {
        super(name);
    }

    public void testDirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A { private class Inner extends B {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        ClassDeclaration classDeclB     = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testDirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A { private class Inner implements B {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testDirectImport3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A { private interface Inner extends B {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB     = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testImplicitImport1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A { private class Inner extends Exception {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test.A.Inner");
        ClassDeclaration classDeclEx    = (ClassDeclaration)_project.getType("java.lang.Exception");

        assertEquals(classDeclEx,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testImplicitImport2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A { private class Inner implements Clonable {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner        = (ClassDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclClonable = (InterfaceDeclaration)_project.getType("java.lang.Clonable");

        assertEquals(interfaceDeclClonable,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testImplicitImport3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "interface B { }\n"+
                "public class A { private interface Inner extends Clonable {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner    = (InterfaceDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclClonable = (InterfaceDeclaration)_project.getType("java.lang.Clonable");

        assertEquals(interfaceDeclClonable,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_DirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.C;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_DirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.C;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Enclosing1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n"+
                "public class A {\n"+
                "  private class Inner1 extends B {\n"+
                "    private class Deep implements Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Enclosing2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n"+
                "public class A {\n"+
                "  private class Inner1 extends B {\n"+
                "    private interface Deep extends Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OtherDeepInner1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected interface Deep {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements Inner2.Deep {}\n"+
                "  }\n"+
                "  private class Inner2 extends B {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep1     = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclDeep2 = (InterfaceDeclaration)_project.getType("test.B.Deep");

        assertEquals(interfaceDeclDeep2,
                     classDeclDeep1.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OtherDeepInner2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected interface Deep {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends Inner2.Deep {}\n"+
                "  }\n"+
                "  private class Inner2 extends B {}\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep1 = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclDeep2 = (InterfaceDeclaration)_project.getType("test.B.Deep");

        assertEquals(interfaceDeclDeep2,
                     interfaceDeclDeep1.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OtherTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class C extends B {}\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OtherTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class C extends B {}\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Qualified1() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements test1.C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Qualified2() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends test1.C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_SamePackage1() throws ANTLRException, ParseException
    {
        addType("test.C",
                "package test;\n"+
                "class B {\n"+
                "  interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_SamePackage2() throws ANTLRException, ParseException
    {
        addType("test.C",
                "package test;\n"+
                "class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n"+
                "public class C extends B { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends C.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_TopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {\n"+
                "  protected class Inner2 { }\n"+
                "}\n"+
                "public class A extends B {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep extends Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDeclDeep   = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        ClassDeclaration classDeclInner2 = (ClassDeclaration)_project.getType("test.B.Inner2");

        assertEquals(classDeclInner2,
                     classDeclDeep.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_TopLevel2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected class Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "class C extends B { }\n"+
                "public class A extends C {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep extends Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDeclDeep   = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        ClassDeclaration classDeclInner2 = (ClassDeclaration)_project.getType("test.B.Inner2");

        assertEquals(classDeclInner2,
                     classDeclDeep.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_TopLevel3() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected class Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep extends Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDeclDeep   = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        ClassDeclaration classDeclInner2 = (ClassDeclaration)_project.getType("test.B.Inner2");

        assertEquals(classDeclInner2,
                     classDeclDeep.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherDeepInner1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements Inner2.Deep {}\n"+
                "  }\n"+
                "  private class Inner2 {\n"+
                "    public interface Deep {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep1     = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclDeep2 = (InterfaceDeclaration)_project.getType("test.A.Inner2.Deep");

        assertEquals(interfaceDeclDeep2,
                     classDeclDeep1.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherDeepInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends Inner2.Deep {}\n"+
                "  }\n"+
                "  private class Inner2 {\n"+
                "    public interface Deep {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep1 = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclDeep2 = (InterfaceDeclaration)_project.getType("test.A.Inner2.Deep");

        assertEquals(interfaceDeclDeep2,
                     interfaceDeclDeep1.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep extends Inner2 {}\n"+
                "  }\n"+
                "  private class Inner2 {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration classDeclDeep   = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        ClassDeclaration classDeclInner2 = (ClassDeclaration)_project.getType("test.A.Inner2");

        assertEquals(classDeclInner2,
                     classDeclDeep.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements Inner2 {}\n"+
                "  }\n"+
                "  private interface Inner2 {}\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.A.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends Inner2 {}\n"+
                "  }\n"+
                "  private interface Inner2 {}\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.A.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements test1.B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {\n"+
                "  public interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends test1.B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test2.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test1.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private class Deep implements B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        ClassDeclaration     classDeclDeep       = (ClassDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     classDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected interface Inner2 {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class Inner1 {\n"+
                "    private interface Deep extends B.Inner2 {}\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclDeep   = (InterfaceDeclaration)_project.getType("test.A.Inner1.Deep");
        InterfaceDeclaration interfaceDeclInner2 = (InterfaceDeclaration)_project.getType("test.B.Inner2");

        assertEquals(interfaceDeclInner2,
                     interfaceDeclDeep.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testObject() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A { private class Inner {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner  = (ClassDeclaration)_project.getType("test.A.Inner");
        ClassDeclaration classDeclObject = (ClassDeclaration)_project.getType("java.lang.Object");

        assertEquals(classDeclObject,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A { private class Inner extends B {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        ClassDeclaration classDeclB     = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A { private class Inner implements B {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A { private interface Inner extends B {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB     = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualified1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A { private class Inner extends test1.B {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        ClassDeclaration classDeclB     = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualified2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A { private class Inner implements test1.B {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner = (ClassDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualified3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A { private interface Inner extends test1.B {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test2.A.Inner");
        InterfaceDeclaration interfaceDeclB     = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "class B { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A { private class Inner extends B {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test.A.Inner");
        ClassDeclaration classDeclB     = (ClassDeclaration)_project.getType("test.B");

        assertEquals(classDeclB,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testSamePackage2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "interface B { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A { private class Inner implements B {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner = (ClassDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testSamePackage3() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "interface B { }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A { private interface Inner extends B {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclB     = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B { }\n"+
                "public class A { private class Inner extends B {} }\n",
                true);
        resolve();

        ClassDeclaration classDeclInner = (ClassDeclaration)_project.getType("test.A.Inner");
        ClassDeclaration classDeclB     = (ClassDeclaration)_project.getType("test.B");

        assertEquals(classDeclB,
                     classDeclInner.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "interface B { }\n"+
                "public class A { private class Inner implements B {} }\n",
                true);
        resolve();

        ClassDeclaration     classDeclInner = (ClassDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     classDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "interface B { }\n"+
                "public class A { private interface Inner extends B {} }\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.A.Inner");
        InterfaceDeclaration interfaceDeclB     = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclInner.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }
}
