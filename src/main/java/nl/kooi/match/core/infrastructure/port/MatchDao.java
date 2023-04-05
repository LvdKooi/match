package nl.kooi.match.core.infrastructure.port;

import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;

import java.util.Optional;

public interface MatchDao {

    Optional<Match> findById(Long id);

    Match save(Match match);

}
