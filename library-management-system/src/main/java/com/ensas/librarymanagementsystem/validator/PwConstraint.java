package com.ensas.librarymanagementsystem.validator;

import com.nimbusds.jose.Payload;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PwConstraint {
    String message() default "Password must contain at least one uppercase letter, one number, and one special character";

    int min() default 8;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
