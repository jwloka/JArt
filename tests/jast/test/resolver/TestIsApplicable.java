package jast.test.resolver;
import jast.Global;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.nodes.ArgumentList;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.Expression;
import jast.ast.nodes.FormalParameter;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.InterfaceDeclaration;
import jast.ast.nodes.PrimitiveType;
import junitx.framework.TestAccessException;

public class TestIsApplicable extends TestResolverBase
{
    private ArgumentList        _args;
    private FormalParameterList _params;

    private Expression          _argBoolean;
    private Expression          _argLong;
    private Expression          _argFloat;
    private Expression          _argDouble;
    private Expression          _argBoolean2Array;
    private Expression          _argByte2Array;
    private Expression          _argLongArray;
    private Expression          _argLong2Array;
    private Expression          _argDoubleArray;
    private Expression          _argNull;
    private Expression          _argObject;
    private Expression          _argBaseInterface;
    private Expression          _argTestClass;
    private Expression          _argOtherClass;
    private Expression          _argBaseClass;
    private Expression          _argSubClass;
    private Expression          _argBaseInterfaceArray;
    private Expression          _argTestClassArray;
    private Expression          _argOtherClassArray;
    private Expression          _argOtherClass2Array;
    private Expression          _argOtherSubClassArray;

    private FormalParameter     _paramDouble;
    private FormalParameter     _paramLong2Array;
    private FormalParameter     _paramBaseInterface;
    private FormalParameter     _paramTestClass;
    private FormalParameter     _paramOtherClassArray;
    private FormalParameter     _paramObject;
    private FormalParameter     _paramClonable;
    private FormalParameter     _paramSerializable;

