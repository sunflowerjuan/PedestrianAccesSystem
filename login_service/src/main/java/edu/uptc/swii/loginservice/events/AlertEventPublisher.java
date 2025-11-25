package edu.uptc.swii.loginservice.events;

import edu.uptc.swii.loginservice.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class AlertEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AlertEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserNotFound(String username) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ALERTS_EXCHANGE,
                "login.user_not_registered",
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "description", "Intento de login con usuario inexistente: " + username,
                        "code", "LOGINUSRNOTREGISTERED",
                        "employeeDocument", ""));
    }

    public void publishAttemptsExceeded(String username) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ALERTS_EXCHANGE,
                "login.attempts_exceeded",
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "description", "Usuario bloqueado por múltiples intentos: " + username,
                        "code", "LOGINUSRATTEMPSEXCEEDED",
                        "employeeDocument", ""));
    }
}
