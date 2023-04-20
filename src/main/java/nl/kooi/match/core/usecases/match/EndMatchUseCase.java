package nl.kooi.match.core.usecases.match;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.kooi.match.core.command.match.EndMatchUseCaseRequest;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Transactional
public class EndMatchUseCase extends StateTransitioningMatchUseCase implements UseCaseHandler<EndMatchUseCaseRequest, MatchUseCaseResponse> {
    public EndMatchUseCase(MatchDao matchDao) {
        super(matchDao);
    }

    @Override
    public MatchUseCaseResponse handle(@Valid EndMatchUseCaseRequest command) {
        return handleStateTransition(command.matchId(), MatchStatus.FINISHED);
    }
}
