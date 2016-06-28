package io.stiefel.ayms

import io.stiefel.ayms.dao.ClientDao
import io.stiefel.ayms.dao.CompanyDao
import io.stiefel.ayms.dao.UserDao
import io.stiefel.ayms.domain.Address
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.User
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.math.RandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * @author jason@stiefel.io
 */
@ContextConfiguration(locations = ['classpath:io/stiefel/ayms/context.xml'])
class PersistenceSpecIT extends Specification {

    @Autowired CompanyDao companyDao
    @Autowired UserDao userDao
    @Autowired ClientDao clientDao

    Address address = new Address('Somewhere in', null, null, null, 'Charlotte', 'NC', '28205')
    Company company = new Company(
            name: RandomStringUtils.randomAlphabetic(10),
            address: address
    )

    def setup() {
        companyDao.remove(company)
        companyDao.save(company)
    }

    def "saves and finds company"() {

        when:
        new Date()

        then:
        companyDao.find(company.id) == company

    }

    def "saves and finds user"() {

        when:
        User user = new User()
        user.company = company
        user.role = User.Role.COMPANY_ADMIN
        user.name = RandomStringUtils.randomAlphabetic(5)
        user.firstName = RandomStringUtils.randomAlphabetic(10)
        user.lastName = RandomStringUtils.randomAlphabetic(10)
        userDao.save(user)

        then:
        userDao.findByCompany(company)[0] == user
        userDao.findByName(user.name) == user

    }

    def "saves and finds client"() {

        when:
        Client client = new Client()
        client.address = address
        client.company = company
        client.dateOfBirth = new Date() - RandomUtils.nextInt(3000)
        client.firstName = RandomStringUtils.randomAlphabetic(20)
        client.lastName = RandomStringUtils.randomAlphabetic(20)
        clientDao.save(client)

        then:
        clientDao.find(client.id) == client
        clientDao.find(client.id).company == company
        clientDao.findAllByCompany(company)[0] == client
        clientDao.findAllByCompanyAndState(company, client.address.state)[0] == client

    }

}
