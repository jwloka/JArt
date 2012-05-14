package jart.test.smelling;
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
        TestSuite suite = new TestSuite("Smelling tests");

        suite.addTest(new TestSuite(TestHasAwfullyLongName.class));
        suite.addTest(new TestSuite(TestHasFeatureEnvy.class));
        suite.addTest(new TestSuite(TestHasLongParameterList.class));
        suite.addTest(new TestSuite(TestHasSharedCollection.class));
        suite.addTest(new TestSuite(TestHasTemporaryFields.class));
        suite.addTest(new TestSuite(TestIsDataClass.class));
        suite.addTest(new TestSuite(TestIsInappropriatelyIntimate.class));
        suite.addTest(new TestSuite(TestIsUnnecessaryOpen.class));

        return suite;
    }
}
