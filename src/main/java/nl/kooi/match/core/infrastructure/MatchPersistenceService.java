package nl.kooi.match.core.infrastructure;

import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatchPersistenceService implements MatchDao {
    @Override
    public Optional<Match> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Match save(Match match) {
        return null;
    }
}
