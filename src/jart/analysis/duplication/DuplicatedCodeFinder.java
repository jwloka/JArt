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
package jart.analysis.duplication;
import jart.analysis.duplication.collections.CandidateArrayTable;
import jast.ast.Project;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.visitor.DescendingVisitor;



public class DuplicatedCodeFinder extends DescendingVisitor
{

    private int                _minStatements   = 6;
    private boolean            _isCreatingViews = false;
    private CandidateArrayTable _candidates;
    private Project            _project;
    private LinearView         _linearView;

    public DuplicatedCodeFinder()
    {
        super();
    }

    private void addLinearView(InvocableDeclaration node)
    {
        if (_linearView == null)
            {
            return;
        }

        _linearView.visitInvocableDeclaration(node);

        // add linear view as property to the node
        node.setProperty(LinearView.PROPERTY_LABEL, _linearView.getView());
    }

    public void addLinearViews(Project prj)
    {
        _project = prj;

        // set view builder mode
        _isCreatingViews = true;

        _linearView = new LinearView();
        visitProject(prj);
    }

    private void analize(InvocableDeclaration node)
    {
        LinearAnalysis analizer = new LinearAnalysis(node);
        analizer.setCandidates(_candidates);
        analizer.setMinStatementLength(_minStatements);

        // look for similars in the whole project
        analizer.visitProject(_project);
    }

    public void analize(Project prj)
    {
        // set analysis mode
        _isCreatingViews = false;
        _candidates = new CandidateArrayTable();

        visitProject(prj);
    }

    public CandidateArrayTable getCandidates()
    {
        return _candidates;
    }

    public int getMinStatements()
    {
        return _minStatements;
    }

    public void setMinStatements(int num)
    {
        _minStatements = num;
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        // 1. step - build linear views
        if (_isCreatingViews)
            {
            addLinearView(node);
        }
        // 2. step - analize linear views
        else
            {
            analize(node);
        }
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        // 1. step - build linear views
        if (_isCreatingViews)
            {
            addLinearView(node);
        }
        // 2. step - analize linear views
        else
            {
            analize(node);
        }
    }
}
