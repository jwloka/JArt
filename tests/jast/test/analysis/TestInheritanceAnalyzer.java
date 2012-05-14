package jast.test.analysis;
import jast.ParseException;
import jast.analysis.InheritanceAnalyzer;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.TypeDeclarationArray;
import junitx.framework.TestAccessException;



public class TestInheritanceAnalyzer extends TestBase
{
    private InheritanceAnalyzer _testObject;

    public TestInheritanceAnalyzer(String name)
    {
        super(name);
    }

    private TypeDeclarationArray addAncestors(TypeDeclaration type) throws TestAccessException
    {
        Object[] args = { type };

        invoke(_testObject, "addAncestors", args);

        return (TypeDeclarationArray)get(_testObject, "_ancestors");
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
        _testObject = new InheritanceAnalyzer();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();
        _testObject = null;
    }

    public void testAddAncestors_ClassDeclaration1() throws ParseException, TestAccessException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Class2");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(0,
                     result.getCount());
    }

    public void testAddAncestors_ClassDeclaration2() throws ParseException, TestAccessException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Class2");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(1,
                     result.getCount());
        assertTrue(!result.get(0).equals(typeDecl));
    }

    public void testAddAncestors_ClassDeclaration3() throws ParseException, TestAccessException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {}\n",
                true);
        addType("Class4",
                "class Class4 extends Class3 {}\n",
                true);
        addType("Class5",
                "class Class5 extends Class4 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Class5");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(4,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration1() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface2");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(0,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration2() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 extends Interface1 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface2");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(1,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration3() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 {}\n",
                true);
        addType("Interface4",
                "interface Interface4 extends Interface1, Interface2, Interface3 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface4");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(3,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration4() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 extends Interface1 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 extends Interface2 {}\n",
                true);
        addType("Interface4",
                "interface Interface4 extends Interface3 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface4");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(3,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration5() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 extends Interface1, Interface2 {}\n",
                true);
        addType("Interface4",
                "interface Interface4 extends Interface3 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface4");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(3,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration6() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 extends Interface1, Interface2 {}\n",
                true);
        addType("Interface4",
                "interface Interface4 extends Interface3 {}\n",
                true);
        addType("Interface5",
                "interface Interface5 {}\n",
                true);
        addType("Interface6",
                "interface Interface6 {}\n",
                true);
        addType("Interface7",
                "interface Interface7 extends Interface5, Interface4, Interface6 {} \n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface7");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(6,
                     result.getCount());
    }

    public void testAddAncestors_InterfaceDeclaration7() throws ParseException, TestAccessException
    {
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 extends Interface1, Interface2 {}\n",
                true);
        addType("Interface4",
                "interface Interface4 extends Interface3 {}\n",
                true);
        addType("Interface5",
                "interface Interface5 {}\n",
                true);
        addType("Interface6",
                "interface Interface6 extends Interface5, Interface4 {}\n",
                true);

        resolve();

        TypeDeclaration      typeDecl = _project.getType("Interface6");
        TypeDeclarationArray result   = addAncestors(typeDecl);

        assertEquals(5,
                     result.getCount());
    }

    public void testGetAncestors1() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(0,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors2() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(1,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors3() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Class2",
                "class Class2 implements Interface1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(1,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors4() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 implements Interface1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(2,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors5() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 implements Interface1, Interface2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(3,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors6() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 extends Interface2 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 implements Interface1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(3,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors7() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 extends Interface2, Interface3 {}\n",
                true);
        addType("Interface2",
                "interface Interface2 {}\n",
                true);
        addType("Interface3",
                "interface Interface3 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 implements Interface1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(4,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors8() throws ParseException
    {
        addType("java.lang.Object",
                "package java.lang;\n"+
                "public class Object {}\n",
                true);
        addType("Class1",
                "class Class1 extends java.lang.Object {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {}\n",
                true);
        addType("Class4",
                "class Class4 extends Class3 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class4"));

        assertEquals(3,
                     _testObject.getAncestors().getCount());
    }

    public void testGetAncestors9() throws ParseException
    {
        addType("Class1",
                "class Class1 {}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 implements Interface1 {}\n",
                true);
        addType("Class4",
                "class Class4 extends Class3 {}\n",
                true);
        addType("Interface1",
                "interface Interface1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class4"));

        assertEquals(4,
                     _testObject.getAncestors().getCount());
    }

    public void testGetInheritedFields1() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(0,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields2() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(0,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields3() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  MyField otherField;\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(1,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields4() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  MyField field;\n"+
                "}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {\n"+
                "  MyField otherField;\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class3"));

        assertEquals(1,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields5() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  private MyField field1;\n"+
                "             MyField field2;\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(1,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields6() throws ParseException
    {
        addType("package1.Class1",
                "package package1;\n"+
                "public class Class1 {\n"+
                "  public MyField field1;\n"+
                "            MyField field2;\n"+
                "}\n",
                true);
        addType("package2.Class2",
                "package package2;\n"+
                "public class Class2 extends package1.Class1 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("package2.Class2"));

        assertEquals(1,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields7() throws ParseException
    {
        addType("package1.Class1",
                "package package1;\n"+
                "public class Class1 {\n"+
                "  protected MyField field1;\n"+
                "               MyField field2;\n"+
                "}\n",
                true);
        addType("package1.Class2",
                "package package1;\n"+
                "public class Class2 extends Class1 {}\n",
                true);
        addType("package2.Class3",
                "package package2;\n"+
                "public class Class3 extends package1.Class2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("package2.Class3"));

        assertEquals(1,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedFields8() throws ParseException
    {
        addType("package1.Class1",
                "package package1;\n"+
                "public class Class1 {\n"+
                "  MyField field2;\n"+
                "}\n",
                true);
        addType("package2.Class2",
                "package package2;\n"+
                "public class Class2 extends package1.Class1 {\n"+
                "  protected MyField otherField;\n"+
                "}\n",
                true);
        addType("package1.Class3",
                "package package1;\n"+
                "public class Class3 extends package2.Class2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("package1.Class3"));

        // 2 is WRONG -- unsequential inheritance hierarchy flattening doesnt work here properly
        assertEquals(2,
                     _testObject.getInheritedFields().getCount());
    }

    public void testGetInheritedMethods1() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(0,
                     _testObject.getInheritedMethods().getCount());
    }

    public void testGetInheritedMethods2() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(0,
                     _testObject.getInheritedMethods().getCount());
    }

    public void testGetInheritedMethods3() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  void otherMethod() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class2"));

        assertEquals(1,
                     _testObject.getInheritedMethods().getCount());
    }

    public void testGetInheritedMethods4() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {\n"+
                "  void otherMethod() {}\n"+
                "}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class3"));

        assertEquals(1,
                     _testObject.getInheritedMethods().getCount());
    }

    public void testGetInheritedMethods5() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method() {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class3"));

        assertEquals(1,
                     _testObject.getInheritedMethods().getCount());
    }

    public void testGetInheritedMethods6() throws ParseException
    {
        addType("Class1",
                "class Class1 {\n"+
                "  void method(Parameter param) {}\n"+
                "}\n",
                true);
        addType("Class2",
                "class Class2 extends Class1 {\n"+
                "  int method() { return 0; }\n"+
                "}\n",
                true);
        addType("Class3",
                "class Class3 extends Class2 {}\n",
                true);

        resolve();
        _testObject.setLocalType(_project.getType("Class3"));

        assertEquals(2,
                     _testObject.getInheritedMethods().getCount());
    }
}