    public TestIsApplicable(String name) throws SyntaxException
    {
        super(name);

        NodeFactory          factory       = Global.getFactory();
        ClassDeclaration     baseClass     = factory.createClassDeclaration(factory.createModifiers(),
                                                                            "BaseClass",
                                                                            false);
        ClassDeclaration     testClass     = factory.createClassDeclaration(factory.createModifiers(),
                                                                            "TestClass",
                                                                            false);
        ClassDeclaration     subClass      = factory.createClassDeclaration(factory.createModifiers(),
                                                                            "SubClass",
                                                                            false);
        ClassDeclaration     otherClass    = factory.createClassDeclaration(factory.createModifiers(),
                                                                            "OtherClass",
                                                                            false);
        ClassDeclaration     otherSubClass = factory.createClassDeclaration(factory.createModifiers(),
                                                                            "OtherSubClass",
                                                                            false);
        InterfaceDeclaration baseInterface = factory.createInterfaceDeclaration(factory.createModifiers(),
                                                                                "BaseInterface");

        testClass.setBaseClass(factory.createType(baseClass, 0));
        subClass.setBaseClass(factory.createType(testClass, 0));
        subClass.getBaseInterfaces().add(factory.createType(baseInterface, 0));
        otherClass.setBaseClass(factory.createType(baseInterface, 0));
        otherSubClass.setBaseClass(factory.createType(otherClass, 0));

        _argBoolean            = factory.createBooleanLiteral(false);
        _argLong               = factory.createIntegerLiteral("1L");
        _argFloat              = factory.createFloatingPointLiteral("2.0f");
        _argDouble             = factory.createFloatingPointLiteral("-3.0");
        _argBoolean2Array      = factory.createArrayCreation(
                                     factory.createType(PrimitiveType.BOOLEAN_TYPE, 0),
                                     2);
        _argByte2Array         = factory.createArrayCreation(
                                     factory.createType(PrimitiveType.BYTE_TYPE, 0),
                                     2);
        _argLongArray          = factory.createArrayCreation(
                                     factory.createType(PrimitiveType.LONG_TYPE, 0),
                                     1);
        _argLong2Array         = factory.createArrayCreation(
                                     factory.createType(PrimitiveType.LONG_TYPE, 0),
                                     2);
        _argDoubleArray        = factory.createArrayCreation(
                                     factory.createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     1);
        _argNull               = factory.createNullLiteral();
        _argObject             = factory.createInstantiation(
                                     null,
                                     factory.createType("java.lang.Object", 0),
                                     null,
                                     null);
        _argBaseInterface      = factory.createInstantiation(
                                     null,
                                     factory.createType(baseInterface, 0),
                                     null,
                                     null);    // would usually need anonymous class
        _argTestClass          = factory.createInstantiation(
                                     null,
                                     factory.createType(testClass, 0),
                                     null,
                                     null);
        _argOtherClass         = factory.createInstantiation(
                                     null,
                                     factory.createType(otherClass, 0),
                                     null,
                                     null);
        _argBaseClass          = factory.createInstantiation(
                                     null,
                                    factory.createType(baseClass, 0),
                                     null,
                                     null);
        _argSubClass           = factory.createInstantiation(
                                     null,
                                     factory.createType(subClass, 0),
                                     null,
                                     null);
        _argBaseInterfaceArray = factory.createArrayCreation(
                                     factory.createType(baseInterface, 0),
                                     1);
        _argTestClassArray     = factory.createArrayCreation(
                                     factory.createType(testClass, 0),
                                     1);
        _argOtherClassArray    = factory.createArrayCreation(
                                     factory.createType(otherClass, 0),
                                     1);
        _argOtherClass2Array   = factory.createArrayCreation(
                                     factory.createType(otherClass, 0),
                                     2);
        _argOtherSubClassArray = factory.createArrayCreation(
                                     factory.createType(otherSubClass, 0),
                                     1);

        _paramDouble           = factory.createFormalParameter(
                                     false,
                                     factory.createType(PrimitiveType.DOUBLE_TYPE, 0),
                                     "doubleValue");
        _paramLong2Array       = factory.createFormalParameter(
                                     false,
                                     factory.createType(PrimitiveType.LONG_TYPE, 2),
                                     "long2Array");
        _paramBaseInterface    = factory.createFormalParameter(
                                     false,
                                     factory.createType(baseInterface, 0),
                                     "baseInterfaceValue");
        _paramTestClass        = factory.createFormalParameter(
                                     false,
                                     factory.createType(testClass, 0),
                                     "testClassValue");
        _paramOtherClassArray  = factory.createFormalParameter(
                                     false,
                                     factory.createType(otherClass, 1),
                                     "otherClassValue");
        _paramObject           = factory.createFormalParameter(
                                     false,
                                     factory.createType("java.lang.Object", 0),
                                     "objectValue");
        _paramClonable         = factory.createFormalParameter(
                                     false,
                                     factory.createType("java.lang.Clonable", 0),
                                     "clonableValue");
        _paramSerializable     = factory.createFormalParameter(
                                     false,
                                     factory.createType("java.io.Serializable", 0),
                                     "serializableValue");
    }

    protected void setUp() throws SyntaxException, TestAccessException
    {
        super.setUp();

        _args    = Global.getFactory().createArgumentList();
        _params  = Global.getFactory().createFormalParameterList();
    }

    protected void tearDown()
    {
        super.tearDown();

        _args    = null;
        _params  = null;
    }

    public void testIsApplicable1()
    {
        assertTrue(_resolver.isApplicable(null, null));
    }

