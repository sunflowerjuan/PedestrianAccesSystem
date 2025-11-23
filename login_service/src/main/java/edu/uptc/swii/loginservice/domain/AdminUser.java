package edu.uptc.swii.loginservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "admin_users")
public class AdminUser {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;

    private int failedLoginAttempts = 0;
    private Long lockTime = null;

    //Multifactor
    private String otpCode;
    private Long otpCodeExpiry;
}
