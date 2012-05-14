/*
 * Copyright (C) 2002 Thomas Dudziak, Jan Wloka
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */
package jast;

import jast.ast.ParsePosition;
import jast.ast.Project;
import jast.ast.nodes.Block;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.Feature;
import jast.ast.nodes.FeatureWithBody;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.VariableInitializer;
import jast.ast.nodes.collections.ConstructorIterator;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.FormalParameterIterator;
import jast.ast.nodes.collections.InitializerIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.helpers.SortedArray;
import jast.helpers.StringArray;
import jast.helpers.StringIterator;
import jast.parser.CommentFilter;
import jast.parser.JavaLexer;
import jast.parser.JavaParser;
import jast.parser.ParserHelper;
import jast.parser.Seeker;
import jast.parser.UnicodeReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import antlr.ANTLRException;
import antlr.RecognitionException;

// Parses a java source file
// Note that for the name of the compilation units the complete
// path to that file is used, not the local path (this is
// necessary for completing the parse later on)
public class FileParser extends ProgressSourceBase implements Parser
{
    private ParserHelper _helper            = new ParserHelper();
    private StringArray  _dirs              = new StringArray();
    // whether parsed units are marked as project units
    private boolean      _projectUnitStatus = true;

    public void setProjectUnitStatus(boolean willBeProjectUnit)
    {
        _projectUnitStatus = willBeProjectUnit;
    }

    public boolean getProjectUnitStatus()
    {
        return _projectUnitStatus;
    }

    public void addSearchDirectory(String path) throws IOException
    {
        File file = new File(path);

        if (!file.isDirectory())
        {
            throw new IOException(path+" is not a directory");
        }
        if (!file.canRead())
        {
            throw new IOException("Directory "+path+" is not readable");
        }
        if (path.endsWith(File.separator))
        {
            _dirs.add(path.substring(0, path.length()-1));
        }
        else
        {
            _dirs.add(path);
        }
    }

    private boolean checkFile(File file, boolean isDirectory)
    {
        return (file != null) && file.exists() && file.canRead() &&
               (( isDirectory && file.isDirectory()) ||
                (!isDirectory && file.isFile()));
    }

    // Completes the parse of the given unit (ie fills the methods, fields
    // and initializers)
    public void complete(CompilationUnit unit) throws ParseException
    {
        SortedArray     features = new SortedArray();
        TypeDeclaration decl;

        _helper.setProject((Project)unit.getContainer());
        for (TypeDeclarationIterator typeDeclIt = unit.getTypeDeclarations().getIterator(); typeDeclIt.hasNext();)
        {
            decl = typeDeclIt.getNext();
            if (decl instanceof ClassDeclaration)
            {
                try
                {
                    parseFeatures(unit.getName(), (ClassDeclaration)decl);
                }
                catch (IOException ex)
                {
                    Global.getOutput().addError("Cannot complete the compilation unit "+unit.getName()+":\n"+ex, null);
                    throw new ParseException(ex.getMessage());
                }
            }
        }
        unit.setComplete(true);
        // needs resolving !
        unit.setResolvingStatus(false);
    }

    private void defineParameters(FormalParameterList params)
    {
        if (params != null)
        {
            _helper.pushBlockScope();
            if (params != null)
            {
                for (FormalParameterIterator it = params.getParameters().getIterator(); it.hasNext();)
                {
                    _helper.defineVariable(it.getNext());
                }
            }
        }
    }

    // Indicates that the type with the qualified name can be parsed
    // Is probably a conservative approximation, e.g. a file parser might
    // determine that there is a corresponding source file, but the file
    // does not have to contain a type
    public boolean existsType(String qualifiedName)
    {
        String  filename = find(qualifiedName.replace('.', File.separatorChar) + ".java", false);
        boolean result   = false;

        if (filename != null)
        {
            File srcFile = new File(filename);

            return srcFile.isFile() && srcFile.canRead();
        }
        return false;
    }

    // Searches for a file/directory with the given name in the local
    // and all specified search directories. The name can
    // specify a local path. If it was found, its path is
    // returned, otherwise null.
    private String find(String name, boolean isDirectory)
    {
        String path;
        File   file;

        // Already valid path ?
        file = new File(name);
        if (checkFile(file, isDirectory))
        {
            return name;
        }
        // local directory
        path = "." + File.separatorChar + name;
        file = new File(path);
        if (checkFile(file, isDirectory))
        {
            return path;
        }
        // directories
        for (StringIterator it = _dirs.getIterator(); it.hasNext();)
        {
            path = it.getNext() + File.separatorChar + name;
            file = new File(path);
            if (checkFile(file, isDirectory))
            {
                return path;
            }
        }
        return null;
    }

