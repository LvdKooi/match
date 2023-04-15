package nl.kooi.match.core.usecases.player;

import nl.kooi.match.core.command.LineUpPlayerRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.domain.PlayerEvent;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.port.MatchDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

@SpringJUnitConfig(LineUpPlayerUseCase.class)
class LineUpPlayerUseCaseTest {

    @Autowired
    private LineUpPlayerUseCase useCase;

    @MockBean
    private MatchDao matchDao;

    @Test
    void whenMatchIsUnknown_thenAMatchNotFoundStatusIsReturned() {
        when(matchDao.findById(1L)).thenReturn(Optional.empty());

        assertThat(useCase.handle(new LineUpPlayerRequest(2L, 1L, 0)).getResponseType())
                .isNotNull()
                .isEqualTo(MATCH_NOT_FOUND);

        verify(matchDao, never()).update(any(Match.class));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"FINISHED", "POSTPONED", "CANCELLED"})
    void whenMatchStatusIsNotAllowedForLineUp_thenLineUpNotAllowedIsReturned(MatchStatus status) {
        when(matchDao.findById(1L)).thenReturn(Optional.of(getDefaultMatchForPlayerWithIdAndMatchStatus(1L, status)));

        assertThat(useCase.handle(new LineUpPlayerRequest(2L, 1L, 0)).getResponseType())
                .isNotNull()
                .isEqualTo(LINE_UP_NOT_ALLOWED);

        verify(matchDao, never()).update(any(Match.class));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"ANNOUNCED", "STARTED"})
    void whenMatchStatusAllowsForLineUp_AndPlayerIsNotLinedUpYet_thenSuccessfulIsReturned(MatchStatus status) {
        when(matchDao.findById(1L)).thenReturn(Optional.of(getDefaultMatchForPlayerWithIdAndMatchStatus(1L, status)));

        assertThat(useCase.handle(new LineUpPlayerRequest(2L, 1L, 0)).getResponseType())
                .isNotNull()
                .isEqualTo(PROCESSED_SUCCESSFULLY);

        verify(matchDao, atMostOnce()).update(any(Match.class));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"ANNOUNCED", "STARTED"})
    void whenMatchStatusAllowsForLineUp_AndPlayerIsLinedUpButInjured_thenSuccessfulIsReturned(MatchStatus status) {
        var additionalEvents = Set.of(PlayerEvent.builder().playerId(2L).minute(1).matchId(1L).eventType(PlayerEventType.INJURED).build());

        var match = getDefaultMatchForPlayerWithId(2L, additionalEvents, status);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));


        assertThat(useCase.handle(new LineUpPlayerRequest(2L, 1L, 3)).getResponseType())
                .isNotNull()
                .isEqualTo(PROCESSED_SUCCESSFULLY);

        verify(matchDao, atMostOnce()).update(any(Match.class));
    }

    @ParameterizedTest
    @EnumSource(value = MatchStatus.class, names = {"ANNOUNCED", "STARTED"})
    void whenMatchStatusAllowsForLineUp_ButPlayerHadRedCard_thenSuccessfulIsReturned(MatchStatus status) {
        var additionalEvents = Set.of(PlayerEvent.builder().playerId(2L).minute(1).matchId(1L).eventType(PlayerEventType.RED_CARD).build());

        var match = getDefaultMatchForPlayerWithId(2L, additionalEvents, status);

        when(matchDao.findById(1L)).thenReturn(Optional.of(match));

        assertThat(useCase.handle(new LineUpPlayerRequest(2L, 1L, 3)).getResponseType())
                .isNotNull()
                .isEqualTo(LINE_UP_NOT_ALLOWED);

        verify(matchDao, never()).update(any(Match.class));
    }
}