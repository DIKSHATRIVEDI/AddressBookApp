package com.example.addressbookapp.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ContactAddedConsumer {

    @RabbitListener(queues = "contact.added")
    public void handleContactAdded(String email) {
        System.out.println("New contact added: " + email);
    }
}
