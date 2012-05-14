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
import jast.ast.ASTHelper;
import jast.ast.nodes.ClassDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.nodes.Modifiers;
import jast.ast.nodes.Type;
import jast.ast.nodes.TypeDeclaration;
import jast.ast.nodes.collections.TypeDeclarationArray;
import jast.ast.nodes.collections.TypeDeclarationIterator;
import jast.ast.nodes.collections.TypeIterator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class RedefinitionAnalysis
{
    private MethodDeclaration _method;
    private String            _signature;
    private Hashtable         _rootMethods    = new Hashtable();
    private Hashtable         _alreadyChecked = new Hashtable();
    private Vector            _results        = new Vector();

    private void addMethod(MethodDeclaration method)
    {
        if ((method != null) && (method != _method))
        {
            _results.addElement(method);
        }
    }

    private boolean checkForRootTypes(TypeDeclaration type)
    {
        if (type == null)
        {
            return false;
        }
        if (_rootMethods.containsKey(type))
        {
            return true;
        }

        boolean result = false;

        if (type instanceof ClassDeclaration)
        {
            Type baseType = ((ClassDeclaration)type).getBaseClass();

            if (baseType != null)
            {
                result = result ||
                         checkForRootTypes(baseType.getReferenceBaseTypeDecl());
            }
        }
        for (TypeIterator it = type.getBaseInterfaces().getIterator(); it.hasNext();)
        {
            result = result ||
                     checkForRootTypes(it.getNext().getReferenceBaseTypeDecl());
        }

        MethodDeclaration baseMethod = type.getMethods().get(_signature);

        if (baseMethod != null)
        {
            Modifiers baseMods = baseMethod.getModifiers();

            if (baseMods.isPublic() ||
                baseMods.isProtected() ||
                (baseMods.isFriendly() && ASTHelper.isInSamePackage(baseMethod, _method)))
            {
                if (!result)
                {
                    _rootMethods.put(type, baseMethod);
                }
                return true;
            }
        }
        return result;
    }

    private void determineRedefinitions(TypeDeclaration startType)
    {
        InheritanceAnalyzer  analyzer = new InheritanceAnalyzer(startType);
        TypeDeclarationArray subTypes = analyzer.getChildren(false);
        TypeDeclaration      type;
        MethodDeclaration    method;

        for (TypeDeclarationIterator it = subTypes.getIterator(); it.hasNext(); )
        {
            type = it.getNext();
            if (!_alreadyChecked.containsKey(type))
            {
                addMethod(type.getMethods().get(_signature));
                _alreadyChecked.put(type, type);
            }
        }
    }

    public Vector getRedefinitions(MethodDeclaration method)
    {
        _method    = method;
        _signature = method.getSignature();

        _rootMethods.clear();
        _results.clear();
        _alreadyChecked.clear();

        if (!_method.getModifiers().isStatic())
        {
            TypeDeclaration type = ASTHelper.getTypeDeclarationOf(_method);

            // first we determine all base types that define/declare a
            // method with the same signature
            checkForRootTypes(type);

            for (Enumeration en = _rootMethods.keys(); en.hasMoreElements();)
            {
                type = (TypeDeclaration)en.nextElement();
                addMethod((MethodDeclaration)_rootMethods.get(type));
                determineRedefinitions(type);
            }
        }

        return _results;
    }
}
