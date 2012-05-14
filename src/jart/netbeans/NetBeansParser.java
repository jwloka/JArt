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
package jart.netbeans;
import jast.FileParser;
import jast.Global;
import jast.ParseException;
import jast.SyntaxException;
import jast.ast.NodeFactory;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.FormalParameterList;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Initializer;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.TypeDeclaration;
import jast.parser.UnicodeReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.src.ClassElement;
import org.openide.src.ConstructorElement;
import org.openide.src.FieldElement;
import org.openide.src.Identifier;
import org.openide.src.Import;
import org.openide.src.InitializerElement;
import org.openide.src.MethodElement;
import org.openide.src.MethodParameter;
import org.openide.src.SourceElement;

public class NetBeansParser extends FileParser
{

    public static boolean isFromSource(ClassElement classElem)
    {
        DataObject dataObj =
            (DataObject) classElem.getSource().getCookie(DataObject.class);

        if (dataObj == null)

            {
            return false;
        }

        FileObject fileObj = dataObj.getPrimaryFile();

        if ("java".equalsIgnoreCase(fileObj.getExt()))
            {
            // We will assume that the ending java is reserved for source files
            return true;
        }
        if ("class".equalsIgnoreCase(fileObj.getExt()))
            {
            // Same for class and bytecode files
            return false;
        }

        // We determine whether the type is from source via the
        // following indicators :
        // * fields with initializers
        // * methods/constructors with bodies
        // * type initializers
        FieldElement[] fieldElems = classElem.getFields();
        ConstructorElement[] constructorElem = classElem.getConstructors();
        MethodElement[] methodElems = classElem.getMethods();
        InitializerElement[] initElems = classElem.getInitializers();
        int idx;

        if ((initElems != null) && (initElems.length > 0))
            {
            return true;
        }
        if (classElem.isInterface()
            && ((fieldElems == null) || (fieldElems.length == 0)))
            {
            return true;
        }
        if (fieldElems != null)
            {
            for (idx = 0; idx < fieldElems.length; idx++)
                {
                if (fieldElems[idx].getInitValue().length() > 0)
                    {
                    // initialized field
                    return true;
                }
            }
        }
        if (constructorElem != null)
            {
            for (idx = 0; idx < constructorElem.length; idx++)
                {
                if ((constructorElem[idx].getBody() != null)
                    && (constructorElem[idx].getBody().length() > 0))
                    {
                    return true;
                }
            }
        }
        if (methodElems != null)
            {
            for (idx = 0; idx < methodElems.length; idx++)
                {
                if ((methodElems[idx].getBody() != null)
                    && (methodElems[idx].getBody().length() > 0))
                    {
                    return true;
                }
            }
        }

        return false;
    }

    private static String convertTypeName(String originalName)
    {
        String result = originalName;
        int pos = -1;

        if (result != null)
            {
            while ((pos = result.indexOf("$", pos + 1)) >= 0)
                {
                if (Character.isDigit(result.charAt(pos + 1)))
                    {
                    Global.getOutput().addNote(
                        "==> Found type name of anonymous type :" + originalName,
                        null);
                }
                result = result.substring(0, pos) + "." + result.substring(pos + 1);
            }
        }
        return result;
    }

    public NetBeansParser()
    {
        super();
    }

    private CompilationUnit parse(Project project, SourceElement sourceElem)
    {
        CompilationUnit unit =
            Global.getFactory().createCompilationUnit(getHelper().getUnitName());
        Import[] imports = sourceElem.getImports();
        ClassElement[] classes = sourceElem.getAllClasses();
        boolean hasParsed = false;
        TypeDeclaration type;
        int idx;

        getHelper().setProject(project);
        unit.setComplete(false);
        unit.setModifiableStatus(false);
        unit.setProjectUnitStatus(getProjectUnitStatus());

        //Global.getOutput().addNote("Starting to parse source element "+unit.getName(), null);
        for (idx = 0; idx < imports.length; idx++)
            {
            unit.getImportDeclarations().add(parseImportDeclaration(imports[idx]));
        }
        for (idx = 0; idx < classes.length; idx++)
            {
            // inner classes are parsed automatically, so we do not want them here
            if (classes[idx].isInner())
                {
                continue;
            }
            try
                {
                type = parseTypeDeclaration(classes[idx]);
                if (type != null)
                    {
                    unit.getTypeDeclarations().add(type);
                }
                hasParsed = true;
            }
            catch (SyntaxException ex)
                {
                Global.getOutput().addError(
                    "Error while parsing " + unit.getName() + ":\n" + ex,
                    null);
            }
        }
        if (hasParsed)
            {
            project.addCompilationUnit(unit, sourceElem.getPackage().getFullName());
            //Global.getOutput().addNote("Added NetBeans-parsed type "+unit.getName()+" in package "+sourceElem.getPackage().getFullName()+" to project", null);
            return unit;
        }
        else
            {
            Global.getOutput().addNote(
                "No top-level type in source element " + unit.getName(),
                null);
            return null;
        }
    }

