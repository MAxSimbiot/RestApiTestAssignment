package com.test.assignment.domain.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class UserResponse {
  UUID id;
  String firstName;
  String lastName;
  String email;
  LocalDateTime birthDate;
  String address;
  String phoneNumber;
}
