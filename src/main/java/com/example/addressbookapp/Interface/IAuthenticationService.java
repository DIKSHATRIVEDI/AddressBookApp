package com.example.addressbookapp.Interface;

import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.dto.LoginDTO;
import com.example.addressbookapp.model.AuthUser;

public interface IAuthenticationService {
    AuthUser register(AuthUserDTO userDTO) throws Exception;
    String login(LoginDTO loginDTO);

}