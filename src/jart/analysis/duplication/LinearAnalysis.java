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
import jart.analysis.duplication.collections.StringPatternIterator;
import jast.ast.nodes.ConstructorDeclaration;
import jast.ast.nodes.InvocableDeclaration;
import jast.ast.nodes.MethodDeclaration;
import jast.ast.visitor.DescendingVisitor;


public class LinearAnalysis extends DescendingVisitor
{
    private final static int MATCH_BYTE_COUNT = 2;

    private int                  _minStringLength;
    private InvocableDeclaration _matchingNode;
    private CandidateArrayTable      _candidates;
    private PatternMatcher          _matcher;

    public LinearAnalysis(InvocableDeclaration node)
        throws IllegalArgumentException
    {
        if ((node == null) || (node.getProperty(LinearView.PROPERTY_LABEL) == null))
            {
            throw new IllegalArgumentException();
        }

        setMatchingNode(node);

        String pattern = (String) _matchingNode.getProperty(LinearView.PROPERTY_LABEL);

        _matcher = new PatternMatcher();
        _matcher.setMatchByteCount(MATCH_BYTE_COUNT);
        _matcher.setPattern(pattern);
    }

    private void analize(InvocableDeclaration node)
    {
        if ((_matchingNode == null) || (_matchingNode.equals(node)))
        {
            return;
        }

        String linearView = (String) node.getProperty(LinearView.PROPERTY_LABEL);
        if (linearView == null) 
        {
			return;
		}
        _matcher.findMatches(linearView, _minStringLength);

        for (StringPatternIterator iter = _matcher.getMatches().getIterator();
            iter.hasNext();
            )
            {
            StringPattern currentMatch = iter.getNext();

            if (!_candidates.contains(currentMatch.getPattern()))
                {
                _candidates.add(
                    new Candidate(
                        currentMatch.getPattern(),
                        _matchingNode,
                        _matcher.getIndexOfPattern(currentMatch.getPattern())));
            }

            _candidates.add(
                new Candidate(
                    currentMatch.getPattern(),
                    node,
                    currentMatch.getStartPosition()));
        }
    }

    public CandidateArrayTable getCandidates()
    {
        return _candidates;
    }

    public int getMinStatementLength()
    {
        return _minStringLength / 2;
    }

    public void setCandidates(CandidateArrayTable table)
    {
        _candidates = table;
    }

    private void setMatchingNode(InvocableDeclaration node)
    {
        _matchingNode = node;
    }

    public void setMinStatementLength(int length)
    {
        _minStringLength = length * 2;
    }

    public void visitConstructorDeclaration(ConstructorDeclaration node)
    {
        analize(node);
    }

    public void visitMethodDeclaration(MethodDeclaration node)
    {
        analize(node);
    }
}