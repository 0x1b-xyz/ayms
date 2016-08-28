package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import groovy.util.logging.Log4j
import io.stiefel.ayms.domain.Ctrl
import io.stiefel.ayms.domain.CtrlId
import io.stiefel.ayms.domain.Definition
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.CtrlRepo
import io.stiefel.ayms.repo.DataRepo
import io.stiefel.ayms.repo.DefinitionRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 * @author jason@stiefel.io
 */
@RestController
@Transactional(readOnly = true)
@Log4j
class DefinitionController {

    @Autowired CtrlRepo ctrlRepo
    @Autowired DataRepo dataRepo
    @Autowired DefinitionRepo defRepo

    @RequestMapping(path = '/form', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('form/definitions')
    }

    @RequestMapping(path = '/form', method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<Definition>> findAllDefinitions() {
        new Result(defRepo.findAll())
    }

    @RequestMapping(path = '/form', method = RequestMethod.POST, produces = 'application/json')
    @Transactional(readOnly = false)
    Result<Long> saveDefinition(@Valid Definition definition, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        defRepo.save(definition)
        new Result(definition.id)
    }

    @RequestMapping(path = '/form/{definitionId}', method = RequestMethod.GET, produces = 'text/html')
    @JsonView(View.Summary)
    ModelAndView findDefinitions(@PathVariable Long definitionId) {
        return new ModelAndView('form/builder', [definition: defRepo.findOne(definitionId)])
    }

    @RequestMapping(path = '/form/{definitionId}', method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Detail)
    Result<Definition> getDefinition(@PathVariable Long definitionId) {
        new Result(defRepo.findOne(definitionId))
    }

    @RequestMapping(path = '/form/{definitionId}/ctrl', method = RequestMethod.POST, produces = 'application/json')
    @Transactional(readOnly = false)
    Result<Definition> saveCtrls(@PathVariable Long definitionId,
                                 @RequestBody List<Ctrl> ctrls,
                                 HttpServletResponse response) {

        Definition definition = defRepo.findOne(definitionId)
        if (!definition)
            response.sendError(404)

        List<String> submitted = ctrls.collect { it.name }
        List<String> deleted = definition.ctrls.findAll { !submitted.contains(it.name) }.collect { it.name }
        deleted.each { deletedCtrl ->
            if (definition.ctrls.removeAll { it.name == deletedCtrl }) {
                dataRepo.deleteForDefinitionIdAndCtrl(definition.id, deletedCtrl)
                ctrlRepo.delete(new CtrlId(definition, deletedCtrl))
            }
        }

        ctrls.each { submittedCtrl ->

            Ctrl ctrl = definition.ctrls.find { submittedCtrl.id.name == it.id.name }
            if (!ctrl) {
                submittedCtrl.id.definition = definition
                definition.ctrls << submittedCtrl
                return;
            }

            // This is kind of lame for updating attributes on an existing control, right?
            ctrl.attr = submittedCtrl.attr
            ctrl.id.definition = definition
            ctrl.layout = submittedCtrl.layout
            ctrl.name = submittedCtrl.name
            ctrl.type = submittedCtrl.type

        }

        defRepo.save(definition)

        new Result(definition);

    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/form/{definitionId}/delete', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView delete(@PathVariable Long definitionId) {
        defRepo.delete(definitionId)
        new ModelAndView("redirect:/form");
    }

}
