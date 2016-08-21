package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import groovy.util.logging.Log4j
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/formDef')
@Log4j
@Transactional(readOnly = true)
class FormDefController {

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
    @Transactional(readOnly = false)
    Result<Long> save(@Valid FormDef formDefinition, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        repo.save(formDefinition)
        new Result(formDefinition.id)
    }

    @RequestMapping(path = '/{definitionId}', method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Detail)
    Result<FormDef> get(@PathVariable Long definitionId) {
        def formDef = repo.findOne(definitionId)
        new Result(formDef)
    }

    @RequestMapping(path = '/{definitionId}/ctrl', method = RequestMethod.POST)
    @Transactional(readOnly = false)
    void replace(@PathVariable Long definitionId,
                 @RequestBody List<FormCtrl> ctrls, HttpServletResponse response) {

        FormDef formDef = repo.findOne(definitionId)

        List<String> existingCtrls = ctrlRepo.findIdByDefinitionId(definitionId)
        List<String> deletedCtrls = existingCtrls.findAll { String existingId ->
            !ctrls.find { it.id == existingId }
        }

        if (log.debugEnabled) {
            deletedCtrls.each {
                log.debug("Removing control: ${it} ...")
            }
        }
        deletedCtrls.each { ctrlRepo.delete(it) }

        ctrls.each { ctrl ->

            ctrl.definition = formDef

            log.debug("Saving control: ${ctrl} ...")

            ctrlRepo.save(ctrl);

        }

        response.status = 200
    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/delete', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView delete(@PathVariable Long definitionId) {
        repo.delete(definitionId)
        new ModelAndView("redirect:/formDef");
    }


}
