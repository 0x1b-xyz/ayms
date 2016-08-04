package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.service.FormDefService
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/formDef')
class FormDefController {

    @Autowired FormDefService service
    @Autowired FormCtrlRepo ctrlRepo
    @Autowired FormDefRepo repo

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('form/definitions')
    }

    @RequestMapping(path = '/{defId}', method = RequestMethod.GET, produces = 'text/html')
    @JsonView(View.Summary)
    ModelAndView find(@PathVariable Long defId) {
        return new ModelAndView('form/builder', [formDef: repo.findOne(defId)])
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDef>> findAll() {
        new Result(repo.findAll())
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid FormDef formDefinition, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        repo.save(formDefinition)
        new Result(formDefinition.id)
    }

    @RequestMapping(path = '/{formDefId}/ctrl', method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormCtrl>> findAll(@PathVariable Long formDefId) {
        new Result(ctrlRepo.findByDefinitionId(formDefId))
    }

    @RequestMapping(path = '/{formDefId}/ctrl', method = RequestMethod.POST)
    void replace(@PathVariable Long formDefId,
                 @RequestBody List<FormCtrl> ctrls, HttpServletResponse response) {
        service.update(formDefId, ctrls)
        response.status = 200
    }


}
