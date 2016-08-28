package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Ctrl;
import io.stiefel.ayms.domain.CtrlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query

/**
 * @author jason@stiefel.io
 */
public interface CtrlRepo extends JpaRepository<Ctrl, CtrlId> {

    List<Ctrl> findByIdDefinitionId(Long formDefinitionId);

    @Query("select ctrl.id from Ctrl ctrl where ctrl.id.definition.id = ?1")
    List<String> findIdByDefinitionId(Long formDefinitionId);

}
