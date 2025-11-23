package edu.uptc.swii.loginservice.application;

import edu.uptc.swii.loginservice.domain.AdminUser;
import edu.uptc.swii.loginservice.domain.CreateAdminPort;
import edu.uptc.swii.loginservice.domain.FindAdminPort;
import edu.uptc.swii.loginservice.dto.AdminUserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminUserService {
    private final CreateAdminPort createAdminPort;
    private final FindAdminPort findAdminPort;

    public AdminUserService(CreateAdminPort createPort, FindAdminPort findPort) {
        this.createAdminPort = createPort;
        this.findAdminPort = findPort;
    }

    public AdminUser registerAdmin(AdminUserDTO dto) {
        AdminUser admin = new AdminUser();
        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        return createAdminPort.create(admin);
    }

    public Optional<AdminUser> findByUsername(String username) {
        return findAdminPort.findByUsername(username);
    }

    public void save(AdminUser adminUser) {
        createAdminPort.create(adminUser);
    }
}
