package io.stiefel.ayms.domain

import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

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

}
