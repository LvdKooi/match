package nl.kooi.match.core.domain;

import lombok.Data;
import nl.kooi.match.core.domain.exception.PlayerException;
import nl.kooi.match.core.enums.MatchStatus;
import nl.kooi.match.core.enums.PlayerEventType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Data
public class Match {
    private Long id;

    private MatchStatus matchStatus;

    private String matchName;

    private Set<PlayerEvent> playerEvents = new HashSet<>();

    public void addPLayerEvent(PlayerEvent event) {
        switch (event.getEventType()) {

            case INJURED -> handleInjuredEvent(event);

        }

    }

    private void handleInjuredEvent(PlayerEvent event) {
        Optional.of(this)
                .filter(isPlayerCurrentlyPartOfMatch(event))
                .map(match -> match.getPlayerEvents().add(event))
                .orElseThrow(PlayerException::notActiveInMatch);
    }

    private static Predicate<Match> isPlayerCurrentlyPartOfMatch(PlayerEvent event) {
        return match -> !match.getPlayerEvents().isEmpty() &&
                match.getPlayerEvents().stream()
                        .filter(filterUntilMinuteInclusive(event.getMinute()))
                        .filter(filterPlayerEventsById(event.getPlayerId()))
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
