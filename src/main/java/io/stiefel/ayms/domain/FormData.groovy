package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapKeyColumn

/**
 * @author jason@stiefel.io
 */
class FormData {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'form_ctrl_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    FormCtrl ctrl

    @Column(name = 'value', nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "key")
    @CollectionTable(name = "aym_form_data_attr",
            joinColumns = @JoinColumn(name = "id"))
    @JsonView(View.Summary)
    @NotEmpty
    Map<String,String> attr;

}
