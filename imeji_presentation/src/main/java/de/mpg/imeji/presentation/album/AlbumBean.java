/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.album;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import de.mpg.imeji.logic.controller.AlbumController;
import de.mpg.imeji.logic.controller.ItemController;
import de.mpg.imeji.logic.controller.UserController;
import de.mpg.imeji.logic.ingest.vo.Items;
import de.mpg.imeji.logic.security.Authorization;
import de.mpg.imeji.logic.security.Operations.OperationsType;
import de.mpg.imeji.logic.security.Security;
import de.mpg.imeji.logic.util.ObjectHelper;
import de.mpg.imeji.logic.vo.Album;
import de.mpg.imeji.logic.vo.Item;
import de.mpg.imeji.logic.vo.Organization;
import de.mpg.imeji.logic.vo.Person;
import de.mpg.imeji.logic.vo.User;
import de.mpg.imeji.presentation.beans.Navigation;
import de.mpg.imeji.presentation.image.ThumbnailBean;
import de.mpg.imeji.presentation.session.SessionBean;
import de.mpg.imeji.presentation.util.BeanHelper;
import de.mpg.imeji.presentation.util.ImejiFactory;
import de.mpg.imeji.presentation.util.ObjectLoader;

/**
 * The javabean for the {@link Album}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class AlbumBean
{
    private SessionBean sessionBean = null;
    private Album album = null;
    private String id = null;
    private int authorPosition;
    private int organizationPosition;
    private List<SelectItem> profilesMenu = new ArrayList<SelectItem>();
    private boolean active;
    /**
     * True if the {@link AlbumBean} is used for the crete page, else false
     */
    private boolean create;
    private boolean selected;
    private static Logger logger = Logger.getLogger(AlbumBean.class);

    /**
     * Construct an {@link AlbumBean} from an {@link Album}
     * 
     * @param album
     */
    public AlbumBean(Album album)
    {
        this.album = album;
        sessionBean = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
        this.id = ObjectHelper.getId(album.getId());
        if (sessionBean.getActiveAlbum() != null && sessionBean.getActiveAlbum().getId().equals(album.getId()))
        {
            active = true;
        }
        AlbumController ac = new AlbumController();
        this.album = (Album)ac.loadContainerItems(album, sessionBean.getUser(), -1, 0);
    }

    /**
     * Construct an emtpy {@link AlbumBean}
     */
    public AlbumBean()
    {
        sessionBean = (SessionBean)BeanHelper.getSessionBean(SessionBean.class);
    }

    /**
     * Load the {@link Album} and its {@link Item} when the {@link AlbumBean} page is called, and initialize it.
     */
    public void initView()
    {
        try
        {
            if (id != null)
            {
                Album a = ObjectLoader.loadAlbumLazy(ObjectHelper.getURI(Album.class, id), sessionBean.getUser());
                ItemController ic = new ItemController(sessionBean.getUser());
                ic.loadContainerItems(a, sessionBean.getUser(), 1, 0);
                setAlbum(a);
                if (album != null)
                {
                    if (sessionBean.getActiveAlbum() != null
                            && sessionBean.getActiveAlbum().getId().equals(album.getId()))
                    {
                        active = true;
                        sessionBean.setActiveAlbum(album);
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Error init album view", e);
        }
    }

    /**
     * Initialize the album form to edit the metadata of the album
     */
    public void initEdit()
    {
        AlbumController ac = new AlbumController();
        try
        {
            setAlbum(ac.retrieveLazy(ObjectHelper.getURI(Album.class, id), sessionBean.getUser()));
            create = false;
            if (sessionBean.getActiveAlbum() != null
                    && sessionBean.getActiveAlbum().getId().toString().equals(album.getId().toString()))
            {
                active = true;
            }
        }
        catch (Exception e)
        {
            BeanHelper.error(e.getMessage());
            logger.error("Error init album edit", e);
        }
    }

    public void initCreate()
    {
        setAlbum(new Album());
        getAlbum().getMetadata().setTitle("");
        getAlbum().getMetadata().setDescription("");
        getAlbum().getMetadata().getPersons().clear();
        getAlbum().getMetadata().getPersons().add(ImejiFactory.newPerson());
        create = true;
    }

    /**
     * Return the link for the Cancel button
     * 
     * @return
     */
    public String getCancel()
    {
        Navigation nav = (Navigation)BeanHelper.getApplicationBean(Navigation.class);
        if (create)
        {
            return nav.getAlbumsUrl();
        }
        return nav.getAlbumUrl() + id + "/" + nav.getInfosPath();
    }

    public boolean valid()
    {
        boolean valid = true;
        boolean hasAuthor = false;
        if (getAlbum().getMetadata().getTitle() == null || "".equals(getAlbum().getMetadata().getTitle()))
        {
            BeanHelper.error(sessionBean.getMessage("collection_create_error_title"));
            valid = false;
        }
        for (Person c : getAlbum().getMetadata().getPersons())
        {
            boolean hasOrganization = false;
            if (!"".equals(c.getFamilyName()))
            {
                hasAuthor = true;
            }
            for (Organization o : c.getOrganizations())
            {
                if (!"".equals(o.getName()) || "".equals(c.getFamilyName()))
                {
                    hasOrganization = true;
                }
                if (hasOrganization && "".equals(c.getFamilyName()))
                {
                    BeanHelper.error(sessionBean.getMessage("error_author_need_one_family_name"));
                    valid = false;
                }
            }
            if (!hasOrganization)
            {
                BeanHelper.error(sessionBean.getMessage("error_author_need_one_organization"));
                valid = false;
            }
        }
        if (!hasAuthor)
        {
            BeanHelper.error(sessionBean.getMessage("error_album_need_one_author"));
            valid = false;
        }
        return valid;
    }

    public String addAuthor()
    {
        List<Person> list = (List<Person>)getAlbum().getMetadata().getPersons();
        list.add(authorPosition + 1, ImejiFactory.newPerson());
        return "";
    }

    public String removeAuthor()
    {
        List<Person> list = (List<Person>)getAlbum().getMetadata().getPersons();
        if (list.size() > 1)
            list.remove(authorPosition);
        else
            BeanHelper.error(sessionBean.getMessage("error_album_need_one_author"));
        return "";
    }

    public String addOrganization()
    {
        Collection<Person> persons = getAlbum().getMetadata().getPersons();
        List<Organization> orgs = (List<Organization>)((List<Person>)persons).get(authorPosition).getOrganizations();
        orgs.add(organizationPosition + 1, ImejiFactory.newOrganization());
        return "";
    }

    public String removeOrganization()
    {
        List<Person> persons = (List<Person>)getAlbum().getMetadata().getPersons();
        List<Organization> orgs = (List<Organization>)persons.get(authorPosition).getOrganizations();
        if (orgs.size() > 1)
            orgs.remove(organizationPosition);
        else
            BeanHelper.error(sessionBean.getMessage("error_author_need_one_organization"));
        return "";
    }

    /**
     * Listener for the discard comment
     * 
     * @param event
     */
    public void discardCommentListener(ValueChangeEvent event)
    {
        album.setDiscardComment(event.getNewValue().toString());
    }

    protected String getNavigationString()
    {
        return "pretty:";
    }

    public int getAuthorPosition()
    {
        return authorPosition;
    }

    public void setAuthorPosition(int pos)
    {
        this.authorPosition = pos;
    }

    /**
     * @return the collectionPosition
     */
    public int getOrganizationPosition()
    {
        return organizationPosition;
    }

    /**
     * @param collectionPosition the collectionPosition to set
     */
    public void setOrganizationPosition(int organizationPosition)
    {
        this.organizationPosition = organizationPosition;
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    public List<SelectItem> getProfilesMenu()
    {
        return profilesMenu;
    }

    public void setProfilesMenu(List<SelectItem> profilesMenu)
    {
        this.profilesMenu = profilesMenu;
    }

    public int getSize()
    {
        return album.getImages().size();
    }

    public boolean getIsOwner()
    {
        if (sessionBean.getUser() != null)
        {
            return getAlbum().getCreatedBy().equals(ObjectHelper.getURI(User.class, sessionBean.getUser().getEmail()));
        }
        else
            return false;
    }

    /**
     * Load the 5 first {@link Items} of the {@link Album} as {@link ThumbnailBean}
     * 
     * @return
     * @throws Exception
     */
    public List<ThumbnailBean> getThumbnails() throws Exception
    {
        ItemController ic = new ItemController(sessionBean.getUser());
        if (album != null)
        {
            List<String> uris = new ArrayList<String>();
            for (URI uri : album.getImages())
            {
                uris.add(uri.toString());
            }
            return ImejiFactory.imageListToThumbList(ic.loadItems(uris, 5, 0));
        }
        return null;
    }

    /**
     * Save (create or update) the {@link Album} in the database
     * 
     * @return
     * @throws Exception
     */
    public String save() throws Exception
    {
        if (create)
        {
            AlbumController ac = new AlbumController();
            if (valid())
            {
                ac.create(getAlbum(), sessionBean.getUser());
                UserController uc = new UserController(sessionBean.getUser());
                sessionBean.setUser(uc.retrieve(sessionBean.getUser().getEmail()));
                BeanHelper.info(sessionBean.getMessage("success_album_create"));
                return "pretty:albums";
            }
        }
        else
        {
            update();
        }
        return "";
    }

    public String update() throws Exception
    {
        AlbumController ac = new AlbumController();
        if (valid())
        {
            ac.updateLazy(album, sessionBean.getUser());
            BeanHelper.info(sessionBean.getMessage("success_album_update"));
            Navigation navigation = (Navigation)BeanHelper.getApplicationBean(Navigation.class);
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            navigation.getAlbumUrl() + ObjectHelper.getId(getAlbum().getId()) + "/"
                                    + navigation.getInfosPath() + "?init=1");
        }
        return "";
    }

    public void setAlbum(Album album)
    {
        this.album = album;
    }

    public Album getAlbum()
    {
        return album;
    }

    public String getPersonString()
    {
        String personString = "";
        for (Person p : album.getMetadata().getPersons())
        {
            if (!"".equals(personString))
            {
                personString += ", ";
            }
            personString += p.getFamilyName() + " " + p.getGivenName();
        }
        return personString;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean getActive()
    {
        return active;
    }

    public String makeActive()
    {
        sessionBean.setActiveAlbum(this.album);
        this.setActive(true);
        return "pretty:";
    }

    public String makeInactive()
    {
        sessionBean.setActiveAlbum(null);
        this.setActive(false);
        return "pretty:";
    }

    public String release()
    {
        AlbumController ac = new AlbumController();
        try
        {
            ac.release(album, sessionBean.getUser());
            makeInactive();
            BeanHelper.info(sessionBean.getMessage("success_album_release"));
        }
        catch (Exception e)
        {
            BeanHelper.error(sessionBean.getMessage("error_album_release"));
            BeanHelper.error(e.getMessage());
            e.printStackTrace();
        }
        return "pretty:";
    }

    /**
     * delete an {@link Album}
     * 
     * @return
     */
    public String delete()
    {
        AlbumController c = new AlbumController();
        try
        {
            makeInactive();
            c.delete(album, sessionBean.getUser());
            BeanHelper.info(sessionBean.getMessage("success_album_delete"));
        }
        catch (Exception e)
        {
            BeanHelper.error(sessionBean.getMessage("error_album_delete"));
            BeanHelper.error(e.getMessage());
            e.printStackTrace();
        }
        return "pretty:albums";
    }

    /**
     * Withdraw an {@link Album}
     * 
     * @return
     * @throws Exception
     */
    public String withdraw() throws Exception
    {
        AlbumController c = new AlbumController();
        try
        {
            c.withdraw(album, sessionBean.getUser());
            BeanHelper.info(sessionBean.getMessage("success_album_withdraw"));
        }
        catch (Exception e)
        {
            BeanHelper.error(sessionBean.getMessage("error_album_withdraw"));
            BeanHelper.error(e.getMessage());
            e.printStackTrace();
        }
        return "pretty:";
    }

    /**
     * True if the {@link Album} is selected in the album list
     * 
     * @return
     */
    public boolean getSelected()
    {
        if (sessionBean.getSelectedAlbums().contains(album.getId()))
            selected = true;
        else
            selected = false;
        return selected;
    }

    public void setSelected(boolean selected)
    {
        if (selected)
        {
            if (!(sessionBean.getSelectedAlbums().contains(album.getId())))
                sessionBean.getSelectedAlbums().add(album.getId());
        }
        else
            sessionBean.getSelectedAlbums().remove(album.getId());
        this.selected = selected;
    }

    public boolean isEditable()
    {
        Security security = new Security();
        return security.check(OperationsType.UPDATE, sessionBean.getUser(), album);
    }

    public boolean isVisible()
    {
        Security security = new Security();
        return security.check(OperationsType.READ, sessionBean.getUser(), album);
    }

    public boolean isDeletable()
    {
        Security security = new Security();
        return security.check(OperationsType.DELETE, sessionBean.getUser(), album);
    }

    public boolean isAdmin()
    {
        Authorization auth = new Authorization();
        return auth.isContainerAdmin(sessionBean.getUser(), album);
    }
}
