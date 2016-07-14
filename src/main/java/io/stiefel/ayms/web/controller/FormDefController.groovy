package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.dao.FormDefDao
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
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
@RequestMapping(value = '/formdef')
class FormDefController {

    @Autowired FormDefDao dao

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('formdef/index')
    }

    @RequestMapping(path = '/{formdefId}', method = RequestMethod.GET, produces = 'text/html')
    @JsonView(View.Summary)
    ModelAndView find(@PathVariable Long formdefId) {
        return new ModelAndView('formdef/builder', [formdef: dao.find(formdefId)])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDef>> findAll() {
        new Result(dao.findAll())
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid FormDef formdef, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        dao.save(formdef)
        new Result(formdef.id)
    }


}
