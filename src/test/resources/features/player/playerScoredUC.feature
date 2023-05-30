Feature: the player scored use case

  Background:
    Given a match between team1 and team2
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario: A player that is currently in the match scores
    Given this match is currently taking place
    When player Ronaldo scores a goal at minute 0
    Then the event is added to the match
    And the match contains the following events for player Ronaldo: "LINED_UP, SCORED"

  Scenario: A player that is currently in the match scores twice
    Given this match is currently taking place
    When player Ronaldo scores a goal at minute 0
    And player Ronaldo scores a goal at minute 45
    And the match contains the following events for player Ronaldo: "LINED_UP, SCORED, SCORED"

  Scenario: A player that is not playing in the match scores
    Given this match is currently taking place
    When a player that is not part of the match scores a goal at minute 0
    Then event is not added to the match because the player is not active in the match

  Scenario: A player scores while match has not started yet
    When player Ronaldo scores a goal at minute 0
    Then event is not added to the match because the match is not active

  Scenario: A player scores after match has taken place
    Given this match has already ended
    And team1 has a player Ronaldo
    When player Ronaldo scores a goal at minute 0
    Then event is not added to the match because the match is not active