package com.test.assignment.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.test.assignment.validator.BirthDateValidator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = BirthDateValidator.class)
@Import(BirthDateValidatorTest.TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class BirthDateValidatorTest {

  @Autowired private BirthDateValidator testable;

  @Test
  void shouldReturnTrueOnNullValue() {
    assertTrue(testable.isValid(null, null));
  }

  @Test
  void shouldReturnTrueOnValidAgeValue() {
    assertTrue(testable.isValid(LocalDateTime.now().minusYears(18), null));
    assertTrue(testable.isValid(LocalDateTime.now().minusYears(25), null));
  }

  @Test
  void shouldReturnFalseOnLessThenSpecifiedInProperties() {
    assertFalse(testable.isValid(LocalDateTime.now().minusYears(10), null));
  }

  @TestConfiguration
  public static class TestConfig {
    @Bean
    PropertyPlaceholderConfigurer propConfig() {
      PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
      ppc.setLocation(new ClassPathResource("application-test.properties"));
      return ppc;
    }
  }
}
