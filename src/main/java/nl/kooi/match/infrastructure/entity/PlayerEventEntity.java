package nl.kooi.match.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import nl.kooi.match.enums.PlayerEventType;

@Entity
@Getter
@Builder
public class PlayerEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PlayerEntity player;

    @ManyToOne
    private MatchEntity match;

    private int minute;

    @Enumerated(value = EnumType.STRING)
    private PlayerEventType eventType;
}
