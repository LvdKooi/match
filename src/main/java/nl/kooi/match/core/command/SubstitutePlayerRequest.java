package nl.kooi.match.core.command;

public record SubstitutePlayerRequest(Long playerId, Long substituteForPlayerId, int minute) {
}
