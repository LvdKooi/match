package nl.kooi.match.core.domain;

import lombok.Data;
import lombok.Getter;
import nl.kooi.match.core.enums.MatchStatus;

import java.util.HashSet;
import java.util.Set;

@Data
public class Match {
    private Long id;

    private MatchStatus matchStatus;

    private String matchName;

    private Set<PlayerEvent> playerEvents = new HashSet<>();


}
