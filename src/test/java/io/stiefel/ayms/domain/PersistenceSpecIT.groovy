package io.stiefel.ayms.domain

import io.stiefel.ayms.repo.*
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
@ContextConfiguration(classes = [Context])
@Transactional
@Rollback(false)
class PersistenceSpecIT extends Specification {

    @Autowired CompanyRepo companyRepo
    @Autowired EmployeeRepo employeeRepo
    @Autowired ClientRepo clientRepo
    @Autowired ServiceRepo serviceRepo
    @Autowired NoteRepo noteRepo
    @Autowired DefinitionRepo formDefRepo
    @Autowired CtrlRepo formCtrlRepo

    Address address = new Address('Somewhere in', null, 'Charlotte', 'NC', '28205')
    Company company = new Company(
            "Random Company - ${RandomStringUtils.randomAlphabetic(5)}",
            address
    )

    def setup() {
        companyRepo.save(company)
    }

    def "saves and finds company"() {

        when:
        new Date()

        then:
        companyRepo.findOne(company.id) == company

    }

    def "saves and finds employee"() {

        when:
        Employee employee = employee()
        employeeRepo.save(employee)

        then:
        employeeRepo.findByCompanyId(company.id)[0] == employee

    }

    def "saves and finds client"() {

        when:
        Client client = client()
        clientRepo.save(client)

        then:
        clientRepo.findOne(client.id) == client
        clientRepo.findOne(client.id).company == company
        clientRepo.findByCompanyId(company.id)[0] == client
        clientRepo.findByCompanyIdAndAddressState(company.id, client.address.state)[0] == client

    }

    /**
     * We're executing multiple saves here so we have to make the method transactional until we
     * move the logic into a service class.
     */
    def "saves and finds services"() {

        when:
        Employee employee = employee()
        employeeRepo.save(employee)
        Client client = client()
        employeeRepo.save(client)
        Service svc = new Service()
        svc.employee = employee
        svc.client = client
        serviceRepo.save(svc)

        then:
        serviceRepo.findOne(svc.id) == svc

        when:
        svc.scheduled = new Date() - RandomUtils.nextInt(100)
        serviceRepo.save(svc)

        then:
        serviceRepo.findOne(svc.id).scheduled != null
        serviceRepo.findOne(svc.id) == svc

        when:
        svc.arrived = new Date() - RandomUtils.nextInt(25)
        serviceRepo.save(svc)

        then:
        serviceRepo.findOne(svc.id).arrived != null
        serviceRepo.findOne(svc.id) == svc

    }

    def "saves and finds notes"() {

        when:
        Client client = client()
        clientRepo.save(client)
        Employee employee = employee()
        employeeRepo.save(employee)
        Service svc = new Service()
        svc.employee = employee
        svc.client = client
        serviceRepo.save(svc)

        Note note = new Note()
        note.service = svc
        note.employee = employee
        note.text = RandomStringUtils.randomAlphabetic(200)
        noteRepo.save(note)

        then:
        noteRepo.findOne(note.id) == note

    }

    def "saves and finds form ctrls"() {

        when:
        Definition formDef = new Definition("form-${UUID.randomUUID()}", 'description')
        formDefRepo.save(formDef)
        Ctrl ctrl = new Ctrl(new CtrlId(formDef, RandomStringUtils.randomAlphabetic(5)),
                'TextField', ['labelAlign': 'horizontal'], new Layout(1, 2, 100, 200))
        formCtrlRepo.save(ctrl);

        then:
        formCtrlRepo.findOne(ctrl.id).id.definition == formDef
        formCtrlRepo.findOne(ctrl.id) == ctrl
        formCtrlRepo.findOne(ctrl.id).attr['labelAlign'] == 'horizontal'
        formCtrlRepo.findOne(ctrl.id).layout.x == 1
        formCtrlRepo.findOne(ctrl.id).layout.width == 100

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
