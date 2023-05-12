Feature: the player substitute use case

  Background:
    Given a match between team1 and team2
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario: A player that is currently in the match gets substituted
    Given team1 has a player Messi
    And this match is currently taking place
    When player Ronaldo is substituted by player Messi at minute 0
    Then the request is handled successfully
    And the match contains 2 events for player Ronaldo
    And the match contains 1 event for player Messi

  Scenario: A player that is not playing in the match gets substituted
    Given this match is currently taking place
    When player Ronaldo is substituted by a player that is not part of the match at minute 0
    Then an error is shown stating: "Event is not valid: LINE_UP_NOT_ALLOWED"
    And the match contains 1 events for player Ronaldo

  Scenario: A player that was already substituted gets substituted again
    Given team1 has a player Messi
    And this match is currently taking place
    And player Ronaldo is substituted by player Messi at minute 0
    When player Ronaldo is substituted by player Messi at minute 2
    Then an error is shown stating: "Event is not valid: PROCESSED_UNSUCCESSFULLY"
    And the match contains 2 events for player Ronaldo
    And the match contains 1 event for player Messi

  Scenario: A player that is currently in the match gets substituted by another player that is currently in the match
    Given team1 has a player Messi
    And player Messi is currently lined up
    And this match is currently taking place
    When player Ronaldo is substituted by player Messi at minute 2
    Then an error is shown stating: "Event is not valid: LINE_UP_NOT_ALLOWED"
    And the match contains 1 event for player Ronaldo
    And the match contains 1 event for player Messi

  Scenario: A player gets substituted while match has not started yet
    Given team1 has a player Messi
    When player Ronaldo is substituted by player Messi at minute 0
    Then an error is shown stating: "Event is not valid: MATCH_NOT_ACTIVE"

  Scenario: A player gets substituted after match has taken place
    Given team1 has a player Messi
    And this match has already ended
    When player Ronaldo is substituted by player Messi at minute 0
    Then an error is shown stating: "Event is not valid: MATCH_NOT_ACTIVE"