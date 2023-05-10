package nl.kooi.match.api.dto.match;

import jakarta.validation.constraints.NotNull;
import nl.kooi.match.enums.MatchEventType;

public record MatchEventRequestDto(@NotNull MatchEventType eventType) {

}