    private ImportDeclaration parseImportDeclaration(Import importElem)
    {
        return Global.getFactory().createImportDeclaration(
            importElem.toString(),
            importElem.isPackage());
    }

    private TypeDeclaration parseTypeDeclaration(ClassElement classElem)
        throws SyntaxException
    {
        NodeFactory factory = Global.getFactory();
        TypeDeclaration typeDecl = null;
        Modifiers mods = factory.createModifiers(classElem.getModifiersMask());
        String typeName = convertTypeName(classElem.getName().toString());
        String name;
        int idx;

        if (classElem.isInterface())
            {
            typeDecl = factory.createInterfaceDeclaration(mods, typeName);
        }
        else
            {
            typeDecl = factory.createClassDeclaration(mods, typeName, false);
            if (classElem.getSuperclass() != null)
                {
                name = convertTypeName(classElem.getSuperclass().getFullName());
                if ((name != null) && (name.length() > 0))
                    {
                    ((ClassDeclaration) typeDecl).setBaseClass(factory.createType(name, 0));
                }
            }
        }
        if (classElem.getInterfaces() != null)
            {
            Identifier[] interfaces = classElem.getInterfaces();

            for (idx = 0; idx < interfaces.length; idx++)
                {
                name = convertTypeName(interfaces[idx].getFullName());
                if ((name != null) && (name.length() > 0))
                    {
                    typeDecl.getBaseInterfaces().add(factory.createType(name, 0));
                }
            }
        }
        if (classElem.getClasses() != null)
            {
            ClassElement[] types = classElem.getClasses();

            for (idx = 0; idx < types.length; idx++)
                {
                typeDecl.getInnerTypes().add(parseTypeDeclaration(types[idx]));
            }
        }
        if (classElem.getInitializers() != null)
            {
            InitializerElement[] inits = classElem.getInitializers();

            for (idx = 0; idx < inits.length; idx++)
                {
                ((ClassDeclaration) typeDecl).getInitializers().add(
                    parseInitializer(inits[idx]));
            }
        }
        if (classElem.getFields() != null)
            {
            FieldElement[] fields = classElem.getFields();

            for (idx = 0; idx < fields.length; idx++)
                {
                typeDecl.getFields().add(parseFieldDeclaration(fields[idx]));
            }
        }
        if (classElem.getConstructors() != null)
            {
            ConstructorElement[] constructors = classElem.getConstructors();

            for (idx = 0; idx < constructors.length; idx++)
                {
                ((ClassDeclaration) typeDecl).getConstructors().add(
                    parseConstructorDeclaration(constructors[idx]));
            }
        }
        if (classElem.getMethods() != null)
            {
            MethodElement[] methods = classElem.getMethods();

            for (idx = 0; idx < methods.length; idx++)
                {
                typeDecl.getMethods().add(parseMethodDeclaration(methods[idx]));
            }
        }
        return typeDecl;
    }

    private Initializer parseInitializer(InitializerElement initElem)
    {
        return Global.getFactory().createInitializer(null, initElem.isStatic());
    }

    private FieldDeclaration parseFieldDeclaration(FieldElement fieldElem)
        throws SyntaxException
    {
        return Global.getFactory().createFieldDeclaration(
            Global.getFactory().createModifiers(fieldElem.getModifiersMask()),
            convertType(fieldElem.getType()),
            fieldElem.getName().toString(),
            null);
    }

    private ConstructorDeclaration parseConstructorDeclaration(ConstructorElement constructorElem)
        throws SyntaxException
    {
        // NetBeans seems to use the qualified type name as the name
        // of the constructor; therefore we remove the package part
        String name = convertTypeName(constructorElem.getName().toString());
        int pos = name.lastIndexOf(".");

        if (pos > 0)
            {
            name = name.substring(pos + 1);
        }

        return Global.getFactory().createConstructorDeclaration(
            Global.getFactory().createModifiers(constructorElem.getModifiersMask()),
            name,
            convertParameters(constructorElem.getParameters()));
    }

