package io.stiefel.ayms

import io.stiefel.ayms.dao.ClientDao
import io.stiefel.ayms.dao.CompanyDao
import io.stiefel.ayms.dao.FormCtrlDao
import io.stiefel.ayms.dao.FormDefinitionDao
import io.stiefel.ayms.dao.NoteDao
import io.stiefel.ayms.dao.ServiceDao
import io.stiefel.ayms.dao.EmployeeDao
import io.stiefel.ayms.domain.Address
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDefinition
import io.stiefel.ayms.domain.Layout
import io.stiefel.ayms.domain.Note
import io.stiefel.ayms.domain.Service
import io.stiefel.ayms.domain.Employee
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.math.RandomUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

/**
 * @author jason@stiefel.io
 */
@ContextConfiguration(classes = Context)
@Transactional
@Rollback(false)
class PersistenceSpecIT extends Specification {

    @Autowired CompanyDao companyDao
    @Autowired EmployeeDao employeeDao
    @Autowired ClientDao clientDao
    @Autowired ServiceDao serviceDao
    @Autowired NoteDao noteDao
    @Autowired FormDefinitionDao formDefinitionDao
    @Autowired FormCtrlDao formCtrlDao

    Address address = new Address('Somewhere in', null, 'Charlotte', 'NC', '28205')
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

    def "saves and finds employee"() {

        when:
        Employee employee = employee()
        employeeDao.save(employee)

        then:
        employeeDao.findAllByCompany(company)[0] == employee
        employeeDao.findByName(employee.name) == employee

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

    def "saves and finds form ctrls"() {

        when:
        FormDefinition formDef = new FormDefinition(null, "form-${UUID.randomUUID()}", 'description')
        formDefinitionDao.save(formDef)
        FormCtrl ctrl = new FormCtrl(UUID.randomUUID().toString(), formDef,
                'TextField', ['labelAlign': 'horizontal'])
        ctrl.layout = new Layout(1, 2, 100, 200)
        formCtrlDao.save(ctrl);

        then:
        formCtrlDao.find(ctrl.id).definition == formDef
        formCtrlDao.find(ctrl.id) == ctrl
        formCtrlDao.find(ctrl.id).attr['labelAlign'] == 'horizontal'
        formCtrlDao.find(ctrl.id).layout.x == 1
        formCtrlDao.find(ctrl.id).layout.width == 100

    }

    /**
     * We're executing multiple saves here so we have to make the method transactional until we
     * move the logic into a service class.
     */
    def "saves and finds services"() {

        when:
        Employee employee = employee()
        employeeDao.save(employee)
        Client client = client()
        clientDao.save(client)
        Service svc = new Service()
        svc.employee = employee
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

    def "saves and finds notes"() {

        when:
        Client client = client()
        clientDao.save(client)
        Employee employee = employee()
        employeeDao.save(employee)
        Service svc = new Service()
        svc.employee = employee
        svc.client = client
        serviceDao.save(svc)

        Note note = new Note()
        note.service = svc
        note.employee = employee
        note.text = RandomStringUtils.randomAlphabetic(200)
        noteDao.save(note)

        then:
        noteDao.find(note.id) == note

    }


    Client client() {
        Client client = new Client()
        client.address = address
        client.company = company
        client.dateOfBirth = new Date() - RandomUtils.nextInt(3000)
        client.firstName = RandomStringUtils.randomAlphabetic(20)
        client.lastName = RandomStringUtils.randomAlphabetic(20)
        client.ssn = "000-00-0000"
        client
    }

    Employee employee() {
        Employee employee = new Employee()
        employee.company = company
        employee.role = Employee.Role.COMPANY_ADMIN
        employee.name = RandomStringUtils.randomAlphabetic(5)
        employee.firstName = RandomStringUtils.randomAlphabetic(10)
        employee.lastName = RandomStringUtils.randomAlphabetic(10)
        employee
    }

}
