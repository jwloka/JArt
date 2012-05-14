package jast.test.resolver;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.resolver.Resolver;
import antlr.ANTLRException;

// Tests accessibility. Note that some of the tested types would
// not compile due to the used modifier combination - this however
// is not interesting to the tests
public class TestCanAccess extends TestBase
{

    public TestCanAccess(String name)
    {
        super(name);
    }

    private void addType(String qualifiedName, String compUnit) throws ParseException
    {
        super.addType(qualifiedName, compUnit, true);
    }

    public void testCurrentType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclA));
        assertTrue(Resolver.canAccess(classDeclB, classDeclB));
    }

    public void testCurrentType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclA));
        assertTrue(Resolver.canAccess(interfaceDeclB, interfaceDeclB));
    }

    public void testCurrentType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclA));
        assertTrue(Resolver.canAccess(classDeclB, classDeclB));
    }

    public void testCurrentType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclA));
        assertTrue(Resolver.canAccess(interfaceDeclB, interfaceDeclB));
    }

    public void testDeepType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private interface C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration     classDeclB     = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclC));
        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclC));
    }

    public void testDeepType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    protected class C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
        assertTrue(Resolver.canAccess(classDeclB, classDeclC));
    }

    public void testDeepType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    interface C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration     classDeclB     = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclC));
        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclC));
    }

    public void testDeepType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    public class C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
        assertTrue(Resolver.canAccess(classDeclB, classDeclC));
    }

    public void testEnclosingType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "private class A {\n"+
                "  private class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
    }

    public void testEnclosingType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "protected class A {\n"+
                "  private interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(interfaceDeclB, classDeclA));
    }

    public void testEnclosingType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
    }

    public void testEnclosingType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(interfaceDeclB, classDeclA));
    }

    public void testInheritedDeepType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test2.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclC));
    }

    public void testInheritedDeepType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected interface C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test2.D");

        assertTrue(!Resolver.canAccess(classDeclD, interfaceDeclC));
    }

    public void testInheritedDeepType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    class C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test2.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclC));
    }

    public void testInheritedDeepType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public interface C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test2.D");

        assertTrue(Resolver.canAccess(classDeclD, interfaceDeclC));
    }

    public void testInheritedDeepType5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    class C {}\n"+
                "  }\n"+
                "}\n");
        addType("test.D",
                "package test;\n"+
                "public class D extends A { }\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.B.C");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test.D");

        assertTrue(Resolver.canAccess(classDeclD, classDeclC));
    }

    public void testInheritedInnerType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test2.C");

        assertTrue(!Resolver.canAccess(classDeclC, classDeclB));
    }

    public void testInheritedInnerType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclC     = (ClassDeclaration)_project.getType("test2.C");

        assertTrue(Resolver.canAccess(classDeclC, interfaceDeclB));
    }

    public void testInheritedInnerType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test2.C");

        assertTrue(!Resolver.canAccess(classDeclC, classDeclB));
    }

    public void testInheritedInnerType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclC     = (ClassDeclaration)_project.getType("test2.C");

        assertTrue(Resolver.canAccess(classDeclC, interfaceDeclB));
    }

    public void testInnerType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclB));
    }

    public void testInnerType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclB));
    }

    public void testInnerType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, classDeclB));
    }

    public void testInnerType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)classDeclA.getInnerTypes().get("B");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclB));
    }

    public void testLocalType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    class Local {\n"+
                "      class Inner {}\n"+
                "    }\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA     = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB     = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration classDeclC     = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration classDeclLocal = (ClassDeclaration)classDeclA.getMethods().get(0).getBody().getBlockStatements().get(0);
        ClassDeclaration classDeclInner = (ClassDeclaration)classDeclLocal.getInnerTypes().get("Inner");

        assertTrue(!Resolver.canAccess(classDeclA, classDeclLocal));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclInner));

        assertTrue(Resolver.canAccess(classDeclLocal, classDeclLocal));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclInner));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclA));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclB));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclC));

        assertTrue(Resolver.canAccess(classDeclInner, classDeclLocal));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclInner));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclA));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclB));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclC));
    }

    public void testLocalType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  private void doSomething() {\n"+
                "    class Local {\n"+
                "      void doSomething() {\n"+
                "        class Inner {}\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA     = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB     = (ClassDeclaration)classDeclA.getInnerTypes().get("B");
        ClassDeclaration classDeclC     = (ClassDeclaration)classDeclB.getInnerTypes().get("C");
        ClassDeclaration classDeclLocal = (ClassDeclaration)classDeclA.getMethods().get(0).getBody().getBlockStatements().get(0);
        ClassDeclaration classDeclInner = (ClassDeclaration)classDeclLocal.getMethods().get(0).getBody().getBlockStatements().get(0);

        assertTrue(!Resolver.canAccess(classDeclA, classDeclLocal));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclInner));

        assertTrue(Resolver.canAccess(classDeclLocal, classDeclLocal));
        assertTrue(!Resolver.canAccess(classDeclLocal, classDeclInner));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclA));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclB));
        assertTrue(Resolver.canAccess(classDeclLocal, classDeclC));

        assertTrue(Resolver.canAccess(classDeclInner, classDeclLocal));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclInner));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclA));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclB));
        assertTrue(Resolver.canAccess(classDeclInner, classDeclC));
    }

    public void testOtherDeepType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  private class C {\n"+
                "    private interface D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclD));
    }

    public void testOtherDeepType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  protected class C {\n"+
                "    protected class D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclB, classDeclD));
    }

    public void testOtherDeepType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  public class C {\n"+
                "    interface D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclD));
    }

    public void testOtherDeepType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  public class C {\n"+
                "    public class D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclB, classDeclD));
    }

    public void testOtherDeepType5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  private class D {\n"+
                "    private interface E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclC     = (ClassDeclaration)_project.getType("test.A.B.C");
        InterfaceDeclaration interfaceDeclE = (InterfaceDeclaration)_project.getType("test.A.D.E");

        assertTrue(Resolver.canAccess(classDeclC, interfaceDeclE));
    }

    public void testOtherDeepType6() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  protected class D {\n"+
                "    protected class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.B.C");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test.A.D.E");

        assertTrue(Resolver.canAccess(classDeclC, classDeclE));
    }

    public void testOtherDeepType7() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  public class D {\n"+
                "    interface E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclC     = (ClassDeclaration)_project.getType("test.A.B.C");
        InterfaceDeclaration interfaceDeclE = (InterfaceDeclaration)_project.getType("test.A.D.E");

        assertTrue(Resolver.canAccess(classDeclC, interfaceDeclE));
    }

    public void testOtherDeepType8() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "  public class D {\n"+
                "    public class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.B.C");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test.A.D.E");

        assertTrue(Resolver.canAccess(classDeclC, classDeclE));
    }

    public void testOtherInheritedInnerType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test2.C.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testOtherInheritedInnerType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {}\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test2.C.D");

        assertTrue(Resolver.canAccess(classDeclD, interfaceDeclB));
    }

    public void testOtherInheritedInnerType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test2.C.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testOtherInheritedInnerType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {}\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test2.C.D");

        assertTrue(Resolver.canAccess(classDeclD, interfaceDeclB));
    }

    public void testOtherInheritedInnerType5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {\n"+
                "    private class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test2.C.D.E");

        assertTrue(!Resolver.canAccess(classDeclE, classDeclB));
    }

    public void testOtherInheritedInnerType6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {\n"+
                "    private class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test2.C.D.E");

        assertTrue(Resolver.canAccess(classDeclE, interfaceDeclB));
    }

    public void testOtherInheritedInnerType7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {\n"+
                "    private class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test2.C.D.E");

        assertTrue(!Resolver.canAccess(classDeclE, classDeclB));
    }

    public void testOtherInheritedInnerType8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A {\n"+
                "  private class D {\n"+
                "    private class E {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test2.C.D.E");

        assertTrue(Resolver.canAccess(classDeclE, interfaceDeclB));
    }

    public void testOtherInnerType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  private class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclC));
    }

    public void testOtherInnerType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  protected interface C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.A.C");

        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclC));
    }

    public void testOtherInnerType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclC));
    }

    public void testOtherInnerType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  public interface C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.A.C");

        assertTrue(Resolver.canAccess(classDeclB, interfaceDeclC));
    }

    public void testOtherInnerType5() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "  private class C {\n"+
                "    private interface D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(interfaceDeclD, classDeclB));
    }

    public void testOtherInnerType6() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  protected class B {}\n"+
                "  private class C {\n"+
                "    private class D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testOtherInnerType7() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "  private class C {\n"+
                "    private interface D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.A.B");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(interfaceDeclD, classDeclB));
    }

    public void testOtherInnerType8() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  public class B {}\n"+
                "  private class C {\n"+
                "    private class D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test.A.C.D");

        assertTrue(Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testOtherInnerType9() throws ANTLRException, ParseException
    {
        addType("test1.C",
                "package test1;\n"+
                "class A {\n"+
                "  public interface B {}\n"+
                "}\n"+
                "public class C extends A { }\n");
        addType("test2.D",
                "package test2;\n"+
                "import test1.C;\n"+
                "public class D { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test2.D");

        assertTrue(!Resolver.canAccess(classDeclD, interfaceDeclB));
    }

    public void testOtherRemotelyInheritedInnerType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test3.D.E");

        assertTrue(!Resolver.canAccess(classDeclE, classDeclB));
    }

    public void testOtherRemotelyInheritedInnerType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {}\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test3.D.E");

        assertTrue(Resolver.canAccess(classDeclE, interfaceDeclB));
    }

    public void testOtherRemotelyInheritedInnerType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test3.D.E");

        assertTrue(!Resolver.canAccess(classDeclE, classDeclB));
    }

    public void testOtherRemotelyInheritedInnerType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {}\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test3.D.E");

        assertTrue(Resolver.canAccess(classDeclE, interfaceDeclB));
    }

    public void testOtherRemotelyInheritedInnerType5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {\n"+
                "    private class F {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclF = (ClassDeclaration)_project.getType("test3.D.E.F");

        assertTrue(!Resolver.canAccess(classDeclF, classDeclB));
    }

    public void testOtherRemotelyInheritedInnerType6() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {\n"+
                "    private class F {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclF     = (ClassDeclaration)_project.getType("test3.D.E.F");

        assertTrue(Resolver.canAccess(classDeclF, interfaceDeclB));
    }

    public void testOtherRemotelyInheritedInnerType7() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {\n"+
                "    private class F {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclF = (ClassDeclaration)_project.getType("test3.D.E.F");

        assertTrue(!Resolver.canAccess(classDeclF, classDeclB));
    }

    public void testOtherRemotelyInheritedInnerType8() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C {\n"+
                "  private class E {\n"+
                "    private class F {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclF     = (ClassDeclaration)_project.getType("test3.D.E.F");

        assertTrue(Resolver.canAccess(classDeclF, interfaceDeclB));
    }

    public void testOtherTopLevelType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {}\n"+
                "public class B {\n"+
                "  private interface C {\n"+
                "    private interface D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration     classDeclB     = (ClassDeclaration)_project.getType("test.B");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)classDeclB.getInnerTypes().get("C");
        InterfaceDeclaration interfaceDeclD = (InterfaceDeclaration)interfaceDeclC.getInnerTypes().get("D");

        assertTrue(Resolver.canAccess(classDeclA, classDeclB));
        assertTrue(!Resolver.canAccess(classDeclA, interfaceDeclC));
        assertTrue(!Resolver.canAccess(classDeclA, interfaceDeclD));
        assertTrue(Resolver.canAccess(interfaceDeclC, classDeclA));
        assertTrue(Resolver.canAccess(interfaceDeclD, classDeclA));
    }

    public void testOtherTopLevelType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {}\n"+
                "public class B {\n"+
                "  protected class C {\n"+
                "    protected class D {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.B.C");
        ClassDeclaration classDeclD = (ClassDeclaration)classDeclC.getInnerTypes().get("D");

        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
        assertTrue(Resolver.canAccess(classDeclA, classDeclD));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(Resolver.canAccess(classDeclD, classDeclA));
    }

    public void testOtherTopLevelType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {}\n"+
                "public class B {\n"+
                "  interface C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.B.C");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclC));
        assertTrue(Resolver.canAccess(interfaceDeclC, classDeclA));
    }

    public void testOtherTopLevelType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {}\n"+
                "public class B {\n"+
                "  public interface C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.B.C");

        assertTrue(Resolver.canAccess(classDeclA, interfaceDeclC));
        assertTrue(Resolver.canAccess(interfaceDeclC, classDeclA));
    }

    public void testOuterType1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "private class A {\n"+
                "  private class B {\n"+
                "    private interface C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.A.B.C");

        assertTrue(Resolver.canAccess(interfaceDeclC, classDeclA));
    }

    public void testOuterType2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "protected class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.B.C");

        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
    }

    public void testOuterType3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {\n"+
                "  private class B {\n"+
                "    private interface C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration     classDeclA     = (ClassDeclaration)_project.getType("test.A");
        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test.A.B.C");

        assertTrue(Resolver.canAccess(interfaceDeclC, classDeclA));
    }

    public void testOuterType4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {}\n"+
                "  }\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test.A.B.C");

        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
    }

    public void testRemotelyInheritedDeepType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    private interface C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        addType("test3.E",
                "package test3;\n"+
                "public class E extends test2.D { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test3.E");

        assertTrue(!Resolver.canAccess(classDeclE, interfaceDeclC));
    }

    public void testRemotelyInheritedDeepType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected class B {\n"+
                "    protected class C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        addType("test3.E",
                "package test3;\n"+
                "public class E extends test2.D { }\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test3.E");

        assertTrue(!Resolver.canAccess(classDeclE, classDeclC));
    }

    public void testRemotelyInheritedDeepType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    interface C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        addType("test3.E",
                "package test3;\n"+
                "public class E extends test2.D { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclC = (InterfaceDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration     classDeclE     = (ClassDeclaration)_project.getType("test3.E");

        assertTrue(!Resolver.canAccess(classDeclE, interfaceDeclC));
    }

    public void testRemotelyInheritedDeepType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public class B {\n"+
                "    public class C {}\n"+
                "  }\n"+
                "}\n");
        addType("test2.D",
                "package test2;\n"+
                "public class D extends test1.A { }\n");
        addType("test3.E",
                "package test3;\n"+
                "public class E extends test2.D { }\n");
        resolve();

        ClassDeclaration classDeclC = (ClassDeclaration)_project.getType("test1.A.B.C");
        ClassDeclaration classDeclE = (ClassDeclaration)_project.getType("test3.E");

        assertTrue(Resolver.canAccess(classDeclE, classDeclC));
    }

    public void testRemotelyInheritedInnerType1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  private class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C { }\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test3.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testRemotelyInheritedInnerType2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  protected interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test3.D");

        assertTrue(Resolver.canAccess(classDeclD, interfaceDeclB));
    }

    public void testRemotelyInheritedInnerType3() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  class B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C { }\n");
        resolve();

        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test1.A.B");
        ClassDeclaration classDeclD = (ClassDeclaration)_project.getType("test3.D");

        assertTrue(!Resolver.canAccess(classDeclD, classDeclB));
    }

    public void testRemotelyInheritedInnerType4() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {\n"+
                "  public interface B {}\n"+
                "}\n");
        addType("test2.C",
                "package test2;\n"+
                "public class C extends test1.A { }\n");
        addType("test3.D",
                "package test3;\n"+
                "public class D extends test2.C { }\n");
        resolve();

        InterfaceDeclaration interfaceDeclB = (InterfaceDeclaration)_project.getType("test1.A.B");
        ClassDeclaration     classDeclD     = (ClassDeclaration)_project.getType("test3.D");

        assertTrue(Resolver.canAccess(classDeclD, interfaceDeclB));
    }

    public void testTypeInOtherPackage1() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "public class A {}\n");
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  private class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test2.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInOtherPackage2() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "class A {}\n");
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  private class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test2.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(!Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclB));
        assertTrue(!Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInOtherPackage3() throws ANTLRException, ParseException
    {
        addType("A",
                "public class A {}\n");
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInOtherPackage4() throws ANTLRException, ParseException
    {
        addType("A",
                "public class A {}\n");
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInOtherPackage5() throws ANTLRException, ParseException
    {
        addType("test1.A",
                "package test1;\n"+
                "class A {}\n");
        addType("test2.B",
                "package test2;\n"+
                "public class B {\n"+
                "  public class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test1.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test2.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(!Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclB));
        assertTrue(!Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n");
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  private class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(!Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInSamePackage2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "class A {}\n");
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  protected class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInSamePackage3() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n");
        addType("test.B",
                "package test;\n"+
                "class B {\n"+
                "  class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
    }

    public void testTypeInSamePackage4() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {}\n");
        addType("test.B",
                "package test;\n"+
                "public class B {\n"+
                "  public class C {}\n"+
                "}\n");
        resolve();

        ClassDeclaration classDeclA = (ClassDeclaration)_project.getType("test.A");
        ClassDeclaration classDeclB = (ClassDeclaration)_project.getType("test.B");
        ClassDeclaration classDeclC = (ClassDeclaration)classDeclB.getInnerTypes().get("C");

        assertTrue(Resolver.canAccess(classDeclB, classDeclA));
        assertTrue(Resolver.canAccess(classDeclC, classDeclA));
        assertTrue(Resolver.canAccess(classDeclA, classDeclC));
    }
}
