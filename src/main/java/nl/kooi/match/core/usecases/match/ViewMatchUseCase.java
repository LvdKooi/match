package nl.kooi.match.core.usecases.match;

import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.match.ViewMatchUseCaseRequest;
import nl.kooi.match.core.command.match.ViewMatchUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class ViewMatchUseCase implements UseCaseHandler<ViewMatchUseCaseRequest, ViewMatchUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public ViewMatchUseCaseResponse handle(ViewMatchUseCaseRequest command) {
        return matchDao.findById(command.matchId())
                .map(ViewMatchUseCase::map)
                .orElseGet(() -> ViewMatchUseCaseResponse.notFound(command.matchId()));
    }

    private static ViewMatchUseCaseResponse map(Match match) {
        return ViewMatchUseCaseResponse.successful(
                match.id(),
                match.matchName(),
                match.matchStatus(),
                match.startTimestamp(),
                match.endTimestamp(),
                match.playerEvents());
    }
}
