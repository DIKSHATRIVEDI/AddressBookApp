package com.example.addressbookapp.Interface;

import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.model.AuthUser;

public interface IAuthenticationService {
    AuthUser register(AuthUserDTO userDTO) throws Exception;

}