package nl.kooi.match.core.usecases.match;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import nl.kooi.match.core.command.MatchUseCaseResponse;
import nl.kooi.match.core.command.PostponeMatchRequest;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Transactional
public class PostponeMatchUseCase extends StateTransitioningMatchUseCase implements UseCaseHandler<PostponeMatchRequest, MatchUseCaseResponse> {
    public PostponeMatchUseCase(MatchDao matchDao) {
        super(matchDao);
    }

    @Override
    public MatchUseCaseResponse handle(@Valid PostponeMatchRequest command) {
        return handleStateTransition(command.matchId(), MatchStatus.POSTPONED);
    }
}
