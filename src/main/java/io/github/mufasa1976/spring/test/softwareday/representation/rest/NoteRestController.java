package io.github.mufasa1976.spring.test.softwareday.representation.rest;

import io.github.mufasa1976.spring.test.softwareday.Routes;
import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import io.github.mufasa1976.spring.test.softwareday.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@Validated
public class NoteRestController {
  private final NoteService noteService;

  @GetMapping(Routes.NOTES)
  public PagedResources<NoteResource> findAllNotes(Pageable pageable) {
    return noteService.findAllNotes(pageable);
  }

  @GetMapping(Routes.NOTE)
  public ResponseEntity<NoteResource> findNoteByReference(@PathVariable(Routes.Param.REFERENCE) UUID reference) {
    return noteService.findNoteByReference(reference)
                      .map(ResponseEntity::ok)
                      .orElseGet(ResponseEntity.notFound()::build);
  }

  @PostMapping(Routes.NOTES)
  public NoteResource create(@RequestBody @NotNull @Valid NoteResource noteResource) {
    return noteService.create(noteResource);
  }

  @PutMapping(Routes.NOTE)
  public ResponseEntity<NoteResource> update(@PathVariable(Routes.Param.REFERENCE) UUID reference, @RequestBody @NotNull @Valid NoteResource noteResource) {
    return noteService.update(reference, noteResource)
                      .map(ResponseEntity::ok)
                      .orElseGet(ResponseEntity.notFound()::build);
  }

  @DeleteMapping(Routes.NOTE)
  @ResponseStatus(NO_CONTENT)
  public void delete(
      @PathVariable(Routes.Param.REFERENCE) UUID reference,
      @RequestParam(Routes.Param.LAST_UPDATED_AT) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt) {
    noteService.delete(reference, lastUpdatedAt);
  }
}
