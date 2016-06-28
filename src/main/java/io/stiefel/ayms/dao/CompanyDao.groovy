package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * @author jason@stiefel.io
 */
@Repository
class CompanyDao extends AbstractDao<Company, Long> {}
