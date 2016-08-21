package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical

import javax.persistence.*

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
    @JsonView([View.Summary, View.Detail])
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = 'definition_id')
    @JsonView([View.Summary, View.Detail])
    FormDef definition

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date created = new Date()

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date updated

    @OneToMany(mappedBy = 'result', cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView([View.Detail])
    List<FormData> data

    @PreUpdate
    @PrePersist
    void update() {
        10.times { println "!!!! UPDATING FORM RESULT"}
        updated = new Date()
    }

}
