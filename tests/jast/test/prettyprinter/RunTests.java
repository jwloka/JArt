package jast.test.prettyprinter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunTests extends    TestCase
                      implements junitx.framework.TestPackage
{

    public RunTests(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
        //junit.swingui.TestRunner.run(RunTests.class);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Prettyprinter tests");

        // base elements
        suite.addTest(new TestSuite(TestIndentPrinter.class));
        suite.addTest(new TestSuite(TestType.class));
        suite.addTest(new TestSuite(TestLiteral.class));
        suite.addTest(new TestSuite(TestPrimary.class));
        suite.addTest(new TestSuite(TestExpression.class));

        return suite;
    }
}
