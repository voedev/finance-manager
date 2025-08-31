package com.voedev.financebackend.model.dto.account.request;

import com.voedev.financebackend.validation.account.ValidAccountStatus;
import com.voedev.financebackend.validation.currency.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    @NotBlank
    @Min(value = 0, message = "The balance is not correct.")
    private Long id;

    @NotBlank(message = "The title field is required.")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;

    @NotNull
    @Min(value = 0, message = "The balance is not correct.")
    private BigDecimal balance;

    @NotBlank(message = "Currency field cannot be null.")
    @ValidCurrency
    private String currency;

    @NotBlank(message = "Status field cannot be null.")
    @ValidAccountStatus
    private String status;
}
