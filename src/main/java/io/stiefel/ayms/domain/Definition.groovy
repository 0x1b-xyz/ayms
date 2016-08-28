package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_form_definition')
@Canonical
class Definition extends AbstractEntity<Long> {

    @Column(unique = true, nullable = false, length = 100)
    @JsonView([View.Summary, View.Detail])
    @NotEmpty
    String name;

    @Column
    @JsonView([View.Summary, View.Detail])
    @NotEmpty
    String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date created = new Date()

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView([View.Summary, View.Detail])
    Date updated

    @OneToMany(mappedBy = 'id.definition', cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JsonView([View.Detail])
    List<Ctrl> ctrls;

    @PreUpdate
    @PrePersist
    void update() {
        updated = new Date()
    }

}
