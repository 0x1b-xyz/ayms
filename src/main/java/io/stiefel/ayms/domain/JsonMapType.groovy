package io.stiefel.ayms.domain

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SessionImplementor
import org.hibernate.usertype.UserType

import javax.persistence.Converter
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * @author jason@stiefel.io
 */
@Converter
class JsonMapType implements UserType {

    @Override
    int[] sqlTypes() {
        [Types.JAVA_OBJECT]
    }

    @Override
    Class returnedClass() {
        Map
    }

    @Override
    boolean equals(Object o, Object o1) throws HibernateException {
        o.equals(o1)
    }

    @Override
    int hashCode(Object o) throws HibernateException {
        o.hashCode()
    }

    @Override
    Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {

        String json = resultSet.getString(names[0])
        if (resultSet.wasNull())
            return null;

        new JsonSlurper().parseText(json)

    }

    @Override
    void nullSafeSet(PreparedStatement ps, Object value, int idx, SessionImplementor sessionImplementor) throws HibernateException, SQLException {

        if (value == null) {
            ps.setNull(idx, Types.OTHER)
            return;
        }

        String json = JsonOutput.toJson(value)
        ps.setObject(idx, json, Types.OTHER)

    }

    @Override
    Object deepCopy(Object o) throws HibernateException {
        new JsonSlurper().parseText(JsonOutput.toJson(o))
    }

    @Override
    boolean isMutable() {
        true
    }

    @Override
    Serializable disassemble(Object o) throws HibernateException {
        o
    }

    @Override
    Object assemble(Serializable serializable, Object o) throws HibernateException {
        serializable
    }

    @Override
    Object replace(Object o, Object o1, Object o2) throws HibernateException {
        o
    }

}
