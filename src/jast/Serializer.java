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

import jast.parser.ParserApplicationHelper;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

// Provides the ability to parse types/packages and serialize
// the created AST for later use
public class Serializer implements ProgressObserver
{
    private ParserApplicationHelper _helper = new ParserApplicationHelper("serializer");

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            printUsage();
            return;
        }

        new Serializer().perform(args);
    }

    public void printMessages()
    {
        ((BasicMessageContainer)Global.getOutput()).printAll();
    }

    private static void printUsage()
    {
        System.out.println("Usage:");
        System.out.println("  java "+Serializer.class.getName()+" [options]");
        System.out.println("with the following options:");
        System.out.println("  Input options");
        System.out.println("    -package <pckgName> Parses the given package");
        System.out.println("    -type    <typeName> Parses the given type");
        System.out.println("    -srcdir  <path>     Uses the given path as a search path\n");
        System.out.println("  Processing options");
        System.out.println("    -interface Parses only the interfaces of the types (no bodies)\n");
        System.out.println("  Output options");
        System.out.println("    -output <filename> The serialized AST is stored in the indicated file.\n");
        System.out.println("                       If not given, a file 'ast.ser' is generated per default\n");
        System.out.println("Note that the package/class options are evaluated immediately which");
        System.out.println("means that only the source directories that have been specified before");
        System.out.println("are used.");
    }

    public void serialize(String filename)
    {
        try
        {
            FileOutputStream   fileOutput = new FileOutputStream(filename);
            ObjectOutputStream objOutput  = new ObjectOutputStream(
                                                 new BufferedOutputStream(fileOutput));

            objOutput.writeObject(_helper.getProject());
            objOutput.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void startPackage(String qualifiedName, int numOfTypes)
    {
        System.out.println("Processing package "+qualifiedName);
        System.out.flush();
    }

    public void startType(String qualifiedName)
    {
        System.out.println("Processing type "+qualifiedName);
        System.out.flush();
    }


    public void finishPackage(String qualifiedName)
    {
    }



    public void finishType(String qualifiedName)
    {
    }



    public void perform(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            return;
        }

        String filename = "ast.ser";

        for (int idx = 0; idx < args.length; idx++)
        {
            if ("-output".equalsIgnoreCase(args[idx]))
            {
                filename = args[++idx];
            }
        }
        _helper.addObserver(this);
        _helper.perform(args);
        _helper.remObserver(this);
        printMessages();

        serialize(filename);
    }
}
