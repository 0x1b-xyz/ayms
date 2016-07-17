package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.FormDef;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jason@stiefel.io
 */
public interface FormDefRepo extends JpaRepository<FormDef, Long> {}
