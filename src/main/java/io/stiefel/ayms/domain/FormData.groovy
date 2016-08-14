package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
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
        @UniqueConstraint(columnNames = ["result_id", "ctrl_id"])
])
@Canonical
class FormData extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'result_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator, property = 'id')
    @JsonIdentityReference(alwaysAsId = true)
    FormResult result

    @ManyToOne(optional = false)
    @JoinColumn(name = 'ctrl_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    FormCtrl ctrl

    @Column(name = 'value', nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "key")
    @CollectionTable(name = "aym_form_data_field",
            joinColumns = @JoinColumn(name = "data_id"))
    @JsonView(View.Summary)
    @NotEmpty
    Map<String,String> fields;

    @Override
    public String toString() {
        return "FormData{" +
                "ctrl=" + ctrl +
                ", fields=" + fields +
                '}';
    }

}
