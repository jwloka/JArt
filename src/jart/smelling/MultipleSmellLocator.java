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
package jart.smelling;
import jart.analysis.AnalysisFunction;
import jast.ast.Node;
import jast.ast.Project;
import jast.ast.nodes.CompilationUnit;
import jast.ast.visitor.DescendingReflectionVisitor;

import java.util.Hashtable;
import java.util.Vector;

// Applies multiple analysis function on the AST
// and creates occurrences if necessary
public class MultipleSmellLocator extends    DescendingReflectionVisitor
                                  implements SmellLocator
{
    private Hashtable _functions   = new Hashtable();
    private Vector    _occurrences = new Vector();
    private Project   _project;

    public void addAnalysisFunction(AnalysisFunction function)
    {
        Class[] types = function.isApplicableTo();
        Vector functions;

        for (int idx = 0; idx < types.length; idx++)
            {
            functions = (Vector) _functions.get(types[idx]);
            if (functions == null)
                {
                functions = new Vector();
                _functions.put(types[idx], functions);
            }
            functions.addElement(function);
        }
    }



    public void applyTo(Project project)
    {
        _project = project;
        visit(_project);
    }



    public SmellOccurrenceIterator getOccurrences()
    {
        return new SmellOccurrenceIterator()
        {
            private int _curIdx = 0;

            public boolean hasNext()
            {
                return (_curIdx < _occurrences.size());
            }

            public SmellOccurrence getNext()
            {
                return (SmellOccurrence) _occurrences.elementAt(_curIdx++);
            }
        };
    }



    private void invokeAnalysisFunction(AnalysisFunction function, Object obj)
    {
        if ((obj == null) || !(obj instanceof Node))
            {
            return;
        }

        Node node = (Node) obj;

        if (function.check(_project, node))
            {
            _occurrences.addElement(
                new SmellOccurrence(function.getClass(), node, node.getStartPosition()));
        }
    }



    public void preVisit(Object obj)
    {
        Class  cls = obj.getClass();
        Vector handler;

        if (obj instanceof CompilationUnit)
        {
            // we only smell project units
            if (!((CompilationUnit)obj).isProjectUnit())
            {
            }
        }
        while (cls != null)
        {
            handler = (Vector) _functions.get(cls);
            if (handler != null)
            {
                for (int idx = 0; idx < handler.size(); idx++)
                {
                    invokeAnalysisFunction((AnalysisFunction) handler.elementAt(idx), obj);
                }
            }
            cls = cls.getSuperclass();
        }
    }



    public void reset()
    {
        _functions.clear();
        _occurrences.clear();
    }



    public void visitCompilationUnit(CompilationUnit node)
    {
        // we only smell project units
        if (node.isProjectUnit())
        {
            super.visitCompilationUnit(node);
        }
    }
}