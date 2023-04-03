package nl.kooi.match.core.infrastructure.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nl.kooi.match.core.enums.MatchStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

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

}
