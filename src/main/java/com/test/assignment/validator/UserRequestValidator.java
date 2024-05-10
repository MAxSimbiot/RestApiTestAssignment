package com.test.assignment.validator;

import com.test.assignment.exception.InvalidPathVariableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserRequestValidator {
  public static final String EMAIL_REGEX =
      "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  private final Validator validator;

  public <T> void validate(T request) {
    Set<ConstraintViolation<T>> violations = validator.validate(request);
    if (!violations.isEmpty()) {
      log.error(
          "Request failed validation: {}",
          violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.joining(". ")));
      throw new ConstraintViolationException(violations);
    }
  }

  public void validateRangeRequestParam(LocalDateTime from, LocalDateTime to) {
    if (from.isAfter(to)) {
      throw new InvalidPathVariableException("Invalid request! 'From' date should be before 'to'!");
    }
  }
}
