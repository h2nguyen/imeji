package de.mpg.imeji.util;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import de.mpg.imeji.beans.SessionBean;
import de.mpg.jena.controller.AlbumController;
import de.mpg.jena.controller.CollectionController;
import de.mpg.jena.controller.ImageController;
import de.mpg.jena.controller.ProfileController;
import de.mpg.jena.vo.Album;
import de.mpg.jena.vo.CollectionImeji;
import de.mpg.jena.vo.Image;
import de.mpg.jena.vo.MetadataProfile;
import de.mpg.jena.vo.Statement;
import de.mpg.jena.vo.User;

public class ObjectLoader 
{

	public static CollectionImeji loadCollection(URI id, User user)
	{
		try 
		{
			CollectionController cl = new CollectionController(user);
			return cl.retrieve(id);
		} 
		catch (thewebsemantic.NotFoundException e) 
		{
			writeErrorNotFound("collection", id);
		}
		catch (Exception e) 
		{
			writeException(e);
		}
		return null;
	}
	
	public static Album loadAlbum(URI id, User user)
	{
        try 
        {
        	AlbumController ac = new AlbumController(user); 
        	return ac.retrieve(id);
		} 
        catch (thewebsemantic.NotFoundException e) 
		{
			writeErrorNotFound("album", id);
		}
        catch (Exception e) 
		{
			writeException(e);
		}
		return null;
	}
	
	public static Image loadImage(URI id, User user)
	{
		try 
        {
    		ImageController ic = new ImageController(user);
         	return ic.retrieve(id);
 		} 
		catch (thewebsemantic.NotFoundException e) 
		{
			writeErrorNotFound("image", id);
		}
	    catch (Exception e) 
	    {
			writeException(e);
		}
		return null;
	}
	
	public static MetadataProfile loadProfile(URI id, User user)
	{
		try 
        {
    		ProfileController pc = new ProfileController(user);
    		MetadataProfile p = pc.retrieve(id);
    		Collections.sort((List<Statement>) p.getStatements());
         	return p;
 		} 
		catch (thewebsemantic.NotFoundException e) 
		{
			writeErrorNotFound("profile", id);
		}
	    catch (Exception e) 
	    {
			writeException(e);
		}
		return null;
	}
		
	private static void writeErrorNotFound(String objectType, URI id)
	{
		BeanHelper.error(((SessionBean)BeanHelper.getSessionBean(SessionBean.class)).getLabel(objectType) + " " + id 
				+ ((SessionBean)BeanHelper.getSessionBean(SessionBean.class)).getLabel("not_found"));
	}
	
	private static void writeException(Exception e)
	{
		BeanHelper.error(((SessionBean)BeanHelper.getSessionBean(SessionBean.class)).getLabel("error") + " " + e);
	}
	
}