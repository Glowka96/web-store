package com.example.porfolio.webstorespring.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueEmailConstraintsValidator.class)
public @interface UniqueEmail {

    String message() default "This email is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
