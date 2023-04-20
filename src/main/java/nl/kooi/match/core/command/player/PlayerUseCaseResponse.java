package nl.kooi.match.core.command.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.enums.ResponseType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class PlayerUseCaseResponse {

    private final ResponseType responseType;

    public static PlayerUseCaseResponse playerIsNotActiveInMatch() {
        return new PlayerUseCaseResponse(ResponseType.PLAYER_NOT_ACTIVE_IN_MATCH);
    }

    public static PlayerUseCaseResponse matchNotFound() {
        return new PlayerUseCaseResponse(ResponseType.MATCH_NOT_FOUND);
    }

    public static PlayerUseCaseResponse successful() {
        return new PlayerUseCaseResponse(ResponseType.PROCESSED_SUCCESSFULLY);
    }

    public static PlayerUseCaseResponse alreadyHadAYellowCard() {
        return new PlayerUseCaseResponse(ResponseType.ALREADY_HAD_A_YELLOW_CARD);
    }

    public static PlayerUseCaseResponse lineUpNotAllowed() {
        return new PlayerUseCaseResponse(ResponseType.LINE_UP_NOT_ALLOWED);
    }

    public static PlayerUseCaseResponse fail() {
        return new PlayerUseCaseResponse(ResponseType.PROCESSED_UNSUCCESSFULLY);
    }

    public static PlayerUseCaseResponse matchIsNotActive() {
        return new PlayerUseCaseResponse(ResponseType.MATCH_NOT_ACTIVE);
    }
}
