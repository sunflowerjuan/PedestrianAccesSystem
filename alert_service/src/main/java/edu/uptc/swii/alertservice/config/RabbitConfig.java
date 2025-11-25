package edu.uptc.swii.alertservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ALERTS_EXCHANGE = "alerts.exchange";

    // Queues donde escuchará AlertService
    public static final String QUEUE_LOGIN_NOT_REGISTERED = "alert.login.user_not_registered";
    public static final String QUEUE_LOGIN_ATTEMPTS_EXCEEDED = "alert.login.attempts_exceeded";
    public static final String QUEUE_ACCESS_EMPLOYEE_ALREADY_ENTERED = "alert.access.employee_already_entered";
    public static final String QUEUE_ACCESS_EMPLOYEE_ALREADY_LEFT = "alert.access.employee_already_left";
    public static final String QUEUE_ACCESS_EMPLOYEE_NOT_EXISTS = "alert.access.employee_not_exists";

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("rabbitmq", 5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    @Bean
    public TopicExchange alertsExchange() {
        return new TopicExchange(ALERTS_EXCHANGE, true, false);
    }

    // Declaración de colas
    @Bean
    public Queue loginNotRegisteredQueue() {
        return new Queue(QUEUE_LOGIN_NOT_REGISTERED, true);
    }

    @Bean
    public Queue loginAttemptsExceededQueue() {
        return new Queue(QUEUE_LOGIN_ATTEMPTS_EXCEEDED, true);
    }

    @Bean
    public Queue employeeAlreadyEnteredQueue() {
        return new Queue(QUEUE_ACCESS_EMPLOYEE_ALREADY_ENTERED, true);
    }

    @Bean
    public Queue employeeAlreadyLeftQueue() {
        return new Queue(QUEUE_ACCESS_EMPLOYEE_ALREADY_LEFT, true);
    }

    @Bean
    public Queue employeeNotExistsQueue() {
        return new Queue(QUEUE_ACCESS_EMPLOYEE_NOT_EXISTS, true);
    }

    // Bindings
    @Bean
    public Binding loginNotRegisteredBinding() {
        return BindingBuilder.bind(loginNotRegisteredQueue())
                .to(alertsExchange())
                .with("login.user_not_registered");
    }

    @Bean
    public Binding loginAttemptsExceededBinding() {
        return BindingBuilder.bind(loginAttemptsExceededQueue())
                .to(alertsExchange())
                .with("login.attempts_exceeded");
    }

    @Bean
    public Binding employeeAlreadyEnteredBinding() {
        return BindingBuilder.bind(employeeAlreadyEnteredQueue())
                .to(alertsExchange())
                .with("access.employee_already_entered");
    }

    @Bean
    public Binding employeeAlreadyLeftBinding() {
        return BindingBuilder.bind(employeeAlreadyLeftQueue())
                .to(alertsExchange())
                .with("access.employee_already_left");
    }

    @Bean
    public Binding employeeNotExistsBinding() {
        return BindingBuilder.bind(employeeNotExistsQueue())
                .to(alertsExchange())
                .with("access.employee_not_exists");
    }
}
