package com.voedev.financebackend.validation.account;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountStatusValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountStatus {
    String message() default "Invalid account status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}