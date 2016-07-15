package io.stiefel.ayms

import liquibase.integration.spring.SpringLiquibase
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

/**
 * Primary application context. Configures JPA, datasources, daos and services.
 *
 * @author jason@stiefel.io
 */
@Configuration
@PropertySources([
        @PropertySource('classpath:io/stiefel/ayms/defaults.properties'),
        @PropertySource(value = 'file:${user.home}/ayms.properties', ignoreResourceNotFound = true),
        @PropertySource(value = 'file://#{systemProperties["ayms.properties"]}', ignoreResourceNotFound = true)
])
@EnableTransactionManagement
@ComponentScan(['io.stiefel.ayms.dao', 'io.stiefel.ayms.service'])
class Context {

    @Autowired Environment env

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(destroyMethod = 'close')
    DataSource dataSource() {
        URI dbUri = new URI(env.getProperty('ayms.jdbc.url'))
        new BasicDataSource(
                driverClassName: env.getProperty('ayms.jdbc.driver'),
                url: "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}",
                username: dbUri.userInfo.split(':')[0],
                password: dbUri.userInfo.split(':')[1]
        )
    }

    @Bean
    SpringLiquibase liquibase() {
        new SpringLiquibase(
                dataSource: dataSource(),
                changeLog: 'classpath:io/stiefel/ayms/db-changelog.xml',
                contexts: env.getProperty('ayms.jdbc.contexts')
        )
    }

    @Bean
    LocalContainerEntityManagerFactoryBean emf() {
        new LocalContainerEntityManagerFactoryBean(
                dataSource: dataSource(),
                packagesToScan: ['io.stiefel.ayms.domain'],
                jpaVendorAdapter: new HibernateJpaVendorAdapter(),
                jpaProperties: [
                        'hibernate.hbm2ddl.auto': env.getProperty('ayms.jdbc.hbm2ddl'),
                        'hibernate.dialect': env.getProperty('ayms.jdbc.dialect'),
                        'hibernate.show_sql': env.getProperty('ayms.jdbc.showSql')
                ]
        )
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        new JpaTransactionManager(
                entityManagerFactory: emf
        )
    }

}
