package io.stiefel.ayms.search

import io.stiefel.ayms.domain.FormResult
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.core.query.SimpleQuery
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import javax.annotation.Resource

/**
 * @author jason@stiefel.io
 */
@Component
@Transactional(readOnly = true)
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
    List<String> find(Map<String,String> terms,
                      Pageable pageable = new PageRequest(0, Integer.MAX_VALUE)) {

        def q = new SimpleQuery(terms.collect { "${it.key}_t:${it.value}" }.join(' '), pageable)
                            .addProjectionOnFields('id')
        solr.queryForPage(q, ResultsDocument).collect { it.id }

    }

}
