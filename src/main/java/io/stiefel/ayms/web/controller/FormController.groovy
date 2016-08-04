package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/form')
class FormController {

    @Autowired FormCtrlRepo ctrlRepo

    @RequestMapping(path = '/create/{formDefId}', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView create(@PathVariable Long formDefId) {
        new ModelAndView('form/instance')
    }

    @RequestMapping(path = '/create/{formDefId}/ctrl', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<FormCtrl>> findAll(@PathVariable Long formDefId) {
        new Result(ctrlRepo.findByDefinitionId(formDefId))
    }


}
