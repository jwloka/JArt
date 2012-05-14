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
package jart.restructuring;
import jast.ast.Node;

public abstract class Restructuring
{
    protected RestructuringResults _results = new RestructuringResults();

    /**
     * Determines whether the restructuring is applicable.
     *
     * @return <code>false</code> if the restructuring is not applicable
     */
    public abstract boolean analyze();

    public RestructuringResults getResultContainer()
    {
        return _results;
    }

    /**
     * Initializes the restructuring. The required number of nodes
     * and their types as well as the structure of the processing data
     * are dependent on the actual restructuring
     *
     * @param  nodes          The input nodes
     * @param  processingData Data required to perform the restructuring
     *                        (such as a new name)
     * @return <code>false</code> if an error occured during initialization
     *         (iow the input was not ok)
     */
    public abstract boolean initialize(Node[] nodes, Object[] processingData);

    /**
     * Performs the restructuring. Note that it does not check whether
     * the restructuring is applicable. Therefore it should only be
     * performed if <code>analyze</code> returned <code>true</code>.
     *
     * @return <code>false</code> if the restructuring couldn't be
     *         performed
     */
    public abstract boolean perform();

    public void setResultContainer(RestructuringResults results)
    {
        _results = results;
    }
}
