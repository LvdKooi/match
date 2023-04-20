package nl.kooi.match.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.kooi.match.enums.InjuryType;

@Data
@EqualsAndHashCode(callSuper = true)
public class InjuryEventDto extends PlayerEventRequestDto {

    @NotNull
    private InjuryType injuryType;
}
