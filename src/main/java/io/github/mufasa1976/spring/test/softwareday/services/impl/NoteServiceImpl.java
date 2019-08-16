package io.github.mufasa1976.spring.test.softwareday.services.impl;

import io.github.mufasa1976.spring.test.softwareday.assemblers.NoteResourceAssembler;
import io.github.mufasa1976.spring.test.softwareday.entities.NoteEntity;
import io.github.mufasa1976.spring.test.softwareday.exceptions.LastUpdateAtNotSetException;
import io.github.mufasa1976.spring.test.softwareday.exceptions.RowNotFoundException;
import io.github.mufasa1976.spring.test.softwareday.repositories.NoteRepository;
import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import io.github.mufasa1976.spring.test.softwareday.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
  private static final String LAST_MODIFICATION_DATE = "lastUpdatedAt";

  private final NoteRepository noteRepository;
  private final PagedResourcesAssembler<NoteEntity> pagedResourcesAssembler;
  private final NoteResourceAssembler noteResourceAssembler;
  private final EntityManager entityManager;

  @Override
  public PagedResources<NoteResource> findAllNotes(Pageable pageable) {
    return pagedResourcesAssembler.toResource(noteRepository.findAll(pageable), noteResourceAssembler);
  }

  @Override
  public Optional<NoteResource> findNoteByReference(@NotNull UUID reference) {
    return noteRepository.findOptionalByReference(reference)
                         .map(noteResourceAssembler::toResource);
  }

  @Override
  @Transactional
  public NoteResource create(@NotNull NoteResource noteResource) {
    return save(noteResource).apply(new NoteEntity());
  }

  private Function<NoteEntity, NoteResource> save(NoteResource noteResource) {
    return noteEntity -> {
      noteEntity.setSubject(noteResource.getSubject());
      noteEntity.setBody(noteResource.getBody());
      noteEntity.setLastUpdatedAt(noteResource.getLastUpdatedAt());

      noteEntity = noteRepository.saveAndFlush(noteEntity);
      entityManager.refresh(noteEntity);

      return noteResourceAssembler.toResource(noteEntity);
    };
  }

  @Override
  @Transactional
  public Optional<NoteResource> update(@NotNull UUID reference, @NotNull NoteResource noteResource) {
    checkIfLastModificationDateExists(reference, noteResource);
    return noteRepository.findOptionalByReference(reference)
                         .map(this::detach)
                         .map(save(noteResource));
  }

  private <T extends ResourceSupport> void checkIfLastModificationDateExists(UUID reference, T resource) {
    final BeanWrapper wrappedResource = new BeanWrapperImpl(resource);
    if (!wrappedResource.isReadableProperty(LAST_MODIFICATION_DATE) || wrappedResource.getPropertyValue(LAST_MODIFICATION_DATE) == null) {
      throw new LastUpdateAtNotSetException(resource.getClass(), reference);
    }
  }

  private <T> T detach(T entity) {
    Optional.ofNullable(entity)
            .ifPresent(entityManager::detach);
    return entity;
  }

  @Override
  @Transactional
  public void delete(@NotNull UUID reference, @NotNull LocalDateTime lastUpdatedAt) {
    noteRepository.findOptionalByReferenceAndLastUpdatedAt(reference, lastUpdatedAt)
                  .ifPresentOrElse(
                      noteRepository::delete,
                      () -> { throw RowNotFoundException.notFound(NoteResource.class, reference, lastUpdatedAt); });
  }
}
