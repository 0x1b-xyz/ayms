package io.stiefel.ayms.repo;

import io.stiefel.ayms.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jason@stiefel.io
 */
public interface NoteRepo extends JpaRepository<Note, Long> {}
