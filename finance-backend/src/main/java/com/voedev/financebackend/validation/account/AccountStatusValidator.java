package com.voedev.financebackend.validation.account;

import com.voedev.financebackend.model.enums.AccountStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AccountStatusValidator implements ConstraintValidator<ValidAccountStatus, String> {

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        return status != null && Arrays.stream(AccountStatus.values())
                .map(Enum::name)
                .anyMatch(status::equals);
    }
}
