package com.voedev.finance.service.impl;

import com.voedev.finance.model.dto.user.request.AuthenticationRequest;
import com.voedev.finance.model.dto.user.request.RegisterRequest;
import com.voedev.finance.model.dto.user.response.AuthenticationResponse;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {



    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                // mapper
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
