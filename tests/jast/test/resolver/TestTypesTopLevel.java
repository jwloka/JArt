package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import antlr.ANTLRException;

public class TestTypesTopLevel extends TestBase
{

    public TestTypesTopLevel(String name)
    {
        super(name);
    }

    public void testDefaultPackage1() throws ANTLRException, ParseException
    {
        addType("B",
                "public class B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "public class A extends B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testDefaultPackage2() throws ANTLRException, ParseException
    {
        addType("B",
                "public interface B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "public class A implements B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testDefaultPackage3() throws ANTLRException, ParseException
    {
        addType("B",
                "public interface B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "import B;\n"+
                "public interface A extends B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testDirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A extends B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testDirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A implements B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testDirectImport3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public interface A extends B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testImplicitImport1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A extends Exception {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA  = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclEx = (ClassDeclaration)_project.getType("java.lang.Exception");

        assertEquals(classDeclEx,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testImplicitImport2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A implements Clonable {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA            = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclClonable = (InterfaceDeclaration)_project.getType("java.lang.Clonable");

        assertEquals(interfaceDeclClonable,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testImplicitImport3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public interface A extends Clonable {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA        = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclClonable = (InterfaceDeclaration)_project.getType("java.lang.Clonable");

        assertEquals(interfaceDeclClonable,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_DirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "import test2.C;\n"+
                "public class A implements C.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_DirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C implements test1.B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public interface A extends test2.C.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "import test2.*;\n"+
                "public class A implements C.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_OnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class C extends B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "import test2.*;\n"+
                "public interface A extends C.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Qualified1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public class A implements test2.C.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_Qualified2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test3.A",
                "package test3;\n"+
                "public interface A extends test2.C.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test3.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_SamePackage1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A implements C.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_SamePackage2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.B { }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public interface A extends C.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_TopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "class C extends test1.B { }\n"+
                "public class A implements C.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInner_TopLevel2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "class C extends test1.B { }\n"+
                "public interface A extends C.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public class A implements B.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.B;\n"+
                "public interface A extends B.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A implements B.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public interface A extends B.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A implements test1.B.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B { public interface Inner {} }\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public interface A extends test1.B.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test1.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B { interface Inner {} }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A implements B.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B { interface Inner {} }\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public interface A extends B.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_TopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B { protected interface Inner {} }\n"+
                "public class A implements B.Inner {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA         = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.B.Inner");

        assertEquals(interfaceDeclInner,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_TopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B { protected interface Inner {} }\n"+
                "public interface A extends B.Inner {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA     = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclInner = (InterfaceDeclaration)_project.getType("test.B.Inner");

        assertEquals(interfaceDeclInner,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testObject() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA      = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclObject = (ClassDeclaration)_project.getType("java.lang.Object");

        assertEquals(classDeclObject,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A extends B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A implements B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public interface A extends B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualified1() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public class B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A extends test1.B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test2.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualified2() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A implements test1.B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualified3() throws ANTLRException, ParseException
    {
        addType("test1.B",
                "package test1;\n"+
                "public interface B {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public interface A extends test1.B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test2.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public class B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A extends B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testSamePackage2() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public interface B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A implements B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testSamePackage3() throws ANTLRException, ParseException
    {
        addType("test.B",
                "package test;\n"+
                "public interface B {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public interface A extends B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class B {}\n"+
                "public class A extends B {}\n",
                true);
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");

        assertEquals(classDeclB,
                     classDeclA.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "interface B {}\n"+
                "public class A implements B {}\n",
                true);
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     classDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testTopLevel3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "interface B {}\n"+
                "public interface A extends B {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test.B");

        assertEquals(interfaceDeclB,
                     interfaceDeclA.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }


    public void testDefaultPackage4() throws ANTLRException, ParseException
    {
        addType("A",
                "public interface A {}\n",
                true);
        addType("B",
                "public interface B {}\n",
                true);
        addType("C",
                "public interface C extends A, B {}\n",
                true);
        addType("D",
                "public interface D extends C {}\n",
                true);
        addType("E",
                "public interface E {}\n",
                true);
        addType("F",
                "public interface F {}\n",
                true);
        addType("G",
                "public interface G extends D, E, F {}\n",
                true);
        resolve();

        InterfaceDeclaration interfaceDeclA = (InterfaceDeclaration)_project.getType("A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("C");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)_project.getType("D");
        InterfaceDeclaration interfaceDeclE = (InterfaceDeclaration)_project.getType("E");
        InterfaceDeclaration interfaceDeclF = (InterfaceDeclaration)_project.getType("F");
        InterfaceDeclaration interfaceDeclG = (InterfaceDeclaration)_project.getType("G");

        assertEquals(interfaceDeclA,
                     interfaceDeclC.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
        assertEquals(interfaceDeclB,
                     interfaceDeclC.getBaseInterfaces().get(1).getReferenceBaseTypeDecl());

        assertEquals(interfaceDeclC,
                     interfaceDeclD.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());

        assertEquals(interfaceDeclD,
                     interfaceDeclG.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
        assertEquals(interfaceDeclE,
                     interfaceDeclG.getBaseInterfaces().get(1).getReferenceBaseTypeDecl());
        assertEquals(interfaceDeclF,
                     interfaceDeclG.getBaseInterfaces().get(2).getReferenceBaseTypeDecl());
    }
}
