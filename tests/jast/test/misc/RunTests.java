package jast.test.misc;
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
        TestSuite suite = new TestSuite("Misceancellous tests");

        // Helpers
        suite.addTest(new TestSuite(TestHelpersStringArray.class));
        suite.addTest(new TestSuite(TestHelpersStringMap.class));
        suite.addTest(new TestSuite(TestHelpersHashArray.class));
        suite.addTest(new TestSuite(TestHelpersSortedArray.class));

        // Other tests
        suite.addTest(new TestSuite(TestNodeEquality.class));
        suite.addTest(new TestSuite(TestIsSubTypeOf.class));
        suite.addTest(new TestSuite(TestIsAssignmentCompatibleTo.class));

        return suite;
    }
}
