package nl.kooi.match.core.usecases.match;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.command.match.StartMatchRequest;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Transactional
public class StartMatchUseCase extends StateTransitioningMatchUseCase implements UseCaseHandler<StartMatchRequest, MatchUseCaseResponse> {
    public StartMatchUseCase(MatchDao matchDao) {
        super(matchDao);
    }

    @Override
    public MatchUseCaseResponse handle(@Valid StartMatchRequest command) {
        return handleStateTransition(command.matchId(), MatchStatus.STARTED);
    }
}
