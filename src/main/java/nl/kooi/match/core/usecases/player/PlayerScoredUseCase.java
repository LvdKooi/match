package nl.kooi.match.core.usecases.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.PlayerScoredRequest;
import nl.kooi.match.core.command.player.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerException;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Validated
public class PlayerScoredUseCase implements UseCaseHandler<PlayerScoredRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid PlayerScoredRequest command) {
        return matchDao
                .findById(command.matchId())
                .map(match -> handlePlayerEvent(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvent(Match match, PlayerScoredRequest command) {
        try {
            return Optional.ofNullable(command)
                    .map(PlayerScoredUseCase::createPlayerEvent)
                    .map(match::addPlayerEvent)
                    .map(matchDao::update)
                    .map(successful -> PlayerUseCaseResponse.successful())
                    .orElseGet(PlayerUseCaseResponse::fail);
        } catch (MatchStatusException matchStatusException) {
            return PlayerUseCaseResponse.matchIsNotActive();
        } catch (PlayerException playerException) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        }
    }

    private static PlayerEvent createPlayerEvent(PlayerScoredRequest command) {
        return PlayerEvent.builder()
                .eventType(PlayerEventType.SCORED)
                .matchId(command.matchId())
                .playerId(command.playerId())
                .minute(command.minute())
                .build();
    }
}
