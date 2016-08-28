package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    List<Employee> findByCompanyId(Long companyId);

}
