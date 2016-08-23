package io.stiefel.ayms

import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormData
import io.stiefel.ayms.domain.FormDataId
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.FormResult
import io.stiefel.ayms.search.ResultSearchRepo
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
    @Resource ResultSearchRepo searcher

//    def "create and update results"() {
//
//        given:
//        [new FormResult(id: 'my_instance', definition: new FormDef(id: 16), data: [
//                new FormData(new FormDataId(new FormCtrl()), 'client.address.line1': ['3500 Warp'],
//                'zipcode': ['28205']
//        ])].each { searcher.save(it) }
//        solr.commit()
//
//        def result = solr.solrClient.getById('my_instance')
//        println result
//
//        expect:
//        solr.queryForPage(new SimpleQuery(new Criteria('firstName').is('Jason')), ResultsDocument).each {
//            println "from firstName: ${it}"
//        }
//        solr.queryForPage(new SimpleQuery(new Criteria('zipcode_t').startsWith('2820'))
//                                .addProjectionOnFields('id','definition'), ResultsDocument).each {
//            println "from zipcode: ${it}"
//        }
//        solr.queryForPage(new SimpleQuery(new Criteria('client.address.line1_t').startsWith('35')), ResultsDocument).each {
//            println "from address.line1: ${it}"
//        }
//        println searcher.find(['client.address.line1': '3500 Warp'])
//
//    }

}
