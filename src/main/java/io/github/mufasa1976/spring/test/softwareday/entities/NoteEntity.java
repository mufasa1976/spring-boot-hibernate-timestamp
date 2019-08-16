package io.github.mufasa1976.spring.test.softwareday.entities;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "notes")
@Data
@NoArgsConstructor // needed by Hibernate
@AllArgsConstructor(access = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class NoteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(NONE)
  private Long id;

  @NotNull
  @Column(updatable = false, unique = true)
  @Setter(NONE)
  @Type(type = "uuid-char")
  @Builder.Default
  private UUID reference = UUID.randomUUID();

  @NotNull
  @Size(max = 255)
  private String subject;

  @Lob
  private String body;

  @NotNull
  @Version
  @LastModifiedDate
  @Builder.Default
  private LocalDateTime lastUpdatedAt = LocalDateTime.now();
}
