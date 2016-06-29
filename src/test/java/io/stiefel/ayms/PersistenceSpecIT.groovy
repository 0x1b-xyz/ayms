package io.stiefel.ayms

import io.stiefel.ayms.dao.ClientDao
import io.stiefel.ayms.dao.CompanyDao
import io.stiefel.ayms.dao.ServiceDao
import io.stiefel.ayms.dao.UserDao
import io.stiefel.ayms.domain.Address
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Service
import io.stiefel.ayms.domain.User
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.math.RandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

/**
 * @author jason@stiefel.io
 */
@ContextConfiguration(classes = Context)
class PersistenceSpecIT extends Specification {

    @Autowired CompanyDao companyDao
    @Autowired UserDao userDao
    @Autowired ClientDao clientDao
    @Autowired ServiceDao serviceDao

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
        User user = user()
        userDao.save(user)

        then:
        userDao.findByCompany(company)[0] == user
        userDao.findByName(user.name) == user

    }

    def "saves and finds client"() {

        when:
        Client client = client()
        clientDao.save(client)

        then:
        clientDao.find(client.id) == client
        clientDao.find(client.id).company == company
        clientDao.findAllByCompany(company)[0] == client
        clientDao.findAllByCompanyAndState(company, client.address.state)[0] == client

    }

    /**
     * We're executing multiple saves here so
     * @return
     */
    @Transactional
    def "saves and finds services"() {

        when:
        User user = user()
        userDao.save(user)
        Client client = client()
        clientDao.save(client)
        Service svc = new Service()
        svc.user = user
        svc.client = client
        serviceDao.save(svc)

        then:
        serviceDao.find(svc.id) == svc

        when:
        svc.scheduled = new Date() - RandomUtils.nextInt(100)
        serviceDao.save(svc)

        then:
        serviceDao.find(svc.id).scheduled != null
        serviceDao.find(svc.id) == svc

        when:
        svc.arrived = new Date() - RandomUtils.nextInt(25)
        serviceDao.save(svc)

        then:
        serviceDao.find(svc.id).arrived != null
        serviceDao.find(svc.id) == svc

    }

    Client client() {
        Client client = new Client()
        client.address = address
        client.company = company
        client.dateOfBirth = new Date() - RandomUtils.nextInt(3000)
        client.firstName = RandomStringUtils.randomAlphabetic(20)
        client.lastName = RandomStringUtils.randomAlphabetic(20)
        client
    }

    User user() {
        User user = new User()
        user.company = company
        user.role = User.Role.COMPANY_ADMIN
        user.name = RandomStringUtils.randomAlphabetic(5)
        user.firstName = RandomStringUtils.randomAlphabetic(10)
        user.lastName = RandomStringUtils.randomAlphabetic(10)
        user
    }

}
