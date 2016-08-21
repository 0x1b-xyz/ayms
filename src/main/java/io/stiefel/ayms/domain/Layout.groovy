package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author jason@stiefel.io
 */
@Embeddable
@Canonical
class Layout {

    @Column(name = 'layout_x')
    @JsonView(View.Detail)
    int x;

    @Column(name = 'layout_y')
    @JsonView(View.Detail)
    int y;

    @Column(name = 'layout_width')
    @JsonView(View.Detail)
    int width;

    @Column(name = 'layout_height')
    @JsonView(View.Detail)
    int height;

}
