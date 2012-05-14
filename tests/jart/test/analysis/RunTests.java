package jart.test.analysis;
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

        suite.addTest(new TestSuite(TestIsCollectionClass.class));
        suite.addTest(new TestSuite(TestIsTestClass.class));
        suite.addTest(new TestSuite(TestIsUtilityClass.class));
        suite.addTest(new TestSuite(TestIsUtilityMethod.class));

        return suite;
    }
}
