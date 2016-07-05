package io.stiefel.ayms.web

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.stiefel.ayms.domain.View

/**
 * Outcome of call to a web endpoint
 *
 * @author jason@stiefel.io
 */
@ToString
@EqualsAndHashCode
class Result<T> implements Serializable {

    @JsonView(View.Summary)
    boolean success = true

    @JsonView(View.Summary)
    T data

    @JsonView(View.Summary)
    String message

    Result(T data) {
        this.data = data
    }

    Result(boolean success, T data) {
        this.success = success
        this.data = data
    }

    Result(Throwable t) {
        success = false
        message = t.message
    }

}
