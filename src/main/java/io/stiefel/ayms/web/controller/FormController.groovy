package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormData
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.FormResult
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDataRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.repo.FormResultRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletResponse
import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/form')
@Transactional(readOnly = true)
class FormController {

    @Autowired FormDefRepo defRepo
    @Autowired FormCtrlRepo ctrlRepo
    @Autowired FormResultRepo resultRepo
    @Autowired FormDataRepo dataRepo

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

    @RequestMapping(path = '/{definitionId}/result', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView list(@PathVariable Long definitionId) {
        FormDef definition = defRepo.findOne(definitionId)
        new ModelAndView('form/results', [definition: definition])
    }

    @RequestMapping(path = '/{definitionId}/result', method = RequestMethod.GET, produces = 'application/json')
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
    ModelAndView getResultHtml(@PathVariable Long definitionId, @PathVariable String resultId) {
        new ModelAndView("form/result", [result: resultRepo.findOneByIdAndDefinitionId(resultId, definitionId)]);
    }

    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Detail)
    Result<FormResult> getResult(@PathVariable Long definitionId, @PathVariable String resultId,
                                 HttpServletResponse response) {

        def result = resultRepo.findOneByIdAndDefinitionId(resultId, definitionId)
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

        FormResult result = resultRepo.findOneByIdAndDefinitionId(resultId, definitionId)
        if (!result) {
            response.setStatus(404)
            return new Result(false)
        }

        // Collect the detail of what was submitted as a list of tuples
        List<Map<String,String>> submitted = data.collect { String ctrlId, Map<String,String> ctrlData ->
            ctrlData.collect { String field, String value ->
                [ctrlId: ctrlId, name: field, value: value]
            }
        }.flatten()

        // Attempt to update each data submitted and update any existing values
        submitted.each { submittedData ->
            FormData existing = result.data.find { it.ctrl.id == submittedData.ctrlId && it.name == submittedData.name }
            if (existing) {
                existing.value = submittedData.value
            } else {
                FormCtrl ctrl = result.definition.ctrls.find { it.id == submittedData.ctrlId }
                FormData fd = new FormData(result, ctrl,
                        submittedData.name, submittedData.value)
                result.data << fd
            }
        }

        // Remove anything we didn't see in the submitted data
        result.data.removeAll { FormData formData ->
            !submitted.find { it.ctrlId == formData.ctrl.id && it.name == formData.name }
        }

        //result.update()
        resultRepo.save(result)

        new Result(result)

    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/result/{resultId}/delete', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView delete(@PathVariable Long definitionId, @PathVariable String resultId) {
        doDelete(definitionId, resultId)
        new ModelAndView("redirect:/form/${definitionId}/result");
    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/{definitionId}/result/{resultId}', method = RequestMethod.DELETE, produces = 'application/json')
    Result delete(@PathVariable Long definitionId, @PathVariable String resultId,
                  HttpServletResponse response) {
        return new Result(doDelete(definitionId, resultId))
    }

    private boolean doDelete(Long definition, String resultId) {
        try {
            resultRepo.delete(resultRepo.findOneByIdAndDefinitionId(resultId,definition))
        } catch (Throwable t) {
            return false
        }
        true
    }

}
