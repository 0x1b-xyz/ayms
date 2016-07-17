package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.FormCtrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface FormCtrlRepo extends JpaRepository<FormCtrl, Long> {

    List<FormCtrl> findByDefinitionId(Long formDefinitionId);

}
