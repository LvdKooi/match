package nl.kooi.match.core.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nl.kooi.match.core.enums.MatchStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @CreationTimestamp
    private Instant creationTimestamp;

    @NotNull
    private Instant startTimestamp;
    private Instant endTimeStamp;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private MatchStatus matchStatus;

    @OneToMany
    private TeamEntity team1;
    @OneToMany
    private TeamEntity team2;

    @OneToMany
    private Set<PlayerEventEntity> events;
}
