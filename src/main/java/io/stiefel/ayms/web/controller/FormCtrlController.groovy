package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDefRepo
import io.stiefel.ayms.service.FormDefService
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletResponse
import java.beans.PropertyEditorSupport

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/form/def/{formDefId}/ctrl')
class FormCtrlController {

    @Autowired FormDefService service
    @Autowired FormDefRepo formDefRepo
    @Autowired FormCtrlRepo repo

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FormDef, new PropertyEditorSupport() {
            @Override
            void setAsText(String text) throws IllegalArgumentException {
                setValue(formDefRepo.findOne(Long.parseLong(text)))
            }
        })
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDef>> findAll(@PathVariable Long formDefId) {
        new Result(repo.findByDefinitionId(formDefId))
    }

//    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
//    Result<Long> save(@Valid FormCtrl formCtrl, BindingResult binding) {
//        if (binding.hasErrors())
//            return new Result(false, null).binding(binding)
//        repo.save(formCtrl)
//        new Result(formCtrl.id)
//    }

    @RequestMapping(path = '/replace', method = RequestMethod.POST)
    void replace(@PathVariable Long formDefId,
                 @RequestBody List<FormCtrl> ctrls, HttpServletResponse response) {
        service.replace(formDefId, ctrls)
        response.status = 200

    }

}
