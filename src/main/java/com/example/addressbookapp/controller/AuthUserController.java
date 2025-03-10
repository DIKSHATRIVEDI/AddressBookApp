package com.example.addressbookapp.controller;

import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.dto.ResponseDTO;
import com.example.addressbookapp.model.AuthUser;
import com.example.addressbookapp.service.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody AuthUserDTO userDTO) throws Exception{
        AuthUser user=authenticationService.register(userDTO);
        ResponseDTO responseUserDTO =new ResponseDTO("User details is submitted!",user);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }
    
}