package nl.kooi.match.infrastructure.port;

import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.enums.MatchStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

public interface MatchDao {

    Optional<Match> findById(Long id);

    Match update(Match match);


    Match moveToStatus(Long matchId, MatchStatus matchStatus);

    Match createNewMatch(Instant startTimestamp, Team team1, Team team2);
}
