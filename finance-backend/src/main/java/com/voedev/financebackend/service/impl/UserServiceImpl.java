package com.voedev.financebackend.service.impl;

import com.voedev.financebackend.model.entity.User;
import com.voedev.financebackend.model.enums.user.UserStatus;
import com.voedev.financebackend.repository.UserRepository;
import com.voedev.financebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // todo: need change UserStatus
        return userRepository.findByEmailAndStatus(currentUserEmail, UserStatus.VERIFY_EMAIL)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
