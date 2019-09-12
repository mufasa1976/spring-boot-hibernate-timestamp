package io.github.mufasa1976.spring.test.softwareday.resources;

import lombok.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor // needed by Jackson
@AllArgsConstructor(access = PRIVATE)
@Builder
@Relation(value = "note", collectionRelation = "notes")
public class NoteResource extends ResourceSupport {
  private UUID reference;
  @NotNull
  @Size(max = 255)
  private String subject;
  private String body;
  private LocalDateTime lastUpdatedAt;
}
