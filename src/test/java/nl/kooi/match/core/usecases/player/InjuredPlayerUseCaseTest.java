package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.InjuredPlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.core.enums.InjuryType;
import nl.kooi.match.core.enums.PlayerEventType;
import nl.kooi.match.core.infrastructure.port.MatchDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static nl.kooi.match.core.enums.ResponseType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(InjuredPlayerUseCase.class)
class InjuredPlayerUseCaseTest {

    @Autowired
    private InjuredPlayerUseCase useCase;

    @MockBean
    private MatchDao matchDao;

    @Test
    void whenMatchIsUnknown_thenAMatchNotFoundStatusIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.empty());

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(MATCH_NOT_FOUND);

        verify(matchDao, never()).addPlayerEvent(anyLong(), any(PlayerEvent.class));
    }

    @Test
    void whenMatchHasNoPlayerEvents_thenPlayerNotActiveInMatchIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.of(new Match()));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).addPlayerEvent(anyLong(), any(PlayerEvent.class));
    }

    @Test
    void whenPlayerHasHadRedCard_thenPlayerNotActiveInMatchIsReturned() {

        var match = getDefaultMatchForPlayerWithId(1L);
        match.getPlayerEvents().add(PlayerEvent.builder().playerId(1L).eventType(PlayerEventType.RED_CARD).minute(1).build());

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).addPlayerEvent(anyLong(), any(PlayerEvent.class));
    }

    @Test
    void whenPlayerIsAlreadySubstituted_thenPlayerNotActiveInMatchIsReturned() {

        var match = getDefaultMatchForPlayerWithId(1L);
        match.getPlayerEvents().add(PlayerEvent.builder().playerId(1L).eventType(PlayerEventType.SUBSTITUTED).minute(1).build());

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType())
                .isNotNull()
                .isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).addPlayerEvent(anyLong(), any(PlayerEvent.class));
    }

    @Test
    void whenPlayerIsPartOfMatch_theSuccessfulIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.of(getDefaultMatchForPlayerWithId(1L)));
        when(matchDao.addPlayerEvent(anyLong(), any(PlayerEvent.class)))
                .thenReturn(getDefaultMatchForPlayerWithId(1L));

        assertThat(useCase.handle(getDefaultRequest()).getResponseType()).isNotNull().isEqualTo(SUCCESSFULLY_PROCESSED);

        verify(matchDao, times(1)).addPlayerEvent(anyLong(), any(PlayerEvent.class));
    }

    private static Match getDefaultMatchForPlayerWithId(Long id) {
        var match = new Match();
        match.setId(1L);
        match.getPlayerEvents()
                .add(PlayerEvent.builder().matchId(1L).minute(0).playerId(id).eventType(PlayerEventType.LINED_UP).build());

        return match;
    }

    private static InjuredPlayerRequest getDefaultRequest() {
        return InjuredPlayerRequest.builder()
                .playerId(1L)
                .matchId(1L)
                .minute(1)
                .injuryType(InjuryType.INJURED)
                .build();
    }
}