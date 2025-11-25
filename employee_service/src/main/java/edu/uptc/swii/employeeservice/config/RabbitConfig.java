package edu.uptc.swii.employeeservice.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ALERTS_EXCHANGE = "alerts.exchange";

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        // Cambia localhost por rabbitmq, que es el nombre del contenedor de RabbitMQ en
        // Docker
        CachingConnectionFactory factory = new CachingConnectionFactory("rabbitmq", 5672);

        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        return new RabbitTemplate(cf);
    }
}
