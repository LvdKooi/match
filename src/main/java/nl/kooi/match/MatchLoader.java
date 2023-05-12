//package nl.kooi.match;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import nl.kooi.match.core.command.match.AnnounceMatchRequest;
//import nl.kooi.match.core.command.match.StartMatchRequest;
//import nl.kooi.match.core.usecases.match.AnnounceMatchUseCase;
//import nl.kooi.match.core.usecases.match.StartMatchUseCase;
//import nl.kooi.match.infrastructure.entity.PlayerEntity;
//import nl.kooi.match.infrastructure.entity.TeamEntity;
//import nl.kooi.match.infrastructure.repository.PlayerRepository;
//import nl.kooi.match.infrastructure.repository.TeamRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//
//@Component
//@RequiredArgsConstructor
//public class MatchLoader implements CommandLineRunner {
//
//    private final AnnounceMatchUseCase announceMatchUseCase;
//    private final StartMatchUseCase startMatchUseCase;
//
//    private final TeamRepository teamRepository;
//
//    private final PlayerRepository playerRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        var team1 = teamRepository.save(TeamEntity.builder().name("AJAX").build());
//        var team2 = teamRepository.save(TeamEntity.builder().name("FEYENOORD").build());
//        var player1 = playerRepository.save(PlayerEntity.builder().build());
//        team1.getPlayers().add(player1);
//        teamRepository.save(team1);
//
//
//        var response = announceMatchUseCase.handle(new AnnounceMatchRequest(Instant.now().plus(1, ChronoUnit.MINUTES), team1.getId(), team2.getId()));
//        startMatchUseCase.handle(new StartMatchRequest(response.getMatchId()));
//    }
//}
