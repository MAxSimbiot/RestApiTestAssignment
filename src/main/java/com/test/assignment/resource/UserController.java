package com.test.assignment.resource;

import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import com.test.assignment.exception.InvalidPathVariableException;
import com.test.assignment.service.UserService;
import com.test.assignment.validator.UserRequestValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private static final String ID_PATH_VAR = "/{id}";
  private final UserRequestValidator validator;
  private final UserService service;

  @GetMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserResponse>> getUsersByBirthDateRange(
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime from,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime to) {
    validator.validateRangeRequestParam(from, to);
    List<UserResponse> response = service.getUsersByBirthDateRange(from, to);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
    validator.validate(request);

    UserResponse response = service.createUser(request);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PatchMapping(
      value = ID_PATH_VAR,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable String id, @RequestBody UserUpdateRequest request) {
    validator.validate(request);
    UserResponse response = service.updateUser(extractId(id), request);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping(
      value = ID_PATH_VAR,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> replaceUser(
      @PathVariable String id, @RequestBody UserCreateRequest request) {
    validator.validate(request);
    UserResponse response = service.replaceUser(extractId(id), request);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(ID_PATH_VAR)
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    service.deleteUser(extractId(id));
    return ResponseEntity.ok().build();
  }

  protected UUID extractId(String pathVariable) {
    try {
      return UUID.fromString(pathVariable);
    } catch (IllegalArgumentException exception) {
      log.warn("Invalid path variable for request! Got=" + exception);
      throw new InvalidPathVariableException(pathVariable);
    }
  }
}
