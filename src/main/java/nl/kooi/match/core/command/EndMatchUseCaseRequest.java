package nl.kooi.match.core.command;

import jakarta.validation.constraints.NotNull;

public record EndMatchUseCaseRequest(@NotNull Long matchId) {
}
