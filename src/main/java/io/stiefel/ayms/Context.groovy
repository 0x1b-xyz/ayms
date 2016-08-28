package io.stiefel.ayms

import groovy.util.logging.Log4j
import liquibase.integration.spring.SpringLiquibase
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionTemplate

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

/**
 * Primary application context. Configures JPA, datasources, daos and services.
 *
 * @author jason@stiefel.io
 */
@Configuration('root')
@PropertySources([
        @PropertySource('classpath:io/stiefel/ayms/defaults.properties'),
        @PropertySource(value = 'file:${user.home}/ayms.properties', ignoreResourceNotFound = true),
        @PropertySource(value = 'file://#{systemProperties["ayms.properties"]}', ignoreResourceNotFound = true)
])
@Log4j
class Context {

    public static enum Environment {
        dev
    }

    @Autowired org.springframework.core.env.Environment env

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        new PropertySourcesPlaceholderConfigurer();
    }

}
