package de.mpg.imeji.presentation.metadata;

import java.net.URI;

import de.mpg.imeji.logic.util.DateFormatter;
import de.mpg.imeji.logic.util.ObjectHelper;
import de.mpg.imeji.logic.vo.Metadata;
import de.mpg.imeji.logic.vo.Person;
import de.mpg.imeji.presentation.metadata.util.MetadataHelper;

/**
 * Bean for all Metadata types. This bean should have all variable that have been defined in all metadata types.
 * 
 * @author saquet
 */
public class SuperMetadataBean
{
    private Metadata metadata;
    private int pos = 0;
    // All possible fields defined for a metadata:
    private String text;
    private Person person;
    private URI coneId;
    private URI uri;
    private String label;
    private String date;
    private long time;
    private double longitude = Double.NaN;
    private double latitude = Double.NaN;
    private String name;
    private String exportFormat;
    private String citation;
    private double number = Double.NaN;
    private String license = null;
    private URI externalUri;

    /**
     * Bean for all Metadata types. This bean should have all variable that have been defined in all metadata types.
     * 
     * @param metadata
     */
    public SuperMetadataBean(Metadata metadata)
    {
        this.metadata = metadata;
        ObjectHelper.copyFields(metadata, this);
    }

    /**
     * Get {@link SuperMetadataBean} as {@link Metadata}
     * 
     * @return
     */
    public Metadata asMetadata()
    {
        ObjectHelper.copyFields(this, metadata);
        MetadataHelper.setConeID(metadata);
        return metadata;
    }

    public URI getStatement()
    {
        return metadata.getStatement();
    }

    public String getTypeNamespace()
    {
        return metadata.getTypeNamespace();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }

    public URI getConeId()
    {
        return coneId;
    }

    public void setConeId(URI coneId)
    {
        this.coneId = coneId;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        if (date != null && !"".equals(date))
        {
            time = DateFormatter.getTime(date);
            this.date = date;
        }
        this.date = date;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExportFormat()
    {
        return exportFormat;
    }

    public void setExportFormat(String exportFormat)
    {
        this.exportFormat = exportFormat;
    }

    public String getCitation()
    {
        return citation;
    }

    public void setCitation(String citation)
    {
        this.citation = citation;
    }

    public double getNumber()
    {
        return number;
    }

    public void setNumber(double number)
    {
        this.number = number;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    /**
     * @return the externalUri
     */
    public URI getExternalUri()
    {
        return externalUri;
    }

    /**
     * @param externalUri the externalUri to set
     */
    public void setExternalUri(URI externalUri)
    {
        this.externalUri = externalUri;
    }
}
