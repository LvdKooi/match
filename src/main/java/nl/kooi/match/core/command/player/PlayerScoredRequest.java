package nl.kooi.match.core.command.player;

import jakarta.validation.constraints.NotNull;

public record PlayerScoredRequest(@NotNull Long playerId, @NotNull Long matchId, int minute) {
}
