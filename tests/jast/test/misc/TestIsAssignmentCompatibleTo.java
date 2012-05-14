package jast.test.misc;
import jast.Global;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.PrimitiveType;
import jast.ast.nodes.Type;
import junit.framework.TestCase;

public class TestIsAssignmentCompatibleTo extends TestCase
{
    private Type _typeA;
    private Type _typeB;
    private Type _booleanType;
    private Type _byteType;
    private Type _shortType;
    private Type _charType;
    private Type _intType;
    private Type _longType;
    private Type _floatType;
    private Type _doubleType;

    private Type _nullType;
    private Type _objectType;
    private Type _stringType;
    private Type _hashtableType;
    private Type _serializableType;
    private Type _clonableType;

    private ClassDeclaration     _someClass;
    private ClassDeclaration     _anotherClass;
    private InterfaceDeclaration _someInterface;
    private InterfaceDeclaration _anotherInterface;

    public TestIsAssignmentCompatibleTo(String name)
    {
        super(name);
    }

    protected void setUp() throws SyntaxException
    {
        NodeFactory factory = Global.getFactory();

        _booleanType = factory.createType(PrimitiveType.BOOLEAN_TYPE, 0);
        _byteType    = factory.createType(PrimitiveType.BYTE_TYPE, 0);
        _shortType   = factory.createType(PrimitiveType.SHORT_TYPE, 0);
        _charType    = factory.createType(PrimitiveType.CHAR_TYPE, 0);
        _intType     = factory.createType(PrimitiveType.INT_TYPE, 0);
        _longType    = factory.createType(PrimitiveType.LONG_TYPE, 0);
        _floatType   = factory.createType(PrimitiveType.FLOAT_TYPE, 0);
        _doubleType  = factory.createType(PrimitiveType.DOUBLE_TYPE, 0);

        _nullType         = factory.createNullType();
        _objectType       = factory.createType("java.lang.Object", 0);
        _stringType       = factory.createType("java.lang.String", 0);
        _hashtableType    = factory.createType("java.util.Hashtable", 0);
        _serializableType = factory.createType("java.io.Serializable", 0);
        _clonableType     = factory.createType("java.lang.Clonable", 0);

        _someClass        = factory.createClassDeclaration(factory.createModifiers(),
                                                           "SomeClass",
                                                           false);
        _anotherClass     = factory.createClassDeclaration(factory.createModifiers(),
                                                           "AnotherClass",
                                                           false);
        _someInterface    = factory.createInterfaceDeclaration(factory.createModifiers(),
                                                               "SomeInterface");
        _anotherInterface = factory.createInterfaceDeclaration(factory.createModifiers(),
                                                            "SomeInterface");
    }

    protected void tearDown()
    {
        _typeA       = null;
        _typeB       = null;
        _booleanType = null;
        _byteType    = null;
        _shortType   = null;
        _charType    = null;
        _intType     = null;
        _longType    = null;
        _floatType   = null;
        _doubleType  = null;

        _someClass     = null;
        _someInterface = null;
    }

    public void testIsAssignmentCompatibleTo1()
    {
        assertTrue(_booleanType.isAssignmentCompatibleTo(_booleanType));
        assertTrue(_byteType.isAssignmentCompatibleTo(_byteType));
        assertTrue(_shortType.isAssignmentCompatibleTo(_shortType));
        assertTrue(_charType.isAssignmentCompatibleTo(_charType));
        assertTrue(_intType.isAssignmentCompatibleTo(_intType));
        assertTrue(_longType.isAssignmentCompatibleTo(_longType));
        assertTrue(_floatType.isAssignmentCompatibleTo(_floatType));
        assertTrue(_doubleType.isAssignmentCompatibleTo(_doubleType));
    }

    public void testIsAssignmentCompatibleTo10()
    {
        _intType.setDimensions(3);

        assertTrue(_intType.isAssignmentCompatibleTo(_intType));
    }

    public void testIsAssignmentCompatibleTo11()
    {
        _byteType.setDimensions(3);
        _intType.setDimensions(3);

        assertTrue(!_byteType.isAssignmentCompatibleTo(_intType));
        assertTrue(!_intType.isAssignmentCompatibleTo(_byteType));
    }

