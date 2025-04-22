package com.voedev.financebackend.service;

import com.voedev.financebackend.model.dto.auth.request.AuthenticationRequest;
import com.voedev.financebackend.model.dto.auth.request.RegisterRequest;
import com.voedev.financebackend.model.dto.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
