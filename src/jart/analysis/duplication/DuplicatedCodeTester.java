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
import jart.analysis.duplication.collections.CandidateArray;
import jart.analysis.duplication.collections.CandidateArrayIterator;
import jart.analysis.duplication.collections.CandidateArrayTable;
import jart.analysis.duplication.collections.CandidateIterator;
import jast.FileParser;
import jast.ast.Project;
import jast.ast.nodes.ClassDeclaration;
import jast.prettyprinter.PrettyPrinter;

import java.io.File;



public class DuplicatedCodeTester
{

    public DuplicatedCodeTester()
    {
        super();
    }

    public static void main(String[] args)
    {
        if (args.length != 1)
            {
            System.out.println(
                "usage: java jart.analysis.DuplicatedCodeTester PATH or [PATH]/FILENAME.java");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists())
            {
            System.out.println("This file doesn't exist: \"" + args[0] + "\"");
            return;
        }

        Project prj = new Project("DupFinder");
        FileParser psr = new FileParser();

        try
            {
            if (file.isFile())
                {
                psr.parseFile(prj, args[0]);
            }
            else if (file.isDirectory())
                {
                psr.parsePackage(prj, args[0], false);
            }
        }
        catch (Exception ex)
            {
            ex.printStackTrace(System.out);
        }

        DuplicatedCodeFinder finder = new DuplicatedCodeFinder();
        finder.setMinStatements(10);
        finder.addLinearViews(prj);
        finder.analize(prj);

        showResult(finder.getCandidates());
    }

    public static void showResult(CandidateArrayTable table)
    {
        PrettyPrinter printer = new PrettyPrinter();

        System.out.println("PROJECT SUMMARY:");
        System.out.println(
            "duplications: " + table.getCount() + " different matches found");
        System.out.println();

        for (CandidateArrayIterator iter = table.getIterator(); iter.hasNext();)
            {
            CandidateArray candArray = iter.getNext();
            System.out.println("Candidates for Match: " + candArray.get(0).getMatch());
            System.out.println("Was " + candArray.getCount() + " times found!");
            System.out.println("-----------------------------------------------------");
            System.out.println();

            for (CandidateIterator candIter = candArray.getIterator(); candIter.hasNext();)
                {
                Candidate currentCand = candIter.getNext();
                String className =
                    ((ClassDeclaration) currentCand.getNode().getContainer()).getName();
                String methodName = currentCand.getNode().getSignature();
                int startPos = currentCand.getNode().getStartPosition().getAbsolute();
                int endPos = currentCand.getNode().getFinishPosition().getAbsolute();

                System.out.println("Name        : " + className + "::" + methodName);
                System.out.println(
                    "LinearView  : "
                        + currentCand.getNode().getProperty(LinearView.PROPERTY_LABEL));
                System.out.println("Match       : " + currentCand.getMatch());
                System.out.println("Startzeichen: " + startPos);
                System.out.println("Endzeichen  : " + endPos);
                System.out.println();

                currentCand.getNode().accept(printer);
                System.out.println();
                System.out.println();
            }
        }

        //+ Klassen-/Methodenname
        //+ ParsePosition in Quelle von/bis
        //+ Welche Statements sind doppelt
        //+ Source der Doppelten Statements mit PrettyPrinter
    }
}
