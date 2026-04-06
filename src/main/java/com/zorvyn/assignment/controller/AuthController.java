package com.zorvyn.assignment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zorvyn.assignment.dto.LoginRequestDTO;
import com.zorvyn.assignment.dto.LoginResponseDTO;
import com.zorvyn.assignment.dto.SignUpRequestDTO;
import com.zorvyn.assignment.dto.SignUpResponseDTO;
import com.zorvyn.assignment.entity.Role;
import com.zorvyn.assignment.entity.User;
import com.zorvyn.assignment.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setMessage("Login successful");
        response.setToken(token);

        return response;
    }

    @PostMapping("/signup")
    public SignUpResponseDTO signup(@RequestBody SignUpRequestDTO signUpRequest) {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());

        Role role = new Role();
        role.setName(signUpRequest.getRole());
        user.setRole(role);

        User savedUser = userService.signup(user);

        SignUpResponseDTO response = new SignUpResponseDTO();
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole().getName());

        return response;
    }
}