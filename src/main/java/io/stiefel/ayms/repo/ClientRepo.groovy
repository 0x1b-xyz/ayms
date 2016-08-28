package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author jason@stiefel.io
 */
public interface ClientRepo extends JpaRepository<Client,Long> {

    List<Client> findByCompanyId(Long companyId);

    List<Client> findByCompanyIdAndAddressState(Long companyId, String addressState);

}
