package com.voedev.finance.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateUserRequestDto(

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Length(max = 3)
        String currency
) {
}
