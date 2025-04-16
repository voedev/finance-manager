package com.voedev.finance.model.dto.user.request;

import com.voedev.finance.validation.StrongPassword;
import com.voedev.finance.validation.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "E-mail is required")
    @ValidEmail
    private String email;

    @StrongPassword
    @NotBlank(message = "Password is required")
    private String password;
}
