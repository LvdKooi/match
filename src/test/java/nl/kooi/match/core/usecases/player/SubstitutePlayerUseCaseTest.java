package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.player.SubstitutePlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Comparator;
import java.util.Optional;

import static nl.kooi.match.core.usecases.player.PlayerUseCaseHelper.getDefaultMatchForPlayerWithId;
import static nl.kooi.match.enums.ResponseType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(SubstitutePlayerUseCase.class)
class SubstitutePlayerUseCaseTest {

    @Autowired
    private SubstitutePlayerUseCase useCase;

    @MockBean
    private MatchDao matchDao;

    @Captor
    private ArgumentCaptor<Match> matchArgumentCaptor;

    @Test
    void whenMatchIsUnknown_thenAMatchNotFoundStatusIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.empty());

        assertThat(useCase.handle(new SubstitutePlayerRequest(1L, 1L, 2L, 10))
                .getResponseType())
                .isNotNull()
                .isEqualTo(MATCH_NOT_FOUND);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    void aValidSubstitutionTriggersTwoPlayerEvents() {
        var match = getDefaultMatchForPlayerWithId(1L);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));
        when(matchDao.isPlayerPartOfMatch(2L, 1L)).thenReturn(true);

        var response = useCase.handle(new SubstitutePlayerRequest(1L, 1L, 2L, 10));

        assertThat(response.getResponseType()).isEqualTo(PROCESSED_SUCCESSFULLY);

        verify(matchDao, times(1)).update(matchArgumentCaptor.capture());

        assertThat(matchArgumentCaptor.getValue().playerEvents()
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .allMatch(event -> (event.getPlayerId().equals(1L) && event.getEventType() == PlayerEventType.SUBSTITUTED) ||
                        (event.getPlayerId().equals(2L) && event.getEventType() == PlayerEventType.LINED_UP))
        ).isTrue();
    }

    @Test
    @DisplayName("Player that is substituted is not part of game.")
    void substitutionWithOneFailure_returnsSpecificResponseTypeWithFailure() {
        var match = getDefaultMatchForPlayerWithId(3L);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));
        when(matchDao.isPlayerPartOfMatch(2L, 1L)).thenReturn(true);

        var response = useCase.handle(new SubstitutePlayerRequest(1L, 1L, 2L, 10));

        assertThat(response.getResponseType()).isEqualTo(PLAYER_NOT_ACTIVE_IN_MATCH);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    @DisplayName("Player that is substituted is not part of game, and the player that will be lined up is already lined up; 2 failures return a PROCESSED_UNSUCCESSFULLY")
    void substitutionWithTwoFailure_returnsFail() {
        var match = getDefaultMatchForPlayerWithId(3L);

        match.addPLayerEvent(PlayerEvent.builder().playerId(2L).eventType(PlayerEventType.LINED_UP).minute(0).build());

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        var response = useCase.handle(new SubstitutePlayerRequest(1L, 1L, 2L, 10));

        assertThat(response.getResponseType()).isEqualTo(PROCESSED_UNSUCCESSFULLY);

        verify(matchDao, never()).update(any(Match.class));
    }

    @Test
    @DisplayName("Player that is substituted is part of game, but the player that will be lined up is already lined up; 2 failures return a PROCESSED_UNSUCCESSFULLY")
    void substitutionWithOneFailure_returnsSpecificResponseTypeWithFailure2() {
        var match = getDefaultMatchForPlayerWithId(1L);

        match.addPLayerEvent(PlayerEvent.builder().playerId(2L).eventType(PlayerEventType.LINED_UP).minute(0).build());

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        var response = useCase.handle(new SubstitutePlayerRequest(1L, 1L, 2L, 10));

        assertThat(response.getResponseType()).isEqualTo(LINE_UP_NOT_ALLOWED);

        verify(matchDao, never()).update(any(Match.class));
    }
}