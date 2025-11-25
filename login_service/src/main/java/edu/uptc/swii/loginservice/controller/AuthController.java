package edu.uptc.swii.loginservice.controller;

import edu.uptc.swii.loginservice.application.AdminUserService;
import edu.uptc.swii.loginservice.domain.AdminUser;
import edu.uptc.swii.loginservice.dto.AdminUserDTO;
import edu.uptc.swii.loginservice.events.AlertEventPublisher;
import edu.uptc.swii.loginservice.security.PasetoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AdminUserService adminUserService;
    private final PasetoService pasetoService;
    private final AlertEventPublisher alertEventPublisher;

    public AuthController(AdminUserService adminUserService, PasetoService pasetoService,
            AlertEventPublisher alertEventPublisher) {
        this.alertEventPublisher = alertEventPublisher;
        this.adminUserService = adminUserService;
        this.pasetoService = pasetoService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminUserDTO dto) {
        Optional<AdminUser> maybe = adminUserService.findByUsername(dto.getUsername());
        if (maybe.isEmpty()) {
            alertEventPublisher.publishUserNotFound(dto.getUsername());
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        AdminUser user = maybe.get();

        // Bloqueo por intentos fallidos
        if (user.getLockTime() != null && user.getLockTime() > 0) {
            long now = System.currentTimeMillis();
            if (now - user.getLockTime() < 10 * 60 * 1000) { // 10 minutos
                return ResponseEntity.status(403)
                        .body("Usuario bloqueado por intentos fallidos. Intente en 10 minutos.");
            } else {
                // Desbloquear usuario tras 10 minutos
                user.setFailedLoginAttempts(0);
                user.setLockTime(null);
                adminUserService.save(user);
            }
        }

        if (!user.getPassword().equals(dto.getPassword())) {
            int intentos = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(intentos);
            if (intentos >= 3) {
                alertEventPublisher.publishAttemptsExceeded(user.getUsername());
                user.setLockTime(System.currentTimeMillis());
                adminUserService.save(user);
                return ResponseEntity.status(403).body("Usuario bloqueado por múltiples intentos fallidos.");
            } else {
                adminUserService.save(user);
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }
        }

        String otp = String.valueOf(new java.util.Random().nextInt(100000, 999999));
        long expiry = System.currentTimeMillis() + (2 * 60 * 1000); // 2 minutos
        user.setOtpCode(otp);
        user.setOtpCodeExpiry(expiry);
        adminUserService.save(user);

        System.out.println("OTP generado para " + user.getUsername() + ": " + otp); // Simulación
        return ResponseEntity.ok(Map.of(
                "otpRequired", true,
                "message", "Código enviado a la consola, ingréselo en el endpoint /auth/verify-otp.",
                "username", user.getUsername()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String otpCode = payload.get("otpCode");
        Optional<AdminUser> maybe = adminUserService.findByUsername(username);
        if (maybe.isEmpty()) {

            return ResponseEntity.status(401).body("Usuario no encontrado");
        }
        AdminUser user = maybe.get();
        if (user.getOtpCode() != null && user.getOtpCodeExpiry() != null) {
            long now = System.currentTimeMillis();
            if (now < user.getOtpCodeExpiry() && otpCode.equals(user.getOtpCode())) {
                // OTP correcto y vigente
                user.setOtpCode(null);
                user.setOtpCodeExpiry(null);
                user.setFailedLoginAttempts(0);
                user.setLockTime(null);
                adminUserService.save(user);

                String token = pasetoService.generateToken(user.getUsername(), "ADMIN");
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(403).body("OTP incorrecto o expirado");
            }
        }
        return ResponseEntity.status(403).body("No se solicitó OTP");
    }

}
