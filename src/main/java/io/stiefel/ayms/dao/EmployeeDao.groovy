package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Employee
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class EmployeeDao extends AbstractDao<Employee, Long> {

    Employee findByCompanyAndId(Company company, Long id) {
        em.createNamedQuery('Employee.findByCompanyAndId', Employee)
                .setParameter('company', company)
                .setParameter('id', id)
                .singleResult
    }

    Employee findByName(String name) {
        em.createNamedQuery('Employee.findByName', Employee).setParameter('name', name).singleResult
    }

    List<Employee> findAllByCompany(Company company) {
        em.createNamedQuery('Employee.findAllByCompany').setParameter('company', company).resultList
    }

}
