package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.dao.ClientDao
import io.stiefel.ayms.dao.CompanyDao
import io.stiefel.ayms.dao.ServiceDao
import io.stiefel.ayms.dao.EmployeeDao
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Service
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid
import java.beans.PropertyEditorSupport
import java.text.SimpleDateFormat

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/company/{companyId}/client/{clientId}/service')
class ServiceController {

    @Autowired
    CompanyDao companyDao
    @Autowired
    ClientDao clientDao
    @Autowired
    ServiceDao serviceDao
    @Autowired
    EmployeeDao employeeDao

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date, new CustomDateEditor(new SimpleDateFormat('yyyy-MM-dd'), false)
        )
        binder.registerCustomEditor(
                Employee, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(employeeDao.find(Long.valueOf(text)))
            }
        })
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index(@PathVariable Long companyId, @PathVariable Long clientId) {
        Company company = companyDao.find(companyId)
        new ModelAndView('service', [
                companyId: companyId,
                clientId : clientId,
                employees    : employeeDao.findAllByCompany(company).sort { "${it.lastName}, ${it.firstName}" }
        ])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Service>> findAll(@PathVariable Long companyId, @PathVariable Long clientId) {
        Company company = companyDao.find(companyId)
        Client client = clientDao.findByCompanyAndId(company, clientId)
        new Result(serviceDao.findAllByClient(client))
    }

    @RequestMapping(path = '/{serviceId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Service> find(@PathVariable Long companyId, @PathVariable Long clientId,
                         @PathVariable Long serviceId) {
        Company company = companyDao.find(companyId)
        Client client = clientDao.findByCompanyAndId(company, clientId)
        new Result(serviceDao.findByClientAndId(client, serviceId))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@PathVariable Long companyId, @PathVariable Long clientId, @Valid Service service,
                      BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)

        Company company = companyDao.find(companyId)
        Client client = clientDao.findByCompanyAndId(company, clientId)
        service.client = client
        serviceDao.save(service)
        new Result(service.id)
    }

}
