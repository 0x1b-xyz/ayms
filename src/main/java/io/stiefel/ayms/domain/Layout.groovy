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
    @JsonView(View.Summary)
    int x;

    @Column(name = 'layout_y')
    @JsonView(View.Summary)
    int y;

    @Column(name = 'layout_width')
    @JsonView(View.Summary)
    int width;

    @Column(name = 'layout_height')
    @JsonView(View.Summary)
    int height;

}
