package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Client
import io.stiefel.ayms.domain.Employee
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.ClientRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid
import java.text.SimpleDateFormat

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/client')
class ClientController {

    @Autowired ClientRepo repo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date, new CustomDateEditor(new SimpleDateFormat('yyyy-MM-dd'), false)
        )
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

    @RequestMapping(path = '/{clientId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<Employee> find(@PathVariable Long clientId) {
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
