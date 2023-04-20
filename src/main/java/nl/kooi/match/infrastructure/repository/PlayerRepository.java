package nl.kooi.match.infrastructure.repository;

import nl.kooi.match.infrastructure.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
}
