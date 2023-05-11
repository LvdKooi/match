package nl.kooi.match.api.dto.player;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.kooi.match.enums.CardType;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisciplineEventDto extends PlayerEventRequestDto {

    @NotNull
    private CardType card;
}
