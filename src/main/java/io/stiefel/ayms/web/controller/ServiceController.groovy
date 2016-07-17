package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.Service
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.ClientRepo
import io.stiefel.ayms.repo.EmployeeRepo
import io.stiefel.ayms.repo.ServiceRepo
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
@RequestMapping(value = '/client/{clientId}/service')
class ServiceController {

    @Autowired
    ClientRepo clientRepo
    @Autowired
    ServiceRepo serviceRepo
    @Autowired
    EmployeeRepo employeeRepo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date, new CustomDateEditor(new SimpleDateFormat('yyyy-MM-dd'), false)
        )
        binder.registerCustomEditor(
                Client, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(clientRepo.findOne(Long.valueOf(text)))
            }
        })
        binder.registerCustomEditor(
                Employee, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(employeeRepo.findOne(Long.valueOf(text)))
            }
        })
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index(@PathVariable Long clientId) {
        new ModelAndView('service', [
                client : clientRepo.findOne(clientId),
                employees : employeeRepo.findAll().sort { "${it.lastName}, ${it.firstName}" }
        ])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Service>> findAll(@PathVariable Long clientId) {
        new Result(serviceRepo.findByClientId(clientId))
    }

    @RequestMapping(path = '/{serviceId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Service> find(@PathVariable Long serviceId) {
        new Result(serviceRepo.findOne(serviceId))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid Service service, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        serviceRepo.save(service)
        new Result(service.id)
    }

}
