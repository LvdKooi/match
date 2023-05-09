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

        var matchOptional = matchRepository.findById(match.id());

//        TODO: refactor me!

        if (matchOptional.isPresent()) {
            var matchEntity = matchOptional.get();
            var team1 = matchEntity.getTeam1();
            var team2 = matchEntity.getTeam2();
            return matchOptional.map(matchExists -> mapper.map(match, Pair.of(team1, team2)))
                    .map(matchRepository::save)
                    .map(saveSuccessful -> match)
                    .orElseThrow(() -> new NotFoundException("Update for a match that is unknown"));
        }
        return null;
    }

    @Override
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