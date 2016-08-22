package io.stiefel.ayms.domain

import org.hibernate.dialect.PostgreSQLDialect

import java.sql.Types

/**
 * @author jason@stiefel.io
 */
class JsonPostgresDialect extends PostgreSQLDialect {

    JsonPostgresDialect() {
        registerColumnType(Types.JAVA_OBJECT, 'jsonb')
    }

}
