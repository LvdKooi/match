package nl.kooi.match.core.infrastructure;


import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.core.infrastructure.mapper.Mapper;
import nl.kooi.match.core.infrastructure.port.TeamDao;
import nl.kooi.match.core.infrastructure.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamPersistenceService implements TeamDao {

    private final TeamRepository teamRepository;
    private final Mapper mapper;
    @Override
    public Optional<Team> findById(Long teamId) {
        return teamRepository.findById(teamId).map(mapper::map);
    }
}
