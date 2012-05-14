
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends    TestCase
                         implements junitx.framework.TestPackage
{

    public AllTests(String name)
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

        suite.addTest(jast.test.RunAllTests.suite());
        suite.addTest(jart.test.RunAllTests.suite());

        return suite;
    }
}