    protected ParserHelper getHelper()
    {
        return _helper;
    }

    public void ignoreComments(boolean ignoreThem)
    {
        _helper.ignoreComments(ignoreThem);
    }

    public boolean isParsingInterfaceOnly()
    {
        return _helper.isParsingInterfaceOnly();
    }

    protected CompilationUnit parse(Project project, UnicodeReader input) throws IOException, ParseException
    {
        _helper.setProject(project);

        try
        {
            JavaLexer  lexer  = new JavaLexer(input);
            JavaParser parser = new JavaParser(new CommentFilter(lexer, _helper));

            parser.setHelper(_helper);

            CompilationUnit unit = parser.compilationUnit(project);

            _helper.comment(unit);
            unit.setModifiableStatus(true);
            unit.setProjectUnitStatus(_projectUnitStatus);
            return unit;
        }
        catch (RecognitionException recEx)
        {
            Global.getOutput().addFatalError(recEx.getMessage(), null);
            throw new ParseException(recEx.getMessage(), recEx.line, recEx.column);
        }
        catch (ANTLRException ex)
        {
            Global.getOutput().addFatalError(ex.getMessage(), null);
            throw new ParseException(ex.getMessage());
        }
    }

    protected Block parseBlock(UnicodeReader input, ParsePosition curPos, ParsePosition start) throws IOException, ParseException
    {
        if ((start == null) || (start.getLine() < 0) || (start.getColumn() < 0))
        {
            return null;
        }
        else
        {
            try
            {
                JavaLexer  lexer  = new JavaLexer(input);
                JavaParser parser = new JavaParser(new CommentFilter(lexer, _helper));

                lexer.setPosition(start.getLine(),
                                  start.getColumn(),
                                  Seeker.advance(input, curPos, start));
                parser.setHelper(_helper);

                Block result = parser.block();

                _helper.comment(result);
                return result;
            }
            catch (RecognitionException recEx)
            {
                throw new ParseException(recEx.getMessage(), recEx.line, recEx.column);
            }
            catch (ANTLRException ex)
            {
                throw new ParseException(ex.getMessage());
            }
        }
    }

    // Allows to parse a specified directory
    // Note that the directory structure does not need to reflect the
    // package structure of the source files
    // In contrast to parsePackage, no parse notifications are created
    // (ProgressObserver)
    public boolean parseDirectory(Project project,
                                  File    path,
                                  boolean withSubDirs) throws ParseException
    {
        if (path == null)
        {
            return false;
        }

        String[] files = path.list(new JavaSourceFileFilter());

        if ((files != null) && (files.length > 0))
        {
            File file;

            for (int idx = 0; idx < files.length; idx++)
            {
                file = new File(path, files[idx]);
                if (file.isFile() && file.canRead())
                {
                    try
                    {
                        parseFile(project, file.getAbsolutePath());
                    }
                    catch (Exception ex)
                    {
                        Global.getOutput().addFatalError("Error while trying to parse "+file.getAbsolutePath()+":"+ex, null);
                    }
                }
            }
        }
        if (withSubDirs)
        {
            files = path.list(new DirectoryFilter());
            if ((files != null) && (files.length > 0))
            {
                for (int idx = 0; idx < files.length; idx++)
                {
                    parseDirectory(project,
                                   new File(path, files[idx]),
                                   true);
                }
            }
        }
        return true;
    }

    public boolean parseDirectory(Project project,
                                  String  path,
                                  boolean withSubDirs) throws ParseException
    {
        File pathFile = (path == null ? null : new File(path));

        if ((pathFile == null) || !pathFile.isDirectory() || !pathFile.canRead())
        {
            return false;
        }
        return parseDirectory(project, pathFile, withSubDirs);
    }

