package jast.test.lexer;
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
        TestSuite suite = new TestSuite("Lexer tests");

        // lexer
        suite.addTest(new TestSuite(TestCharacters.class));
        suite.addTest(new TestSuite(TestComments.class));
        suite.addTest(new TestSuite(TestIdentifier.class));
        suite.addTest(new TestSuite(TestOperator.class));
        suite.addTest(new TestSuite(TestSeparator.class));
        suite.addTest(new TestSuite(TestIntegerLiteral.class));
        suite.addTest(new TestSuite(TestFloatingPointLiteral.class));

        return suite;
    }
}
