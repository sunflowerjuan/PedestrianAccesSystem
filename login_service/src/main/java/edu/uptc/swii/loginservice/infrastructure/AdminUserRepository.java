package edu.uptc.swii.loginservice.infrastructure;

import edu.uptc.swii.loginservice.domain.AdminUser;
import edu.uptc.swii.loginservice.domain.CreateAdminPort;
import edu.uptc.swii.loginservice.domain.FindAdminPort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AdminUserRepository extends MongoRepository<AdminUser, String>, CreateAdminPort, FindAdminPort {
    Optional<AdminUser> findByUsername(String username);

    @Override
    default AdminUser create(AdminUser adminUser) {
        return save(adminUser);
    }
}
