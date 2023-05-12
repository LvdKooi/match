package nl.kooi.match.infrastructure;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.exception.NotFoundException;
import nl.kooi.match.infrastructure.entity.MatchEntity;
import nl.kooi.match.infrastructure.mapper.Mapper;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.repository.MatchRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchPersistenceService implements MatchDao {

    private final MatchRepository matchRepository;
    private final Mapper mapper;

    @Override
    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id).map(mapper::map);
    }

    @Override
    public boolean isPlayerPartOfMatch(Long playerId, Long matchId) {
        return matchRepository
                .findById(matchId)
                .stream()
                .flatMap(match -> Stream.of(match.getTeam1(), match.getTeam2()))
                .flatMap(team -> team.getPlayers().stream())
                .anyMatch(player -> player.getId().equals(playerId));
    }

    @Override
    @Transactional
    public Match update(Match match) {
        return matchRepository
                .findById(match.id())
                .map(m -> mapper.map(match, Pair.of(m.getTeam1(), m.getTeam2()), m.getCreationTimestamp()))
                .map(matchRepository::save)
                .map(success -> match)
                .orElseThrow(() -> new NotFoundException("Match not found"));
    }

    @Override
    @Transactional
    public Match moveToStatus(Long matchId, MatchStatus matchStatus) {
        return matchRepository.findById(matchId)
                .map(match -> {
                    match.setMatchStatus(matchStatus);
                    return match;
                })
                .map(matchRepository::save)
                .map(mapper::map)
                .orElseThrow(() -> new NotFoundException("Update for a match that is unknown"));
    }

    @Override
    @Transactional
    public Match createNewMatch(Instant startTimestamp, Team team1, Team team2) {
        return Optional.of(MatchEntity.builder()
                        .matchStatus(MatchStatus.ANNOUNCED)
                        .team1(mapper.map(team1))
                        .team2(mapper.map(team2))
                        .startTimestamp(startTimestamp)
                        .build())
                .map(matchRepository::save)
                .map(mapper::map)
                .orElse(null);
    }
}