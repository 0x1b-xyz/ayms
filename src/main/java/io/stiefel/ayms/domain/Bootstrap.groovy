package io.stiefel.ayms.domain

import groovy.util.logging.Log4j
import io.stiefel.ayms.Context
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceUnit

/**
 * Ok this is a teensy bit hacky but it's not a bad way to create and maintain a simple default model
 * in a dev environment. When {@code ayms.env=dev} a simple model will be inserted or updated to
 * provide test data. The state of this can be reset at any time by calling this method on the {@link Context}
 *
 * @todo Needs tx
 *
 * @author jason@stiefel.io
 */
@Transactional
@Component
@Log4j
class Bootstrap implements InitializingBean, Runnable {

    @Autowired Environment environment
    @Autowired TransactionTemplate tx
    @PersistenceUnit EntityManagerFactory emf

    /**
     * Calls {@link #run()} on startup
     */
    @Override
    void afterPropertiesSet() throws Exception {
        run()
    }

    void run(Context.Environment env) {

        if (Context.Environment.dev != env) {
            log.info("Skipping model initialization on env: ${env} ...")
            return;
        }

        log.info("Initializing model on env: ${env} ...")

        tx.execute(new TransactionCallback() {
            @Override
            Object doInTransaction(TransactionStatus status) {

//                EntityManager em = emf.createEntityManager()
//                try {
//                    Company company1 = em.find(Company, 1l) ?: new Company(id: 1l)
//                    [
//                        address: new Address('3453 Card St', null, 'Charlotte', 'NC', '28808'),
//                        name: 'That Company on Card'
//                    ].each { company1[it.key] = it.value }
//                    if (!emf.persistenceUnitUtil.isLoaded(company1)) {
//                        em.
//                    }
//
//                    em.persist(company1)
//
//                } finally {
////                    em?.flush()
//                    em?.close()
//                }

                null

            }
        })


    }

    /**
     * Runs using the {@link Context.Environment} snagged from the {@link Environment}.
     *
     * @see #run(io.stiefel.ayms.Context.Environment)
     */
    @Override
    void run() {
        run(environment.getProperty('ayms.env', Context.Environment))
    }

}
