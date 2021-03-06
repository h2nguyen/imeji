/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.util;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mpg.imeji.logic.vo.Item;
import de.mpg.imeji.logic.vo.MetadataProfile;

/**
 * Helper methods related to {@link MetadataProfile}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class ProfileHelper
{
    /**
     * Load the all th {@link MetadataProfile} found in a {@link List} of {@link Item}
     * 
     * @param imgs
     * @return
     * @throws Exception
     */
    public static Map<URI, MetadataProfile> loadProfiles(List<Item> imgs) throws Exception
    {
        Map<URI, MetadataProfile> pMap = new HashMap<URI, MetadataProfile>();
        for (Item im : imgs)
        {
            if (pMap.get(im.getMetadataSet().getProfile()) == null)
            {
                pMap.put(im.getMetadataSet().getProfile(),
                        ObjectCachedLoader.loadProfile(im.getMetadataSet().getProfile()));
            }
        }
        return pMap;
    }
}
