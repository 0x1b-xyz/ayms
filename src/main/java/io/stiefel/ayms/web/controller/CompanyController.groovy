package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.CompanyRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/company')
class CompanyController {

    @Autowired CompanyRepo repo

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() { new ModelAndView('company') }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Company>> findAll() {
        new Result(repo.findAll())
    }

    @RequestMapping(path = '/{companyId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Company> find(@PathVariable Long companyId) {
        new Result(repo.findOne(companyId))
    }

    @RequestMapping(path = '/search', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<Company>> search(@RequestParam String field, @RequestParam String term) {
        Company company = new Company()
        company[field] = term
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withMatcher(field, ExampleMatcher.GenericPropertyMatchers.startsWith())
        new Result(repo.findAll(Example.of(company, matcher)))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid Company company, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        repo.save(company)
        new Result(company.id)
    }

}
