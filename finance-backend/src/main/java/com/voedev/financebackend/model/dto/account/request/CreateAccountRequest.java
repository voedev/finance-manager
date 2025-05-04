package com.voedev.financebackend.model.dto.account.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    private String title;
    private Integer currency_id;
    private Integer user_id;

}
