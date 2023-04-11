package nl.kooi.match.core.infrastructure.repository;

import nl.kooi.match.core.infrastructure.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
