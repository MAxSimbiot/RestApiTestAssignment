package com.test.assignment.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDateTime> {
  private static final long DEFAULT_AGE = 18;

  @Value("${validation.minAllowedAge}")
  private long minAllowedAge;

  @Override
  public void initialize(ValidBirthDate constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    if (minAllowedAge == 0) {
      minAllowedAge = DEFAULT_AGE;
    }
  }

  @Override
  public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    var calculated = value.plusYears(minAllowedAge);
    return calculated.isBefore(LocalDateTime.now()) || calculated.isEqual(LocalDateTime.now());
  }
}
