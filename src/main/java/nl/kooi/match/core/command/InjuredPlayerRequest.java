package nl.kooi.match.core.command;

import jakarta.validation.constraints.NotNull;
import nl.kooi.match.core.enums.InjuryType;

public record InjuredPlayerRequest(@NotNull Long playerId,
                                   @NotNull Long matchId,
                                   int minute,
                                   @NotNull InjuryType injuryType) {
}
