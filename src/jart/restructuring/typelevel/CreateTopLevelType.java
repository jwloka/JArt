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
package jart.restructuring.typelevel;

import jart.restructuring.Restructuring;
import jart.restructuring.packagelevel.CreatePackage;
import jast.Global;
import jast.analysis.AnalysisHelper;
import jast.analysis.TypeUsageIterator;
import jast.analysis.Usages;
import jast.ast.ASTHelper;
import jast.ast.Node;
import jast.ast.NodeFactory;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Package;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.CompilationUnitArray;
import jast.ast.nodes.collections.CompilationUnitArrayImpl;
import jast.ast.nodes.collections.CompilationUnitIterator;
import jast.ast.nodes.collections.ImportIterator;
import jast.ast.nodes.collections.TypeDeclarationIterator;

public class CreateTopLevelType extends Restructuring
{
    /** The project which will contain the new type */
    private Project         _project     = null;
    /** The name of the package to add the new type to */
    private String          _pckgName    = null;
    /** The name of new type */
    private String          _typeName    = null;
    /** The unit of the new type */
    private CompilationUnit _unit        = null;
    /** Helper restructuring */
    private CreatePackage   _pckgCreator = null;

    public TypeDeclaration getCreatedType()
    {
        return _project.getType(getQualifiedName());
    }

    public boolean analyze()
    {
        if (_project.getType(_pckgName) != null)
        {
            _results.addError("The given package name does not denote a package but an existing type",
                               _project.getType(_pckgName));
            return false;
        }
        if (_project.getPackage(getQualifiedName()) != null)
        {
            _results.addError("The given qualified name denotes an already existing package",
                               null);
            return false;
        }

        Package pckg = _project.getPackage(_pckgName);

        if (pckg == null)
        {
            Node[]   nodes = { _project };
            Object[] args  = { _pckgName };

            _pckgCreator = new CreatePackage();
            _pckgCreator.setResultContainer(_results);
            if (!_pckgCreator.initialize(nodes, args))
            {
                return false;
            }
            if (!_pckgCreator.analyze())
            {
                return false;
            }
        }
        else
        {
            if (pckg.getTypes().get(_typeName) != null)
            {
                _results.addError("A type with this qualified name already exists",
                                   pckg.getTypes().get(_typeName));
                return false;
            }
        }
        if (Global.getParser().existsType("java.lang."+_typeName))
        {
            _results.addWarning("A type of that name exists in the java.lang package",
                                 null);
        }
        if (Global.getParser().existsType(_typeName))
        {
            _results.addWarning("A type of that name exists in the default package",
                                 null);
        }

        return true;
    }

        private void determineAffectedUnits()
        {
            CompilationUnitArray candidates = new CompilationUnitArrayImpl();
            CompilationUnit      unit;
            TypeDeclaration      typeDecl;
            ImportDeclaration    importDecl;
            Usages               usages;

            for (CompilationUnitIterator unitIt = _project.getCompilationUnits().getIterator(); unitIt.hasNext();)
            {
                unit = unitIt.getNext();
                if (ASTHelper.isInSamePackage(_unit, unit) &&
                    (_unit != unit))
                {
                    candidates.add(unit);
                    continue;
                }
                for (ImportIterator importIt = unit.getImportDeclarations().getIterator(); importIt.hasNext();)
                {
                    importDecl = importIt.getNext();
                    if (importDecl.isOnDemand() &&
                        importDecl.getImportedPackageOrType().equals(_pckgName))
                    {
                        candidates.add(unit);
                        break;
                    }
                }
            }
            for (CompilationUnitIterator unitIt = candidates.getIterator(); unitIt.hasNext();)
            {
                unit = unitIt.getNext();
    outer:        for (TypeDeclarationIterator typeIt = unit.getTypeDeclarations().getIterator(); typeIt.hasNext();)
                {
                    usages = AnalysisHelper.ensureUsagesPresent(typeIt.getNext());
                    for (TypeUsageIterator usageIt = usages.getUsedTypes().getIterator(); usageIt.hasNext(); )
                    {
                        if (_typeName.equals(usageIt.getNext().getType().getName()))
                        {
                            _results.getChangedUnits().add(unit);
                            break outer;
                        }
                    }
                }

            }
        }

