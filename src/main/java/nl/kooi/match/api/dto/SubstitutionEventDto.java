package nl.kooi.match.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubstitutionEventDto extends PlayerEventRequestDto {

    @NotNull
    private Long substituteForPlayerId;
}
