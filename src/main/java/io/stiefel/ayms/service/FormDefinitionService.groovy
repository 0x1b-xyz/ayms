package io.stiefel.ayms.service

import io.stiefel.ayms.dao.FormCtrlDao
import io.stiefel.ayms.dao.FormDefinitionDao
import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDefinition
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author jason@stiefel.io
 */
@Service
@Transactional
class FormDefinitionService {

    @Autowired FormDefinitionDao definitionDao
    @Autowired FormCtrlDao ctrlDao

    /**
     * Replaces any existing {@link FormCtrl}s on the definition with the new set.
     */
    void replace(Long formDefinitionId, List<FormCtrl> ctrls) {

        FormDefinition definition = definitionDao.find(formDefinitionId)
        ctrlDao.removeByDefinition(definition.id)
        ctrls.each {
            it.definition = definition
            ctrlDao.save(it)
        }

    }

}
