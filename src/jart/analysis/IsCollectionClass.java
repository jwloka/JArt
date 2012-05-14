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
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.FieldDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.FieldIterator;
import jast.ast.nodes.collections.MethodIterator;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.helpers.StringArray;


public class IsCollectionClass implements AnalysisFunction
{

    private final static String[] COLLECTION_CLASSES =
    {
        "java.util.Collection",
        "java.util.Map",
        "java.util.BitSet",
        "java.util.Dictionary",
        "java.security.PermissionCollection",
        "java.sql.ResultSet",
        "javax.accessibility.AccessibleRelationSet",
        "javax.accessibility.AccessibleStateSet",
        "javax.swing.ActionMap",
        "javax.swing.InputMap",
        "javax.swing.event.EventListenerList",
        "javax.swing.text.AttributeSet",
        "javax.swing.text.Keymap",
        "org.omg.CORBA.ContextList",
        "org.omg.CORBA.ExceptionList",
        "org.omg.CORBA.NVList"
    };

    private final static String[] ADD_METHODS =
    {
        "add",
        "append",
        "insert"
    };
    private final static String[] REMOVE_METHODS =
    {
        "remove",
        "delete"
    };

    private final static String[] STACK_METHODS =
    {
        "push",
        "pop",
        "top"
    };

    public IsCollectionClass()
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
        TypeDeclaration type = (TypeDeclaration) node;

        if (isKnownCollection(type))
            {
            return true;
        }

        if (inheritsFromCollection(type))
            {
            indicatorCount++;
        }
        if (containsCollection(type))
            {
            indicatorCount++;
        }
        if (implementsAddPattern(type))
            {
            indicatorCount++;
        }
        if (implementsRemovePattern(type))
            {
            indicatorCount++;
        }
        if (implementsStackPattern(type))
            {
            indicatorCount++;
        }

        return (indicatorCount >= 3);
    }

    public boolean containsCollection(TypeDeclaration type)
    {
        for (FieldIterator fieldIter = type.getFields().getIterator();
            fieldIter.hasNext();
            )
            {
            FieldDeclaration curField = fieldIter.getNext();
            TypeDeclaration owningType = curField.getType().getReferenceBaseTypeDecl();
            TypeDeclarationArray owningTypeAncestors =
                new InheritanceAnalyzer(owningType).getAncestors();

            for (int idx = 0; idx < COLLECTION_CLASSES.length; idx++)
                {
                if (owningType.getQualifiedName().equals(COLLECTION_CLASSES[idx])
                    || owningTypeAncestors.contains(COLLECTION_CLASSES[idx]))
                    {
                    return true;
                }
            }
        }

        return false;
    }

    public StringArray getMethodNamesStartWith(
        TypeDeclaration type,
        String methodNameStartsWith)
    {
        String methName = methodNameStartsWith.toLowerCase();
        StringArray result = new StringArray();

        if ((type == null)
            || (methodNameStartsWith == null)
            || (methodNameStartsWith.length() == 0))
            {
            return result;
        }

        for (MethodIterator iter = type.getMethods().getIterator(); iter.hasNext();)
            {
            MethodDeclaration curMethod = iter.getNext();
            String curName = curMethod.getName();
            // =!= had the method in question to be public?
            if (curMethod.getModifiers().isPublic()
                && curName.toLowerCase().startsWith(methName))
                {
                result.add(curName);
            }
        }

        return result;
    }

    public boolean implementsAddPattern(TypeDeclaration type)
    {
        for (int idx = 0; idx < ADD_METHODS.length; idx++)
            {
            if (!getMethodNamesStartWith(type, ADD_METHODS[idx]).isEmpty())
                {
                return true;
            }
        }

        return false;
    }

    public boolean implementsRemovePattern(TypeDeclaration type)
    {
        for (int idx = 0; idx < REMOVE_METHODS.length; idx++)
            {
            if (!getMethodNamesStartWith(type, REMOVE_METHODS[idx]).isEmpty())
                {
                return true;
            }
        }

        return false;
    }

    public boolean implementsStackPattern(TypeDeclaration type)
    {
        for (int idx = 0; idx < STACK_METHODS.length; idx++)
            {
            if (!getMethodNamesStartWith(type, STACK_METHODS[idx]).isEmpty())
                {
                return true;
            }
        }

        return false;
    }

    public boolean inheritsFromCollection(TypeDeclaration type)
    {
        TypeDeclarationArray ancestors = new InheritanceAnalyzer(type).getAncestors();

        for (int idx = 0; idx < COLLECTION_CLASSES.length; idx++)
            {
            if (ancestors.contains(COLLECTION_CLASSES[idx]))
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
        return new Class[] { TypeDeclaration.class };
    }

    public boolean isKnownCollection(TypeDeclaration type)
    {
        for (int idx = 0; idx < COLLECTION_CLASSES.length; idx++)
            {
            if (type.getQualifiedName().equals(COLLECTION_CLASSES[idx]))
                {
                return true;
            }
        }

        return false;
    }
}
