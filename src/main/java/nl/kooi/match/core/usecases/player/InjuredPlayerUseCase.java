package nl.kooi.match.core.usecases.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.InjuredPlayerRequest;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.core.enums.PlayerEventType;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import static nl.kooi.match.core.enums.InjuryType.INJURED;

@Component
@RequiredArgsConstructor
@Validated
public class InjuredPlayerUseCase implements UseCaseHandler<InjuredPlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid InjuredPlayerRequest command) {
        return matchDao
                .findById(command.matchId())
                .map(match -> handlePlayerEvent(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvent(Match match, InjuredPlayerRequest command) {
        try {
            match.addPLayerEvent(createPlayerEvent(command));
            matchDao.save(match);
            return PlayerUseCaseResponse.successful();
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        }
    }

    private static PlayerEvent createPlayerEvent(InjuredPlayerRequest command) {
        return PlayerEvent.builder()
                .eventType(command.injuryType() == INJURED ? PlayerEventType.INJURED : PlayerEventType.LINED_UP)
                .minute(command.minute())
                .matchId(command.matchId())
                .playerId(command.playerId())
                .build();
    }
}
