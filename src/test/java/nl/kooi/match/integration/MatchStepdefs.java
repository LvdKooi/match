package nl.kooi.match.integration;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.kooi.match.core.command.match.EndMatchUseCaseRequest;
import nl.kooi.match.core.command.match.StartMatchRequest;
import nl.kooi.match.core.command.player.*;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.usecases.match.EndMatchUseCase;
import nl.kooi.match.core.usecases.match.StartMatchUseCase;
import nl.kooi.match.core.usecases.player.DisciplinePlayerUseCase;
import nl.kooi.match.core.usecases.player.InjuredPlayerUseCase;
import nl.kooi.match.core.usecases.player.LineUpPlayerUseCase;
import nl.kooi.match.core.usecases.player.SubstitutePlayerUseCase;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.ResponseType;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import nl.kooi.match.infrastructure.mapper.Mapper;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.repository.MatchRepository;
import nl.kooi.match.infrastructure.repository.PlayerRepository;
import nl.kooi.match.infrastructure.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static nl.kooi.match.enums.InjuryType.INJURED;
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
    private InjuredPlayerUseCase injuredPlayerUseCase;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;


    @Autowired
    private StartMatchUseCase startMatchUseCase;

    @Autowired
    private EndMatchUseCase endMatchUseCase;

    @Autowired
    private SubstitutePlayerUseCase substitutePlayerUseCase;

    @Autowired
    private Mapper mapper;

    @After
    public void setUp() {
        playersByName.clear();
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

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
    @Transactional
    public void teamHasAPlayerNamed(int teamNumber, String playerName) {
        var team = teamRepository.findByName(teamNumber == 1 ? "team1" : "team2").get();

        var player = playerRepository.save(PlayerEntity.builder().name(playerName).build());

        playersByName.put(playerName, player);

        team.getPlayers().add(player);
        teamRepository.save(team);
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
        endMatchUseCase.handle(new EndMatchUseCaseRequest(match.id()));
    }


    @When("player {word} becomes injured in minute {int}")
    @Given("player {word} became injured in minute {int}")
    public void playerBecomesInjured(String name, int minute) {
        playerUseCaseResponse = injuredPlayerUseCase.handle(new InjuredPlayerRequest(playersByName.get(name).getId(), match.id(), minute, INJURED));
    }

    @When("a player that is not part of the match becomes injured at minute {int}")
    public void aPlayerThatIsNotPartOfTheMatchBecomesInjuredAtMinuteInt(int minute) {
        playerUseCaseResponse = injuredPlayerUseCase.handle(new InjuredPlayerRequest(10000L, match.id(), minute, INJURED));
    }

    @And("player {word} is currently lined up")
    public void playerRonaldoIsCurrentlyLinedUp(String playerName) {
        lineUpPlayerUseCase.handle(new LineUpPlayerRequest(playersByName.get(playerName).getId(), match.id(), 0));
    }

    @When("player {word} is substituted by player {word} at minute {int}")
    public void playerRonaldoIsSubstitutedByPlayerMessi(String player1, String player2, int minute) {
        playerUseCaseResponse = substitutePlayerUseCase.handle(new SubstitutePlayerRequest(playersByName.get(player1).getId(), match.id(), playersByName.get(player2).getId(), minute));
    }

    @When("player {word} is substituted by a player that is not part of the match at minute {int}")
    public void playerRonaldoIsSubstitutedByAPlayerThatIsNotPartOfTheMatchAtMinute(String playerName, int minute) {
        playerUseCaseResponse = substitutePlayerUseCase.handle(new SubstitutePlayerRequest(playersByName.get(playerName).getId(), match.id(), 10000L, minute));
    }
}
