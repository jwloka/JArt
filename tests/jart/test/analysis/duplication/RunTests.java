package jart.test.analysis.duplication;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.framework.TestPackage;

public class RunTests extends    TestCase
                      implements TestPackage
{

    public RunTests(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite("Duplication tests");

        suite.addTest(new TestSuite(TestPatternMatcher.class));
        suite.addTest(new TestSuite(TestLinearView.class));
        suite.addTest(new TestSuite(TestLinearAnalysis.class));
        suite.addTest(new TestSuite(TestDuplicatedCodeFinder.class));

        suite.addTest(new TestSuite(TestStringPatternArray.class));

        return suite;
    }
}
