package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Definition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author jason@stiefel.io
 */
public interface DefinitionRepo extends JpaRepository<Definition, Long> {

    @Query(value = """
        SELECT DISTINCT inside.name
         FROM (
            SELECT fc.name || '.' || fd.name as name
            FROM aym_form_data fd JOIN aym_form_ctrl fc on fd.ctrl = fc.name
            WHERE fd.definition_id = ?1
        ) inside
    """, nativeQuery = true)
    List<String> findFieldNames(Long definitionId);

}
