package io.stiefel.ayms.web

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.stiefel.ayms.domain.View
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

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
    List<Field> fields

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

    /**
     * Extracts all the {@link Field} errors from the provided {@link BindingResult}.
     */
    Result binding(BindingResult bindingResult) {
        fields = bindingResult.fieldErrors.collect {
            new Field(it.objectName, it.field, it.rejectedValue.toString(), it.code)
        }
        this
    }

}
