package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Company
import io.stiefel.ayms.domain.Employee
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter

/**
 * @author jason@stiefel.io
 */
class CompanyConverter implements GenericConverter {

    final Set<GenericConverter.ConvertiblePair> convertibleTypes = [
            new GenericConverter.ConvertiblePair(String, Employee),
            new GenericConverter.ConvertiblePair(Employee, String)
    ]

    @Autowired CompanyDao dao

    @Override
    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String == sourceType.type)
            return dao.find(Long.parseLong((String)source))
        return ((Company)source).id.toString()
    }
}
