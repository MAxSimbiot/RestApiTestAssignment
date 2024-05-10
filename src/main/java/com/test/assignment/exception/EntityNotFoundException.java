package com.test.assignment.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(UUID id) {
    super("User not found by id=" + id);
  }

  public EntityNotFoundException(String message) {
    super(message);
  }
}
