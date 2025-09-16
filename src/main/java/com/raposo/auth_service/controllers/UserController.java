package com.raposo.auth_service.controllers;


import com.raposo.auth_service.models.dto.RegisterRequestDTO;
import com.raposo.auth_service.models.dto.RegisterResponseDTO;
import com.raposo.auth_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest){
        try{
            RegisterResponseDTO registerResponse = userService.registerUser(registerRequest);
            return ResponseEntity.ok(registerResponse);
        } catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
