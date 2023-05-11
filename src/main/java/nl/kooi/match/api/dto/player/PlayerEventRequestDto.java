package nl.kooi.match.api.dto.player;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import nl.kooi.match.enums.PlayerEventType;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "eventType",
        use = JsonTypeInfo.Id.NAME,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DisciplineEventDto.class, name = "RED_CARD"),
        @JsonSubTypes.Type(value = DisciplineEventDto.class, name = "YELLOW_CARD"),
        @JsonSubTypes.Type(value = InjuryEventDto.class, name = "INJURED"),
        @JsonSubTypes.Type(value = PlayerLineUpEventDto.class, name = "LINED_UP"),
        @JsonSubTypes.Type(value = SubstitutionEventDto.class, name = "SUBSTITUTED"),
})
@Data
public abstract class PlayerEventRequestDto {

    @NotNull
    private PlayerEventType eventType;

    @NotNull
    private Long playerId;

    @PositiveOrZero
    private int minute;

}
