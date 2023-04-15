package nl.kooi.match.infrastructure.repository;

import nl.kooi.match.infrastructure.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
