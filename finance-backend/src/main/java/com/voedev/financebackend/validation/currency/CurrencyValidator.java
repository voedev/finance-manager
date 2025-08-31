package com.voedev.financebackend.validation.currency;

import com.voedev.financebackend.model.enums.CurrencyType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext context) {
        return currency != null && Arrays.stream(CurrencyType.values())
                .map(Enum::name)
                .anyMatch(currency::equals);
    }
}
