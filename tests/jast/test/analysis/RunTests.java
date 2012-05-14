package jast.test.analysis;
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
        TestSuite suite = new TestSuite("Analysis tests");

        suite.addTest(new TestSuite(TestInheritanceAnalyzer.class));
        suite.addTest(new TestSuite(TestMethodAnalyzer.class));

        // usage relations
        suite.addTest(new TestSuite(TestTypeUsage.class));
        suite.addTest(new TestSuite(TestUsages.class));
        suite.addTest(new TestSuite(TestFeatureUsages.class));
        suite.addTest(new TestSuite(TestUsageVisitor.class));
        suite.addTest(new TestSuite(TestUsageAnalyzer.class));

        // usage collections
        suite.addTest(new TestSuite(TestTypeUsageArray.class));
        suite.addTest(new TestSuite(TestUsagesStack.class));

        return suite;
    }
}
