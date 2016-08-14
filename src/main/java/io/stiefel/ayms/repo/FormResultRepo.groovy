package io.stiefel.ayms.repo

import io.stiefel.ayms.domain.FormResult
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author jason@stiefel.io
 */
interface FormResultRepo extends JpaRepository<FormResult, String> {

    List<FormResult> findAllByDefinitionId(Long definitionId);

    FormResult findOneByIdAndDefinitionId(String resultId, Long definitionId);

}
