package nl.kooi.match.core.command;

import java.time.Instant;

public record AnnounceMatchRequest(Instant startTimestamp,
                                   Long idTeam1,
                                   Long idTeam2) {
}
