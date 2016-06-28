package io.stiefel.ayms.dao

import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.lang.reflect.ParameterizedType

/**
 * @author jason@stiefel.io
 */
@Transactional
abstract class AbstractDao<E,K> {

    @PersistenceContext
    EntityManager em

    private final Class<? extends E> type;

    AbstractDao() {
        type = (Class)((ParameterizedType)getClass().genericSuperclass).actualTypeArguments[0]
    }

    void save(E entity) {
        em.persist(entity)
    }

    void remove(E entity) {
        em.remove(entity)
    }

    E find(K key) {
        em.find(type, key)
    }

    List<E> findAll() {
        em.createQuery("select e from ${type.simpleName} e").resultList
    }

}
