package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.FormCtrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface FormCtrlRepo extends JpaRepository<FormCtrl, String> {

    List<FormCtrl> findByDefinitionId(Long formDefinitionId);

    @Query("select ctrl.id from FormCtrl ctrl where ctrl.definition.id = ?1")
    List<String> findIdByDefinitionId(Long formDefinitionId);

}
