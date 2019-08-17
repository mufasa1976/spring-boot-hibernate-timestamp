package io.github.mufasa1976.spring.test.softwareday.repositories;

import io.github.mufasa1976.spring.test.softwareday.entities.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
  Optional<NoteEntity> findOptionalByReference(UUID reference);

  Optional<NoteEntity> findOptionalByReferenceAndLastUpdatedAt(UUID reference, LocalDateTime lastUpdatedAt);
}
