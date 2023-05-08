package nl.kooi.match.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.kooi.match.core.command.match.StartMatchRequest;
import nl.kooi.match.core.command.player.DisciplinePlayerRequest;
import nl.kooi.match.core.command.player.LineUpPlayerRequest;
import nl.kooi.match.core.command.player.PlayerUseCaseResponse;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.usecases.match.StartMatchUseCase;
import nl.kooi.match.core.usecases.player.DisciplinePlayerUseCase;
import nl.kooi.match.core.usecases.player.LineUpPlayerUseCase;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.MatchStatus;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import nl.kooi.match.infrastructure.mapper.Mapper;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.repository.PlayerRepository;
import nl.kooi.match.infrastructure.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class MatchStepdefs extends CucumberBaseIT {

    private Match match;

    private TeamEntity team1;

    private TeamEntity team2;

    private Map<String, PlayerEntity> playersByName = new HashMap<>();

    private PlayerUseCaseResponse playerUseCaseResponse;

    @Autowired
    private DisciplinePlayerUseCase disciplinePlayerUseCase;

    @Autowired
    private LineUpPlayerUseCase lineUpPlayerUseCase;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private StartMatchUseCase startMatchUseCase;

    @Autowired
    private Mapper mapper;

    @Given("a match between team1 and team2")
    public void aMatchBetweenTeamAndTeam() {
        team1 = teamRepository.save(TeamEntity.builder().name("team1").build());
        team2 = teamRepository.save(TeamEntity.builder().name("team2").build());
        match = matchDao.createNewMatch(Instant.now(), mapper.map(team1), mapper.map(team2));
    }

    @And("this match is currently taking place")
    public void thisMatchIsCurrentlyTakingPlace() {
        startMatchUseCase.handle(new StartMatchRequest(match.id()));
    }

    @And("team{int} has a player {word}")
    public void teamHasAPlayerRonaldo(int teamNumber, String playerName) {
        playersByName.put(playerName, playerRepository.save(PlayerEntity.builder().team(teamNumber == 1 ? team1 : team2).build()));
        lineUpPlayerUseCase.handle(new LineUpPlayerRequest(playersByName.get(playerName).getId(), match.id(), 0));
    }

    @Then("the request is handled successfully")
    public void theRequestIsHandledSuccessfully() {
        assertThat(Optional.ofNullable(playerUseCaseResponse)
                .map(PlayerUseCaseResponse::getResponseType)
                .map(type -> type == ResponseType.PROCESSED_SUCCESSFULLY)
                .orElse(false)).isTrue();
    }

    @When("player {word} gets a {word} card at minute {int}")
    @Given("player {word} got a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String playerName, String cardType, int minute) {
        playerUseCaseResponse = disciplinePlayerUseCase.handle(new DisciplinePlayerRequest(playersByName.get(playerName).getId(), match.id(), minute, CardType.valueOf(cardType)));
    }

    @When("a player that is not part of the match gets a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String cardType, int minute) {
        var card = CardType.valueOf(cardType.toUpperCase());
        playerUseCaseResponse = disciplinePlayerUseCase.handle(new DisciplinePlayerRequest(10000L, match.id(), minute, card));
    }

    @Then("an error is shown stating: {string}")
    public void anErrorIsShownStatingPlayerIsCurrentlyNotInMatch(String msg) {
        assertThat(playerUseCaseResponse.getResponseType().name()).isEqualTo(msg);

    }

    @And("this match has already ended")
    public void thisMatchHasAlreadyEnded() {
        var finishedMatch = match.copyMatchWithStatus(MatchStatus.FINISHED);

        matchDao.update(finishedMatch);
    }
}
