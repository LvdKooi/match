package nl.kooi.match.core.usecases.player;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.InjuredPlayerRequest;
import nl.kooi.match.core.command.player.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static nl.kooi.match.enums.InjuryType.INJURED;

@Component
@RequiredArgsConstructor
@Transactional
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
            return Optional.ofNullable(command)
                    .map(InjuredPlayerUseCase::createPlayerEvent)
                    .map(match::addPlayerEvent)
                    .map(matchDao::update)
                    .map(success -> PlayerUseCaseResponse.successful())
                    .orElseGet(PlayerUseCaseResponse::fail);
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        } catch (MatchStatusException e) {
            return PlayerUseCaseResponse.matchIsNotActive();
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
