package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.ManyToOne

/**
 * @author jason@stiefel.io
 */
@TupleConstructor
@EqualsAndHashCode
@Embeddable
class FormDataId implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'result_id')
    FormResult result

    @ManyToOne(optional = false)
    @JoinColumns([
            @JoinColumn(name = 'definition_id', referencedColumnName = 'definition_id'),
            @JoinColumn(name = 'ctrl', referencedColumnName = 'name')
    ])
    FormCtrl ctrl

}
