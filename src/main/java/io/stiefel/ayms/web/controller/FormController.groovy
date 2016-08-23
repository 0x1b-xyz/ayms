package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import groovy.json.JsonOutput
import groovy.util.logging.Log4j
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormCtrlId
import io.stiefel.ayms.domain.FormData
import io.stiefel.ayms.domain.FormDataId
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.FormResult
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDataRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.repo.FormResultRepo
import io.stiefel.ayms.search.ResultSearchRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.solr.core.SolrTemplate
import org.springframework.transaction.annotation.Transactional
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
@RequestMapping(value = '/form')
@Transactional(readOnly = true)
@Log4j
class FormController {

    @Autowired FormDefRepo defRepo
    @Autowired FormCtrlRepo ctrlRepo
    @Autowired FormResultRepo resultRepo
    @Autowired FormDataRepo dataRepo

    @Autowired ResultSearchRepo search

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FormResult, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(resultRepo.findOne(text))
            }
        })
        binder.registerCustomEditor(FormCtrl, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(ctrlRepo.findOne(text))
            }
        })
    }

    @RequestMapping(method = RequestMethod.GET, produces = 'text/html')
    ModelAndView index() {
        new ModelAndView('form/definitions')
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDef>> findAllDefinitions() {
        new Result(defRepo.findAll())
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    @Transactional(readOnly = false)
    Result<Long> saveDefinition(@Valid FormDef definition, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        defRepo.save(definition)
        new Result(definition.id)
    }

    @RequestMapping(path = '/{definitionId}', method = RequestMethod.GET, produces = 'text/html')
    @JsonView(View.Summary)
    ModelAndView findDefinitions(@PathVariable Long definitionId) {
        return new ModelAndView('form/builder', [definition: defRepo.findOne(definitionId)])
    }

    @RequestMapping(path = '/{definitionId}', method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Detail)
    Result<FormDef> getDefinition(@PathVariable Long definitionId) {
        new Result(defRepo.findOne(definitionId))
    }

    @RequestMapping(path = '/{definitionId}/ctrl', method = RequestMethod.POST, produces = 'application/json')
    @Transactional(readOnly = false)
    Result<FormDef> saveCtrls(@PathVariable Long definitionId,
                                @RequestBody List<FormCtrl> ctrls,
                              HttpServletResponse response) {

        FormDef definition = defRepo.findOne(definitionId)
        if (!definition)
            response.sendError(404)

        List<String> submitted = ctrls.collect { it.name }
        List<String> deleted = definition.ctrls.findAll { !submitted.contains(it.name) }.collect { it.name }
        deleted.each { deletedCtrl ->
            if (definition.ctrls.removeAll { it.name == deletedCtrl }) {
                dataRepo.deleteForDefinitionIdAndCtrl(definition.id, deletedCtrl)
                ctrlRepo.delete(new FormCtrlId(definition, deletedCtrl))
            }
        }

        ctrls.each { submittedCtrl ->

            FormCtrl ctrl = definition.ctrls.find { submittedCtrl.id.name == it.id.name }
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
    @RequestMapping(path = '/{definitionId}/delete', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView delete(@PathVariable Long definitionId) {
        defRepo.delete(definitionId)
        new ModelAndView("redirect:/form");
    }

    @RequestMapping(path = '/{definitionId}/result', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView list(@PathVariable Long definitionId) {
        FormDef definition = defRepo.findOne(definitionId)
        new ModelAndView('form/results', [definition: definition])
    }

    @RequestMapping(path = '/{definitionId}/result', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<FormResult>> listResult(@PathVariable Long definitionId) {
        new Result(resultRepo.findAllByDefinitionId(definitionId));
    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/result/create', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView create(@PathVariable Long definitionId) {
        FormDef definition = defRepo.findOne(definitionId)
        String resultId = UUID.randomUUID().toString().replaceAll('-','').toLowerCase()
        FormResult result = new FormResult(resultId, definition)
        resultRepo.save(result)
        new ModelAndView("redirect:${resultId}")
    }

    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView getResultHtml(@PathVariable Long definitionId, @PathVariable String resultId,
                               HttpServletResponse response) {
        if (!resultRepo.exists(resultId))
            return response.sendError(404)
        new ModelAndView("form/result", [definitionId: definitionId, resultId: resultId]);
    }

    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Detail)
    Result<FormResult> getResult(@PathVariable Long definitionId, @PathVariable String resultId,
                                 HttpServletResponse response) {

        def result = resultRepo.findOneByDefinitionIdAndId(definitionId, resultId)
        if (!result) {
            response.setStatus(404)
            return new Result(false)
        }

        new Result(result);
    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.POST, produces = 'application/json')
    Result<FormResult> save(@PathVariable Long definitionId, @PathVariable String resultId,
                            @RequestBody Map<String,Map<String,String>> data,
                            HttpServletResponse response) {

        FormResult result = resultRepo.findOneByDefinitionIdAndId(definitionId, resultId)
        if (!result) {
            response.setStatus(404)
            return new Result(false)
        }

        // Collect the detail of what was submitted as a list of tuples
        List<Map<String,String>> submitted = data.collect { String ctrl, Map<String,String> ctrlData ->
            ctrlData.collect { String field, String value ->
                [ctrl: ctrl, name: field, value: value]
            }
        }.flatten()

        // Attempt to update each data submitted and update any existing values
        submitted.each { submittedData ->
            FormData existing = result.data.find { it.id.ctrl.name == submittedData.ctrl }
            if (existing) {
                existing.value = submittedData.value
            } else {
                FormCtrl ctrl = result.definition.ctrls.find { it.name == submittedData.ctrl }
                FormData fd = new FormData(new FormDataId(result, ctrl),
                        submittedData.name, submittedData.value)
                result.data << fd
            }
        }

        // Remove anything we didn't see in the submitted data
        result.data.removeAll { FormData formData ->
            !submitted.find { it.ctrl == formData.id.ctrl.name }
        }

        result.update()
        resultRepo.save(result)

        search.save(result)

        new Result(result)

    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.DELETE, produces = 'application/json')
    Result delete(@PathVariable Long definitionId, @PathVariable String resultId,
                  HttpServletResponse response) {
        try {
            resultRepo.delete(resultRepo.findOneByDefinitionIdAndId(definitionId, resultId))
            search.delete(resultId)
        } catch (Throwable t) {
            return new Result(t)
        }
        return new Result()
    }

}
