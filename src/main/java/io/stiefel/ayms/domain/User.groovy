package io.stiefel.ayms.domain

import groovy.transform.Canonical

import javax.persistence.*

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_user')
@Canonical(includes = 'id')
@NamedQueries([
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
    Long id

    @ManyToOne(optional = false)
    @JoinColumn(name = 'company_id')
    Company company

    @Column(nullable = false)
    Role role = Role.COMPANY_USER

    @Column(nullable = false, unique = true, length = 20)
    String name

    @Column(nullable = false, length = 75)
    String firstName

    @Column(nullable = false, length = 75)
    String lastName

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'user', orphanRemoval = true)
    List<Service> services;

}
