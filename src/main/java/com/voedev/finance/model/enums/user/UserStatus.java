package com.voedev.finance.model.enums.user;

import lombok.Getter;

@Getter
public enum UserStatus {

    ACTIVE,
    BLOCKED,
    DELETED,
    VERIFY_EMAIL,
    UPDATE_CREDENTIALS
}
