package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.player.DisciplinePlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.Set;

import static nl.kooi.match.enums.ResponseType.*;
import static nl.kooi.match.core.usecases.player.PlayerUseCaseHelper.getDefaultMatchForPlayerWithId;
import static nl.kooi.match.core.usecases.player.PlayerUseCaseHelper.getDefaultMatchForPlayerWithIdAndMatchStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(DisciplinePlayerUseCase.class)
class DisciplinePlayerUseCaseTest {

    @Autowired
    private DisciplinePlayerUseCase useCase;

    @MockBean
    private MatchDao matchDao;

    @Test
    void whenPlayerAlreadyHadAYellowCard_thenAlreadyHadAYellowCardIsReturned() {
        var match = getDefaultMatchForPlayerWithId(1L, Set.of(PlayerEvent.builder().matchId(1L).playerId(1L).minute(1).eventType(PlayerEventType.YELLOW_CARD).build()));

        when(matchDao.findById(1L))
                .thenReturn(Optional.of(match));

        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.YELLOW)).getResponseType())
                .isEqualTo(ALREADY_HAD_A_YELLOW_CARD);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsInGameAndReceivesYellowCard_thenSuccessfulIsReturned() {
        var match = getDefaultMatchForPlayerWithId(1L);

        when(matchDao.findById(1L))
                .thenReturn(Optional.of(match));

        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.YELLOW)).getResponseType())
                .isEqualTo(PROCESSED_SUCCESSFULLY);

        verify(matchDao, atMostOnce()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsInGameAndReceivesRedCard_thenSuccessfulIsReturned() {
        var match = getDefaultMatchForPlayerWithId(1L);

        when(matchDao.findById(1L))
                .thenReturn(Optional.of(match));

        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.RED)).getResponseType())
                .isEqualTo(PROCESSED_SUCCESSFULLY);

        verify(matchDao, atMostOnce()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsNotInGame_thenPlayerNotActiveInMatchIsReturned() {
        var match = getDefaultMatchForPlayerWithId(1L, Set.of(PlayerEvent.builder().matchId(1L).playerId(1L).minute(1).eventType(PlayerEventType.RED_CARD).build()));

        when(matchDao.findById(1L))
                .thenReturn(Optional.of(match));

        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.YELLOW)).getResponseType())
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenMatchIsUnknown_thenAMatchNotFoundStatusIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.empty());

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.YELLOW)).getResponseType())
                .isEqualTo(MATCH_NOT_FOUND);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void whenPlayerIsPartOfMatch_butMatchHasntStartedYet_theMatchNotActiveIsReturned() {
        var match = getDefaultMatchForPlayerWithIdAndMatchStatus(1L, MatchStatus.ANNOUNCED);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));
        when(matchDao.update(any(Match.class))).thenReturn(match);

        assertThat(useCase.handle(new DisciplinePlayerRequest(1L, 1L, 2, CardType.RED)).getResponseType())
                .isEqualTo(MATCH_NOT_ACTIVE);

        verify(matchDao, never()).update(any(Match.class));
    }
}