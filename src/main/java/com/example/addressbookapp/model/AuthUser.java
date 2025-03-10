package com.example.addressbookapp.model;

import com.example.addressbookapp.dto.AuthUserDTO;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Table(name = "auth_user")
@Data
@NoArgsConstructor
@Entity
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String resetToken;

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthUser(AuthUserDTO userDTO) {
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword(); // Encrypt before saving
    }

}