    private String getQualifiedName()
    {
        if (_pckgName.length() == 0)
        {
            return _typeName;
        }
        else
        {
            return _pckgName + "." + _typeName;
        }
    }

    /**
     * Initializes the restructuring. The input node is the project to add
     * the type to (is not necessary if a package node is given as a
     * processing data item). Processing data consists of a boolean indicating
     * that a class shall be created (interface otherwise) and the name of the
     * new type which can be given in one of three forms:<br><ul>
     * <li>the package to add the type to plus the unqualified type name</li>
     * <li>the name of the package to add the type to plus the unqualified type name</li>
     * <li>the qualified type name</li>
     * </li><br>
     * Except for the first variant, the project must be given as input node.
     *
     * @param  nodes          The project to add the type to
     * @param  processingData Whether to create a class, and the qualified type name
     *                        in one of the forms described above
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public boolean initialize(Node[] nodes, Object[] processingData)
    {
        boolean createClass;

        if (nodes != null)
        {
            if (nodes.length == 1)
            {
                if (!(nodes[0] instanceof Project))
                {
                    _results.addFatalError("The input node must be a project",
                                            null);
                    return false;
                }
                _project = (Project)nodes[0];
            }
            else if (nodes.length > 0)
            {
                _results.addFatalError("No valid input data given",
                                        null);
                return false;
            }
        }
        if ((processingData == null) ||
            (processingData.length == 0))
        {
            _results.addFatalError("No valid input data given",
                                    null);
            return false;
        }
        if (processingData.length > 3)
        {
            _results.addFatalError("Too much processing data",
                                    null);
            return false;
        }
        if (!(processingData[0] instanceof Boolean))
        {
            _results.addFatalError("The first processing data item has to be a boolean indicating whether to create a class",
                                    null);
            return false;
        }
        createClass = ((Boolean)processingData[0]).booleanValue();

        if (processingData[1] instanceof Package)
        {
            Package pckg = (Package)processingData[1];

            _pckgName = pckg.getQualifiedName();
            _project  = ASTHelper.getProjectOf(pckg);

            if ((processingData.length != 3) ||
                !(processingData[2] instanceof String))
            {
                _results.addFatalError("The unqualified type name is missing (2nd processing data item)",
                                        null);
                return false;
            }
            _typeName = (String)processingData[2];
        }
        else if (processingData[1] instanceof String)
        {
            _pckgName = (String)processingData[1];
            if (processingData.length == 3)
            {
                if (!(processingData[2] instanceof String))
                {
                    _results.addFatalError("The second processing data item has to state the unqualified type name ",
                                            null);
                    return false;
                }
                _typeName = (String)processingData[2];
            }
            else
            {
                int pos = _pckgName.lastIndexOf('.');

                _typeName = _pckgName.substring(pos+1);
                if (pos >= 0)
                {
                    _pckgName = _pckgName.substring(0, pos);
                }
                else
                {
                    _pckgName = "";
                }
            }
            if (_project == null)
            {
                _results.addFatalError("A project is required (as the input node)",
                                        null);
                return false;
            }
        }
        if (_typeName.indexOf('.') >= 0)
        {
            _results.addFatalError("The name of the new type must be unqualified",
                                    null);
            return false;
        }

        NodeFactory     factory  = Global.getFactory();
        Modifiers       mods     = factory.createModifiers();
        String          fileName = java.io.File.separator +
                                   getQualifiedName().replace('.', java.io.File.separatorChar) +
                                   ".java";
        TypeDeclaration typeDecl;

        // the real filename will be set by the application
        _unit = factory.createCompilationUnit(fileName);

        mods.setPublic();
        if (createClass)
        {
            typeDecl = factory.createClassDeclaration(
                           mods,
                           _typeName,
                           false);
        }
        else
        {
            typeDecl = factory.createInterfaceDeclaration(
                           mods,
                           _typeName);
        }
        _unit.getTypeDeclarations().add(typeDecl);

        return true;
    }

    public boolean perform()
    {
        if (_pckgCreator != null)
        {
            if (!_pckgCreator.perform())
            {
                return false;
            }
        }
        _project.addCompilationUnit(_unit, _pckgName);
        _results.markUnitAsNew(_unit);

        // add all other affected units
        determineAffectedUnits();

        return true;
    }
}
