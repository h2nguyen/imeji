/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.model.SelectItem;

import de.mpg.imeji.presentation.session.SessionBean;

/**
 * Helper to work with vocabularies
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class VocabularyHelper
{
    private List<SelectItem> vocabularies;
    private static Properties properties;

    /**
     * Load the properties and initialize the vocabularies
     */
    public VocabularyHelper()
    {
        try
        {
            loadProperties();
            initVocabularies();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the vocabularies
     */
    public void initVocabularies()
    {
        SessionBean session = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        vocabularies = new ArrayList<SelectItem>();
        vocabularies.add(new SelectItem("", "--"));
        for (Object o : properties.keySet())
        {
            vocabularies.add(new SelectItem(properties.getProperty(o.toString()), session.getLabel("vocabulary_"
                    + o.toString())));
        }
    }

    /**
     * Load the properties form the file vocabularies.properties
     * 
     * @throws IOException
     */
    public void loadProperties() throws IOException
    {
        if (properties == null)
        {
            InputStream instream = null;
            try
            {
                instream = PropertyReader.getInputStream("vocabulary.properties");
                properties = new Properties();
                properties.load(instream);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally
            {
                if (instream != null)
                {
                    instream.close();
                }
            }
        }
    }

    /**
     * Return the name of a vocabulary as defined in the properties
     * 
     * @param uri
     * @return
     */
    public String getVocabularyName(URI uri)
    {
        if (uri == null)
        {
            return null;
        }
        else
        {
            for (SelectItem voc : vocabularies)
            {
                if (voc.getValue().toString().equals(uri.toString()))
                {
                    return voc.getLabel();
                }
            }
        }
        return "unknown";
    }

    public List<SelectItem> getVocabularies()
    {
        return vocabularies;
    }
}
