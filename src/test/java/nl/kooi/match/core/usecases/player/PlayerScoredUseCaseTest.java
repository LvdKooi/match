package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.player.PlayerScoredRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.Set;

import static nl.kooi.match.core.usecases.player.PlayerUseCaseHelper.getDefaultMatchForPlayerWithId;
import static nl.kooi.match.core.usecases.player.PlayerUseCaseHelper.getDefaultMatchForPlayerWithIdAndMatchStatus;
import static nl.kooi.match.enums.ResponseType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringJUnitConfig(PlayerScoredUseCase.class)
class PlayerScoredUseCaseTest {

    @Autowired
    private PlayerScoredUseCase useCase;

    @MockBean
    private MatchDao matchDao;

    @Test
    void playerThatIsLinedUpScores() {
        var match = PlayerUseCaseHelper.getDefaultMatchForPlayerWithId(1L);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));
        when(matchDao.update(any(Match.class))).thenReturn(match);

        var response = useCase.handle(getDefaultRequest());

        assertThat(response).isNotNull().matches(resp -> resp.getResponseType() == PROCESSED_SUCCESSFULLY, "Is successful");

        verify(matchDao, times(1)).update(any(Match.class));
    }

    @Test
    void whenMatchIsUnknown_thenAMatchNotFoundStatusIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.empty());

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(MATCH_NOT_FOUND);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenMatchHasNoPlayerEvents_thenPlayerNotActiveInMatchIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.of(Match.createMatch(MatchStatus.STARTED, null, null)));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenPlayerHasHadRedCard_thenPlayerNotActiveInMatchIsReturned() {

        var match = getDefaultMatchForPlayerWithId(1L, Set.of(PlayerEvent.builder().playerId(1L).eventType(PlayerEventType.RED_CARD).minute(1).build()));

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsAlreadySubstituted_thenPlayerNotActiveInMatchIsReturned() {

        var match = getDefaultMatchForPlayerWithId(1L,
                Set.of(PlayerEvent.builder().playerId(1L).eventType(PlayerEventType.SUBSTITUTED).minute(1).build()));

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsPartOfMatch_butMatchHasntStartedYet_theMatchNotActiveIsReturned() {
        var match = getDefaultMatchForPlayerWithIdAndMatchStatus(1L, MatchStatus.ANNOUNCED);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));
        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(getDefaultRequest()).getResponseType()).isNotNull().isEqualTo(MATCH_NOT_ACTIVE);

        verify(matchDao, never()).update(any(Match.class));
    }

    private static PlayerScoredRequest getDefaultRequest() {
        return new PlayerScoredRequest(1L, 1L, 1);
    }
}