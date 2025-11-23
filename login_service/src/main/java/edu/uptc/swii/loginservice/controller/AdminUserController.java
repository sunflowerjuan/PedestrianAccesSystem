package edu.uptc.swii.loginservice.controller;

import edu.uptc.swii.loginservice.application.AdminUserService;
import edu.uptc.swii.loginservice.dto.AdminUserDTO;
import edu.uptc.swii.loginservice.domain.AdminUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService service) {
        this.adminUserService = service;
    }

    @PostMapping
    public ResponseEntity<AdminUser> registerAdmin(@RequestBody AdminUserDTO dto) {
        AdminUser created = adminUserService.registerAdmin(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AdminUser> getByUsername(@PathVariable String username) {
        return adminUserService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
