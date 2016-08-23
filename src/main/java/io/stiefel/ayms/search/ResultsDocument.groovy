package io.stiefel.ayms.search

import groovy.transform.Canonical
import org.apache.solr.client.solrj.beans.Field
import org.springframework.data.annotation.Id
import org.springframework.data.solr.core.mapping.Dynamic
import org.springframework.data.solr.core.mapping.SolrDocument

/**
 * @author jason@stiefel.io
 */
@SolrDocument
@Canonical
class ResultsDocument {

    @Id
    String id

    @Field
    Long definition

    @Dynamic
    @Field('*_t')
    Map<String,List<String>> data

}
