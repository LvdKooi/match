package nl.kooi.match.integration;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.kooi.match.api.dto.match.AnnounceMatchRequestDto;
import nl.kooi.match.api.dto.match.AnnounceMatchResponseDto;
import nl.kooi.match.api.dto.match.MatchEventRequestDto;
import nl.kooi.match.api.dto.player.*;
import nl.kooi.match.enums.CardType;
import nl.kooi.match.enums.MatchEventType;
import nl.kooi.match.enums.PlayerEventType;
import nl.kooi.match.infrastructure.entity.PlayerEntity;
import nl.kooi.match.infrastructure.entity.TeamEntity;
import nl.kooi.match.infrastructure.repository.MatchRepository;
import nl.kooi.match.infrastructure.repository.PlayerRepository;
import nl.kooi.match.infrastructure.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
    private static final String PLAYER_URL = "/matches/{matchId}/player-events";
    private static final String MATCH_URL = "/matches/{matchId}/match-events";
    private static final String MATCH_ANNOUNCEMENT_URL = "/matches/announce";

    private Long matchId;

    private final Map<String, PlayerEntity> playersByName = new HashMap<>();

    private ResponseEntity<Void> response;

    private ResponseEntity<ProblemDetail> error;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private String portNumber;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;


    @After
    public void cleanUp() {
        playersByName.clear();
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Given("a match between team1 and team2")
    public void aMatchBetweenTeamAndTeam() {
        var idTeam1 = teamRepository.save(TeamEntity.builder().name("team1").build()).getId();
        var idTeam2 = teamRepository.save(TeamEntity.builder().name("team2").build()).getId();

        makeMatchAnnouncementRequest(new AnnounceMatchRequestDto(Instant.now(), idTeam1, idTeam2));
    }

    @And("this match is currently taking place")
    public void thisMatchIsCurrentlyTakingPlace() {
        makeMatchUseCaseRequest(matchId, new MatchEventRequestDto(MatchEventType.STARTING));
    }

    @And("team{int} has a player {word}")
    @Transactional
    public void teamHasAPlayerNamed(int teamNumber, String playerName) {
        var team = teamRepository.findByName(teamNumber == 1 ? "team1" : "team2").orElseThrow();

        var player = playerRepository.save(PlayerEntity.builder().name(playerName).build());

        playersByName.put(playerName, player);

        team.getPlayers().add(player);
        teamRepository.save(team);
    }

    @Then("the request is handled successfully")
    public void theRequestIsHandledSuccessfully() {
        assertThat(Optional.ofNullable(response)
                .map(r -> r.getStatusCode().is2xxSuccessful())
                .orElse(false))
                .isTrue();
    }

    @When("player {word} gets a {word} card at minute {int}")
    @Given("player {word} got a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String playerName, String cardType, int minute) {
        var request = new DisciplineEventDto();
        request.setMinute(minute);
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setEventType(cardType.equalsIgnoreCase("YELLOW") ? PlayerEventType.YELLOW_CARD : PlayerEventType.RED_CARD);
        request.setCard(CardType.valueOf(cardType.toUpperCase()));

        makePlayerUseCaseRequest(matchId, request);

    }

    @When("a player that is not part of the match gets a {word} card at minute {int}")
    public void playerRonaldoGetsAYellowCardAtMinute(String cardType, int minute) {
        var request = new DisciplineEventDto();
        request.setMinute(minute);
        request.setPlayerId(10000L);
        request.setEventType(cardType.equalsIgnoreCase("YELLOW") ? PlayerEventType.YELLOW_CARD : PlayerEventType.RED_CARD);
        request.setCard(CardType.valueOf(cardType.toUpperCase()));

        makePlayerUseCaseRequest(matchId, request);
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
        makeMatchUseCaseRequest(matchId, new MatchEventRequestDto(MatchEventType.ENDING));
    }


    @When("player {word} becomes injured in minute {int}")
    @Given("player {word} became injured in minute {int}")
    public void playerBecomesInjured(String name, int minute) {
        var request = new InjuryEventDto();
        request.setPlayerId(playersByName.get(name).getId());
        request.setMinute(minute);
        request.setInjuryType(INJURED);
        request.setEventType(PlayerEventType.INJURED);

        makePlayerUseCaseRequest(matchId, request);
    }

    @When("a player that is not part of the match becomes injured at minute {int}")
    public void aPlayerThatIsNotPartOfTheMatchBecomesInjuredAtMinuteInt(int minute) {
        var request = new InjuryEventDto();
        request.setPlayerId(10000L);
        request.setMinute(minute);
        request.setInjuryType(INJURED);
        request.setEventType(PlayerEventType.INJURED);

        makePlayerUseCaseRequest(matchId, request);
    }

    @And("player {word} is currently lined up")
    public void playerRonaldoIsCurrentlyLinedUp(String playerName) {
        var request = new PlayerLineUpEventDto();
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setMinute(0);
        request.setEventType(PlayerEventType.LINED_UP);

        makePlayerUseCaseRequest(matchId, request);
    }

    @When("player {word} is substituted by player {word} at minute {int}")
    public void playerRonaldoIsSubstitutedByPlayerMessi(String player1, String player2, int minute) {
        var request = new SubstitutionEventDto();
        request.setPlayerId(playersByName.get(player1).getId());
        request.setSubstituteForPlayerId(playersByName.get(player2).getId());
        request.setMinute(minute);
        request.setEventType(PlayerEventType.SUBSTITUTED);

        makePlayerUseCaseRequest(matchId, request);
    }

    @When("player {word} is substituted by a player that is not part of the match at minute {int}")
    public void playerRonaldoIsSubstitutedByAPlayerThatIsNotPartOfTheMatchAtMinute(String playerName, int minute) {
        var request = new SubstitutionEventDto();
        request.setPlayerId(playersByName.get(playerName).getId());
        request.setSubstituteForPlayerId(10000L);
        request.setMinute(minute);
        request.setEventType(PlayerEventType.SUBSTITUTED);

        makePlayerUseCaseRequest(matchId, request);
    }

    private void makePlayerUseCaseRequest(Long matchId, PlayerEventRequestDto dto) {
        var url = "http://localhost:".concat(portNumber).concat(PLAYER_URL).replace("{matchId}", matchId.toString());
        response = restTemplate.postForEntity(url, dto, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            response = null;
            error = restTemplate.postForEntity(url, dto, ProblemDetail.class);
        }
    }

    private void makeMatchUseCaseRequest(Long matchId, MatchEventRequestDto dto) {
        var url = "http://localhost:".concat(portNumber).concat(MATCH_URL).replace("{matchId}", matchId.toString());
        restTemplate.postForEntity(url, dto, Void.class);
    }

    private void makeMatchAnnouncementRequest(AnnounceMatchRequestDto dto) {
        var url = "http://localhost:".concat(portNumber).concat(MATCH_ANNOUNCEMENT_URL);
        var response = restTemplate.postForEntity(url, dto, AnnounceMatchResponseDto.class);

        matchId = Optional.ofNullable(response).map(ResponseEntity::getBody)
                .map(AnnounceMatchResponseDto::matchId)
                .orElseThrow();
    }
}
