package nl.kooi.match.core.usecases.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.InjuredPlayerRequest;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.enums.PlayerEventType;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.function.Predicate;

import static nl.kooi.match.core.enums.InjuryType.INJURED;

@Component
@RequiredArgsConstructor
@Validated
public class InjuredPlayerUseCase implements UseCaseHandler<InjuredPlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid InjuredPlayerRequest command) {
        var matchOptional = matchDao
                .findById(command.matchId());

        return matchOptional.isEmpty() ?
                PlayerUseCaseResponse.matchNotFound() :
                matchOptional
                        .filter(isPlayerCurrentlyPartOfMatch(command))
                        .map(match -> matchDao.addPlayerEvent(match.getId(), createPlayerEvent(command)))
                        .map(saveSuccessful -> PlayerUseCaseResponse.successful())
                        .orElseGet(PlayerUseCaseResponse::playerIsNotActiveInMatch);
    }

    private static PlayerEvent createPlayerEvent(InjuredPlayerRequest command) {
        return PlayerEvent.builder()
                .eventType(command.injuryType() == INJURED ? PlayerEventType.INJURED : PlayerEventType.LINED_UP)
                .minute(command.minute())
                .matchId(command.matchId())
                .playerId(command.playerId())
                .build();
    }

    private static Predicate<Match> isPlayerCurrentlyPartOfMatch(InjuredPlayerRequest command) {
        return match -> match.getPlayerEvents().stream()
                .filter(filterUntilMinuteInclusive(command.minute()))
                .filter(filterPlayerEventsById(command.playerId()))
                .noneMatch(isPlayerPartOfGame().negate()
                        .or(hasPlayerHadRedCard())
                        .or(isPlayerSubstituted()));
    }

    private static Predicate<PlayerEvent> filterPlayerEventsById(Long id) {
        return event -> event.getPlayerId().equals(id);
    }

    private static Predicate<PlayerEvent> filterUntilMinuteInclusive(int minute) {
        return event -> event.getMinute() <= minute;
    }

    private static Predicate<PlayerEvent> isPlayerPartOfGame() {
        return event -> event.getEventType() == PlayerEventType.LINED_UP;
    }

    private static Predicate<PlayerEvent> hasPlayerHadRedCard() {
        return event -> event.getEventType() == PlayerEventType.RED_CARD;
    }

    private static Predicate<PlayerEvent> isPlayerSubstituted() {
        return event -> event.getEventType() == PlayerEventType.SUBSTITUTED;
    }
}
