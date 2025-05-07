package com.voedev.financebackend.controller;

import com.voedev.financebackend.model.dto.account.request.CreateAccountRequest;
import com.voedev.financebackend.model.dto.account.response.AccountResponse;
import com.voedev.financebackend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE') and hasAnyRole('ADMIN','USER')")
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        AccountResponse accountResponse = accountService.create(createAccountRequest);
        return ResponseEntity.ok()
                .body(accountResponse);
    }
}
