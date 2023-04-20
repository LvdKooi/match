package nl.kooi.match.core.command.player;

import jakarta.validation.constraints.NotNull;

public record SubstitutePlayerRequest(@NotNull Long playerId, @NotNull Long matchId,
                                      @NotNull Long substituteForPlayerId, int minute) {
}
