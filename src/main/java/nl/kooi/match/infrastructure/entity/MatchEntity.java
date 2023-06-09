package nl.kooi.match.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import nl.kooi.match.enums.MatchStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreationTimestamp()
    @EqualsAndHashCode.Include
    private Instant creationTimestamp;

    @NotNull
    @EqualsAndHashCode.Include
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
    @Builder.Default
    private Set<PlayerEventEntity> playerEvents = new HashSet<>();
}
