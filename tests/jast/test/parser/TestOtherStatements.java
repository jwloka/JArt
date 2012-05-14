package jast.test.parser;
import jast.ast.nodes.EmptyStatement;
import jast.ast.nodes.IntegerLiteral;
import jast.ast.nodes.ReturnStatement;
import antlr.ANTLRException;

public class TestOtherStatements extends TestBase
{

    public TestOtherStatements(String name)
    {
        super(name);
    }

    public void testEmptyStatement1() throws ANTLRException
    {
        setupParser(";");

        EmptyStatement result = (EmptyStatement)_parser.statement();

        assertNotNull(result);
    }

    public void testEmptyStatement2()
    {
        setupParser("");
        try
        {
            _parser.statement();
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testReturnStatement1() throws ANTLRException
    {
        setupParser("return ;");

        ReturnStatement result = (ReturnStatement)_parser.statement();

        assertNotNull(result);
        assertNull(result.getReturnValue());
    }

    public void testReturnStatement2() throws ANTLRException
    {
        setupParser("return 1;");

        ReturnStatement result = (ReturnStatement)_parser.statement();

        assertNotNull(result);

        IntegerLiteral intLit = (IntegerLiteral)result.getReturnValue();

        assertNotNull(intLit);
        assertEquals("1",
                     intLit.asString());
    }
}
