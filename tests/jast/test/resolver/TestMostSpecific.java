package jast.test.resolver;
import jast.Global;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.collections.InvocableArrayImpl;
import junitx.framework.TestAccessException;

public class TestMostSpecific extends TestResolverBase
{
    private ClassDeclaration  _baseClass;
    private ClassDeclaration  _subClass1;
    private ClassDeclaration  _subClass2;

    private MethodDeclaration _baseTest1;
    private MethodDeclaration _baseTest2Int;
    private MethodDeclaration _baseTest2Long;
    private MethodDeclaration _baseTest3Base;
    private MethodDeclaration _baseTest3Sub1;
    private MethodDeclaration _baseTest3Sub2;

    private MethodDeclaration _sub1Test1;
    private MethodDeclaration _sub1Test2Int;
    private MethodDeclaration _sub1Test3Base;

    private MethodDeclaration _sub2Test1;
    private MethodDeclaration _sub2Test2Int;
    private MethodDeclaration _sub2Test3Sub2;

    private InvocableArrayImpl _methods;

    private void startCompilationUnit()
    {
    }

    public TestMostSpecific(String name) throws SyntaxException
    {
        super(name);

        NodeFactory         factory = Global.getFactory();
        FormalParameterList params;

        _baseClass = factory.createClassDeclaration(factory.createModifiers(),
                                                    "Base",
                                                    false);
        _subClass1 = factory.createClassDeclaration(factory.createModifiers(),
                                                    "Sub1",
                                                    false);
        _subClass2 = factory.createClassDeclaration(factory.createModifiers(),
                                                    "Sub2",
                                                    false);
        _subClass1.setBaseClass(factory.createType(_baseClass, 0));
        _subClass2.setBaseClass(factory.createType(_baseClass, 0));

        _baseTest1     = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test1",
                                                         null);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(PrimitiveType.INT_TYPE, 0),
                                                "param"));
        _baseTest2Int  = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test2",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(PrimitiveType.LONG_TYPE, 0),
                                                "param"));
        _baseTest2Long = factory.createMethodDeclaration(factory.createModifiers(),
                                                         factory.createType(PrimitiveType.INT_TYPE, 0),
                                                         "test2",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(_baseClass, 0),
                                                "param"));
        _baseTest3Base = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test3",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(_subClass1, 0),
                                                "param"));
        _baseTest3Sub1 = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test3",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(_subClass2, 0),
                                                "param"));
        _baseTest3Sub2 = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test3",
                                                         params);
        _baseClass.getMethods().add(_baseTest1);
        _baseClass.getMethods().add(_baseTest2Int);
        _baseClass.getMethods().add(_baseTest2Long);
        _baseClass.getMethods().add(_baseTest3Base);
        _baseClass.getMethods().add(_baseTest3Sub1);
        _baseClass.getMethods().add(_baseTest3Sub2);

        _sub1Test1     = factory.createMethodDeclaration(factory.createModifiers(),
                                                         factory.createType(PrimitiveType.INT_TYPE, 0),
                                                         "test1",
                                                         null);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(PrimitiveType.INT_TYPE, 0),
                                                "param"));
        _sub1Test2Int  = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test2",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(_baseClass, 0),
                                                "param"));
        _sub1Test3Base = factory.createMethodDeclaration(factory.createModifiers(),
                                                         factory.createType(PrimitiveType.INT_TYPE, 0),
                                                         "test3",
                                                         params);
        _subClass1.getMethods().add(_sub1Test1);
        _subClass1.getMethods().add(_sub1Test2Int);
        _subClass1.getMethods().add(_sub1Test3Base);

        _sub2Test1     = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test1",
                                                         null);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(PrimitiveType.INT_TYPE, 0),
                                                "param"));
        _sub2Test2Int  = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test2",
                                                         params);
        params = factory.createFormalParameterList();
        params.getParameters().add(factory.createFormalParameter(
                                                false,
                                                factory.createType(_subClass2, 0),
                                                "param"));
        _sub2Test3Sub2 = factory.createMethodDeclaration(factory.createModifiers(),
                                                         null,
                                                         "test3",
                                                         params);
        _subClass2.getMethods().add(_sub2Test1);
        _subClass2.getMethods().add(_sub2Test2Int);
        _subClass2.getMethods().add(_sub2Test3Sub2);
    }

    protected void setUp() throws SyntaxException, TestAccessException
    {
        super.setUp();

        _methods = new InvocableArrayImpl();
    }

    protected void tearDown()
    {
        super.tearDown();

        _methods = null;
    }

    public void testGetMostSpecific1()
    {
        _methods.add(_sub1Test1);
        _methods.add(_baseTest1);
        assertEquals(_sub1Test1,
                     _resolver.getMostSpecific(_methods));
    }

    public void testGetMostSpecific2()
    {
        _methods.add(_baseTest2Int);
        _methods.add(_baseTest2Long);

        assertEquals(_baseTest2Int,
                     _resolver.getMostSpecific(_methods));
    }

    public void testGetMostSpecific3()
    {
        _methods.add(_baseTest3Base);
        _methods.add(_baseTest3Sub1);
        _methods.add(_baseTest3Sub2);

        // there are two most specific methods (sub1/sub2)
        // but sub1 has been found earlier
        // Note that in reality this would only make sense
        // if the types base/sub1/sub2 are related interfaces
        // and the actual argument implements them all
        assertEquals(_baseTest3Sub1,
                     _resolver.getMostSpecific(_methods));
    }

    public void testIsMoreSpecificThan1()
    {
        assertTrue(_resolver.isMoreSpecificThan(_sub1Test1, _baseTest1));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest1, _sub1Test1));
    }

    public void testIsMoreSpecificThan10()
    {
        assertTrue(!_resolver.isMoreSpecificThan(_sub2Test3Sub2, _baseTest3Sub1));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Sub1, _sub2Test3Sub2));
    }

    public void testIsMoreSpecificThan11()
    {
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Sub2, _baseTest3Sub1));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Sub1, _baseTest3Sub2));
    }

    public void testIsMoreSpecificThan12()
    {
        assertTrue(!_resolver.isMoreSpecificThan(_sub1Test3Base, _sub2Test3Sub2));
        assertTrue(!_resolver.isMoreSpecificThan(_sub2Test3Sub2, _sub1Test3Base));
    }

    public void testIsMoreSpecificThan2()
    {
        assertTrue(!_resolver.isMoreSpecificThan(_sub1Test1, _sub2Test1));
        assertTrue(!_resolver.isMoreSpecificThan(_sub2Test1, _sub1Test1));
    }

    public void testIsMoreSpecificThan3()
    {
        assertTrue(_resolver.isMoreSpecificThan(_sub1Test2Int, _baseTest2Int));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest2Int, _sub1Test2Int));
    }

    public void testIsMoreSpecificThan4()
    {
        assertTrue(!_resolver.isMoreSpecificThan(_sub1Test2Int, _sub2Test2Int));
        assertTrue(!_resolver.isMoreSpecificThan(_sub2Test2Int, _sub1Test2Int));
    }

    public void testIsMoreSpecificThan5()
    {
        assertTrue(_resolver.isMoreSpecificThan(_sub1Test3Base, _baseTest3Base));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Base, _sub1Test3Base));
    }

    public void testIsMoreSpecificThan6()
    {
        assertTrue(_resolver.isMoreSpecificThan(_sub1Test2Int, _baseTest2Long));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest2Long, _sub1Test2Int));
    }

    public void testIsMoreSpecificThan7()
    {
        assertTrue(_resolver.isMoreSpecificThan(_baseTest2Int, _baseTest2Long));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest2Long, _baseTest2Int));
    }

    public void testIsMoreSpecificThan8()
    {
        assertTrue(_resolver.isMoreSpecificThan(_baseTest3Sub1, _baseTest3Base));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Base, _baseTest3Sub1));
    }

    public void testIsMoreSpecificThan9()
    {
        assertTrue(_resolver.isMoreSpecificThan(_sub2Test3Sub2, _baseTest3Base));
        assertTrue(!_resolver.isMoreSpecificThan(_baseTest3Base, _sub2Test3Sub2));
    }
}
