package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
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
    @JsonView(View.Summary)
    Long id;

    @Column(unique = true, nullable = false, length = 100)
    @JsonView(View.Summary)
    String name;

    @Embedded
    @JsonView(View.Summary)
    Address address

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<Client> clients;

}
