package edu.uptc.swii.alertservice.events;

import edu.uptc.swii.alertservice.application.AlertService;
import edu.uptc.swii.alertservice.dto.AlertDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class AlertEventListener {

    private final AlertService alertService;

    public AlertEventListener(AlertService alertService) {
        this.alertService = alertService;
    }

    private void handle(Map<String, Object> msg) {
        AlertDTO dto = new AlertDTO();
        dto.setTimestamp(LocalDateTime.parse((String) msg.get("timestamp")));
        dto.setDescription((String) msg.get("description"));
        dto.setCode((String) msg.get("code"));
        dto.setEmployeeDocument((String) msg.get("employeeDocument"));
        alertService.registerAlert(dto);
    }

    @RabbitListener(queues = "alert.login.user_not_registered")
    public void loginUserNotRegistered(Map<String, Object> msg) {
        handle(msg);
    }

    @RabbitListener(queues = "alert.login.attempts_exceeded")
    public void loginAttemptsExceeded(Map<String, Object> msg) {
        handle(msg);
    }

    @RabbitListener(queues = "alert.access.employee_already_entered")
    public void employeeEntered(Map<String, Object> msg) {
        handle(msg);
    }

    @RabbitListener(queues = "alert.access.employee_already_left")
    public void employeeLeft(Map<String, Object> msg) {
        handle(msg);
    }

    @RabbitListener(queues = "alert.access.employee_not_exists")
    public void employeeNotExists(Map<String, Object> msg) {
        handle(msg);
    }
}
