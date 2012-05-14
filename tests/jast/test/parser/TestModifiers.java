package jast.test.parser;
import jast.Global;
import jast.ast.nodes.Modifiers;
import antlr.ANTLRException;

public class TestModifiers extends TestBase
{
    private Modifiers _mods;

    public TestModifiers(String name)
    {
        super(name);
    }

    protected void setUp()
    {
        _mods = Global.getFactory().createModifiers();
    }

    protected void tearDown()
    {
        _mods = null;
    }

    public void testAbstractMethodModifiers1() throws ANTLRException
    {
        setupParser("public");
        _parser.abstractMethodModifiers(_mods);

        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isAbstract());
    }

    public void testAbstractMethodModifiers10()
    {
        setupParser("synchronized");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers11()
    {
        setupParser("transient");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers12()
    {
        setupParser("volatile");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers2() throws ANTLRException
    {
        setupParser("abstract");
        _parser.abstractMethodModifiers(_mods);

        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isAbstract());
    }

    public void testAbstractMethodModifiers3() throws ANTLRException
    {
        setupParser("public abstract");
        _parser.abstractMethodModifiers(_mods);

        assertTrue(_mods.isPublic());
        assertTrue(_mods.isAbstract());
    }

    public void testAbstractMethodModifiers4()
    {
        setupParser("final");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers5()
    {
        setupParser("native");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers6()
    {
        setupParser("private");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers7()
    {
        setupParser("protected");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers8()
    {
        setupParser("static");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testAbstractMethodModifiers9()
    {
        setupParser("strictfp");
        try
        {
            _parser.abstractMethodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassModifiers1() throws ANTLRException
    {
        setupParser("public static abstract");
        _parser.classModifiers(_mods);

        assertTrue(_mods.isAbstract());
        assertTrue(!_mods.isFinal());
        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isStrictfp());
    }

    public void testClassModifiers2() throws ANTLRException
    {
        setupParser("private strictfp");
        _parser.classModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(_mods.isStrictfp());
    }

    public void testClassModifiers3() throws ANTLRException
    {
        setupParser("protected final");
        _parser.classModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isStrictfp());
    }

    public void testClassModifiers4()
    {
        setupParser("native");
        try
        {
            _parser.classModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassModifiers5()
    {
        setupParser("synchronized");
        try
        {
            _parser.classModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassModifiers6()
    {
        setupParser("transient");
        try
        {
            _parser.classModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testClassModifiers7()
    {
        setupParser("volatile");
        try
        {
            _parser.classModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers1() throws ANTLRException
    {
        setupParser("public");
        _parser.constantModifiers(_mods);

        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isFinal());
    }

    public void testConstantModifiers10()
    {
        setupParser("synchronized");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers11()
    {
        setupParser("transient");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers12()
    {
        setupParser("volatile");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers2() throws ANTLRException
    {
        setupParser("static");
        _parser.constantModifiers(_mods);

        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isFinal());
    }

    public void testConstantModifiers3() throws ANTLRException
    {
        setupParser("final");
        _parser.constantModifiers(_mods);

        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isStatic());
        assertTrue(_mods.isFinal());
    }

    public void testConstantModifiers4() throws ANTLRException
    {
        setupParser("public static final");
        _parser.constantModifiers(_mods);

        assertTrue(_mods.isPublic());
        assertTrue(_mods.isStatic());
        assertTrue(_mods.isFinal());
    }

    public void testConstantModifiers5()
    {
        setupParser("abstract");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers6()
    {
        setupParser("native");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers7()
    {
        setupParser("private");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers8()
    {
        setupParser("protected");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstantModifiers9()
    {
        setupParser("strictfp");
        try
        {
            _parser.constantModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers1() throws ANTLRException
    {
        setupParser("public");
        _parser.constructorModifiers(_mods);

        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
    }

    public void testConstructorModifiers10()
    {
        setupParser("transient");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers11()
    {
        setupParser("volatile");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers2() throws ANTLRException
    {
        setupParser("protected");
        _parser.constructorModifiers(_mods);

        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isProtected());
        assertTrue(!_mods.isPrivate());
    }

    public void testConstructorModifiers3() throws ANTLRException
    {
        setupParser("private");
        _parser.constructorModifiers(_mods);

        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(_mods.isPrivate());
    }

    public void testConstructorModifiers4()
    {
        setupParser("abstract");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers5()
    {
        setupParser("final");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers6()
    {
        setupParser("native");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers7()
    {
        setupParser("static");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers8()
    {
        setupParser("strictfp");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testConstructorModifiers9()
    {
        setupParser("synchronized");
        try
        {
            _parser.constructorModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldModifiers1() throws ANTLRException
    {
        setupParser("public static volatile");
        _parser.fieldModifiers(_mods);

        assertTrue(!_mods.isFinal());
        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(_mods.isVolatile());
        assertTrue(!_mods.isTransient());
    }

    public void testFieldModifiers2() throws ANTLRException
    {
        setupParser("private final transient");
        _parser.fieldModifiers(_mods);

        assertTrue(_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isVolatile());
        assertTrue(_mods.isTransient());
    }

    public void testFieldModifiers3() throws ANTLRException
    {
        setupParser("protected volatile");
        _parser.fieldModifiers(_mods);

        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isTransient());
        assertTrue(_mods.isVolatile());
    }

    public void testFieldModifiers4() throws ANTLRException
    {
        setupParser("strictfp");
        try
        {
            _parser.fieldModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldModifiers5() throws ANTLRException
    {
        setupParser("native");
        try
        {
            _parser.fieldModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldModifiers6() throws ANTLRException
    {
        setupParser("abstract");
        try
        {
            _parser.fieldModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldModifiers7()
    {
        setupParser("synchronized");
        try
        {
            _parser.fieldModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testFieldModifiers8() throws ANTLRException
    {
        setupParser("final static volatile");
        _parser.fieldModifiers(_mods);

        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isTransient());
        assertTrue(_mods.isVolatile());
    }

    public void testFieldModifiers9() throws ANTLRException
    {
        setupParser("volatile static final");
        _parser.fieldModifiers(_mods);

        assertTrue(_mods.isFinal());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isTransient());
        assertTrue(!_mods.isVolatile());
    }

    public void testInitial()
    {
        assertTrue(!_mods.isAbstract());
        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isNative());
        assertTrue(!_mods.isPrivate());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isStrictfp());
        assertTrue(!_mods.isSynchronized());
        assertTrue(!_mods.isTransient());
        assertTrue(!_mods.isVolatile());
    }

    public void testInterfaceModifiers1() throws ANTLRException
    {
        setupParser("public static");
        _parser.interfaceModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isStrictfp());
    }

    public void testInterfaceModifiers2() throws ANTLRException
    {
        setupParser("private strictfp");
        _parser.interfaceModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(_mods.isStrictfp());
    }

    public void testInterfaceModifiers3() throws ANTLRException
    {
        setupParser("protected abstract");
        _parser.interfaceModifiers(_mods);

        assertTrue(_mods.isAbstract());
        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isStrictfp());
    }

    public void testInterfaceModifiers4()
    {
        setupParser("final");
        try
        {
            _parser.interfaceModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceModifiers5()
    {
        setupParser("native");
        try
        {
            _parser.interfaceModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceModifiers6()
    {
        setupParser("synchronized");
        try
        {
            _parser.interfaceModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceModifiers7()
    {
        setupParser("transient");
        try
        {
            _parser.interfaceModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testInterfaceModifiers9()
    {
        setupParser("volatile");
        try
        {
            _parser.interfaceModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testMethodModifiers1() throws ANTLRException
    {
        setupParser("public static abstract");
        _parser.methodModifiers(_mods);

        assertTrue(_mods.isAbstract());
        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isNative());
        assertTrue(_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(_mods.isStatic());
        assertTrue(!_mods.isSynchronized());
        assertTrue(!_mods.isStrictfp());
    }

    public void testMethodModifiers2() throws ANTLRException
    {
        setupParser("private final native");
        _parser.methodModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(_mods.isFinal());
        assertTrue(_mods.isNative());
        assertTrue(!_mods.isPublic());
        assertTrue(!_mods.isProtected());
        assertTrue(_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(!_mods.isSynchronized());
        assertTrue(!_mods.isStrictfp());
    }

    public void testMethodModifiers3() throws ANTLRException
    {
        setupParser("protected synchronized strictfp");
        _parser.methodModifiers(_mods);

        assertTrue(!_mods.isAbstract());
        assertTrue(!_mods.isFinal());
        assertTrue(!_mods.isNative());
        assertTrue(!_mods.isPublic());
        assertTrue(_mods.isProtected());
        assertTrue(!_mods.isPrivate());
        assertTrue(!_mods.isStatic());
        assertTrue(_mods.isSynchronized());
        assertTrue(_mods.isStrictfp());
    }

    public void testMethodModifiers4()
    {
        setupParser("transient");
        try
        {
            _parser.methodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }

    public void testMethodModifiers5()
    {
        setupParser("volatile");
        try
        {
            _parser.methodModifiers(_mods);
        }
        catch (ANTLRException ex)
        {
            return;
        }
        fail();
    }
}
