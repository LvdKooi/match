Feature: the player is injured use case

  Background:
    Given a match between team1 and team2
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario: A player that is currently in the match gets injured
    Given this match is currently taking place
    When player Ronaldo becomes injured in minute 0
    Then the event is added to the match
    And the match contains the following events for player Ronaldo: "LINED_UP, INJURED"

  Scenario: A player that is not playing in the match gets injured
    Given this match is currently taking place
    When a player that is not part of the match becomes injured at minute 0
    Then event is not added to the match because the player is not active in the match

  Scenario: A player that is was already injured before gets injured again
    Given this match is currently taking place
    And player Ronaldo became injured in minute 0
    When player Ronaldo becomes injured in minute 1
    Then the event is added to the match
    And the match contains the following events for player Ronaldo: "LINED_UP, INJURED, INJURED"

  Scenario: A player becomes injured while match has not started yet
    Given team1 has a player Ronaldo
    When player Ronaldo becomes injured in minute 1
    Then event is not added to the match because the match is not active

  Scenario: A player becomes injured after match has taken place
    Given this match has already ended
    And team1 has a player Ronaldo
    When player Ronaldo becomes injured in minute 1
    Then event is not added to the match because the match is not active