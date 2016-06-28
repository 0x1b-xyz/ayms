package io.stiefel.ayms.domain

import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_service')
@Canonical(includes = 'id')
class Service {

    @Id
    @GeneratedValue
    Long id

    @ManyToOne(optional = false)
    Company company

    @ManyToOne(optional = false)
    Client client

    @ManyToOne(optional = false)
    User user

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date scheduled

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date arrived

}
