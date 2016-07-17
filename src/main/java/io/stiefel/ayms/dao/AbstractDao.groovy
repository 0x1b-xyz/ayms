package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.AbstractEntity
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceContext
import java.lang.reflect.ParameterizedType

/**
 * @author jason@stiefel.io
 */
@Transactional
abstract class AbstractDao<K, E extends AbstractEntity<K>> {

    @PersistenceContext
    EntityManager em

    private final Class<K> keyType;
    private final Class<E> entityType;

    AbstractDao() {
        def typeArguments = ((ParameterizedType) getClass().genericSuperclass).actualTypeArguments
        keyType = (Class)typeArguments[0]
        entityType = (Class) typeArguments[1]
    }

    E save(E entity) {
        if (entity.id == null) {
            em.persist(entity)
            return entity
        } else
            return em.merge(entity)
    }

    void removeById(K key) {
        E entity = find(key);
        remove(entity);
    }

    void remove(E entity) {
        em.remove(entity)
    }

    E find(K key) {
        E entity = em.find(entityType, key)
        if (!entity)
            throw new EntityNotFoundException("${entityType}:${key}")
        entity
    }

    List<E> findAll() {
        em.createQuery("select e from ${entityType.simpleName} e").resultList
    }

}
