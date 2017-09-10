package demo.repository;

import demo.domain.PropertyHistory;
import demo.service.core.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyHistoryRepository extends JpaRepository<PropertyHistory, Code> {
}
