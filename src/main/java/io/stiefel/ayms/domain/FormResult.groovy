package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Represents an instance of a completed form
 *
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_form_result')
@Canonical
class FormResult {

    @Id
    @JsonView(View.Summary)
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = 'form_definition_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    FormDef definition

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date created = new Date()

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date updated = new Date()

}
