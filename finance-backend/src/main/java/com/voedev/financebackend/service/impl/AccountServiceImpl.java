package com.voedev.financebackend.service.impl;

import com.voedev.financebackend.exception.AccountAlreadyExistsException;
import com.voedev.financebackend.mapper.AccountMapper;
import com.voedev.financebackend.model.dto.account.request.BaseAccountRequest;
import com.voedev.financebackend.model.dto.account.request.CreateAccountRequest;
import com.voedev.financebackend.model.dto.account.request.UpdateAccountRequest;
import com.voedev.financebackend.model.dto.account.response.AccountResponse;
import com.voedev.financebackend.model.entity.Account;
import com.voedev.financebackend.model.entity.Currency;
import com.voedev.financebackend.model.entity.User;
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
        checkUniqAccount(request, user);
        Currency currency = getCurrency(request);

        Account account = Account.builder()
                .title(request.getTitle())
                .user(user)
                .currency(currency)
                .build();

        account = accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    public AccountResponse update(UpdateAccountRequest request) {
        User user = userService.getCurrentUser();
        checkUniqAccount(request, user);
        Currency currency = getCurrency(request);

    }

    private Currency getCurrency(BaseAccountRequest request) {
        return currencyRepository.findByValue(CurrencyType.valueOf(request.getCurrency()))
                .orElseThrow(() -> new IllegalArgumentException("Currency not found."));
    }

    private void checkUniqAccount(BaseAccountRequest request, User user) {
        user.getAccounts().stream()
                .filter(account -> account.getTitle().equals(request.getTitle()))
                .findFirst()
                .ifPresent(account -> {
                    log.info("Account" +
                            " with title {} already exists", account.getTitle());
                    throw new AccountAlreadyExistsException(request.getTitle(), "Account already exists.");
                });
    }


}
