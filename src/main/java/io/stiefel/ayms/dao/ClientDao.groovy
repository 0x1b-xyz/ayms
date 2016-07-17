package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class ClientDao extends AbstractDao<Long, Client> {

    Client findByCompanyAndId(Company company, Long id) {
        em.createNamedQuery('Client.findByCompanyAndId', Client)
                .setParameter('company', company)
                .setParameter('id', id)
                .singleResult
    }

    List<Client> findAllByCompany(Company company) {
        em.createNamedQuery('Client.findAllByCompany', Client).setParameter('company', company).resultList
    }

    List<Client> findAllByCompanyAndState(Company company, String state) {
        em.createNamedQuery('Client.findAllByCompanyAndState', Client)
            .setParameter('company', company)
            .setParameter('state', state)
            .resultList
    }

}