    public void testIsApplicable10()
    {
        _args.getArguments().add(_argOtherClass);
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramObject);
        _params.getParameters().add(_paramDouble);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable11()
    {
        _args.getArguments().add(_argDouble);
        _args.getArguments().add(_argOtherClassArray);
        _params.getParameters().add(_paramDouble);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable12()
    {
        _args.getArguments().add(_argDouble);
        _args.getArguments().add(_argTestClass);
        _params.getParameters().add(_paramDouble);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable13()
    {
        _args.getArguments().add(_argDouble);
        _args.getArguments().add(_argTestClass);
        _params.getParameters().add(_paramLong2Array);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable14()
    {
        _args.getArguments().add(_argDouble);
        _args.getArguments().add(_argBaseInterfaceArray);
        _params.getParameters().add(_paramLong2Array);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable2()
    {
        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable3()
    {
        assertTrue(_resolver.isApplicable(null, _params));
    }

    public void testIsApplicable4()
    {
        assertTrue(_resolver.isApplicable(_args, null));
    }

    public void testIsApplicable5()
    {
        _params.getParameters().add(_paramObject);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable6()
    {
        _args.getArguments().add(_argObject);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable7()
    {
        _args.getArguments().add(_argTestClass);
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable8()
    {
        _params.getParameters().add(_paramObject);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicable9()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramObject);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable1()
    {
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramClonable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramClonable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable3()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramClonable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable4()
    {
        _args.getArguments().add(_argLongArray);
        _params.getParameters().add(_paramClonable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable5()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramClonable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableClonable6()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramClonable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject1()
    {
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramObject);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramObject);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject3()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramObject);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject4()
    {
        _args.getArguments().add(_argLongArray);
        _params.getParameters().add(_paramObject);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject5()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramObject);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableObject6()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramObject);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim1()
    {
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramDouble);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim2()
    {
        _args.getArguments().add(_argFloat);
        _params.getParameters().add(_paramDouble);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim3()
    {
        _args.getArguments().add(_argBoolean);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim4()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim5()
    {
        _args.getArguments().add(_argTestClass);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim6()
    {
        _args.getArguments().add(_argDoubleArray);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim7()
    {
        _args.getArguments().add(_argOtherClassArray);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrim8()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramDouble);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray1()
    {
        _args.getArguments().add(_argLong);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray3()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray4()
    {
        _args.getArguments().add(_argLong2Array);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray5()
    {
        _args.getArguments().add(_argByte2Array);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray6()
    {
        _args.getArguments().add(_argBoolean2Array);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray7()
    {
        _args.getArguments().add(_argLongArray);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray8()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicablePrimArray9()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramLong2Array);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef1()
    {
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef10()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramTestClass);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef3()
    {
        _args.getArguments().add(_argTestClass);
        _params.getParameters().add(_paramTestClass);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef4()
    {
        _args.getArguments().add(_argSubClass);
        _params.getParameters().add(_paramTestClass);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef5()
    {
        _args.getArguments().add(_argBaseClass);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef6()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef7()
    {
        _args.getArguments().add(_argBoolean2Array);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef8()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRef9()
    {
        _args.getArguments().add(_argBaseInterface);
        _params.getParameters().add(_paramTestClass);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray1()
    {
        _args.getArguments().add(_argBoolean);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray10()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray3()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray4()
    {
        _args.getArguments().add(_argLongArray);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray5()
    {
        _args.getArguments().add(_argOtherClassArray);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray6()
    {
        _args.getArguments().add(_argBaseInterfaceArray);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray7()
    {
        _args.getArguments().add(_argOtherSubClassArray);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray8()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableRefArray9()
    {
        _args.getArguments().add(_argOtherClass2Array);
        _params.getParameters().add(_paramOtherClassArray);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable1()
    {
        _args.getArguments().add(_argDouble);
        _params.getParameters().add(_paramSerializable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable2()
    {
        _args.getArguments().add(_argObject);
        _params.getParameters().add(_paramSerializable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable3()
    {
        _args.getArguments().add(_argOtherClass);
        _params.getParameters().add(_paramSerializable);

        assertTrue(!_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable4()
    {
        _args.getArguments().add(_argLongArray);
        _params.getParameters().add(_paramSerializable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable5()
    {
        _args.getArguments().add(_argTestClassArray);
        _params.getParameters().add(_paramSerializable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }

    public void testIsApplicableSerializable6()
    {
        _args.getArguments().add(_argNull);
        _params.getParameters().add(_paramSerializable);

        assertTrue(_resolver.isApplicable(_args, _params));
    }
}
