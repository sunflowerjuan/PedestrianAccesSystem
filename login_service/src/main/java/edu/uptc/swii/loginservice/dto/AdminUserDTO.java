package edu.uptc.swii.loginservice.dto;

import lombok.Data;

@Data
public class AdminUserDTO {
    private String username;
    private String password;
    private String email;
}
