package nl.kooi.match.core.usecases.match;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.AnnounceMatchRequest;
import nl.kooi.match.core.command.MatchUseCaseResponse;
import nl.kooi.match.core.domain.Team;
import nl.kooi.match.core.domain.exception.NotFoundException;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.infrastructure.port.TeamDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Validated
public class AnnounceMatchUseCase implements UseCaseHandler<AnnounceMatchRequest, MatchUseCaseResponse> {

    private final MatchDao matchDao;
    private final TeamDao teamDao;

    @Override
    public MatchUseCaseResponse handle(AnnounceMatchRequest command) {
        try {
            var match = matchDao.createNewMatch(command.startTimestamp(), getTeam(command.idTeam1()), getTeam(command.idTeam2()));
            return MatchUseCaseResponse.successful(match.matchStatus(), match.matchName());
        } catch (NotFoundException e) {
            return MatchUseCaseResponse.notExistingTeam();
        }
    }

    private Team getTeam(Long id) {
        return teamDao.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Team with id %d is not found", id)));
    }
}
