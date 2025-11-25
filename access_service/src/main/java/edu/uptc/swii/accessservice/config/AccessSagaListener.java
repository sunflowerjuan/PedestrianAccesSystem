package edu.uptc.swii.accessservice.config;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import edu.uptc.swii.accessservice.application.AccessService;
import edu.uptc.swii.accessservice.events.AlertEventPublisher;

@Component
public class AccessSagaListener {

    private final AccessService accessService;
    private final AlertEventPublisher alertPublisher;

    public AccessSagaListener(AccessService accessService, AlertEventPublisher alertPublisher) {
        this.accessService = accessService;
        this.alertPublisher = alertPublisher;
    }

    @RabbitListener(queues = "saga.access.employee_exists")
    public void onEmployeeExists(Map<String, Object> msg) {
        String document = (String) msg.get("document");
        String action = (String) msg.get("actionType");

        if (action.equals("ENTRADA")) {
            accessService.finalizeCheckin(document);
        } else {
            accessService.finalizeCheckout(document);
        }
    }

    @RabbitListener(queues = "saga.access.employee_not_exists")
    public void onEmployeeNotExists(Map<String, Object> msg) {
        String document = (String) msg.get("document");

        // ALERTA YA IMPLEMENTADA EN TU SISTEMA
        alertPublisher.employeeNotExists(document);
    }
}
