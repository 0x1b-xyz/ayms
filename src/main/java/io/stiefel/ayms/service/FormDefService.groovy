package io.stiefel.ayms.service

import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDef
import io.stiefel.ayms.repo.FormCtrlRepo
import io.stiefel.ayms.repo.FormDefRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author jason@stiefel.io
 */
@Service
@Transactional
class FormDefService {

    @Autowired FormDefRepo formDefRepo
    @Autowired FormCtrlRepo formCtrlRepo

    /**
     * Replaces any existing {@link FormCtrl}s on the definition with the new set.
     */
    void replace(Long formDefinitionId, List<FormCtrl> ctrls) {

        FormDef formDef = formDefRepo.findOne(formDefinitionId)
        formCtrlRepo.delete((Iterable)formCtrlRepo.findByDefinitionId(formDef.id))
        ctrls.each {
            it.definition = formDef
            formCtrlRepo.save(it)
        }

    }

}
