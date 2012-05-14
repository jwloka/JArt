package jast.test.resolver;

import jast.ParseException;
import jast.ast.nodes.AnonymousClassDeclaration;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Instantiation;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.SingleInitializer;
import jast.ast.nodes.TypeDeclaration;
import antlr.ANTLRException;

/*
 * Tests the resolving of an anonymous class in non-top-level type which extends
 *     * the owning type
 *     * other inner type of the owning type (not inherited)
 *     * other inner type of the owning type (inherited)
 *     * other owning type
 *     * other top-level type
 *     * inner type of other top-level type (not inherited)
 *     * inner type of other top-level type (inherited)
 *     * directly imported type
 *     * inner type of directly imported type (not inherited)
 *     * inner type of directly imported type (inherited)
 *     * on-demand imported type
 *     * inner type of on-demand imported type (not inherited)
 *     * inner type of on-demand imported type (inherited)
 *     * type in same package
 *     * inner type of type in same package (not inherited)
 *     * inner type of type in same package (inherited)
 *     * qualified type (not inherited)
 *     * inner type of qualified type (not inherited)
 *     * inner type of qualified type (inherited)
 */
public class TestAnonymousTypes extends TestBase
{

    public TestAnonymousTypes(String name)
    {
        super(name);
    }

    public void testDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDeclD     = _project.getType("test1.D");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDeclD,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDeclD,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDeclD,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testEnclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        C var = new C() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.A.B.C");
        MethodDeclaration         methodDecl    = typeDecl.getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInheritedInner() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        E var = new E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.F;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F.E var = new F.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F.E var = new F.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F.E var = new F.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class F extends test1.D {}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.F.E var = new test1.F.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInheritedInnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.F",
                "package test;\n"+
                "interface D {\n"+
                "  interface E {}\n"+
                "}\n"+
                "class F implements D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F.E var = new F.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_DirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Enclosing() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends C {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = (ClassDeclaration)_project.getType("test.A.B.C");
        MethodDeclaration         methodDecl    = typeDecl.getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(typeDecl,
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInner1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C extends test1.D {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInner2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.D var = new test1.D() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfDirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.F;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements F.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfDirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  protected interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.F;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F var = new F() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  public interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements F.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  protected interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F var = new F() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOtherTopLevel1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements F.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class F extends test1.D {}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfOtherTopLevel2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F var = new F() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class F extends test1.D {}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.F",
                "package test1;\n"+
                "interface D {\n"+
                "  protected interface E {}\n"+
                "}\n"+
                "public class F implements D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.F var = new test1.F() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.F",
                "package test;\n"+
                "interface D {\n"+
                "  interface E {}\n"+
                "}\n"+
                "class F implements D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements F.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InheritedInnerOfSamePackage2() throws ANTLRException, ParseException
    {
        addType("test.F",
                "package test;\n"+
                "interface D {\n"+
                "  interface E {}\n"+
                "}\n"+
                "class F implements D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        F var = new F() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfDirectImport1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfDirectImport2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOnDemandImport1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOnDemandImport2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOtherTopLevel1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfOtherTopLevel2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() {\n"+
                "          class X extends E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {\n"+
                "  protected class E {}\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfQualified1() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements test1.D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfQualified2() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  protected interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.D var = new test1.D() {\n"+
                "          class X implements test1.D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfSamePackage1() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {\n"+
                "  interface E {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D.E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_InnerOfSamePackage2() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {\n"+
                "  interface E {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() {\n"+
                "          class X implements E {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D.E"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner1() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private interface D {}\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherInner2() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          interface D {}\n"+
                "          class X implements D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl     = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl        = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation  = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass      = instantiation.getAnonymousClass();
        InterfaceDeclaration      interfaceDeclD = (InterfaceDeclaration)anonClass.getInnerTypes().get("D");
        ClassDeclaration          classDeclX     = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(interfaceDeclD,
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_OtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Outer() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends A {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.A"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_Qualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends test1.D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_QualifiedOtherInner() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private interface D {}\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X implements test.A.B.C.D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.A.B.C.D"),
                     classDeclX.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInner_QualifiedOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends test.A.B {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.A.B"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_RemoteInheritedInner() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D extends E {}\n"+
                "class E {\n"+
                "  public class F {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() {\n"+
                "          class X extends F {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test1.E.F"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInner_SamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        Object var = new Object() {\n"+
                "          class X extends D {}\n"+
                "        };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();
        ClassDeclaration          classDeclX    = (ClassDeclaration)anonClass.getInnerTypes().get("X");

        assertEquals(_project.getType("test.D"),
                     classDeclX.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInnerOfDirectImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.E var = new D.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInnerOfOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.E var = new D.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInnerOfOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.E var = new D.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {\n"+
                "  protected class E {}\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testInnerOfQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {\n"+
                "  public interface E {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.D.E var = new test1.D.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testInnerOfSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {\n"+
                "  interface E {}\n"+
                "}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.E var = new D.E() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.D.E");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOnDemandImport() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public interface D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.*;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOtherInner() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private interface D {}\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.A.B.C.D");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testOtherTopLevel() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n"+
                "class D {}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.D");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        B var = new B() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.A.B");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualified() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D {}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test1.D var = new test1.D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.D");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testQualifiedOtherInner() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private interface D {}\n"+
                "      private void doSomething() {\n"+
                "        test.A.B.C.D var = new test.A.B.C.D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.A.B.C.D");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        // As the base type is an interface, the anonymous class must inherit from
        // java.lang.Object
        assertEquals(_project.getType("java.lang.Object"),
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseInterfaces().get(0).getReferenceBaseTypeDecl());
    }

    public void testQualifiedOuter() throws ANTLRException, ParseException
    {
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        test.A.B var = new test.A.B() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.A.B");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testRemoteInheritedInner() throws ANTLRException, ParseException
    {
        addType("test1.D",
                "package test1;\n"+
                "public class D extends E {}\n"+
                "class E {\n"+
                "  public class F {}\n"+
                "}\n",
                false);
        addType("test2.A",
                "package test2;\n"+
                "import test1.D;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D.F var = new D.F() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test1.E.F");
        MethodDeclaration         methodDecl    = _project.getType("test2.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }

    public void testSamePackage() throws ANTLRException, ParseException
    {
        addType("test.D",
                "package test;\n"+
                "class D {}\n",
                false);
        addType("test.A",
                "package test;\n"+
                "public class A {\n"+
                "  private class B {\n"+
                "    private class C {\n"+
                "      private void doSomething() {\n"+
                "        D var = new D() { };\n"+
                "      }\n"+
                "    }\n"+
                "  }\n"+
                "}\n",
                true);
        resolve();

        TypeDeclaration           typeDecl      = _project.getType("test.D");
        MethodDeclaration         methodDecl    = _project.getType("test.A.B.C").getMethods().get(0);
        LocalVariableDeclaration  varDecl       = (LocalVariableDeclaration)methodDecl.getBody().getBlockStatements().get(0);
        Instantiation             instantiation = (Instantiation)((SingleInitializer)varDecl.getInitializer()).getInitEpression();
        AnonymousClassDeclaration anonClass     = instantiation.getAnonymousClass();

        assertEquals(typeDecl,
                     instantiation.getInstantiatedType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseType().getReferenceBaseTypeDecl());
        assertEquals(typeDecl,
                     anonClass.getBaseClass().getReferenceBaseTypeDecl());
    }
}
