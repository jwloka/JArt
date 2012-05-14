package jart.test.analysis.duplication;
import jart.analysis.duplication.LinearAnalysis;
import jart.analysis.duplication.LinearView;
import jart.analysis.duplication.collections.CandidateArrayTable;
import jast.ParseException;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;


public class TestLinearAnalysis extends jart.test.TestBase
{
    private   LinearAnalysis _testObject;

    public TestLinearAnalysis(String name)
    {
        super(name);
    }

    private void addLinearViews(TypeDeclaration type)
    {
        LinearView lv = new LinearView();

        for (int idx = 0; idx < type.getMethods().getCount(); idx++)
            {
            InvocableDeclaration cur = type.getMethods().get(idx);
            lv.visitInvocableDeclaration(cur);
            cur.setProperty(LinearView.PROPERTY_LABEL, lv.getView());

            //System.out.println(lv.getView());
        }
    }

    protected void setUp() throws ParseException
    {
        super.setUp();
    }

    protected void tearDown() throws ParseException
    {
        super.tearDown();

        _testObject = null;
    }

    public void testForMatch1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    for (;;)\n"
                + "      break;\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    for (;;) {\n"
                + "      break;\n"
                + "    }\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);

        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.visitProject(_project);

        assertEquals(2, _testObject.getCandidates().get(linearView).getCount());
    }

    public void testForMatch2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    for (;;)\n"
                + "      break;\n"
                + "  }\n"
                + "  public void doDifferent() {\n"
                + "    for (;;) \n"
                + "      doSomethingElse();\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);

        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.visitProject(_project);

        assertEquals(null, _testObject.getCandidates().get(linearView));
    }

    public void testIfThenElseMatch1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    if (var)\n"
                + "      break;\n"
                + "    else\n"
                + "      continue;\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    if (var) {\n"
                + "      break;\n"
                + "    }\n"
                + "    else {\n"
                + "      continue;\n"
                + "    }\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);

        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.visitProject(_project);

        assertEquals(2, _testObject.getCandidates().get(linearView).getCount());
    }

    public void testIfThenElseMatch2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    if (var)\n"
                + "      break;\n"
                + "    else\n"
                + "      continue;\n"
                + "  }\n"
                + "  public void doNearlySame() {\n"
                + "    if (var)\n"
                + "      break;\n"
                + "    else\n"
                + "      doSomethingElse();\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);

        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setMinStatementLength(3);
        _testObject.setCandidates(new CandidateArrayTable());

        _testObject.visitProject(_project);

        assertEquals(2, _testObject.getCandidates().get("I[BS][").getCount());
    }

    public void testSetGetMinStringLength1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "  public void doSomething() {}\n" + "}\n",
            true);
        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);
        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        _testObject = new LinearAnalysis(meth);

        _testObject.setMinStatementLength(3);

        assertEquals(6, getInt(_testObject, "_minStringLength"));
        assertEquals(3, _testObject.getMinStatementLength());
    }

    public void testSetGetMinStringLength2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n" + "  public void doSomething() {}\n" + "}\n",
            true);
        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);
        addLinearViews(type1);

        MethodDeclaration meth = type1.getMethods().get(0);
        _testObject = new LinearAnalysis(meth);

        _testObject.setMinStatementLength(0);

        assertEquals(0, getInt(_testObject, "_minStringLength"));
        assertEquals(0, _testObject.getMinStatementLength());
    }

    public void testSimpleMatch1() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    break;\n"
                + "    break;\n"
                + "    break;\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    break;\n"
                + "    break;\n"
                + "    break;\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);

        addLinearViews(type);

        MethodDeclaration meth = type.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.visitClassDeclaration((ClassDeclaration) type);

        assertEquals(2, _testObject.getCandidates().get(linearView).getCount());
    }

    public void testSimpleMatch2() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    int x = 10;\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    int x = 10;\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void doSame() {\n"
                + "    int x = 10;\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);
        TypeDeclaration type2 =
            _project.getCompilationUnits().get(2).getTypeDeclarations().get(0);

        addLinearViews(type1);
        addLinearViews(type2);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.setMinStatementLength(1);
        _testObject.visitProject(_project);

        assertEquals(3, _testObject.getCandidates().get(linearView).getCount());
    }

    public void testSimpleMatch3() throws Exception
    {
        addType(
            "Class1",
            "public class Class1 {\n"
                + "  public void doSomething() {\n"
                + "    doSomethingElse();\n"
                + "  }\n"
                + "  public void doSame() {\n"
                + "    doSomethingElse();\n"
                + "  }\n"
                + "}\n",
            true);

        addType(
            "Class2",
            "public class Class2 {\n"
                + "  public void doSame() {\n"
                + "    doSomethingElse();\n"
                + "  }\n"
                + "}\n",
            true);

        TypeDeclaration type1 =
            _project.getCompilationUnits().get(1).getTypeDeclarations().get(0);
        TypeDeclaration type2 =
            _project.getCompilationUnits().get(2).getTypeDeclarations().get(0);

        addLinearViews(type1);
        addLinearViews(type2);

        MethodDeclaration meth = type1.getMethods().get(0);
        String linearView = (String) meth.getProperty(LinearView.PROPERTY_LABEL);

        _testObject = new LinearAnalysis(meth);
        _testObject.setCandidates(new CandidateArrayTable());
        _testObject.setMinStatementLength(1);
        _testObject.visitProject(_project);

        assertEquals(3, _testObject.getCandidates().get(linearView).getCount());
    }
}
