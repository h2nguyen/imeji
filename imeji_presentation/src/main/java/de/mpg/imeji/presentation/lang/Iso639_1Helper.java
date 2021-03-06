/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.lang;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import de.mpg.imeji.presentation.util.PropertyReader;

public class Iso639_1Helper
{
    private static Logger logger = Logger.getLogger(Iso639_1Helper.class);
    private List<SelectItem> list = null;

    public Iso639_1Helper()
    {
        list = new ArrayList<SelectItem>();
        parseVocabularyString(getVocabularyString());
    }

    private String getVocabularyString()
    {
        try
        {
            HttpClient client = new HttpClient();
            GetMethod getMethod = new GetMethod(PropertyReader.getProperty("escidoc.cone.isos639_1.all")
                    + "?format=options");
            client.executeMethod(getMethod);
            return getMethod.getResponseBodyAsString();
        }
        catch (Exception e)
        {
            logger.error("Couldn't read ISO639_1 vocabulary, will use default one! Error: " + e);
            return "en|en - English\nde|de - German";
        }
    }

    private void parseVocabularyString(String v)
    {
        for (String l : v.split("\n"))
        {
            String[] s = l.split("\\|");
            list.add(new SelectItem(s[0], s[1]));
        }
    }

    public List<SelectItem> getList()
    {
        return list;
    }

    public void setList(List<SelectItem> list)
    {
        this.list = list;
    }
}
