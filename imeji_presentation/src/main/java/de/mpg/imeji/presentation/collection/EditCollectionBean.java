/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.collection;

import java.util.LinkedList;

import javax.faces.context.FacesContext;

import de.mpg.imeji.logic.controller.CollectionController;
import de.mpg.imeji.logic.util.ObjectHelper;
import de.mpg.imeji.logic.vo.Organization;
import de.mpg.imeji.logic.vo.Person;
import de.mpg.imeji.presentation.beans.Navigation;
import de.mpg.imeji.presentation.session.SessionBean;
import de.mpg.imeji.presentation.util.BeanHelper;
import de.mpg.imeji.presentation.util.UrlHelper;

public class EditCollectionBean extends CollectionBean
{
    private CollectionController collectionController;
    private SessionBean sessionBean;
    private CollectionSessionBean collectionSession = null;
    private boolean init;

    public EditCollectionBean()
    {
        super();
        sessionBean = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        collectionSession = (CollectionSessionBean)BeanHelper.getSessionBean(CollectionSessionBean.class);
        collectionController = new CollectionController(sessionBean.getUser());
        super.setTab(TabType.COLLECTION);
        init = UrlHelper.getParameterBoolean("init");
        super.setCollection(collectionSession.getActive());
    }

    public void init() throws Exception
    {
        if (init)
        {
            String id = super.getId();
            if (id != null)
            {
                ((ViewCollectionBean)BeanHelper.getSessionBean(ViewCollectionBean.class)).setId(id);
                ((ViewCollectionBean)BeanHelper.getSessionBean(ViewCollectionBean.class)).init();
                setProfile(((ViewCollectionBean)BeanHelper.getSessionBean(ViewCollectionBean.class)).getProfile());
                setCollection(((ViewCollectionBean)BeanHelper.getSessionBean(ViewCollectionBean.class)).getCollection());
                LinkedList<Person> persons = new LinkedList<Person>();
                if (getCollection().getMetadata().getPersons().size() == 0)
                {
                    getCollection().getMetadata().getPersons().add(new Person());
                }
                for (Person p : getCollection().getMetadata().getPersons())
                {
                    LinkedList<Organization> orgs = new LinkedList<Organization>();
                    for (Organization o : p.getOrganizations())
                    {
                        orgs.add(o);
                    }
                    p.setOrganizations(orgs);
                    persons.add(p);
                }
                getCollection().getMetadata().setPersons(persons);
                collectionSession.setActive(getCollection());
            }
            else
            {
                BeanHelper.error(sessionBean.getLabel("error") + " : no ID in URL");
            }
            init = false;
        }
    }

    public String save() throws Exception
    {
        if (valid())
        {
            collectionController.updateLazy(getCollection(), sessionBean.getUser());
            BeanHelper.info(sessionBean.getMessage("success_collection_save"));
            Navigation navigation = (Navigation)BeanHelper.getApplicationBean(Navigation.class);
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            navigation.getCollectionUrl() + ObjectHelper.getId(getCollection().getId()) + "/"
                                    + navigation.getInfosPath() + "?init=1");
        }
        return "";
    }

    @Override
    protected String getNavigationString()
    {
        return "pretty:editCollection";
    }
}
