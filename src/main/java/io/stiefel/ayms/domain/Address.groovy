package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author jason@stiefel.io
 */
@Embeddable
@Canonical
class Address {

    @Column(nullable = false, name = 'address_line1')
    @NotEmpty
    @JsonView(View.Summary)
    String line1

    @Column(name = 'address_line2')
    @JsonView(View.Summary)
    String line2

    @Column(nullable = false, name = 'address_city')
    @NotEmpty
    @JsonView(View.Summary)
    String city

    @Column(nullable = false, length = 2, name = 'address_state')
    @NotEmpty
    @JsonView(View.Summary)
    String state

    @Column(nullable = false, length = 5, name = 'address_zipcode')
    @NotEmpty
    @JsonView(View.Summary)
    String zipcode

}
