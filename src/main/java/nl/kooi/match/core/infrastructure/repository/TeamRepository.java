package nl.kooi.match.core.infrastructure.repository;

import nl.kooi.match.core.infrastructure.entity.TeamEntity;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
}