    // Parse all features that are not yet parsed
    private void parseFeatures(String unitName, ClassDeclaration classDecl) throws IOException, ParseException
    {
        SortedArray            features = new SortedArray();
        ParsePosition          start;
        TypeDeclaration        type;
        Initializer            initializer;
        FieldDeclaration       field;
        ConstructorDeclaration constructor;
        MethodDeclaration      method;

        for (InitializerIterator it = classDecl.getInitializers().getIterator(); it.hasNext();)
        {
            initializer = it.getNext();
            start       = (ParsePosition)initializer.getProperty(JavaParser.PROPERTY_BODY_START);
            if ((initializer.getBody() == null) && (start != null))
            {
                features.add(start, initializer);
            }
        }
        for (FieldIterator it = classDecl.getFields().getIterator(); it.hasNext();)
        {
            field = it.getNext();
            if (field.hasInitializer())
            {
                start = (ParsePosition)field.getInitializer().getProperty(JavaParser.PROPERTY_BODY_START);
                if (start != null)
                {
                    features.add(start, field);
                }
            }
        }
        for (ConstructorIterator it = classDecl.getConstructors().getIterator(); it.hasNext();)
        {
            constructor = it.getNext();
            start       = (ParsePosition)constructor.getProperty(JavaParser.PROPERTY_BODY_START);
            if ((constructor.getBody() == null) && (start != null))
            {
                features.add(start, constructor);
            }
        }
        for (MethodIterator it = classDecl.getMethods().getIterator(); it.hasNext();)
        {
            method = it.getNext();
            start  = (ParsePosition)method.getProperty(JavaParser.PROPERTY_BODY_START);
            if ((method.getBody() == null) && (start != null))
            {
                features.add(start, method);
            }
        }

        // Now we have gathered all features that need to be parsed
        if (!features.isEmpty())
        {
            UnicodeReader       input = new UnicodeReader(reopen(unitName));
            Feature             feature;
            Block               body;
            VariableInitializer init;
            ParsePosition       finish = new ParsePosition(1, 1, 1);

            _helper.pushTypeScope(classDecl);
            for (int idx = 0; idx < features.getCount(); idx++)
            {
                start   = (ParsePosition)features.getKey(idx);
                feature = (Feature)features.get(idx);
                if (feature instanceof FeatureWithBody)
                {
                    if (feature instanceof InvocableDeclaration)
                    {
                        defineParameters(((InvocableDeclaration)feature).getParameterList());
                    }
                    body = parseBlock(input, finish, start);
                    if (feature instanceof InvocableDeclaration)
                    {
                        undefineParameters(((InvocableDeclaration)feature).getParameterList());
                    }
                    ((FeatureWithBody)feature).setBody(body);
                    finish.copyFrom(body.getFinishPosition());
                }
                else
                {
                    // a field
                    field = (FieldDeclaration)feature;
                    init  = parseVarInit(input, finish, start);
                    field.setInitializer(init);
                    finish.copyFrom(init.getFinishPosition());
                }
                finish.incColumn();
            }
            _helper.popTypeScope();
        }

        for (TypeDeclarationIterator typeIt = classDecl.getInnerTypes().getIterator(); typeIt.hasNext();)
        {
            type = typeIt.getNext();
            if (type instanceof ClassDeclaration)
            {
                parseFeatures(unitName, (ClassDeclaration)type);
            }
        }
    }

    public void parseFile(Project project,
                          String  filename) throws IOException, ParseException
    {
        parseStream(project, filename, new FileReader(filename));
    }

