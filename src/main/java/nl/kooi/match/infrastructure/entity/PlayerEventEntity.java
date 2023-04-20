package nl.kooi.match.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.kooi.match.enums.PlayerEventType;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PlayerEntity player;

    @ManyToOne
    private MatchEntity match;

    @Column(name = "event_minute")
    private int minute;

    @Enumerated(value = EnumType.STRING)
    private PlayerEventType eventType;
}
