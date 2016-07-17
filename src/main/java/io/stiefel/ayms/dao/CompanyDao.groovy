package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class CompanyDao extends AbstractDao<Long, Company> {}
