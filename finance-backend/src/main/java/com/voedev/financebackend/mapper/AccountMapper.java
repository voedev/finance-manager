package com.voedev.financebackend.mapper;

import com.voedev.financebackend.model.dto.account.response.AccountResponse;
import com.voedev.financebackend.model.entity.Account;
import com.voedev.financebackend.model.enums.AccountStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "currency.value", target = "currency")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    AccountResponse toAccountResponse(Account account);

    @Named("statusToString")
    default String statusToString(AccountStatus status) {
        return status != null ? status.name() : null;
    }
}
