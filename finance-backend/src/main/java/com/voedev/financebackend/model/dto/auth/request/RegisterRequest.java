package com.voedev.financebackend.model.dto.auth.request;

import com.voedev.financebackend.validation.StrongPassword;
import com.voedev.financebackend.validation.ValidEmail;
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
