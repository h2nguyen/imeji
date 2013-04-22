/**
 * License: src/main/resources/license/escidoc.license
 */
package de.mpg.imeji.logic.vo.predefinedMetadata;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.mpg.imeji.logic.vo.Metadata;
import de.mpg.j2j.annotations.j2jDataType;
import de.mpg.j2j.annotations.j2jId;
import de.mpg.j2j.annotations.j2jLiteral;
import de.mpg.j2j.annotations.j2jResource;

/**
 * {@link Metadata} for links (URL)
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
@j2jResource("http://imeji.org/terms/metadata")
@j2jDataType("http://imeji.org/terms/metadata#link")
@j2jId(getMethod = "getId", setMethod = "setId")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "link", namespace = "http://imeji.org/terms/metadata#link")
public class Link extends Metadata
{
    @j2jResource("http://imeji.org/terms/uri")
    private URI uri;
    @j2jLiteral("http://www.w3.org/2000/01/rdf-schema-label")
    private String label;
    @j2jResource("http://imeji.org/terms/statement")
    private URI statement;

    public Link()
    {
    }

    @XmlElement(name = "uri", namespace="http://imeji.org/terms/uri")
    public java.net.URI getUri()
    {
        return uri;
    }

    public void setUri(java.net.URI uri)
    {
        this.uri = uri;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
    
    @XmlElement(name = "label", namespace = "http://www.w3.org/2000/01/rdf-schema-label")
    public String getLabel()
    {
        return label;
    }

    @Override
    @XmlElement(name = "statement", namespace="http://imeji.org/terms/statement")
    public URI getStatement()
    {
        return statement;
    }

    @Override
    public void setStatement(URI namespace)
    {
        this.statement = namespace;
    }

    @Override
    public void copy(Metadata metadata)
    {
        if (metadata instanceof Link)
        {
            this.label = ((Link)metadata).getLabel();
            this.uri = ((Link)metadata).getUri();
            this.statement = metadata.getStatement();
        }
    }

    @Override
    public String asFulltext()
    {
        return label + " " + uri.toString();
    }
}
