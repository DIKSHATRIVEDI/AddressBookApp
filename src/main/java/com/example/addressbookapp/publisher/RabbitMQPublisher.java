package com.example.addressbookapp.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void sendMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}