    public void testIsAssignmentCompatibleTo12()
    {
        assertTrue(_shortType.isAssignmentCompatibleTo(_byteType));
        assertTrue(_charType.isAssignmentCompatibleTo(_byteType));
        assertTrue(_intType.isAssignmentCompatibleTo(_byteType));

        assertTrue(_byteType.isAssignmentCompatibleTo(_shortType));
        assertTrue(_shortType.isAssignmentCompatibleTo(_shortType));
        assertTrue(_charType.isAssignmentCompatibleTo(_shortType));
        assertTrue(_intType.isAssignmentCompatibleTo(_shortType));

        assertTrue(_byteType.isAssignmentCompatibleTo(_charType));
        assertTrue(_shortType.isAssignmentCompatibleTo(_charType));
        assertTrue(_intType.isAssignmentCompatibleTo(_charType));

        assertTrue(_byteType.isAssignmentCompatibleTo(_intType));
        assertTrue(_shortType.isAssignmentCompatibleTo(_intType));
        assertTrue(_charType.isAssignmentCompatibleTo(_intType));
    }

    public void testIsAssignmentCompatibleTo2()
    {
        assertTrue(_byteType.isAssignmentCompatibleTo(_floatType));
        assertTrue(!_floatType.isAssignmentCompatibleTo(_byteType));
    }

    public void testIsAssignmentCompatibleTo3()
    {
        assertTrue(_stringType.isAssignmentCompatibleTo(_stringType));
        assertTrue(_stringType.isAssignmentCompatibleTo(_objectType));
        assertTrue(!_objectType.isAssignmentCompatibleTo(_stringType));
        assertTrue(_objectType.isAssignmentCompatibleTo(_objectType));
    }

    public void testIsAssignmentCompatibleTo4()
    {
        assertTrue(!_booleanType.isAssignmentCompatibleTo(_objectType));
        assertTrue(!_objectType.isAssignmentCompatibleTo(_booleanType));
    }

    public void testIsAssignmentCompatibleTo5()
    {
        assertTrue(!_nullType.isAssignmentCompatibleTo(_intType));
        assertTrue(!_intType.isAssignmentCompatibleTo(_nullType));
    }

    public void testIsAssignmentCompatibleTo6()
    {
        assertTrue(_nullType.isAssignmentCompatibleTo(_stringType));
        assertTrue(!_stringType.isAssignmentCompatibleTo(_nullType));
    }

    public void testIsAssignmentCompatibleTo7() throws SyntaxException
    {
        _someClass.setBaseClass(Global.getFactory().createType("AnotherClass", 0));
        _typeA = Global.getFactory().createType(_someClass, 0);
        _typeB = Global.getFactory().createType("AnotherClass", 0);

        assertTrue(_typeA.isAssignmentCompatibleTo(_typeB));
        assertTrue(!_typeB.isAssignmentCompatibleTo(_typeA));
    }

