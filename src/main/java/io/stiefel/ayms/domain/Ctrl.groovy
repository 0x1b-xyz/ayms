package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
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
@TupleConstructor(excludes = 'name')
@EqualsAndHashCode(includes = 'id')
@ToString(includes = ['id', 'type'])
class Ctrl {

    @EmbeddedId
    @JsonIgnore
    CtrlId id

    @Column(nullable = false, length = 50)
    @JsonView([View.Summary,View.Detail])
    @NotEmpty
    String type;

    @Column(name = 'value', nullable = true)
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @MapKeyColumn(name = "key")
    @CollectionTable(name = "aym_form_ctrl_attr",
            joinColumns = [@JoinColumn(name = "ctrl"), @JoinColumn(name = "definition_id")])
    @JsonView(View.Detail)
    @NotEmpty
    Map<String,String> attr;

    @Column(nullable = false)
    Boolean required = false

    @Column(nullable = false)
    Boolean summary = false

    @Embedded
    @JsonView(View.Detail)
    Layout layout;

    /**
     * Synonym for {@code getId().getName()}
     */
    @Transient
    @JsonView([View.Summary, View.Detail])
    String getName() {
        id?.name
    }

    /**
     * Stuffs the {@param name} into the {@link #getId()}. Will create a new instance
     * if one is not present.
     */
    void setName(String name) {
        if (!id)
            id = new CtrlId()
        id.name = name
    }

}
