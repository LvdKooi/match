package nl.kooi.match.core.command;

import jakarta.validation.constraints.NotNull;

public record LineUpPlayerRequest(@NotNull Long playerId, @NotNull Long matchId, int minute) {
}
