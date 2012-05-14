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
package jast.export.xml;

import jast.ast.nodes.Package;
import jast.ast.nodes.Type;
import jast.ast.nodes.collections.PackageIterator;
import jast.helpers.StringArray;
import jast.parser.ParserApplicationHelper;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jato.Interpreter;
import org.jato.JatoException;
import org.jato.JatoFunction;
import org.jato.JatoIntegrator;
import org.jato.transform.JatoSource;
import org.jato.transform.JatoTransformerFactory;
import org.jdom.Element;

public class ExportToXML implements JatoIntegrator
{
    private ParserApplicationHelper _helper = new ParserApplicationHelper("ExportToXML");
    private Hashtable               _ids    = new Hashtable();
    private Hashtable               _objs   = new Hashtable();
    private long                    _curId  = 0L;

    private void addPackageToHierarchyList(Hashtable pckgs, String pckgName)
    {
        StringArray       pckgNames  = StringArray.fromString(pckgName, ".");
        PackageDescriptor curSubPckg = null;
        String            curSubPckgName;
        String            curPckgName;
        Package           pckg;

        for (int idx = 0; idx < pckgNames.getCount(); idx++)
        {
            curSubPckgName = pckgNames.get(idx);
            curPckgName    = pckgNames.asString(".", idx+1);
            pckg           = _helper.getProject().getPackage(curPckgName);
            if (idx == 0)
            {
                // top-level package
                curSubPckg = (PackageDescriptor)pckgs.get(curSubPckgName);
                if (curSubPckg == null)
                {
                    if (pckg == null)
                    {
                        curSubPckg = new PackageDescriptor(curPckgName);
                    }
                    else
                    {
                        curSubPckg = new PackageDescriptor(pckg);
                    }
                    pckgs.put(curSubPckgName, curSubPckg);
                }
            }
            else
            {
                // sub package
                if (!curSubPckg.hasSubPackage(curSubPckgName))
                {
                    if (pckg == null)
                    {
                        curSubPckg.addSubPackage(curPckgName);
                    }
                    else
                    {
                        curSubPckg.addSubPackage(pckg);
                    }
                }
                curSubPckg = curSubPckg.getSubPackage(curSubPckgName);
            }
        }
    }

    public String createId()
    {
        return String.valueOf(_curId++);
    }

    public String getIdOf(Object target)
    {
        Object obj = target;

        if (obj instanceof Type)
        {
            Type type = (Type)obj;

            if (type.hasReferenceBaseTypeDecl())
            {
                obj = type.getReferenceBaseTypeDecl();
            }
            else
            {
                obj = type.getBaseType().toString();
            }
        }

        String id = (String)_ids.get(obj);

        if (id == null)
        {
            id = createId();
            _ids.put(obj, id);
        }
        return id;
    }

    public Object getObject(String key, Properties parms) throws JatoException
    {
        if (key.equalsIgnoreCase("project"))
        {
            return _helper.getProject();
        }
        else if (key.equalsIgnoreCase("packages"))
        {
            return getPackageHierarchy();
        }
        else
        {
            return _objs.get(key);
        }
    }

    private Hashtable getPackageHierarchy()
    {
        Hashtable result = new Hashtable();

        for (PackageIterator it = _helper.getProject().getPackages().getIterator(); it.hasNext();)
        {
            addPackageToHierarchyList(result, it.getNext().getQualifiedName());
        }
        return result;
    }

    public static void main(String[] args)
    {
        new ExportToXML().perform(args);
    }

    public void perform(String[] args)
    {
        if (args.length == 0)
        {
            return;
        }

        OutputStream output     = System.out;
        String       scriptName = "Ast2Xmi.xml";

        for (int idx = 0; idx < args.length; idx++)
        {
            if ("-output".equalsIgnoreCase(args[idx]))
            {
                try
                {
                    output = new FileOutputStream(args[++idx]);
                }
                catch (Exception ex)
                {
                	ex.printStackTrace();
                	output = System.out;
                }
            }
            else if ("-script".equalsIgnoreCase(args[idx]))
            {
                scriptName = args[++idx];
            }
        }

        _ids.clear();
        _curId = 0L;
        try
        {
            _helper.perform(args);

            Transformer jato = new JatoTransformerFactory().newTransformer(
                                        new StreamSource(new FileReader(scriptName)));

            jato.setParameter("{http://jato.sourceforge.net}integrator", this);
            jato.setOutputProperty("{http://jato.sourceforge.net}indent", "  ");
            registerFunctions(jato);

            Map objects = new HashMap();

            objects.put("project", _helper.getProject());

            jato.transform(new JatoSource(objects),
                           new StreamResult(output));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void publish(String key, Object obj, int state) throws JatoException
    {
        _objs.put(key, obj);
    }

    public void registerFunctions(Transformer jato)
    {
        jato.setParameter(
            "{http://jato.sourceforge.net}functions",
            new JatoFunction()
            {
                public String getName()
                {
                    return "idOf";
                }

                public Object invoke(Properties  params,
                                     Interpreter jato,
                                     Object      thisObj,
                                     Element     xmlIn,
                                     Element     xmlOut) throws JatoException
                {
                    return getIdOf(thisObj);
                }
            });
        jato.setParameter(
            "{http://jato.sourceforge.net}functions",
            new JatoFunction()
            {
                public String getName()
                {
                    return "createId";
                }

                public Object invoke(Properties  params,
                                     Interpreter jato,
                                     Object      thisObj,
                                     Element     xmlIn,
                                     Element     xmlOut) throws JatoException
                {
                    return createId();
                }
            });
        jato.setParameter(
            "{http://jato.sourceforge.net}functions",
            new JatoFunction()
            {
                public String getName()
                {
                    return "storeInList";
                }

                public Object invoke(Properties  params,
                                     Interpreter jato,
                                     Object      thisObj,
                                     Element     xmlIn,
                                     Element     xmlOut) throws JatoException
                {
                    storeInList(params.getProperty("list"), thisObj);

                    return thisObj;
                }
            });
        jato.setParameter(
            "{http://jato.sourceforge.net}functions",
            new JatoFunction()
            {
                public String getName()
                {
                    return "idOfAndStoreInMap";
                }

                public Object invoke(Properties  params,
                                     Interpreter jato,
                                     Object      thisObj,
                                     Element     xmlIn,
                                     Element     xmlOut) throws JatoException
                {
                    String id = getIdOf(thisObj);

                    storeInMap(params.getProperty("map"), id, thisObj);

                    return id;
                }
            });
    }

    private void storeInList(String listName, Object obj)
    {
        Vector list = (Vector)_objs.get(listName);

        if (list == null)
        {
            list = new Vector();
            _objs.put(listName, list);
        }
        if (!list.contains(obj))
        {
            list.addElement(obj);
        }
    }

    private void storeInMap(String mapName, String key, Object obj)
    {
        Hashtable map = (Hashtable)_objs.get(mapName);

        if (map == null)
        {
            map = new Hashtable();
            _objs.put(mapName, map);
        }
        map.put(key, obj);
    }
}
