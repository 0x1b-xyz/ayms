package io.stiefel.ayms.domain

import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author jason@stiefel.io
 */
@Embeddable
@Canonical
class Address {

    @Column(nullable = false, name = 'address_line1')
    String line1

    @Column(name = 'address_line2')
    String line2

    @Column(name = 'address_line3')
    String line3

    @Column(name = 'address_line4')
    String line4

    @Column(nullable = false, name = 'address_city')
    String city

    @Column(nullable = false, length = 2, name = 'address_state')
    String state

    @Column(nullable = false, length = 5, name = 'address_zipcode')
    String zipcode

}
