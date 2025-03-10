package com.example.addressbookapp.controller;

import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.dto.ForgetPasswordDTO;
import com.example.addressbookapp.dto.LoginDTO;
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

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO){
        String result=authenticationService.login(loginDTO);
        ResponseDTO responseUserDTO=new ResponseDTO("Login successfully!!",result);
        return  new ResponseEntity<>(responseUserDTO,HttpStatus.OK);
    }

    @PutMapping("/forgetPassword/{email}")
    public ResponseEntity<ResponseDTO> forgetPassword(@PathVariable String email,
                                                      @Valid @RequestBody ForgetPasswordDTO forgetPasswordDTO) {
        String response = authenticationService.forgetPassword(email, forgetPasswordDTO);
        return new ResponseEntity<>(new ResponseDTO(response, null), HttpStatus.OK);
    }

    @PutMapping("/resetPassword/{email}")
    public ResponseEntity<ResponseDTO> resetPassword(@PathVariable String email,
                                                     @RequestParam String currentPassword,
                                                     @RequestParam String newPassword) {
        String response = authenticationService.resetPassword(email, currentPassword, newPassword);
        return new ResponseEntity<>(new ResponseDTO(response, null), HttpStatus.OK);
    }
    
}