    public boolean parsePackage(Project project,
                                String  qualifiedName,
                                boolean withSubPackages) throws ParseException
    {
        if (qualifiedName == null)
        {
            qualifiedName = "";
        }

        String path   = qualifiedName.replace('.', File.separatorChar);
        String srcDir = find(path, true);

        if (srcDir != null)
        {
            String[] files = (new File(srcDir)).list(new JavaSourceFileFilter());

            if ((files != null) && (files.length > 0))
            {
                String base     = (qualifiedName.length() > 0 ? qualifiedName + "." : "");
                String typeName;
                long   numFiles = 0;
                File   file;

                notifyStartPackage(qualifiedName, files.length);
                for (int idx = 0; idx < files.length; idx++)
                {
                    typeName = base + files[idx].replace(File.separatorChar, '.');
                    typeName = typeName.substring(0, typeName.length() - 5);
                    file     = new File(srcDir + File.separator + files[idx]);
                    if (file.isFile() && file.canRead())
                    {
                        notifyStartType(typeName);
                        try
                        {
                            parseFile(project,
                                      file.getAbsolutePath());
                        }
                        catch (Exception ex)
                        {
                            Global.getOutput().addFatalError("Error while trying to parse "+file.getAbsolutePath()+":"+ex, null);
                        }
                        notifyFinishType(typeName);
                    }
                    numFiles++;
                }
                notifyFinishPackage(qualifiedName);
            }
            if (withSubPackages)
            {
                files = (new File(srcDir)).list(new DirectoryFilter());
                if ((files != null) && (files.length > 0))
                {
                    String base = (qualifiedName.length() > 0 ? qualifiedName + "." : "");

                    for (int idx = 0; idx < files.length; idx++)
                    {
                        parsePackage(project,
                                     base + files[idx],
                                     true);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void parseStream(Project project,
                            String  filename,
                            Reader  input) throws IOException, ParseException
    {
        _helper.init();
        _helper.setUnitName(filename);
        parse(project, new UnicodeReader(new BufferedReader(input)));
    }

    public boolean parseType(Project project,
                             String  qualifiedName) throws ParseException
    {
        String  filename = find(qualifiedName.replace('.', File.separatorChar) + ".java", false);
        boolean result   = false;

        if (filename != null)
        {
            File srcFile = new File(filename);

            if (srcFile.isFile() && srcFile.canRead())
            {
                notifyStartType(qualifiedName);
                try
                {
                    parseFile(project, srcFile.getAbsolutePath());
                    result = true;
                }
                catch (Exception ex)
                {
                    Global.getOutput().addFatalError("Error while trying to parse "+srcFile.getAbsolutePath()+":"+ex, null);
                }
                notifyFinishType(qualifiedName);
            }
        }
        return result;
    }

    protected VariableInitializer parseVarInit(UnicodeReader input,
                                               ParsePosition curPos,
                                               ParsePosition start) throws IOException, ParseException
    {
        if ((start == null) || (start.getLine() < 0) || (start.getColumn() < 0))
        {
            return null;
        }
        else
        {
            try
            {
                JavaLexer  lexer  = new JavaLexer(input);
                JavaParser parser = new JavaParser(new CommentFilter(lexer, _helper));

                lexer.setPosition(start.getLine(),
                                  start.getColumn(),
                                  Seeker.advance(input, curPos, start));
                parser.setHelper(_helper);

                VariableInitializer result = parser.variableInitializer();

                _helper.comment(result);
                return result;
            }
            catch (RecognitionException recEx)
            {
                throw new ParseException(recEx.getMessage(), recEx.line, recEx.column);
            }
            catch (ANTLRException ex)
            {
                throw new ParseException(ex.getMessage());
            }
        }
    }

    private Reader reopen(String unitName) throws FileNotFoundException
    {
        return new BufferedReader(new FileReader(unitName));
    }

    public void setParsingInterface(boolean onlyInterface)
    {
        _helper.setParsingInterface(onlyInterface);
    }

    // Reduces the compilation unit of a class declaration
    // to its interface (no method bodies etc.)
    public void uncomplete(CompilationUnit unit)
    {
        TypeDeclaration decl;

        _helper.setProject((Project)unit.getContainer());
        for (TypeDeclarationIterator typeDeclIt = unit.getTypeDeclarations().getIterator(); typeDeclIt.hasNext();)
        {
            decl = typeDeclIt.getNext();
            if (decl instanceof ClassDeclaration)
            {
                uncompleteFeatures((ClassDeclaration)decl);
            }
        }
        unit.setComplete(false);
        // Do not need new resolving status !
    }

    private void uncompleteFeatures(ClassDeclaration classDecl)
    {
        ParsePosition          start;
        TypeDeclaration        type;
        Initializer            initializer;
        ConstructorDeclaration constructor;
        MethodDeclaration      method;

        for (InitializerIterator initializerIt = classDecl.getInitializers().getIterator(); initializerIt.hasNext();)
        {
            initializer = initializerIt.getNext();
            if (initializer.getBody() != null)
            {
                start = initializer.getBody().getStartPosition();
                initializer.setProperty(JavaParser.PROPERTY_BODY_START, start);
                initializer.setBody(null);
            }
        }
        for (ConstructorIterator constructorIt = classDecl.getConstructors().getIterator(); constructorIt.hasNext();)
        {
            constructor = constructorIt.getNext();
            if (constructor.getBody() != null)
            {
                start = constructor.getBody().getStartPosition();
                constructor.setProperty(JavaParser.PROPERTY_BODY_START, start);
                constructor.setBody(null);
            }
        }
        for (MethodIterator methodIt = classDecl.getMethods().getIterator(); methodIt.hasNext();)
        {
            method = methodIt.getNext();
            if (method.getBody() != null)
            {
                start = method.getBody().getStartPosition();
                method.setProperty(JavaParser.PROPERTY_BODY_START, start);
                method.setBody(null);
            }
        }

        for (TypeDeclarationIterator typeIt = classDecl.getInnerTypes().getIterator(); typeIt.hasNext();)
        {
            type = typeIt.getNext();
            if (type instanceof ClassDeclaration)
            {
                uncompleteFeatures((ClassDeclaration)type);
            }
        }
    }

    private void undefineParameters(FormalParameterList params)
    {
        if (params != null)
        {
            _helper.popBlockScope();
        }
    }
}
