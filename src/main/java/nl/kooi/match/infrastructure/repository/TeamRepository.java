package nl.kooi.match.infrastructure.repository;

import nl.kooi.match.core.domain.Team;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<TeamEntity, Long> {


    Optional<TeamEntity> findByName(String name);
}
