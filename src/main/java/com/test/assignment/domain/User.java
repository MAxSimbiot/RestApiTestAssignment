package com.test.assignment.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
  UUID id;
  String firstName;
  String lastName;
  String email;
  LocalDateTime birthDate;
  String address;
  String phoneNumber;
}
