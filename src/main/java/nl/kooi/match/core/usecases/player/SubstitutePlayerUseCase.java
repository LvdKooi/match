package nl.kooi.match.core.usecases.player;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.PlayerUseCaseResponse;
import nl.kooi.match.core.command.SubstitutePlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.domain.exception.LineUpNotAllowedException;
import nl.kooi.match.core.domain.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.core.enums.PlayerEventType;
import nl.kooi.match.core.enums.ResponseType;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Transactional
public class SubstitutePlayerUseCase implements UseCaseHandler<SubstitutePlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(SubstitutePlayerRequest command) {
        return matchDao.findById(command.matchId())
                .map(match -> handlePlayerEvents(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvents(Match match, SubstitutePlayerRequest command) {

        var substituteResponse = handleSubstituteEvent(match, command);
        var lineUpResponse = handleLineUpEvent(match, command);

        if (Stream.of(substituteResponse, lineUpResponse)
                .allMatch(response -> response.getResponseType() == ResponseType.PROCESSED_SUCCESSFULLY)) {
            return PlayerUseCaseResponse.successful();
        }

        return Stream.of(substituteResponse, lineUpResponse)
                .filter(response -> response.getResponseType() != ResponseType.PROCESSED_SUCCESSFULLY)
                .findFirst()
                .orElseGet(PlayerUseCaseResponse::fail);
    }

    private PlayerUseCaseResponse handleSubstituteEvent(Match match, SubstitutePlayerRequest command) {
        try {
            match.addPLayerEvent(createSubstituteEvent(command));
            matchDao.save(match);
            return PlayerUseCaseResponse.successful();
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        }
    }

    private PlayerUseCaseResponse handleLineUpEvent(Match match, SubstitutePlayerRequest command) {
        try {
            match.addPLayerEvent(createLineUpEvent(command));
            matchDao.save(match);
            return PlayerUseCaseResponse.successful();
        } catch (LineUpNotAllowedException e) {
            return PlayerUseCaseResponse.lineUpNotAllowed();
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
