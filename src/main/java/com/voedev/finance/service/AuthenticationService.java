package com.voedev.finance.service;


import com.voedev.finance.model.dto.user.request.AuthenticationRequest;
import com.voedev.finance.model.dto.user.request.RegisterRequest;
import com.voedev.finance.model.dto.user.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
