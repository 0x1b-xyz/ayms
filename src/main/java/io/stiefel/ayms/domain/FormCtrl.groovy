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
@NamedQueries([
    @NamedQuery(name = 'FormCtrl.findAllByDefinition', query = 'select ctrl from FormCtrl ctrl where ctrl.definition = :definition'),
])
@Canonical
@EqualsAndHashCode
class FormCtrl {

    @Id
    @JsonView(View.Summary)
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = 'form_definition_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    FormDefinition definition

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