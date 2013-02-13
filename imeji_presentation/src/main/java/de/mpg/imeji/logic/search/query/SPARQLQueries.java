/*
 *
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License"). You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or http://www.escidoc.de/license.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */
/*
 * Copyright 2006-2007 Fachinformationszentrum Karlsruhe Gesellschaft
 * für wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Förderung der Wissenschaft e.V.
 * All rights reserved. Use is subject to license terms.
 */
package de.mpg.imeji.logic.search.query;

import de.mpg.imeji.logic.vo.Album;
import de.mpg.imeji.logic.vo.CollectionImeji;
import de.mpg.imeji.logic.vo.Grant;
import de.mpg.imeji.logic.vo.Item;
import de.mpg.imeji.logic.vo.Metadata;
import de.mpg.imeji.logic.vo.MetadataProfile;
import de.mpg.imeji.logic.vo.Statement;
import de.mpg.imeji.logic.vo.User;

/**
 * SPARQL queries for imeji
 * 
 * @author saquet (initial creation)
 * @author $Author$ (last modification)
 * @version $Revision$ $LastChangedDate$
 */
public class SPARQLQueries
{
    /**
     * Select all {@link Metadata} which are restricted, according to their {@link Statement}
     * 
     * @return
     */
    public static String selectMetadataRestricted()
    {
        return "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> SELECT DISTINCT ?s ?sort0 WHERE {  ?it a <http://imeji.org/terms/item>"
                + " . ?it <http://imeji.org/terms/collection> ?sort0"
                + ". ?it <http://imeji.org/terms/metadataSet> ?mds . ?mds <http://imeji.org/terms/metadata> ?s . ?s <http://imeji.org/terms/statement> ?st"
                + " . ?st <http://imeji.org/terms/restricted> ?r  .FILTER(?r='true'^^<http://www.w3.org/2001/XMLSchema#boolean>) }";
    }

    /**
     * Select all {@link User}
     * 
     * @return
     */
    public static String selectUserAll()
    {
        return "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> SELECT DISTINCT ?s WHERE {?s a <http://imeji.org/terms/user> }";
    }

    /**
     * Select all {@link Metadata} which are not related to a statement. Happens when a {@link Statement} is removed
     * from a {@link MetadataProfile}
     * 
     * @return
     */
    public static String selectMetadataUnbounded()
    {
        return "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> SELECT DISTINCT ?s ?sort0 WHERE {?mds <http://imeji.org/terms/metadata> ?s"
                + " . ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?sort0 . ?s <http://imeji.org/terms/statement> ?st"
                + " . not exists{?p a <http://imeji.org/terms/mdprofile> . ?p <http://imeji.org/terms/statement> ?st}}";
    }

    /**
     * Select all {@link Statement} which are not bounded to a {@link MetadataProfile}. Should not happen...
     * 
     * @return
     */
    public static String selectStatementUnbounded()
    {
        return "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> SELECT DISTINCT ?s WHERE {?s <http://purl.org/dc/terms/type> ?type"
                + " . not exists{ ?p a <http://imeji.org/terms/mdprofile> . ?p <http://imeji.org/terms/statement> ?s}}";
    }

    /**
     * Select all {@link Grant} which are not valid anymore. For instance, when a {@link User}, or an object (
     * {@link CollectionImeji}, {@link Album}) is deleted, some related {@link Grant} might stay in the database, even
     * if they are not needed anymore.
     * 
     * @return
     */
    public static String selectGrantUnbounded()
    {
        return "PREFIX fn: <http://www.w3.org/2005/xpath-functions#> SELECT DISTINCT ?s WHERE { ?s <http://imeji.org/terms/grantType> ?type"
                + " . not exists{ ?user <http://imeji.org/terms/grant> ?s}}";
    }

    /**
     * Select all {@link CollectionImeji} available imeji
     * 
     * @return
     */
    public static String selectCollectionAll()
    {
        return "SELECT ?s WHERE { ?s a <http://imeji.org/terms/collection>}";
    }

    /**
     * Select all {@link Album} available imeji
     * 
     * @return
     */
    public static String selectAlbumAll()
    {
        return "SELECT ?s WHERE { ?s a <http://imeji.org/terms/collection>}";
    }

    /**
     * Select all {@link Item} available imeji
     * 
     * @return
     */
    public static String selectItemAll()
    {
        return "SELECT ?s WHERE { ?s a <http://imeji.org/terms/item>}";
    }
}
