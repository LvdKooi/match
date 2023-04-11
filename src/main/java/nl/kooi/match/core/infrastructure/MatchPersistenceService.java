package nl.kooi.match.core.infrastructure;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.infrastructure.mapper.Mapper;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.infrastructure.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Match save(Match match) {
        return Optional.of(matchRepository.save(mapper.map(match)))
                .map(mapper::map)
                .orElse(null);
    }
}
