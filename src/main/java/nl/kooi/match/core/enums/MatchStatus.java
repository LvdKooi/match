package nl.kooi.match.core.enums;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public enum MatchStatus {

    FINISHED(Collections.emptySet()),
    STARTED(Set.of(FINISHED)),
    CANCELLED(Collections.emptySet()),
    POSTPONED(Set.of(STARTED)),
    ANNOUNCED(Set.of(STARTED, CANCELLED, POSTPONED));

    private final Set<MatchStatus> canAdvanceTo;
}
