package jast.test.misc;
import jast.Global;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.Type;
import junit.framework.TestCase;

public class TestIsSubTypeOf extends TestCase
{
    private Type _objectType;
    private Type _serializableType;
    private Type _clonableType;
    private Type _stringType;
    private Type _hashtableType;
    private Type _typeA;
    private Type _typeB;

    private ClassDeclaration     _someClass;
    private ClassDeclaration     _anotherClass;
    private InterfaceDeclaration _someInterface;
    private InterfaceDeclaration _anotherInterface;

    public TestIsSubTypeOf(String name)
    {
        super(name);
    }

    protected void setUp() throws SyntaxException
    {
        NodeFactory factory = Global.getFactory();

        _objectType       = factory.createType("java.lang.Object", 0);
        _serializableType = factory.createType("java.io.Serializable", 0);
        _clonableType     = factory.createType("java.lang.Clonable", 0);
        _stringType       = factory.createType("java.lang.String", 0);
        _hashtableType    = factory.createType("java.util.Hashtable", 0);

        _someClass    = factory.createClassDeclaration(factory.createModifiers(),
                                                       "SomeClass",
                                                       false);
        _anotherClass = factory.createClassDeclaration(factory.createModifiers(),
                                                       "AnotherClass",
                                                       false);

        _someInterface    = factory.createInterfaceDeclaration(factory.createModifiers(),
                                                               "SomeInterface");
        _anotherInterface = factory.createInterfaceDeclaration(factory.createModifiers(),
                                                               "AnotherInterface");
    }

    protected void tearDown()
    {
        _typeA = null;
        _typeB = null;

        _objectType       = null;
        _serializableType = null;
        _clonableType     = null;
        _stringType       = null;
        _hashtableType    = null;

        _someClass        = null;
        _anotherClass     = null;
        _someInterface    = null;
        _anotherInterface = null;
    }

    public void testIsSubTypeOf1()
    {
        _typeA = Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 0);
        _typeB = Global.getFactory().createType(_someClass, 0);

        assertTrue(!_typeA.isSubTypeOf(_typeB));
        assertTrue(!_typeB.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_typeB.isSubTypeOf(_typeB));
    }

    public void testIsSubTypeOf10()
    {
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf11() throws SyntaxException
    {
        _someClass.setBaseClass(_hashtableType);
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_hashtableType));
        assertTrue(!_hashtableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_hashtableType.isSubTypeOf(_hashtableType));
    }

    public void testIsSubTypeOf12() throws SyntaxException
    {
        _someClass.getBaseInterfaces().add(_serializableType);
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf13() throws SyntaxException
    {
        _someClass.getBaseInterfaces().add(_serializableType);
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(!_typeA.isSubTypeOf(_clonableType));
        assertTrue(!_clonableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_clonableType.isSubTypeOf(_clonableType));
    }

    public void testIsSubTypeOf14() throws SyntaxException
    {
        _someClass.setBaseClass(Global.getFactory().createType("AnotherClass", 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf15() throws SyntaxException
    {
        _anotherClass.getBaseInterfaces().add(_serializableType);
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf16() throws SyntaxException
    {
        _typeA = Global.getFactory().createType(_someInterface, 0);

        assertTrue(!_typeA.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf17() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(_serializableType);
        _typeA = Global.getFactory().createType(_someInterface, 0);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf18() throws SyntaxException
    {
        _anotherInterface.getBaseInterfaces().add(_serializableType);
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _typeA = Global.getFactory().createType(_someInterface, 0);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf19() throws SyntaxException
    {
        _anotherInterface.getBaseInterfaces().add(_serializableType);
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _someClass.getBaseInterfaces().add(Global.getFactory().createType(_someInterface, 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf2()
    {
        _typeA = Global.getFactory().createType(PrimitiveType.BYTE_TYPE, 0);

        assertTrue(!_typeA.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf3()
    {
        _typeA = Global.getFactory().createType(PrimitiveType.CHAR_TYPE, 1);

        assertTrue(_typeA.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf4()
    {
        _stringType.incDimensions();

        assertTrue(_stringType.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_stringType));
        assertTrue(!_stringType.isSubTypeOf(_stringType));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }

    public void testIsSubTypeOf5()
    {
        _typeA = Global.getFactory().createType(PrimitiveType.SHORT_TYPE, 1);

        assertTrue(_typeA.isSubTypeOf(_clonableType));
        assertTrue(!_clonableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_clonableType.isSubTypeOf(_clonableType));
    }

    public void testIsSubTypeOf6()
    {
        _objectType.incDimensions();

        assertTrue(_objectType.isSubTypeOf(_clonableType));
        assertTrue(!_clonableType.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
        assertTrue(!_clonableType.isSubTypeOf(_clonableType));
    }

    public void testIsSubTypeOf7()
    {
        _typeA = Global.getFactory().createType(PrimitiveType.DOUBLE_TYPE, 2);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf8()
    {
        _typeA = Global.getFactory().createType("java.io.Serializable", 2);

        assertTrue(_typeA.isSubTypeOf(_serializableType));
        assertTrue(!_serializableType.isSubTypeOf(_typeA));
        assertTrue(!_typeA.isSubTypeOf(_typeA));
        assertTrue(!_serializableType.isSubTypeOf(_serializableType));
    }

    public void testIsSubTypeOf9()
    {
        assertTrue(_stringType.isSubTypeOf(_objectType));
        assertTrue(!_objectType.isSubTypeOf(_stringType));
        assertTrue(!_stringType.isSubTypeOf(_stringType));
        assertTrue(!_objectType.isSubTypeOf(_objectType));
    }
}
