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
package jart.analysis;
import jast.analysis.InheritanceAnalyzer;
import jast.analysis.TypeUsageIterator;
import jast.analysis.TypeUsages;
import jast.analysis.Usages;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.CompilationUnit;
import jast.ast.nodes.ImportDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.ImportIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.helpers.StringArray;


/**
 *  This class implements a positive list (circumstancial
 *  evidences only) for determining whether a given class
 *  is a test-class:
 *      + imports junit.* or jtest.*
 *      + inherits from TestCase/TestSuite/TestPackage/??? (jtest ?)
 *    + uses assert* methods
 *    + only few methods whose names do not begin with "test"
 *    + uses TestCase/TestSuite/TestPackage/??? (jtest ?)
 *
 *  If at least 3 from 5 are hold then a class is regarded
 *  as test class.
 */
public class IsTestClass implements AnalysisFunction
{

    private final static int MIN_PERCENT_OF_TEST_METHODS = 75;

    private final static String[] TEST_CLASSES =
    {
        "junit.framework.Assert",
        "junit.framework.TestSuite",
        "junit.framework.TestPackage"
    };

    private final static String[] TEST_PACKAGES =
    {
        "junit",
        "jtest"
    };

    public IsTestClass()
    {
        super();
    }

    public boolean check(Project project, Node node)
    {
        if ((node == null) || !isApplicable(node))
            {
            return false;
        }

        int indicatorCount = 0;
        ClassDeclaration clsDecl = (ClassDeclaration) node;

        if (containsMainlyTestMethods(clsDecl))
            {
            indicatorCount++;
        }
        if (usesAssertMethod(clsDecl))
            {
            indicatorCount++;
        }
        if (inheritsFromTestClass(clsDecl))
            {
            indicatorCount++;
        }
        if (usesTestClass(clsDecl))
            {
            indicatorCount++;
        }
        if (importsTestPackage(clsDecl))
            {
            indicatorCount++;
        }

        return (indicatorCount >= 3);
    }

    public boolean containsMainlyTestMethods(ClassDeclaration clsDecl)
    {
        if (clsDecl == null)
            {
            return false;
        }

        float methodCount = clsDecl.getMethods().getCount();
        float testMethodCount = getLocalDefinedMethodCount(clsDecl, "test");

        return ((100 * testMethodCount / methodCount) >= MIN_PERCENT_OF_TEST_METHODS);
    }

    private int getLocalDefinedMethodCount(
        ClassDeclaration clsDecl,
        String methodNameStartsWith)
    {
        return getMethodNamesStartWith(clsDecl, methodNameStartsWith).getCount();
    }

    private StringArray getMethodNamesStartWith(
        ClassDeclaration clsDecl,
        String methodNameStartsWith)
    {
        String methName = methodNameStartsWith.toLowerCase();
        StringArray result = new StringArray();

        if ((clsDecl == null)
            || (methodNameStartsWith == null)
            || (methodNameStartsWith.length() == 0))
            {
            return result;
        }

        for (MethodIterator iter = clsDecl.getMethods().getIterator(); iter.hasNext();)
            {
            MethodDeclaration curMethod = iter.getNext();
            String curName = curMethod.getName();
            // =!= had the method in question to be public?
            if (curName.toLowerCase().startsWith(methName))
                {
                result.add(curName);
            }
        }

        return result;
    }

    private boolean hasImportFor(ClassDeclaration clsDecl, String /*
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
packageOrType)
    {
        if (clsDecl == null)
            {
            return false;
        }

        if (clsDecl.getContainer() instanceof CompilationUnit)
            {
            CompilationUnit cu = (CompilationUnit) clsDecl.getContainer();

            for (ImportIterator iter = cu.getImportDeclarations().getIterator();
                iter.hasNext();
                )
                {
                ImportDeclaration currentImport = iter.getNext();
                String importString = currentImport.getImportedPackageOrType();

                if ((importString.indexOf(/*
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
packageOrType + ".") > -1)
                    || importString.equals(/*
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
packageOrType))
                    {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean importsTestPackage(ClassDeclaration clsDecl)
    {
        for (int idx = 0; idx < TEST_PACKAGES.length; idx++)
            {
            if (hasImportFor(clsDecl, TEST_PACKAGES[idx]))
                {
                return true;
            }
        }

        return false;
    }

    public boolean inheritsFromTestClass(ClassDeclaration clsDecl)
    {
        TypeDeclarationArray parents = new InheritanceAnalyzer(clsDecl).getAncestors();

        for (int idx = 0; idx < TEST_CLASSES.length; idx++)
            {
            if (parents.contains(TEST_CLASSES[idx]))
                {
                return true;
            }
        }

        return false;
    }

    private boolean isApplicable(Node node)
    {
        Class[] applicableClasses = isApplicableTo();

        for (int idx = 0; idx < applicableClasses.length; idx++)
            {
            if (applicableClasses[idx].isInstance(node))
                {
                return true;
            }
        }

        return false;
    }

    public Class[] isApplicableTo()
    {
        return new Class[] { ClassDeclaration.class };
    }

    public boolean usesAssertMethod(ClassDeclaration clsDecl)
    {
        if (usesMethod(clsDecl, "assert"))
            {
            return true;
        }

        return false;
    }

    private boolean usesMethod(
        ClassDeclaration clsDecl,
        String methodNameStartsWith)
    {
        if ((clsDecl == null)
            || (methodNameStartsWith == null)
            || (methodNameStartsWith.length() < 1))
            {
            return false;
        }

        TypeUsages usages = (TypeUsages) clsDecl.getProperty(Usages.PROPERTY_LABEL);

        for (TypeUsageIterator iter = usages.getUsedTypes().getIterator();
            iter.hasNext();
            )
            {
            if (iter.getNext().usesMethod(methodNameStartsWith))
                {
                return true;
            }
        }

        return false;
    }

    public boolean usesTestClass(ClassDeclaration clsDecl)
    {
        TypeUsages usages = (TypeUsages) clsDecl.getProperty(Usages.PROPERTY_LABEL);

        for (int idx = 0; idx < TEST_CLASSES.length; idx++)
            {
            for (TypeUsageIterator iter = usages.getUsedTypes().getIterator();
                iter.hasNext();
                )
                {

                TypeDeclaration curType = iter.getNext().getType();
                if (curType.getQualifiedName().equals(TEST_CLASSES[idx]))
                    {
                    return true;
                }

                InheritanceAnalyzer inhAnalizer = new InheritanceAnalyzer(curType);
                if (inhAnalizer.getAncestors().contains(TEST_CLASSES[idx]))
                    {
                    return true;
                }
            }
        }

        return false;
    }
}
