package nl.kooi.match.core.usecases.player;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.kooi.match.core.command.player.DisciplinePlayerRequest;
import nl.kooi.match.core.command.player.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.exception.MatchStatusException;
import nl.kooi.match.exception.PlayerNotActiveInMatchException;
import nl.kooi.match.exception.YellowCardException;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.core.usecases.UseCaseHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@Transactional
@Validated
public class DisciplinePlayerUseCase implements UseCaseHandler<DisciplinePlayerRequest, PlayerUseCaseResponse> {

    private final MatchDao matchDao;

    @Override
    public PlayerUseCaseResponse handle(@Valid DisciplinePlayerRequest command) {
        return matchDao.findById(command.matchId())
                .map(match -> handlePlayerEvent(match, command))
                .orElseGet(PlayerUseCaseResponse::matchNotFound);
    }

    private PlayerUseCaseResponse handlePlayerEvent(Match match, DisciplinePlayerRequest command) {
        try {
            match.addPLayerEvent(createPlayerEvent(command));
            matchDao.update(match);
            return PlayerUseCaseResponse.successful();
        } catch (YellowCardException e) {
            return PlayerUseCaseResponse.alreadyHadAYellowCard();
        } catch (PlayerNotActiveInMatchException e) {
            return PlayerUseCaseResponse.playerIsNotActiveInMatch();
        } catch (MatchStatusException e) {
            return PlayerUseCaseResponse.matchIsNotActive();
        }
    }

    private static PlayerEvent createPlayerEvent(DisciplinePlayerRequest command) {
        return PlayerEvent.builder()
                .playerId(command.playerId())
                .minute(command.minute())
                .eventType(command.card() == CardType.YELLOW ? PlayerEventType.YELLOW_CARD : PlayerEventType.RED_CARD)
                .matchId(command.matchId())
                .build();
    }
}
