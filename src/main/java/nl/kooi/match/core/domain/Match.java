package nl.kooi.match.core.domain;

import nl.kooi.match.core.domain.exception.PlayerException;
import nl.kooi.match.core.enums.MatchStatus;
import nl.kooi.match.core.enums.PlayerEventType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;


public record Match(Long id, MatchStatus matchStatus, String matchName, Set<PlayerEvent> playerEvents) {

    public Match {
        if (playerEvents == null) {
            playerEvents = new HashSet<>(
            );
        }
    }

    public void addPLayerEvent(PlayerEvent event) {
        switch (event.getEventType()) {
            case INJURED, SUBSTITUTED, RED_CARD -> verifyIsCurrentlyPartOfMatch(event);
            case LINED_UP -> verifyLineUpEvent(event);
            case YELLOW_CARD -> {
                verifyIsCurrentlyPartOfMatch(event);
                verifyYellowCardEvent(event);
            }
        }

        this.playerEvents.add(event);
    }

    private void verifyYellowCardEvent(PlayerEvent event) {
        Optional.of(this)
                .filter(isPlayerCurrentlyPartOfMatch(event))
                .filter(hasPlayerAlreadyHadAYellowCard(event))
                .orElseThrow(PlayerException::alreadyHadYellowCard);
    }

    private void verifyLineUpEvent(PlayerEvent event) {
        Optional.of(this)
                .filter((isPlayerCurrentlyPartOfMatch(event).and(hasPlayerBeenInjured(event)))
                        .or(hasPlayerNotPlayedInMatchYet(event)))
                .orElseThrow(PlayerException::lineUpNotAllowed);
    }

    private void verifyIsCurrentlyPartOfMatch(PlayerEvent event) {
        Optional.of(this)
                .filter(isPlayerCurrentlyPartOfMatch(event))
                .orElseThrow(PlayerException::notActiveInMatch);
    }

    private static Predicate<Match> hasPlayerAlreadyHadAYellowCard(PlayerEvent event) {
        return match -> match.playerEvents().stream()
                .filter(filterPlayerEventsById(event.getPlayerId()))
                .anyMatch(playerEvent -> playerEvent.getEventType() == PlayerEventType.YELLOW_CARD);
    }

    private static Predicate<Match> hasPlayerBeenInjured(PlayerEvent event) {
        return match -> match.playerEvents().stream()
                .filter(filterPlayerEventsById(event.getPlayerId()))
                .filter(filterUntilMinuteInclusive(event.getMinute()))
                .min(Comparator.naturalOrder())
                .map(PlayerEvent::getEventType)
                .map(type -> type == PlayerEventType.INJURED)
                .orElse(false);
    }

    private static Predicate<Match> hasPlayerNotPlayedInMatchYet(PlayerEvent event) {
        return match -> match.playerEvents().isEmpty() ||
                match.playerEvents().stream().noneMatch(filterPlayerEventsById(event.getPlayerId()));
    }

    private static Predicate<Match> isPlayerCurrentlyPartOfMatch(PlayerEvent event) {
        return match -> !match.playerEvents().isEmpty() &&
                match.playerEvents().stream()
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
