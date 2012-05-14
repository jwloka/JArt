package jart.test;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunAllTests extends    TestCase
                         implements junitx.framework.TestPackage
{

    public RunAllTests(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
        //junit.swingui.TestRunner.run(RunAllTests.class);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Tests");

        suite.addTest(jart.test.analysis.RunTests.suite());
        suite.addTest(jart.test.analysis.duplication.RunTests.suite());
        suite.addTest(jart.test.restructuring.RunTests.suite());
        suite.addTest(jart.test.smelling.RunTests.suite());

        return suite;
    }
}
