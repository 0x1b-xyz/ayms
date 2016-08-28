package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

import javax.persistence.*

/**
 * Represents an instance of a completed form
 *
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_form_result')
@Canonical
class Result {

    @Id
    @JsonView([View.Summary, View.Detail])
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = 'definition_id')
    @JsonView([View.Summary, View.Detail])
    Definition definition

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date created = new Date()

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date updated

    @OneToMany(mappedBy = 'id.result', cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView([View.Detail])
    @Fetch(FetchMode.SELECT)
    List<Data> data

    @PreUpdate
    @PrePersist
    void update() {
        updated = new Date()
    }

}
