package jast.test.resolver;
import jast.FileParser;
import jast.ParseException;
import jast.ast.Project;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

// Helper class which allows to define compilation units for
// specific types via strings
public class StringParser extends FileParser
{
    private Hashtable _types = new Hashtable();

    public void addType(String qualifiedName, String text)
    {
        _types.put(qualifiedName, text);
    }

    public boolean existsType(String qualifiedName)
    {
        return _types.containsKey(qualifiedName) ||
               super.existsType(qualifiedName);
    }

    // The resulting CompilationUnit for 'pckg.Type' will have the filename
    // '/pckg/Type.java' or '\pckg\Type.java' depending on the current
    // separator char (java.io.File.separator)
    public boolean parseType(Project project, String qualifiedName) throws ParseException
    {
        if (_types.containsKey(qualifiedName))
        {
            try
            {
                parseStream(project,
                            java.io.File.separator + qualifiedName.replace('.', java.io.File.separatorChar) + ".java",
                            new StringReader((String)_types.get(qualifiedName)));
                return true;
            }
            catch (IOException ex)
            {
                return false;
            }
        }
        else
        {
            return super.parseType(project, qualifiedName);
        }
    }
}
