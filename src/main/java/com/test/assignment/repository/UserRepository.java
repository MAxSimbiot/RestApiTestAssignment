package com.test.assignment.repository;

import com.test.assignment.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  User createUser(User user);

  User updateUser(UUID id, User user);

  User replaceUser(UUID id, User user);

  void deleteUser(UUID id);

  User findUserById(UUID id);

  List<User> findUsersByBirthDateRange(LocalDateTime from, LocalDateTime to);
}
