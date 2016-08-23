package io.stiefel.ayms

import io.stiefel.ayms.search.ResultsDocument
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.core.query.Criteria
import org.springframework.data.solr.core.query.SimpleQuery
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.annotation.Resource

/**
 * @author jason@stiefel.io
 */
@ContextConfiguration(classes = Context)
@Transactional
@Rollback(false)
class ResultsSearchSpecIT extends Specification {

    @Resource SolrTemplate solr

    def "create and update results"() {

        given:
        def results = [new ResultsDocument('my_instance', 16, [
                'client.address.line1': ['3500 Warp'],
                'zipcode': ['28205']
        ])]
        solr.saveBean(results)
        solr.commit()

        def result = solr.solrClient.getById('my_instance')
        println result

        expect:
        solr.queryForPage(new SimpleQuery(new Criteria('firstName').is('Jason')), ResultsDocument).each {
            println "from firstName: ${it}"
        }
        solr.queryForPage(new SimpleQuery(new Criteria('zipcode_t').startsWith('2820'))
                                .addProjectionOnFields('id','definition'), ResultsDocument).each {
            println "from zipcode: ${it}"
        }
        solr.queryForPage(new SimpleQuery(new Criteria('client.address.line1_t').startsWith('35')), ResultsDocument).each {
            println "from address.line1: ${it}"
        }

    }

}
