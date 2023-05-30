package nl.kooi.match.core.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerException;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Validated
public record Match(Long id,
                    MatchStatus matchStatus,
                    Instant startTimestamp,
                    Instant endTimestamp,
                    String matchName,
                    Set<PlayerEvent> playerEvents) {

    public Match {
        if (playerEvents == null) {
            playerEvents = new HashSet<>(
            );
        }
    }

    public static Match createMatch(MatchStatus status, Instant startTimestamp, String matchName) {
        return new Match(null, status, startTimestamp, null, matchName, null);
    }

    public Match advanceToStatus(MatchStatus newStatus) {
        return withMatch()
                .filter(match -> match.matchStatus().canTransitionTo(newStatus))
                .map(match -> new Match(match.id(), newStatus, match.startTimestamp, match.endTimestamp, match.matchName, match.playerEvents))
                .orElseThrow(() -> MatchStatusException.newStatusNotAllowed(matchName, matchStatus, newStatus));
    }

    @Override
    public Set<PlayerEvent> playerEvents() {
        return Set.copyOf(playerEvents);
    }

    public Match addPlayerEvent(@Valid @NotNull PlayerEvent event) {
        switch (event.getEventType()) {
            case INJURED, SUBSTITUTED, RED_CARD, SCORED -> {
                verifyIfMatchIsStarted();
                verifyIsCurrentlyPartOfMatch(event);
            }
            case LINED_UP -> verifyLineUpEvent(event);
            case YELLOW_CARD -> {
                verifyIfMatchIsStarted();
                verifyIsCurrentlyPartOfMatch(event);
                verifyYellowCardEvent(event);
            }
        }

        return new Match(this.id,
                this.matchStatus,
                this.startTimestamp,
                this.endTimestamp,
                this.matchName,
                updatedPlayerEvents(event));
    }

    private Set<PlayerEvent> updatedPlayerEvents(PlayerEvent event) {
        return Optional.of(playerEvents())
                .map(HashSet::new)
                .map(events -> removeFutureEvents(events, event.getPlayerId(), event.getMinute()))
                .map(events -> {
                    events.add(event);
                    return events;
                })
                .orElseGet(Collections::emptySet);
    }

    private Set<PlayerEvent> removeFutureEvents(Set<PlayerEvent> events, Long playerId, int afterMinute) {
        var updatedEvents = new HashSet<>(events);
        updatedEvents.removeIf(event -> event.getPlayerId().equals(playerId) && event.getMinute() > afterMinute);
        return updatedEvents;
    }

    private void verifyIfMatchIsStarted() {
        withMatch()
                .map(Match::matchStatus)
                .filter(status -> status == MatchStatus.STARTED)
                .orElseThrow(() -> MatchStatusException.matchIsNotActive(matchName));
    }

    private Optional<Match> withMatch() {
        return Optional.of(this);
    }

    private void filterMatchOnStatusOrElseThrow(Stream<MatchStatus> matchStatusses, Supplier<MatchStatusException> exceptionSupplier) {
        withMatch()
                .map(Match::matchStatus)
                .filter(status -> matchStatusses.anyMatch(st -> st == status))
                .orElseThrow(exceptionSupplier);
    }

    private void verifyYellowCardEvent(PlayerEvent event) {
        withMatch()
                .filter(hasPlayerAlreadyHadAYellowCard(event).negate())
                .orElseThrow(PlayerException::alreadyHadYellowCard);
    }

    private void verifyLineUpEvent(PlayerEvent event) {
        filterMatchOnStatusOrElseThrow(Stream.of(MatchStatus.STARTED, MatchStatus.ANNOUNCED), MatchStatusException::matchDoesntAcceptLineUps);

        withMatch()
                .filter((isPlayerCurrentlyPartOfMatch(event).and(hasPlayerBeenInjured(event)))
                        .or(hasPlayerNotPlayedInMatchYet(event)))
                .orElseThrow(PlayerException::lineUpNotAllowed);
    }

    private void verifyIsCurrentlyPartOfMatch(PlayerEvent event) {
        withMatch()
                .filter(isPlayerCurrentlyPartOfMatch(event))
                .orElseThrow(PlayerException::notActiveInMatch);
    }

    private static Predicate<Match> hasPlayerAlreadyHadAYellowCard(PlayerEvent event) {
        return match -> match.playerEvents().stream()
                .filter(filterPlayerEventsById(event.getPlayerId()))
                .filter(filterUntilMinuteInclusive(event.getMinute()))
                .anyMatch(playerEvent -> playerEvent.getEventType() == PlayerEventType.YELLOW_CARD);
    }

    private static Predicate<Match> hasPlayerBeenInjured(PlayerEvent event) {
        return match -> match.playerEvents().stream()
                .filter(filterPlayerEventsById(event.getPlayerId()))
                .filter(filterUntilMinuteInclusive(event.getMinute()))
                .max(Comparator.naturalOrder())
                .map(PlayerEvent::getEventType)
                .map(type -> type == PlayerEventType.INJURED)
                .orElse(false);
    }

    private static Predicate<Match> hasPlayerNotPlayedInMatchYet(PlayerEvent event) {
        return match -> match.playerEvents().isEmpty() ||
                match.playerEvents().stream().noneMatch(filterPlayerEventsById(event.getPlayerId()));
    }

    private static Predicate<Match> isPlayerCurrentlyPartOfMatch(PlayerEvent event) {
        return match -> match.playerEvents.stream().filter(filterUntilMinuteInclusive(event.getMinute()))
                .filter(filterPlayerEventsById(event.getPlayerId()))
                .anyMatch(isPlayerLinedUp()) &&
                match.playerEvents().stream()
                        .filter(filterUntilMinuteInclusive(event.getMinute()))
                        .filter(filterPlayerEventsById(event.getPlayerId()))
                        .noneMatch(hasPlayerHadRedCard()
                                .or(isPlayerSubstituted()));
    }

    private static Predicate<PlayerEvent> filterPlayerEventsById(Long id) {
        return event -> event.getPlayerId().equals(id);
    }

    private static Predicate<PlayerEvent> filterUntilMinuteInclusive(int minute) {
        return event -> event.getMinute() <= minute;
    }

    private static Predicate<PlayerEvent> isPlayerLinedUp() {
        return event -> event.getEventType() == PlayerEventType.LINED_UP;
    }

    private static Predicate<PlayerEvent> hasPlayerHadRedCard() {
        return event -> event.getEventType() == PlayerEventType.RED_CARD;
    }

    private static Predicate<PlayerEvent> isPlayerSubstituted() {
        return event -> event.getEventType() == PlayerEventType.SUBSTITUTED;
    }
}
