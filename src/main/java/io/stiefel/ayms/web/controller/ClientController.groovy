package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.ClientRepo
import io.stiefel.ayms.repo.CompanyRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
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
@RequestMapping(value = '/client')
class ClientController {

    @Autowired CompanyRepo companyRepo
    @Autowired ClientRepo repo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date, new CustomDateEditor(new SimpleDateFormat('yyyy-MM-dd'), false)
        )
        binder.registerCustomEditor(Company, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(companyRepo.findOne(Long.parseLong(text)))
            }
        })

    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('client')
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Client>> findAll() {
        new Result(repo.findAll())
    }

    @RequestMapping(path = '/search', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<Client>> search(@RequestParam String field, @RequestParam String term) {
        Client client = new Client()
        client[field] = term
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreCase()
            .withMatcher(field, ExampleMatcher.GenericPropertyMatchers.startsWith())
        new Result(repo.findAll(Example.of(client, matcher)))
    }

    @RequestMapping(path = '/{clientId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Client> find(@PathVariable Long clientId) {
        new Result(repo.findOne(clientId))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid Client client, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        repo.save(client)
        new Result(client.id)
    }

}
