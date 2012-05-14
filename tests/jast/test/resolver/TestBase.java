package jast.test.resolver;
import jast.Global;
import jast.ParseException;
import jast.ast.Project;
import jast.resolver.Resolver;

public abstract class TestBase extends jast.test.TestBase
{
    protected Project      _project  = null;
    protected StringParser _parser   = null;
    protected Resolver     _resolver = null;

    public TestBase(String name)
    {
        super(name);
    }

    protected void addType(String qualifiedName, String compUnit, boolean doParse) throws ParseException
    {
        _parser.addType(qualifiedName, compUnit);
        if (doParse)
        {
            _parser.parseType(_project, qualifiedName);
        }
    }

    // Setting up (reduced) base types
    private void initBaseTypes(StringParser parser)
    {
        // Object
        parser.addType(
            "java.lang.Object",
            "package java.lang;\n"+
            "public class Object {\n"+
            "    public final native Class getClass();\n"+
            "    public native int hashCode();\n"+
            "    public boolean equals(Object obj);\n"+
            "    protected native Object clone() throws CloneNotSupportedException;\n"+
            "    public String toString();\n"+
            "    public final native void notify();\n"+
            "    public final native void notifyAll();\n"+
            "    public final native void wait(long timeout) throws InterruptedException;\n"+
            "    public final void wait(long timeout, int nanos) throws InterruptedException;\n"+
            "    public final void wait() throws InterruptedException;\n"+
            "    protected void finalize() throws Throwable;\n"+
            "}");
        // Serializable
        parser.addType(
            "java.io.Serializable",
            "package java.io;\n"+
            "public interface Serializable {}");
        // Throwable
        parser.addType(
            "java.lang.Throwable",
            "package java.lang;\n"+
            "public class Throwable implements java.io.Serializable {\n"+
            "    public Throwable() {}\n"+
            "    public Throwable(String message) {}\n"+
            "    public String getMessage();\n"+
            "    public String getLocalizedMessage();\n"+
            "    public String toString();\n"+
            "    public void printStackTrace();\n"+
            "    public native Throwable fillInStackTrace();\n"+
            "}");
        // Exception
        parser.addType(
            "java.lang.Exception",
            "package java.lang;\n"+
            "public class Exception extends Throwable {\n"+
            "    public Exception() {}\n"+
            "    public Exception(String s) {}\n"+
            "}");
        // CloneNotSupportedException
        parser.addType(
            "java.lang.CloneNotSupportedException ",
            "package java.lang;\n"+
            "public class CloneNotSupportedException extends Exception {\n"+
            "    public CloneNotSupportedException() {}\n"+
            "    public CloneNotSupportedException(String s) {}\n"+
            "}");
        // InterruptedException
        parser.addType(
            "java.lang.InterruptedException",
            "package java.lang;\n"+
            "public class InterruptedException extends Exception {\n"+
            "    public InterruptedException() {}\n"+
            "    public InterruptedException(String s) {}\n"+
            "}");
        // Clonable
        parser.addType(
            "java.lang.Clonable",
            "package java.lang;\n"+
            "public interface Clonable {}");
        // String
        parser.addType(
            "java.lang.String",
            "package java.lang;\n"+
            "public final class String implements java.io.Serializable {\n"+
            "    public String() {}\n"+
            "    public String(String value) {}\n"+
            "    public int length();\n"+
            "}");
        // Class
        parser.addType(
            "java.lang.Class",
            "package java.lang;\n"+
            "public final class Class implements java.io.Serializable {\n"+
            "    private Class() {}\n"+
            "    public String toString();\n"+
            "    public static Class forName(String className);\n"+
            "    public Object newInstance();\n"+
            "    public native String getName();\n"+
            "}");

    }

    protected void resolve()
    {
        _resolver.resolve(_project);
    }

    protected void setUp() throws ParseException
    {
        _project  = Global.getFactory().createProject("Test");
        _parser   = new StringParser();
        _resolver = new Resolver(_parser);

        Global.setParser(_parser);
        Global.setResolver(_resolver);
        initBaseTypes(_parser);
    }

    protected void tearDown() throws ParseException
    {
        _resolver = null;
        _parser   = null;
        _project  = null;
    }
}
