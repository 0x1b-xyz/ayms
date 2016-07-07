package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_note')
@Canonical(includes = 'id')
class Note {

    @Id
    @GeneratedValue
    @JsonView(View.Summary)
    Long id

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    Service service

    @ManyToOne(optional = false)
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    User user

    @Column
    @JsonView(View.Summary)
    @NotEmpty
    String text

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date created = new Date()

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView(View.Summary)
    Date modified

    @PrePersist
    private void prePersist() {
        this.modified = new Date()
    }

}
