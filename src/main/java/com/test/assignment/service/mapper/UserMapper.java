package com.test.assignment.service.mapper;

import com.test.assignment.domain.User;
import com.test.assignment.domain.request.UserCreateRequest;
import com.test.assignment.domain.request.UserUpdateRequest;
import com.test.assignment.domain.response.UserResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
  @Mappings(
      value = {
        @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())"),
        @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "trim"),
        @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "trim"),
        @Mapping(target = "email", source = "request.email", qualifiedByName = "trim"),
        @Mapping(target = "birthDate", source = "request.birthDate"),
        @Mapping(target = "address", source = "request.address"),
        @Mapping(target = "phoneNumber", source = "request.phoneNumber")
      })
  User toEntity(UserCreateRequest request);

  @Mappings(
      value = {
        @Mapping(target = "id", source = "requestedId"),
        @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "trim"),
        @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "trim"),
        @Mapping(target = "email", source = "request.email", qualifiedByName = "trim"),
        @Mapping(target = "birthDate", source = "request.birthDate"),
        @Mapping(target = "address", source = "request.address"),
        @Mapping(target = "phoneNumber", source = "request.phoneNumber")
      })
  User toEntity(UUID requestedId, UserCreateRequest request);

  @Mappings(
      value = {
        @Mapping(target = "id", source = "requestedId"),
        @Mapping(target = "firstName", source = "request.firstName", qualifiedByName = "trim"),
        @Mapping(target = "lastName", source = "request.lastName", qualifiedByName = "trim"),
        @Mapping(target = "email", source = "request.email", qualifiedByName = "trim"),
        @Mapping(target = "birthDate", source = "request.birthDate"),
        @Mapping(target = "address", source = "request.address"),
        @Mapping(target = "phoneNumber", source = "request.phoneNumber")
      })
  User toEntity(UUID requestedId, UserUpdateRequest request);

  @Mappings(
      value = {
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "firstName", source = "entity.firstName"),
        @Mapping(target = "lastName", source = "entity.lastName"),
        @Mapping(target = "email", source = "entity.email"),
        @Mapping(target = "birthDate", source = "entity.birthDate"),
        @Mapping(target = "address", source = "entity.address"),
        @Mapping(target = "phoneNumber", source = "entity.phoneNumber")
      })
  UserResponse toResponse(User entity);

  List<UserResponse> toResponses(List<User> users);

  @Named("trim")
  default String trim(String s) {
    return Optional.ofNullable(s).map(String::trim).orElse(null);
  }
}
