package io.github.mufasa1976.spring.test.softwareday.entities;

import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.NONE;

@Entity
@Table(name = "notes")
@Data
public class NoteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(NONE)
  private Long id;

  @NotNull
  @Column(updatable = false, unique = true)
  @Setter(NONE)
  @Type(type = "uuid-char")
  private UUID reference = UUID.randomUUID();

  @NotNull
  @Size(max = 255)
  private String subject;

  @Lob
  private String body;

  @NotNull
  @Version
  private LocalDateTime lastUpdatedAt = LocalDateTime.now();
}
