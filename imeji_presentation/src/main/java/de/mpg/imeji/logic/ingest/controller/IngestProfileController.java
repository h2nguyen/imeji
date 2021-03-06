package de.mpg.imeji.logic.ingest.controller;

import java.io.File;

import de.mpg.imeji.logic.controller.ProfileController;
import de.mpg.imeji.logic.ingest.parser.ProfileParser;
import de.mpg.imeji.logic.ingest.validator.ProfileValidator;
import de.mpg.imeji.logic.vo.MetadataProfile;
import de.mpg.imeji.logic.vo.User;

/**
 * Controller to ingest a {@link MetadataProfile}
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class IngestProfileController
{
    private User user;

    /**
     * Constructor
     * 
     * @param user
     */
    public IngestProfileController(User user)
    {
        this.user = user;
    }

    /**
     * Ingest a {@link MetadataProfile} as defined in an xml {@link File}
     * 
     * @param profileXmlFile
     * @throws Exception
     */
    public void ingest(File profileXmlFile) throws Exception
    {
        ProfileValidator pv = new ProfileValidator();
        pv.valid(profileXmlFile);
        ProfileParser pp = new ProfileParser();
        MetadataProfile mdp = pp.parse(profileXmlFile);
        ProfileController pc = new ProfileController();
        pc.update(mdp, user);
    }
}
