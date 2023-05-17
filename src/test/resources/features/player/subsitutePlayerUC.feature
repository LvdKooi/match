Feature: the player substitute use case

  Background:
    Given a match between team1 and team2
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario: A player that is currently in the match gets substituted
    Given team1 has a player Messi
    And this match is currently taking place
    When player Ronaldo is substituted by player Messi at minute 0
    Then the event is added to the match
    And the match contains the following events for player Ronaldo: "LINED_UP, SUBSTITUTED"
    And the match contains the following events for player Messi: "LINED_UP"

  Scenario: A player that is not playing in the match gets substituted
    Given this match is currently taking place
    When player Ronaldo is substituted by a player that is not part of the match at minute 0
    Then event is not added to the match because the line up is not allowed
    And the match contains the following events for player Ronaldo: "LINED_UP"

  Scenario: A player that was already substituted gets substituted again
    Given team1 has a player Messi
    And this match is currently taking place
    And player Ronaldo is substituted by player Messi at minute 0
    When player Ronaldo is substituted by player Messi at minute 2
    Then event is not added to the match because the player is not active in the match
    And the match contains the following events for player Ronaldo: "LINED_UP, SUBSTITUTED"
    And the match contains the following events for player Messi: "LINED_UP"

  Scenario: A player that is currently in the match gets substituted by another player that is currently in the match
    Given team1 has a player Messi
    And player Messi is currently lined up
    And this match is currently taking place
    When player Ronaldo is substituted by player Messi at minute 2
    Then event is not added to the match because the line up is not allowed
    And the match contains the following events for player Ronaldo: "LINED_UP"
    And the match contains the following events for player Messi: "LINED_UP"

  Scenario: A player gets substituted while match has not started yet
    Given team1 has a player Messi
    When player Ronaldo is substituted by player Messi at minute 0
    Then event is not added to the match because the match is not active

  Scenario: A player gets substituted after match has taken place
    Given team1 has a player Messi
    And this match has already ended
    When player Ronaldo is substituted by player Messi at minute 0
    Then event is not added to the match because the match is not active