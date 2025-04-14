package com.voedev.finance.model.dto.user.request;

import com.voedev.finance.model.enums.user.UserRole;
import com.voedev.finance.validation.StrongPassword;
import com.voedev.finance.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "email is required")
    @ValidEmail
    private String email;

    @StrongPassword
    @NotBlank(message = "password is required")
    private String password;

    @NotNull
    private UserRole role;
}