    public void testIsAssignmentCompatibleTo8() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType("AnotherInterface", 0));
        _typeA = Global.getFactory().createType(_someInterface, 0);
        _typeB = Global.getFactory().createType("AnotherInterface", 0);

        assertTrue(_typeA.isAssignmentCompatibleTo(_typeB));
        assertTrue(!_typeB.isAssignmentCompatibleTo(_typeA));
    }

    public void testIsAssignmentCompatibleTo9()
    {
        _objectType.setDimensions(3);
        _stringType.setDimensions(3);

        assertTrue(_stringType.isAssignmentCompatibleTo(_objectType));
        assertTrue(!_objectType.isAssignmentCompatibleTo(_stringType));
    }

    public void testIsWideningCompatibleTo1()
    {
        assertTrue(!_booleanType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_byteType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_shortType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_charType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_intType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_longType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_floatType));
        assertTrue(!_booleanType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo10()
    {
        assertTrue(!_objectType.isWideningConvertibleTo(_objectType));
    }

    public void testIsWideningCompatibleTo11()
    {
        assertTrue(!_booleanType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_booleanType));
    }

    public void testIsWideningCompatibleTo12()
    {
        _stringType.setDimensions(1);

        assertTrue(_stringType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_stringType));
    }

    public void testIsWideningCompatibleTo13()
    {
        _floatType.setDimensions(1);

        assertTrue(_floatType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_floatType));
    }

    public void testIsWideningCompatibleTo14()
    {
        _typeA = Global.getFactory().createType("somepackage.SomeClass", 1);

        assertTrue(_typeA.isWideningConvertibleTo(_clonableType));
        assertTrue(!_clonableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo15()
    {
        _byteType.setDimensions(1);

        assertTrue(_byteType.isWideningConvertibleTo(_clonableType));
        assertTrue(!_clonableType.isWideningConvertibleTo(_byteType));
    }

    public void testIsWideningCompatibleTo16()
    {
        _typeA = Global.getFactory().createType("SomeInterface", 1);

        assertTrue(_typeA.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo17()
    {
        _intType.setDimensions(1);

        assertTrue(_intType.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_intType));
    }

    public void testIsWideningCompatibleTo18()
    {
        assertTrue(!_nullType.isWideningConvertibleTo(_intType));
        assertTrue(!_intType.isWideningConvertibleTo(_nullType));
    }

    public void testIsWideningCompatibleTo19()
    {
        assertTrue(_nullType.isWideningConvertibleTo(_stringType));
        assertTrue(!_stringType.isWideningConvertibleTo(_nullType));
    }

    public void testIsWideningCompatibleTo2()
    {
        assertTrue(!_byteType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_byteType.isWideningConvertibleTo(_byteType));
        assertTrue(_byteType.isWideningConvertibleTo(_shortType));
        assertTrue(!_byteType.isWideningConvertibleTo(_charType));
        assertTrue(_byteType.isWideningConvertibleTo(_intType));
        assertTrue(_byteType.isWideningConvertibleTo(_longType));
        assertTrue(_byteType.isWideningConvertibleTo(_floatType));
        assertTrue(_byteType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo20()
    {
        _charType.setDimensions(1);

        assertTrue(_nullType.isWideningConvertibleTo(_charType));
        assertTrue(!_charType.isWideningConvertibleTo(_nullType));
    }

    public void testIsWideningCompatibleTo21()
    {
        _typeB = Global.getFactory().createType("SomeInterface", 1);

        assertTrue(_nullType.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_nullType));
    }

    public void testIsWideningCompatibleTo22() throws SyntaxException
    {
        _someClass.setBaseClass(Global.getFactory().createType("AnotherClass", 0));
        _typeA = Global.getFactory().createType(_someClass, 0);
        _typeB = Global.getFactory().createType("AnotherClass", 0);

        assertTrue(_typeA.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo23() throws SyntaxException
    {
        _anotherClass.setBaseClass(_hashtableType);
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isWideningConvertibleTo(_hashtableType));
        assertTrue(!_hashtableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo24() throws SyntaxException
    {
        _someClass.getBaseInterfaces().add(Global.getFactory().createType("SomeInterface", 0));
        _typeA = Global.getFactory().createType(_someClass, 0);
        _typeB = Global.getFactory().createType("SomeInterface", 0);

        assertTrue(_typeA.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo25() throws SyntaxException
    {
        _anotherClass.getBaseInterfaces().add(_serializableType);
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo26() throws SyntaxException
    {
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 0);

        assertTrue(_typeA.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo27() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType("AnotherInterface", 0));
        _typeA = Global.getFactory().createType(_someInterface, 0);
        _typeB = Global.getFactory().createType("AnotherInterface", 0);

        assertTrue(_typeA.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo28() throws SyntaxException
    {
        _anotherInterface.getBaseInterfaces().add(_serializableType);
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _typeA = Global.getFactory().createType(_someInterface, 0);

        assertTrue(_typeA.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo29() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _typeA = Global.getFactory().createType(_someInterface, 0);

        assertTrue(_typeA.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo3()
    {
        assertTrue(!_shortType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_shortType.isWideningConvertibleTo(_byteType));
        assertTrue(!_shortType.isWideningConvertibleTo(_shortType));
        assertTrue(!_shortType.isWideningConvertibleTo(_charType));
        assertTrue(_shortType.isWideningConvertibleTo(_intType));
        assertTrue(_shortType.isWideningConvertibleTo(_longType));
        assertTrue(_shortType.isWideningConvertibleTo(_floatType));
        assertTrue(_shortType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo30()
    {
        _objectType.setDimensions(3);
        _stringType.setDimensions(3);

        assertTrue(_stringType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_stringType));
    }

    public void testIsWideningCompatibleTo31()
    {
        _objectType.setDimensions(3);
        _stringType.setDimensions(1);

        assertTrue(!_stringType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_stringType));
    }

    public void testIsWideningCompatibleTo32()
    {
        _intType.setDimensions(3);
        _objectType.setDimensions(3);

        assertTrue(!_intType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_intType));
    }

    public void testIsWideningCompatibleTo33() throws SyntaxException
    {
        _someClass.getBaseInterfaces().add(Global.getFactory().createType("SomeInterface", 0));
        _typeA = Global.getFactory().createType(_someClass, 2);
        _typeB = Global.getFactory().createType("SomeInterface", 2);

        assertTrue(_typeA.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo34() throws SyntaxException
    {
        _anotherClass.getBaseInterfaces().add(Global.getFactory().createType("java.io.Serializable", 0));
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 1);
        _serializableType.setDimensions(1);

        assertTrue(_typeA.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo35() throws SyntaxException
    {
        _someClass.setBaseClass(Global.getFactory().createType(_anotherClass, 0));
        _typeA = Global.getFactory().createType(_someClass, 3);
        _objectType.setDimensions(3);

        assertTrue(_typeA.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo36() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType("AnotherInterface", 0));
        _typeA = Global.getFactory().createType(_someInterface, 1);
        _typeB = Global.getFactory().createType("AnotherInterface", 1);

        assertTrue(_typeA.isWideningConvertibleTo(_typeB));
        assertTrue(!_typeB.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo37() throws SyntaxException
    {
        _anotherInterface.getBaseInterfaces().add(Global.getFactory().createType("java.io.Serializable", 0));
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _typeA = Global.getFactory().createType(_someInterface, 4);
        _serializableType.setDimensions(4);

        assertTrue(_typeA.isWideningConvertibleTo(_serializableType));
        assertTrue(!_serializableType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo38() throws SyntaxException
    {
        _someInterface.getBaseInterfaces().add(Global.getFactory().createType(_anotherInterface, 0));
        _typeA = Global.getFactory().createType(_someInterface, 1);
        _objectType.setDimensions(1);

        assertTrue(_typeA.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_typeA));
    }

    public void testIsWideningCompatibleTo39()
    {
        _byteType.setDimensions(1);
        _intType.setDimensions(1);

        assertTrue(!_byteType.isWideningConvertibleTo(_intType));
        assertTrue(!_intType.isWideningConvertibleTo(_byteType));
    }

    public void testIsWideningCompatibleTo4()
    {
        assertTrue(!_charType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_charType.isWideningConvertibleTo(_byteType));
        assertTrue(!_charType.isWideningConvertibleTo(_shortType));
        assertTrue(!_charType.isWideningConvertibleTo(_charType));
        assertTrue(_charType.isWideningConvertibleTo(_intType));
        assertTrue(_charType.isWideningConvertibleTo(_longType));
        assertTrue(_charType.isWideningConvertibleTo(_floatType));
        assertTrue(_charType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo5()
    {
        assertTrue(!_intType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_intType.isWideningConvertibleTo(_byteType));
        assertTrue(!_intType.isWideningConvertibleTo(_shortType));
        assertTrue(!_intType.isWideningConvertibleTo(_charType));
        assertTrue(!_intType.isWideningConvertibleTo(_intType));
        assertTrue(_intType.isWideningConvertibleTo(_longType));
        assertTrue(_intType.isWideningConvertibleTo(_floatType));
        assertTrue(_intType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo6()
    {
        assertTrue(!_longType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_longType.isWideningConvertibleTo(_byteType));
        assertTrue(!_longType.isWideningConvertibleTo(_shortType));
        assertTrue(!_longType.isWideningConvertibleTo(_charType));
        assertTrue(!_longType.isWideningConvertibleTo(_intType));
        assertTrue(!_longType.isWideningConvertibleTo(_longType));
        assertTrue(_longType.isWideningConvertibleTo(_floatType));
        assertTrue(_longType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo7()
    {
        assertTrue(!_floatType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_floatType.isWideningConvertibleTo(_byteType));
        assertTrue(!_floatType.isWideningConvertibleTo(_shortType));
        assertTrue(!_floatType.isWideningConvertibleTo(_charType));
        assertTrue(!_floatType.isWideningConvertibleTo(_intType));
        assertTrue(!_floatType.isWideningConvertibleTo(_longType));
        assertTrue(!_floatType.isWideningConvertibleTo(_floatType));
        assertTrue(_floatType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo8()
    {
        assertTrue(!_doubleType.isWideningConvertibleTo(_booleanType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_byteType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_shortType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_charType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_intType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_longType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_floatType));
        assertTrue(!_doubleType.isWideningConvertibleTo(_doubleType));
    }

    public void testIsWideningCompatibleTo9()
    {
        assertTrue(_stringType.isWideningConvertibleTo(_objectType));
        assertTrue(!_objectType.isWideningConvertibleTo(_stringType));
    }
}
