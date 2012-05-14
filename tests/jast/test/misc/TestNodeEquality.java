package jast.test.misc;
import jast.Global;
import jast.SyntaxException;
import jast.ast.Node;
import jast.ast.nodes.Block;
import jast.ast.nodes.Type;

import java.util.Hashtable;

import junit.framework.TestCase;

public class TestNodeEquality extends TestCase
{
    private Node      _node1;
    private Node      _node2;
    private Hashtable _map;

    public TestNodeEquality(String name)
    {
        super(name);
    }

    protected void tearDown()
    {
        _node1 = null;
        _node2 = null;
        _map   = null;
    }

    public void testEquality1()
    {
        _node1 = Global.getFactory().createBlock();

        assertTrue(_node1.equals(_node1));
    }

    public void testEquality2() throws SyntaxException
    {
        _node1 = Global.getFactory().createStringLiteral("bla");

        assertTrue(_node1.equals(_node1));
    }

    public void testEquality3()
    {
        _node1 = Global.getFactory().createBlock();
        _node2 = Global.getFactory().createBlock();

        assertTrue(!_node1.equals(_node2));
    }

    public void testEquality4() throws SyntaxException
    {
        _node1 = Global.getFactory().createClassDeclaration(
                    Global.getFactory().createModifiers(),
                    "test",
                    false);
        _node2 = Global.getFactory().createAnonymousClassDeclaration("Object");

        assertTrue(!_node1.equals(_node2));
    }

    public void testEquality5() throws SyntaxException
    {
        _node1 = Global.getFactory().createType("Object", 2);
        _node2 = ((Type)_node1).getClone();

        assertTrue(!_node1.equals(_node2));
    }

    public void testEquality6() throws SyntaxException
    {
        Type node1 = Global.getFactory().createType("Object", 2);
        Type node2 = ((Type)node1).getClone();

        assertTrue(!node1.equals(node2));
    }

    public void testEquality7()
    {
        Node node1 = Global.getFactory().createBlock();
        Node node2 = Global.getFactory().createBlock();

        assertTrue(!node1.equals(node2));
    }

    public void testEquality8()
    {
        Block node1 = Global.getFactory().createBlock();

        assertTrue(node1.equals(node1));
    }

    public void testEqualityInHashtable1()
    {
        Block node1 = Global.getFactory().createBlock();

        _map = new Hashtable();
        _map.put(node1, "test");

        assertEquals((String)_map.get(node1),
                     "test");
    }

    public void testEqualityInHashtable2()
    {
        Block node1 = Global.getFactory().createBlock();
        Block node2 = Global.getFactory().createBlock();

        _map = new Hashtable();
        _map.put(node1, "test");
        _map.put(node2, "test2");

        assertEquals((String)_map.get(node2),
                     "test2");
        assertEquals((String)_map.get(node1),
                     "test");
    }

    public void testEqualityInHashtable3()
    {
        Type node1 = Global.getFactory().createType("Object", 2);
        Type node2 = ((Type)node1).getClone();

        _map = new Hashtable();
        _map.put(node1, "test");

        assertNull(_map.get(node2));
    }

    public void testEqualityInHashtable4()
    {
        Block node1 = Global.getFactory().createBlock();

        _map = new Hashtable();
        _map.put(node1, "test");

        assertTrue(_map.containsKey(node1));
    }
}
