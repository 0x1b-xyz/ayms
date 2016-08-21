package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapKeyColumn
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_form_data', uniqueConstraints = [
        @UniqueConstraint(columnNames = ["result_id", "ctrl_id", "name"])
])
@TupleConstructor
class FormData extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'result_id')
    @JsonIgnore
    FormResult result

    @ManyToOne(optional = false)
    @JoinColumn(name = 'ctrl_id')
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonView([View.Summary,View.Detail])
    FormCtrl ctrl

    @Column(nullable = false)
    @NotEmpty
    @JsonView([View.Summary,View.Detail])
    String name

    @Column(nullable = true)
    @JsonView([View.Summary,View.Detail])
    String value

}
