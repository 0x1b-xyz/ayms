package io.stiefel.ayms.domain

import com.fasterxml.jackson.annotation.JsonView
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty

import javax.persistence.*
import javax.validation.Valid

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
    @NotEmpty
    String name;

    @Embedded
    @JsonView(View.Summary)
    @Valid
    Address address

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = 'company', orphanRemoval = true)
    List<Client> clients;

}
