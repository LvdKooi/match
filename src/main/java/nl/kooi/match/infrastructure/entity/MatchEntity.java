package nl.kooi.match.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nl.kooi.match.enums.MatchStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant creationTimestamp;

    @NotNull
    private Instant startTimestamp;
    private Instant endTimestamp;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private MatchStatus matchStatus;

    @OneToOne
    private TeamEntity team1;
    @OneToOne
    private TeamEntity team2;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerEventEntity> playerEvents;
}
