package io.stiefel.ayms.search

import org.apache.solr.core.CoreContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.repository.config.EnableSolrRepositories
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean

/**
 * @author jason@stiefel.io
 */
@Configuration
@EnableSolrRepositories('io.stiefel.ayms.search')
class EmbeddedContext {

    @Bean
    EmbeddedSolrServerFactoryBean solrServerFactoryBean() {

        EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();
        factory.setSolrHome('classpath:io/stiefel/ayms/search')
        return factory;

    }

    @Bean
    SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }

}
