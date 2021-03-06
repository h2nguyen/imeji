/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import de.mpg.imeji.presentation.session.SessionBean;
import de.mpg.imeji.presentation.util.BeanHelper;
import de.mpg.imeji.presentation.util.PropertyReader;

/**
 * Java Bean managing language features
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class InternationalizationBean
{
    private List<SelectItem> languages = null;
    List<SelectItem> isolanguages = null;
    private String currentLanguage = "en";
    private SessionBean session = null;
    private List<SelectItem> internationalizedLanguages;
    // The languages supported in imeji (defined in the properties)
    private String[] supportedLanguages;

    /**
     * Constructor
     */
    public InternationalizationBean()
    {
        session = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        init();
        internationalizeLanguages();
    }

    /**
     * Initialize the bean
     */
    private void init()
    {
        try
        {
            Iso639_1Helper iso639_1Helper = new Iso639_1Helper();
            isolanguages = iso639_1Helper.getList();
            readSupportedLanguagesProperty();
            initLanguagesMenu();
            changeLanguage(session.getLocale().getLanguage());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Menu with first, the supported languages out of the properties, second all the iso languages
     * 
     * @param supportedLanguages
     */
    private void initLanguagesMenu()
    {
        // Add first languages out of properties
        languages = new ArrayList<SelectItem>();
        languages.add(new SelectItem("", "--"));
        languages.addAll(getsupportedLanguages(true));
        // add a separator
        languages.add(new SelectItem("", "--"));
        // Add the other languages (non supported)
        languages.addAll(getsupportedLanguages(false));
    }

    /**
     * REad in the imeji.properties which langauges are supported
     * 
     * @return
     */
    private void readSupportedLanguagesProperty()
    {
        try
        {
            supportedLanguages = PropertyReader.getProperty("imeji.i18n.languages").split(",");
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error reading property imeji.i18n.languages. Check Propety file: " + e);
        }
    }

    /**
     * Languages for imeji internationalization
     */
    private void internationalizeLanguages()
    {
        internationalizedLanguages = getsupportedLanguages(true);
    }

    /**
     * True if a language (defined in iso639_1) is supported in imeji (according to the properties)
     * 
     * @param langString
     * @return
     */
    public boolean isSupported(String langString)
    {
        for (int i = 0; i < supportedLanguages.length; i++)
        {
            if (supportedLanguages[i].equals(langString))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Return: <br/>
     * - the supported languages if the parameter is set to true <br/>
     * - the non supported languages if the parameter is set to false
     * 
     * @param supported
     * @return
     */
    private List<SelectItem> getsupportedLanguages(boolean supported)
    {
        List<SelectItem> l = new ArrayList<SelectItem>();
        for (SelectItem iso : isolanguages)
        {
            if (supported && isSupported(iso.getValue().toString()))
            {
                l.add(iso);
            }
        }
        return l;
    }

    /**
     * Change the language of imeji
     * 
     * @param languageString
     */
    private void changeLanguage(String languageString)
    {
        if (isSupported(languageString))
        {
            currentLanguage = languageString;
        }
        else
        {
            currentLanguage = "en";
        }
        session.setLocale(new Locale(currentLanguage));
        internationalizeLanguages();
    }

    /**
     * Listener when the language for imeji is changed
     * 
     * @param event
     */
    public void currentlanguageListener(ValueChangeEvent event)
    {
        if (event != null && !event.getNewValue().toString().equals(event.getOldValue().toString()))
        {
            changeLanguage(event.getNewValue().toString());
        }
    }

    public void setCurrentLanguage(String currentLanguage)
    {
        this.currentLanguage = currentLanguage;
    }

    public String getCurrentLanguage()
    {
        return currentLanguage;
    }

    public List<SelectItem> getLanguages()
    {
        return languages;
    }

    public void setLanguages(List<SelectItem> languages)
    {
        this.languages = languages;
    }

    public List<SelectItem> getInternationalizedLanguages()
    {
        return internationalizedLanguages;
    }

    public void setInternationalizedLanguages(List<SelectItem> internationalizedLanguages)
    {
        this.internationalizedLanguages = internationalizedLanguages;
    }
}
