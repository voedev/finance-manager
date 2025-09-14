package com.voedev.financebackend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voedev.financebackend.model.entity.Account;
import com.voedev.financebackend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    @PostMapping
    public Account create(@RequestBody Account account) {
        return accountRepository.save(account);
    }


    @GetMapping
    public PagedModel<Account> getAll(@ParameterObject Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return new PagedModel<>(accounts);
    }

    @GetMapping("/{id}")
    public Account getOne(@PathVariable Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @GetMapping("/by-ids")
    public List<Account> getMany(@RequestParam List<Long> ids) {
        return accountRepository.findAllById(ids);
    }


    @PatchMapping("/{id}")
    public Account patch(@PathVariable Long id, @RequestBody JsonNode patchNode) throws IOException {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        objectMapper.readerForUpdating(account).readValue(patchNode);

        return accountRepository.save(account);
    }

    @PatchMapping
    public List<Long> patchMany(@RequestParam List<Long> ids, @RequestBody JsonNode patchNode) throws IOException {
        Collection<Account> accounts = accountRepository.findAllById(ids);

        for (Account account : accounts) {
            objectMapper.readerForUpdating(account).readValue(patchNode);
        }

        List<Account> resultAccounts = accountRepository.saveAll(accounts);
        return resultAccounts.stream()
                .map(Account::getId)
                .toList();
    }

    @DeleteMapping("/{id}")
    public Account delete(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            accountRepository.delete(account);
        }
        return account;
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        accountRepository.deleteAllById(ids);
    }
}
