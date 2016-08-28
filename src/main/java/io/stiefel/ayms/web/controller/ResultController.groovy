package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import groovy.util.logging.Log4j
import io.stiefel.ayms.domain.*
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDataRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.repo.FormResultRepo
import io.stiefel.ayms.search.ResultSearchRepo
import io.stiefel.ayms.search.ResultsDocument
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletResponse
import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@Transactional(readOnly = true)
@Log4j
class ResultController {

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
//        binder.registerCustomEditor(FormCtrl, new PropertyEditorSupport() {
//            @Override
//            void setAsText(String text) throws IllegalArgumentException {
//                setValue(ctrlRepo.findOne(text))
//            }
//        })
    }

    @RequestMapping(path = '/form/{definitionId}/result', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView list(@PathVariable Long definitionId) {
        FormDef definition = defRepo.findOne(definitionId)
        new ModelAndView('form/results', [definition: definition])
    }

    @RequestMapping(path = '/form/{definitionId}/result', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<FormResult>> listResult(@PathVariable Long definitionId) {
        new Result(resultRepo.findAllByDefinitionId(definitionId));
    }

    @Transactional(readOnly = false)
    @RequestMapping(path = '/form/{definitionId}/result/create', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView create(@PathVariable Long definitionId) {
        FormDef definition = defRepo.findOne(definitionId)
        String resultId = UUID.randomUUID().toString().replaceAll('-','').toLowerCase()
        FormResult result = new FormResult(resultId, definition)
        resultRepo.save(result)
        new ModelAndView("redirect:${resultId}")
    }

    @RequestMapping(path = '/form/{definitionId}/result/{resultId}', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView getResultHtml(@PathVariable Long definitionId, @PathVariable String resultId,
                               HttpServletResponse response) {
        if (!resultRepo.exists(resultId))
            return response.sendError(404)
        new ModelAndView("form/result", [definitionId: definitionId, resultId: resultId]);
    }

    @RequestMapping(path = '/form/{definitionId}/result/{resultId}', method = RequestMethod.GET, produces = 'application/json')
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
    @RequestMapping(path = '/form/{definitionId}/result/{resultId}', method = RequestMethod.POST, produces = 'application/json')
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
    @RequestMapping(path = '/form/{definitionId}/result/{resultId}', method = RequestMethod.DELETE, produces = 'application/json')
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

    @RequestMapping(path = '/form/{definitionId}/fields', method = RequestMethod.GET, produces = 'application/json')
    Result<List<String>> fields(@PathVariable Long definitionId) {
        new Result(defRepo.findFieldNames(definitionId))
    }

    @RequestMapping(path = '/form/{definitionId}/search', method = RequestMethod.POST, produces = 'application/json')
    Result<List<ResultsDocument>> search(@PathVariable Long definitionId,
                                         @RequestBody Map<String,String> terms) {
        new Result(search.find(definitionId, terms))
    }

}
