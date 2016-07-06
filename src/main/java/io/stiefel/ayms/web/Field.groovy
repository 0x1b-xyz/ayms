package io.stiefel.ayms.web

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import io.stiefel.ayms.domain.View

/**
 * Represents an error processing a form field
 *
 * @author jason@stiefel.io
 */
@Canonical
class Field {

    @JsonView(View.Summary)
    String objectName

    @JsonView(View.Summary)
    String name

    @JsonView(View.Summary)
    String rejectedValue

    @JsonView(View.Summary)
    String code

}
