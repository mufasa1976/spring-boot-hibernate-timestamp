package io.github.mufasa1976.spring.test.softwareday.representation.rest;

import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import io.github.mufasa1976.spring.test.softwareday.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
public class NoteRestController {
  private final NoteService noteService;

  @GetMapping
  public PagedResources<NoteResource> findAllNotes(Pageable pageable) {
    return noteService.findAllNotes(pageable);
  }

  @GetMapping("{reference}")
  public ResponseEntity<NoteResource> findNoteByReference(@PathVariable("reference") UUID reference) {
    return noteService.findNoteByReference(reference)
                      .map(ResponseEntity::ok)
                      .orElseGet(ResponseEntity.notFound()::build);
  }

  @PostMapping
  public NoteResource create(@RequestBody @NotEmpty NoteResource noteResource) {
    return noteService.create(noteResource);
  }

  @PutMapping("{reference}")
  public ResponseEntity<NoteResource> update(@PathVariable("reference") UUID reference, @RequestBody @NotEmpty NoteResource noteResource) {
    return noteService.update(reference, noteResource)
                      .map(ResponseEntity::ok)
                      .orElseGet(ResponseEntity.notFound()::build);
  }

  @DeleteMapping("{reference}")
  @ResponseStatus(NO_CONTENT)
  public void delete(
      @PathVariable("reference") UUID reference,
      @RequestParam("lastUpdatedAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdatedAt) {
    noteService.delete(reference, lastUpdatedAt);
  }
}
