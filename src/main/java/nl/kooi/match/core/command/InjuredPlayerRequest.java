package nl.kooi.match.core.command;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import nl.kooi.match.enums.InjuryType;

@Builder
public record InjuredPlayerRequest(@NotNull Long playerId,
                                   @NotNull Long matchId,
                                   int minute,
                                   @NotNull InjuryType injuryType) {
}
