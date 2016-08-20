package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.voodoodyne.jackson.jsog.JSOGGenerator
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_client')
@Canonical
class Client extends AbstractEntity<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = 'company_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=JSOGGenerator)
    Company company

    @Column(nullable = false, length = 75)
    @JsonView(View.Summary)
    @NotEmpty
    String firstName

    @Column(nullable = false, length = 75)
    @JsonView(View.Summary)
    @NotEmpty
    String lastName

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonView(View.Summary)
    @NotNull
    Date dateOfBirth

    @Column
    @JsonView(View.Summary)
    @NotEmpty
    @Pattern(regexp = '\\d{3}-\\d{2}-\\d{4}')
    String ssn

    @Embedded
    @JsonView(View.Summary)
    @Valid
    Address address

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'client', orphanRemoval = true)
    List<Service> services;

}
