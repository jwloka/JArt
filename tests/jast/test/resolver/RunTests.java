package jast.test.resolver;
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
        TestSuite suite = new TestSuite("Resolver tests");

        // Helpers
        suite.addTest(new TestSuite(TestIsApplicable.class));
        suite.addTest(new TestSuite(TestMostSpecific.class));

        // types
        suite.addTest(new TestSuite(TestCanAccess.class));
        suite.addTest(new TestSuite(TestTypesTopLevel.class));
        suite.addTest(new TestSuite(TestTypesInner.class));
        suite.addTest(new TestSuite(TestTypesLocal.class));
        suite.addTest(new TestSuite(TestTypesOcclusion.class));
        suite.addTest(new TestSuite(TestAnonymousTypes.class));
        suite.addTest(new TestSuite(TestInstantiationTypes.class));

        // features
        suite.addTest(new TestSuite(TestFieldAccessibility.class));
        suite.addTest(new TestSuite(TestFieldOcclusion.class));
        suite.addTest(new TestSuite(TestConstructorAccessibility.class));
        suite.addTest(new TestSuite(TestConstructorArguments.class));
        suite.addTest(new TestSuite(TestConstructorMostSpecific.class));
        suite.addTest(new TestSuite(TestMethodAccessibility.class));
        suite.addTest(new TestSuite(TestMethodArguments.class));
        suite.addTest(new TestSuite(TestMethodMostSpecific.class));
        suite.addTest(new TestSuite(TestMethodDepth.class));

        // Specific nodes
        suite.addTest(new TestSuite(TestExpressions.class));
        suite.addTest(new TestSuite(TestArrayAccess.class));
        suite.addTest(new TestSuite(TestSelfAccess.class));
        suite.addTest(new TestSuite(TestUnresolvedAccess.class));

        return suite;
    }
}