    private MethodDeclaration parseMethodDeclaration(MethodElement methodElem)
        throws SyntaxException
    {
        return Global.getFactory().createMethodDeclaration(
            Global.getFactory().createModifiers(methodElem.getModifiersMask()),
            convertType(methodElem.getReturn()),
            methodElem.getName().toString(),
            convertParameters(methodElem.getParameters()));
    }

    private FormalParameterList convertParameters(MethodParameter[] params)
        throws SyntaxException
    {
        NodeFactory factory = Global.getFactory();
        FormalParameterList paramList = null;
        String name = null;

        if ((params != null) && (params.length > 0))
            {
            paramList = factory.createFormalParameterList();
            for (int idx = 0; idx < params.length; idx++)
                {
                name = params[idx].getName();
                if ((name == null) || (name.length() == 0))
                    {
                    name = "param" + idx;
                }
                paramList.getParameters().add(
                    factory.createFormalParameter(
                        params[idx].isFinal(),
                        convertType(params[idx].getType()),
                        name));
            }
        }
        return paramList;
    }

    private jast.ast.nodes.Type convertType(org.openide.src.Type type)
        throws SyntaxException
    {
        if (type.getFullString().equals("void"))
            {
            return null;
        }
        else
            {
            org.openide.src.Type baseType = type;
            int dim = 0;

            while (baseType.isArray())
                {
                dim++;
                baseType = baseType.getElementType();
            }

            jast.ast.nodes.Type result =
                Global.getFactory().createType(
                    convertTypeName(baseType.getFullString()),
                    baseType.isPrimitive());

            result.setDimensions(dim);
            return result;
        }
    }

    public boolean parsePackage(
        Project project,
        String name,
        boolean withSubPackages)
        throws ParseException
    {
        // Currently not used in NetBeans
        return false;
    }

    public boolean parseType(Project project, String qualifiedName)
        throws ParseException
    {
        ClassElement classElem = ClassElement.forName(qualifiedName);

        if (classElem == null)
            {
            // no type of that name (possibly a package name)
            return false;
        }

        SourceElement sourceElem = classElem.getSource();

        if (sourceElem == null)
            {
            Global.getOutput().addError(
                "Identifier " + qualifiedName + " does not denote a valid type",
                null);
            return false;
        }

        DataObject dataObj = (DataObject) sourceElem.getCookie(DataObject.class);

        //Global.getOutput().addNote("Type "+qualifiedName+(dataObj != null ? " has" : " hasn't")+" a data object", null);

        FileObject fileObj = (dataObj == null ? null : dataObj.getPrimaryFile());
        CompilationUnit unit = null;
        boolean fromSrc = (fileObj != null) && isFromSource(classElem);

        getHelper().init();
        // As opposed to the FileParser we use the qualified name because
        // under NetBeans this will be much more useful
        getHelper().setUnitName(
            fileObj == null ? qualifiedName : fileObj.getPackageName('.'));

        if (fromSrc)

            {
            // Using source to parse the compilation unit
            //Global.getOutput().addNote("Trying to read type "+qualifiedName+" from source", null);
            try
                {
                unit =
                    parse(
                        project,
                        new UnicodeReader(
                            new BufferedReader(new InputStreamReader(fileObj.getInputStream()))));
                unit.setProperty(
                    Integration.PROPERTY_LASTMODIFIED,
                    fileObj.lastModified().toString());
            }
            catch (Exception ex)
                {
                Global.getOutput().addError(
                    "Could not parse file " + getHelper().getUnitName() + " from source:\n" + ex,
                    null);
                fromSrc = false;
            }
        }
        if (!fromSrc)
            {
            // Using NetBeans to retrieve the interface of the type
            //Global.getOutput().addNote("Reading type "+qualifiedName+" with OpenAPI", null);
            unit = parse(project, sourceElem);
        }
        if (unit != null)
            {
            //Global.getOutput().addNote("Reading of type "+qualifiedName+" done", null);
            unit.setModifiableStatus(fromSrc && !fileObj.isReadOnly());

            return true;
        }
        else
            {
            Global.getOutput().addNote(
                "Could not successfully read type " + qualifiedName,
                null);
            return false;
        }
    }

    public boolean existsType(String qualifiedName)
    {
        return (ClassElement.forName(qualifiedName) != null);
    }
}