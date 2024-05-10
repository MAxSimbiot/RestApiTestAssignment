package com.test.assignment.service;

import com.test.assignment.domain.User;
import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import com.test.assignment.exception.EntityNotFoundException;
import com.test.assignment.repository.UserRepository;
import com.test.assignment.service.mapper.UserMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper mapper;
  private final UserRepository repository;

  public UserResponse createUser(UserCreateRequest user) {
    User requested = mapper.toEntity(user);
    User created = repository.createUser(requested);
    log.info("User created successfully, id={}", created.getId());

    return mapper.toResponse(created);
  }

  public UserResponse updateUser(UUID id, UserUpdateRequest user) {
    User existing = repository.findUserById(id);
    if (existing == null) {
      log.warn("Could not update not existing user id=" + id);
      throw new EntityNotFoundException(id);
    }

    User requested = mapper.toEntity(id, user);
    User updated = repository.updateUser(id, buildUserUpdate(existing, requested));
    log.info("User updated successfully, id={}", updated.getId());

    return mapper.toResponse(updated);
  }

  public UserResponse replaceUser(UUID id, UserCreateRequest user) {
    User existing = repository.findUserById(id);
    if (existing == null) {
      log.warn("Could not replace not existing user id=" + id);
      throw new EntityNotFoundException(id);
    }

    User requested = mapper.toEntity(id, user);
    User updated = repository.replaceUser(id, requested);
    log.info("User replaced successfully, id={}", updated.getId());

    return mapper.toResponse(updated);
  }

  public void deleteUser(UUID id) {
    User existing = repository.findUserById(id);
    if (existing == null) {
      log.warn("Could not delete not existing user id=" + id);
      throw new EntityNotFoundException(id);
    }

    repository.deleteUser(id);
    log.info("User deleted successfully, id={}", id);
  }

  public List<UserResponse> getUsersByBirthDateRange(LocalDateTime from, LocalDateTime to) {
    List<User> users = repository.findUsersByBirthDateRange(from, to);
    if (users.isEmpty()) {
      log.warn("Could not find not users by range " + from + " - " + to);
      throw new EntityNotFoundException("Users not found by DateRange=" + from + " - " + to);
    }

    return mapper.toResponses(users);
  }

  private User buildUserUpdate(User existing, User requested) {
    return existing.toBuilder()
        .firstName(resolve(existing.getFirstName(), requested.getFirstName()))
        .lastName(resolve(existing.getLastName(), requested.getLastName()))
        .email(resolve(existing.getEmail(), requested.getEmail()))
        .birthDate(resolve(existing.getBirthDate(), requested.getBirthDate()))
        .phoneNumber(resolve(existing.getPhoneNumber(), requested.getPhoneNumber()))
        .address(resolve(existing.getAddress(), requested.getAddress()))
        .build();
  }

  private static <T> T resolve(T existing, T requested) {
    return Optional.ofNullable(requested).orElse(existing);
  }
}
