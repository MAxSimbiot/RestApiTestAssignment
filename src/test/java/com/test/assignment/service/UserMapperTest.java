package com.test.assignment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.test.assignment.domain.User;
import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import com.test.assignment.service.mapper.UserMapper;
import com.test.assignment.service.mapper.UserMapperImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserMapperTest {
  private static final UUID USER_ID = UUID.fromString("34ae7f06-c206-4bc1-96c6-1a79d871f9eb");
  private static final LocalDateTime DATE_NOW = LocalDateTime.now();
  private final UserMapper testable = new UserMapperImpl();

  @Test
  void testShouldMapUserCreateRequestToUserEntity() {
    User expected = buildTestUser(USER_ID);
    UserCreateRequest request = buildTestUserCreateRequest();
    User actual = testable.toEntity(request);

    assertThat(actual)
        .as("UserRequest should be mapped correctly")
        .satisfies(u -> assertThat(u.getId()).as("Id should be generated").isNotNull())
        .satisfies(
            u ->
                assertThat(u.getFirstName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getFirstName()))
        .satisfies(
            u ->
                assertThat(u.getLastName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getLastName()))
        .satisfies(
            u ->
                assertThat(u.getEmail())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));
  }

  @Test
  void testShouldMapUserCreateRequestWithIdToUserEntity() {
    User expected = buildTestUser(USER_ID);
    UserCreateRequest request = buildTestUserCreateRequest();
    User actual = testable.toEntity(USER_ID, request);

    assertThat(actual)
        .as("UserRequest should be mapped correctly")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(
            u ->
                assertThat(u.getFirstName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getFirstName()))
        .satisfies(
            u ->
                assertThat(u.getLastName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getLastName()))
        .satisfies(
            u ->
                assertThat(u.getEmail())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));
  }

  @Test
  void testShouldMapUserUpdateRequestToUserEntity() {
    User expected = buildTestUser(USER_ID);
    UserUpdateRequest request = buildTestUserUpdateRequest();
    User actual = testable.toEntity(USER_ID, request);

    assertThat(actual)
        .as("UserRequest should be mapped correctly")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(
            u ->
                assertThat(u.getFirstName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getFirstName()))
        .satisfies(
            u ->
                assertThat(u.getLastName())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getLastName()))
        .satisfies(
            u ->
                assertThat(u.getEmail())
                    .as("Should be mapped and trimmed")
                    .isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));
  }

  @Test
  void testShouldMapUserEntityToUserResponse() {
    User user = buildTestUser(USER_ID);
    UserResponse expected = buildTestUserResponse();
    UserResponse actual = testable.toResponse(user);

    assertThat(actual)
        .as("UserRequest should be mapped correctly")
        .satisfies(u -> assertThat(u.getId()).isEqualTo(expected.getId()))
        .satisfies(u -> assertThat(u.getFirstName()).isEqualTo(expected.getFirstName()))
        .satisfies(u -> assertThat(u.getLastName()).isEqualTo(expected.getLastName()))
        .satisfies(u -> assertThat(u.getEmail()).isEqualTo(expected.getEmail()))
        .satisfies(u -> assertThat(u.getBirthDate()).isEqualTo(expected.getBirthDate()))
        .satisfies(u -> assertThat(u.getPhoneNumber()).isEqualTo(expected.getPhoneNumber()))
        .satisfies(u -> assertThat(u.getAddress()).isEqualTo(expected.getAddress()));
  }

  @Test
  void testShouldMapUserEntityListToUserResponseList() {
    List<User> users = List.of(buildTestUser(USER_ID));
    List<UserResponse> expected = List.of(buildTestUserResponse());
    List<UserResponse> actual = testable.toResponses(users);

    assertThat(actual)
        .as("List of users is mapped to list of responses correctly")
        .hasSameSizeAs(expected)
        .hasSameElementsAs(expected);
  }

  private UserResponse buildTestUserResponse() {
    return UserResponse.builder()
        .id(USER_ID)
        .firstName("TestFirstName")
        .lastName("TestLastName")
        .email("testUser@test.com")
        .birthDate(DATE_NOW.minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }

  private UserUpdateRequest buildTestUserUpdateRequest() {
    return UserUpdateRequest.builder()
        .firstName("TestFirstName")
        .lastName("TestLastName")
        .email("testUser@test.com")
        .birthDate(DATE_NOW.minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }

  private UserCreateRequest buildTestUserCreateRequest() {
    return UserCreateRequest.builder()
        .firstName("  TestFirstName ")
        .lastName("  TestLastName   ")
        .email("  testUser@test.com  ")
        .birthDate(DATE_NOW.minusYears(18))
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
        .birthDate(DATE_NOW.minusYears(18))
        .address("Brooklyn Street, 18")
        .phoneNumber("+3805553535")
        .build();
  }
}
