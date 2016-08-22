package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.TupleConstructor
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_form_data', uniqueConstraints = [
        @UniqueConstraint(columnNames = ["result_id", "ctrl", "name"])
])
@TupleConstructor
class FormData {

    @EmbeddedId
    @JsonIgnore
    FormDataId id

    @Column(nullable = false)
    @NotEmpty
    @JsonView([View.Summary,View.Detail])
    String name

    @Column(nullable = true)
    @JsonView([View.Summary,View.Detail])
    String value

    /**
     * A synonym for {@code id#ctrl.name}
     */
    @JsonView([View.Summary,View.Detail])
    String getCtrl() {
        id?.ctrl?.name
    }

}
