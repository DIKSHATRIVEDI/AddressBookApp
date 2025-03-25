package com.example.addressbookapp.consumer;


import com.example.addressbookapp.util.EmailSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = "contactQueue")
    public void receiveMessage(String message) {
        System.out.println(" Received Message: " + message);
    }
    @Autowired
    EmailSenderService emailSenderService;

    @RabbitListener(queues = "userQueue")
    public void handleUserRegistration(String message) {
        System.out.println(" Received User Registration Event: " + message);
        emailSenderService.sendEmail("admin@gamil.com", "New User Registered", message);
    }

    @RabbitListener(queues = "loginQueue")
    public void handleUserLogin(String message) {
        System.out.println("Received Login Event: " + message);
        emailSenderService.sendEmail(message.substring(16), "User Logged In", message);
        System.out.println((message.substring(16)));
    }

    @RabbitListener(queues = "passwordQueue")
    public void handlePasswordReset(String message) {
        System.out.println("📩 Received Password Reset Event: " + message);
        emailSenderService.sendEmail(message.substring(16), "User Reset Password", message);
    }
}