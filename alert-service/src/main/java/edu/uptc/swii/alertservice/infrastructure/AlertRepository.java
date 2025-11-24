package edu.uptc.swii.alertservice.infrastructure;

import edu.uptc.swii.alertservice.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByEmployeeDocument(String document);
    List<Alert> findByCode(String code);
}
