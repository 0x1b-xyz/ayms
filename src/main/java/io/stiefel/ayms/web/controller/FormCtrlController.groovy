package io.stiefel.ayms.web.controller

import com.fasterxml.jackson.annotation.JsonView
import io.stiefel.ayms.dao.FormCtrlDao
import io.stiefel.ayms.dao.FormDefinitionDao
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDefinition
import io.stiefel.ayms.domain.View
import io.stiefel.ayms.web.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

/**
 * @author jason@stiefel.io
 */
@RestController
@RequestMapping(value = '/formDefinition/{formDefinitionId}/formCtrl')
class FormCtrlController {

    @Autowired FormDefinitionDao definitionDao
    @Autowired FormCtrlDao ctrlDao

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @JsonView(View.Summary)
    Result<List<FormDefinition>> findAll(@PathVariable Long formDefinitionId) {
        FormDefinition definition = definitionDao.find(formDefinitionId)
        new Result(ctrlDao.findAllByDefinition(definition))
    }

    @RequestMapping(method = RequestMethod.POST, produces = 'application/json')
    Result<Long> save(@Valid FormCtrl formCtrl, BindingResult binding) {
        if (binding.hasErrors())
            return new Result(false, null).binding(binding)
        ctrlDao.save(formCtrl)
        new Result(formCtrl.id)
    }

}
