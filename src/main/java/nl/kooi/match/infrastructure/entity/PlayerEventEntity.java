package nl.kooi.match.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import nl.kooi.match.enums.PlayerEventType;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Include
    private PlayerEntity player;

    @ManyToOne
    @EqualsAndHashCode.Include
    private MatchEntity match;

    @Column(name = "event_minute")
    @EqualsAndHashCode.Include
    private int minute;

    @Enumerated(value = EnumType.STRING)
    @EqualsAndHashCode.Include
    private PlayerEventType eventType;
}
