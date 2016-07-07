package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.dao.ClientDao
import io.stiefel.ayms.dao.CompanyDao
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid
import java.text.SimpleDateFormat

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/company/{companyId}/client')
class ClientController {

    @Autowired CompanyDao companyDao
    @Autowired ClientDao clientDao

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date, new CustomDateEditor(new SimpleDateFormat('yyyy-MM-dd'), false)
        )
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index(@PathVariable Long companyId) {
        new ModelAndView('client', [companyId: companyId])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Client>> findAll(@PathVariable Long companyId) {
        Company company = companyDao.find(companyId)
        new Result(clientDao.findAllByCompany(company))
    }

    @RequestMapping(path = '/{clientId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Employee> find(@PathVariable Long companyId, @PathVariable Long clientId) {
        Company company = companyDao.find(companyId)
        new Result(clientDao.findByCompanyAndId(company, clientId))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@PathVariable Long companyId, @Valid Client client, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        client.company = companyDao.find(companyId)
        clientDao.save(client)
        new Result(client.id)
    }

}
