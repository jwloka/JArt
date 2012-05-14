package jart.test.analysis.duplication;
import jart.analysis.duplication.DuplicatedCodeFinder;
import jart.analysis.duplication.LinearView;
import jart.analysis.duplication.collections.CandidateArray;
import jart.analysis.duplication.collections.CandidateArrayIterator;
import jast.ParseException;
import jast.ast.nodes.MethodDeclaration;


public class TestDuplicatedCodeFinder extends jart.test.TestBase
{
    private   DuplicatedCodeFinder _testObject;

    public TestDuplicatedCodeFinder(String name)
    {
        super(name);
    }

    private void addTestClass() throws ParseException
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "}\n",
            true);
    }

    protected void setUp() throws ParseException
    {
        super.setUp();

        _testObject = new DuplicatedCodeFinder();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testAnalysis1() throws Exception
    {
        addTestClass();

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(1, _testObject.getCandidates().getCount());

        CandidateArray array1 = _testObject.getCandidates().getIterator().getNext();
        assertEquals(array1.get(0).getMatch(), array1.get(1).getMatch());
    }

    public void testAnalysis2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {}\n"
                + "  public void method2() {}\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {}\n"
                + "  public void method4() {}\n"
                + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(0, _testObject.getCandidates().getCount());

    }

    public void testAnalysis3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "  public void method4() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(1, _testObject.getCandidates().getCount());

        CandidateArray array1 = _testObject.getCandidates().getIterator().getNext();

        assertEquals(4, array1.getCount());
        assertEquals(array1.get(0).getMatch(), array1.get(1).getMatch());
        assertEquals(array1.get(1).getMatch(), array1.get(2).getMatch());
    }

    public void testAnalysis4() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    int x = 10;\n"
                + "    doSomething1(x);\n"
                + "    doSomething2(x);\n"
                + "    doSomething3(x);\n"
                + "    doSomething4(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
                + "    int x = 10;\n"
                + "    doSomething1(x);\n"
                + "    doSomething2(x);\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {\n"
                + "    int x = 10;\n"
                + "    doSomething1(x);\n"
                + "    doSomething2(x);\n"
                + "  }\n"
                + "  public void method4() {\n"
                + "    int x = 10;\n"
                + "    doSomething3(x);\n"
                + "    doSomething4(x);\n"
                + "  }\n"
                + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(1, _testObject.getCandidates().getCount());

        CandidateArray array1 = _testObject.getCandidates().getIterator().getNext();

        assertEquals(4, array1.getCount());
        assertEquals(array1.get(0).getMatch(), array1.get(1).getMatch());
        assertEquals(array1.get(1).getMatch(), array1.get(2).getMatch());
    }

    public void testAnalysis5() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
                + "    int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "  public void method4() {\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(2, _testObject.getCandidates().getCount());

        CandidateArrayIterator candidates = _testObject.getCandidates().getIterator();
        CandidateArray array1 = candidates.getNext();

        assertEquals("for-loops", 3, array1.getCount());
        assertEquals(array1.get(0).getMatch(), array1.get(1).getMatch());

        CandidateArray array2 = candidates.getNext();
        assertEquals("if-else-statments", 2, array2.getCount());
        assertEquals(array2.get(0).getMatch(), array2.get(1).getMatch());

    }

    public void testAnalysis6() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
                + "    int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "}\n",
            true);

        addType("Class2", "public class Class2 {\n" 
        	+ "  public void method3() {\n"
//        	+ "    int x = 10;\n"
        	+"     for (int idx = 0; idx < len; idx++) {\n"
            + "      doSomething1(idx);\n"
            + "      doSomething2(x);\n"
            + "    }\n"
            + "  }\n"
            + "  public void method4() {\n"
            + "    int x = 10;\n"
            + "    for (int idx = 0; idx < len; idx++) {\n"
            + "      doSomething1(idx);\n"
            + "      doSomething2(x);\n"
            + "    }\n"
            + "    if (var) \n"
            + "      doSomething3(x);\n"
            + "    else \n"
            + "      doSomething4(x);\n"
            + "  }\n"
            + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(3, _testObject.getCandidates().getCount());

        CandidateArrayIterator candidates = _testObject.getCandidates().getIterator();
        CandidateArray array1 = candidates.getNext();

        assertEquals("for-loop", 3, array1.getCount());

        CandidateArray array2 = candidates.getNext();
        assertEquals("if-else-statement", 2, array2.getCount());

        CandidateArray array3 = candidates.getNext();
        assertEquals("variable-declaration + for-loop", 2, array3.getCount());
    }

    public void testAnalysis7() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
                + "    int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {\n"
                + "       int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "  public void method4() {\n"
        //+ "    int x = 10;\n"
        +"    for (int idx = 0; idx < len; idx++) {\n"
            + "      doSomething1(idx);\n"
            + "      doSomething2(x);\n"
            + "    }\n"
            + "    if (var) \n"
            + "      doSomething3(x);\n"
            + "    else \n"
            + "      doSomething4(x);\n"
            + "  }\n"
            + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(3, _testObject.getCandidates().getCount());

        CandidateArrayIterator candidates = _testObject.getCandidates().getIterator();
        CandidateArray array1 = candidates.getNext();

        assertEquals("for-loop", 3, array1.getCount());

        CandidateArray array2 = candidates.getNext();
        assertEquals("if-else-statement", 2, array2.getCount());

        CandidateArray array3 = candidates.getNext();
        assertEquals("variable-declaration + for-loop", 2, array3.getCount());

    }

    public void testAnalysis8() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void method1() {\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "  public void method2() {\n"
        //+ "    int x = 10;\n"
        +"    for (int idx = 0; idx < len; idx++) {\n"
            + "      doSomething1(idx);\n"
            + "      doSomething2(x);\n"
            + "    }\n"
            + "  }\n"
            + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void method3() {\n"
                + "       int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "  }\n"
                + "  public void method4() {\n"
                + "    int x = 10;\n"
                + "    for (int idx = 0; idx < len; idx++) {\n"
                + "      doSomething1(idx);\n"
                + "      doSomething2(x);\n"
                + "    }\n"
                + "    if (var) \n"
                + "      doSomething3(x);\n"
                + "    else \n"
                + "      doSomething4(x);\n"
                + "  }\n"
                + "}\n",
            true);

        _testObject.setMinStatements(3);
        _testObject.addLinearViews(_project);
        _testObject.analize(_project);

        assertEquals(3, _testObject.getCandidates().getCount());

        CandidateArrayIterator candidates = _testObject.getCandidates().getIterator();
        CandidateArray array1 = candidates.getNext();

        assertEquals("for-loop", 3, array1.getCount());

        CandidateArray array2 = candidates.getNext();
        assertEquals("if-else-statement", 2, array2.getCount());

        CandidateArray array3 = candidates.getNext();
        assertEquals("variable-declaration + for-loop", 2, array3.getCount());
    }

    public void testBuildViews1() throws Exception
    {
        addTestClass();

        _testObject.addLinearViews(_project);

        assertNotNull(
            _project
                .getCompilationUnits()
                .get(1)
                .getTypeDeclarations()
                .get(0)
                .getMethods()
                .get(0)
                .getProperty(LinearView.PROPERTY_LABEL));
    }

    public void testBuildViews2() throws Exception
    {
        addTestClass();

        _testObject.addLinearViews(_project);

        MethodDeclaration meth =
            _project
                .getCompilationUnits()
                .get(1)
                .getTypeDeclarations()
                .get(0)
                .getMethods()
                .get(0);
        LinearView lv = new LinearView();
        lv.visitMethodDeclaration(meth);

        assertEquals(
            lv.getView(),
            (String) _project
                .getCompilationUnits()
                .get(1)
                .getTypeDeclarations()
                .get(0)
                .getMethods()
                .get(0)
                .getProperty(LinearView.PROPERTY_LABEL));
    }

    public void testBuildViews3() throws Exception
    {
        addTestClass();

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void doSame() {\n"
                + "    int x = 10;\n"
                + "    doSomethingMore1(x);\n"
                + "    doSomethingMore2(x);\n"
                + "  }\n"
                + "}\n",
            true);

        _testObject.addLinearViews(_project);

        MethodDeclaration meth =
            _project
                .getCompilationUnits()
                .get(1)
                .getTypeDeclarations()
                .get(0)
                .getMethods()
                .get(0);
        LinearView lv = new LinearView();
        lv.visitMethodDeclaration(meth);

        assertEquals(
            lv.getView(),
            (String) _project
                .getCompilationUnits()
                .get(1)
                .getTypeDeclarations()
                .get(0)
                .getMethods()
                .get(0)
                .getProperty(LinearView.PROPERTY_LABEL));
    }
}
