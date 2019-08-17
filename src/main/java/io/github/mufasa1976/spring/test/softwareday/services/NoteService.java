package io.github.mufasa1976.spring.test.softwareday.services;

import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface NoteService {
  String LAST_MODIFICATION_DATE = "lastUpdatedAt";

  PagedResources<NoteResource> findAllNotes(Pageable pageable);

  Optional<NoteResource> findNoteByReference(@NotNull UUID reference);

  NoteResource create(@NotNull NoteResource noteResource);

  Optional<NoteResource> update(@NotNull UUID reference, @NotNull NoteResource noteResource);

  void delete(@NotNull UUID reference, @NotNull LocalDateTime lastUpdatedAt);
}
