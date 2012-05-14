package jast.test.parser;
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
        TestSuite suite = new TestSuite("Parser tests");

        // base elements
        suite.addTest(new TestSuite(TestType.class));
        suite.addTest(new TestSuite(TestLiteral.class));
        suite.addTest(new TestSuite(TestArgumentList.class));
        suite.addTest(new TestSuite(TestVariableDeclaratorId.class));
        suite.addTest(new TestSuite(TestVariableInitializer.class));
        suite.addTest(new TestSuite(TestVariableDeclarator.class));
        suite.addTest(new TestSuite(TestVariableDeclaration.class));
        suite.addTest(new TestSuite(TestFormalParameter.class));
        suite.addTest(new TestSuite(TestModifiers.class));

        // primary expressions
        suite.addTest(new TestSuite(TestArrayAccess.class));
        suite.addTest(new TestSuite(TestArrayCreation.class));
        suite.addTest(new TestSuite(TestClassAccess.class));
        suite.addTest(new TestSuite(TestVariableAndTypeAccesses.class));
        suite.addTest(new TestSuite(TestInstantiation.class));
        suite.addTest(new TestSuite(TestMethodInvocation.class));
        suite.addTest(new TestSuite(TestParenthizedExpression.class));
        suite.addTest(new TestSuite(TestSelfAccess.class));
        suite.addTest(new TestSuite(TestConstructorInvocation.class));
        suite.addTest(new TestSuite(TestOtherPrimaries.class));

        // other expressions
        suite.addTest(new TestSuite(TestPostfixExpression.class));
        suite.addTest(new TestSuite(TestUnaryExpression.class));
        suite.addTest(new TestSuite(TestMultiplicativeExpression.class));
        suite.addTest(new TestSuite(TestAdditiveExpression.class));
        suite.addTest(new TestSuite(TestShiftExpression.class));
        suite.addTest(new TestSuite(TestRelationalExpression.class));
        suite.addTest(new TestSuite(TestInstanceofExpression.class));
        suite.addTest(new TestSuite(TestEqualityExpression.class));
        suite.addTest(new TestSuite(TestBitwiseAndExpression.class));
        suite.addTest(new TestSuite(TestBitwiseXorExpression.class));
        suite.addTest(new TestSuite(TestBitwiseOrExpression.class));
        suite.addTest(new TestSuite(TestAndExpression.class));
        suite.addTest(new TestSuite(TestOrExpression.class));
        suite.addTest(new TestSuite(TestConditionalExpression.class));
        suite.addTest(new TestSuite(TestAssignmentExpression.class));

        // statements
        suite.addTest(new TestSuite(TestLabeledStatement.class));
        suite.addTest(new TestSuite(TestExpressionStatement.class));
        suite.addTest(new TestSuite(TestBreakStatement.class));
        suite.addTest(new TestSuite(TestContinueStatement.class));
        suite.addTest(new TestSuite(TestThrowStatement.class));
        suite.addTest(new TestSuite(TestSynchronizedStatement.class));
        suite.addTest(new TestSuite(TestIfThenElseStatement.class));
        suite.addTest(new TestSuite(TestWhileStatement.class));
        suite.addTest(new TestSuite(TestDoWhileStatement.class));
        suite.addTest(new TestSuite(TestForStatement.class));
        suite.addTest(new TestSuite(TestCatchClause.class));
        suite.addTest(new TestSuite(TestTryStatement.class));
        suite.addTest(new TestSuite(TestCaseBlock.class));
        suite.addTest(new TestSuite(TestSwitchStatement.class));
        suite.addTest(new TestSuite(TestOtherStatements.class));
        suite.addTest(new TestSuite(TestBlock.class));

        // higher elements
        suite.addTest(new TestSuite(TestFieldDeclaration.class));
        suite.addTest(new TestSuite(TestConstructorDeclaration.class));
        suite.addTest(new TestSuite(TestMethodDeclaration.class));
        suite.addTest(new TestSuite(TestInitializer.class));
        suite.addTest(new TestSuite(TestClassDeclaration.class));
        suite.addTest(new TestSuite(TestInterfaceDeclaration.class));
        suite.addTest(new TestSuite(TestCompilationUnit.class));

        // comments
        suite.addTest(new TestSuite(TestComments.class));

        // Other tests
        suite.addTest(new TestSuite(TestGetType.class));
        suite.addTest(new TestSuite(TestFileParser.class));
        suite.addTest(new TestSuite(TestParsePositions.class));
        suite.addTest(new TestSuite(TestCompletingParse.class));

        return suite;
    }
}
