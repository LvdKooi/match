package nl.kooi.match.core.infrastructure;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.core.domain.exception.NotFoundException;
import nl.kooi.match.core.enums.MatchStatus;
import nl.kooi.match.core.infrastructure.entity.MatchEntity;
import nl.kooi.match.core.infrastructure.mapper.Mapper;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.infrastructure.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

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
    @Transactional
    public Match update(Match match) {
        return findById(match.id())
                .map(matchExists -> mapper.map(match))
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