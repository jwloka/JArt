package jart.test.restructuring;
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
        //junit.swingui.TestRunner.run(suite());
    }

        public static Test suite()
        {
            TestSuite suite = new TestSuite("Restructuring tests");
            suite.addTest(new TestSuite(TestCreateLocalVariable.class));
            suite.addTest(new TestSuite(TestRenameLocalVariable.class));
            suite.addTest(new TestSuite(TestRemoveAssignmentsToParameters.class));
            suite.addTest(new TestSuite(TestCreateField.class));
            suite.addTest(new TestSuite(TestCreateAccessorMethod.class));
            suite.addTest(new TestSuite(TestEncapsulateField.class));
            suite.addTest(new TestSuite(TestCreateMethod.class));
            suite.addTest(new TestSuite(TestCreateConstructor.class));
            suite.addTest(new TestSuite(TestCreateTopLevelType.class));
            suite.addTest(new TestSuite(TestCreatePackage.class));
            suite.addTest(new TestSuite(TestTidyImports.class));
            suite.addTest(new TestSuite(TestIntroduceParameterObject.class));

            return suite;
        }
}
