package nl.kooi.match.core.usecases.match;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.match.AnnounceMatchRequest;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.exception.NotFoundException;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.port.TeamDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class AnnounceMatchUseCase implements UseCaseHandler<AnnounceMatchRequest, MatchUseCaseResponse> {

    private final MatchDao matchDao;
    private final TeamDao teamDao;

    @Override
    public MatchUseCaseResponse handle(@Valid AnnounceMatchRequest command) {
        try {
            var match = matchDao.createNewMatch(command.startTimestamp(), getTeam(command.idTeam1()), getTeam(command.idTeam2()));
            return MatchUseCaseResponse.successful(match.matchStatus(), match.id(), match.matchName());
        } catch (NotFoundException e) {
            return MatchUseCaseResponse.notExistingTeam();
        }
    }

    private Team getTeam(Long id) {
        return teamDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Team with id %d is not found", id)));
    }
}
