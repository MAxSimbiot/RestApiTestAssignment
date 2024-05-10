package com.test.assignment.domain.request;

import static com.test.assignment.validator.UserRequestValidator.EMAIL_REGEX;

import com.test.assignment.validator.ValidBirthDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserCreateRequest(
    @NotNull @Size(min = 2, max = 30) String firstName,
    @NotNull @Size(min = 2, max = 30) String lastName,
    @NotNull @Pattern(regexp = EMAIL_REGEX, message = "Email must be valid") String email,
    @NotNull @Past @ValidBirthDate LocalDateTime birthDate,
    String address,
    String phoneNumber) {}
