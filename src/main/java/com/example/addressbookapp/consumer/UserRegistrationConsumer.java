package com.example.addressbookapp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationConsumer {

    @RabbitListener(queues = "user.registration")
    public void handleUserRegistration(String email) {
        System.out.println("New user registered: " + email);
    }
}
