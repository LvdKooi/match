package nl.kooi.match.core.usecases.match;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.infrastructure.port.MatchDao;

import java.util.Optional;

@RequiredArgsConstructor
abstract class StateTransitioningMatchUseCase {

    protected final MatchDao matchDao;

    protected MatchUseCaseResponse handleStateTransition(Long matchId, MatchStatus newStatus) {
        return matchDao.findById(matchId)
                .map(match -> Optional.of(match).filter(m -> m.matchStatus().canTransitionTo(newStatus))
                        .map(m -> m.copyMatchWithStatus(newStatus))
                        .map(matchDao::update)
                        .map(m -> MatchUseCaseResponse.successful(m.matchStatus(), m.id(), match.matchName()))
                        .orElseGet(() -> MatchUseCaseResponse.statusUpdateNotAllowed(match.matchStatus(), matchId, match.matchName())))
                .orElseGet(MatchUseCaseResponse::notFound);
    }
}
