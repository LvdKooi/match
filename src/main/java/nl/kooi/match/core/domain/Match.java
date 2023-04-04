package nl.kooi.match.core.domain;

import lombok.Getter;
import nl.kooi.match.core.enums.MatchStatus;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Match {
    private Long id;

    private MatchStatus matchStatus;

    private String matchName;

    private Set<PlayerEvent> playerEvents = new HashSet<>();


}
