package nl.kooi.match.core.command.player;

import jakarta.validation.constraints.NotNull;

public record LineUpPlayerRequest(@NotNull Long playerId, @NotNull Long matchId, int minute) {
}
