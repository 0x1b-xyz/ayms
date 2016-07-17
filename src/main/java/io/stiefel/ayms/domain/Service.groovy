package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
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
@Table(name = 'aym_service')
@Canonical
@NamedQueries([
        @NamedQuery(name = 'Service.findAllByClient', query = 'select s from Service s where s.client = :client'),
        @NamedQuery(name = 'Service.findByClientAndId', query = 'select s from Service s where s.client = :client and s.id = :id')
])
class Service extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=false)
    Client client

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=false)
    Employee employee

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date scheduled

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date arrived

}
