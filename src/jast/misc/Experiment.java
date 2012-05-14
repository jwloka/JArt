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
package jast.misc;

import jast.BasicMessageContainer;
import jast.FileParser;
import jast.Global;
import jast.ast.Project;
import jast.resolver.Resolver;
import jast.ui.NodeTree;

import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class Experiment
{
    private class Test
    {
        public class Deep {}
    }

    private String text = \u0022abc";

    private static String decode(String rep)
    {
        if (rep == null)
        {
            return "";
        }
        char[] result = new char[rep.length()];
        int count = 0;
        int idx = 0;
        while (idx < result.length)
        {
            if ((rep.charAt(idx) == '\\') && (idx < result.length - 1))
            {
                idx++;
                char start = rep.charAt(idx);
                char real = ' ';
                int value;
                switch (start)
                {
                    case 'b' :
                        real = '\b';
                        break;
                    case 't' :
                        real = '\t';
                        break;
                    case 'n' :
                        real = '\n';
                        break;
                    case 'f' :
                        real = '\f';
                        break;
                    case 'r' :
                        real = '\r';
                        break;
                    case '"' :
                        real = '"';
                        break;
                    case '\'' :
                        real = '\'';
                        break;
                    case '\\' :
                        real = '\\';
                        break;
                    case '0' :
                    case '1' :
                    case '2' :
                    case '3' :
                    case '4' :
                    case '5' :
                    case '6' :
                    case '7' :
                        if ((start <= '3') && (idx < result.length - 2) && (rep.charAt(idx + 1) >= '0') && (rep.charAt(idx + 1) <= '7') && (rep.charAt(idx + 2) >= '0') && (rep.charAt(idx + 2) <= '7'))
                        {
                            value = 64 * Character.digit(start, 10) + 8 * Character.digit(rep.charAt(idx + 1), 10) + Character.digit(rep.charAt(idx + 2), 10);
                            idx += 2;
                        }
                        else
                            if ((idx < result.length - 1) && (rep.charAt(idx + 1) >= '0') && (rep.charAt(idx + 1) <= '7'))
                            {
                                value = 8 * Character.digit(start, 10) + Character.digit(rep.charAt(idx + 1), 10);
                                idx++;
                            }
                            else
                            {
                                value = Character.digit(start, 10);
                            }
                        real = (char) value;
                        break;
                    case 'u' :
                        if (idx >= result.length - 4)
                        {
                            throw new RuntimeException();
                        }
                        value = 4096 * Character.digit(rep.charAt(idx + 1), 16) + 256 * Character.digit(rep.charAt(idx + 2), 16) + 16 * Character.digit(rep.charAt(idx + 3), 16) + Character.digit(rep.charAt(idx + 4), 16);
                        real = (char) value;
                        idx += 4;
                        break;
                    default :
                        throw new RuntimeException();
                }
                result[count] = real;
                count++;
            }
            else
            {
                result[count] = rep.charAt(idx);
                count++;
            }
            idx++;
        }
        if (count < result.length)
        {
            char[] realResult = new char[count];
            System.arraycopy(result, 0, realResult, 0, count);
            result = realResult;
        }
        return new String(result);
    }

        public static void main(String[] args) throws Exception
        {
            Project    project   = new Project("test");
            FileParser parser    = new FileParser();
            String     pckgName  = "java.lang";
            String     typeName  = "String";
            boolean    doResolve = false;
            JFrame     mainWin   = new JFrame("Project");
            NodeTree   tree      = new NodeTree(project);

            tree.putClientProperty("JTree.lineStyle", "Angled");

            mainWin.getContentPane().add(new JScrollPane(tree));
            mainWin.setSize(400, 600);
            mainWin.setVisible(true);

            parser.addSearchDirectory("\\Development\\jdk1.3.1\\src");
    /*
            parser.addObserver(new ProgressObserver()
                {
                    public void finishPackage(String qualifiedName)
                    {
                        System.out.println("Package "+qualifiedName+" finished.");
                        System.out.flush();
                    }

                    public void finishType(String qualifiedName)
                    {
                        System.out.println("Type "+qualifiedName+" finished.");
                        System.out.flush();
                    }

                    public void startPackage(String qualifiedName, int numOfTypes)
                    {
                        System.out.println("Starting package "+qualifiedName);
                        System.out.flush();
                    }

                    public void startType(String qualifiedName)
                    {
                        System.out.println("Starting type "+qualifiedName);
                        System.out.flush();
                    }
                });
    */
            parser.setParsingInterface(false);

            System.out.println("Parsing "+pckgName+"."+typeName);
            System.out.flush();

            if (parser.parseType(project, pckgName+"."+typeName))
            {
                if (doResolve)
                {
                    Resolver resolver = new Resolver(parser);

                    System.out.println("\nResolving "+pckgName+"."+typeName);
                    System.out.flush();
                    resolver.resolve(project);
                }

                ((BasicMessageContainer)Global.getOutput()).printAll();
            }
            tree.refresh();
        }

    public static double parseNumber(String literal)
    {
        try
        {
            String number = literal.toLowerCase();
            boolean isFloat = number.endsWith("f");
            if (number.endsWith("f") || number.endsWith("d"))
            {
                number = number.substring(0, number.length() - 1);
            }
            if (isFloat)
            {
                return Float.parseFloat(number);
            }
            else
            {
                return Double.parseDouble(number);
            }
        }
        catch (NumberFormatException ex)
        {
            throw new IllegalArgumentException();
        }
    }
}
