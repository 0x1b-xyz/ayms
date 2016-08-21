package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = "aym_form_ctrl", uniqueConstraints = [
        @UniqueConstraint(columnNames = ['definition_id', 'name'])
])
@Canonical
@EqualsAndHashCode(includes = 'id')
@ToString(includes = ['id','name','type'])
class FormCtrl {

    @Id
    @JsonView([View.Summary,View.Detail])
    String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = 'definition_id')
    @JsonIgnore
    FormDef definition

    @Column(nullable = false, length = 50)
    @JsonView([View.Summary,View.Detail])
    @NotEmpty
    String name;

    @Column(nullable = false, length = 50)
    @JsonView([View.Summary,View.Detail])
    @NotEmpty
    String type;

    @Column(name = 'value', nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @MapKeyColumn(name = "key")
    @CollectionTable(name = "aym_form_ctrl_attr",
            joinColumns = @JoinColumn(name = "ctrl_id"))
    @JsonView(View.Detail)
    @NotEmpty
    Map<String,String> attr;

    @Embedded
    @JsonView(View.Detail)
    Layout layout;

}
