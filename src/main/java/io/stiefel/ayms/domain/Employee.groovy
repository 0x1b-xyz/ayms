package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.voodoodyne.jackson.jsog.JSOGGenerator
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_employee')
@Canonical
class Employee extends AbstractEntity<Long> {

    enum Role {
        AYM_ADMIN,
        AYM_USER,
        COMPANY_ADMIN,
        COMPANY_USER
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = 'company_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=JSOGGenerator)
    @NotNull
    Company company

    @Column(nullable = false)
    @JsonView(View.Summary)
    @NotNull
    Role role = Role.COMPANY_USER

    @Column(nullable = false, unique = true, length = 20)
    @NotEmpty
    @JsonView(View.Summary)
    String name

    @Column(nullable = false, length = 75)
    @NotEmpty
    @JsonView(View.Summary)
    String firstName

    @Column(nullable = false, length = 75)
    @NotEmpty
    @JsonView(View.Summary)
    String lastName

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'employee', orphanRemoval = true)
    List<Service> services;

}
