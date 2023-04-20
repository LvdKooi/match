package nl.kooi.match.core.usecases.match;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.kooi.match.core.command.match.CancelMatchRequest;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Transactional
public class CancelMatchUseCase extends StateTransitioningMatchUseCase implements UseCaseHandler<CancelMatchRequest, MatchUseCaseResponse> {


    public CancelMatchUseCase(MatchDao matchDao) {
        super(matchDao);
    }

    @Override
    public MatchUseCaseResponse handle(@Valid CancelMatchRequest command) {
        return handleStateTransition(command.matchId(), MatchStatus.CANCELLED);
    }
}
