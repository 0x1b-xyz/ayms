package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_user')
@Canonical(includes = 'id')
@NamedQueries([
        @NamedQuery(name = 'User.findByCompanyAndId', query = 'select u from User u where u.company = :company and u.id = :id'),
        @NamedQuery(name = 'User.findByName', query = 'select u from User u where u.name = :name'),
        @NamedQuery(name = 'User.findAllByCompany', query = 'select u from User u where u.company = :company')
])
class User {

    enum Role {
        AYM_ADMIN,
        AYM_USER,
        COMPANY_ADMIN,
        COMPANY_USER
    }

    @Id
    @GeneratedValue
    @JsonView(View.Summary)
    Long id

    @ManyToOne(optional = false)
    @JoinColumn(name = 'company_id')
    @JsonView(View.Summary)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'user', orphanRemoval = true)
    List<Service> services;

}
