Feature: the player substitute use case

  Background:
    Given a match between team1 and team2
    And this match is currently taking place
    And team1 has a player Ronaldo
    And player Ronaldo is currently lined up

  Scenario Outline: A player that is currently in the match gets a valid card
    When player Ronaldo gets a <card_type> card at minute 0
    Then the request is handled successfully
    Examples:
      | card_type |
      | YELLOW    |
      | RED       |

  Scenario: A player that is not playing in the match gets a yellow card
    When a player that is not part of the match gets a YELLOW card at minute 0
    Then an error is shown stating: "Event is not valid: PLAYER_NOT_ACTIVE_IN_MATCH"

  Scenario: A player that already had a yellow card gets another yellow card
    Given player Ronaldo got a YELLOW card at minute 0
    When player Ronaldo gets a YELLOW card at minute 1
    Then an error is shown stating: "Event is not valid: ALREADY_HAD_A_YELLOW_CARD"

  Scenario: A player that already had a yellow card gets a red card
    Given player Ronaldo got a YELLOW card at minute 0
    When player Ronaldo gets a RED card at minute 1
    Then the request is handled successfully

  Scenario: A player that already had taking red card gets another red card
    Given player Ronaldo got a RED card at minute 0
    When player Ronaldo gets a RED card at minute 1
    Then an error is shown stating: "Event is not valid: PLAYER_NOT_ACTIVE_IN_MATCH"

  Scenario: A player gets disciplined while match has not started yet
    When a player that is not part of the match gets a RED card at minute 0
    Then an error is shown stating: "Event is not valid: PLAYER_NOT_ACTIVE_IN_MATCH"

  Scenario: A player gets disciplined after match has taken place
    Given this match has already ended
    And team1 has a player Ronaldo
    Given player Ronaldo got a RED card at minute 0
    Then an error is shown stating: "Event is not valid: MATCH_NOT_ACTIVE"