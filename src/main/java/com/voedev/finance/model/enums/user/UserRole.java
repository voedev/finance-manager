package com.voedev.finance.model.enums.user;

import com.voedev.finance.model.enums.Privilege;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static com.voedev.finance.model.enums.Privilege.*;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_ADMIN(
            Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE, UPDATE_PRIVILEGE, DELETE_PRIVILEGE)
    ),
    ROLE_USER(
            Set.of(READ_PRIVILEGE, WRITE_PRIVILEGE)
    );

    private final Set<Privilege> privileges;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .toList();
    }
}
