package com.test.assignment.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import com.test.assignment.service.UserService;
import com.test.assignment.validator.UserRequestValidator;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(value = {UserControllerTest.TestConfiguration.class})
@PropertySource("classpath:application.properties")
class UserControllerTest {
  private static final UUID USER_ID = UUID.fromString("34ae7f06-c206-4bc1-96c6-1a79d871f9eb");
  private static final LocalDateTime DATE_NOW = LocalDateTime.now();
  private static final DateTimeFormatter FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
  private static final String USERS_API_PATH = "/users";
  private static final String ID_PATH_VAR = "/{id}";

  @Autowired private MockMvc mvc;
  @MockBean private UserService service;

  @org.springframework.boot.test.context.TestConfiguration
  static class TestConfiguration {
    @Bean
    public UserRequestValidator userRequestValidator() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      return new UserRequestValidator(factory.getValidator());
    }

    @Bean
    PropertyPlaceholderConfigurer propConfig() {
      PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
      ppc.setLocation(new ClassPathResource("application.properties"));
      return ppc;
    }
  }

  @Test
  void shouldCreateUserSuccessfully() throws Exception {
    when(service.createUser(any(UserCreateRequest.class))).thenReturn(buildTestUserResponse());
    String bDayString = DATE_NOW.minusYears(18).toString();

    mvc.perform(
            MockMvcRequestBuilders.post(USERS_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(bDayString)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Bob"))
        .andExpect(jsonPath("$.lastName").value("John"))
        .andExpect(jsonPath("$.email").value("bobJohn@email.com"))
        .andExpect(jsonPath("$.birthDate").value(DATE_NOW.minusYears(18).format(FORMAT)))
        .andExpect(jsonPath("$.address").value("Some Random Street, 12"))
        .andExpect(jsonPath("$.phoneNumber").value("+380775553535"));
  }

  @Test
  void shouldRejectInvalidUserCreateRequest() throws Exception {
    when(service.createUser(any(UserCreateRequest.class))).thenReturn(buildTestUserResponse());
    String invalidBDayString = DATE_NOW.minusYears(10).toString();
    String validBDayString = DATE_NOW.minusYears(19).toString();

    mvc.perform(
            MockMvcRequestBuilders.post(USERS_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(validBDayString)))
        .andExpect(status().isBadRequest());

    mvc.perform(
            MockMvcRequestBuilders.post(USERS_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(invalidBDayString)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateUserSuccessfully() throws Exception {
    when(service.updateUser(any(UUID.class), any(UserUpdateRequest.class)))
        .thenReturn(buildTestUserResponse());
    String bDayString = DATE_NOW.minusYears(18).toString();

    mvc.perform(
            MockMvcRequestBuilders.patch(USERS_API_PATH + ID_PATH_VAR, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(bDayString)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Bob"))
        .andExpect(jsonPath("$.lastName").value("John"))
        .andExpect(jsonPath("$.email").value("bobJohn@email.com"))
        .andExpect(jsonPath("$.birthDate").value(DATE_NOW.minusYears(18).format(FORMAT)))
        .andExpect(jsonPath("$.address").value("Some Random Street, 12"))
        .andExpect(jsonPath("$.phoneNumber").value("+380775553535"));
  }

  @Test
  void shouldReplaceUserSuccessfully() throws Exception {
    when(service.replaceUser(any(UUID.class), any(UserCreateRequest.class)))
        .thenReturn(buildTestUserResponse());
    String bDayString = DATE_NOW.minusYears(18).toString();

    mvc.perform(
            MockMvcRequestBuilders.put(USERS_API_PATH + ID_PATH_VAR, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(bDayString)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName").value("Bob"))
        .andExpect(jsonPath("$.lastName").value("John"))
        .andExpect(jsonPath("$.email").value("bobJohn@email.com"))
        .andExpect(jsonPath("$.birthDate").value(DATE_NOW.minusYears(18).format(FORMAT)))
        .andExpect(jsonPath("$.address").value("Some Random Street, 12"))
        .andExpect(jsonPath("$.phoneNumber").value("+380775553535"));
  }

  @Test
  void shouldRejectReplaceInvalidUser() throws Exception {
    String invalidBDayString = DATE_NOW.minusYears(10).toString();
    String validBDayString = DATE_NOW.minusYears(19).toString();

    mvc.perform(
            MockMvcRequestBuilders.put(USERS_API_PATH + ID_PATH_VAR, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(validBDayString)))
        .andExpect(status().isBadRequest());

    mvc.perform(
            MockMvcRequestBuilders.put(USERS_API_PATH + ID_PATH_VAR, USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(invalidBDayString)))
        .andExpect(status().isBadRequest());

    mvc.perform(
            MockMvcRequestBuilders.put(USERS_API_PATH + ID_PATH_VAR, "random string")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(
                    """
            {
                "firstName": "Bob",
                "lastName": "John",
                "email": "bobJohn@email.com",
                "birthDate": "%s",
                "address": "Some Random Street, 12",
                "phoneNumber": "+380775553535"
            }
            """
                        .formatted(validBDayString)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeleteUserSuccessfully() throws Exception {
    mvc.perform(MockMvcRequestBuilders.delete(USERS_API_PATH + ID_PATH_VAR, USER_ID))
        .andExpect(status().isOk());
  }

  @Test
  void shouldFindUsersByBirthDateRangeSuccessfully() throws Exception {
    when(service.getUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(List.of(buildTestUserResponse()));

    mvc.perform(
            MockMvcRequestBuilders.get(USERS_API_PATH)
                .param("from", DATE_NOW.minusYears(20).toString())
                .param("to", DATE_NOW.minusYears(5).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].firstName").value("Bob"))
        .andExpect(jsonPath("$[0].lastName").value("John"))
        .andExpect(jsonPath("$[0].email").value("bobJohn@email.com"))
        .andExpect(jsonPath("$[0].birthDate").value(DATE_NOW.minusYears(18).format(FORMAT)))
        .andExpect(jsonPath("$[0].address").value("Some Random Street, 12"))
        .andExpect(jsonPath("$[0].phoneNumber").value("+380775553535"));
  }

  @Test
  void shouldRejectFindRequestWithInvalidDateRange() throws Exception {
    when(service.getUsersByBirthDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(List.of(buildTestUserResponse()));

    mvc.perform(
            MockMvcRequestBuilders.get(USERS_API_PATH)
                .param("from", DATE_NOW.minusYears(5).toString())
                .param("to", DATE_NOW.minusYears(10).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  private UserResponse buildTestUserResponse() {
    return UserResponse.builder()
        .id(USER_ID)
        .firstName("Bob")
        .lastName("John")
        .email("bobJohn@email.com")
        .birthDate(DATE_NOW.minusYears(18))
        .address("Some Random Street, 12")
        .phoneNumber("+380775553535")
        .build();
  }
}
