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
import nl.kooi.match.exception.LineUpNotAllowedException;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
@Validated
public class SubstitutePlayerUseCase implements UseCaseHandler<SubstitutePlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid SubstitutePlayerRequest command) {
        return matchDao.findById(command.matchId())
                .map(match -> handlePlayerEvents(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvents(Match match, SubstitutePlayerRequest command) {
        try {
            return Optional.ofNullable(match)
                    .map(m -> m.addPlayerEvent(createSubstituteEvent(command)))
                    .map(m -> m.addPlayerEvent(createLineUpEvent(command)))
                    .map(matchDao::update)
                    .map(success -> PlayerUseCaseResponse.successful())
                    .orElseGet(PlayerUseCaseResponse::fail);
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        } catch (MatchStatusException e) {
            return PlayerUseCaseResponse.matchIsNotActive();
        } catch (LineUpNotAllowedException e) {
            return PlayerUseCaseResponse.lineUpNotAllowed();
        }
    }

    private void verifyIfPlayerPartOfTeams(SubstitutePlayerRequest command) {
        if (!matchDao.isPlayerPartOfMatch(command.substituteForPlayerId(), command.matchId())) {
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

    private PlayerEvent createLineUpEvent(SubstitutePlayerRequest command) {
        verifyIfPlayerPartOfTeams(command);

        return PlayerEvent.builder()
                .playerId(command.substituteForPlayerId())
                .eventType(PlayerEventType.LINED_UP)
                .matchId(command.matchId())
                .minute(command.minute())
                .build();
    }
}
