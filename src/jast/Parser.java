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

import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;

public interface Parser extends ProgressSource
{


    // Completes the parse of the given unit (ie fills the methods, fields
    // and initializers)
    public void complete(CompilationUnit unit) throws ParseException;

    // Indicates that the type with the qualified name can be parsed
    // Is probably a conservative approximation, e.g. a file parser might
    // determine that there is a corresponding source file, but the file
    // does not have to contain a type
    public boolean existsType(String qualifiedName);

    public boolean getProjectUnitStatus();

    public void ignoreComments(boolean ignoreThem);

    public boolean isParsingInterfaceOnly();

    // Tries to parse the indicated package into the given project
    public boolean parsePackage(Project project,
                                String  name,
                                boolean withSubPackages) throws ParseException;

    // Tries to parse the indicated type into the given project
    public boolean parseType(Project project,
                             String  qualifiedName) throws ParseException;

    public void setParsingInterface(boolean onlyInterface);

    public void setProjectUnitStatus(boolean willBeProjectUnit);

    // Reduces the compilation unit of a class declaration
    // to its interface (no method bodies etc.)
    public void uncomplete(CompilationUnit unit);
}
