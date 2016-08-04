package io.stiefel.ayms.service

import groovy.util.logging.Log4j
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
@Log4j
class FormDefService {

    @Autowired FormDefRepo formDefRepo
    @Autowired FormCtrlRepo formCtrlRepo

    /**
     * Updates any existing {@link FormCtrl}s on the definition with the new set, adding any new
     * definitions.
     */
    void update(Long formDefinitionId, List<FormCtrl> ctrls) {

        FormDef formDef = formDefRepo.findOne(formDefinitionId)

        List<String> existingCtrls = formCtrlRepo.findIdByDefinitionId(formDefinitionId)
        List<String> deletedCtrls = existingCtrls.findAll { String existingId ->
            !ctrls.find { it.id == existingId }
        }

        if (log.debugEnabled) {
            deletedCtrls.each {
                log.debug("Removing control: ${it} ...")
            }
        }
        deletedCtrls.each { formCtrlRepo.delete(it) }

        ctrls.each { ctrl ->

            log.debug("Saving control: ${ctrl} ...")

            ctrl.definition = formDef
            formCtrlRepo.save(ctrl);

        }

    }

}
