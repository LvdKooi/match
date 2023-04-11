package nl.kooi.match.core.infrastructure.mapper;

import nl.kooi.match.core.infrastructure.entity.MatchEntity;
import nl.kooi.match.core.infrastructure.entity.TeamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static nl.kooi.match.core.enums.MatchStatus.STARTED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(MapperImpl.class)
class MapperTest {

    @Autowired
    private Mapper mapper;

    @Test
    void mapMatchEntityToMatch() {
        var entity = MatchEntity.builder()
                .id(1L)
                .matchStatus(STARTED)
                .team1(TeamEntity.builder().name("Ajax").build())
                .team2(TeamEntity.builder().name("Feyenoord").build())
                .build();

        var match = mapper.map(entity);

        assertThat(match.matchName()).isEqualTo("Ajax - Feyenoord");
        assertThat(match.matchStatus()).isEqualTo(STARTED);
    }
}