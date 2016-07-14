package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDefinition
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class FormCtrlDao extends AbstractDao<FormCtrl, String> {

    List<FormCtrl> findAllByDefinition(FormDefinition definition) {
        em.createNamedQuery('FormCtrl.findAllByDefinition', FormCtrl)
                .setParameter('definition', definition)
                .resultList
    }

}
