package com.example.addressbookapp.service;

import com.example.addressbookapp.Interface.IAuthenticationService;
import com.example.addressbookapp.dto.AuthUserDTO;
import com.example.addressbookapp.model.AuthUser;
import com.example.addressbookapp.dto.LoginDTO;
import com.example.addressbookapp.repository.AuthUserRepository;
import com.example.addressbookapp.util.EmailSenderService;
import com.example.addressbookapp.util.JwtToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public String login(LoginDTO loginDTO) {
        try {
            Optional<AuthUser> user = Optional.ofNullable(authUserRepository.findByEmail(loginDTO.getEmail()));
            if (user.isPresent()) {
                if (passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
                    String token = tokenUtil.createToken(user.get().getUserId());
                    try {
                        emailSenderService.sendEmail(
                                user.get().getEmail(),
                                "Logged in Successfully!",
                                "Hi " + user.get().getFirstName() + ",\n\nYou have successfully logged in into Greeting App!"
                        );
                    } catch (Exception emailException) {
                        System.err.println("Error sending email: " + emailException.getMessage());
                    }
                    return "Congratulations!! You have logged in successfully!"+token;
                } else {
                    return "Sorry! Email or Password is incorrect!";
                }
            } else {
                return "Sorry! Email or Password is incorrect!";
            }
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            return "Login failed due to a system error.";
        }
    }

    public String logout(Long userId, String token) {
        if (tokenUtil.isUserLoggedIn(userId, token)) {
            tokenUtil.logoutUser(userId);
            return "Successfully logged out!";
        }
        return "User not logged in!";
    }
}
