package nl.kooi.match.core.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.enums.ResponseType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerUseCaseResponse {

    private final ResponseType responseType;

    public static PlayerUseCaseResponse playerIsNotActiveInMatch() {
        return new PlayerUseCaseResponse(ResponseType.PLAYER_NOT_ACTIVE_IN_MATCH);
    }

    public static PlayerUseCaseResponse matchNotFound() {
        return new PlayerUseCaseResponse(ResponseType.MATCH_NOT_FOUND);
    }

    public static PlayerUseCaseResponse successful() {
        return new PlayerUseCaseResponse(ResponseType.SUCCESSFULLY_PROCESSED);
    }

}
