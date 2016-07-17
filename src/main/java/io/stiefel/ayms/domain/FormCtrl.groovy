package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = "aym_form_ctrl", uniqueConstraints = [
//        @UniqueConstraint(columnNames = ["form_definition_id", "name"])
])
@Canonical
@EqualsAndHashCode
class FormCtrl extends AbstractEntity<Long> {

    @Column(nullable = false, unique = true)
    @JsonView(View.Summary)
    String guid

    @ManyToOne(optional = false)
    @JoinColumn(name = 'form_definition_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    FormDef definition

    @Column(nullable = false, length = 50)
    @JsonView(View.Summary)
    @NotEmpty
    String type;

    @Column(name = 'value', nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "key")
    @CollectionTable(name = "aym_form_ctrl_attr",
            joinColumns = @JoinColumn(name = "id"))
    @JsonView(View.Summary)
    @NotEmpty
    Map<String,String> attr;

    @Embedded
    @JsonView(View.Summary)
    Layout layout;

}
