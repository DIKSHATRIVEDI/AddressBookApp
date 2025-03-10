package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAuthenticationService;
import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.model.AuthUser;
import com.example.addressbookapp.repository.AuthUserRepository;
import com.example.addressbookapp.util.EmailSenderService;
import com.example.addressbookapp.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements IAuthenticationService {
    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    JwtToken tokenUtil;

    @Autowired
    EmailSenderService emailSenderService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthUser register(AuthUserDTO userDTO) {

        try {
            AuthUser user = new AuthUser(userDTO);
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
            String token = tokenUtil.createToken(user.getUserId());
            authUserRepository.save(user);

            try {
                emailSenderService.sendEmail(
                        user.getEmail(),
                        "Registered in Greeting App",
                        "Hi " + user.getFirstName() + ",\n\nYou have been successfully registered!\n\nYour details:\n\n" +
                                "User Id: " + user.getUserId() + "\nFirst Name: " + user.getFirstName() + "\nLast Name: " + user.getLastName() +
                                "\nEmail: " + user.getEmail() + "\nToken: " + token
                );
            } catch (Exception emailException) {
                System.err.println("Error sending email: " + emailException.getMessage());
            }

            return user;
        } catch (Exception e) {
            System.err.println("Registration failed: " + e.getMessage());
            return null;
        }
    }
}
