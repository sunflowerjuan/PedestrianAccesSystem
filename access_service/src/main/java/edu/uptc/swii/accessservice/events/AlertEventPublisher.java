package edu.uptc.swii.accessservice.events;

import edu.uptc.swii.accessservice.config.RabbitConfig;
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

    private void send(String key, String text, String code, String doc) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ALERTS_EXCHANGE,
                key,
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "description", text,
                        "code", code,
                        "employeeDocument", doc));
    }

    public void employeeNotExists(String doc) {
        send("access.employee_not_exists",
                "Intento de acceso con documento inexistente",
                "EMPLOYEENOTEXISTS", doc);
    }

    public void alreadyEntered(String doc) {
        send("access.employee_already_entered",
                "Empleado ya ingresó y no ha registrado salida",
                "EMPLOYEEALREADYENTERED", doc);
    }

    public void alreadyLeft(String doc) {
        send("access.employee_already_left",
                "Empleado ya registró salida",
                "EMPLOYEEALREADYLEFT", doc);
    }
}
