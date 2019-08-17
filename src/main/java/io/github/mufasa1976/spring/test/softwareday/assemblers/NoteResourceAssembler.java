package io.github.mufasa1976.spring.test.softwareday.assemblers;

import io.github.mufasa1976.spring.test.softwareday.entities.NoteEntity;
import io.github.mufasa1976.spring.test.softwareday.representation.rest.NoteRestController;
import io.github.mufasa1976.spring.test.softwareday.resources.NoteResource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class NoteResourceAssembler extends ResourceAssemblerSupport<NoteEntity, NoteResource> {
  public NoteResourceAssembler() {
    super(NoteRestController.class, NoteResource.class);
  }

  @Override
  public NoteResource toResource(NoteEntity noteEntity) {
    if (noteEntity == null) {
      return null;
    }

    final NoteResource noteResource = instantiateResource(noteEntity);
    noteResource.setReference(noteEntity.getReference());
    noteResource.setSubject(noteEntity.getSubject());
    noteResource.setBody(noteEntity.getBody());
    noteResource.setLastUpdatedAt(noteEntity.getLastUpdatedAt());

    noteResource.add(linkTo(methodOn(NoteRestController.class).findNoteByReference(noteEntity.getReference())).withSelfRel());
    return noteResource;
  }
}
