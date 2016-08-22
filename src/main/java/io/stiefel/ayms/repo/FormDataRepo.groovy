package io.stiefel.ayms.repo

import io.stiefel.ayms.domain.FormData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

/**
 * @author jason@stiefel.io
 */
interface FormDataRepo extends JpaRepository<FormData, Long> {

    @Query(value = """
    delete from aym_form_data where result_id = (
        select id from aym_form_result where aym_form_result.definition_id = ?1
    ) AND ctrl = ?2
    """, nativeQuery = true)
    @Modifying
    void deleteForDefinitionIdAndCtrl(Long definitionId, String ctrl);

}
