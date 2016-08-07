package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.CompanyRepo
import io.stiefel.ayms.repo.EmployeeRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid
import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/employee')
class EmployeeController {

    @Autowired CompanyRepo companyRepo
    @Autowired EmployeeRepo repo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Company, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(companyRepo.findOne(Long.parseLong(text)))
            }
        })
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('employee')
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Employee>> findAll(Long companyId) {
        if (companyId) {
            return new Result(repo.findByCompanyId(companyId))
        }
        new Result(repo.findAll())
    }

    @RequestMapping(path = '/{employeeId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Employee> find(@PathVariable Long employeeId) {
        new Result(repo.findOne(employeeId))
    }

    @RequestMapping(path = '/search', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<Company>> search(@RequestParam String field, @RequestParam String term) {
        Employee employee = new Employee()
        employee[field] = term
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths('role')
                .withMatcher(field, ExampleMatcher.GenericPropertyMatchers.startsWith())
        new Result(repo.findAll(Example.of(employee, matcher)))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid Employee employee, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        repo.save(employee)
        new Result(employee.id)
    }

}
