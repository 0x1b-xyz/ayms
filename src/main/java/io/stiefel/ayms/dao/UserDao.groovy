package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.User
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class UserDao extends AbstractDao<User, Long> {

    User findByIdAndCompany(Long id, Company company) {
        em.createNamedQuery('User.findByIdAndCompany', User)
                .setParameter('id', id)
                .setParameter('company', company)
                .singleResult
    }

    User findByName(String name) {
        em.createNamedQuery('User.findByName', User).setParameter('name', name).singleResult
    }

    List<User> findByCompany(Company company) {
        em.createNamedQuery('User.findAllByCompany').setParameter('company', company).resultList
    }

}
