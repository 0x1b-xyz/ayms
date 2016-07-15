package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.dao.FormDefinitionDao
import io.stiefel.ayms.domain.FormDefinition
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/form/definition')
class FormDefinitionController {

    @Autowired FormDefinitionDao dao

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('form/index')
    }

    @RequestMapping(path = '/{definitionId}', method = RequestMethod.GET, produces = 'text/html')
    @JsonView(View.Summary)
    ModelAndView find(@PathVariable Long definitionId) {
        return new ModelAndView('form/builder', [formDefinition: dao.find(definitionId)])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDefinition>> findAll() {
        new Result(dao.findAll())
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid FormDefinition formDefinition, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        dao.save(formDefinition)
        new Result(formDefinition.id)
    }


}
