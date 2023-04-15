package nl.kooi.match.core.command;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AnnounceMatchRequest(@NotNull Instant startTimestamp,
                                   @NotNull Long idTeam1,
                                   @NotNull Long idTeam2) {
}
