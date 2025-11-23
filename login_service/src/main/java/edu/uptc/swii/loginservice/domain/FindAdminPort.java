package edu.uptc.swii.loginservice.domain;

import java.util.Optional;

public interface FindAdminPort {
    Optional<AdminUser> findByUsername(String username);
}
