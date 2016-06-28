package io.stiefel.ayms.domain

import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_client')
@Canonical(includes = 'id')
@NamedQueries([
        @NamedQuery(name = 'Client.findAllByCompany', query = 'select c from Client c where c.company = :company'),
        @NamedQuery(name = 'Client.findAllByCompanyAndState', query = 'select c from Client c where c.company = :company and c.address.state = :state')
])
class Client {

    @Id
    @GeneratedValue
    Long id

    @ManyToOne(optional = false)
    @JoinColumn(name = 'company_id')
    Company company

    @Column(nullable = false, length = 75)
    String firstName

    @Column(nullable = false, length = 75)
    String lastName

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    Date dateOfBirth

    @Column
    String ssn

    @Embedded
    Address address

}
