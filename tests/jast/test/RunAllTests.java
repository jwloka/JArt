package jast.test;
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
        //junit.swingui.TestRunner.run(RunTests.class);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("All Tests");

        suite.addTest(jast.test.lexer.RunTests.suite());
        suite.addTest(jast.test.misc.RunTests.suite());
        suite.addTest(jast.test.parser.RunTests.suite());
        suite.addTest(jast.test.resolver.RunTests.suite());
        suite.addTest(jast.test.analysis.RunTests.suite());
        suite.addTest(jast.test.prettyprinter.RunTests.suite());

        return suite;
    }
}
