package nl.kooi.match.core.usecases.player;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.PlayerUseCaseResponse;
import nl.kooi.match.core.command.player.SubstitutePlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.usecases.UseCaseHandler;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.exception.LineUpNotAllowedException;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.port.TeamDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.stream.Stream;

import static nl.kooi.match.enums.ResponseType.PROCESSED_SUCCESSFULLY;

@Component
@RequiredArgsConstructor
@Transactional
@Validated
public class SubstitutePlayerUseCase implements UseCaseHandler<SubstitutePlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;
    private final TeamDao teamDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid SubstitutePlayerRequest command) {
        return matchDao.findById(command.matchId())
                .map(match -> handlePlayerEvents(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvents(Match match, SubstitutePlayerRequest command) {
        var substituteResponse = addSubstituteEvent(match, command);
        var lineUpResponse = addLineUpEvent(match, command);

        if (getResponseTypeStream(substituteResponse, lineUpResponse).allMatch(responseType -> responseType == PROCESSED_SUCCESSFULLY)) {
            matchDao.update(match);
            return PlayerUseCaseResponse.successful();
        }

        if (getResponseTypeStream(substituteResponse, lineUpResponse).noneMatch(responseType -> responseType == PROCESSED_SUCCESSFULLY)) {
            return PlayerUseCaseResponse.fail();
        }

        return Stream.of(substituteResponse, lineUpResponse)
                .filter(response -> response.getResponseType() != PROCESSED_SUCCESSFULLY)
                .findFirst()
                .orElseGet(PlayerUseCaseResponse::fail);
    }

    private static Stream<ResponseType> getResponseTypeStream(PlayerUseCaseResponse... responses) {
        return Arrays.stream(responses).map(PlayerUseCaseResponse::getResponseType);
    }

    private PlayerUseCaseResponse addSubstituteEvent(Match match, SubstitutePlayerRequest command) {
        try {
            match.addPLayerEvent(createSubstituteEvent(command));
            return PlayerUseCaseResponse.successful();
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        } catch (MatchStatusException e) {
            return PlayerUseCaseResponse.matchIsNotActive();
        }
    }

    private PlayerUseCaseResponse addLineUpEvent(Match match, SubstitutePlayerRequest command) {
        try {
            verifyIfPlayerPartOfTeams(match, command.substituteForPlayerId());
            match.addPLayerEvent(createLineUpEvent(command));
            return PlayerUseCaseResponse.successful();
        } catch (LineUpNotAllowedException e) {
            return PlayerUseCaseResponse.lineUpNotAllowed();
        }
    }

    private void verifyIfPlayerPartOfTeams(Match match, Long playerId) {
        var matches = match.matchName().split(" - ");

        if (!teamDao.isPlayerPartOfTeams(playerId, matches[0], matches[1])) {
            throw new LineUpNotAllowedException("Player is not part of the teams that are in match");
        }
    }

    private static PlayerEvent createSubstituteEvent(SubstitutePlayerRequest command) {
        return PlayerEvent.builder()
                .playerId(command.playerId())
                .eventType(PlayerEventType.SUBSTITUTED)
                .matchId(command.matchId())
                .minute(command.minute())
                .build();
    }

    private static PlayerEvent createLineUpEvent(SubstitutePlayerRequest command) {
        return PlayerEvent.builder()
                .playerId(command.substituteForPlayerId())
                .eventType(PlayerEventType.LINED_UP)
                .matchId(command.matchId())
                .minute(command.minute())
                .build();
    }
}
