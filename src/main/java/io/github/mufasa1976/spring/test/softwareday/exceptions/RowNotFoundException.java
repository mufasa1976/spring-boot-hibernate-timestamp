package io.github.mufasa1976.spring.test.softwareday.exceptions;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Value
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor(access = PRIVATE)
public class RowNotFoundException extends RuntimeException {
  public static RowNotFoundException notFound(Class<? extends ResourceSupport> resourceClass, UUID reference, LocalDateTime lastUpdatedAt) {
    return new RowNotFoundException(resourceClass, reference, lastUpdatedAt);
  }

  private final Class<? extends ResourceSupport> resourceClass;
  private final UUID reference;
  private final LocalDateTime lastUpdatedAt;
}
