package nl.kooi.match.core.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import nl.kooi.match.core.enums.MatchStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
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

    @OneToOne
    private TeamEntity team1;
    @OneToOne
    private TeamEntity team2;

    @OneToMany
    private Set<PlayerEventEntity> events;
}
