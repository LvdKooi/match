package nl.kooi.match.core.usecases.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.match.core.command.match.MatchUseCaseResponse;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.infrastructure.port.MatchDao;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
abstract class StateTransitioningMatchUseCase {

    protected final MatchDao matchDao;

    protected MatchUseCaseResponse handleStateTransition(Long matchId, MatchStatus newStatus) {
        return CompletableFuture.supplyAsync(() -> matchDao.findById(matchId)
                        .map(match -> match.advanceToStatus(newStatus))
                        .map(matchDao::update)
                        .map(match -> MatchUseCaseResponse.successful(newStatus, matchId, match.matchName()))
                        .orElseGet(MatchUseCaseResponse::notFound))
                .exceptionally(throwable -> handleException(throwable, newStatus, matchId))
                .join();
    }


    private static MatchUseCaseResponse handleException(Throwable throwable, MatchStatus newStatus, Long matchId) {
        return Optional.of(throwable)
                .filter(MatchStatusException.class::isInstance)
                .map(exc -> MatchUseCaseResponse.statusUpdateNotAllowed(newStatus, matchId, null))
                .orElseGet(() -> {
                    log.error(throwable.getMessage());
                    return MatchUseCaseResponse.fail();
                });
    }
}
