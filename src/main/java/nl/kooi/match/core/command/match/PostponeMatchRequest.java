package nl.kooi.match.core.command.match;

import jakarta.validation.constraints.NotNull;

public record PostponeMatchRequest(@NotNull Long matchId) {
}
