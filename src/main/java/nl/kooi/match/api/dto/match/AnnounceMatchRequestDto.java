package nl.kooi.match.api.dto.match;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AnnounceMatchRequestDto(@NotNull Instant startTimestamp,
                                      @NotNull Long idTeam1,
                                      @NotNull Long idTeam2) {
}
