package edu.uptc.swii.accessservice.config;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccessSagaPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AccessSagaPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void requestEmployeeValidation(String document, String actionType) {
        rabbitTemplate.convertAndSend(
                RabbitSagaConfig.SAGA_EXCHANGE,
                RabbitSagaConfig.CHECK_EMPLOYEE,
                Map.of(
                        "document", document,
                        "actionType", actionType // ENTRADA o SALIDA
                ));
    }
}
