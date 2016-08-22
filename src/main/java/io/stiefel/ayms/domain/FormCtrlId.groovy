package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

/**
 * @author jason@stiefel.io
 */
@EqualsAndHashCode
@Embeddable
@TupleConstructor
class FormCtrlId implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'definition_id')
    FormDef definition

    @Column(name = 'name', nullable = false, length = 50)
    @NotEmpty
    String name;

}
