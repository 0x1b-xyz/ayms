package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Service
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class ServiceDao extends AbstractDao<Long, Service> {

    List<Service> findAllByClient(Client client) {
        em.createNamedQuery('Service.findAllByClient', Service)
                .setParameter('client', client)
                .resultList
    }

    Service findByClientAndId(Client client, Long id) {
        em.createNamedQuery('Service.findByClientAndId', Service)
                .setParameter('client', client)
                .setParameter('id', id)
                .singleResult
    }

}
