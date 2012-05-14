package jart.test.analysis.duplication;
import jart.analysis.duplication.LinearView;
import jast.Global;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.BreakStatement;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.ContinueStatement;
import jast.ast.nodes.DoWhileStatement;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.ExpressionStatement;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.ForStatement;
import jast.ast.nodes.IfThenElseStatement;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.LocalVariableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.ReturnStatement;
import jast.ast.nodes.Statement;
import jast.ast.nodes.SwitchStatement;
import jast.ast.nodes.SynchronizedStatement;
import jast.ast.nodes.ThrowStatement;
import jast.ast.nodes.TryStatement;
import jast.ast.nodes.WhileStatement;
import jast.parser.CommentFilter;
import jast.parser.JavaLexer;
import jast.parser.JavaParser;
import jast.parser.ParserHelper;
import jast.parser.UnicodeReader;

import java.io.StringReader;

import junitx.framework.PrivateTestCase;


public class TestLinearView extends PrivateTestCase
{
    private ParserHelper _helper;
    private JavaParser   _parser;
    private JavaLexer    _lexer;
    private LinearView   _testObject;

    public TestLinearView(String name)
    {
        super(name);
    }

    public void setUp()
    {
        _helper = new ParserHelper();
        _helper.setProject(Global.getFactory().createProject("Test"));
        _helper.setUnitName("SomeClass.java");
        _helper.pushTypeScope(null);
        _helper.pushBlockScope();

        _testObject = new LinearView();
    }

    protected void setupParser(String text)
    {
        _lexer = new JavaLexer(new UnicodeReader(new StringReader(text)));
        _parser = new JavaParser(new CommentFilter(_lexer, _helper));
        _parser.setHelper(_helper);
    }

    public void tearDown()
    {
        if (_helper != null)
            {
            _helper = null;
        }
        _parser = null;
        _lexer = null;

        _testObject = null;
    }

    public void testAddViewToNode1() throws Exception
    {
        setupParser("void doNothing() { ; }");

        MethodDeclaration meth = (MethodDeclaration) _parser.classBodyDeclaration();

        _testObject.visitInvocableDeclaration(meth);

        meth.setProperty(LinearView.PROPERTY_LABEL, _testObject.getView());

        assertEquals(
            _testObject.getView(),
            meth.getProperty(LinearView.PROPERTY_LABEL));
    }

    public void testBeakStatement() throws Exception
    {
        setupParser("break ;");
        _testObject.visitBreakStatement((BreakStatement) _parser.statement());

        assertEquals(LinearView.ABRV_BREAK_STATEMENT, _testObject.getView());
    }

    public void testCaseBlock1() throws Exception
    {
        setupParser("case 0:");
        _testObject.visitCaseBlock(_parser.caseBlock());

        assertEquals(LinearView.ABRV_CASE_BRANCH, _testObject.getView());
    }

    public void testCaseBlock2() throws Exception
    {
        setupParser("case 0: ; ");
        _testObject.visitCaseBlock(_parser.caseBlock());

        assertEquals(
            LinearView.ABRV_CASE_BRANCH_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_CASE_BRANCH_CLOSE,
            _testObject.getView());
    }

    public void testCatchClause1() throws Exception
    {
        setupParser("catch (Exception ex) {}");

        _testObject.visitCatchClause(_parser.catchClause());

        assertEquals(
            LinearView.ABRV_CATCH_CLAUSE_OPEN + LinearView.ABRV_CATCH_CLAUSE_CLOSE,
            _testObject.getView());

    }

    public void testCatchClause2() throws Exception
    {
        setupParser("catch (Exception ex) { ; }");

        _testObject.visitCatchClause(_parser.catchClause());

        assertEquals(
            LinearView.ABRV_CATCH_CLAUSE_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_CATCH_CLAUSE_CLOSE,
            _testObject.getView());

    }

    public void testContinueStatement() throws Exception
    {
        setupParser("continue ;");

        _testObject.visitContinueStatement((ContinueStatement) _parser.statement());

        assertEquals(LinearView.ABRV_CONTINUE_STATEMENT, _testObject.getView());
    }

