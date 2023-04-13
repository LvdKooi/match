package nl.kooi.match.core.infrastructure.port;

import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.Team;

import java.time.Instant;
import java.util.Optional;

public interface MatchDao {

    Optional<Match> findById(Long id);

    Match update(Match match);

    Match createNewMatch(Instant startTimestamp, Team team1, Team team2);
}
