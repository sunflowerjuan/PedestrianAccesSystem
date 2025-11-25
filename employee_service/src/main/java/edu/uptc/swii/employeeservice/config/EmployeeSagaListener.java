package edu.uptc.swii.employeeservice.config;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import edu.uptc.swii.employeeservice.application.EmployeeService;

@Component
public class EmployeeSagaListener {

    private final EmployeeService employeeService;
    private final RabbitTemplate rabbitTemplate;

    public EmployeeSagaListener(EmployeeService employeeService, RabbitTemplate rabbitTemplate) {
        this.employeeService = employeeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "saga.access.check_employee")
    public void onEmployeeCheck(Map<String, Object> msg) {
        String document = (String) msg.get("document");
        String actionType = (String) msg.get("actionType");

        boolean exists = employeeService.findByDocument(document).isPresent();
        String routingKey = exists
                ? RabbitSagaConfig.EMPLOYEE_EXISTS
                : RabbitSagaConfig.EMPLOYEE_NOT_EXISTS;

        rabbitTemplate.convertAndSend(
                RabbitSagaConfig.SAGA_EXCHANGE,
                routingKey,
                Map.of(
                        "document", document,
                        "actionType", actionType));
    }
}
