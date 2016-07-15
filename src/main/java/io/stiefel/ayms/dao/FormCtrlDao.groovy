package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.FormCtrl
import io.stiefel.ayms.domain.FormDefinition
import org.springframework.stereotype.Repository

import javax.persistence.Query

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

    /**
     * Removes {@link FormCtrl#attr}s then all the {@link FormCtrl}s.
     */
    void removeByDefinition(Long formDefinitionId) {
        removeAttrs(formDefinitionId)
        em.createNativeQuery("""
    delete from aym_form_ctrl where form_definition_id = :definitionId
            """)
            .setParameter('definitionId', formDefinitionId)
            .executeUpdate()
    }

    void removeAttrs(Long formDefinitionId) {
        em.createNativeQuery("""
    delete from aym_form_ctrl_attr where id in (select id from aym_form_ctrl where form_definition_id = :definitionId)
            """)
            .setParameter('definitionId', formDefinitionId)
            .executeUpdate()
    }

}
