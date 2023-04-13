package nl.kooi.match.core.usecases.player;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.LineUpPlayerRequest;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.exception.LineUpNotAllowedException;
import nl.kooi.match.core.domain.exception.MatchStatusException;
import nl.kooi.match.core.enums.PlayerEventType;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Transactional
@Validated
public class LineUpPlayerUseCase implements UseCaseHandler<LineUpPlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid LineUpPlayerRequest command) {
        return matchDao.findById(command.matchId())
                .map(match -> handlePlayerEvent(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvent(Match match, LineUpPlayerRequest command) {
        try {
            match.addPLayerEvent(createPlayerEvent(command));
            matchDao.update(match);
            return PlayerUseCaseResponse.successful();
        } catch (LineUpNotAllowedException | MatchStatusException e) {
            return PlayerUseCaseResponse.lineUpNotAllowed();
        }
    }

    private static PlayerEvent createPlayerEvent(LineUpPlayerRequest command) {
        return PlayerEvent.builder()
                .eventType(PlayerEventType.LINED_UP)
                .minute(command.minute())
                .matchId(command.matchId())
                .playerId(command.playerId())
                .build();
    }
}
