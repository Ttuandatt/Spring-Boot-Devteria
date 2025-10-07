package com.deveria.identity.service.controller;

import com.deveria.identity.service.dto.request.UserCreateRequest;
import com.deveria.identity.service.entity.User;
import com.deveria.identity.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    User createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

}
