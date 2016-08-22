package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.FormCtrl;
import io.stiefel.ayms.domain.FormCtrlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface FormCtrlRepo extends JpaRepository<FormCtrl, FormCtrlId> {

    List<FormCtrl> findByIdDefinitionId(Long formDefinitionId);

    @Query("select ctrl.id from FormCtrl ctrl where ctrl.id.definition.id = ?1")
    List<String> findIdByDefinitionId(Long formDefinitionId);

}
