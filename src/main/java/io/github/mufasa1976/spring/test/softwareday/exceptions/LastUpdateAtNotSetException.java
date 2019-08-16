package io.github.mufasa1976.spring.test.softwareday.exceptions;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class LastUpdateAtNotSetException extends RuntimeException {
  private final Class<? extends ResourceSupport> resourceClass;
  private final UUID reference;
}
