package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.AbstractEntity
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter

import java.lang.reflect.ParameterizedType

/**
 * Simple converter implementation for entities.
 *
 * @author jason@stiefel.io
 */
abstract class AbstractConverter<K, E extends AbstractEntity<K>> implements GenericConverter {

    final Class<E> entityType
    final Class<K> keyType
    final Set<GenericConverter.ConvertiblePair> convertibleTypes

    AbstractConverter() {
        def types = ((ParameterizedType) getClass().annotatedSuperclass).actualTypeArguments
        keyType = (Class<K>)types[0]
        entityType = (Class<E>)types[1]
        convertibleTypes = [
                new GenericConverter.ConvertiblePair(entityType, String),
                new GenericConverter.ConvertiblePair(String, keyType)
        ]
    }

    abstract AbstractDao<K,E> getDao();

    /**
     * Overload this if your key type is not a {@link Long}.
     */
    K getKey(String source) {
        if (keyType != Long)
            throw new UnsupportedOperationException("Only Long key types are supported. Implement your own getKey")
        (K)Long.valueOf(source)
    }

    @Override
    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (String == sourceType.type)
            return dao.find(getKey(source.toString()))
        return ((E)source).id.toString()
    }

}
