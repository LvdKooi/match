package nl.kooi.match.infrastructure.repository;

import nl.kooi.match.infrastructure.entity.TeamEntity;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
}
