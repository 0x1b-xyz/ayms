package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jason@stiefel.io
 */
public interface CompanyRepo extends JpaRepository<Company,Long> {}
