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
class Service extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    Client client

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
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
