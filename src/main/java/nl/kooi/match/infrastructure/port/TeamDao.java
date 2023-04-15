package nl.kooi.match.infrastructure.port;

import nl.kooi.match.core.domain.Team;

import java.util.Optional;

public interface TeamDao {

    Optional<Team> findById(Long teamId);
}
