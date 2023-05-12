package nl.kooi.match.core.command.match;

import jakarta.validation.constraints.NotNull;

public record ViewMatchUseCaseRequest(@NotNull Long matchId) {
}
