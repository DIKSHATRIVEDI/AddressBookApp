package com.example.addressbookapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
public class LoginDTO {

    @NotNull(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}