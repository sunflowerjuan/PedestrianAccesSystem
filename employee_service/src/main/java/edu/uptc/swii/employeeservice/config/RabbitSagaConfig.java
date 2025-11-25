package edu.uptc.swii.employeeservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitSagaConfig {

    public static final String SAGA_EXCHANGE = "access.saga.exchange";

    public static final String CHECK_EMPLOYEE = "saga.access.check_employee";
    public static final String EMPLOYEE_EXISTS = "saga.access.employee_exists";
    public static final String EMPLOYEE_NOT_EXISTS = "saga.access.employee_not_exists";

    @Bean
    public TopicExchange sagaExchange() {
        return new TopicExchange(SAGA_EXCHANGE, true, false);
    }

    /** Cola que recibe la petición del access-service */
    @Bean
    public Queue checkEmployeeQueue() {
        return new Queue(CHECK_EMPLOYEE, true);
    }

    @Bean
    public Binding checkEmployeeBinding() {
        return BindingBuilder.bind(checkEmployeeQueue())
                .to(sagaExchange())
                .with(CHECK_EMPLOYEE);
    }

    /** Cola de respuesta cuando SI existe */
    @Bean
    public Queue employeeExistsQueue() {
        return new Queue(EMPLOYEE_EXISTS, true);
    }

    @Bean
    public Binding employeeExistsBinding() {
        return BindingBuilder.bind(employeeExistsQueue())
                .to(sagaExchange())
                .with(EMPLOYEE_EXISTS);
    }

    /** Cola de respuesta cuando NO existe */
    @Bean
    public Queue employeeNotExistsQueue() {
        return new Queue(EMPLOYEE_NOT_EXISTS, true);
    }

    @Bean
    public Binding employeeNotExistsBinding() {
        return BindingBuilder.bind(employeeNotExistsQueue())
                .to(sagaExchange())
                .with(EMPLOYEE_NOT_EXISTS);
    }
}
