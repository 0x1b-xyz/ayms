package io.stiefel.ayms.search

import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.data.solr.repository.config.EnableSolrRepositories

import javax.annotation.Resource

/**
 * @author jason@stiefel.io
 */
@Configuration('search')
@Import(io.stiefel.ayms.Context)
@ComponentScan('io.stiefel.ayms.search')
@EnableSolrRepositories('io.stiefel.ayms.search')
class Context {

    @Resource Environment environment

    @Bean
    SolrClient solrClient() {
        new HttpSolrClient(environment.getRequiredProperty('ayms.solr.url'))
    }

    @Bean
    SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrClient());
    }

}
