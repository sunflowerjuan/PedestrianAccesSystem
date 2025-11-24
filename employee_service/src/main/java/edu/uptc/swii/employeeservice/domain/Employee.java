package edu.uptc.swii.employeeservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @Column(nullable = false, unique = true)
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Boolean status = true;
}
