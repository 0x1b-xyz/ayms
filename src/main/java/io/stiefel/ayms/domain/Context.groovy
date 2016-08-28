package io.stiefel.ayms.domain

import liquibase.integration.spring.SpringLiquibase
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
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
 * @author jason@stiefel.io
 */
@Configuration('domain')
@Import(io.stiefel.ayms.Context)
@ComponentScan(['io.stiefel.ayms.domain','io.stiefel.ayms.repo'])
@EnableJpaRepositories(basePackages = 'io.stiefel.ayms.repo', entityManagerFactoryRef = 'emf')
@EnableTransactionManagement
class Context {

    @Autowired org.springframework.core.env.Environment environment

    @Bean(destroyMethod = 'close')
    DataSource dataSource() {
        URI dbUri = new URI(environment.getRequiredProperty('ayms.jdbc.url'))
        new BasicDataSource(
                driverClassName: environment.getRequiredProperty('ayms.jdbc.driver'),
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
                contexts: environment.getRequiredProperty('ayms.jdbc.contexts')
        )
    }

    @Bean
    LocalContainerEntityManagerFactoryBean emf() {
        new LocalContainerEntityManagerFactoryBean(
                dataSource: dataSource(),
                packagesToScan: ['io.stiefel.ayms.domain'],
                jpaVendorAdapter: new HibernateJpaVendorAdapter(),
                jpaProperties: [
                        'hibernate.hbm2ddl.auto': environment.getRequiredProperty('ayms.jdbc.hbm2ddl'),
                        'hibernate.dialect': environment.getRequiredProperty('ayms.jdbc.dialect'),
                        'hibernate.show_sql': environment.getRequiredProperty('ayms.jdbc.showSql')
                ]
        )
    }

    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        new JpaTransactionManager(
                entityManagerFactory: emf
        )
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        new NamedParameterJdbcTemplate(dataSource)
    }

    @Bean
    TransactionTemplate tx(PlatformTransactionManager transactionManager) {
        new TransactionTemplate(transactionManager)
    }


}
