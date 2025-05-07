package com.voedev.financebackend.model.dto.account.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voedev.financebackend.validation.ValidCurrency;
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
public class CreateAccountRequest {

    @NotBlank(message = "The title field is required.")
    private String title;

    @NotBlank(message = "Currency field cannot be null.")
    @ValidCurrency
    private String currency;

    @NotNull(message = "A user ID is required.")
    @JsonProperty("user_id")
    private Long userId;
}
