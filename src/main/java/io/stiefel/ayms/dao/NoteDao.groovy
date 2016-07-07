package io.stiefel.ayms.dao

import io.stiefel.ayms.domain.Note
import org.springframework.stereotype.Repository

/**
 * @author jason@stiefel.io
 */
@Repository
class NoteDao extends AbstractDao<Note, Long> {}
