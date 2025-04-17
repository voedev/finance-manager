package com.voedev.finance.service;


import com.voedev.finance.model.dto.auth.request.AuthenticationRequest;
import com.voedev.finance.model.dto.auth.request.RegisterRequest;
import com.voedev.finance.model.dto.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
