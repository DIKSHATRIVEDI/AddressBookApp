package com.example.addressbookapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue registrationQueue() {
        return new Queue("user.registration", true);
    }

    @Bean
    public Queue contactQueue() {
        return new Queue("contact.added", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("app.exchange");
    }

    @Bean
    public Binding registrationBinding(Queue registrationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(registrationQueue).to(exchange).with("user.register");
    }

    @Bean
    public Binding contactBinding(Queue contactQueue, DirectExchange exchange) {
        return BindingBuilder.bind(contactQueue).to(exchange).with("contact.add");
    }
}
