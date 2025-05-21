package com.voedev.financebackend.service.impl;

import com.voedev.financebackend.handlers.exception.AccountAlreadyExistsException;
import com.voedev.financebackend.handlers.exception.AccountNotFoundException;
import com.voedev.financebackend.mapper.AccountMapper;
import com.voedev.financebackend.model.dto.account.request.CreateAccountRequest;
import com.voedev.financebackend.model.dto.account.request.UpdateAccountRequest;
import com.voedev.financebackend.model.dto.account.response.AccountResponse;
import com.voedev.financebackend.model.entity.Account;
import com.voedev.financebackend.model.entity.Currency;
import com.voedev.financebackend.model.entity.User;
import com.voedev.financebackend.model.enums.AccountStatus;
import com.voedev.financebackend.model.enums.CurrencyType;
import com.voedev.financebackend.repository.AccountRepository;
import com.voedev.financebackend.repository.CurrencyRepository;
import com.voedev.financebackend.service.AccountService;
import com.voedev.financebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountMapper accountMapper;
    private final UserService userService;

    @Transactional
    @Override
    public AccountResponse create(CreateAccountRequest request) {
        User user = userService.getCurrentUser();
        checkUniqAccount(request.getTitle(), user);
        Currency currency = getCurrency(request.getCurrency());

        Account account = Account.builder()
                .title(request.getTitle())
                .user(user)
                .currency(currency)
                .build();

        account = accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse update(UpdateAccountRequest request) {
        User user = userService.getCurrentUser();
        Currency currency = getCurrency(request.getCurrency());

        return user.getAccounts().stream()
                .filter(account -> account.getId().equals(request.getId()))
                .findFirst()
                .map(account -> {
                    if (!account.getTitle().equals(request.getTitle())) {
                        checkUniqAccount(request.getTitle(), user);
                    }

                    account.setTitle(request.getTitle());
                    account.setCurrency(currency);
                    account.setBalance(request.getBalance());
                    account.setStatus(AccountStatus.valueOf(request.getStatus()));

                    Account savedAccount = accountRepository.save(account);
                    return accountMapper.toAccountResponse(savedAccount);
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found.", request.getTitle()));
    }

    private Currency getCurrency(String currency) {
        return currencyRepository.findByValue(CurrencyType.valueOf(currency))
                .orElseThrow(() -> new IllegalArgumentException("Currency not found."));
    }

    private void checkUniqAccount(String title, User user) {
        user.getAccounts().stream()
                .filter(account -> account.getTitle().equals(title))
                .findFirst()
                .ifPresent(account -> {
                    log.info("{}: Account with this title already exists.", account.getTitle());
                    throw new AccountAlreadyExistsException(title, "Account already exists.");
                });
    }


}
