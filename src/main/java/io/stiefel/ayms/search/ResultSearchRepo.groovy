package io.stiefel.ayms.search

import groovy.util.logging.Log4j
import io.stiefel.ayms.domain.FormResult
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.core.query.Criteria
import org.springframework.data.solr.core.query.SimpleQuery
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

/**
 * @author jason@stiefel.io
 */
@Component
@Transactional(readOnly = true)
@Log4j
class ResultSearchRepo {

    @Resource SolrTemplate solr

    /**
     * Saves or updates the {@link FormResult} into solr
     */
    @Transactional
    void save(FormResult result) {
        ResultsDocument doc = new ResultsDocument(result.id, result.definition.id,
                result.data.collectEntries { [[it.ctrl, it.name].join('.'), it.value] })
        solr.saveBean(doc)
        solr.commit()
    }

    /**
     * Deletes the index for the {@link FormResult}
     */
    @Transactional
    void delete(String resultId) {
        solr.deleteById(resultId)
        solr.commit()
    }

    /**
     * Runs a simple search for {@code field = value}
     */
    List<String> find(Long definitionId,
                        Map<String,String> terms,
                      Pageable pageable = new PageRequest(0, Integer.MAX_VALUE)) {

        Criteria c = new Criteria("definition").is(definitionId.toString())
        terms.each { String field, String }

        String query = "definition:${definitionId} ${terms.collect { "${it.key}_t:${it.value}" }.join(' ')}"
        log.info("Searching with query: ${query}")
        def q = new SimpleQuery(query, pageable).addProjectionOnFields('id')
        solr.queryForPage(q, ResultsDocument).collect { it.id }

    }

}
