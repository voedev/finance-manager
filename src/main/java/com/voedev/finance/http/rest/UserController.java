package com.voedev.finance.http.rest;

import com.voedev.finance.model.dto.user.CreateUserRequestDto;
import com.voedev.finance.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @PostMapping
    public ResponseEntity<User> create(@RequestBody CreateUserRequestDto) {

    }
}
