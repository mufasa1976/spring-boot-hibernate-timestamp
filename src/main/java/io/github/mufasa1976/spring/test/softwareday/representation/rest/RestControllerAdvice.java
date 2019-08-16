package io.github.mufasa1976.spring.test.softwareday.representation.rest;

import io.github.mufasa1976.spring.test.softwareday.exceptions.LastUpdateAtNotSetException;
import io.github.mufasa1976.spring.test.softwareday.exceptions.RowNotFoundException;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {
  @Value
  @Builder
  private static class RowNotFoundExceptionResponse {
    private Class<? extends ResourceSupport> resourceClass;
    private UUID reference;
    private LocalDateTime lastUpdatedAt;
  }

  @Value
  @Builder
  private static class LastModificationDateNotSetExceptionResponse {
    private Class<? extends ResourceSupport> resourceClass;
    private UUID reference;
  }

  @ExceptionHandler
  public ResponseEntity<String> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception) {
    log.error("Row already updated in another Session", exception);
    return ResponseEntity.status(CONFLICT)
                         .body("Row has already been updated in another Session");
  }

  @ExceptionHandler
  public ResponseEntity<RowNotFoundExceptionResponse> handleRowNotFoundException(RowNotFoundException exception) {
    log.error("Row '{}' of Resource {} not found (LastUpdatedAt: {})", exception.getReference(), exception.getResourceClass(), exception.getLastUpdatedAt());
    return ResponseEntity.status(NOT_FOUND)
                         .body(RowNotFoundExceptionResponse.builder()
                                                           .resourceClass(exception.getResourceClass())
                                                           .reference(exception.getReference())
                                                           .lastUpdatedAt(exception.getLastUpdatedAt())
                                                           .build());
  }

  @ExceptionHandler
  public ResponseEntity<LastModificationDateNotSetExceptionResponse> handleLastModificationDateNotSetException(LastUpdateAtNotSetException exception) {
    log.error("mandatory Field 'lastUpdatedAt' on {} (ID: {}) is empty or not available", exception.getResourceClass(), exception.getReference());
    return ResponseEntity.status(BAD_REQUEST)
                         .body(LastModificationDateNotSetExceptionResponse.builder()
                                                                          .resourceClass(exception.getResourceClass())
                                                                          .reference(exception.getReference())
                                                                          .build());
  }
}
