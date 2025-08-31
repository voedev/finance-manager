package com.voedev.financebackend.controller;

import com.voedev.financebackend.model.dto.account.request.CreateAccountRequest;
import com.voedev.financebackend.model.dto.account.request.UpdateAccountRequest;
import com.voedev.financebackend.model.dto.account.response.AccountResponse;
import com.voedev.financebackend.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE') and hasAnyRole('ADMIN','USER')")
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse accountResponse = accountService.create(request);
        return ResponseEntity.ok().body(accountResponse);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_PRIVILEGE') and hasAnyRole('ADMIN','USER')")
    public ResponseEntity<AccountResponse> update(@Valid @RequestBody UpdateAccountRequest request) {
        AccountResponse accountResponse = accountService.update(request);
        return ResponseEntity.ok().body(accountResponse);
    }
}
