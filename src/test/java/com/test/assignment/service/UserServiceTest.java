package com.test.assignment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.assignment.domain.User;
import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import com.test.assignment.exception.EntityNotFoundException;
import com.test.assignment.repository.UserRepository;
import com.test.assignment.service.mapper.UserMapper;
import com.test.assignment.service.mapper.UserMapperImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository repository;
  @Spy private UserMapper mapper = new UserMapperImpl();
  @InjectMocks private UserService testable;

  @Test
  void testShouldCreateUserSuccessfully() {
    UUID testId = UUID.randomUUID();
    User requested = buildTestUser(testId);
    UserCreateRequest request = buildTestUserCreateRequest();

    when(repository.createUser(any(User.class))).thenReturn(requested);

    UserResponse expected = mapper.toResponse(requested);
    UserResponse actual = testable.createUser(request);
    assertThat(actual)
        .as("Created user response should match expected")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(u -> assertThat(u.getFirstName()).isEqualTo(expected.getFirstName()))
        .satisfies(u -> assertThat(u.getLastName()).isEqualTo(expected.getLastName()))
        .satisfies(u -> assertThat(u.getEmail()).isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));

    verify(repository).createUser(any(User.class));
    verify(mapper).toEntity(request);
    verify(mapper, atLeastOnce()).toResponse(requested);
  }

  @Test
  void testShouldUpdateUserSuccessfully() {
    UUID testId = UUID.randomUUID();
    User existing = buildTestUser(testId);
    User updated = existing.toBuilder().firstName("NewName").email("updated@mail.com").build();
    UserUpdateRequest request =
        buildTestUserUpdateRequest().toBuilder()
            .firstName("NewName")
            .email("updated@gmail.com")
            .build();

    when(repository.findUserById(any(UUID.class))).thenReturn(existing);
    when(repository.updateUser(any(UUID.class), any(User.class))).thenReturn(updated);

    UserResponse expected = mapper.toResponse(updated);
    UserResponse actual = testable.updateUser(testId, request);
    assertThat(actual)
        .as("Updated user response should match expected")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(u -> assertThat(u.getFirstName()).isEqualTo(expected.getFirstName()))
        .satisfies(u -> assertThat(u.getLastName()).isEqualTo(expected.getLastName()))
        .satisfies(u -> assertThat(u.getEmail()).isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));

    verify(repository).findUserById(testId);
    verify(repository).updateUser(any(UUID.class), any(User.class));
    verify(mapper).toEntity(testId, request);
    verify(mapper, atLeastOnce()).toResponse(updated);
  }

  @Test
  void testShouldThrowEntityNotFoundExceptionOnUpdateNonExistentUser() {
    UUID testId = UUID.randomUUID();

    UserUpdateRequest request =
        buildTestUserUpdateRequest().toBuilder()
            .firstName("NewName")
            .email("updated@gmail.com")
            .build();

    when(repository.findUserById(any(UUID.class))).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> testable.updateUser(testId, request));

    verify(repository).findUserById(testId);
  }

  @Test
  void testShouldReplaceUserSuccessfully() {
    UUID testId = UUID.randomUUID();
    User existing = buildTestUser(testId);
    User updated =
        existing.toBuilder()
            .firstName("NewName")
            .email("updated@mail.com")
            .address(null)
            .phoneNumber(null)
            .build();
    UserCreateRequest request =
        buildTestUserCreateRequest().toBuilder()
            .firstName("NewName")
            .email("updated@gmail.com")
            .address(null)
            .phoneNumber(null)
            .build();

    when(repository.findUserById(any(UUID.class))).thenReturn(existing);
    when(repository.replaceUser(any(UUID.class), any(User.class))).thenReturn(updated);

    UserResponse expected = mapper.toResponse(updated);
    UserResponse actual = testable.replaceUser(testId, request);
    assertThat(actual)
        .as("Updated user response should match expected")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(u -> assertThat(u.getFirstName()).isEqualTo(expected.getFirstName()))
        .satisfies(u -> assertThat(u.getLastName()).isEqualTo(expected.getLastName()))
        .satisfies(u -> assertThat(u.getEmail()).isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isNull())
        .satisfies(u -> assertThat(u.getAddress()).isNull());

    verify(repository).findUserById(testId);
    verify(repository).replaceUser(any(UUID.class), any(User.class));
    verify(mapper).toEntity(testId, request);
    verify(mapper, atLeastOnce()).toResponse(updated);
  }

  @Test
  void testShouldThrowEntityNotFoundExceptionOnReplaceNonExistentUser() {
    UUID testId = UUID.randomUUID();

    UserCreateRequest request =
        buildTestUserCreateRequest().toBuilder()
            .firstName("NewName")
            .email("updated@gmail.com")
            .address(null)
            .phoneNumber(null)
            .build();

    when(repository.findUserById(any(UUID.class))).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> testable.replaceUser(testId, request));

    verify(repository).findUserById(testId);
  }

  @Test
  void testShouldDeleteUserSuccessfully() {
    UUID testId = UUID.randomUUID();
    User existing = buildTestUser(testId);

    when(repository.findUserById(any(UUID.class))).thenReturn(existing);

    testable.deleteUser(testId);

    verify(repository).findUserById(testId);
    verify(repository).deleteUser(testId);
  }

  @Test
  void testShouldThrowEntityNotFoundExceptionOnDeleteNonExistentUser() {
    UUID testId = UUID.randomUUID();

    when(repository.findUserById(any(UUID.class))).thenReturn(null);

    assertThrows(EntityNotFoundException.class, () -> testable.deleteUser(testId));

    verify(repository).findUserById(testId);
  }

  @Test
  void testShouldFindUsersInBirthDateRangeSuccessfully() {
    UUID testId = UUID.randomUUID();
    User existing = buildTestUser(testId);
    List<UserResponse> expected = mapper.toResponses(List.of(existing));

    when(repository.findUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(List.of(existing));

    List<UserResponse> actual =
        testable.getUsersByBirthDateRange(LocalDateTime.MIN, LocalDateTime.MAX);
    assertThat(actual).isNotEmpty().hasSameSizeAs(expected).hasSameElementsAs(expected);

    verify(repository)
        .findUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    verify(mapper, atLeastOnce()).toResponses(any(List.class));
  }

  @Test
  void testShouldThrowEntityNotFoundExceptionFindUsersInBirthDateRangeThatDontExist() {
    when(repository.findUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(Collections.emptyList());

    assertThrows(
        EntityNotFoundException.class,
        () -> testable.getUsersByBirthDateRange(LocalDateTime.MIN, LocalDateTime.MAX));

    verify(repository)
        .findUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    verify(mapper, never()).toResponses(any(List.class));
  }

  private UserUpdateRequest buildTestUserUpdateRequest() {
    return UserUpdateRequest.builder()
        .firstName("TestFirstName")
        .lastName("TestLastName")
        .email("testUser@test.com")
        .birthDate(LocalDateTime.now().minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }

  private UserCreateRequest buildTestUserCreateRequest() {
    return UserCreateRequest.builder()
        .firstName("TestFirstName")
        .lastName("TestLastName")
        .email("testUser@test.com")
        .birthDate(LocalDateTime.now().minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }

  private User buildTestUser(UUID id) {
    return User.builder()
        .id(id)
        .firstName("TestFirstName")
        .lastName("TestLastName")
        .email("testUser@test.com")
        .birthDate(LocalDateTime.now().minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }
}
