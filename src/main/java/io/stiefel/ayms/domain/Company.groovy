package io.stiefel.ayms.domain

import groovy.transform.Canonical

import javax.persistence.*

/**
 * @author jason@stiefel.io
 */
@Entity
@Table(name = 'aym_company')
@Canonical(includes = 'id')
class Company implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true, nullable = false, length = 100)
    String name;

    @Embedded
    Address address

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<Client> clients;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<Service> services;

}
