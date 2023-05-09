package nl.kooi.match.integration;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.kooi.match.api.dto.*;
import nl.kooi.match.core.command.match.EndMatchUseCaseRequest;
import nl.kooi.match.core.command.match.StartMatchRequest;
import nl.kooi.match.core.domain.Match;
import nl.kooi.match.core.usecases.match.EndMatchUseCase;
import nl.kooi.match.core.usecases.match.StartMatchUseCase;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import nl.kooi.match.infrastructure.mapper.Mapper;
import nl.kooi.match.infrastructure.port.MatchDao;
import nl.kooi.match.infrastructure.repository.MatchRepository;
import nl.kooi.match.infrastructure.repository.PlayerRepository;
import nl.kooi.match.infrastructure.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private String portNumber;

    private static final String PLAYER_URL = "/matches/{matchId}/player-events";
    @Autowired
    private MatchDao matchDao;

    private ResponseEntity<Void> response;

    private ResponseEntity<ProblemDetail> error;

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
        assertThat(Optional.ofNullable(response)
                .map(r -> r.getStatusCode().equals(HttpStatusCode.valueOf(201)))
                .orElse(false)).isTrue();
    }

    @When("player {word} gets a {word} card at minute {int}")
    @Given("player {word} got a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String playerName, String cardType, int minute) {
        var request = new DisciplineEventDto();
        request.setMinute(minute);
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setEventType(cardType.equalsIgnoreCase("YELLOW") ? PlayerEventType.YELLOW_CARD : PlayerEventType.RED_CARD);
        request.setCard(CardType.valueOf(cardType.toUpperCase()));

        makePlayerUseCaseRequest(match.id(), request);

    }

    @When("a player that is not part of the match gets a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String cardType, int minute) {
        var request = new DisciplineEventDto();
        request.setMinute(minute);
        request.setPlayerId(10000L);
        request.setEventType(cardType.equalsIgnoreCase("YELLOW") ? PlayerEventType.YELLOW_CARD : PlayerEventType.RED_CARD);
        request.setCard(CardType.valueOf(cardType.toUpperCase()));

        makePlayerUseCaseRequest(match.id(), request);
    }

    @Then("an error is shown stating: {string}")
    public void anErrorIsShownStatingPlayerIsCurrentlyNotInMatch(String msg) {
        assertThat(Optional.ofNullable(error)
                .map(ResponseEntity::getBody)
                .map(ProblemDetail::getDetail)
                .orElseGet(String::new)).isEqualTo(msg);
    }

    @And("this match has already ended")
    public void thisMatchHasAlreadyEnded() {
        endMatchUseCase.handle(new EndMatchUseCaseRequest(match.id()));
    }


    @When("player {word} becomes injured in minute {int}")
    @Given("player {word} became injured in minute {int}")
    public void playerBecomesInjured(String name, int minute) {
        var request = new InjuryEventDto();
        request.setPlayerId(playersByName.get(name).getId());
        request.setMinute(minute);
        request.setInjuryType(INJURED);
        request.setEventType(PlayerEventType.INJURED);

        makePlayerUseCaseRequest(match.id(), request);
    }

    @When("a player that is not part of the match becomes injured at minute {int}")
    public void aPlayerThatIsNotPartOfTheMatchBecomesInjuredAtMinuteInt(int minute) {
        var request = new InjuryEventDto();
        request.setPlayerId(10000L);
        request.setMinute(minute);
        request.setInjuryType(INJURED);
        request.setEventType(PlayerEventType.INJURED);

        makePlayerUseCaseRequest(match.id(), request);
    }

    @And("player {word} is currently lined up")
    public void playerRonaldoIsCurrentlyLinedUp(String playerName) {
        var request = new PlayerLineUpEventDto();
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setMinute(0);
        request.setEventType(PlayerEventType.LINED_UP);

        makePlayerUseCaseRequest(match.id(), request);
    }

    @When("player {word} is substituted by player {word} at minute {int}")
    public void playerRonaldoIsSubstitutedByPlayerMessi(String player1, String player2, int minute) {
        var request = new SubstitutionEventDto();
        request.setPlayerId(playersByName.get(player1).getId());
        request.setSubstituteForPlayerId(playersByName.get(player2).getId());
        request.setMinute(minute);
        request.setEventType(PlayerEventType.SUBSTITUTED);

        makePlayerUseCaseRequest(match.id(), request);
    }

    @When("player {word} is substituted by a player that is not part of the match at minute {int}")
    public void playerRonaldoIsSubstitutedByAPlayerThatIsNotPartOfTheMatchAtMinute(String playerName, int minute) {
        var request = new SubstitutionEventDto();
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setSubstituteForPlayerId(10000L);
        request.setMinute(minute);
        request.setEventType(PlayerEventType.SUBSTITUTED);

        makePlayerUseCaseRequest(match.id(), request);
    }

    private void makePlayerUseCaseRequest(Long matchId, PlayerEventRequestDto dto) {
        var url = "http://localhost:".concat(portNumber).concat(PLAYER_URL).replace("{matchId}", matchId.toString());
        response = restTemplate.postForEntity(url, dto, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            response = null;
            error = restTemplate.postForEntity(url, dto, ProblemDetail.class);
        }
    }
}
