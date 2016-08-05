package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormData
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.FormResult
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/form')
class FormController {

    @Autowired FormDefRepo defRepo
    @Autowired FormCtrlRepo ctrlRepo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FormCtrl, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(ctrlRepo.findOne(text))
            }
        })
    }

    @RequestMapping(path = '/{formDefId}/create', method = RequestMethod.GET, produces = 'text/html')
    ModelAndView create(@PathVariable Long formDefId) {
        FormDef formDef = defRepo.findOne(formDefId)
        new ModelAndView('form/result', [formDef: formDef])
    }

    @RequestMapping(path = '/{formDefId}/create/ctrl', method = RequestMethod.GET, produces = 'application/json')
    @JsonView(View.Summary)
    Result<List<FormCtrl>> findAll(@PathVariable Long formDefId) {
        new Result(ctrlRepo.findByDefinitionId(formDefId))
    }

    @RequestMapping(path = '/{formDefId}/save', method = RequestMethod.POST, produces = 'application/json')
    Result<FormResult> save(@PathVariable Long formDefId, List<FormData> datas) {
        FormDef formDef = defRepo.findOne(formDefId)
        new Result(new FormResult(UUID.randomUUID().toString(), formDef))
    }

}