    public void testDoWhileStatement() throws Exception
    {
        setupParser("do ; while (true);");

        _testObject.visitDoWhileStatement((DoWhileStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_DO_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_DO_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testEmptyStatement() throws Exception
    {
        setupParser(" ; ");
        _testObject.visitEmptyStatement((EmptyStatement) _parser.statement());

        assertEquals(LinearView.ABRV_EMPTY_STATEMENT, _testObject.getView());
    }

    public void testExpressionStatement() throws Exception
    {
        setupParser("doSomething();");

        _testObject.visitExpressionStatement((ExpressionStatement) _parser.statement());

        assertEquals(LinearView.ABRV_EXPRESSION_STATEMENT, _testObject.getView());
    }

    public void testForStatement1() throws Exception
    {
        setupParser("for (;;) ;");

        _testObject.visitForStatement((ForStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_FOR_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_FOR_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testForStatement2() throws Exception
    {
        setupParser("for (;;) { ; }");

        _testObject.visitForStatement((ForStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_FOR_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_FOR_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testGetNode1() throws Exception
    {
        setupParser("void method1() { int count = calcCount(); break; } ");

        MethodDeclaration node = _parser.methodDeclaration();

        assertTrue(_testObject.getNode(node, 0) instanceof LocalVariableDeclaration);
    }

    public void testGetNode2() throws Exception
    {
        setupParser("void method1() { int count = calcCount(); break; } ");

        MethodDeclaration node = _parser.methodDeclaration();

        assertTrue(_testObject.getNode(node, 1) instanceof BreakStatement);
    }

    public void testIfThenElseStatement1() throws Exception
    {
        setupParser("if (true) ; ");

        _testObject.visitIfThenElseStatement((IfThenElseStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_IF_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_IF_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testIfThenElseStatement2() throws Exception
    {
        setupParser("if (true) { ; }");

        _testObject.visitIfThenElseStatement((IfThenElseStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_IF_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_IF_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testIfThenElseStatement3() throws Exception
    {
        setupParser("if (true) ; else ;");

        _testObject.visitIfThenElseStatement((IfThenElseStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_IF_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_ELSE_PART
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_IF_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testIfThenElseStatement4() throws Exception
    {
        setupParser("if (true) { ; } else { ; }");

        _testObject.visitIfThenElseStatement((IfThenElseStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_IF_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_ELSE_PART
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_IF_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testInvocableDeclaration1() throws Exception
    {
        setupParser("void doSomething() {}");

        _testObject.visitInvocableDeclaration(
            (MethodDeclaration) _parser.classBodyDeclaration());

        assertEquals("", _testObject.getView());
    }

    public void testInvocableDeclaration2() throws Exception
    {
        setupParser("SomeClass() { ; }");

        _testObject.visitInvocableDeclaration(
            (ConstructorDeclaration) _parser.classBodyDeclaration());

        assertEquals(LinearView.ABRV_EMPTY_STATEMENT, _testObject.getView());
    }

    public void testInvocableDeclaration3() throws Exception
    {
        setupParser("SomeClass() {}");

        _testObject.visitInvocableDeclaration(
            (ConstructorDeclaration) _parser.classBodyDeclaration());

        assertEquals("", _testObject.getView());
    }

    public void testInvocableDeclaration4() throws Exception
    {
        setupParser("SomeClass() { ; }");

        _testObject.visitInvocableDeclaration(
            (ConstructorDeclaration) _parser.classBodyDeclaration());

        assertEquals(LinearView.ABRV_EMPTY_STATEMENT, _testObject.getView());
    }

    public void testIsContainedByInvocableDeclaration1() throws Exception
    {
        setupParser("void doSomething() { break; }");

        InvocableDeclaration meth =
            (InvocableDeclaration) _parser.classBodyDeclaration();
        Node node = (Node) meth.getBody().getBlockStatements().get(0);

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration2() throws Exception
    {
        setupParser("public SomeType() { break; }");

        InvocableDeclaration meth =
            (InvocableDeclaration) _parser.classBodyDeclaration();
        Node node = (Node) meth.getBody().getBlockStatements().get(0);

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration3() throws Exception
    {
        setupParser("void doSomething() {}");

        InvocableDeclaration meth =
            (InvocableDeclaration) _parser.classBodyDeclaration();
        Node node = null;

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(!result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration4() throws Exception
    {
        setupParser("{ break; }");

        Block blk = _parser.block();
        Node node = (Node) blk.getBlockStatements().get(0);

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(!result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration5() throws Exception
    {
        setupParser("void doSomething() { { break; } }");

        InvocableDeclaration meth =
            (InvocableDeclaration) _parser.classBodyDeclaration();
        Node node = (Node) meth.getBody().getBlockStatements().get(0);

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration6() throws Exception
    {
        setupParser("break;");

        Statement node = _parser.statement();

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(!result.booleanValue());
    }

    public void testIsContainedByInvocableDeclaration7() throws Exception
    {
        setupParser("class SomeClass { int count = calcCount(); } ");

        FieldDeclaration node = _parser.classDeclaration(false).getFields().get(0);

        Object[] args = { node };
        Boolean result =
            (Boolean) invokeWithKey(_testObject,
                "isContainedByInvocableDeclaration_jast.ast.Node",
                args);

        assertTrue(!result.booleanValue());
    }

    public void testReturnStatement() throws Exception
    {
        setupParser("return ;");

        _testObject.visitReturnStatement((ReturnStatement) _parser.statement());

        assertEquals(LinearView.ABRV_RETURN_STATEMENT, _testObject.getView());
    }

    public void testSwitchStatement1() throws Exception
    {
        setupParser("switch (var) {}");

        _testObject.visitSwitchStatement((SwitchStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_SWITCH_STATEMENT_OPEN + LinearView.ABRV_SWITCH_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testSwitchStatement2() throws Exception
    {
        setupParser("switch (var) { case 1: ; }");

        _testObject.visitSwitchStatement((SwitchStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_SWITCH_STATEMENT_OPEN
                + LinearView.ABRV_CASE_BRANCH_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_CASE_BRANCH_CLOSE
                + LinearView.ABRV_SWITCH_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testSynchronizedStatement1() throws Exception
    {
        setupParser("synchronized ((var)) {}");

        _testObject.visitSynchronizedStatement(
            (SynchronizedStatement) _parser.statement());

        assertEquals(LinearView.ABRV_SYNCHRONIZED_STATEMENT, _testObject.getView());
    }

    public void testSynchronizedStatement2() throws Exception
    {
        setupParser("synchronized ((var)) { ; }");

        _testObject.visitSynchronizedStatement(
            (SynchronizedStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_SYNCHRONIZED_STATEMENT + LinearView.ABRV_EMPTY_STATEMENT,
            _testObject.getView());
    }

    public void testThrowStatement() throws Exception
    {
        setupParser("throw (var);");

        _testObject.visitThrowStatement((ThrowStatement) _parser.statement());

        assertEquals(LinearView.ABRV_THROW_STATEMENT, _testObject.getView());
    }

    public void testTryStatement1() throws Exception
    {
        setupParser("try {} catch (Exception ex) {}");

        _testObject.visitTryStatement((TryStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_TRY_STATEMENT_OPEN
                + LinearView.ABRV_CATCH_CLAUSE_OPEN
                + LinearView.ABRV_CATCH_CLAUSE_CLOSE
                + LinearView.ABRV_TRY_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testTryStatement2() throws Exception
    {
        setupParser("try { ; } catch (Exception ex) { ; }");

        _testObject.visitTryStatement((TryStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_TRY_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_CATCH_CLAUSE_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_CATCH_CLAUSE_CLOSE
                + LinearView.ABRV_TRY_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testWhileStatement1() throws Exception
    {
        setupParser("while (true) ;");

        _testObject.visitWhileStatement((WhileStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_WHILE_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_WHILE_STATEMENT_CLOSE,
            _testObject.getView());
    }

    public void testWhileStatement2() throws Exception
    {
        setupParser("while (true) { ; }");

        _testObject.visitWhileStatement((WhileStatement) _parser.statement());

        assertEquals(
            LinearView.ABRV_WHILE_STATEMENT_OPEN
                + LinearView.ABRV_EMPTY_STATEMENT
                + LinearView.ABRV_WHILE_STATEMENT_CLOSE,
            _testObject.getView());
    }
}
