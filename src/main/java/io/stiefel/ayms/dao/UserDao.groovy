package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.User
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class UserDao extends AbstractDao<User, Long> {

    User findByCompanyAndId(Company company, Long id) {
        em.createNamedQuery('User.findByCompanyAndId', User)
                .setParameter('company', company)
                .setParameter('id', id)
                .singleResult
    }

    User findByName(String name) {
        em.createNamedQuery('User.findByName', User).setParameter('name', name).singleResult
    }

    List<User> findAllByCompany(Company company) {
        em.createNamedQuery('User.findAllByCompany').setParameter('company', company).resultList
    }

}
