package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * @author jason@stiefel.io
 */
@MappedSuperclass
@EqualsAndHashCode
abstract class AbstractEntity<K> implements Serializable {

    @Id
    @GeneratedValue
    @JsonView([View.Summary, View.Detail])
    K id;


    @Override
    public String toString() {
        "${getClass().simpleName}{id=${id}}"
    }

}
