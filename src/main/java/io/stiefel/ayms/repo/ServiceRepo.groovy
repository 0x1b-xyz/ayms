package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface ServiceRepo extends JpaRepository<Service, Long> {

    List<Service> findByClientId(Long clientId);

}
