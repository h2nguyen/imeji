/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.presentation.metadata.editors;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.mpg.imeji.logic.util.MetadataFactory;
import de.mpg.imeji.logic.vo.Item;
import de.mpg.imeji.logic.vo.MetadataProfile;
import de.mpg.imeji.logic.vo.Statement;
import de.mpg.imeji.presentation.metadata.EditorItemBean;
import de.mpg.imeji.presentation.metadata.SuperMetadataBean;
import de.mpg.imeji.presentation.metadata.util.MetadataHelper;

/**
 * Editor for one item (by the item detail page)
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class SimpleImageEditor extends MetadataEditor
{
    /**
     * Editor for one item (by the item detail page)
     * 
     * @param items
     * @param profile
     * @param statement
     */
    public SimpleImageEditor(List<Item> items, MetadataProfile profile, Statement statement)
    {
        super(items, profile, statement);
    }

    /**
     * Convenient constructor for one {@link Item}
     * 
     * @param item
     * @param profile
     * @param statement
     */
    public SimpleImageEditor(Item item, MetadataProfile profile, Statement statement)
    {
        super(Arrays.asList(item), profile, statement);
    }

    @Override
    public void initialize()
    {
    }

    @Override
    public boolean prepareUpdate()
    {
        for (EditorItemBean eib : items)
        {
            for (int i = 0; i < eib.getMetadata().size(); i++)
            {
                if (MetadataHelper.isEmpty(eib.getMetadata().get(i).asMetadata()))
                {
                    eib.getMetadata().remove(i);
                }
                else
                {
                    eib.getMetadata().get(i).setPos(i);
                }
            }
        }
        if (items.size() == 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateMetadataofImages()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void addMetadata(int imagePos, int metadataPos)
    {
        if (imagePos < items.size())
        {
            addMetadata(items.get(imagePos), metadataPos);
        }
    }

    @Override
    public void addMetadata(EditorItemBean eib, int metadataPos)
    {
        if (metadataPos <= eib.getMetadata().size())
        {
            eib.getMetadata().add(metadataPos + 1,
                    new SuperMetadataBean(MetadataFactory.createMetadata(getStatement())));
        }
    }

    @Override
    public void removeMetadata(int imagePos, int metadataPos)
    {
        if (imagePos < items.size())
        {
            removeMetadata(items.get(imagePos), metadataPos);
        }
    }

    @Override
    public void removeMetadata(EditorItemBean eib, int metadataPos)
    {
        if (metadataPos < eib.getMetadata().size())
        {
            eib.getMetadata().remove(metadataPos);
        }
    }
}
