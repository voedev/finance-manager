package com.voedev.financebackend.service;

import com.voedev.financebackend.model.dto.account.request.CreateAccountRequest;
import com.voedev.financebackend.model.dto.account.response.AccountResponse;

public interface AccountService {

    AccountResponse create(CreateAccountRequest createAccountRequest);
}
