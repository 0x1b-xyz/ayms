package io.stiefel.ayms.repo

import io.stiefel.ayms.domain.Result
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author jason@stiefel.io
 */
interface ResultRepo extends JpaRepository<Result, String> {

    List<Result> findAllByDefinitionId(Long definitionId);

    Result findOneByDefinitionIdAndId(Long definitionId, String resultId);

}
